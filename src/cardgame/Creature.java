
package cardgame;

import java.util.ArrayList;
import java.util.List;

/*
    L'interfaccia creature è stata modiificata con le dichiarazioni dei metodi aggiunti alle creature e ai
    decorator
 */
public interface Creature extends Permanent {
    
    boolean tap();
    boolean untap();
    boolean isTapped();
    void attack();
    void defend(Creature c);
    boolean inflictDamage(int dmg);
    void resetDamage();
    int getPower();
    int getToughness();
    
    // returns all the effects associated to this creature
    List<Effect> effects();
    
    // returns only the effects that can be played currently
    // depending on state, e.g., tapped/untapped
    List<Effect> avaliableEffects();

    public void addTarget(Object target);

    public ArrayList getTarget();

    public void clearTarget();

    public boolean getDef();

    public boolean getAtt();

    public ArrayList<String> getDTypes();
}
