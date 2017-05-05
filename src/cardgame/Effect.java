/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardgame;

/*
    L'interfaccia effect è stata modiificata con le dichiarazioni dei metodi aggiunti agli effetti per il funzionamento
    di tutti gli schemi e pattern
 */
public interface Effect {
    // pays for effect and places it in the stack
    boolean play();
    
    // resolves the effect
    void resolve();
    
    @Override
    String toString();
    
    boolean isTargetEffect();
    void setTarget();
    Object getTarget();
}
