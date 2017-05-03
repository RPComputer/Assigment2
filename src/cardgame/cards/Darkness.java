
package cardgame.cards;

import cardgame.AbstractCardEffect;
import cardgame.Card;
import cardgame.CardFactory;
import cardgame.Effect;
import cardgame.Player;
import cardgame.Triggers;
import cardgame.CardGame;
import cardgame.Creature;
import cardgame.DefaultCombatPhase;
import cardgame.Phases;
import cardgame.StaticInitializer;
import cardgame.TriggerAction;
import java.util.ArrayList;

public class Darkness implements Card {
    private static class DarknessFactory implements CardFactory{
        @Override
        public Card create(){
            return new Darkness();
        }
    }
    private static StaticInitializer initializer = new StaticInitializer(new DarknessFactory());
    
    private class DarknessEffect extends AbstractCardEffect {
        public DarknessEffect(Player p,Card c){ super (p,c); }
        
        private class DarknessTrigger implements TriggerAction{
            
            @Override
            public void execute(Object args) {
                DefaultCombatPhase c = (DefaultCombatPhase) CardGame.instance.getCurrentPlayer().getPhase(Phases.COMBAT);
                c.setCreaturesWhichAttacked(new ArrayList<Creature>());
            }
            
        }
        
        private class DeleteDarknessTrigger implements TriggerAction{
            DarknessTrigger t;
            public DeleteDarknessTrigger(DarknessTrigger t){
                this.t = t;
            }
            @Override
            public void execute(Object args) {
                CardGame.instance.getTriggers().deregister(t);
                CardGame.instance.getTriggers().deregister(this);
            }
        }
        
        @Override
        public void resolve() {
            DarknessTrigger t = new DarknessTrigger();
            DeleteDarknessTrigger t1 = new DeleteDarknessTrigger(t);
            CardGame.instance.getTriggers().register(Triggers.STACK_CHARGING_COMPLETED_EVENT, t);
            CardGame.instance.getTriggers().register(Triggers.END_TURN_FILTER, t1);
            
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
        public String toString(){return "Darkness";}
    }
    
    @Override
    public Effect getEffect(Player owner) {
        return new DarknessEffect(owner,this);
    }
    
    
    @Override
    public String name() { return "Darkness"; }
    @Override
    public String type() { return "Instant"; }
    @Override
    public String ruleText() { return name() + " prevent all combat damage that would be dealt this turn"; }
    @Override
    public String toString() { return name() + " (" + type() + ") [" + ruleText() +"]";}
    @Override
    public boolean isInstant() { return true; }
    
}
