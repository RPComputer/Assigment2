
package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.CreatureImage;
import cardgame.StaticInitializer;
import java.util.Scanner;

public class VolcanicHammer implements Card {
    private static class VolcanicHammerFactory implements CardFactory{
        @Override
        public Card create(){
            return new VolcanicHammer();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new VolcanicHammerFactory());
    
    private class VolcanicHammerEffect extends AbstractCardEffect {
        Player target1 = null;
        CreatureImage target2 = null;
        
        public VolcanicHammerEffect(Player o, Card c){
            super(o, c);
        }
        @Override
        public void resolve () {
            if(target1 == null)
                target2.inflictDamage(3);
            else target1.inflictDamage(3);
        }

        @Override
        public boolean isTargetEffect() {
            return true;
        }

        @Override
        public void setTarget() {
            Scanner reader = CardGame.instance.getScanner();
            int choice;

            do{
                System.out.println("Choose your target: 0 for creatures, 1 for players\n");

                try{
                        choice = reader.nextInt();
                }
                catch (NumberFormatException error) {
                    System.out.println("The input is not valid, try again.\n");
                    choice = -1;
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
                } while(choice != 0 && choice != 1);

                if (choice==0) {
                    // OPPONENT PLAYER'S CREATURES
                    Player opponent = CardGame.instance.getOpponent(owner);
                    int i;
                    do{
                        System.out.println("Choose your opponent player's creatures:\n");
                        i = 0;
                        for ( Creature c: opponent.getCreatures()) {
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
                    } while(choice < 0 || choice >= i);

                    
                    this.target2 = (CreatureImage) opponent.getCreatures().get(choice);

                } else /*choice == 1*/ {
                    // YOUR OWN CREATURES
                    int i;
                    do{
                        System.out.println("Choose your creatures:\n");

                        i = 0;
                        for ( Creature c: owner.getCreatures()) {
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
                    } while(choice < 0 || choice >= i);

                    this.target2 = (CreatureImage) owner.getCreatures().get(choice);

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
                } while(choice != 0 && choice != 1);

                if (choice==0) {
                    // OPPONENT PLAYER

                    this.target1 = CardGame.instance.getOpponent(owner);
                } else /*choice == 1*/ {
                    // CURRENT PLAYER
                    this.target1 = owner;
                }

            }
        }

        @Override
        public Object getTarget() {
            if(target1 == null)
                    return target2;
            return target1;
        }
        
        @Override
        public String toString(){return "VolcanicHammer";}
    }
    
    @Override
    public Effect getEffect(Player p) {
        VolcanicHammerEffect e = new VolcanicHammerEffect(p, this);
        return e;       
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