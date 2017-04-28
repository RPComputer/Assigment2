
package cardgame.cards;

import cardgame.AbstractEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import static cardgame.Interfaccia.acquireInput;
import java.util.Scanner;
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
    private static StaticInitializer initializer = new StaticInitializer(new FalsePeaceFactory());
    
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
            CardGame.instance.getTriggers().register(Triggers.UNTAP_FILTER, AdversaryTurn);
        }

        @Override
        public boolean isTargetEffect() {
            return true;
        }

        public void setTarget() {
            System.out.println("Choose the player who will skip his next combat phase, 0 for the first, 1 for second.\n");
            System.out.println(CardGame.instance.getCurrentPlayer().name() + "   " + CardGame.instance.getCurrentAdversary().name());
            
            int choice;
            Scanner s = new Scanner (System.in);
            
            do{
                choice = acquireInput();
            }while(choice!=0 && choice!=1);
            
            if (choice==0) {
                this.target = CardGame.instance.getCurrentPlayer();
            } else {
                this.target = CardGame.instance.getCurrentAdversary(); 
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
    public String type() { return "Enchantment"; }
    @Override
    public String ruleText() { return "Target player skips his next combat phase"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}
