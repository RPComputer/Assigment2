
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
        list.add("cardgame.cards.AncestralMask");
        list.add("cardgame.cards.ArgothianEnchantress");
        list.add("cardgame.cards.AuraBlast");
        list.add("cardgame.cards.BenevolentAncestor"); 
        list.add("cardgame.cards.BoilingEarth");
        list.add("cardgame.cards.BronzeSable");
        list.add("cardgame.cards.CalmingVerse");
        list.add("cardgame.cards.Cancel");
        list.add("cardgame.cards.Darkness");
        list.add("cardgame.cards.DayOfJudgment");
        list.add("cardgame.cards.Deflection");
        list.add("cardgame.cards.FalsePeace");
        list.add("cardgame.cards.Fatigue");
        list.add("cardgame.cards.FriendlyEnvironment");
        list.add("cardgame.cards.Homeopathy");
        list.add("cardgame.cards.NorwoodRanger");
        list.add("cardgame.cards.Reflexologist");
        list.add("cardgame.cards.SavorTheMoment");
        list.add("cardgame.cards.VolcanicHammer");
        list.add("cardgame.cards.WorldAtWar");
    }
    public void load() throws ClassNotFoundException{
        for(String string : list)
            Class.forName(string);
    }
}
