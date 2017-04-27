
package cardgame.cards;

import cardgame.AbstractEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.CardGame;
import cardgame.Effect;
import cardgame.Player;
import cardgame.StaticInitializer;

public class DayOfJudgment implements Card{
    private static class DayOfJudgmentFactory implements CardFactory{
        @Override
        public Card create(){
            return new DayOfJudgment();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new DayOfJudgmentFactory());
    
    private class DayOfJudgmentEffect extends AbstractEffect {
        
        @Override
        public void resolve () {
            CardGame.instance.getCurrentPlayer().getCreatures().clear();

            CardGame.instance.getCurrentAdversary().getCreatures().clear();
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
        public String toString(){return "DayOfJudgment";}
    }
    public Effect getEffect(Player p) { return new DayOfJudgmentEffect(); }
    
    
    public String name() { return "Day of Judgment"; }
    public String type() { return "Enchantment"; }
    public String ruleText() { return "Destroy all creatures"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    public boolean isInstant() { return false; }
}
