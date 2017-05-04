
package cardgame;

public abstract class AbstractEffect implements Effect {
    /*
        Questo metodo è stato modificato per poter implementare uno schema di applicazione unico
        per quanto riguarda gli effetti. Sostanzialmente se necessitano di settare un target ciò
        viene eseguito richiamando la funzione specifica dell'effetto setTarget.
    */
    @Override
    public boolean play() { 
        if(this.isTargetEffect()){
            this.setTarget();
        }
        CardGame.instance.getStack().add(this);
        return true;
    }
}
