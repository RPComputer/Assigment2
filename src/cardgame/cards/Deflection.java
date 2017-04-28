
package cardgame.cards;

import cardgame.AbstractEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import static cardgame.Interfaccia.acquireInput;
import cardgame.StaticInitializer;
import java.util.ArrayList;
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
            int choice, i = 1;
            ArrayList<Effect> effects = new ArrayList();
            Scanner s = new Scanner (System.in);
            System.out.println("Choose the effect target to change.\n");
            for(Effect e : CardGame.instance.getStack()){
                if(e.isTargetEffect())
                    effects.add(e);                
            }
            for(Effect e : effects){
                System.out.println(i + ") " + e.toString() + "  ");
                i++;
            }
            System.out.println("\n");
            do{
                choice = acquireInput();
            }while(choice<=0 && choice>i);
            Effect e = effects.get(choice);
            e.setTarget();
        }

        @Override
        public Object getTarget() {
            return target;
        }
        @Override
        public String toString(){return "Deflection";}
    }
    @Override
    public Effect getEffect(Player p) {
        DeflectionEffect e = new DeflectionEffect();
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
