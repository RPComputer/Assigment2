
package cardgame;

public class StaticInitializer {
    public StaticInitializer(CardFactory c){
        CardGame.register(c);
    }
}
