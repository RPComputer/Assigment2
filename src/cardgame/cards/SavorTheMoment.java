
package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import cardgame.DefaultTurnManager;
import cardgame.SkipPhase;
import cardgame.StaticInitializer;
import cardgame.TriggerAction;
import cardgame.Triggers;

public class SavorTheMoment implements Card {
    private static class SavorTheMomentFactory implements CardFactory{
        @Override
        public Card create(){
            return new SavorTheMoment();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new SavorTheMomentFactory());
    
    private class SavorTheMomentEffect extends AbstractCardEffect {
        Player ownerr = this.owner;
        DefaultTurnManager newturn;
        private SavorTheMomentEffect(Player p, Card c) {
            super(p,c);
        }
        
        private final TriggerAction TheNewTurn = new TriggerAction() { 
            @Override
            public void execute(Object args) {
                CardGame.instance.getTriggers().register(Triggers.COMBAT_FILTER, SkipUntapPhase);
            }
        };
        
        private final TriggerAction SkipUntapPhase = new TriggerAction() { 
            SkipPhase phase;
            @Override
            public void execute(Object args) {
                phase=new SkipPhase(CardGame.instance.getCurrentPlayer().nextPhaseId());
                phase.execute();
                CardGame.instance.getTriggers().register(Triggers.END_TURN_FILTER, DeleteTurnAndDeregister);
            }
        };
        
        private final TriggerAction DeleteTurnAndDeregister = new TriggerAction() { 
            @Override
            public void execute(Object args) {
                CardGame.instance.removeTurnManager(newturn);
                CardGame.instance.getTriggers().deregister(TheNewTurn);
                CardGame.instance.getTriggers().deregister(SkipUntapPhase);
                CardGame.instance.getTriggers().deregister(DeleteTurnAndDeregister);
            }
        };
        
        @Override
        public void resolve () {
            Player[] players = new Player[2];
            players[0]=this.ownerr;
            players[1]=CardGame.instance.getCurrentAdversary();
            newturn = new DefaultTurnManager(players);
            CardGame.instance.setTurnManager(newturn);
            CardGame.instance.getTriggers().register(Triggers.START_TURN_FILTER, TheNewTurn);
        }

        @Override
        public boolean isTargetEffect() {
            return false;
        }

        @Override
        public void setTarget() {}

        @Override
        public Object getTarget() {
            return null;
        }
        @Override
        public String toString(){return "SavorTheMoment";}
    }
    @Override
    public Effect getEffect(Player p) { return new SavorTheMomentEffect(p,this); }
    
    
    @Override
    public String name() { return "SavorTheMoment"; }
    @Override
    public String type() { return "Enchantment"; }
    @Override
    public String ruleText() { return "Take an extra turn after this one. Skip the untap step of that turn"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}
