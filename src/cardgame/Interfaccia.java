
package cardgame;

import java.util.List;
import java.util.Scanner;

/*
    Questa è una classe di servizio che contiene le due funzioni più utilizzate nella stessa identica forma
    in molti punti del codice di tutto il progetto. La cosa più efficace era dichiararle e implementarle in
    una classe apposita per poi importarle dove necessario. Sebbene questo schema implementativo non applichi
    il pattern strategy segue l'idea di base.
*/

public class Interfaccia {
    //Funzione utilizzata per acquisire un numero in input in tutto il progetto, gestisce anche gli errori
    public static int acquireInput(){
        int res;
        Scanner reader = CardGame.instance.getScanner();
        try{
            res = reader.nextInt();
        }catch (NumberFormatException error) {
            System.out.println("The input is not valid, try again.");
            res = -1;
        }
        return res;
    }
    
    //Funzione utilizzata per mostrare tutte le creature presenti in una lista, utilissima in tutto il progetto
    public static boolean showCreatures(List<Creature> l){
	int i = 0;
	for( Creature c: l) {
            System.out.println((i+1)+") " + c.name());
            ++i;
	}
	if(i==0){
            System.out.println("There are no creatures");
            return false;
	}
        return true;
    }

    
}