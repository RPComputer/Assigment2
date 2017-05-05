
package cardgame;

import java.util.ArrayDeque;
import java.util.ArrayList;

/*
    In questa classe, oltre ad aggiungere dei nuovi FILTER abbiamo modificato la classe Entry e il metodo deregister.
    Deregister non funzionava perchè effettuava l'equals tra un oggetto di tipo entry e uno di tipo triggeraction.
    Modificando deregister è stato necessario creare una funzione all'interno di entry che consentisse di effettuare
    correttamente l'equals.
*/

public class Triggers {
    private class Entry { 
        public int filter; 
        public TriggerAction action;
        public Entry(int f, TriggerAction a) { filter=f; action=a; }
        private boolean getTriggerAction(TriggerAction a){
            if(this.action.equals(a))
                return true;
            else return false;
        }
    }
    
    ArrayList<Triggers.Entry> actions = new ArrayList<>();
    
    
    public void register(int phaseTrigger, TriggerAction a) {
        actions.add(new Triggers.Entry(phaseTrigger, a));
    }
    
    public void deregister(TriggerAction a) {
        for(int i=actions.size()-1; i>=0; --i) {
            if (actions.get(i).getTriggerAction(a))
                actions.remove(i);
        }
    }
    
    
    public void trigger(int event) { trigger(event, null); }
    public void trigger(int event, Object args) {
        ArrayDeque<Triggers.Entry> triggerableActions = new ArrayDeque<>();
        
        for (Triggers.Entry p:actions) {
            if ((p.filter & event)!=0) triggerableActions.push(p);
        }
        
        //execute last-inserted-first
        while (!triggerableActions.isEmpty()) {
            Triggers.Entry entry=triggerableActions.pop();
            entry.action.execute(args);
        }
    }
    
    public static final int DRAW_FILTER=1;
    public static final int UNTAP_FILTER=2;
    public static final int COMBAT_FILTER=4;
    public static final int MAIN_FILTER=8;
    public static final int END_FILTER=16;
    public static final int ENTER_CREATURE_FILTER=32;
    public static final int EXIT_CREATURE_FILTER=64;
    public static final int ENTER_ENCHANTMENT_FILTER=128;
    public static final int EXIT_ENCHANTMENT_FILTER=256;
    public static final int START_TURN_FILTER=512;
    public static final int END_TURN_FILTER=1024;
    public static final int STACK_CHARGING_STARTED_EVENT=2048;
    public static final int STACK_CHARGING_COMPLETED_EVENT=4096;
    public static final int ENTER_ENCHANT_CREATURE_ENCHANTMENT_EVENT = 8192; /*nuovo trigger creato da noi per abduction*/
    public static final int EXIT_ENCHANT_CREATURE_ENCHANTMENT_EVENT = 16384; /*nuovo trigger creato da noi per abduction*/
}
