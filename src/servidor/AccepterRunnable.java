/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Jorge
 */
public class AccepterRunnable implements Runnable{
    
    private final Modelo miModelo;
    private static final int PUERTO = 2000; /* puerto de escucha del ServerSocket */
    private final ExecutorService exec;

    public AccepterRunnable(Modelo modelo){
        miModelo = modelo;
        exec = Executors.newFixedThreadPool(miModelo.getNumJugadores());
        miModelo.setExecutor(exec);
    }
    
    @Override
    public void run() {
          try {
            ServerSocket sock = new ServerSocket(PUERTO);
            miModelo.setServerSocket(sock);
            System.out.println("Servidor ejecutandose");
            while (true) {
                Socket s = sock.accept();
                miModelo.addSocket(s);
                exec.execute(new SirvienteRunnable(s, miModelo));
            }
        } catch (IOException ioe) {
            //System.err.println("Servidor: Error de E/S: " + ioe);
        }
    }
}
