package cliente;

import java.io.*;

public class MensajeProtocolo implements Serializable {

    private Primitive primitiva;
    private String mensaje;
    private boolean bool;
    
    public MensajeProtocolo(Primitive p) {
        /* hemos metido dos assert por la peligrosidad del constructor
         * recordad que para activar los assert en la JVM se usa: java -ae ...
         * proviene de "assert enable" */
        assert p.hasMessage()== false && p.hasBoolean() == false;

        this.primitiva = p;
        this.mensaje = "";
    }

    public MensajeProtocolo(Primitive p, String m) {
        assert p.hasMessage()== true && m != null;
        this.primitiva = p;
        this.mensaje = m;
    }
    
    public MensajeProtocolo(Primitive p, String m, boolean b) {
        assert p.hasMessage()== true  && p.hasBoolean() == true && m != null;
        this.primitiva = p;
        this.mensaje = m;
        this.bool = b;
    }

    public Primitive getPrimitive() {
        return this.primitiva;
    }

    public String getMessage() {
        return this.mensaje;
    }
    
    public boolean getBoolean(){
        return bool;
    }
    public String toString(){
        return primitiva.toString();
    }
}
