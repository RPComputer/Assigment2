
package cardgame.cards;

import cardgame.AbstractCardEffect;
import java.util.ArrayList;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import cardgame.StaticInitializer;
import java.util.Scanner;

public class Cancel implements Card {
    private static class CancelFactory implements CardFactory{
        @Override
        public Card create(){
            return new Cancel();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new CancelFactory());
    
    private class CancelEffect extends AbstractCardEffect {
        Effect target = null;
        
        public CancelEffect(Player p, Card c) { super(p,c);}
        
        @Override
        public void resolve () {
            CardGame.instance.getStack().remove(target);
        }

        @Override
        public boolean isTargetEffect() {
            return true;
        }

        @Override
        public void setTarget() {
            ArrayList <Effect> l = new ArrayList<>();
            int i=0;
            int choice;

            for (Effect c: CardGame.instance.getStack()){
                l.add(c);                                 
            }    

            System.out.println("Choose an effect to cancel\n");
            for (Effect e: l){
                System.out.println(i+") " + e.toString() + "\n");
                i++;
            }  

            Scanner s = new Scanner (System.in);
            do{
                try{
                    choice=s.nextInt();
                }
                catch (NumberFormatException error) {
                    System.out.println("The input is not valid, try again.\n");
                    choice=-1;
                }
            }while(choice<0 || choice>i-1);

            target = l.get(choice);
        }

        @Override
        public Object getTarget() {
            return target;
        }
    }
   

    @Override
    public Effect getEffect(Player p) {
        return new CancelEffect(p, this);
    }
    
    
    
    @Override
    public String name() { return "Cancel"; }
    @Override
    public String type() { return "Instant"; }
    @Override
    public String ruleText() { return "Counter target spell"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return true; }
    

}
