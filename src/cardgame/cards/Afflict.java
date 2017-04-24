
package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.AbstractDecorator;
import cardgame.Card;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.CreatureImage;
import cardgame.DecoratorTrigger;
import cardgame.Effect;
import cardgame.Player;
import cardgame.TriggerAction;
import java.util.List;
import java.util.Scanner;

public class Afflict implements Card {
    
    private class AfflictEffect extends AbstractCardEffect {
        CreatureImage target;        
        public AfflictEffect(Player p, Card c, CreatureImage cr) { super(p,c); target = cr;}
        @Override
        public void resolve() {
            AfflictDecorator d = new AfflictDecorator(target);
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
    public Effect getEffect(Player owner) {
        int choosen;
        Scanner reader = CardGame.instance.getScanner();
        System.out.println("Choose a creature to afflict, 0 to see the other player creatures:\n");
        this.showCreatures(CardGame.instance.getCurrentAdversary().getCreatures());
        
        do {
            try{
                choosen = reader.nextInt();
            }catch (NumberFormatException error) {
                System.out.println("The input is not valid, try again.\n");
                choosen = -1;
            }
        }while(choosen==-1); 
        
        if(choosen > 0){
            CreatureImage c = (CreatureImage) CardGame.instance.getCurrentAdversary().getCreatures().get(choosen);
            return new AfflictEffect(owner, this, c);
        }
        else{
            System.out.println("Choose a creature to afflict, 0 to do nothing\n");
            this.showCreatures(CardGame.instance.getCurrentPlayer().getCreatures());
            
            do {
                try{
                    choosen = reader.nextInt();
                }catch (NumberFormatException error) {
                    System.out.println("The input is not valid, try again.\n");
                    choosen = -1;
                }
            }while(choosen==-1); 
            
            if(choosen > 0){
                CreatureImage c = (CreatureImage) CardGame.instance.getCurrentAdversary().getCreatures().get(choosen);
                return new AfflictEffect(owner, this, c);
            }
            else return new AfflictEffect(owner, this, null);
        }
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
