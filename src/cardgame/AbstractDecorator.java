
package cardgame;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDecorator implements Creature{
    private final CreatureImage head;
    private Creature next;
    private Creature prev;
    
    public AbstractDecorator(CreatureImage c){
        this.head = c;
        if(c != null){
            this.next = c.getPointer();
            this.prev = c;
        }
    }
    
    public CreatureImage getHead(){
        return this.head;
    }
    
    public Creature getNext(){
        return next;
    }
    
    public void setNext(Creature n){
        this.next = n;
    }
    
    public Creature getPrev(){
        return prev;
    }
    
    public void setPrev(Creature n){
        this.prev = n;
    }

    @Override
    public boolean tap() {
        return next.tap();
    }

    @Override
    public boolean untap() {
        return next.untap();
    }

    @Override
    public boolean isTapped() {
        return next.isTapped();
    }

    @Override
    public void attack() {
        next.attack();
    }

    @Override
    public void defend(Creature c) {
        next.defend(c);
    }

    @Override
    public boolean inflictDamage(int dmg) {
        return next.inflictDamage(dmg);
    }

    @Override
    public void resetDamage() {
        next.resetDamage();
    }

    @Override
    public int getPower() {
        return next.getPower();
    }

    @Override
    public int getToughness() {
        return next.getToughness();
    }

    @Override
    public List<Effect> effects() {
        return next.effects();
    }

    @Override
    public List<Effect> avaliableEffects() {
        return next.avaliableEffects();
    }

    @Override
    public void addTarget(Object target) {
        next.addTarget(target);
    }

    @Override
    public ArrayList getTarget() {
        return next.getTarget();
    }

    @Override
    public void clearTarget() {
        next.clearTarget();
    }

    @Override
    public boolean getDef() {
        return next.getDef();
    }

    @Override
    public boolean getAtt() {
        return next.getAtt();
    }

    @Override
    public String name() {
        return next.name();
    }

    @Override
    public void insert() {
        next.insert();
    }

    @Override
    public boolean remove() {
        return next.remove();
    }
    
    @Override
    public ArrayList<String> getDTypes(){
        return next.getDTypes();
    }
    
    public void removeDecorator(){
        AbstractDecorator d1, d2;
        if(this.getPrev() instanceof CreatureImage){
            CreatureImage c = this.getHead();
            c.setPointer(this.getNext());
        }
        else{
            d1 = (AbstractDecorator) this.getPrev();
            d1.setNext(this.getNext());
        }
        if(this.getNext() instanceof AbstractDecorator){
            d2 = (AbstractDecorator) this.getNext();
            d2.setPrev(this.getPrev());
        }
    }
    
    public void deregisterDecorator(){
        if(next instanceof AbstractDecorator){
            AbstractDecorator p = (AbstractDecorator)next;
            p.deregisterDecorator();
        }
    }
}
