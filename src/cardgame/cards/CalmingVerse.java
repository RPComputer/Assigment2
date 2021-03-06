
package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import cardgame.Enchantment;
import cardgame.StaticInitializer;
import java.util.ArrayList;

/*
    Questa classe implementa la carta calming verse che distrugge tutti gli incantamenti dell'avversario, l'effetto
    è implementato direttamente nella risoluzione dello stack.
*/

public class CalmingVerse implements Card {
    private static class CalmingVerseFactory implements CardFactory{
        @Override
        public Card create(){
            return new CalmingVerse();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new CalmingVerseFactory());
    
    private class CalmingVerseEffect extends AbstractCardEffect {
        Player to;
        public CalmingVerseEffect(Player p, Card c, Player targetOwner) {
            super(p,c);
            to = targetOwner;
        }
        
        /*
            Si prende la lista degli incantesimi non controllati dal giocatore owner
            e per ogni elemento si fa destroy() 
        */
        @Override
        public void resolve () {
            ArrayList<Enchantment> l = new ArrayList<>();
            for(Enchantment e : to.getEnchantments())
                l.add(e);
            for(Enchantment e : l)
                to.destroy(e);
        }

        @Override
        public boolean isTargetEffect() {
            return false;
        }

        @Override
        public void setTarget() {
            throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getTarget() {
            throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
        }
        @Override
        public String toString(){return "CalmingVerse";}
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
