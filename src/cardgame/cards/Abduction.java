
package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.AbstractDecorator;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.CreatureImage;
import cardgame.Effect;
import static cardgame.Interfaccia.acquireInput;
import static cardgame.Interfaccia.showCreatures;
import cardgame.Player;
import cardgame.StaticInitializer;
import cardgame.Triggers;
import java.util.ArrayList;

/*
    Abduction realizza il suo effetto tramite il decorator applicato alla creatura 
*/

public class Abduction implements Card{
    private static class AbductionFactory implements CardFactory{
        @Override
        public Card create(){
            return new Abduction();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer( new AbductionFactory());
    
    private class AbductionEffect extends AbstractCardEffect {
        CreatureImage c;
        Player opponent;
        public AbductionEffect(Player p, Card c){
            super(p, c);
            this.c = null;
            this.opponent = null;
        }
        
        @Override
        public void resolve () {
            if(c != null){
                owner.getCreatures().add(c);
                opponent.getCreatures().remove(c);
                c.untap();
                AbductionDecorator d = new AbductionDecorator(c, owner, opponent);
                CardGame.instance.getTriggers().trigger(Triggers.ENTER_ENCHANT_CREATURE_ENCHANTMENT_EVENT);
            }
        }

        @Override
        public boolean isTargetEffect() {
            return true;
        }

        @Override
        public void setTarget() {
            System.out.println("Choose one creature to enchant:");
            int i = 0, choosen;
            Player opponent = CardGame.instance.getOpponent(this.owner);
            if(showCreatures(opponent.getCreatures())){
                do {
                    choosen = acquireInput();
                }while(choosen < 0 || choosen > opponent.getCreatures().size());
                if(choosen > 0){
                    CreatureImage cr = (CreatureImage) opponent.getCreatures().get(choosen-1);
                    this.opponent = opponent;
                    this.c = cr;
                }
            }
        }

        @Override
        public Object getTarget() {
            return c;
        }
        @Override
        public String toString(){return "Abduction";}
    }
     
    private class AbductionDecorator extends AbstractDecorator{
        int damage2;
        Player owner;
        Player exowner;
        public AbductionDecorator(CreatureImage c, Player p1, Player p2) {
            super(c);
            c.setPointer(this);
            damage2 = 0;
            owner = p1;
            exowner = p2;
        }
        @Override
        public boolean inflictDamage(int d){
            Creature c;
            damage2 += d;
            if(damage2 >= this.getNext().getToughness()){
                c = this.getHead();
                owner.getCreatures().remove(c);
                exowner.getCreatures().add(c);
                this.removeDecorator();
                CardGame.instance.getTriggers().trigger(Triggers.EXIT_ENCHANT_CREATURE_ENCHANTMENT_EVENT);
            }
            return false;
        }
        
        @Override
        public void resetDamage(){
            super.resetDamage();
            damage2 = 0;
        }
        
        @Override
        public ArrayList<String> getDTypes(){
            ArrayList<String> r = super.getDTypes();
            r.add("Enchantment");
            return r;
        }
        
        @Override
        public void removeDecorator(){
            super.removeDecorator();
            CardGame.instance.getTriggers().trigger(Triggers.EXIT_ENCHANT_CREATURE_ENCHANTMENT_EVENT);
        }
    }
    
    @Override
    public Effect getEffect(Player p) {
        AbductionEffect e = new AbductionEffect(p, this);
        return e;
    }
    
    
    @Override
    public String name() { return "Abduction"; }
    @Override
    public String type() { return "Enchant creature"; }
    @Override
    public String ruleText() { return "Untap enchanted creature, take control of the creature.\n\t\t\t\t When the enchanted creature should die, return it to its owner's control"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }
}
