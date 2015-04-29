/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import rescate.Dificultad;
import rescate.VersionTablero;

public class ControladorServidor {
    private final StateMachine miMaquina;
    private final Modelo miModelo;
    private final VistaServidor miVista;
    private int lastRmiPort;
    public ControladorServidor(Modelo mod, VistaServidor vist){
        miMaquina= Main.getStateMachine();
        miModelo = mod;
        miVista = vist;
        lastRmiPort = miModelo.getRmiPort();
    }

    public void cambiarPuerto(int port) {
        if(!miModelo.setRmiPort(port))
            miModelo.setRmiPort(lastRmiPort);
        else 
            lastRmiPort = port;
        miVista.actualizar();
    }

    public void empezarServidor() {
        //Obtener los valores de la vista: Dificultad
        switch(miVista.getDificultad()){
            case "Novato":
                miModelo.setDificultad(Dificultad.NOVATO);
                break;
            case "Veterano":
                miModelo.setDificultad(Dificultad.VETERANO);
                break;
            case "Heroico":
                miModelo.setDificultad(Dificultad.HEROICO);
                break;
        }
        //Numero de jugadores
        miModelo.setNumJugadores(miVista.getNumJugadores());
        //Tipo de tablero:
        switch(miVista.getVersionTablero()){
            case "Tablero1":
                miModelo.setVersionTablero(VersionTablero.TABLERO1);
                break;
            case "Tablero2":
                miModelo.setVersionTablero(VersionTablero.TABLERO2);
                break;
        }       
        //System.out.println("Antes de darle a iniciar partida he leido "+ miModelo.getDificultad().name()
        //        + " " + miModelo.getNumJugadores() + " " +miModelo.getVersionTablero().name());
        miModelo.iniciarPartida();
        miMaquina.cambiaVistaPartida();
    }
}
