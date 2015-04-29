/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rescate;

import cliente.Imagen;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Jorge
 */
public interface BomberoInterfaz extends Remote {

    public ArrayList<AccionBombero> getListaAcciones() throws RemoteException;

    public void realizarAccion(AccionBombero accion) throws RemoteException;

    //método para pedir el estado del tablero... no se como hacerlo aun ...
    public ArrayList<Imagen> getTablero() throws RemoteException;
}
