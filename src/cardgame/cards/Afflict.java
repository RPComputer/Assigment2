
package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.AbstractDecorator;
import cardgame.Card;
import cardgame.Creature;
import cardgame.CreatureImage;
import cardgame.Effect;
import cardgame.Player;

public class Afflict implements Card {
    
    private class AfflictEffect extends AbstractCardEffect {
        public AfflictEffect(Player p, Card c) { super(p,c); }
        @Override
        public void resolve() {
            
        }
    }
    
    private class AfflictDecorator extends AbstractDecorator{
        
        public AfflictDecorator(CreatureImage c, Creature n) {
            super(c, n);
        }
        
    }
    
    @Override
    public Effect getEffect(Player owner) { 
        
        return new AfflictEffect(owner, this); 
    }
    
    @Override
    public String name() { return "Afflict"; }
    @Override
    public String type() { return "Instant"; }
    @Override
    public String ruleText() { return name() + " this card applies -1/-1 to selected creature"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return true; }
}
