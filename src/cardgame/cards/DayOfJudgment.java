
package cardgame.cards;

import cardgame.AbstractEffect;
import cardgame.Card;
import cardgame.CardGame;
import cardgame.Effect;
import cardgame.Player;

public class DayOfJudgment implements Card{
    private class DayOfJudgmentEffect extends AbstractEffect {
        
        @Override
        public void resolve () {
            CardGame.instance.getCurrentPlayer().getCreatures().clear();

            CardGame.instance.getCurrentAdversary().getCreatures().clear();
        }
    }
    public Effect getEffect(Player p) { return new DayOfJudgmentEffect(); }
    
    
    public String name() { return "Day of Judgment"; }
    public String type() { return "Enchantment"; }
    public String ruleText() { return "Destroy all creatures"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    public boolean isInstant() { return false; }
}
