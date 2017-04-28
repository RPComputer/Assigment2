
package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.AbstractDecorator;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.CreatureImage;
import cardgame.Effect;
import static cardgame.Interfaccia.acquireInput;
import static cardgame.Interfaccia.showCreatures;
import cardgame.Player;
import cardgame.StaticInitializer;
import cardgame.TriggerAction;
import cardgame.Triggers;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AncestralMask implements Card {
    private static class AncestralMaskFactory implements CardFactory{
        @Override
        public Card create(){
            return new AncestralMask();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer( new AncestralMaskFactory());
    
    private class AncestralMaskEffect extends AbstractCardEffect {
        CreatureImage c;
        Player opponent;
        public AncestralMaskEffect(Player p, Card c) { super(p,c);}
        @Override
        public void resolve() {
            AncestralMaskDecorator d = new AncestralMaskDecorator(c);
            CardGame.instance.getTriggers().trigger(Triggers.ENTER_ENCHANT_CREATURE_ENCHANTMENT_EVENT);
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
                choosen = acquireInput();
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
        @Override
        public String toString(){return "AncestralMask";}
    }
    
    private class AncestralMaskDecoratorTrigger implements TriggerAction{
        AncestralMaskDecorator d;
        public AncestralMaskDecoratorTrigger(AncestralMaskDecorator a){
            d = a;
        }
        @Override
        public void execute(Object args) {
            d.countEnchantedCreatures();
        }
        
    }
    
    private class AncestralMaskDecorator extends AbstractDecorator{
        int counter;
        AncestralMaskDecoratorTrigger t;
        public AncestralMaskDecorator(CreatureImage c) {
            super(c);
            c.setPointer(this);
            this.countEnchantedCreatures();
            t = new AncestralMaskDecoratorTrigger(this);
            CardGame.instance.getTriggers().register(24672, t);
        }
        @Override
        public int getPower(){
            return this.getNext().getPower() + (2 * this.counter);
        }
        
        @Override
        public int getToughness(){
            return this.getNext().getToughness() + (2 * this.counter);
        }
        
        public void countEnchantedCreatures(){
            int result = 0;
            for(Creature c : CardGame.instance.getCurrentAdversary().getCreatures())
                if(c.getDTypes().contains("Enchantment"))
                    result++;
            for(Creature c : CardGame.instance.getCurrentPlayer().getCreatures())
                if(c.getDTypes().contains("Enchantment"))
                    result++;
            this.counter = result-1;
        }
        @Override
        public ArrayList<String> getDTypes(){
            ArrayList<String> r = super.getDTypes();
            r.add("Enchantment");
            return r;
        }
        @Override
        public void removeDecorator(){
            super.removeDecorator();
            CardGame.instance.getTriggers().deregister(t);
            CardGame.instance.getTriggers().trigger(Triggers.EXIT_ENCHANT_CREATURE_ENCHANTMENT_EVENT);
        }
        @Override
        public void deregisterDecorator(){
            CardGame.instance.getTriggers().deregister(t);
            super.deregisterDecorator();
        }
    }
    
    @Override
    public Effect getEffect(Player p) {
        AncestralMaskEffect e = new AncestralMaskEffect(p, this);
        return e;
    }
    
    @Override
    public String name() { return "AncestralMask"; }
    @Override
    public String type() { return "Enchantment"; }
    @Override
    public String ruleText() { return name() + " this card applies +2/+2 for each enchanted creature to selected creature"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
    
}
