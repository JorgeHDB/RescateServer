/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.Serializable;

/**
 *
 * @author Jorge
 */
public class Imagen implements Serializable{
    private final String rutaImagen;
    private final int x;
    private final int y;
    
    public Imagen(String rutaImag, int x , int y){
        rutaImagen = rutaImag;
        this.x = x;
        this.y = y;
    }    
    
    public String getRuta(){
        return rutaImagen;
    }
    
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
