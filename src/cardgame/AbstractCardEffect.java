/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardgame;

/**
 *
 * @author atorsell
 */
public abstract class AbstractCardEffect extends AbstractEffect {
    protected Player owner;
    protected Card card;
    
    public String getCardName(){ return card.name(); }
    
    protected AbstractCardEffect(Player p, Card c) { owner=p; card=c; }
    
    @Override
    public boolean play() { 
        owner.getHand().remove(card);
        return super.play();
    }
    
    @Override
    public String toString() { return card.toString(); }
}
