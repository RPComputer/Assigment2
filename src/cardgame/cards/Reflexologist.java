/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardgame.cards;

import cardgame.AbstractCreature;
import cardgame.AbstractCreatureCardEffect;
import cardgame.Card;
import cardgame.Creature;
import cardgame.CreatureImage;
import cardgame.Effect;
import cardgame.Player;
import java.util.List;

/**
 *
 * @author atorsell
 */
public class Reflexologist implements Card {
    
    private class ReflexologistEffect extends AbstractCreatureCardEffect {
        public ReflexologistEffect(Player p, Card c) { super(p,c); }
        @Override
        protected Creature createCreature() {
            Creature c =  new ReflexologistCreature(owner);
            return new CreatureImage(owner, c);
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
    }
    @Override
    public Effect getEffect(Player p) { return new ReflexologistEffect(p,this); }
    
    
    private class ReflexologistCreature extends AbstractCreature {
        
        ReflexologistCreature(Player owner) { 
            super(owner);
        }   
        
        @Override
        public String name() { return "Reflexologist"; }
        
        @Override
        public void attack() {}
        @Override
        public void defend(Creature c) {}
        @Override
        public int getPower() { return 0; }
        @Override
        public int getToughness() { return 1; }

        @Override
        public List<Effect> effects() { return null; }
        @Override
        public List<Effect> avaliableEffects() { return null; }
    }
    
    
    @Override
    public String name() { return "Reflexologist"; }
    @Override
    public String type() { return "Creature"; }
    @Override
    public String ruleText() { return "Put in play a creature Reflexologist(0/1) with tap: Reflexology does nothing"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }

}
