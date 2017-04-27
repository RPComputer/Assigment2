package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import cardgame.DefaultCombatPhase;
import cardgame.DefaultTurnManager;
import cardgame.SkipPhase;
import cardgame.StaticInitializer;
import cardgame.TriggerAction;
import cardgame.Triggers;
import cardgame.Phases;

public class WorldAtWar implements Card {
    private static class WorldAtWarFactory implements CardFactory{
        @Override
        public Card create(){
            return new WorldAtWar();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new WorldAtWarFactory());
    
    private class WorldAtWarEffect extends AbstractCardEffect {
        Player ownerr = this.owner;
        DefaultTurnManager newturn;
        private WorldAtWarEffect(Player p, Card c) {
            super(p,c);
        }
        
        private final TriggerAction NewCombat = new TriggerAction() { 
            SkipPhase phase;
            @Override
            public void execute(Object args) {
                CardGame.instance.getCurrentPlayer().currentPhaseId().next().
                CardGame.instance.getTriggers().register(Triggers.MAIN_FILTER, SkipUntapPhase);
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
                CardGame.instance.getCurrentAdversary().currentPhaseId();
                CardGame.instance.getTriggers().deregister(NewCombat);
                CardGame.instance.getTriggers().deregister(SkipUntapPhase);
                CardGame.instance.getTriggers().deregister(DeleteTurnAndDeregister);
            }
        };
        
        @Override
        public void resolve () {
            CardGame.instance.getTriggers().register(Triggers.COMBAT_FILTER, NewCombat);
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
    public Effect getEffect(Player p) { return new WorldAtWarEffect(p,this); }
    
    
    @Override
    public String name() { return "WorldAtWar"; }
    @Override
    public String type() { return "Enchantment"; }
    @Override
    public String ruleText() { return "After the first postcombat main phase this \n turn , there's an additional combat phase \n followed "; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}
