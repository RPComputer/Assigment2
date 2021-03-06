
package cardgame.cards;

import cardgame.AbstractCardEffect;
import java.util.ArrayList;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import cardgame.Enchantment;
import static cardgame.Interfaccia.acquireInput;
import cardgame.StaticInitializer;

/*
    Questa classe implementa la carta Aura Blast che elimina un incantamento e fa pescare una carta al giocatore
    che la lancia. I due metodi importanti di questa classe sono il resolve dell'effetto e il setTarget.
*/

public class AuraBlast implements Card {
    private static class AuraBlastFactory implements CardFactory{
        @Override
        public Card create(){
            return new AuraBlast();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new AuraBlastFactory());
    
    private class AuraBlastEffect extends AbstractCardEffect {
        Enchantment target = null;
        Player to;
        public AuraBlastEffect(Player p, Card c) {
            super(p,c);
        }
        
        @Override
        public void resolve () {
            if(target != null){
                to.destroy(target);
                owner.draw();
            }
        }

        @Override
        public boolean isTargetEffect() {
            return true;
        }

        @Override
        public void setTarget() {
            System.out.println("Choose your target: 0 for your opponent's enchantments, 1 for yours");
            int choice;
            ArrayList <Effect> l = new ArrayList<>();

            do{
                choice = acquireInput();
            }while(choice!=0 && choice!=1);

            if (choice==0) {
                Player opponent = CardGame.instance.getOpponent(owner);
                System.out.println("Your target is one of your opponent's enchantments.");

                int i = 0;
                for ( Enchantment e: opponent.getEnchantments()) {
                    System.out.println( i + ") " + e.name());
                    i++;
                }
                if(i>0){
                    System.out.println("Choose which one to destroy!");
                    do{
                        choice = acquireInput();
                    } while (choice<0 || choice>i-1);
                    Enchantment e = opponent.getEnchantments().get(choice);
                    this.target = e;
                    this.to = opponent;
                }
                else {
                    System.out.println("There are no enchantments to destroy.");
                    this.target = null;
                }
            }
            else{
                System.out.println("Your target is one of your enchantments.");
                int i = 0;
                for ( Enchantment e: owner.getEnchantments()) {
                    System.out.println( i + ") " + e.name());
                    i++;
                }
                if(i>0){
                    System.out.println("Choose which one to destroy!");
                    do{
                        choice = acquireInput();
                    } while (choice<0 || choice>i-1);
                    Enchantment e = owner.getEnchantments().get(choice);
                    this.target = e;
                    this.to = owner;
                }
                else {
                    System.out.println("There aren't enchantments to destroy.");
                    this.target = null;
                }
            }
        }

        @Override
        public Object getTarget() {
            return target;
        }
        @Override
        public String toString(){return "AuraBlast";}
    }
   

    @Override
    public Effect getEffect(Player p) {
        AuraBlastEffect e = new AuraBlastEffect(p, this);
        return e;  
    }
    

    @Override
    public String name() { return "AuraBlast"; }
    @Override
    public String type() { return "Instant"; }
    @Override
    public String ruleText() { return "Destroys target enchantment and draws a card"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return true; }
    

}
