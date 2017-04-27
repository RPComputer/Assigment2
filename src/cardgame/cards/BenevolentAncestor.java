
package cardgame.cards;

import cardgame.AbstractCreature;
import cardgame.AbstractCreatureCardEffect;
import cardgame.AbstractDecorator;
import cardgame.AbstractEffect;
import cardgame.AbstractPlayerDamageModificator;
import cardgame.Card;
import cardgame.Creature;
import cardgame.CreatureImage;
import cardgame.Effect;
import cardgame.Player;
import cardgame.TriggerAction;
import java.util.List;
import cardgame.CardGame;
import cardgame.DecoratorTrigger;
import cardgame.Triggers;
import java.util.ArrayList;
import java.util.Scanner;

public class BenevolentAncestor implements Card {
    
    private class BenevolentAncestorEffect extends AbstractCreatureCardEffect {
        public BenevolentAncestorEffect(Player p, Card c) { super(p,c); }
        @Override
        protected Creature createCreature() {
            BenevolentAncestorCreature c =  new BenevolentAncestorCreature(owner);
            CreatureImage cr =  new CreatureImage(owner, c);
            c.setCreature(cr);
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
    }
    @Override
    public Effect getEffect(Player p) { return new BenevolentAncestorEffect(p,this); }
    
    
    private class BenevolentAncestorCreature extends AbstractCreature {
        private CreatureImage me;
        public BenevolentAncestorCreature(Player owner) { 
            super(owner);
        }   
        
        private void setCreature(CreatureImage c){
            this.me = c;
        }
        
        private class BenevolentAncestorDecorator extends AbstractDecorator{
            
            public BenevolentAncestorDecorator(CreatureImage c) {
                super(c);
                c.setPointer(this);
                TriggerAction a = new DecoratorTrigger(this);
                CardGame.instance.getTriggers().register(Triggers.END_TURN_FILTER, a);
            }
            @Override
            public void inflictDamage(int pts){
                super.inflictDamage(pts-1);
            }
        }
        
        private class BenevolentAncestorDamageModifier extends AbstractPlayerDamageModificator{
            private class BenevolentAncestorDamageModifierTrigger implements TriggerAction{
                BenevolentAncestorDamageModifier m;
                public BenevolentAncestorDamageModifierTrigger(BenevolentAncestorDamageModifier m){
                    this.m = m;
                }
                @Override
                public void execute(Object args) {
                    m.remove();
                    CardGame.instance.getTriggers().deregister(this);
                }
            }
            
            public BenevolentAncestorDamageModifier(){
                BenevolentAncestorDamageModifierTrigger t = new BenevolentAncestorDamageModifierTrigger(this);
                CardGame.instance.getTriggers().register(Triggers.END_TURN_FILTER, t);
            }
            
            @Override
            public void inflictDamage(int p){
                super.inflictDamage(p-1);
            }
        }
        
        private class BenevolentAncestorCreatureEffect extends AbstractEffect{
            Player target1 = null;
            CreatureImage target2 = null;
            Player owner;
            CreatureImage me;
            
            public BenevolentAncestorCreatureEffect(Player o, CreatureImage c){this.owner = o; this.me = c;}
            
            @Override
            public void resolve() {
                BenevolentAncestorDecorator d;
                BenevolentAncestorDamageModifier m;
                if(target1 == null)
                    d = new BenevolentAncestorDecorator(target2);
                else{
                    m = new BenevolentAncestorDamageModifier();
                    target1.addModificator(m);
                }
            }

            @Override
            public boolean isTargetEffect() {
                return true;
            }

            @Override
            public void setTarget() {
                me.tap();
                Scanner reader = CardGame.instance.getScanner();
                int choice;
                do{
                    System.out.println("Choose who you want to protect: 0 for creature 1 for player:\n");

                    try{
                            choice = reader.nextInt();
                    }
                    catch (NumberFormatException error) {
                        System.out.println("The input is not valid, try again.\n");
                        choice = -1;
                    }

                } while(choice != 0 && choice != 1);
                if(choice == 0){
                    int i;
                    do{
                        System.out.println("Choose your creature, 0 for opponent's creatures:\n");
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
                    if(choice >0){
                        target2 = (CreatureImage) owner.getCreatures().get(choice);
                    }
                    else{
                        Player opponent = CardGame.instance.getOpponent(owner);
                        do{
                            System.out.println("Choose opponent's creature:\n");
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

                        } while(choice <= 0 || choice >= i);
                        target2 = (CreatureImage) opponent.getCreatures().get(choice);
                    }
                }
                else{
                    System.out.println("Defaut target is yourself, 0 to confirm 1 to set other player\n");
                    do{                      
                        try{
                                choice = reader.nextInt();
                        }
                        catch (NumberFormatException error) {
                            System.out.println("The input is not valid, try again.\n");
                            choice = -1;
                        }

                    } while(choice != 0 && choice != 1);
                    if(choice == 0)
                        this.target1 = owner;
                    else this.target1 = CardGame.instance.getOpponent(owner);
                }
            }

            @Override
            public Object getTarget() {
                if(target1 == null)
                    return target2;
                return target1;
            }
            
        }
        
        @Override
        public String name() { return "BenevolentAncestor"; }
        
        @Override
        public int getPower() { return 0; }
        @Override
        public int getToughness() { return 4; }

        @Override
        public List<Effect> effects() {
            ArrayList<Effect> e = new ArrayList();            
            e.add(new BenevolentAncestorCreatureEffect(owner, me));
            return e;
        }
        @Override
        public List<Effect> avaliableEffects() {
            ArrayList<Effect> e = new ArrayList();
            if(!this.isTapped()){
                e.add(new BenevolentAncestorCreatureEffect(owner, me));
            }
            return e;
        }
    }
    
    
    @Override
    public String name() { return "BenevolentAncestor"; }
    @Override
    public String type() { return "Creature"; }
    @Override
    public String ruleText() { return "Put in play BenevolentAncestor(0/4), this creature can't attack\n with tap: prevent 1 damage to a creature or player until the end of the turn\n"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return false; }

}
