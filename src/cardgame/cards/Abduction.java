
package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.AbstractDecorator;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.CreatureImage;
import cardgame.Effect;
import cardgame.Player;
import cardgame.StaticInitializer;
import java.util.ArrayList;
import java.util.Scanner;

public class Abduction implements Card{
    private static class AbductionFactory implements CardFactory{
        @Override
        public Card create(){
            return new Abduction();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer( new AbductionFactory());
    
    private class AbductionEffect extends AbstractCardEffect {
        CreatureImage c;
        Player opponent;
        public AbductionEffect(Player p, Card c){
            super(p, c);
        }
        
        @Override
        public void resolve () {
            owner.getCreatures().add(c);
            opponent.getCreatures().remove(c);
            c.untap();
            AbductionDecorator d = new AbductionDecorator(c, owner, opponent);
        }

        @Override
        public boolean isTargetEffect() {
            return true;
        }

        @Override
        public void setTarget() {
            System.out.println("Choose one creature to enchant:\n");
            Scanner reader = CardGame.instance.getScanner();
            int i = 0, choosen;
            Player opponent = CardGame.instance.getOpponent(this.owner);
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
            this.opponent = opponent;
            this.c = cr;
        }

        @Override
        public Object getTarget() {
            return c;
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
        public boolean inflictDamage(int d){
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
            return false;
        }
        
        @Override
        public ArrayList<String> getDTypes(){
            ArrayList<String> r = super.getDTypes();
            r.add("Enchantment");
            return r;
        }
    }
    
    public Effect getEffect(Player p) {
        AbductionEffect e = new AbductionEffect(p, this);
        e.setTarget();
        return e;
    }
    
    
    public String name() { return "Abduction"; }
    public String type() { return "Enchant creature"; }
    public String ruleText() { return "Untap enchanted creature, take control of the creature. When the enchanted creature should die, return it to its owner's control"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    public boolean isInstant() { return false; }
}
