
package cardgame;

import static cardgame.Interfaccia.acquireInput;
import static cardgame.Interfaccia.showCreatures;
import java.util.ArrayList;
import java.util.List;

public class DefaultCombatPhase implements Phase {
    public ArrayList<Creature> attackingCreatures;
    ArrayList<Creature> canAttackCreatures;
    ArrayList<Creature> canDefendCreatures;
    public DefaultCombatPhase() {
        this.attackingCreatures = new ArrayList<>();
    }
    @Override
    public void execute() {
        Player currentPlayer = CardGame.instance.getCurrentPlayer();
        Player opponentPlayer = CardGame.instance.getCurrentAdversary();
        int attacking = 1, defending = 1;
        
        Creature c;
        System.out.println(currentPlayer.name() + ": starts the COMBAT PHASE");
        
        CardGame.instance.getTriggers().trigger(Triggers.COMBAT_FILTER);
        // TODO combat
        if(!currentPlayer.getCreatures().isEmpty()){
            while(attacking > 0){
                System.out.println(currentPlayer.name() + ": choose an attacking creature, 0 to pass");
                canAttackCreatures = attackers(currentPlayer);
                if(canAttackCreatures.isEmpty()){
                    System.out.println("You don't have creatures that can attack.");
                    attacking = 0;
                }
                else{
                    attacking = acquireInput();
                    if(attacking > 0 && attacking <= canAttackCreatures.size()){
                        c = canAttackCreatures.get(attacking-1);
                        c.tap();
                        c.addTarget(opponentPlayer);
                        attackingCreatures.add((Creature) c);
                    }
                }
            }
        }
        //assunto parte dichiarazione attaccanti effettuata
        chargeCombatStack(opponentPlayer); // primo caricamento dello stack e risoluzione// primo caricamento dello stack e risoluzione
        
        System.out.println("============== Attackers ==============");
        if (attackingCreatures.isEmpty()) {
            System.out.println("No creature are attacking");
        } else {
            for (Creature cre:attackingCreatures)
                System.out.println("  "+cre.name() + "(" + cre.getPower() + "/" + cre.getToughness() + ")");
        }
        System.out.println("=======================================");
        //definizione di chi difende
        if(!attackingCreatures.isEmpty() || opponentPlayer.getCreatures().isEmpty()){
            while(defending > 0){
                canDefendCreatures = defenders(opponentPlayer);
                if(canDefendCreatures.isEmpty()){
                    System.out.println(currentPlayer.name() + " doesn't have creatures that can defend.");
                    defending = 0;
                }
                else{
                    System.out.println(currentPlayer.name() + ": choose an defending creature, 0 to pass");
                    defending = acquireInput();
                    if(defending > 0 && defending <= canDefendCreatures.size()){
                        c = canDefendCreatures.get(defending-1);
                        c.tap();
                        System.out.println(currentPlayer.name() + "choose an attacking creature to stop");
                        showCreatures(attackingCreatures);
                        attacking = acquireInput();
                        if(attacking > 0 && attacking <= attackingCreatures.size())
                            c.defend(attackingCreatures.get(attacking-1));
                        else System.out.println("Input not valid.");
                    }
                }
            }
        }
        chargeCombatStack(currentPlayer); // secondo caricamneto dello stack e risoluzione// secondo caricamneto dello stack e risoluzione
        
        //risoluzione del danno
        System.out.println("============== Combat results ==============");
        for(Creature a : attackingCreatures){
            a.attack();
        }
        System.out.println("============================================");
        attackingCreatures.clear();
        //fine della combat
    }
    
    public void chargeCombatStack(Player currentPlayer){
        int numberPasses=0;
        int responsePlayerIdx = (CardGame.instance.getPlayer(0) == currentPlayer)?0:1;
        System.out.println("CHARGING STACK - START");
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
    
    private ArrayList attackers(Player p){
        ArrayList<Creature> untapped = new ArrayList<>();
        int i = 1;
        for( Creature c:p.getCreatures()) {
            if ( !c.isTapped() && c.getAtt()) {
                untapped.add(c);
                System.out.println(i+") " + c.name());
                i++;
            }
        }
        return untapped;
    }
    
    private ArrayList defenders(Player p){
        ArrayList<Creature> untapped = new ArrayList<>();
        int i = 1;
        for( Creature c:p.getCreatures()) {
            if ( !c.isTapped() && c.getDef()) {
                untapped.add(c);
                System.out.println(i+") " + c.name());
                i++;
            }
        }
        return untapped;
    }
}
