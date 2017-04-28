/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
            System.out.println("The input is not valid, try again.\n");
            res = -1;
        }
        return res;
    }
    
    public static boolean showCreatures(List<Creature> l){
	int i = 0;
	for( Creature c: l) {
            System.out.println(Integer.toString(i+1)+") " + c.toString()+ "\n");
            ++i;
	}
	if(i==0){
            System.out.println("There are no creatures\n");
            return false;
	}
        return true;
    }

    
}