
package cardgame.cards;

import cardgame.AbstractEffect;
import cardgame.Card;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import cardgame.Creature;

public class BoilingEarth implements Card {
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
    }
    @Override
    public Effect getEffect(Player p) { return new BoilingEarthEffect(); }
    
    
    @Override
    public String name() { return "Boiling Earth"; }
    @Override
    public String type() { return "Enchantment"; }
    @Override
    public String ruleText() { return "Deals 1 damage to each creature"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}
