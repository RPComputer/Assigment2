
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

public class Deflection implements Card {
    private static class DeflectionFactory implements CardFactory{
        @Override
        public Card create(){
            return new Deflection();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new DeflectionFactory());
    
    private class DeflectionEffect extends AbstractEffect {
        Effect target;
        
        @Override
        public void resolve () {
            if(target != null)
                target.setTarget();
            else{
                System.out.println("Void deflection effect");
            }
        }

        @Override
        public boolean isTargetEffect() {
            return true;
        }

        @Override
        public void setTarget() {
            int choice, i = 1, j=0;
            ArrayList<Effect> effects = new ArrayList();
            System.out.println("Choose the effect target to change.\n");
            for(Effect e : CardGame.instance.getStack()){
                if(e.isTargetEffect())
                    effects.add(e);                
            }
            for(Effect e : effects){
                System.out.println(i + ") " + e.toString() + "  ");
                i++;
                j=1;
            }
            System.out.println("\n");
            if(j>0){
                do{
                    choice = acquireInput();
                }while(choice<=0 && choice>i);
                Effect e = effects.get(choice);
                target = e;
            }
            else target = null;
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
