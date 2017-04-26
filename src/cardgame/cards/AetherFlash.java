package cardgame.cards;

import cardgame.Card;
import cardgame.Effect;
import cardgame.Enchantment;
import cardgame.Player;
import cardgame.AbstractEnchantmentCardEffect;
import cardgame.AbstractEnchantment;
import cardgame.CardFactory;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.StaticInitializer;
import cardgame.TriggerAction;
import cardgame.Triggers;

public class AetherFlash implements Card {
    private static class AetherFlashFactory implements CardFactory{
        @Override
        public Card create(){
            return new AetherFlash();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer( new AetherFlashFactory());
    
    private class AetherFlashEffect extends AbstractEnchantmentCardEffect {
        public AetherFlashEffect(Player p, Card c) { super(p,c); }
        @Override
        protected Enchantment createEnchantment() { return new AetherFlashEnchantment(owner); }

        @Override
        public boolean isTargetEffect() {
            return false;
        }

        @Override
        public void setTarget() {
            throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getTarget() {
            throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    @Override
    public Effect getEffect(Player p) { return new AetherFlashEffect(p,this); }
    
    private class AetherFlashEnchantment extends AbstractEnchantment {
        public AetherFlashEnchantment(Player owner) {
            super(owner);
        }
        
        private final TriggerAction EnterDamage = new TriggerAction() {
                @Override
                public void execute(Object args) {
                    if (args != null  && args instanceof Creature) {
                        Creature c = (Creature)args;
                        c.inflictDamage(2);
                    }
                }
            };
        
        @Override
        public void insert() {
            super.insert();
            CardGame.instance.getTriggers().register(Triggers.ENTER_CREATURE_FILTER, EnterDamage);
        }
        
        @Override
        public boolean remove() {
            super.remove();
            CardGame.instance.getTriggers().deregister(EnterDamage);
            return true;
        }
        
        @Override
        public String name() { return "AetherFlash"; }
    }
    
    
    @Override
    public String name() { return "AetherFlash"; }
    @Override
    public String type() { return "Enchantment"; }
    @Override
    public String ruleText() { return "Whenever a creature enters the game deals 2 damage to it"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}
