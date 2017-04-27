
package cardgame;

public abstract class AbstractEffect implements Effect {
    @Override
    public boolean play() { 
        if(this.isTargetEffect()){
            this.setTarget();
        }
        CardGame.instance.getStack().add(this);
        return true;
    }
}
