
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
            return new FalsePeace();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new SavorTheMomentFactory());
    
    private class SavorTheMomentEffect extends AbstractCardEffect {
        Player ownerr = this.owner; 

        private SavorTheMomentEffect(Player p, Card c) {
            super(p,c);
        }
        private final TriggerAction AdversaryTurn = new TriggerAction() { // wait until adversary turn starts
            SkipPhase phase;
            DefaultTurnManager t = new DefaultTurnManager(ownerr);
            @Override
            public void execute(Object args) {
                
                CardGame.instance.setTurnManager(t);
                CardGame.instance.removeTurnManager(t);
                class MyTurn implements TurnManager{

                    @Override
                    public Player getCurrentPlayer() {
                        return ownerr;
                    }

                    @Override
                    public Player getCurrentAdversary() {
                        Player p=CardGame.instance.getCurrentPlayer();
                        if(ownerr.equals(p))
                            return ownerr;
                        else
                            return p;
                    }

                    @Override
                    public Player nextPlayer() {
                        return CardGame.instance.nextPlayer();
                    }
                
                }
                MyTurn myturn = new MyTurn();
                CardGame.instance.setTurnManager(myturn);
            }
        };
        
        @Override
        public void resolve () {
            
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
