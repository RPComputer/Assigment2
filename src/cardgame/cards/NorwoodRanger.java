
package cardgame.cards;

import cardgame.AbstractCreature;
import cardgame.AbstractCreatureCardEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Creature;
import cardgame.CreatureImage;
import cardgame.Effect;
import cardgame.Player;
import cardgame.StaticInitializer;
import java.util.List;

public class NorwoodRanger implements Card {
    private static class NorwoodRangerFactory implements CardFactory{
        @Override
        public Card create(){
            return new NorwoodRanger();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new NorwoodRangerFactory());
    
    private class NorwoodRangerEffect extends AbstractCreatureCardEffect {
        public NorwoodRangerEffect(Player p, Card c) { super(p,c); }
        @Override
        protected Creature createCreature() {
            Creature c =  new NorwoodRangerCreature(owner);
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
    public Effect getEffect(Player p) { return new NorwoodRangerEffect(p,this); }
    
    
    private class NorwoodRangerCreature extends AbstractCreature {
        
        NorwoodRangerCreature(Player owner) { 
            super(owner);
        }

        @Override
        public String name() { return "NorwoodRanger"; }
        @Override
        public int getPower() { return 1; }
        @Override
        public int getToughness() { return 2; }

        @Override
        public List<Effect> effects() { return null;}
        @Override
        public List<Effect> avaliableEffects() { return null; }
    }
    
    
    @Override
    public String name() { return "NorwoodRanger"; }
    @Override
    public String type() { return "Creature"; }
    @Override
    public String ruleText() { return "Put in play a creature NorwoodRanger(1/2) with tap: NorwoodRanger does nothing"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }

}
