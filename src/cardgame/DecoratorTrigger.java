
package cardgame;

public class DecoratorTrigger implements TriggerAction{
    AbstractDecorator d;
    public DecoratorTrigger(AbstractDecorator d){
        this.d = d;
    }
    @Override
    public void execute(Object args) {
        AbstractDecorator d1, d2;
        CardGame.instance.getTriggers().deregister(this);
        if(d.getPrev() instanceof CreatureImage){
            CreatureImage c = d.getHead();
            c.setPointer(d.getNext());
        }
        else{
            d1 = (AbstractDecorator) d.getPrev();
            d1.setNext(d.getNext());
        }
        if(d.getNext() instanceof AbstractDecorator){
            d2 = (AbstractDecorator) d.getNext();
            d2.setPrev(d.getPrev());
        }
    }
}
