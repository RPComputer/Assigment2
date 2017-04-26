
package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.AbstractEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import cardgame.CardStack;
import cardgame.Enchantment;
import cardgame.SkipPhase;
import cardgame.StaticInitializer;
import cardgame.TriggerAction;
import cardgame.Triggers;
import java.util.Scanner;

public class Deflection implements Card {
    private static class DeflectionFactory implements CardFactory{
        @Override
        public Card create(){
            return new Deflection();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer("Deflection", new DeflectionFactory());
    
    private class DeflectionEffect extends AbstractEffect {
        AbstractEffect target;
        
        @Override
        public void resolve () {
            Scanner reader = CardGame.instance.getScanner();
            int i=0;
            for(Effect e : CardGame.instance.getStack()){
                if()
            }
            System.out.print("Choose a spell to change it's target : ");
            int r = reader.nextInt();
            CardStack stack = CardGame.instance.getStack();
            
            
        }

        @Override
        public boolean isTargetEffect() {
            return true;
        }

        @Override
        public void setTarget() {
            System.out.println("Choose the player who will skip his next combat phase, 0 for the first, 1 for second.\n");
            System.out.println(CardGame.instance.getCurrentPlayer().name() + "   " + CardGame.instance.getCurrentAdversary().name());
            
            int choice;
            Scanner s = new Scanner (System.in);
            
            do{
                try{
                    choice = s.nextInt();
                }
                catch (NumberFormatException error) {
                    System.out.println("The input is not valid, try again.\n");
                    choice = -1;
                }
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
    }
    @Override
    public Effect getEffect(Player p) {
        DeflectionEffect e = new DeflectionEffect();
        e.setTarget();
        return e;
    }
    
    
    @Override
    public String name() { return "Fatigue"; }
    @Override
    public String type() { return "Enchantment"; }
    @Override
    public String ruleText() { return "Target player skips his next draw step"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}

