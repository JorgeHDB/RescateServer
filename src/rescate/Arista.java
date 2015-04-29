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
public class Arista {
    private final int cas1X, cas1Y, cas2X, cas2Y;
    
    private Obstaculo obstaculo;
    
    public Arista(int cas1X, int cas1Y, int cas2X, int cas2Y, Obstaculo obstaculo){
        this.cas1X=cas1X;
        this.cas2X=cas2X;
        this.cas1Y=cas1Y;
        this.cas2Y=cas2Y;
        this.obstaculo = obstaculo;
    }
    
    /*Devuelve si existe una arista entre la casilla (cas1x, cas1Y) y la casilla (cas2X, cas2Y)
    
    */
    public boolean aristaDeCasillas(int cas1X, int cas1Y, int cas2X, int cas2Y){
        return (cas1X == this.cas1X && cas1Y == this.cas1Y && cas2X == this.cas2X && cas2Y == this.cas2Y) ||
                (cas1X == this.cas2X && cas1Y == this.cas2Y && cas2X == this.cas1X && cas2Y == this.cas1Y);
    }
    
    public Obstaculo getObstaculo(){
        return obstaculo;
    }
    
    public void setObstaculo( Obstaculo o){
        obstaculo = o;
    }
}
