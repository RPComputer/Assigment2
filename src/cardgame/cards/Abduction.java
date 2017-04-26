
package cardgame.cards;

import cardgame.AbstractDecorator;
import cardgame.AbstractEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.CreatureImage;
import cardgame.Effect;
import cardgame.Player;
import cardgame.StaticInitializer;
import java.util.Scanner;

public class Abduction implements Card{
    private static class AbductionFactory implements CardFactory{
        @Override
        public Card create(){
            return new Abduction();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer("Abduction", new AbductionFactory());
    
    private class AbductionEffect extends AbstractEffect {
        CreatureImage c;
        Player opponent;
        Player owner;
        public AbductionEffect(Player p, Player p2, CreatureImage c){
            this.c = c;
            opponent = p2;
            owner = p;
        }
        
        @Override
        public void resolve () {
            owner.getCreatures().add(c);
            opponent.getCreatures().remove(c);
            c.untap();
            AbductionDecorator d = new AbductionDecorator(c, owner, opponent);
        }
    }
    
    private class AbductionDecorator extends AbstractDecorator{
        int damage2;
        Player owner;
        Player exowner;
        public AbductionDecorator(CreatureImage c, Player p1, Player p2) {
            super(c);
            c.setPointer(this);
            damage2 = 0;
            owner = p1;
            exowner = p2;
        }
        @Override
        public void inflictDamage(int d){
            Creature c;
            damage2 += d;
            if(damage2 >= this.getNext().getToughness()){
                c = this.getHead();
                owner.getCreatures().remove(c);
                exowner.getCreatures().add(c);
                AbstractDecorator d1, d2;
                if(this.getPrev() instanceof CreatureImage){
                    CreatureImage c1 = this.getHead();
                    c1.setPointer(this.getNext());
                }
                else{
                    d1 = (AbstractDecorator) this.getPrev();
                    d1.setNext(this.getNext());
                }
                if(this.getNext() instanceof AbstractDecorator){
                    d2 = (AbstractDecorator) this.getNext();
                    d2.setPrev(this.getPrev());
                }
            }
        }
    }
    
    public Effect getEffect(Player p) {
        System.out.println("Choose one creature to enchant:\n");
        Scanner reader = CardGame.instance.getScanner();
        int i = 0, choosen;
        Player opponent;
        if(p.equals(CardGame.instance.getCurrentAdversary()))
            opponent = CardGame.instance.getCurrentPlayer();
        else opponent = CardGame.instance.getCurrentAdversary();
        for(Creature c : opponent.getCreatures()){
            System.out.println(i + ") " + c.name() + " - ");
            i++;
        }
        System.out.println("\n");
        do {
            try{
                choosen = reader.nextInt();
            }catch (NumberFormatException error) {
                System.out.println("The input is not valid, try again.\n");
                choosen = -1;
            }
        }while(choosen < 0 || choosen > opponent.getCreatures().size());
        CreatureImage cr = (CreatureImage) opponent.getCreatures().get(choosen);
        return new AbductionEffect(p, opponent, cr);
    }
    
    
    public String name() { return "Abduction"; }
    public String type() { return "Enchant creature"; }
    public String ruleText() { return "Untap enchanted creature, take control of the creature. When the enchanted creature should die, return it to it's owner"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    public boolean isInstant() { return false; }
}
