/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rescate;

import cliente.Imagen;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author Jorge
 */
public class BomberoRemoto extends UnicastRemoteObject implements BomberoInterfaz {

    private final Bombero b;

    public BomberoRemoto(Bombero bom) throws RemoteException {
        b = bom;
    }

    @Override
    public ArrayList<AccionBombero> getListaAcciones() throws RemoteException {
        return b.getListaAcciones();
    }

    @Override
    public void realizarAccion(AccionBombero accion) throws RemoteException {
        b.realizarAccion(accion);
    }

    @Override
    public ArrayList<Imagen> getTablero() throws RemoteException {
        return b.getTablero().getImagenes();
    }
}
