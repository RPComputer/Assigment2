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
    
    @Override
    public boolean getAtt(){return att;}
    @Override
    public boolean getDef(){return def;}
    
        protected AbstractCreature(Player owner) { this.owner=owner; }
        
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
    @Override
        public void attack() {
            int attackLeft = this.getPower();
            if(target.size() == 1 && target.get(1) instanceof Player){
                Player p = (Player) target.get(1);
                p.inflictDamage(attackLeft);
            }
            else{
                Creature c;
                int takeDamage, takenDamage = 0;
                for(Object t : target){
                    c = (Creature) t;
                    takeDamage = c.getToughness();
                    takenDamage += c.getPower();
                    if(attackLeft > 0)
                        c.inflictDamage(attackLeft);
                    attackLeft -= takeDamage;
                }
                this.inflictDamage(takenDamage);
            }
            target.clear();
        } // to do in assignment 2
    @Override
        public void defend(Creature c) {
            ArrayList t = c.getTarget();
            if(t.get(1) instanceof Player)
                c.clearTarget();
            c.addTarget(this);
        } // to do in assignment 2
    @Override
        public void inflictDamage(int dmg) { 
            damageLeft -= dmg; 
            if (damageLeft<=0)
                owner.destroy(this);        
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
            return null;
        }
}
