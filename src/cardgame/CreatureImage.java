
package cardgame;

import java.util.ArrayList;
import java.util.List;

/*
    Questa classe astratta contiene l'implementazione fondamentale di tutti i metodi necessari per l'implementazione
    del design pattern decorator. Noi abbiamo adottato la soluzione dove i decorator nella lista sono sempre
    compresi fra un testa che fa da rappresentante per la creatura e la creatura stessa. Questa classe appunto
    implementa i metodi necessari al rappresentante, ovviamente implementa l'interfaccia Creature.
*/

public class CreatureImage implements Creature{
    
    private Player owner;
    private Creature pointer; //elemento successivo della lista, può essere la creatura o un decorator
    
    public Player getOwner(){
        return owner;
    }
    
    //Il costruttore si occupa di settare i due attributi fondamentali
    public CreatureImage(Player owner, Creature pointer){
        this.owner = owner;
        this.pointer = pointer;
    }
    
    public void setPointer(Creature np){
        this.pointer = np;
    }
    
    public Creature getPointer(){
        return this.pointer;
    }
    
    @Override
    public int getPower() {
        return pointer.getPower();
    }

    @Override
    public int getToughness() {
        return pointer.getToughness();
    }

    @Override
    public List<Effect> effects() {
        return pointer.effects();
    }

    @Override
    public List<Effect> avaliableEffects() {
        return pointer.avaliableEffects();
    }

    @Override
    public String name() {
        return pointer.name();
    }

    @Override
    public boolean tap() {
        return pointer.tap();
    }

    @Override
    public boolean untap() {
        return pointer.untap();
    }

    @Override
    public boolean isTapped() {
        return pointer.isTapped();
    }

    @Override
    public void attack() {
        pointer.attack();
    }

    @Override
    public void defend(Creature c) {
        pointer.defend(c);
    }

    @Override
    public boolean inflictDamage(int dmg) {
        boolean b;
        AbstractCreature cr;
        if(pointer instanceof AbstractCreature){
            cr = (AbstractCreature) pointer;
            b = cr.inflictRealDamage(dmg);
        }
        else b = pointer.inflictDamage(dmg);
        if(b)
            if(pointer instanceof AbstractDecorator){
                AbstractDecorator p = (AbstractDecorator)pointer;
                p.deregisterDecorator();
            }
        return b;
    }

    @Override
    public void resetDamage() {
        pointer.resetDamage();
    }

    @Override
    public void addTarget(Object target) {
        pointer.addTarget(target);
    }

    @Override
    public ArrayList getTarget() {
        return pointer.getTarget();
    }

    @Override
    public void clearTarget() {
        pointer.clearTarget();
    }

    @Override
    public boolean getDef() {
        return pointer.getDef();
    }

    @Override
    public boolean getAtt() {
        return pointer.getAtt();
    }

    @Override
    public void insert() {
        pointer.insert();
    }

    @Override
    public boolean remove() {
        if(pointer.remove()){
            CardGame.instance.getTriggers().trigger(Triggers.EXIT_CREATURE_FILTER,this);
            return true;
        }
        else return false;
    }
    
    /*
        Questo metodo è stato realizzato per consentire al temine dell'applicazione di un decorator se la somma
        degli effetti applicati comporta la morte della creatura o meno.
    */
    public void checker(){
        if(pointer.getToughness() <= 0){
            owner.destroy(this);
            System.out.println(this.owner.name() + "'s creature: " + this.name() + " is dead.");
        }
    }
    
    @Override
    public ArrayList<String> getDTypes(){
        return pointer.getDTypes();
    }
}
