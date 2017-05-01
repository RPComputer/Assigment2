
package cardgame.cards;

import cardgame.AbstractEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.StaticInitializer;

public class BoilingEarth implements Card {
    private static class BoilingEarthFactory implements CardFactory{
        @Override
        public Card create(){
            return new BoilingEarth();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new BoilingEarthFactory());
    
    private class BoilingEarthEffect extends AbstractEffect {
        
        @Override
        public void resolve () {
            for(Creature a : CardGame.instance.getCurrentPlayer().getCreatures()){
                a.inflictDamage(1);
            }

            for(Creature a : CardGame.instance.getCurrentAdversary().getCreatures()){
                a.inflictDamage(1);
            }
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
        public String toString(){return "BoilingEarth";}
    }
    @Override
    public Effect getEffect(Player p) { return new BoilingEarthEffect(); }
    
    
    @Override
    public String name() { return "BoilingEarth"; }
    @Override
    public String type() { return "Sorcery"; }
    @Override
    public String ruleText() { return "Deals 1 damage to each creature"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}
