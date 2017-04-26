
package cardgame.cards;

import cardgame.AbstractEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import cardgame.SkipPhase;
import cardgame.StaticInitializer;
import cardgame.TriggerAction;
import cardgame.Triggers;

public class FalsePeace implements Card {
    private static class FalsePeaceFactory implements CardFactory{
        @Override
        public Card create(){
            return new FalsePeace();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer("FalsePeace", new FalsePeaceFactory());
    
    private class FalsePeaceEffect extends AbstractEffect {
        Player target = null; // target player 
        
        private final TriggerAction AdversaryTurn = new TriggerAction() { // wait until adversary turn starts
            SkipPhase phase;
            @Override
            public void execute(Object args) {
                phase=new SkipPhase(target.nextPhaseId());
                phase.execute();
                CardGame.instance.getTriggers().deregister(AdversaryTurn);
            }
        };
        
        @Override
        public void resolve () {
            target = CardGame.instance.getCurrentAdversary();
            CardGame.instance.getTriggers().register(Triggers.UNTAP_FILTER, AdversaryTurn);
        }
    }
    @Override
    public Effect getEffect(Player p) { return new FalsePeaceEffect(); }
    
    
    @Override
    public String name() { return "FalsePeace"; }
    @Override
    public String type() { return "Enchantment"; }
    @Override
    public String ruleText() { return "Target player skips his next combat phase"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}
