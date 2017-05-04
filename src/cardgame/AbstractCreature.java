/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardgame;

import java.util.ArrayList;

/**
 *
 * @author atorsell
 */
public abstract class AbstractCreature implements Creature {
    
    protected Player owner;
    protected boolean isTapped=false;
    protected int damageLeft = getToughness();
    public ArrayList target = new ArrayList();
    public boolean att = true;
    public boolean def = true;
    private Creature head;
    
    //Metodo per ottenere il posessore della creatura, utile quando si ha solo la creatura a disposizione
    public Player getOwner(){return owner;}
    
    
    //Questi metodi servono per conoscere se una creatura può per sua definizione attaccare e/o difendere
    @Override
    public boolean getAtt(){return att;}
    @Override
    public boolean getDef(){return def;}
    
        protected AbstractCreature(Player owner) { this.owner=owner;}
        
    //Poichè per implementare il pattern decorator, la creatura ha bisogno di poter conoscere il puntatore alla suo rappresentante
    public void setHead(Creature h){this.head = h;}
    
    @Override
        public boolean tap() { 
            if (isTapped) {
                System.out.println("creature " + name() + " already tapped");
                return false;
            }
            
            System.out.println("tapping creature " + name());
            isTapped=true; 
            return true; 
        }
        
    @Override
        public boolean untap() { 
            if (!isTapped) {
                System.out.println("creature " + name() + " not tapped");
                return false;
            }
            
            System.out.println("untapping creature " + name());
            isTapped=false; 
            return true; 
        }
        
    @Override
        public boolean isTapped() { return isTapped; }
        
    /*
        Il metodo attack delle creature consiste nel calcolo del danno da infliggere sia agli attaccati sia a se stessi
        funziona tramite lo scorrimento della lista dei target che per default viene inizializzata dalla phase combat
        al giocatore avversario. Successivamente al calcolo esso viene inflitto richiamando i metodi appropriati e
        notificando con un output il il risultato.
    */
    @Override
        public void attack() {
            int attackLeft = this.head.getPower();
            if(target.size() == 1 && target.get(0) instanceof Player){
                Player p = (Player) target.get(0);
                p.inflictDamage(attackLeft);
                System.out.println("Damage inflicted ->" + attackLeft + " to: " + p.name()+".");
            }
            else{
                Creature c;
                int takeDamage, takenDamage = 0;
                for(Object t : target){
                    c = (Creature) t;
                    takeDamage = c.getToughness();
                    takenDamage += c.getPower();
                    if(attackLeft > 0){
                        int damage;
                        if(attackLeft > takeDamage){
                            damage = takeDamage;
                            takeDamage = 0;
                        }
                        else{
                            damage = attackLeft;
                            takeDamage = takeDamage-attackLeft;
                        }
                        System.out.println("Damage inflicted ->" + damage + " to: " + c.name()+" -> ("+c.getPower()+"/"+ takeDamage + ").");
                        c.inflictDamage(attackLeft);
                    }
                    attackLeft -= takeDamage;
                }
                System.out.println("Damage inflicted ->" + takenDamage + " to: " + this.name()+" -> ("+this.getPower()+"/"+(this.getToughness()-takenDamage) + ").");
                this.inflictDamage(takenDamage);
            }
            target.clear();
        }
    
    /*
        Il metodo defend è complementare al metodo attack, tuttavia si occupa solo di effettuare un'integrazione:
        si assicura di porre la creatura che deve difendere nella lista dei target dell'attaccante e se in quella lista
        è ancora presente il giocatore che la creatura deve difendere esso viene rimosso.
    */
    @Override
        public void defend(Creature c) {
            ArrayList t = c.getTarget();
            if(t.get(0) instanceof Player)
                c.clearTarget();
            c.addTarget(this);
        } // to do in assignment 2
    
        /*
            Quando un decorator o il rappresentante della creatura devono infliggere realmente il danno alla creatura viene
            chiamato questo metodo
        */
        public boolean inflictRealDamage(int dmg) { 
            damageLeft -= dmg; 
            if (damageLeft<=0){
                owner.destroy(this.head);
                System.out.println(this.owner.name() + "'s creature: " + this.name() + " is dead.");
                return true;
            }
            return false;
        }
    
    //Per consentire il lavoro dei decorator, il danno viene reindirizzato
    @Override
    public boolean inflictDamage(int dmg){
        return this.head.inflictDamage(dmg);
    } 
    @Override
        public void resetDamage() { damageLeft = getToughness(); }
    
    @Override
        public void insert() {
            CardGame.instance.getTriggers().trigger(Triggers.ENTER_CREATURE_FILTER,this);
        }
    
    @Override
        public boolean remove() {
            return true;
        }
    
    @Override
        public String toString() {
            return name() + " (Creature)";
        }
        
        /*
            Funzioni di servizio delle liste legate alla creatura: target e insieme dei tipi di effetti applicati
            alla creatura.
        */
    @Override
        public void addTarget (Object t){
            this.target.add(t);
        }
        
    @Override
        public ArrayList getTarget(){
            return this.target;
        }
        
    @Override
        public void clearTarget(){
            this.target.clear();
        }
        
    @Override
        public ArrayList<String> getDTypes(){
            return new ArrayList();
        }
}
