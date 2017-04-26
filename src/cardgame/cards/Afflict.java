
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
import cardgame.Player;
import cardgame.StaticInitializer;
import cardgame.TriggerAction;
import java.util.List;
import java.util.Scanner;

public class Afflict implements Card {
    private static class AfflictFactory implements CardFactory{
        @Override
        public Card create(){
            return new Afflict();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer("Afflict", new AfflictFactory());
    
    private class AfflictEffect extends AbstractCardEffect {
        CreatureImage c;
        Player opponent;
        public AfflictEffect(Player p, Card c) { super(p,c);}
        @Override
        public void resolve() {
            AfflictDecorator d = new AfflictDecorator(c);
        }

        @Override
        public boolean isTargetEffect() {
            return true;
        }

        @Override
        public void setTarget() {
            System.out.println("Choose a creature to power up, 0 to see the other player creatures:\n");
            Scanner reader = CardGame.instance.getScanner();
            int choosen;
            
            showCreatures(owner.getCreatures());

            int length = owner.getCreatures().size();

            do {
                try{
                    choosen = reader.nextInt();
                }catch (NumberFormatException error) {
                    System.out.println("The input is not valid, try again.\n");
                    choosen = -1;
                }
            }while(choosen<0 || choosen> length); 

            if(choosen > 0){
                CreatureImage cr = (CreatureImage) owner.getCreatures().get(choosen);
                this.c = cr;  
            }
            else{
                Player opponent = CardGame.instance.getOpponent(owner);
                System.out.println("Choose a creature to power up, 0 to do nothing\n");
                showCreatures(opponent.getCreatures());

                length = opponent.getCreatures().size();

                do {
                    try{
                        choosen = reader.nextInt();
                    }catch (NumberFormatException error) {
                        System.out.println("The input is not valid, try again.\n");
                        choosen = -1;
                    }
                }while(choosen<0 || choosen> length);     

                if(choosen > 0){
                    CreatureImage cr = (CreatureImage) opponent.getCreatures().get(choosen);
                    this.opponent = opponent;
                    this.c = cr;
                }                
            }
        }

        @Override
        public Object getTarget() {
            return c;
        }
    }
    
    private class AfflictDecorator extends AbstractDecorator{
        
        public AfflictDecorator(CreatureImage c) {
            super(c);
            c.setPointer(this);
            TriggerAction a = new DecoratorTrigger(this);
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
    }
    
    @Override
    public Effect getEffect(Player p) {
        AfflictEffect e = new AfflictEffect(p, this);
        e.setTarget();
        return e;
    }
    
    
    private void showCreatures(List<Creature> l){
        int i = 0;
        for( Creature c: l) {
            System.out.println(Integer.toString(i+1)+") " + c.toString()+ "\n");
            ++i;
        }
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
