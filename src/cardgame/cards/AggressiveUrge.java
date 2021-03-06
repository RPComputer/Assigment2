
package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.AbstractDecorator;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.CardGame;
import cardgame.CreatureImage;
import cardgame.DecoratorTrigger;
import cardgame.Effect;
import static cardgame.Interfaccia.acquireInput;
import static cardgame.Interfaccia.showCreatures;
import cardgame.Player;
import cardgame.StaticInitializer;
import cardgame.TriggerAction;
import java.util.ArrayList;

/*
    Questa carta applica un decoratore ad una delle creature attive in campo (scelta come target),
     aumentando sia la difesa che l'attacco di 1 punto.
*/

public class AggressiveUrge implements Card {
    private static class AggressiveUrgeFactory implements CardFactory{
        @Override
        public Card create(){
            return new AggressiveUrge();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new AggressiveUrgeFactory());
    
    private class AggressiveUrgeEffect extends AbstractCardEffect {
        CreatureImage c;
        Player opponent;
        AggressiveUrgeDecorator d;
        public AggressiveUrgeEffect(Player p, Card c) { super(p,c);}
        @Override
        public void resolve() {
            if(c != null){
                d = new AggressiveUrgeDecorator(c);
                System.out.println(c.name() + " has been powered up by AggressiveUrge.");
            }
        }

        @Override
        public boolean isTargetEffect() {
            return true;
        }

        @Override
        public void setTarget() {
            System.out.print("About you: ");
            opponent = CardGame.instance.getOpponent(owner);
            int choosen;
            if(!showCreatures(owner.getCreatures())){
                choosen=0;
            }
            else{
                System.out.println("Choose a creature to power up, 0 to see opponent's creatures:");
                int length = owner.getCreatures().size();
                if(length > 0){
                    do {
                        choosen = acquireInput();
                    }while(choosen<0 || choosen> length);
                }
                else choosen = 0;
            }
            if(choosen > 0){
                CreatureImage cr = (CreatureImage) owner.getCreatures().get(choosen-1);
                this.c = cr;  
            }
            else{
                System.out.print("About your opponent: ");
                if(showCreatures(opponent.getCreatures())){
                    System.out.println("Choose a creature to power up, 0 to do nothing");
                    int length = opponent.getCreatures().size();
                    if(length > 0){
                        do {
                            choosen = acquireInput();
                        }while(choosen<0 || choosen> length);     

                        if(choosen > 0){
                            CreatureImage cr = (CreatureImage) opponent.getCreatures().get(choosen-1);
                            this.c = cr;
                        }
                        else c = null;
                    }
                    else c = null;
                }
            }
        }

        @Override
        public Object getTarget() {
            return c;
        }
        @Override
        public String toString(){return "AggressiveUrge";}
    }
    
    private class AggressiveUrgeDecorator extends AbstractDecorator{
        TriggerAction a;
        public AggressiveUrgeDecorator(CreatureImage c) {
            super(c);
            a = new DecoratorTrigger(this);
            CardGame.instance.getTriggers().register(1024, a);
        }
        @Override
        public int getPower(){
            return this.getNext().getPower() + 1;
        }
        
        @Override
        public int getToughness(){
            return this.getNext().getToughness() + 1;
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
        AggressiveUrgeEffect e = new AggressiveUrgeEffect(p, this);
        return e;
    }
    
    @Override
    public String name() { return "AggressiveUrge"; }
    @Override
    public String type() { return "Instant"; }
    @Override
    public String ruleText() { return name() + " this card applies +1/+1 to selected creature"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return true; }
    
}
