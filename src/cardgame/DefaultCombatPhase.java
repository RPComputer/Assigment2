
package cardgame;

import static cardgame.Interfaccia.acquireInput;
import static cardgame.Interfaccia.showCreatures;
import java.util.ArrayList;

/*
    La DefaultCombatPhase è uno dei punti più importanti del secondo assignment. Implementa la fase di combattimento
    standard del gioco di magic che consiste in:
    - dichiarazione attaccanti
    - caricamento dello stack senza sorcery o enchantments, a partire dall'avversario
    - dichiarazione difensori
    - caricamento dello stack senza sorcery o enchantments, a partire dal giocatore di questo turno

    Questa classe si occupa anche dell'output specifico per il combattimento.

    La spiegazione del codice è all'interno dello stesso.
*/

public class DefaultCombatPhase implements Phase {
    ArrayList<Creature> attackingCreatures; //lista delle creature che attaccano
    ArrayList<Creature> defendingCreatures; //lista delle creature che difendono
    ArrayList<Creature> canAttackCreatures; //lista di appoggio per sapere chi può attaccare
    ArrayList<Creature> canDefendCreatures; //lista di appoggio per sapere chi può difendere
    public DefaultCombatPhase() {
        this.attackingCreatures = new ArrayList<>();
        this.defendingCreatures = new ArrayList<>();
    }
    @Override
    public void execute() {
        attackingCreatures.clear(); //reset delle liste
        defendingCreatures.clear();
        Player currentPlayer = CardGame.instance.getCurrentPlayer(); //acquisizione e attribuzione del ruolo dei giocatori
        Player opponentPlayer = CardGame.instance.getCurrentAdversary();
        int attacking = 1, defending = 1;
        
        Creature c;
        System.out.println(currentPlayer.name() + ": starts the COMBAT PHASE");
        
        CardGame.instance.getTriggers().trigger(Triggers.COMBAT_FILTER);
        // TODO combat
        if(!currentPlayer.getCreatures().isEmpty()){ //se il giocatore attaccante ha creature
            while(attacking > 0){ //finchè non dichiara di non voler più attaccare o non ha più creature che possono farlo
                System.out.println(currentPlayer.name() + ": choose an attacking creature, 0 to pass");
                canAttackCreatures = attackers(currentPlayer); //sceglie una creatura che deve attaccare
                if(canAttackCreatures.isEmpty()){
                    System.out.println("You don't have creatures that can attack.");
                    attacking = 0;
                }
                else{
                    attacking = acquireInput();
                    if(attacking > 0 && attacking <= canAttackCreatures.size()){ //settaggio valori in preparazione della risoluzione del danno
                        c = canAttackCreatures.get(attacking-1);
                        c.tap(); //la creeatura viene tappata per poter attaccare
                        c.addTarget(opponentPlayer); //per default il target delle creature è il giocatore avversario
                        attackingCreatures.add((Creature) c);
                    }
                }
            }
        }
        //parte dichiarazione attaccanti effettuata
        chargeCombatStack(opponentPlayer); // primo caricamento dello stack e risoluzione
        //visto che il caricamento dello stack comporta un considerevole output, questo output riepiloga la situazione
        System.out.println("\n============== Attackers ==============");
        if (attackingCreatures.isEmpty()) {
            System.out.println("No creatures are attacking");
        } else {
            for (Creature cre:attackingCreatures)
                System.out.println("  "+cre.name() + "(" + cre.getPower() + "/" + cre.getToughness() + ")");
        }
        System.out.println("=======================================\n");
        //definizione di chi difende
        if(!attackingCreatures.isEmpty() || opponentPlayer.getCreatures().isEmpty()){ //se ci sono delle creature che attaccano e il difensore ha delle creature
            while(defending > 0){//finchè non dichiara di non voler più difendere o non ha più creature che possono farlo
                canDefendCreatures = defenders(opponentPlayer);
                if(canDefendCreatures.isEmpty()){
                    System.out.println(currentPlayer.name() + ": doesn't have creatures that can defend.");
                    defending = 0;
                }
                else{
                    System.out.println(currentPlayer.name() + ": choose a defending creature, 0 to pass");
                    showCreatures(canDefendCreatures);
                    defending = acquireInput(); //sceglie una creatura che deve difendere
                    if(defending > 0 && defending <= canDefendCreatures.size()){
                        c = canDefendCreatures.get(defending-1);
                        c.tap(); //la creatura viene tappata per poter difendere
                        defendingCreatures.add(c);
                        System.out.println(currentPlayer.name() + ": choose an attacking creature to stop");
                        showCreatures(attackingCreatures);
                        attacking = acquireInput(); //il giocatore sceglie la creatura attaccante che la propria creatura deve fronteggiare
                        if(attacking > 0 && attacking <= attackingCreatures.size())
                            c.defend(attackingCreatures.get(attacking-1)); //la creatura difensore modifica il target dell'attaccante
                        else System.out.println("Input not valid.");
                    }
                }
            }
        }
        chargeCombatStack(currentPlayer); // secondo caricamneto dello stack e risoluzione
        
        //risoluzione del danno
        System.out.println("============== Combat results ==============");
        if (attackingCreatures.isEmpty()) {
            System.out.println("No results avaiable.");
        }
        for(Creature a : attackingCreatures){
            a.attack();
        }
        System.out.println("============================================");
        
        //fine della combat
    }
    
    //funzione che effettua output e input e il caricamento dello stack senza sorcery o enchantments
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
    
    //funzione per charge combat stack presa dalla main phase in quanto serve lo stesso servizio
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
    
    //funzione che mostra quali creature possono attaccare e acquisisce la scelta del giocatore
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
    
    //funzione che mostra quali creature possono difendere e acquisisce la scelta del giocatore
    private ArrayList defenders(Player p){
        ArrayList<Creature> untapped = new ArrayList<>();
        int i = 1;
        for( Creature c:p.getCreatures()) {
            if ( !c.isTapped() && c.getDef()) {
                untapped.add(c);
                i++;
            }
        }
        return untapped;
    }
    
    
    //getter e setter delle liste principali della fase combat
    public ArrayList<Creature> getCreaturesWhichAttacked(){
        return attackingCreatures;
    }
    
    public void setCreaturesWhichAttacked(ArrayList<Creature> c){
        this.attackingCreatures = c;
    }
    
    public ArrayList<Creature> getCreaturesWhichDefended(){
        return defendingCreatures;
    }
    
    public void setCreaturesWhichDefended(ArrayList<Creature> c){
        this.defendingCreatures = c; 
    }
}
