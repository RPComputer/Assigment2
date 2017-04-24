
package cardgame.cards;

import cardgame.AbstractCardEffect;
import java.util.ArrayList;
import cardgame.Card;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import java.util.Scanner;

public class Cancel implements Card {
    private class CancelEffect extends AbstractCardEffect {
        Effect target = null;
        
        public CancelEffect(Player p, Card c, Effect t) { super(p,c); target = t;}
        
        @Override
        public void resolve () {
            CardGame.instance.getStack().remove(target);
        }
    }
   

    @Override
    public Effect getEffect(Player p) {
        ArrayList <Effect> l = new ArrayList<>();
        int i=0;
        int choice;

        for (Effect c: CardGame.instance.getStack()){
            l.add(c);                                 
        }    

        System.out.println("Choose an effect to cancel\n");
        for (Effect e: l){
            System.out.println(i+") " + e.toString() + "\n"); // da cambiare getString in getCardName
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
        
        Effect e = l.get(choice);
        return (Effect) new CancelEffect(p, this, e);
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
