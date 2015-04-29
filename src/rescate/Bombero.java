/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rescate;

import java.util.ArrayList;
import servidor.ColaStrings;
import servidor.Modelo;

/**
 * Clase que implementa un Bombero que tiene una posición en un tablero y puede
 * ser movido de acorde a unas reglas, al igual que puede realizar acciones como
 * moverse, extinguir fuegos... etc.
 *
 * @author Jorge
 */
public class Bombero extends Elemento implements Transportable {

    private int numAcciones;
    private int numAccionesGuardadas;
    private final Tablero tablero;
    private final Modelo miModelo;
    private final int numero;
    private final int numJugadores;

    public Bombero(int x, int y, Tablero tab, int numero, int numJugadores, Modelo modelo) {
        super(x, y);
        tablero = tab;
        miModelo = modelo;
        numAcciones = 4;
        numAccionesGuardadas = 0;
        this.numero = numero;
        this.numJugadores = numJugadores;
    }

    /**
     * *
     * Devuelve el tablero en el que se está jugando.
     *
     * @return
     */
    public Tablero getTablero() {
        return tablero;
    }

    private void nuevoTurno() {
        numAcciones = 4 + numAccionesGuardadas;
        numAccionesGuardadas = 0;
    }

    public int getNumAcciones() {
        return numAcciones;
    }

    public ArrayList<AccionBombero> getListaAcciones() {
        ArrayList<AccionBombero> ret = new ArrayList<>();
        if (tablero.getTurno() == numero) { //Suponiendo que estas en tu turno
            //Acciones que cuestan un punto:
            if (numAcciones > 0) {

                //Moverse a una casilla que no está en llamas:
                if (tablero.adyacentes(getX(), getY(), getX() - 1, getY()) && tablero.getCasilla(getX() - 1, getY()) != NivelFuego.FUEGO) {
                    ret.add(AccionBombero.MOVER_NORTE);
                }
                if (tablero.adyacentes(getX(), getY(), getX(), getY() + 1) && tablero.getCasilla(getX(), getY() + 1) != NivelFuego.FUEGO) {
                    ret.add(AccionBombero.MOVER_ESTE);
                }
                if (tablero.adyacentes(getX(), getY(), getX() + 1, getY()) && tablero.getCasilla(getX() + 1, getY()) != NivelFuego.FUEGO) {
                    ret.add(AccionBombero.MOVER_SUR);
                }
                if (tablero.adyacentes(getX(), getY(), getX(), getY() - 1) && tablero.getCasilla(getX(), getY() - 1) != NivelFuego.FUEGO) {
                    ret.add(AccionBombero.MOVER_OESTE);
                }

                //Abrir o cerrar puertas adyacentes:
                for (Arista a : tablero.getAristas()) {
                    if (a.getObstaculo() == Obstaculo.PUERTA_ABIERTA && a.aristaDeCasillas(getX(), getY(), getX() - 1, getY())) {
                        ret.add(AccionBombero.CERRAR_PUERTA_NORTE);
                    }
                    if (a.getObstaculo() == Obstaculo.PUERTA_ABIERTA && a.aristaDeCasillas(getX(), getY(), getX(), getY() + 1)) {
                        ret.add(AccionBombero.CERRAR_PUERTA_ESTE);
                    }
                    if (a.getObstaculo() == Obstaculo.PUERTA_ABIERTA && a.aristaDeCasillas(getX(), getY(), getX() + 1, getY())) {
                        ret.add(AccionBombero.CERRAR_PUERTA_SUR);
                    }
                    if (a.getObstaculo() == Obstaculo.PUERTA_ABIERTA && a.aristaDeCasillas(getX(), getY(), getX(), getY() - 1)) {
                        ret.add(AccionBombero.CERRAR_PUERTA_OESTE);
                    }

                    if (a.getObstaculo() == Obstaculo.PUERTA_CERRADA && a.aristaDeCasillas(getX(), getY(), getX() - 1, getY())) {
                        ret.add(AccionBombero.ABIR_PUERTA_NORTE);
                    }
                    if (a.getObstaculo() == Obstaculo.PUERTA_CERRADA && a.aristaDeCasillas(getX(), getY(), getX(), getY() + 1)) {
                        ret.add(AccionBombero.ABIR_PUERTA_ESTE);
                    }
                    if (a.getObstaculo() == Obstaculo.PUERTA_CERRADA && a.aristaDeCasillas(getX(), getY(), getX() + 1, getY())) {
                        ret.add(AccionBombero.ABIR_PUERTA_SUR);
                    }
                    if (a.getObstaculo() == Obstaculo.PUERTA_CERRADA && a.aristaDeCasillas(getX(), getY(), getX(), getY() - 1)) {
                        ret.add(AccionBombero.ABIR_PUERTA_OESTE);
                    }
                }

                //Extinguir fuego o humo:
                if (tablero.adyacentes(getX(), getY(), getX() - 1, getY()) && tablero.getCasilla(getX() - 1, getY()) != NivelFuego.SIN_FUEGO) {
                    ret.add(AccionBombero.EXTINGUIR_NORTE);
                }
                if (tablero.adyacentes(getX(), getY(), getX(), getY() + 1) && tablero.getCasilla(getX(), getY() + 1) != NivelFuego.SIN_FUEGO) {
                    ret.add(AccionBombero.EXTINGUIR_ESTE);
                }
                if (tablero.adyacentes(getX(), getY(), getX() + 1, getY()) && tablero.getCasilla(getX() + 1, getY()) != NivelFuego.SIN_FUEGO) {
                    ret.add(AccionBombero.EXTINGUIR_SUR);
                }
                if (tablero.adyacentes(getX(), getY(), getX(), getY() - 1) && tablero.getCasilla(getX(), getY() - 1) != NivelFuego.SIN_FUEGO) {
                    ret.add(AccionBombero.EXTINGUIR_OESTE);
                }
            }

        //ACCIONES QUE CUESTAN 2 puntos: Entrar en una casilla en llamas (solo cuando se pueda salir) en realidad necesitas 3 puntos jeje
            //Acciones gratis:    
            ret.add(AccionBombero.FINALIZAR_TURNO);
        }
        return ret;
    }

