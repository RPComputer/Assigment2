
package cardgame;

public class StaticInitializer {
    public StaticInitializer(String s, Class c) throws InstantiationException, IllegalAccessException {
        CardGame.register(s, (CardFactory) c.newInstance());
    }
}
