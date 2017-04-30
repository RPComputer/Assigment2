
package cardgame;

import static cardgame.Interfaccia.acquireInput;
import java.util.ArrayList;
import java.util.Scanner;

public class DefaultMainPhase implements Phase {
    
    @Override
    public void execute() {
        Player currentPlayer = CardGame.instance.getCurrentPlayer();
        int responsePlayerIdx = (CardGame.instance.getPlayer(0) == currentPlayer)?1:0;
        
        System.out.println(currentPlayer.name() + ": starts the MAIN PHASE");
        
        CardGame.instance.getTriggers().trigger(Triggers.MAIN_FILTER);
        
        
        // alternate in placing effect until bith players pass
        int numberPasses=0;
        System.out.println("CHARGING STACK - START");
        
        if (!playAvailableEffect(currentPlayer, true))
            ++numberPasses;
        CardGame.instance.getTriggers().trigger(Triggers.STACK_CHARGING_STARTED_EVENT);
        while (numberPasses<2) {
            if (playAvailableEffect(CardGame.instance.getPlayer(responsePlayerIdx),false))
                numberPasses=0;
            else ++numberPasses;
            
            responsePlayerIdx = (responsePlayerIdx+1)%2;
        }
        CardGame.instance.getTriggers().trigger(Triggers.STACK_CHARGING_COMPLETED_EVENT);
        System.out.println("CHARGING STACK - END");
        CardGame.instance.getStack().resolve();
    }
    
    
    // looks for all playable effects from cards in hand and creatures in play
    // and asks player for which one to play
    // includes creatures and sorceries only if isMain is true
    private boolean playAvailableEffect(Player activePlayer, boolean isMain) {
        //collect and display available effects...
        ArrayList<Effect> availableEffects = new ArrayList<>();
        Scanner reader = CardGame.instance.getScanner();

        //...cards first
        System.out.println(activePlayer.name() + " select card/effect to play, 0 to pass");
        int i=0;
        for( Card c:activePlayer.getHand() ) {
            if ( isMain || c.isInstant() ) {
                availableEffects.add( c.getEffect(activePlayer) );
                System.out.println(Integer.toString(i+1)+") " + c );
                ++i;
            }
        }
        
        //...creature effects last
        for ( Creature c:activePlayer.getCreatures()) {
            for (Effect e:c.avaliableEffects()) {
                availableEffects.add(e);
                System.out.println(Integer.toString(i+1)+") " + c.name() + 
                    " ["+ e + "]" );
                ++i;
            }
        }
        
        //get user choice and play it
        if(i == 0)
            System.out.println("You don't have any playable effects");
        else{
            int idx= acquireInput()-1;
            if (idx<0 || idx>=availableEffects.size())
                return false;
            else{
                availableEffects.get(idx).play();
                return true;
            }
        }
        return false;
    }
    
}
