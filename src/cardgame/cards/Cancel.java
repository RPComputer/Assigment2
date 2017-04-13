/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardgame.cards;

import java.util.ArrayList;
import cardgame.Card;
import cardgame.Effect;
import cardgame.Enchantment;
import cardgame.Player;
import cardgame.AbstractEnchantmentCardEffect;
import cardgame.AbstractEnchantment;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.TriggerAction;
import cardgame.Triggers;
import java.util.Scanner;

/**
 *
 * @author atorsell
 */
public class Cancel implements Card {
    private class CancelEffect extends AbstractEnchantmentCardEffect {
    
        
        public CancelEffect(Player p, Card c) { super(p,c); }
        @Override
        protected Enchantment createEnchantment() { return new CancelEnchantment(owner); }
        
        @Override
        public void resolve () {
            ArrayList <Effect> l = new ArrayList<>();
            int i=0;
            
            for (Effect c: CardGame.instance.getStack()){
                
                l.get(i)=c;
                i++;
                l.iterator().next();                                    
            }    
            
            Scanner s = new Scanner (System.in);
            i=s.nextInt();
            CardGame.instance.getStack().remove(l.get(i));
        

        }
    }
   

    @Override
    public Effect getEffect(Player p) { return new CancelEffect(p,this); }
    
    private class CancelEnchantment extends AbstractEnchantment {
        public CancelEnchantment(Player owner) {
            super(owner);
        }
        
        private final TriggerAction GreetingAction = new TriggerAction() {
                @Override
                public void execute(Object args) {
                    if (args != null  && args instanceof Creature) {
                        Creature c = (Creature)args;
                        System.out.println( name() + " says: \"Welcome " + c.name() +"!\"" );
                    }
                }
            };
        
        @Override
        public void insert() {
            super.insert();
            CardGame.instance.getTriggers().register(Triggers.ENTER_CREATURE_FILTER, GreetingAction);
        }
        
        @Override
        public void remove() {
            super.remove();
            CardGame.instance.getTriggers().deregister(GreetingAction);
        }
        
        @Override
        public String name() { return "Boiling Earth"; }
    }
    
    
    @Override
    public String name() { return "Cancel"; }
    @Override
    public String type() { return "Enchantment"; }
    @Override
    public String ruleText() { return "Counter target spell " + name() + " welcomes it"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return true; }
    

}
