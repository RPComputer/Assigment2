
package cardgame;

/*
    Questa classe implementa il trigger di default per i decorator: l'azione più frequente da applicare è la rimozione
    del decorator in base ad un evento (es. fine del turno). Per questo abbiamo pensato a una implementazione generale
    per questa funzione.
*/

public class DecoratorTrigger implements TriggerAction{
    AbstractDecorator d;
    public DecoratorTrigger(AbstractDecorator d){
        this.d = d;
    }
    @Override
    public void execute(Object args) {
        d.removeDecorator();
    }
}
