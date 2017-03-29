
package cardgame;

public class DefaultCombatPhase implements Phase {
    @Override
    public void execute() {
        Player currentPlayer = CardGame.instance.getCurrentPlayer();
        
        System.out.println(currentPlayer.name() + ": combat phase");
        
        CardGame.instance.getTriggers().trigger(Triggers.COMBAT_FILTER);
        // TODO combat
    }
}
