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
public abstract class Elemento {
    private int x, y;
    
    public Elemento(int x, int y){
        setX(x);
        setY(y);
    }
    
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    /*Dado de 6
    
    */
    public void setX(int x){
        if(x>7) x = 7;
        if(x<0) x = 0;
        this.x = x;
    }
    /*Dado de 8
    
    */
    public void setY(int y){
        if(y>9) y = 9;
        if(y < 0) y = 0;
        this.y = y;
    }
    
    public boolean misCoord(int x, int y){
        return getX() == x && getY() == y;
    }
}
