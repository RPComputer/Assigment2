
package cardgame.cards;

import cardgame.AbstractEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import cardgame.StaticInitializer;
import java.util.Scanner;

public class Deflection implements Card {
    private static class DeflectionFactory implements CardFactory{
        @Override
        public Card create(){
            return new Deflection();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new DeflectionFactory());
    
    private class DeflectionEffect extends AbstractEffect {
        AbstractEffect target;
        
        @Override
        public void resolve () {
            target.setTarget();
        }

        @Override
        public boolean isTargetEffect() {
            return true;
        }

        @Override
        public void setTarget() {
            //da sistemare
            int choice, i = 1, j = 1;
            Scanner s = new Scanner (System.in);
            System.out.println("Choose the effect target to change.\n");
            for(Effect e : CardGame.instance.getStack()){
                if(e.isTargetEffect()){
                    System.out.println(i + ") " + e.toString());
                    j++;
                }
                i++;
            }
            do{
                try{
                    choice = s.nextInt();
                }
                catch (NumberFormatException error) {
                    System.out.println("The input is not valid, try again.\n");
                    choice = -1;
                }
            }while(choice<=0 && choice>j);
            //finire
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
    public String name() { return "Deflection"; }
    @Override
    public String type() { return "Instant"; }
    @Override
    public String ruleText() { return "Change the target of a target spell with a single target"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return true; }
}
