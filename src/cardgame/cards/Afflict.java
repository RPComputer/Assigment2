
package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.AbstractDecorator;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.CreatureImage;
import cardgame.DecoratorTrigger;
import cardgame.Effect;
import static cardgame.Interfaccia.acquireInput;
import static cardgame.Interfaccia.showCreatures;
import cardgame.Player;
import cardgame.StaticInitializer;
import cardgame.TriggerAction;
import java.util.ArrayList;
import java.util.List;

public class Afflict implements Card {
    private static class AfflictFactory implements CardFactory{
        @Override
        public Card create(){
            return new Afflict();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer( new AfflictFactory());
    
    private class AfflictEffect extends AbstractCardEffect {
        CreatureImage c;
        Player opponent;
        public AfflictEffect(Player p, Card c) { super(p,c);}
        @Override
        public void resolve() {
            if(c != null){
                AfflictDecorator d = new AfflictDecorator(c);
            }
        }

        @Override
        public boolean isTargetEffect() {
            return true;
        }

        @Override
        public void setTarget() {
            System.out.println("Choose a creature to afflict, 0 to see the other player creatures:\n");
            
            int choosen;
            
            boolean foo = showCreatures(owner.getCreatures());

            int length = owner.getCreatures().size();
            if(foo){
                do {
                    choosen = acquireInput();
                }while(choosen<0 || choosen> length);
            }
            else choosen = 0;
            if(choosen > 0){
                CreatureImage cr = (CreatureImage) owner.getCreatures().get(choosen);
                this.c = cr;  
            }
            else{
                Player opponent = CardGame.instance.getOpponent(owner);
                System.out.println("Choose a creature to afflict, 0 to do nothing\n");
                foo = showCreatures(opponent.getCreatures());

                length = opponent.getCreatures().size();
                if(foo){
                    do {
                        choosen = acquireInput();
                    }while(choosen<0 || choosen> length);     

                    if(choosen > 0){
                        CreatureImage cr = (CreatureImage) opponent.getCreatures().get(choosen);
                        this.opponent = opponent;
                        this.c = cr;
                    }
                }
                else c = null;
            }
            
        }

        @Override
        public Object getTarget() {
            return c;
        }
        @Override
        public String toString(){return "Afflict";}
    }
    
    private class AfflictDecorator extends AbstractDecorator{
        TriggerAction a;
        public AfflictDecorator(CreatureImage c) {
            super(c);
            c.setPointer(this);
            a = new DecoratorTrigger(this);
            CardGame.instance.getTriggers().register(1024, a);
        }
        @Override
        public int getPower(){
            return this.getNext().getPower() - 1;
        }
        
        @Override
        public int getToughness(){
            return this.getNext().getToughness() - 1;
        }
        
        @Override
        public ArrayList<String> getDTypes(){
            ArrayList<String> r = super.getDTypes();
            r.add("Instant");
            return r;
        }
        
        @Override
        public void removeDecorator(){
            super.removeDecorator();
            CardGame.instance.getTriggers().deregister(a);
        }
        
        @Override
        public void deregisterDecorator(){
            super.deregisterDecorator();
            CardGame.instance.getTriggers().deregister(a);
        }
    }
    
    @Override
    public Effect getEffect(Player p) {
        AfflictEffect e = new AfflictEffect(p, this);
        return e;
    }
    
    @Override
    public String name() { return "Afflict"; }
    @Override
    public String type() { return "Instant"; }
    @Override
    public String ruleText() { return name() + " this card applies -1/-1 to selected creature"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return true; }
    
    
}
