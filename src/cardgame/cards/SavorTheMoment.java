
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
import cardgame.TurnManager;

public class SavorTheMoment implements Card {
    private static class SavorTheMomentFactory implements CardFactory{
        @Override
        public Card create(){
            return new SavorTheMoment();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new SavorTheMomentFactory());
    
    private class SavorTheMomentTurnManager implements TurnManager{
        private final Player[] Players;
        int currentPlayerIdx;

        public SavorTheMomentTurnManager(Player[] p, Player c) {
            Players=p;
            if(c == CardGame.instance.getPlayer(0))
                this.currentPlayerIdx = 0;
            else
                this.currentPlayerIdx = 1;
        }

        @Override
        public Player getCurrentPlayer() { return Players[currentPlayerIdx]; }

        @Override
        public Player getCurrentAdversary() { return Players[(currentPlayerIdx+1)%2]; }

        @Override
        public Player nextPlayer() { 
            currentPlayerIdx = (currentPlayerIdx+1)%2;
            return getCurrentPlayer();
        }
        
    }
    
    private class SavorTheMomentEffect extends AbstractCardEffect {
        Player ownerr = this.owner;
        DefaultTurnManager newturn;
        private SavorTheMomentEffect(Player p, Card c) {
            super(p,c);
        }
        
        private final TriggerAction TheNewTurn = new TriggerAction() { 
            @Override
            public void execute(Object args) {
                CardGame.instance.setTurnManager(newturn);
                CardGame.instance.getTriggers().register(Triggers.DRAW_FILTER, SkipUntapPhase);
            }
        };
        
        private final TriggerAction SkipUntapPhase = new TriggerAction() { 
            SkipPhase phase;
            @Override
            public void execute(Object args) {
                phase = new SkipPhase(CardGame.instance.getCurrentPlayer().currentPhaseId().next());
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
            players[0] = CardGame.instance.getPlayer(0);
            players[1] = CardGame.instance.getPlayer(1);
            SavorTheMomentTurnManager newturn = new SavorTheMomentTurnManager(players, owner);
            CardGame.instance.getTriggers().register(Triggers.END_TURN_FILTER, TheNewTurn);
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
    public String type() { return "Sorcery"; }
    @Override
    public String ruleText() { return "Take an extra turn after this one. Skip the untap step of that turn"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}
