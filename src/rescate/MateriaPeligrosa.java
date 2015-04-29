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
public class MateriaPeligrosa extends Elemento implements Transportable{

    private final Tablero tablero;
    
    public MateriaPeligrosa(int x, int y, Tablero tab) {
        super(x, y);
        tablero = tab;
    }

    @Override
    public void moverNorte() {
        if(tablero.getCasilla(getX()+1,getY())!=NivelFuego.FUEGO && tablero.adyacentes(getX(), getY(), getX()-1, getY()));
        setX(getX()-1);
    }

    @Override
    public void moverSur() {
        if(tablero.getCasilla(getX()-1,getY())!=NivelFuego.FUEGO && tablero.adyacentes(getX(), getY(), getX()+1, getY()));
        setX(getX()+1);
    }

    @Override
    public void moverEste() {
        if(tablero.getCasilla(getX(),getY()-1)!=NivelFuego.FUEGO && tablero.adyacentes(getX(), getY(), getX(), getY()+1));
        setY(getY()+1);
    }

    @Override
    public void moverOeste() {
        if(tablero.getCasilla(getX(),getY()+1)!=NivelFuego.FUEGO && tablero.adyacentes(getX(), getY(), getX(), getY()-1));
        setY(getY()-1);
    }
        
}
