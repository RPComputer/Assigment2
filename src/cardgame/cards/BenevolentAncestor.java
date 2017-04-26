
package cardgame.cards;

import cardgame.AbstractCreature;
import cardgame.AbstractCreatureCardEffect;
import cardgame.AbstractDecorator;
import cardgame.AbstractEffect;
import cardgame.AbstractPlayerDamageModificator;
import cardgame.Card;
import cardgame.Creature;
import cardgame.CreatureImage;
import cardgame.Effect;
import cardgame.Player;
import cardgame.TriggerAction;
import java.util.List;

public class BenevolentAncestor implements Card {
    
    private class BenevolentAncestorEffect extends AbstractCreatureCardEffect {
        public BenevolentAncestorEffect(Player p, Card c) { super(p,c); }
        @Override
        protected Creature createCreature() {
            Creature c =  new BenevolentAncestorCreature(owner);
            return new CreatureImage(owner, c);
        }

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
    public Effect getEffect(Player p) { return new BenevolentAncestorEffect(p,this); }
    
    
    private class BenevolentAncestorCreature extends AbstractCreature {
        
        BenevolentAncestorCreature(Player owner) { 
            super(owner);
        }   
        
        private class BenevolentAncestorDecorator extends AbstractDecorator{
            
            public BenevolentAncestorDecorator(CreatureImage c) {
                super(c);
            }
            
        }
        
        private class BenevolentAncestorDamageModifier extends AbstractPlayerDamageModificator{
            private class BenevolentAncestorDamageModifierTrigger implements TriggerAction{
                BenevolentAncestorDamageModifier m;
                public BenevolentAncestorDamageModifierTrigger(BenevolentAncestorDamageModifier m){
                    this.m = m;
                }
                @Override
                public void execute(Object args) {
                    
                }
                
            }
            
            public BenevolentAncestorDamageModifier(){
                BenevolentAncestorDamageModifierTrigger t = new BenevolentAncestorDamageModifierTrigger(this);
                CardGame.instance
            }
            
            @Override
            public void inflictDamage(int p){
                super.inflictDamage(p-1);
            }
        }
        
        private class BenevolentAncestorCreatureEffect extends AbstractEffect{
            Player target1 = null;
            CreatureImage target2 = null;
            
            @Override
            public void resolve() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean isTargetEffect() {
                return true;
            }

            @Override
            public void setTarget() {
                
            }

            @Override
            public Object getTarget() {
                if(target1 == null)
                    return target2;
                return target1;
            }
            
        }
        
        @Override
        public String name() { return "BenevolentAncestor"; }
        
        @Override
        public int getPower() { return 0; }
        @Override
        public int getToughness() { return 4; }

        @Override
        public List<Effect> effects() { return null; }
        @Override
        public List<Effect> avaliableEffects() { return null; }
    }
    
    
    @Override
    public String name() { return "BenevolentAncestor"; }
    @Override
    public String type() { return "Creature"; }
    @Override
    public String ruleText() { return "Put in play BenevolentAncestor(0/4), this creature can't attack\n with tap: prevent 1 damage to a creature or player until the end of the turn\n"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }

}
