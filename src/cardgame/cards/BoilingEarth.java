
package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.StaticInitializer;
import java.util.ArrayList;

/*
    Questa classe implementa la carta boiling earth che infligge 1 danno a tutte le sue creature con il suo effetto.
*/

public class BoilingEarth implements Card {
    private static class BoilingEarthFactory implements CardFactory{
        @Override
        public Card create(){
            return new BoilingEarth();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new BoilingEarthFactory());
    
    private class BoilingEarthEffect extends AbstractCardEffect {
        
        public BoilingEarthEffect(Player p, Card c) { super(p,c);}
        
        /*
            Si prendono le liste delle creature in campo dei due giocatori 
            e si usa il metodo inflictDamage per infliggere un danno a ogni creatura.
        */
        @Override
        public void resolve () {
            ArrayList<Creature> l1 = new ArrayList<>();
            ArrayList<Creature> l2 = new ArrayList<>();
            for(Creature a : CardGame.instance.getCurrentPlayer().getCreatures()){
                l1.add(a);
            }

            for(Creature a : CardGame.instance.getCurrentAdversary().getCreatures()){
                l2.add(a);
            }
            for(Creature a : l1){
                a.inflictDamage(1);
                System.out.println("Boiling Earth inflicted 1 damage to: " + a.name() + ".");
            }

            for(Creature a : l2){
                a.inflictDamage(1);
                System.out.println("Boiling Earth inflicted 1 damage to: " + a.name() + ".");
            }
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
        public String toString(){return "BoilingEarth";}
    }
    @Override
    public Effect getEffect(Player p) { return new BoilingEarthEffect(p, this); }
    
    
    @Override
    public String name() { return "BoilingEarth"; }
    @Override
    public String type() { return "Sorcery"; }
    @Override
    public String ruleText() { return "Deals 1 damage to each creature"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}
