package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.DefaultCombatPhase;
import cardgame.DefaultMainPhase;
import cardgame.Phase;
import cardgame.PhaseManager;
import cardgame.StaticInitializer;
import cardgame.TriggerAction;
import cardgame.Triggers;
import cardgame.Phases;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;

public class WorldAtWar implements Card {
    private static class WorldAtWarFactory implements CardFactory{
        @Override
        public Card create(){
            return new WorldAtWar();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new WorldAtWarFactory());
    
    private class WorldAtWarEffect extends AbstractCardEffect {
        
        public WorldAtWarEffect(Player p, Card c){
            super(p, c);
        }
        
        private class WorldAtWarTrigger implements TriggerAction{
            
            Player p;
            WorldAtWarPhaseManager m;
            public WorldAtWarTrigger(Player p, WorldAtWarPhaseManager m){
                this.p = p;
                this.m = m;
            }
            
            @Override
            public void execute(Object args) {
                p.removePhaseManager(m);
                CardGame.instance.getTriggers().deregister(this);
            }
            
        }
        
        private class WorldAtWarPhaseManager implements PhaseManager{

            private final EnumMap<Phases, Deque<Phase> > phases = new EnumMap<>(Phases.class);
            private Phases currentPhaseIdx=Phases.NULL;

            public WorldAtWarPhaseManager() {
                phases.put(Phases.COMBAT, new ArrayDeque<Phase>());
                phases.get(Phases.COMBAT).push(new DefaultCombatPhase());

                phases.put(Phases.MAIN, new ArrayDeque<Phase>());
                phases.get(Phases.MAIN).push(new DefaultMainPhase());

                phases.put(Phases.NULL, new ArrayDeque<Phase>());
            }


            @Override
            public Phases currentPhase() { return currentPhaseIdx; }

            @Override
            public Phases nextPhase() { 
                currentPhaseIdx = currentPhaseIdx.next();
                return currentPhase();
            }
            
        }
        
        @Override
        public void resolve() {
            WorldAtWarPhaseManager m = new WorldAtWarPhaseManager();
            WorldAtWarTrigger t = new WorldAtWarTrigger(owner, m);
            CardGame.instance.getTriggers().register(Triggers.END_TURN_FILTER, t);
            owner.setPhaseManager(m);
            DefaultCombatPhase co = (DefaultCombatPhase) owner.getPhase(Phases.COMBAT);
            for(Creature c: co.attackingCreatures){
                c.untap();
            }
        }

        @Override
        public boolean isTargetEffect() {
            return false;
        }

        @Override
        public void setTarget() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getTarget() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    @Override
    public Effect getEffect(Player p) { return new WorldAtWarEffect(p, this); }
    
    
    @Override
    public String name() { return "WorldAtWar"; }
    @Override
    public String type() { return "Sorcery"; }
    @Override
    public String ruleText() { return "After the first postcombat main phase this \n turn , there's an additional combat phase \n followed "; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}
