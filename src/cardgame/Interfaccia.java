
package cardgame;

import java.util.List;
import java.util.Scanner;


public class Interfaccia {
    
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