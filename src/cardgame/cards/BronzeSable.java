
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
import java.util.ArrayList;
import java.util.List;

/*
    Questa è una carta creatura senza effetti di vita 1 e attacco 2
*/
public class BronzeSable implements Card {
    private static class BronzeSableFactory implements CardFactory{
        @Override
        public Card create(){
            return new BronzeSable();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new BronzeSableFactory());
    
    private class BronzeSableEffect extends AbstractCreatureCardEffect {
        public BronzeSableEffect(Player p, Card c) { super(p,c); }
        @Override
        protected Creature createCreature() {
            BronzeSableCreature c =  new BronzeSableCreature(owner);
            CreatureImage cr = new CreatureImage(owner, c);
            c.setHead(cr);
            return cr;
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
        @Override
        public String toString(){return "BronzeSable";}
    }
    @Override
    public Effect getEffect(Player p) { return new BronzeSableEffect(p,this); }
    
    
    private class BronzeSableCreature extends AbstractCreature {
        
        BronzeSableCreature(Player owner) { 
            super(owner);
        }
        
        @Override
        public String name() { return "BronzeSable"; }
        @Override
        public int getPower() { return 2; }
        @Override
        public int getToughness() { return 1; }

        @Override
        public List<Effect> effects() { return new ArrayList<>();}
        @Override
        public List<Effect> avaliableEffects() { return new ArrayList<>();}
    }
    
    
    @Override
    public String name() { return "BronzeSable"; }
    @Override
    public String type() { return "Creature"; }
    @Override
    public String ruleText() { return "Put in play a creature BronzeSable(2/1)"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }

}
