/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rescate;

/**
 *
 * @author Jorge
 */
public interface Transportable {
    
    /*Segun la posicion del elemento en el tablero y de los obstaculos,
    el objeto se podrá mover o no
    */
    public void moverNorte();
    public void moverSur();
    public void moverEste();
    public void moverOeste();
    
}
