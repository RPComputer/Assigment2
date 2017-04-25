
package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.Card;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.CreatureImage;
import java.util.Scanner;

public class VolcanicHammer implements Card {
    private class VolcanicHammerEffect extends AbstractCardEffect {
        Player target1 = null;
        CreatureImage target2 = null;
        
        public VolcanicHammerEffect(Player o, Card c, Object t){
            super(o, c);
            if(t instanceof Player)
                target1 = (Player) t;
            else
                target2 = (CreatureImage) t;
        }
        @Override
        public void resolve () {
            if(target1 == null)
                target2.inflictDamage(3);
            else target1.inflictDamage(3);
        }
    }
    @Override
    public Effect getEffect(Player p) {
        // INIZIO ALGORITMO
        int choice;
        Scanner reader = CardGame.instance.getScanner();
        do{
            System.out.println("Choose your target: 0 for creatures, 1 for players\n");
            
            try{
                    choice = reader.nextInt();
            }
            catch (NumberFormatException error) {
                System.out.println("The input is not valid, try again.\n");
                choice = -1;
            }
            
            if (choice != 0 && choice!= 1) {
                System.out.println("The input is not valid, try again.\n");
            }
            
        } while(choice != 0 && choice != 1);
        
        if (choice==0) {
            // CREATURES
            CreatureImage target = null;
            do{
                System.out.println("Choose your target: 0 for your opponent's creatures, 1 for yours\n");
                
                try{
                    choice = reader.nextInt();
                }
                catch (NumberFormatException error) {
                    System.out.println("The input is not valid, try again.\n");
                    choice = -1;
                }
                
                if (choice != 0 && choice!= 1) {
                    System.out.println("The input is not valid, try again.\n");
                }
                
            } while(choice != 0 && choice != 1);
            
            if (choice==0) {
                // OPPONENT PLAYER'S CREATURES
                int i;
                do{
                    System.out.println("Choose your opponent player's creatures:\n");
                    i = 0;
                    for ( Creature c: CardGame.instance.getCurrentAdversary().getCreatures()) {
                        System.out.println( i + ") for " + c.name() + "\n");
                        i++;
                    }
                    try{
                        choice = reader.nextInt();
                    }
                    catch (NumberFormatException error) {
                        System.out.println("The input is not valid, try again.\n");
                        choice = -1;
                    }

                    if (choice < 0 || choice >= i) {
                        System.out.println("The input is not valid, try again.\n");
                    }

                } while(choice < 0 || choice >= i);
                
                CreatureImage creatureTarget = (CreatureImage) CardGame.instance.getCurrentAdversary().getCreatures().get(choice);
                
                return new VolcanicHammerEffect(p, this, creatureTarget);
                
            } else /*choice == 1*/ {
                // YOUR OWN CREATURES
                int i;
                do{
                    System.out.println("Choose your creatures:\n");
                    
                    i = 0;
                    for ( Creature c: CardGame.instance.getCurrentPlayer().getCreatures()) {
                        System.out.println( i + ") for " + c.name() + "\n");
                        i++;
                    }
                    
                    try{
                        choice = reader.nextInt();
                    }
                    
                    catch (NumberFormatException error) {
                        System.out.println("The input is not valid, try again.\n");
                        choice = -1;
                    }

                    if (choice < 0 || choice >= i) {
                        System.out.println("The input is not valid, try again.\n");
                    }

                } while(choice < 0 || choice >= i);
                
                CreatureImage creatureTarget = (CreatureImage) CardGame.instance.getCurrentPlayer().getCreatures().get(choice);
                
                return new VolcanicHammerEffect(p, this, creatureTarget);
   
            }
            
        } else /*choice == 1*/ {
            // PLAYERS
            do{
                System.out.println("Choose your target: 0 for your opponent, 1 for yourself\n");
                
                try{
                    choice = reader.nextInt();
                }
                catch (NumberFormatException error) {
                    System.out.println("The input is not valid, try again.\n");
                    choice = -1;
                }
                
                if (choice != 0 && choice!= 1) {
                    System.out.println("The input is not valid, try again.\n");
                }
                
            } while(choice != 0 && choice != 1);
            
            if (choice==0) {
                // OPPONENT PLAYER
                
                Player playerTarget = CardGame.instance.getCurrentAdversary();
               
                return new VolcanicHammerEffect(p, this, playerTarget);
            } else /*choice == 1*/ {
                // CURRENT PLAYER
                
                Player playerTarget = CardGame.instance.getCurrentPlayer();
                
                return new VolcanicHammerEffect(p, this, playerTarget);
            }
            
        }
    }
    
    
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