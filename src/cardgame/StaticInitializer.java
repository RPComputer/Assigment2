
package cardgame;

/*
    Questa classe fa parte dell'implementazione del pattern factory. Consiste nel caricare un oggetto factory dato in
    input nell'insieme delle carte instanziabili della classe principale CardGame.
*/

public class StaticInitializer {
    public StaticInitializer(CardFactory c){
        CardGame.register(c);
    }
}
