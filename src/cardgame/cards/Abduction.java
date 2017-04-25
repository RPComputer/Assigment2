
package cardgame.cards;

import cardgame.AbstractEffect;
import cardgame.CardGame;
import cardgame.CreatureImage;
import cardgame.Effect;
import cardgame.Player;

public class Abduction {
    private class AbductionEffect extends AbstractEffect {
        CreatureImage c;
        Player opponent;
        public AbductionEffect(Player p, CreatureImage c){
            this.c = c;
            opponent = p;
        }
        
        @Override
        public void resolve () {
            
        }
    }
    public Effect getEffect(Player p) {
        System.out.println("Choose one creature to enchant:\n");
        
        return new AbductionEffect();
    }
    
    
    public String name() { return "Abduction"; }
    public String type() { return "Enchant creature"; }
    public String ruleText() { return "Untap enchanted creature, take control of the creature. When the enchanted creature should die, return it to it's owner"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    public boolean isInstant() { return false; }
}
