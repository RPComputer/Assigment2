
package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.Card;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;

public class CalmingVerse implements Card {
    private class CalmingVerseEffect extends AbstractCardEffect {
        Player to;
        public CalmingVerseEffect(Player p, Card c, Player targetOwner) {
            super(p,c);
            to = targetOwner;
        }
        
        @Override
        public void resolve () {
            to.getEnchantments().clear();
        }
    }
   

    @Override
    public Effect getEffect(Player p) {
        return new CalmingVerseEffect(p,this,CardGame.instance.getCurrentAdversary());
    }
    
    
    
    @Override
    public String name() { return "CalmingVerse"; }
    @Override
    public String type() { return "Sorcery"; }
    @Override
    public String ruleText() { return "Destroys all enchantments you don't control"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
    

}
