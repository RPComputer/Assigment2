
package cardgame.cards;

import cardgame.AbstractEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import static cardgame.Interfaccia.acquireInput;
import cardgame.Phases;
import cardgame.SkipPhase;
import cardgame.StaticInitializer;

/*
    Applica il metodo setPhase() creando una nuova SkipPhase() di combat al giocatore scelto come target.
*/

public class FalsePeace implements Card {
    private static class FalsePeaceFactory implements CardFactory{
        @Override
        public Card create(){
            return new FalsePeace();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new FalsePeaceFactory());
    
    private class FalsePeaceEffect extends AbstractEffect {
        Player target = null; // target player 
        
        @Override
        public void resolve () {
            //CardGame.instance.getTriggers().register(Triggers.UNTAP_FILTER, AdversaryTurn);
            target.setPhase(Phases.COMBAT,new SkipPhase(Phases.COMBAT));
        }

        @Override
        public boolean isTargetEffect() {
            return true;
        }

        @Override
        public void setTarget() {
            System.out.println("Choose the player who will skip his next combat phase, 0 for your opponent, 1 for you.");
            System.out.println("\t\t       [0]-->" + CardGame.instance.getCurrentAdversary().name() + "     ||     " + "[1]-->" + CardGame.instance.getCurrentPlayer().name());
            
            int choice;
            
            do{
                choice = acquireInput();
            }while(choice!=0 && choice!=1);
            
            if (choice==0) {
                this.target = CardGame.instance.getCurrentAdversary();
            } else {
                this.target = CardGame.instance.getCurrentPlayer(); 
            }
       
        }

        @Override
        public Object getTarget() {
            return target;
        }
        @Override
        public String toString(){return "FalsePeace";}
    }
    @Override
    public Effect getEffect(Player p) {
        FalsePeaceEffect e = new FalsePeaceEffect();
        return e;
    }
    
    
    @Override
    public String name() { return "FalsePeace"; }
    @Override
    public String type() { return "Sorcery"; }
    @Override
    public String ruleText() { return "Target player skips his next combat phase"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}
