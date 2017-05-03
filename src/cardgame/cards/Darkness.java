
package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.AbstractCreature;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import static cardgame.Interfaccia.acquireInput;
import static cardgame.Interfaccia.showCreatures;
import cardgame.Player;
import cardgame.Triggers;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.DefaultDrawPhase;
import cardgame.DefaultEndPhase;
import cardgame.DefaultMainPhase;
import cardgame.DefaultUntapPhase;
import cardgame.Phase;
import cardgame.PhaseManager;
import cardgame.Phases;
import cardgame.StaticInitializer;
import cardgame.TriggerAction;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.EnumMap;
import java.util.Scanner;

public class Darkness implements Card {
    private static class DarknessFactory implements CardFactory{
        @Override
        public Card create(){
            return new Darkness();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new DarknessFactory());
    
    private class DarknessEffect extends AbstractCardEffect {
        public DarknessEffect(Player p,Card c){ super (p,c); }
        /*
        private class DarknessPhaseManager implements PhaseManager{

            private final EnumMap<Phases, Deque<Phase> > phases = new EnumMap<>(Phases.class);
            private Phases currentPhaseIdx=CardGame.instance.getCurrentPlayer().currentPhaseId();

            public DarknessPhaseManager() {
                phases.put(Phases.DRAW, new ArrayDeque<Phase>());
                phases.get(Phases.DRAW).push(new DefaultDrawPhase());
                
                phases.put(Phases.UNTAP, new ArrayDeque<Phase>());
                phases.get(Phases.UNTAP).push(new DefaultUntapPhase());
                
                phases.put(Phases.COMBAT, new ArrayDeque<Phase>());
                phases.get(Phases.COMBAT).push(new CombatWithNoDamage());

                phases.put(Phases.MAIN, new ArrayDeque<Phase>());
                phases.get(Phases.MAIN).push(new DefaultMainPhase());
                
                phases.put(Phases.END, new ArrayDeque<Phase>());
                phases.get(Phases.END).push(new DefaultEndPhase());

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
        
        private class DarknessTrigger implements TriggerAction{
            
            Player p;
            DarknessPhaseManager m;
            public DarknessTrigger(Player p, DarknessPhaseManager m){
                this.p = p;
                this.m = m;
            }
            
            @Override
            public void execute(Object args) {
                p.removePhaseManager(m);
                CardGame.instance.getTriggers().deregister(this);
            }
            
        }*/
        
        @Override
        public void resolve() {
            //DarknessPhaseManager m = new DarknessPhaseManager();
            //DarknessTrigger t = new DarknessTrigger(owner, m);
            //CardGame.instance.getTriggers().register(Triggers.END_TURN_FILTER, t);
            owner.setPhase(Phases.COMBAT,new CombatWithNoDamage());
        }

        @Override
        public boolean isTargetEffect() {
            return false;
        }

        @Override
        public void setTarget() {
            throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getTarget() {
            throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
        }
        @Override
        public String toString(){return "Darkness";}
    }
    
    @Override
    public Effect getEffect(Player owner) {
        return new DarknessEffect(owner,this);
    }
    
    
    @Override
    public String name() { return "Darkness"; }
    @Override
    public String type() { return "Instant"; }
    @Override
    public String ruleText() { return name() + " prevent all combat damage that would be dealt this turn"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return true; }
    
