package servidor;

import cliente.Primitive;
import cliente.MensajeProtocolo;

import java.io.*;
import java.net.*;

class SirvienteRunnable implements Runnable {

    private final Socket socket;
    private int numero;
    private String nombre;
    private final Modelo miModelo;
    
    SirvienteRunnable(Socket s, Modelo modelo) {
        socket = s;
        miModelo = modelo;
    }

    @Override
    public void run() {
        /* preparamos los streams para objetos */
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());

            while (true) {
                MensajeProtocolo me = (MensajeProtocolo) ois.readObject();
                MensajeProtocolo ms = new MensajeProtocolo(Primitive.NOT_UNDERSTAND);
                System.out.println(numero + "<< " + me); /* mensaje entrante de depuración */

                switch (me.getPrimitive()) {
                    case HELLO:
                        String nombreRecibido;
                        nombreRecibido = me.getMessage();
                        int pos = miModelo.añadirCliente(nombreRecibido);
                        if(pos < 0){ //Ha ido mal, el nombre estaba ocupado o ya no hay hueco
                            ms = new MensajeProtocolo(Primitive.HELLO,"", false);
                            System.out.println("Entra un tio con el nombre repetido");//Debug
                        }
                        else{ //Ha ido bien, le han dado un hueco
                            nombre = nombreRecibido;
                            this.numero = pos;
                            ms = new MensajeProtocolo(Primitive.HELLO, "" + numero, true);
                            for (int i = 0; i < miModelo.getNumJugadores(); i++) {
                               if(i==numero)
                                    miModelo.introducirMensajeEnCola("Bienvenido. Eres el cliente número "+ (i+1),i);
                                else
                                    miModelo.introducirMensajeEnCola("Se conectó el cliente número "+ (numero+1), i);
                            }
                        }
                        break;

                    case WAIT_TURN:
                        //Servidor.recursoCompartido tiene cambios?
                        String men = miModelo.leerMensajeDeCola(numero);
                        if (miModelo.getTurno()!= numero || miModelo.getNumClientes() < miModelo.getNumJugadores())
                            ms = new MensajeProtocolo(Primitive.STATUS, men, false);
                        if (miModelo.getTurno() == numero && miModelo.getNumClientes() == miModelo.getNumJugadores())
                            ms = new MensajeProtocolo(Primitive.STATUS, men, true);
                        break;      
                    case GOODBYE: //El cliente se ha desconectado, le sacamos del array de nombres
                        miModelo.quitarCliente(numero);
                        break;
                    default:
                        ms = new MensajeProtocolo(Primitive.NOT_UNDERSTAND);
                } /* fin switch */

                oos.writeObject(ms);
                System.out.println(numero + ">> " + ms); /* mensaje saliente de depuración */
            }
        } catch (IOException | ClassNotFoundException e) {
            //System.err.println("Fin de Cliente");
            //System.err.println("Clase MensajeProtocolo no encontrada");
        }
         finally {
            /* seguimos deshaciéndonos de los sockets y canales abiertos. */
            try {
                ois.close();
                oos.close();
                socket.close();
            } catch (Exception x) {
                System.err.println("Sirviente: Error irrecuperable de cierre de sockets");
            }
        }
    }
}
