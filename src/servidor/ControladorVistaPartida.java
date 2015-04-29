/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

/**
 *
 * @author Jorge
 */
public class ControladorVistaPartida {
    private final StateMachine miMaquina;
    private final Modelo miModelo;
    private final VistaPartida miVista;

    public ControladorVistaPartida(Modelo modelo, VistaPartida vista){
        miMaquina = Main.getStateMachine();
        miModelo = modelo;
        miVista = vista;
    }
    
    public void cerrarServidor() {
        miModelo.limpiarRegistro();
        miModelo.cerrarSockets();
        miMaquina.close();
    }
    
}
