
package cardgame.cards;

import cardgame.AbstractEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import static cardgame.Interfaccia.acquireInput;
import cardgame.Phases;
import cardgame.SkipPhase;
import cardgame.StaticInitializer;
import cardgame.TriggerAction;
import cardgame.Triggers;

public class Fatigue implements Card {
    private static class FatigueFactory implements CardFactory{
        @Override
        public Card create(){
            return new Fatigue();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new FatigueFactory());
    
    private class FatigueEffect extends AbstractEffect {
        Player target = null; // target player 
        
        
        @Override
        public void resolve () {
            target.setPhase(Phases.DRAW,new SkipPhase(Phases.DRAW));
        }

        @Override
        public boolean isTargetEffect() {
            return true;
        }

        @Override
        public void setTarget() {
            System.out.println("Choose the player who will skip his next draw phase, 0 for your opponent, 1 for you.");
            System.out.println("\t\t      [0]-->" + CardGame.instance.getCurrentAdversary().name() + "     ||     " + "[1]-->" + CardGame.instance.getCurrentPlayer().name());
            
            int choice;
            
            do{
                choice = acquireInput();
            }while(choice!=0 && choice!=1);
            
            if (choice==0) {
                this.target = CardGame.instance.getCurrentAdversary();
            } else {
                this.target = CardGame.instance.getCurrentPlayer(); 
            }
        }

        @Override
        public Object getTarget() {
            return target;
        }
        @Override
        public String toString(){return "Fatigue";}
    }
    @Override
    public Effect getEffect(Player p) {
        FatigueEffect e = new FatigueEffect();
        return e;
    }
    
    
    @Override
    public String name() { return "Fatigue"; }
    @Override
    public String type() { return "Sorcery"; }
    @Override
    public String ruleText() { return "Target player skips his next draw phase"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}

