package servidor;

import java.util.*;

public class ColaStrings {

    private ArrayList<String> lista = new ArrayList<>();

    public synchronized void push(final String p) {
        lista.add(p);
        this.notify(); // hace saber que ha llegado un String
    }

    public synchronized String pop() {
        while (lista.isEmpty()) {
            try {
                this.wait(); // espera la llegada de un String
            } catch (final InterruptedException e) {
            }
        }
        return lista.remove(0);
    }

    public boolean estaVacia() {
        return lista.isEmpty();
    }
}