    private class CombatWithNoDamage implements Phase{
        @Override
        public void execute() {
            Player currentPlayer = CardGame.instance.getCurrentPlayer();
            Player opponentPlayer = CardGame.instance.getCurrentAdversary();
            int attacking = 1, defending = 1;
            Scanner reader = CardGame.instance.getScanner();
            ArrayList<Creature> attackingCreatures = new ArrayList<>();
            ArrayList<Creature> canAttackCreatures;
            ArrayList<Creature> canDefendCreatures;
            Creature c;
            System.out.println(currentPlayer.name() + ": combat phase");

            CardGame.instance.getTriggers().trigger(Triggers.COMBAT_FILTER);
            // TODO combat
            if(!currentPlayer.getCreatures().isEmpty()){
                while(attacking > 0){
                    System.out.println(currentPlayer.name() + "choose an attacking creature, 0 to pass");
                    canAttackCreatures = attackers(currentPlayer);
                    if(canAttackCreatures.isEmpty())
                        System.out.println("You don't have creatures that can attack.");
                    showCreatures(canAttackCreatures);
                    attacking = reader.nextInt();
                    if(attacking > 0){
                        c = canAttackCreatures.get(attacking-1);
                        c.tap();
                        c.addTarget(opponentPlayer);
                        attackingCreatures.add((AbstractCreature) c);
                    }
                }
            }
            //assunto parte dichiarazione attaccanti effettuata
            chargeCombatStack(opponentPlayer); // primo caricamento dello stack e risoluzione
            //definizione di chi difende
            if(!attackingCreatures.isEmpty() || opponentPlayer.getCreatures().isEmpty()){
                while(defending > 0){
                    System.out.println(currentPlayer.name() + "choose an defending creature, 0 to pass");
                    canDefendCreatures = defenders(opponentPlayer);
                    showCreatures(canDefendCreatures);
                    if(canDefendCreatures.isEmpty())
                        System.out.println("You don't have creatures that can defend.");
                    defending = reader.nextInt();
                    if(defending > 0){
                        c = canDefendCreatures.get(defending);
                        c.tap();
                        System.out.println(currentPlayer.name() + "choose an attacking creature to stop");
                        showCreatures(attackingCreatures);
                        attacking = reader.nextInt();
                        c.defend(attackingCreatures.get(attacking));
                    }
                }
            }
            chargeCombatStack(currentPlayer); // secondo caricamneto dello stack e risoluzione


        }

        private void chargeCombatStack(Player currentPlayer){
            int numberPasses=0;
            int responsePlayerIdx = (CardGame.instance.getPlayer(0) == currentPlayer)?0:1;
            while (numberPasses<2) {
                if (playAvailableEffect(CardGame.instance.getPlayer(responsePlayerIdx),false))
                    numberPasses=0;
                else ++numberPasses;

                responsePlayerIdx = (responsePlayerIdx+1)%2;
            }

            CardGame.instance.getStack().resolve();
        }


        // looks for all playable effects from cards in hand and creatures in play
        // and asks player for which one to play
        // includes creatures and sorceries only if isMain is true
        private boolean playAvailableEffect(Player activePlayer, boolean isMain) {
            //collect and display available effects...
            ArrayList<Effect> availableEffects = new ArrayList<>();
            Scanner reader = CardGame.instance.getScanner();

            //...cards first
            System.out.println(activePlayer.name() + " select card/effect to play, 0 to pass");
            int i=0;
            for( Card c:activePlayer.getHand() ) {
                if ( isMain || c.isInstant() ) {
                    availableEffects.add( c.getEffect(activePlayer) );
                    System.out.println(Integer.toString(i+1)+") " + c );
                    ++i;
                }
            }

            //...creature effects last
            for ( Creature c:activePlayer.getCreatures()) {
                for (Effect e:c.avaliableEffects()) {
                    availableEffects.add(e);
                    System.out.println(Integer.toString(i+1)+") " + c.name() + 
                        " ["+ e + "]" );
                    ++i;
                }
            }

            //get user choice and play it
            int idx= acquireInput()-1;
            if (idx<0 || idx>=availableEffects.size()) return false;

            availableEffects.get(idx).play();
            return true;
        }

        private ArrayList attackers(Player p){
            ArrayList<Creature> untapped = new ArrayList<>();
            int i = 0;
            for( Creature c:p.getCreatures()) {
                if ( !c.isTapped() && c.getAtt()) {
                    untapped.add(c);
                    System.out.println(Integer.toString(i+1)+") " + c.toString());
                    ++i;
                }
            }
            return untapped;
        }

        private ArrayList defenders(Player p){
            ArrayList<Creature> untapped = new ArrayList<>();
            int i = 0;
            for( Creature c:p.getCreatures()) {
                if ( !c.isTapped() && c.getDef()) {
                    untapped.add(c);
                    System.out.println(Integer.toString(i+1)+") " + c.toString());
                    ++i;
                }
            }
            return untapped;
        }

    }
    
}
