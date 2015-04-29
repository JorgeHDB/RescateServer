/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import rescate.Bombero;
import rescate.BomberoRemoto;
import rescate.Dificultad;
import rescate.Tablero;
import rescate.VersionTablero;

/**
 *
 * @author Jorge
 */
public final class Modelo {

    private InetAddress ip;
    private int rmiPort; // puerto para el socket de conexion
    private Dificultad dificultad;
    private int numJugadores;
    private Tablero tablero;
    private VersionTablero version;

    //Para RMI
    private ArrayList<BomberoRemoto> bomberos;
    private Registry registro;

    //Para el protocolo de sincronizacion
    private ArrayList<Socket> sockets;
    private ServerSocket serverSocket;
    private ExecutorService exec;
    
    private String nombresClientes[];
    private int numClientes;
    private ArrayList<ColaStrings> cola = new ArrayList<>();

    public Modelo() {
        //Para mostrar al creador de la partida en que ip se juega
        try {
            ip = InetAddress.getLocalHost();
        } catch (Exception e) {
        }
        rmiPort = 0;
        setRmiPort(1099); //Si no rula, saldrá 0 en la vista
        //Parámetros de partida iniciales
        numJugadores = 2;
        dificultad = Dificultad.NOVATO;
        version = VersionTablero.TABLERO1;

        sockets = new ArrayList<>();
        cola = new ArrayList<>();
        bomberos = new ArrayList<>();

    }

    /**
     * *
     * Inicializa el tablero y los bomberos en funcion del tablero escogido,
     * tipo de partida y del numero de jugadores que van a jugar. Además, enlaza
     * los bomberos al registro RMI para que los clientes puedan invocar
     * acciones a distancia
     */
    public void iniciarPartida() {
        tablero = new Tablero(version, dificultad, numJugadores);
        nombresClientes = new String[numJugadores];
        for (int i = 0; i < numJugadores; i++) {
            ColaStrings c = new ColaStrings();
            cola.add(c);
        }
        numClientes = 0;
        //Abrir nuevo hilo para el socket de mensajes..:
        (new Thread(new AccepterRunnable(this))).start();
        //Subir Objetos remotos para interactuar con el tablero:
        try {
            registro = LocateRegistry.createRegistry(1099); //Mas tarde en el puerto asignado..
            ArrayList<Bombero> bomberosNoRemotos = new ArrayList<>();
            for (int i = 0; i < numJugadores; i++) {
                ///OOOJOOOO ESTA MAL
                bomberosNoRemotos.add(new Bombero(2+i, 2+i, tablero,i, numJugadores, this));
                bomberos.add(new BomberoRemoto(bomberosNoRemotos.get(i)));
                registro.rebind("Bombero" + i, bomberos.get(i)); //Bombero0, Bombero1 ... 
                System.out.println("Bombero" + i + " enlazado.");
            }
            tablero.setBomberos(bomberosNoRemotos);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void limpiarRegistro() {
        try {
            for (int i = 0; i < numJugadores; i++) {
                registro.unbind("Bombero" + i); //Bombero0, Bombero1 ... 
                System.out.println("Bombero" + i + " desenlazado.");
            }
            UnicastRemoteObject.unexportObject(registro, true);
        } catch (RemoteException | NotBoundException re) {
            re.printStackTrace(System.err);
        }
    }

    public InetAddress getIp() {
        return ip;
    }

    private boolean available(int port) {
        try (Socket ignored = new Socket("localhost", port)) {
            return false;
        } catch (IOException ignored) {
            return true;
        }
    }

    /**
     * *
     * Intenta cambiar el puerto RMI, devuelve si se pudo hacer el cambio
     *
     * @param port Nuevo puerto para RMI
     * @return True si el puerto nuevo estaba libre, False en caso contrario
     */
    public boolean setRmiPort(int port) {
        if (available(port)) {
            this.rmiPort = port;
            return true;
        }
        return false;
    }

    public int getRmiPort() {
        return rmiPort;
    }

    public void setNumJugadores(int num) {
        numJugadores = num;
    }

    public int getNumJugadores() {
        return numJugadores;
    }

    public String[] getNombresClientes() {
        return nombresClientes;
    }

    public void setDificultad(Dificultad dificultad) {
        this.dificultad = dificultad;
    }
    
    public Dificultad getDificultad(){
        return dificultad;
    }

    public void setVersionTablero(VersionTablero versionTablero) {
        version = versionTablero;
    }
    
    public VersionTablero getVersionTablero(){
        return version;
    }
    
    int getTurno() {
        return tablero.getTurno();
    }

    private boolean nombreLibre(String nombreCliente) {
        for (int i = 0; i < numClientes; i++) {
            if (nombresClientes[i] == null) {
                continue;
            }
            if (nombresClientes[i].equals(nombreCliente)) {
                return false;
            }
        }
        return true;
    }

    /**
     * *
     * Elimina un cliente de la cola, cuando realiza la acción de Goodbye y hace
     * hueco a otro cliente.
     *
     * @param numeroCliente
     */
    public void quitarCliente(int numeroCliente) {
        assert (numeroCliente >= 0 && numeroCliente < numJugadores);
        numClientes--;
        System.out.println("Se salio " + nombresClientes[numeroCliente]);
        nombresClientes[numeroCliente] = null;
    }

    /**
     * *
     * Añade un cliente de nombre el string proporcionado y devuelve su numero
     * (posición en el turno). Si no hay hueco para el, le devolvera -1
     *
     * @param nombreCliente nombre del cliente que pide posicion
     * @return
     */
    public int añadirCliente(String nombreCliente) {
        if (nombreLibre(nombreCliente)) {
            for (int i = 0; i < numJugadores; i++) {
                if (nombresClientes[i] == null) {
                    numClientes++;
                    nombresClientes[i] = nombreCliente;
                    System.out.println("Se conecto " + nombreCliente);
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * * Introduce un mensaje en la Cola del mensajes para el jugador i
     *
     * @param mensaje mensaje a introducir en la cola
     * @param numJugador numero de la cola en la que se introduce el mensaje
     */
    public void introducirMensajeEnCola(String mensaje, int numJugador) {
        cola.get(numJugador).push(mensaje);
    }

    public String leerMensajeDeCola(int numJugador) {
        return cola.get(numJugador).pop();
    }

    int getNumClientes() {
        return numClientes;
    }

    public void addSocket(Socket s) {
        sockets.add(s);
    }

    public void cerrarSockets() {
        try {
            serverSocket.close();
            for(Socket s : sockets)
                s.close();
            exec.shutdown();
        } catch (IOException ex) {
            //Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setServerSocket(ServerSocket sock) {
        serverSocket = sock;
    }

    void setExecutor(ExecutorService exec) {
        this.exec = exec;
    }
}
