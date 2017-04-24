
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

        for (Effect c: CardGame.instance.getStack()){
            l.add(c);                                 
        }    

        System.out.println("Choose an effect to cancel");
        for (Effect e: l){
            System.out.println(i+") " + e.toString()); // da cambiare getString in getCardName
            i++;
        }  
        
        Scanner s = new Scanner (System.in);
        try{
            i=s.nextInt();
        }
        catch (NumberFormatException error) {
            System.out.println("The input is not valid, try again.");
            i=s.nextInt();
        }
        
        Effect e = l.get(i);
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
