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
import java.util.ArrayList;
import java.util.Deque;
import java.util.EnumMap;

/*
    Questa classe implementa la carta world at war, questa carta da al giocatore che la attiva una combat e una
    main phase supplementari, successive alla prima main phase; inoltre, effettua l'untap di tutte le creature
    che hanno attaccato nella prima combat.
    L'effetto è implementato sfruttando la creazione di un nuovo phase manager, a cui viene detto di effetturare una combat
    e una main, che viene sovrapposto a quello di base. L'untap viene effettuato tramite il get della lista della combat phase.
    Al termine del turno un trigger rimuove il phase manager, quindi ritorna quello di default.
*/

public class WorldAtWar implements Card {
    private static class WorldAtWarFactory implements CardFactory{
        @Override
        public Card create(){
            return new WorldAtWar();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new WorldAtWarFactory());
    
    private class WorldAtWarEffect extends AbstractCardEffect {
        ArrayList<Creature> attackingCreatures;
        public WorldAtWarEffect(Player p, Card c, ArrayList<Creature> att){
            super(p, c);
            attackingCreatures = att;
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
            private Phases currentPhaseIdx=Phases.UNTAP;

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
            for(Creature c: attackingCreatures){
                c.untap();
            }
            owner.setPhaseManager(m);
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
    
    private ArrayList<Creature> attack;
    private WorldAtWarCardTrigger t;
    
    public WorldAtWar(){
        super();
        t = new WorldAtWarCardTrigger(this);
        CardGame.instance.getTriggers().register(Triggers.STACK_CHARGING_STARTED_EVENT, t);
    }
    
    private class WorldAtWarCardTrigger implements TriggerAction{
            private ArrayList<Creature> a;
            Phase c;
            WorldAtWar w;
            
            public WorldAtWarCardTrigger(WorldAtWar w){
                this.w = w;
            }
            
            @Override
            public void execute(Object args) {
                c = CardGame.instance.getCurrentPlayer().getPhase(Phases.COMBAT);
                DefaultCombatPhase co;
                if(c instanceof DefaultCombatPhase){
                    co = (DefaultCombatPhase) c;
                    a = co.getCreaturesWhichAttacked();
                    w.setAttackers(a);
                }
                
            }
            
    }
    
    private void setAttackers(ArrayList<Creature> a){
        this.attack = a;
    }
    
    @Override
    public Effect getEffect(Player p) {
        CardGame.instance.getTriggers().deregister(t);
        return new WorldAtWarEffect(p, this, attack);
    }
    
    
    @Override
    public String name() { return "WorldAtWar"; }
    @Override
    public String type() { return "Sorcery"; }
    @Override
    public String ruleText() { return "After the first postcombat main phase this turn , there's an additional combat phase followed "; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}
