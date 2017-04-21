package cardgame.cards;

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

public class AetherBarrier implements Card {
    private class AetherBarrierEffect extends AbstractEnchantmentCardEffect {
        public AetherBarrierEffect(Player p, Card c) { super(p,c); }
        @Override
        protected Enchantment createEnchantment() { return new AetherBarrierEnchantment(owner); }
    }
        
    private class AetherBarrierEnchantment extends AbstractEnchantment {
        public AetherBarrierEnchantment(Player owner) {
            super(owner);
        }
        
        private final TriggerAction EnterSacrifice = new TriggerAction() {
                @Override
                public void execute(Object args) {
                    int i = 0, split, choice;
                    Scanner reader = CardGame.instance.getScanner();
                    System.out.println("Choose a permanent to be sacrificed\n");
                    for(Creature c:CardGame.instance.getCurrentPlayer().getCreatures()){
                        System.out.println( i + ") " + c.name() );
                        i++;
                    }
                    split = i;
                    for(Enchantment e:CardGame.instance.getCurrentPlayer().getEnchantments()){
                        System.out.println( i + ") " + e.name() );
                        i++;
                    }
                    choice = reader.nextInt();
                    if(choice > split){
                        CardGame.instance.getCurrentPlayer().getEnchantments().remove(choice-split);
                    }
                    else{
                        CardGame.instance.getCurrentPlayer().getCreatures().remove(choice);
                    }
                }
            };
        
        @Override
        public void insert() {
            super.insert();
            CardGame.instance.getTriggers().register(Triggers.ENTER_CREATURE_FILTER, EnterSacrifice);
        }
        
        @Override
        public boolean remove() {
            super.remove();
            CardGame.instance.getTriggers().deregister(EnterSacrifice);
            return true;
        }
        
        @Override
        public String name() { return "AetherBarrier"; }
    }
    
    @Override
    public Effect getEffect(Player p) { return new AetherBarrierEffect(p,this); }
    @Override
    public String name() { return "AetherBarrier"; }
    @Override
    public String type() { return "Enchantment"; }
    @Override
    public String ruleText() { return "Whenever a player plays a creature spell, that player sacrifices a permanent unless he or she pays 1"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}
