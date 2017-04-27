
package cardgame;

import java.util.ArrayList;
import java.util.Scanner;

public class DefaultCombatPhase implements Phase {
    @Override
    public void execute() {
        Player currentPlayer = CardGame.instance.getCurrentPlayer();
        Player opponentPlayer = CardGame.instance.getCurrentAdversary();
        int attacking = 1, defending = 1;
        Scanner reader = CardGame.instance.getScanner();
        ArrayList<AbstractCreature> attackingCreatures = new ArrayList<>();
        ArrayList<AbstractCreature> canAttackCreatures;
        ArrayList<AbstractCreature> canDefendCreatures;
        Creature c;
        System.out.println(currentPlayer.name() + ": combat phase");
        
        CardGame.instance.getTriggers().trigger(Triggers.COMBAT_FILTER);
        // TODO combat
        if(!currentPlayer.getCreatures().isEmpty()){
            while(attacking > 0){
                System.out.println(currentPlayer.name() + "choose an attacking creature, 0 to pass");
                canAttackCreatures = attackers(currentPlayer);
                if(canAttackCreatures.isEmpty())
                    System.out.println("You don't have creatures that can attack.");
                this.showCreatures(canAttackCreatures);
                attacking = reader.nextInt();
                if(attacking > 0){
                    c = canAttackCreatures.get(attacking-1);
                    c.tap();
                    c.addTarget(opponentPlayer);
                    attackingCreatures.add((AbstractCreature) c);
                }
            }
        }
        //assunto parte dichiarazione attaccanti effettuata
        chargeCombatStack(opponentPlayer); // primo caricamento dello stack e risoluzione
        //definizione di chi difende
        if(!attackingCreatures.isEmpty() || opponentPlayer.getCreatures().isEmpty()){
            while(defending > 0){
                System.out.println(currentPlayer.name() + "choose an defending creature, 0 to pass");
                canDefendCreatures = defenders(opponentPlayer);
                this.showCreatures(canDefendCreatures);
                if(canDefendCreatures.isEmpty())
                    System.out.println("You don't have creatures that can defend.");
                defending = reader.nextInt();
                if(defending > 0){
                    c = canDefendCreatures.get(defending);
                    c.tap();
                    System.out.println(currentPlayer.name() + "choose an attacking creature to stop");
                    this.showCreatures(attackingCreatures);
                    attacking = reader.nextInt();
                    c.defend(attackingCreatures.get(attacking));
                }
            }
        }
        chargeCombatStack(currentPlayer); // secondo caricamneto dello stack e risoluzione
        
        //risoluzione del danno
        for(Creature a : attackingCreatures){
            a.attack();
        }
        //fine della combat
        
    }
    
    private void chargeCombatStack(Player currentPlayer){
        int numberPasses=0;
        int responsePlayerIdx = (CardGame.instance.getPlayer(0) == currentPlayer)?0:1;
        while (numberPasses<2) {
            if (playAvailableEffect(CardGame.instance.getPlayer(responsePlayerIdx),false))
                numberPasses=0;
            else ++numberPasses;
            
            responsePlayerIdx = (responsePlayerIdx+1)%2;
        }
        
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
        int idx= reader.nextInt()-1;
        if (idx<0 || idx>availableEffects.size())
            return false;
        else{
            availableEffects.get(idx).play();
            return true;
        }
    }
    
    private ArrayList attackers(Player p){
        ArrayList<Creature> untapped = new ArrayList<>();
        int i = 0;
        for( Creature c:p.getCreatures()) {
            if ( !c.isTapped() && c.getAtt()) {
                untapped.add(c);
                System.out.println(Integer.toString(i+1)+") " + c.toString());
                ++i;
            }
        }
        return untapped;
    }
    
    private ArrayList defenders(Player p){
        ArrayList<Creature> untapped = new ArrayList<>();
        int i = 0;
        for( Creature c:p.getCreatures()) {
            if ( !c.isTapped() && c.getDef()) {
                untapped.add(c);
                System.out.println(Integer.toString(i+1)+") " + c.toString());
                ++i;
            }
        }
        return untapped;
    }
    
    private void showCreatures(ArrayList<AbstractCreature> l){
        int i = 0;
        for( Creature c: l) {
            System.out.println(Integer.toString(i+1)+") " + c.toString());
            ++i;
        }
    }
}
