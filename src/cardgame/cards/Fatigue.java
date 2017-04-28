
package cardgame.cards;

import cardgame.AbstractEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import static cardgame.Interfaccia.acquireInput;
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
        
        private final TriggerAction AdversaryTurn = new TriggerAction() { // wait until adversary turn starts
            SkipPhase phase;
            @Override
            public void execute(Object args) {
                phase=new SkipPhase(target.currentPhaseId().next());
                phase.execute();
                CardGame.instance.getTriggers().deregister(AdversaryTurn);
            }
        };
        
        @Override
        public void resolve () {
            target = CardGame.instance.getCurrentAdversary();
            CardGame.instance.getTriggers().register(Triggers.START_TURN_FILTER, AdversaryTurn);
        }

        @Override
        public boolean isTargetEffect() {
            return true;
        }

        @Override
        public void setTarget() {
            System.out.println("Choose the player who will skip his next combat phase, 0 for the first, 1 for second.");
            System.out.println(CardGame.instance.getCurrentPlayer().name() + "   " + CardGame.instance.getCurrentAdversary().name());
            
            int choice;
            
            do{
                choice = acquireInput();
            }while(choice!=0 && choice!=1);
            
            if (choice==0) {
                this.target = CardGame.instance.getCurrentPlayer();
            } else {
                this.target = CardGame.instance.getCurrentAdversary(); 
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
    public String ruleText() { return "Target player skips his next draw step"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}

