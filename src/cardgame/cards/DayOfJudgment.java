
package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.AbstractEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.Effect;
import cardgame.Player;
import cardgame.StaticInitializer;
import java.util.ArrayList;

public class DayOfJudgment implements Card{
    private static class DayOfJudgmentFactory implements CardFactory{
        @Override
        public Card create(){
            return new DayOfJudgment();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new DayOfJudgmentFactory());
    
    private class DayOfJudgmentEffect extends AbstractCardEffect {
        
        public DayOfJudgmentEffect(Player p, Card c) { super(p,c);}
        
        @Override
        public void resolve () {
            ArrayList<Creature> l1 = new ArrayList<>();
            ArrayList<Creature> l2 = new ArrayList<>();
            for(Creature c:CardGame.instance.getCurrentPlayer().getCreatures())
                l1.add(c);

            for(Creature c:CardGame.instance.getCurrentAdversary().getCreatures())
                l2.add(c);
            
            for(Creature c:l1)
                CardGame.instance.getCurrentPlayer().destroy(c);

            for(Creature c:l2)
                CardGame.instance.getCurrentAdversary().destroy(c);
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
    public Effect getEffect(Player p) { return new DayOfJudgmentEffect(p, this); }
    
    
    @Override
    public String name() { return "DayOfJudgment"; }
    @Override
    public String type() { return "Sorcery"; }
    @Override
    public String ruleText() { return "Destroy all creatures"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}
