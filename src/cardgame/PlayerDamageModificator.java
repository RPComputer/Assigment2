
package cardgame;

/*
    Questa interfaccia definisce tutti i metodi per l'implementazione dello schema dei modificatori del danno
    dei giocatori, nel nostro progetto si traduce nell'implementazione del AbstractPlayerDamageModificator
    che è il modificatore di default.
*/

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
