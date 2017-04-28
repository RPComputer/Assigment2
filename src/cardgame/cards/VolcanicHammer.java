
package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.CreatureImage;
import static cardgame.Interfaccia.acquireInput;
import static cardgame.Interfaccia.showCreatures;
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
            if(target1 == null){
                if(target2 != null)
                    target2.inflictDamage(3);
            }
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
            
            System.out.println("Choose your target: 0 for creatures, 1 for players\n");
            do{
               choice = acquireInput();
            } while(choice != 0 && choice != 1);

            if (choice==0) {
                // CREATURES
                CreatureImage target = null;
                
                System.out.println("Choose your target: 0 for your opponent's creatures, 1 for yours\n");
                do{
                    choice = acquireInput();
                } while(choice != 0 && choice != 1);

                if (choice==0) {
                    // OPPONENT PLAYER'S CREATURES
                    Player opponent = CardGame.instance.getOpponent(owner);
                    System.out.println("Choose your opponent player's creatures:\n");
                    
                    boolean foo = showCreatures(opponent.getCreatures());
                    int length = opponent.getCreatures().size();
                    if(foo){
                        do{
                            choice = acquireInput();
                        } while(choice < 0 || choice > length);


                        this.target2 = (CreatureImage) opponent.getCreatures().get(choice);
                    }
                    else target2 = null;

                } else /*choice == 1*/ {
                    // YOUR OWN CREATURES
                    System.out.println("Choose your creatures:\n");
                    boolean foo = showCreatures(owner.getCreatures());
                    int length = owner.getCreatures().size();
                    if(foo){
                        do{
                            choice = acquireInput();
                        } while(choice < 0 || choice > length);


                        this.target2 = (CreatureImage) owner.getCreatures().get(choice);
                    }
                    else target2 = null;
                }

            } else /*choice == 1*/ {
                // PLAYERS
                
                System.out.println("Choose your target: 0 for your opponent, 1 for yourself\n");
                do{
                    choice = acquireInput();
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
    public String type() { return "Sorcery"; }
    @Override
    public String ruleText() { return "Deals 3 damage to each creature or player"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}