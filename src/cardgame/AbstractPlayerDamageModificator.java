
package cardgame;

public abstract class AbstractPlayerDamageModificator implements PlayerDamageModificator{
    private Player owner;
    private PlayerDamageModificator next;
    private PlayerDamageModificator prev;

    public AbstractPlayerDamageModificator(Player p){
        this.owner = p;
        next = null;
        prev = null;
    }
    
    @Override
    public void inflictDamage(int p) {
        if(next != null)
            next.inflictDamage(p);
        else owner.inflictPureDamage(p);
    }

    @Override
    public void remove() {
        if(next != null){
            if(prev != null){
                prev.setNext(next);
                next.setPrev(prev);
            }
            else owner.modificators = next;
        }
        else if(prev != null)
            prev.setNext(null);
        else owner.modificators = null;
    }

    @Override
    public void delete(PlayerDamageModificator m) {
        if (this.equals(m))
            this.remove();
        else next.delete(m);
    }

    @Override
    public PlayerDamageModificator getNext() {
        return next;
    }

    @Override
    public void setNext(PlayerDamageModificator m) {
        this.next = m;
    }

    @Override
    public PlayerDamageModificator getPrev() {
        return prev;
    }

    @Override
    public void setPrev(PlayerDamageModificator m) {
        this.prev = m;
    }

    @Override
    public void insert(PlayerDamageModificator m) {
        if(next == null){
            next = m;
            m.setPrev(this);
        }
        else owner.modificators.insert(this);
    }
    
    
}
