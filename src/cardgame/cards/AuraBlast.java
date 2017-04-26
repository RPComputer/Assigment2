
package cardgame.cards;

import cardgame.AbstractCardEffect;
import java.util.ArrayList;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import cardgame.Enchantment;
import cardgame.StaticInitializer;
import java.util.Scanner;

public class AuraBlast implements Card {
    private static class AuraBlastFactory implements CardFactory{
        @Override
        public Card create(){
            return new AuraBlast();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer("AuraBlast", new AuraBlastFactory());
    
    private class AuraBlastEffect extends AbstractCardEffect {
        Enchantment target = null;
        Player to;
        public AuraBlastEffect(Player p, Card c, Player targetOwner, Enchantment t) {
            super(p,c);
            target = t;
            to = targetOwner;
        }
        
        @Override
        public void resolve () {
            to.getEnchantments().remove(target);
            owner.draw();
        }
    }
   

    @Override
    public Effect getEffect(Player p) {
        ArrayList <Effect> l = new ArrayList<>();
        int choice;
        Scanner s = new Scanner (System.in);
       
        System.out.println("Choose your target: 0 for your opponent's enchantments, 1 for yours\n");
            
        do{
            try{
                choice = s.nextInt();
            }
            catch (NumberFormatException error) {
                System.out.println("The input is not valid, try again.\n");
                choice = -1;
            }
        }while(choice!=0 && choice!=1);

        if (choice==0) {
            Player opponent = CardGame.instance.getOpponent(p);
            System.out.println("Your target is one of your opponent's enchantments.\nChoose which one to destroy!\n");
            
            int i = 0;
            for ( Enchantment e: opponent.getEnchantments()) {
                System.out.println( i + ") for " + e.name() + "\n");
                i++;
            }
            
            do{
                try{
                    choice = s.nextInt();
                }
                catch (NumberFormatException error) {
                    System.out.println("The input is not valid, try again.\n");
                    choice = -1;
                }
            } while (choice<0 || choice>i-1);
            
            Enchantment e = opponent.getEnchantments().get(choice);
            return new AuraBlastEffect(p, this, opponent, e);
        }
        else{
            
            System.out.println("Your target is one of your enchantments.\nChoose which one to destroy!\n");
            
            int i = 0;
            for ( Enchantment e: p.getEnchantments()) {
                System.out.println( i + ") for " + e.name() + "\n");
                i++;
            }
            
            do{
                try{
                    choice = s.nextInt();
                }
                catch (NumberFormatException error) {
                    System.out.println("The input is not valid, try again.\n");
                    choice = s.nextInt();
                }
            } while (choice<0 || choice>i-1);
                
            Enchantment e = p.getEnchantments().get(choice);
            return new AuraBlastEffect(p, this, p, e);
        }

        
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
