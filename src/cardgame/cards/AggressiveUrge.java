
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

public class AggressiveUrge implements Card {
    private static class AggressiveUrgeFactory implements CardFactory{
        @Override
        public Card create(){
            return new AggressiveUrge();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer("AggressiveUrge", new AggressiveUrgeFactory());
    
    private class AggressiveUrgeEffect extends AbstractCardEffect {
        CreatureImage target;        
        public AggressiveUrgeEffect(Player p, Card c, CreatureImage cr) { super(p,c); target = cr;}
        @Override
        public void resolve() {
            AggressiveUrgeDecorator d = new AggressiveUrgeDecorator(target);
        }
    }
    
    private class AggressiveUrgeDecorator extends AbstractDecorator{
        
        public AggressiveUrgeDecorator(CreatureImage c) {
            super(c);
            c.setPointer(this);
            TriggerAction a = new DecoratorTrigger(this);
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
    }
    
    @Override
    public Effect getEffect(Player owner) {
        int choosen;
        Scanner reader = CardGame.instance.getScanner();
        System.out.println("Choose a creature to power up, 0 to see the other player creatures:\n");
        this.showCreatures(owner.getCreatures());
        
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
            CreatureImage c = (CreatureImage) owner.getCreatures().get(choosen);
            return new AggressiveUrgeEffect(owner, this, c);
        }
        else{
            Player opponent = CardGame.instance.getOpponent(owner);
            System.out.println("Choose a creature to power up, 0 to do nothing\n");
            this.showCreatures(opponent.getCreatures());
            
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
                CreatureImage c = (CreatureImage) opponent.getCreatures().get(choosen);
                return new AggressiveUrgeEffect(owner, this, c);
            }
            else return new AggressiveUrgeEffect(owner, this, null);
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
