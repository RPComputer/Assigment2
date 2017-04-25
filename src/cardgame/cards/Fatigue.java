
package cardgame.cards;

import cardgame.AbstractEffect;
import cardgame.Card;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import cardgame.SkipPhase;
import cardgame.TriggerAction;
import cardgame.Triggers;

public class Fatigue implements Card {
    private class FatigueEffect extends AbstractEffect {
        Player target = null; // target player 
        private final TriggerAction AdversarySkipsDRAW = new TriggerAction() {
            SkipPhase phase;
            @Override
            public void execute(Object args) {
                phase=new SkipPhase(target.currentPhaseId());
                phase.execute();
            }
        };
        
        private final TriggerAction AdversaryTurn = new TriggerAction() { // wait until adversary turn starts
                @Override
                public void execute(Object args) {
                    CardGame.instance.getTriggers().register(Triggers.DRAW_FILTER, AdversarySkipsDRAW);
                    // start AdversarySkipsDRAW Trigger action
                }
        };
        
        @Override
        public void resolve () {
            target = CardGame.instance.getCurrentAdversary();
            CardGame.instance.getTriggers().register(Triggers.START_TURN_FILTER, AdversaryTurn);
        }
    }
    @Override
    public Effect getEffect(Player p) { return new FatigueEffect(); }
    
    
    @Override
    public String name() { return "Fatigue"; }
    @Override
    public String type() { return "Enchantment"; }
    @Override
    public String ruleText() { return "Target player skips his next draw step"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}

