
package cardgame;

import java.util.ArrayList;
import java.util.List;

public class CreatureImage implements Creature{
    
    private Player owner;
    private Creature pointer;
    
    public Player getOwner(){
        return owner;
    }
    
    public CreatureImage(Player owner, Creature pointer){
        this.owner = owner;
        this.pointer = pointer;
    }
    
    public void setPointer(Creature np){
        this.pointer = np;
    }
    
    public Creature getPointer(){
        return this.pointer;
    }
    
    @Override
    public int getPower() {
        return pointer.getPower();
    }

    @Override
    public int getToughness() {
        return pointer.getToughness();
    }

    @Override
    public List<Effect> effects() {
        return pointer.effects();
    }

    @Override
    public List<Effect> avaliableEffects() {
        return pointer.avaliableEffects();
    }

    @Override
    public String name() {
        return pointer.name();
    }

    @Override
    public boolean tap() {
        return pointer.tap();
    }

    @Override
    public boolean untap() {
        return pointer.untap();
    }

    @Override
    public boolean isTapped() {
        return pointer.isTapped();
    }

    @Override
    public void attack() {
        pointer.attack();
    }

    @Override
    public void defend(Creature c) {
        pointer.defend(c);
    }

    @Override
    public boolean inflictDamage(int dmg) {
        boolean b = pointer.inflictDamage(dmg);
        if(b)
            if(pointer instanceof AbstractDecorator){
                AbstractDecorator p = (AbstractDecorator)pointer;
                p.deregisterDecorator();
            }
        return b;
    }

    @Override
    public void resetDamage() {
        pointer.resetDamage();
    }

    @Override
    public void addTarget(Object target) {
        pointer.addTarget(target);
    }

    @Override
    public ArrayList getTarget() {
        return pointer.getTarget();
    }

    @Override
    public void clearTarget() {
        pointer.clearTarget();
    }

    @Override
    public boolean getDef() {
        return pointer.getDef();
    }

    @Override
    public boolean getAtt() {
        return pointer.getAtt();
    }

    @Override
    public void insert() {
        pointer.insert();
    }

    @Override
    public boolean remove() {
        if(pointer.remove()){
            owner.getCreatures().remove(this);
            CardGame.instance.getTriggers().trigger(Triggers.EXIT_CREATURE_FILTER,this);
            return true;
        }
        else return false;
    }
    
    public void checker(){
        if(pointer.getToughness() <= 0){
            this.remove();
            System.out.println(this.owner.name() + "'s creature: " + this.name() + " is dead.");
        }
    }
    
    @Override
    public ArrayList<String> getDTypes(){
        return pointer.getDTypes();
    }
}
