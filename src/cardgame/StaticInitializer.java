
package cardgame;

public class StaticInitializer {
    public StaticInitializer(String s, CardFactory c){
        CardGame.register(s, c);
    }
}
