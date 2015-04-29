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
public class Casilla {
    NivelFuego nivel;
    boolean focoDeCalor;
    
    public Casilla(){
        nivel = NivelFuego.SIN_FUEGO;
        focoDeCalor = false;
    }
    
    public void ponerFocoDeCalor(){
        focoDeCalor = true;
    }
    
    public boolean hayFocoDeCalor(){
        return focoDeCalor;
    }
    public NivelFuego getNivelDeFuego(){
        return nivel;
    }
    
    public void setNivelDeFuego(NivelFuego nuevoNivel){
        nivel = nuevoNivel;
    }
}