    public void realizarAccion(AccionBombero accion) {
        //Es una accion válida si está aqui
        if(accion == AccionBombero.NUEVO_TURNO){
            for (int i = 0; i < numJugadores; i++) {
                //Podemos detallarlo un poco mas...
                //if(i != numero)
                    miModelo.introducirMensajeEnCola("El jugador " + numero + " empieza su turno.", i);
                //Con esto conseguimos que se vuelvan a sincronizar todos los jugadores, tras meter mensajes en la cola (es el compromiso del protocolo).
            }            
            nuevoTurno();
        }
        if (getListaAcciones().contains(accion)) {
            switch (accion) {
                case FINALIZAR_TURNO:
                    finalizarTurno();
                    break;
                case MOVER_NORTE:
                    numAcciones--;
                    if (tablero.getCasilla(getX() - 1, getY()) == NivelFuego.FUEGO) {
                        numAcciones--;
                    }
                    moverNorte();
                    break;
                case MOVER_ESTE:
                    if (tablero.getCasilla(getX(), getY()+1) == NivelFuego.FUEGO) {
                        numAcciones--;
                    }
                    numAcciones--;
                    moverEste();
                    break;
                case MOVER_SUR:
                    if (tablero.getCasilla(getX() + 1, getY()) == NivelFuego.FUEGO) {
                        numAcciones--;
                    }
                    numAcciones--;
                    moverSur();
                    break;
                case MOVER_OESTE:
                    if (tablero.getCasilla(getX(), getY()-1) == NivelFuego.FUEGO) {
                        numAcciones--;
                    }
                    numAcciones--;
                    moverOeste();
                    break;
                case EXTINGUIR_NORTE:
                    numAcciones--;
                    extinguirNorte();
                    break;
                case EXTINGUIR_ESTE:
                    numAcciones--;
                    extinguirEste();
                    break;
                case EXTINGUIR_SUR:
                    numAcciones--;
                    extinguirSur();
                    break;
                case EXTINGUIR_OESTE:
                    numAcciones--;
                    extinguirOeste();
                    break;
                case ABIR_PUERTA_NORTE:
                    numAcciones--;
                    abrirPuertaNorte();
                    break;
                case ABIR_PUERTA_ESTE:
                    numAcciones--;
                    abrirPuertaEste();
                    break;
                case ABIR_PUERTA_SUR:
                    numAcciones--;
                    abrirPuertaSur();
                    break;
                case ABIR_PUERTA_OESTE:
                    numAcciones--;
                    abrirPuertaOeste();
                    break;
                case CERRAR_PUERTA_NORTE:
                    numAcciones--;
                    cerrarPuertaNorte();
                    break;
                case CERRAR_PUERTA_ESTE:
                    numAcciones--;
                    cerrarPuertaEste();
                    break;
                case CERRAR_PUERTA_SUR:
                    numAcciones--;
                    cerrarPuertaSur();
                    break;
                case CERRAR_PUERTA_OESTE:
                    numAcciones--;
                    cerrarPuertaOeste();
                    break;
            }
            for (int i = 0; i < numJugadores; i++) {
                //Podemos detallarlo un poco mas...
                miModelo.introducirMensajeEnCola("El jugador " + numero + " realizo una acción.", i);
                //Con esto conseguimos que se vuelvan a sincronizar todos los jugadores, tras meter mensajes en la cola (es el compromiso del protocolo).
            }
        }
    }

