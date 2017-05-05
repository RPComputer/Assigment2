
package cardgame.cards;

import cardgame.AbstractCreature;
import cardgame.AbstractCreatureCardEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.CreatureImage;
import cardgame.Effect;
import cardgame.Enchantment;
import cardgame.Player;
import cardgame.StaticInitializer;
import cardgame.TriggerAction;
import cardgame.Triggers;
import java.util.ArrayList;
import java.util.List;

/*
    Questa creatura utilizza i trigger per potersi nascondere dall'inizio del caricamento dello stack per tutta la durata del caricamento.
    In questo modo non può essere mai scelta come target se non durante la combat phase.
    Sempre tramite l'utilizzo dei trigger, fa pescare una carta ogni volta che viene giocato un incantamento.
*/

public class ArgothianEnchantress implements Card {
    private static class ArgothianEnchantressFactory implements CardFactory{
        @Override
        public Card create(){
            return new ArgothianEnchantress();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new ArgothianEnchantressFactory());
    
    private class ArgothianEnchantressEffect extends AbstractCreatureCardEffect {
        
        public ArgothianEnchantressEffect(Player p, Card c) { super(p,c); }
        @Override
        protected Creature createCreature() {
            ArgothianEnchantressCreature c =  new ArgothianEnchantressCreature (owner);
            CreatureImage cr = new CreatureImage(owner, c);
            c.setHead(cr);
            c.activateATriggers(cr);
            return cr;
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
        public String toString(){return "ArgothianEnchantress";}
    }
    @Override
    public Effect getEffect(Player p) { return new ArgothianEnchantressEffect(p,this); }
    
    
    private class ArgothianEnchantressCreature extends AbstractCreature {
        TriggerAction t1rem;
        TriggerAction t2readd;
        TriggerAction t3draw;
        
        private class HideTrigger implements TriggerAction{
            CreatureImage cr;
            public HideTrigger(CreatureImage c){
                cr = c;
            }
            @Override
            public void execute(Object args) {
                cr.getOwner().getCreatures().remove(cr);
            }
        }
        
        private class UnHideTrigger implements TriggerAction{
            CreatureImage cr;
            public UnHideTrigger(CreatureImage c){
                cr = c;
            }
            @Override
            public void execute(Object args) {
                cr.getOwner().getCreatures().add(cr);
            }
        }
        
        private class ArgothianEnchantressDrawTrigger implements TriggerAction{
            Player owner;
            public ArgothianEnchantressDrawTrigger(Player p){
                this.owner = p;
            }
            @Override
            public void execute(Object args) {
                if(args != null)
                    if(args instanceof Enchantment){
                        Enchantment e = (Enchantment) args;
                        Player p = e.getOwner();
                        if(p == owner)
                            this.owner.draw();
                    }
            }
        }
        
        public ArgothianEnchantressCreature(final Player owner) { 
            super(owner);
        }
        
        public void activateATriggers(CreatureImage c){
            t1rem = new HideTrigger(c);
            t2readd = new UnHideTrigger(c);
            t3draw = new ArgothianEnchantressDrawTrigger(owner);
            CardGame.instance.getTriggers().register(Triggers.STACK_CHARGING_STARTED_EVENT, t1rem);
            CardGame.instance.getTriggers().register(Triggers.STACK_CHARGING_COMPLETED_EVENT, t2readd);
            CardGame.instance.getTriggers().register(Triggers.ENTER_ENCHANTMENT_FILTER, t3draw);
        }
        
        @Override
        public String name() { return "ArgothianEnchantress"; }
        @Override
        public int getPower() { return 0; }
        @Override
        public int getToughness() { return 1; }
        @Override
        public boolean remove(){
            CardGame.instance.getTriggers().deregister(t1rem);
            CardGame.instance.getTriggers().deregister(t2readd);
            CardGame.instance.getTriggers().deregister(t3draw);
            return true;
        }
        @Override
        public List<Effect> effects() { return new ArrayList<>();}
        @Override
        public List<Effect> avaliableEffects() { return new ArrayList<>();}
    }
    
    
    @Override
    public String name() { return "ArgothianEnchantress"; }
    @Override
    public String type() { return "Creature"; }
    @Override
    public String ruleText() { return "Put in play a creature ArgothianEnchantress(0/1)\n\t\t\t\t    Shroud: this creature can't be the target of spells or abilities\n\t\t\t\t    Whenever you cast an enchantment spell, draw a card"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }

}

