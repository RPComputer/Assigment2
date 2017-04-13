
package cardgame.cards;

import cardgame.AbstractEffect;
import cardgame.Card;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import cardgame.Creature;
import java.util.Scanner;

public class VolcanicHammer implements Card {
    private class VolcanicHammerEffect extends AbstractEffect {
        
        @Override
        public void resolve () {
            for(Creature a : CardGame.instance.getCurrentPlayer().getCreatures()){
                a.inflictDamage(3);
            }

            for(Creature a : CardGame.instance.getCurrentAdversary().getCreatures()){
                a.inflictDamage(3);
            }
        }
    }
    @Override
    public Effect getEffect(Player p) {
        int choice;
        Scanner reader = CardGame.instance.getScanner();
        do{
            System.out.println("Choose your target: 0 for creatures, 1 for players");
            choice = reader.nextInt();
            
            if (choice != 0 && choice!= 1) {
                System.out.println("Not valid input!");
            }
            
            
        } while(choice == 0 || choice == 1);
        
        if (choice==0) {
            // CREATURES
            
            /* TODO */
        } else /*choice == 1*/ {
            // PLAYERS
            do{
                System.out.println("Choose your target: 0 for your opponent, 1 for yourself");
                choice = reader.nextInt();
            } while(choice == 0 || choice == 1);
            
            if (choice==0) {
                CardGame.instance.getCurrentAdversary().inflictDamage(3);
            } else /*choice == 1*/ {
                CardGame.instance.getCurrentPlayer().inflictDamage(3);
            }
            
        }
        
        return new VolcanicHammerEffect(); }
    
    
    @Override
    public String name() { return "Volcanic Hammer"; }
    @Override
    public String type() { return "Enchantment"; }
    @Override
    public String ruleText() { return "Deals 3 damage to each creature or player"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}
