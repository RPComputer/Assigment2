/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardgame;

/**
 *
 * @author atorsell
 */
public abstract class AbstractEnchantment implements Enchantment {
    protected Player owner;        
    protected AbstractEnchantment(Player owner) { this.owner=owner; }
    
    @Override
        public Player getOwner() {
            return owner;
        }
    
    @Override
        public void insert() {
            CardGame.instance.getTriggers().trigger(Triggers.ENTER_ENCHANTMENT_FILTER,this);
        }
    
    @Override
        public boolean remove() {
            CardGame.instance.getTriggers().trigger(Triggers.EXIT_ENCHANTMENT_FILTER,this);
            return true;
        }
        
    @Override
        public String toString() {
            return name() + " (Enchantment)";
        }
}
