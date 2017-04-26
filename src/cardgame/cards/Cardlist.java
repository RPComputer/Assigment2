
package cardgame.cards;

import java.util.ArrayList;

public class Cardlist {
    ArrayList<String> list = new ArrayList();
    public Cardlist(){
        list.add("cardgame.cards.Abduction");
        list.add("cardgame.cards.AetherBarrier");
        list.add("cardgame.cards.AetherFlash");
        list.add("cardgame.cards.Afflict");
        list.add("cardgame.cards.AggressiveUrge");
        list.add("cardgame.cards.AuraBlast");
        list.add("cardgame.cards.BoilingEarth");
        list.add("cardgame.cards.BronzeSable");
        list.add("cardgame.cards.CalmingVerse");
        list.add("cardgame.cards.Cancel");
        list.add("cardgame.cards.DayOfJudgment");
        
    }
    public void load() throws ClassNotFoundException{
        for(String string : list)
            Class.forName(string);
    }
}