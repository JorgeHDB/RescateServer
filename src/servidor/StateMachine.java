package servidor;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class StateMachine {
    
    private JFrame currentState;
    private Point posVentana;
    private Modelo miModelo;
    private Runnable vistaActual;
    public StateMachine() {
        miModelo = new Modelo();
        posVentana = new Point();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        vistaActual = new Runnable() {
            @Override
            public void run() {
                currentState = new VistaServidor(miModelo);
                //Coloca el programa en el centro de la pantalla.
                int x = (dim.width - currentState.getWidth())/2;
                int y = (dim.height - currentState.getHeight())/2;
                posVentana.setLocation(x, y);
                currentState.setLocation(posVentana);
                
                currentState.setVisible(true);
            }
        };
        java.awt.EventQueue.invokeLater( vistaActual);
    } 
    
    public void cambiaVistaPartida() {
        posVentana  = currentState.getLocation(); 
        currentState.dispose(); 
        vistaActual = new Runnable() {
                    @Override
                    public void run() {
                        currentState = new VistaPartida(miModelo);
                        currentState.setLocation(posVentana);
                        currentState.setVisible(true);
                    }
                };
        java.awt.EventQueue.invokeLater(vistaActual);
    }
    
    public void close() {
        currentState.dispose();
    } 
}