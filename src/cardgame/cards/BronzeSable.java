
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
import java.util.ArrayList;
import java.util.List;

public class BronzeSable implements Card {
    private static class BronzeSableFactory implements CardFactory{
        @Override
        public Card create(){
            return new BronzeSable();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer("BronzeSable", new BronzeSableFactory());
    
    private class BronzeSableEffect extends AbstractCreatureCardEffect {
        public BronzeSableEffect(Player p, Card c) { super(p,c); }
        @Override
        protected Creature createCreature() {
            Creature c =  new BronzeSableCreature(owner);
            return new CreatureImage(owner, c);
        }
    }
    @Override
    public Effect getEffect(Player p) { return new BronzeSableEffect(p,this); }
    
    
    private class BronzeSableCreature extends AbstractCreature {
        ArrayList<Effect> all_effects= new ArrayList<>();
        ArrayList<Effect> tap_effects= new ArrayList<>();
        
        BronzeSableCreature(Player owner) { 
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
                                        { return "tap: BronzeSable does nothing"; }
                                }
                ); 
        }
        
        @Override
        public String name() { return "BronzeSable"; }
        @Override
        public int getPower() { return 2; }
        @Override
        public int getToughness() { return 1; }

        @Override
        public List<Effect> effects() { return all_effects; }
        @Override
        public List<Effect> avaliableEffects() { return (isTapped)?tap_effects:all_effects; }
    }
    
    
    @Override
    public String name() { return "BronzeSable"; }
    @Override
    public String type() { return "Creature"; }
    @Override
    public String ruleText() { return "Put in play a creature BronzeSable(2/1) with tap: BronzeSable does nothing"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }

}
