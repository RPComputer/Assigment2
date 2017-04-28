
package cardgame.cards;

import cardgame.AbstractEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.CardGame;
import cardgame.Creature;
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
            for(Creature c:CardGame.instance.getCurrentPlayer().getCreatures())
                c.remove();

            for(Creature c:CardGame.instance.getCurrentAdversary().getCreatures())
                c.remove();
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
    @Override
    public Effect getEffect(Player p) { return new DayOfJudgmentEffect(); }
    
    
    @Override
    public String name() { return "Day of Judgment"; }
    @Override
    public String type() { return "Sorcery"; }
    @Override
    public String ruleText() { return "Destroy all creatures"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}