    //ACCIONES BÁSICAS:
    private void finalizarTurno() {
        tablero.nuevoTurno();
        numAccionesGuardadas = numAcciones;
        if (numAccionesGuardadas > 4) {
            numAccionesGuardadas = 4;
        }
    }

    @Override
    public void moverNorte() {
        if (tablero.getCasilla(getX() - 1, getY()) != NivelFuego.FUEGO && tablero.adyacentes(getX(), getY(), getX() - 1, getY()));
        setX(getX() - 1);
    }

    @Override
    public void moverSur() {
        if (tablero.getCasilla(getX() + 1, getY()) != NivelFuego.FUEGO && tablero.adyacentes(getX(), getY(), getX() + 1, getY()));
        setX(getX() + 1);
    }

    @Override
    public void moverEste() {
        if (tablero.getCasilla(getX(), getY() + 1) != NivelFuego.FUEGO && tablero.adyacentes(getX(), getY(), getX(), getY() + 1));
        setY(getY() + 1);
    }

    @Override
    public void moverOeste() {
        if (tablero.getCasilla(getX(), getY() - 1) != NivelFuego.FUEGO && tablero.adyacentes(getX(), getY(), getX(), getY() - 1));
        setY(getY() - 1);
    }

    public void liderarNorte(Transportable ob) {
        moverNorte();
        ob.moverNorte();
    }

    public void liderarSur(Transportable ob) {
        moverSur();
        ob.moverSur();
    }

    public void liderarEste(Transportable ob) {
        moverEste();
        ob.moverEste();
    }

    public void liderarOeste(Transportable ob) {
        moverOeste();
        ob.moverOeste();
    }

    public void extinguirNorte() {
        tablero.extinguirCasilla(getX() - 1, getY());
    }

    public void extinguirSur() {
        tablero.extinguirCasilla(getX() + 1, getY());

    }

    public void extinguirEste() {
        tablero.extinguirCasilla(getX(), getY() + 1);
    }

    public void extinguirOeste() {
        tablero.extinguirCasilla(getX(), getY() - 1);
    }

    public void abrirPuertaNorte() {
        for (Arista a : tablero.getAristas()) {
            if (a.getObstaculo() == Obstaculo.PUERTA_CERRADA && a.aristaDeCasillas(getX(), getY(), getX() - 1, getY())) {
                a.setObstaculo(Obstaculo.PUERTA_ABIERTA);
            }
        }
    }

    public void abrirPuertaSur() {
        for (Arista a : tablero.getAristas()) {
            if (a.getObstaculo() == Obstaculo.PUERTA_CERRADA && a.aristaDeCasillas(getX(), getY(), getX() + 1, getY())) {
                a.setObstaculo(Obstaculo.PUERTA_ABIERTA);
            }
        }
    }

    public void abrirPuertaEste() {
        for (Arista a : tablero.getAristas()) {
            if (a.getObstaculo() == Obstaculo.PUERTA_CERRADA && a.aristaDeCasillas(getX(), getY(), getX(), getY() + 1)) {
                a.setObstaculo(Obstaculo.PUERTA_ABIERTA);
            }
        }
    }

    public void abrirPuertaOeste() {
        for (Arista a : tablero.getAristas()) {
            if (a.getObstaculo() == Obstaculo.PUERTA_CERRADA && a.aristaDeCasillas(getX(), getY(), getX(), getY() - 1)) {
                a.setObstaculo(Obstaculo.PUERTA_ABIERTA);
            }
        }
    }

    public void cerrarPuertaNorte() {
        for (Arista a : tablero.getAristas()) {
            if (a.getObstaculo() == Obstaculo.PUERTA_ABIERTA && a.aristaDeCasillas(getX(), getY(), getX() - 1, getY())) {
                a.setObstaculo(Obstaculo.PUERTA_CERRADA);
            }
        }
    }

    public void cerrarPuertaSur() {
        for (Arista a : tablero.getAristas()) {
            if (a.getObstaculo() == Obstaculo.PUERTA_ABIERTA && a.aristaDeCasillas(getX(), getY(), getX() + 1, getY())) {
                a.setObstaculo(Obstaculo.PUERTA_CERRADA);
            }
        }
    }

    public void cerrarPuertaEste() {
        for (Arista a : tablero.getAristas()) {
            if (a.getObstaculo() == Obstaculo.PUERTA_ABIERTA && a.aristaDeCasillas(getX(), getY(), getX(), getY() + 1)) {
                a.setObstaculo(Obstaculo.PUERTA_CERRADA);
            }
        }
    }

    public void cerrarPuertaOeste() {
        for (Arista a : tablero.getAristas()) {
            if (a.getObstaculo() == Obstaculo.PUERTA_ABIERTA && a.aristaDeCasillas(getX(), getY(), getX(), getY() - 1)) {
                a.setObstaculo(Obstaculo.PUERTA_CERRADA);
            }
        }
    }

}
