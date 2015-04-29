package cliente;

public enum Primitive {

    HELLO(true,true), //Cliente -> servidor, indica nombre jugador. 
                 //Servidor -> cliente, indica si la conexion fue exitosa o no + numero cliente            
    WAIT_TURN(false,false), // Cliente -> servidor, espera a cambios por el resto de jugadores y a que le digan si es su turno
    STATUS(true,true), // Servidor -> cliente, devuelve string con el ultimo cambio en el tablero y le dice si es su turno.
    GOODBYE(false,false),
    NOT_UNDERSTAND(false,false);

    /* msg: TRue significa que le acompaña mensaje, y false que no. */
    private final boolean msg;
    private final boolean bool;
    
    Primitive(boolean b, boolean c) {
        this.msg = b;
        this.bool = c;
    }

    public boolean hasMessage() {
        return this.msg;
    }

    public boolean hasBoolean(){
        return this.bool;
    }

    @Override
    public String toString() {
        return this.name() + msg;
    }
}
