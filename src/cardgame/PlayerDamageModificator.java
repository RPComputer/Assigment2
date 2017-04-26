
package cardgame;

public interface PlayerDamageModificator {
    public void remove();
    public void delete(PlayerDamageModificator m);
    public void insert(PlayerDamageModificator m);
    public PlayerDamageModificator getNext();
    public void setNext(PlayerDamageModificator m);
    public PlayerDamageModificator getPrev();
    public void setPrev(PlayerDamageModificator m);
    public void inflictDamage(int p);
}
