
package cardgame.cards;

import cardgame.AbstractCreature;
import cardgame.AbstractCreatureCardEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.CreatureImage;
import cardgame.Effect;
import cardgame.Player;
import cardgame.StaticInitializer;
import cardgame.TriggerAction;
import java.util.ArrayList;
import java.util.List;

public class ArgothianEnchantress implements Card {
    private static class ArgothianEnchantressFactory implements CardFactory{
        @Override
        public Card create(){
            return new ArgothianEnchantress();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer("ArgothianEnchantress", new ArgothianEnchantressFactory());
    
    private class ArgothianEnchantressEffect extends AbstractCreatureCardEffect {
        
        public ArgothianEnchantressEffect(Player p, Card c) { super(p,c); }
        @Override
        protected Creature createCreature() {
            Creature c =  new ArgothianEnchantressCreature (owner);
            return new CreatureImage(owner, c);
            
        }
        
        private final TriggerAction SaltaStack = new TriggerAction() { 
            
            @Override
            public void execute(Object args) {
                
                CardGame.instance.getTriggers();
            }
        };
        
        @Override
        public void resolve () {
            target = CardGame.instance.getCurrentAdversary();
            CardGame.instance.getTriggers().register(Triggers.START_TURN_FILTER, AdversaryTurn);
        }


        @Override
        public boolean isTargetEffect() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setTarget() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getTarget() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    @Override
    public Effect getEffect(Player p) { return new ArgothianEnchantressEffect(p,this); }
    
    
    private class ArgothianEnchantressCreature extends AbstractCreature {
        ArrayList<Effect> all_effects= new ArrayList<>();
        ArrayList<Effect> tap_effects= new ArrayList<>();
        
        ArgothianEnchantressCreature(Player owner) { 
            super(owner);
            all_effects.add( new Effect() { 
                                    @Override
                                    public boolean play() { 
                                        CardGame.instance.getStack().add(this);
                                        return tap(); 
                                    }
                                    @Override
                                    public void resolve() {}
                                    @Override
                                    public String toString() 
                                        { return "ArgothianEnchantress :Shroud this creature can't be the target of spells or abilities"
                                                + "tap: ArgothianEnchantress does whenever you cast an enchantment spell, draw a card"; }

                @Override
                public boolean isTargetEffect() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void setTarget() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public Object getTarget() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                                }
                ); 
        }
        
        @Override
        public String name() { return "ArgothianEnchantress"; }
        @Override
        public int getPower() { return 0; }
        @Override
        public int getToughness() { return 1; }

        @Override
        public List<Effect> effects() { return all_effects; }
        @Override
        public List<Effect> avaliableEffects() { return (isTapped)?tap_effects:all_effects; }
    }
    
    
    @Override
    public String name() { return "ArgothianEnchantress"; }
    @Override
    public String type() { return "Creature"; }
    @Override
    public String ruleText() { return "Put in play a creature ArgothianEnchantress(0/1) \"Shroud: this creature can't be the target of spells or abilities\"\n" +
"                                                + \"tap: ArgothianEnchantress does whenever you cast an enchantment spell, draw a card\"; }"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }

}

