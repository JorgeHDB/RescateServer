/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rescate;

import cliente.Imagen;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jorge
 */
public final class Tablero {

    private Casilla[][] tablero;
    private ArrayList<Arista> aristas;
    private int numeroDeDaños;
    private ArrayList<MateriaPeligrosa> materiasPeligrosas;
    private ArrayList<Bombero> bomberos;
    private int numeroDeFocos;
    private int numeroDeBomberos;
    private int numDeTurnos;

    /*Creación de un tablero inicial para una partida de Rescate con las reglas
     avanzadas, en función de la dificultad, número de bomberos y version del tablero
     pasados como parametro.
     @param dificultad Dificultad entre Novato, Veterano o Heroico.
     @param numBomberos Cantidad de bomberos con los que se juega la partida.
     */
    public Tablero(VersionTablero version, Dificultad nivel, int numBomberos) {
        tablero = new Casilla[8][10]; //Primero el d6, luego el d8
        aristas = new ArrayList<>();
        materiasPeligrosas = new ArrayList<>();
        numeroDeBomberos = numBomberos;
        numDeTurnos = 0;
        numeroDeDaños = 0;
        numeroDeFocos = 6;
        if (nivel == Dificultad.HEROICO) {
            numeroDeFocos = 12;
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 10; j++) {
                tablero[i][j] = new Casilla(); //Se ponen las casillas sin humo y fuego y sin focos de calor
            }
        }
        //TABLERO FACIL
        if (version == VersionTablero.TABLERO1) {
            //Paredes exteriores
            for (int i = 1; i < 7; i++) {
                if (i != 4) //Puerta siempre abierta
                {
                    aristas.add(new Arista(i, 9, i, 8, Obstaculo.PARED));
                }
                if (i != 3) //Puerta siempre abierta
                {
                    aristas.add(new Arista(i, 0, i, 1, Obstaculo.PARED));
                }
            }
            for (int i = 1; i < 9; i++) {
                if (i != 6) //Puerta de entrada
                {
                    aristas.add(new Arista(0, i, 1, i, Obstaculo.PARED));
                }
                if (i != 3) //Puerta de entrada
                {
                    aristas.add(new Arista(6, i, 7, i, Obstaculo.PARED));
                }
            }
            //Pared horizontal ue cruza a lo largo el edificio
            for (int i = 1; i < 9; i++) {
                if (i != 4) {
                    aristas.add(new Arista(4, i, 5, i, Obstaculo.PARED));
                } else {
                    aristas.add(new Arista(4, i, 5, i, Obstaculo.PUERTA_CERRADA));
                }
            }
            //Pared horizontal que cruza del 3 al 8
            for (int i = 3; i < 8; i++) {
                aristas.add(new Arista(2, i, 3, i, Obstaculo.PARED));
            }
            aristas.add(new Arista(2, 8, 3, 8, Obstaculo.PUERTA_CERRADA));
            //Puertas en vertical seguidas de la pared que las continua
            aristas.add(new Arista(6, 7, 6, 8, Obstaculo.PUERTA_CERRADA));
            aristas.add(new Arista(5, 7, 5, 8, Obstaculo.PARED));

            aristas.add(new Arista(6, 5, 6, 6, Obstaculo.PUERTA_CERRADA));
            aristas.add(new Arista(5, 5, 5, 6, Obstaculo.PARED));

            aristas.add(new Arista(4, 6, 4, 7, Obstaculo.PUERTA_CERRADA));
            aristas.add(new Arista(3, 6, 3, 7, Obstaculo.PARED));

            aristas.add(new Arista(3, 2, 3, 3, Obstaculo.PUERTA_CERRADA));
            aristas.add(new Arista(4, 2, 4, 3, Obstaculo.PARED));

            aristas.add(new Arista(2, 5, 2, 6, Obstaculo.PUERTA_CERRADA));
            aristas.add(new Arista(1, 5, 1, 6, Obstaculo.PARED));

            aristas.add(new Arista(1, 3, 1, 4, Obstaculo.PUERTA_CERRADA));
            aristas.add(new Arista(2, 3, 2, 4, Obstaculo.PARED));
        }

        if (version == VersionTablero.TABLERO2) {
            for (int i = 1; i < 7; i++) {
                aristas.add(new Arista(i, 9, i, 8, Obstaculo.PARED));
                if (i != 3) //Puerta siempre abierta
                {
                    aristas.add(new Arista(i, 0, i, 1, Obstaculo.PARED));
                }
            }
            for (int i = 1; i < 9; i++) {
                aristas.add(new Arista(0, i, 1, i, Obstaculo.PARED));
                if (i != 3) //Puerta de entrada
                {
                    aristas.add(new Arista(6, i, 7, i, Obstaculo.PARED));
                }
            }
            //Pared horizontal arriba
            for (int i = 1; i < 7; i++) {
                if (i != 3 && i != 6) {
                    aristas.add(new Arista(4, i, 5, i, Obstaculo.PARED));
                } else {
                    aristas.add(new Arista(4, i, 5, i, Obstaculo.PUERTA_CERRADA));
                }
            }
            //Segunda pared horizontal, para hacer pasillo
            for (int i = 4; i < 9; i++) {
                if (i != 5 && i != 6) {
                    aristas.add(new Arista(3, i, 4, i, Obstaculo.PARED));
                } else {
                    aristas.add(new Arista(3, i, 4, i, Obstaculo.PUERTA_CERRADA));
                }
            }

            //Paredes restantes horizontales:
            aristas.add(new Arista(2, 1, 3, 1, Obstaculo.PARED));
            aristas.add(new Arista(2, 2, 3, 2, Obstaculo.PARED));
            aristas.add(new Arista(1, 4, 2, 4, Obstaculo.PARED));
            aristas.add(new Arista(1, 5, 2, 5, Obstaculo.PARED));

            //Paredes y puertas en vertical:
            aristas.add(new Arista(6, 6, 6, 7, Obstaculo.PARED));
            aristas.add(new Arista(5, 6, 5, 7, Obstaculo.PARED));

            aristas.add(new Arista(6, 3, 6, 4, Obstaculo.PARED));
            aristas.add(new Arista(5, 3, 5, 4, Obstaculo.PARED));

            aristas.add(new Arista(4, 6, 4, 7, Obstaculo.PUERTA_CERRADA));

            aristas.add(new Arista(3, 3, 3, 4, Obstaculo.PARED));
            aristas.add(new Arista(3, 5, 3, 6, Obstaculo.PUERTA_CERRADA));

            aristas.add(new Arista(2, 3, 2, 4, Obstaculo.PARED));
            aristas.add(new Arista(2, 5, 2, 6, Obstaculo.PARED));

            aristas.add(new Arista(1, 5, 1, 6, Obstaculo.PUERTA_CERRADA));
            aristas.add(new Arista(1, 3, 1, 4, Obstaculo.PARED));
        }

        //Generación aleatoria de explosiones y asignacion de focos de calor
        //Primera explosion //Cambiar por funcion que añade focos de calor en ese sitio
        Random rnd = new Random();
        switch (rnd.nextInt(8) + 1) {
            case 1:
                explosion(3, 3);
                tablero[3][3].ponerFocoDeCalor();
                break;
            case 2:
                explosion(3, 4);
                tablero[3][4].ponerFocoDeCalor();
                break;
            case 3:
                explosion(3, 5);
                tablero[3][5].ponerFocoDeCalor();
                break;
            case 4:
                explosion(3, 6);
                tablero[3][6].ponerFocoDeCalor();
                break;
            case 5:
                explosion(4, 6);
                tablero[4][6].ponerFocoDeCalor();
                break;
            case 6:
                explosion(4, 5);
                tablero[4][5].ponerFocoDeCalor();
                break;
            case 7:
                explosion(4, 4);
                tablero[4][4].ponerFocoDeCalor();
                break;
            case 8:
                explosion(4, 3);
                tablero[4][3].ponerFocoDeCalor();
                break;
        }

        //Segunda explosion
        int d6, d8;
        do {
            d6 = rnd.nextInt(6) + 1;
            d8 = rnd.nextInt(8) + 1;
        } while (getCasilla(d6, d8) != NivelFuego.SIN_FUEGO);
        explosion(d6, d8);
        tablero[d6][d8].ponerFocoDeCalor();

        //Tercera explosion (dar la vuelta al dado 8 y usar el dado 6.
        // Si la casilla estuviera en llamas volver a tirar el dado 6.
        d8 = dadoOchoOpuesto(d8);
        while (getCasilla(d6, d8) != NivelFuego.SIN_FUEGO) {
            d6 = rnd.nextInt(6) + 1;
        }
        explosion(d6, d8);
        tablero[d6][d8].ponerFocoDeCalor();

        //Cuarta explosion en el caso de que sea nivel heroico:
        if (nivel == Dificultad.HEROICO) {
            do {
                d6 = rnd.nextInt(6) + 1;
                d8 = rnd.nextInt(8) + 1;
            } while (this.getCasilla(d6, d8) != NivelFuego.SIN_FUEGO);
            explosion(d6, d8);
            tablero[d6][d8].ponerFocoDeCalor();
        }

        //Añadido de materias peligrosas: !!COMPROBAR QUE NO HABIA YA UNA ASIGNADA A ESE HUECO
        int numMaterias = 0;
        switch (nivel) {
            case NOVATO:
                numMaterias = 3;
                break;
            case VETERANO:
                numMaterias = 4;
                break;
            case HEROICO:
                numMaterias = 5;
                break;
        }
        for (int i = 0; i < numMaterias; i++) {
            boolean salir;
            do {
                salir = true;
                d6 = rnd.nextInt(6) + 1;
                d8 = rnd.nextInt(8) + 1;
                if (getCasilla(d6, d8) != NivelFuego.SIN_FUEGO) {
                    salir = false;
                }
                for (MateriaPeligrosa m : materiasPeligrosas) {
                    if (m.misCoord(d6, d8)) //Comprobación de que el foco no existia ya
                    {
                        salir = false;
                    }
                }
            } while (!salir);
            materiasPeligrosas.add(new MateriaPeligrosa(d6, d8, this));
        }

        //Añadido de focos de calor adicionales:
        int numFocos = 0;
        if (nivel == Dificultad.HEROICO || nivel == Dificultad.VETERANO) {
            numFocos += 3;
        }
        if (numBomberos == 3) {
            numFocos += 2;
        }
        if (numBomberos > 3) {
            numFocos += 3;
        }

        for (int i = 0; i < numFocos; i++) {
            while (true) {
                d6 = rnd.nextInt(6) + 1;
                d8 = rnd.nextInt(8) + 1;
                if (!tablero[d6][d8].hayFocoDeCalor()) {
                    break;
                }
            }
            tablero[d6][d8].ponerFocoDeCalor();
        }
    }

    public void setBomberos(ArrayList<Bombero> bombs){
        bomberos= bombs;
    }
    /*
     public void nuevoTurno(Scanner sc){
     int seleccion = 0;
     if(this.numDeTurnos< this.numeroDeBomberos){
     do{
     System.out.println(this.toString());
     System.out.println("Selecciona donde quieres que empiece el bombero:\n1: Puerta Sur\n2: Puerta Este\n3: Puerta Norte\n4: Puerta Oeste");
     seleccion = sc.nextInt();
     sc.nextLine();
     }
     while(seleccion<1 || seleccion > 4);
     switch(seleccion){
     case 1: bomberos[numDeTurnos] = new Bombero(0,6); break;
     case 2: bomberos[numDeTurnos] = new Bombero(3,0); break;
     case 3: bomberos[numDeTurnos] = new Bombero(7,3); break;
     case 4: bomberos[numDeTurnos] = new Bombero(4,9); break;                                        
     }
     }
     //Reiniciar puntos de acción del bombero
     bomberos[numDeTurnos%numeroDeBomberos].nuevoTurno();
     do{
     System.out.println(toString());
     System.out.println(bomberos[numDeTurnos%numeroDeBomberos].listarAcciones(this));
     seleccion = sc.nextInt();
     sc.nextLine();
     bomberos[numDeTurnos%numeroDeBomberos].realizarAccion(this, seleccion);
     } while(seleccion !=0);
        
     propagar(false); //Propagar sin foco de calor, obv
        
        
     numDeTurnos++;
     }*/
    /**
     *
     * @return
     */
    public ArrayList<MateriaPeligrosa> getMateriasPeligrosas() {
        return materiasPeligrosas;
    }

    /*
     public String listarMateriasPeligrosas(){
     String res = "";
     for(MateriaPeligrosa m : materiasPeligrosas){
     res+="Materia peligrosa en "+m.getX() + "," + m.getY()+ "\n"; 
     }
     return res;
     }
    
     public String listarFocosDeCalor(){
     String res = "";
     for(FocoDeCalor f : focosDeCalor){
     res+="Foco de calor en "+f.getX() + "," + f.getY()+ "\n"; 
     }
     return res;
     }*/
    /**
     * *
     * Aumenta en uno los daños estructurales del edificio. La partida se acaba
     * si los daños superan cierto numero
     */
    private void aumentarDaños() {
        this.numeroDeDaños++;
    }

    /**
     * *
     * Devuelve el numero de daños que ha recibido el edificio en su estructura
     *
     * @return
     */
    public int getNumeroDeDaños() {
        return numeroDeDaños;
    }

    /*Numero que se encuentra en el lado opuesto del numero sacado en la
     tirada de un dado de ocho caras.
     */
    private int dadoOchoOpuesto(int num) {
        switch (num) {
            case 1:
                return 6;
            case 2:
                return 5;
            case 3:
                return 8;
            case 4:
                return 7;
            case 5:
                return 2;
            case 6:
                return 1;
            case 7:
                return 4;
            case 8:
                return 3;
            default:
                return 1;
        }
    }

    /*Tirar los dados y propagar el fuego en la casilla resultante. Si ponerFoco
     DeCalor está a true, es que antes caimos en una casilla con foco de calor,
     por lo que habrá que poner si quedan un foco de calor en la casilla que toque.
     */
    private void propagar(boolean ponerFocoDeCalor) {
        Random rnd = new Random();
        int x = rnd.nextInt(6) + 1;
        int y = rnd.nextInt(8) + 1;
        propagar(x, y);
        if (ponerFocoDeCalor) { //Primero compruebo que no se han puesto ya todos los focos de calor,
            // y despues si en esacasilla no habia focos de calor ya.
            if (this.numeroDeFocos > 0) {
                if (!tablero[x][y].hayFocoDeCalor()) {
                    numeroDeFocos--;
                    tablero[x][y].ponerFocoDeCalor();
                }
            }
        }
        lanzaderaLlamarada();
    }

    /*Tras tirar los dados, en función de lo que hubiera previamente en la casilla
     se propaga el fuego.
     Si estaba vacía aparece humo, si estaba en humo se torna en llamas y
     si ya habia fuego se produce una explosión.
     A continuación de propagar se hace llamarada (los humos al lado de fuego
     combustionan y forman fuego)
     */
    private void propagar(int dado6, int dado8) {
        if (getCasilla(dado6, dado8) == NivelFuego.SIN_FUEGO) {
            setCasillaEnHumo(dado6, dado8);
        } else if (getCasilla(dado6, dado8) == NivelFuego.HUMO) {
            setCasillaEnFuego(dado6, dado8);
        } else if (getCasilla(dado6, dado8) == NivelFuego.FUEGO) {
            explosion(dado6, dado8);
        }

        //Y además , si en esta casilla hay un foco de calor, hay que volver a propagar, poniendo
        //en la casilla resultante un nuevo foco de calor...
        if (tablero[dado6][dado8].hayFocoDeCalor()) {
            propagar(true);
        }

    }

    /*Genera una explosión en la casilla solicitada
     */
    private void explosion(int dado6, int dado8) {
        //Para cuando creamos un juego aleatorio, no siempre tiene porqué estar
        //esta casilla previamente en llamas...
        //System.out.println("Se produjo una explosión en ("+dado6+","+dado8+")");
        this.setCasillaEnFuego(dado6, dado8);
        explosionNorte(dado6, dado8);
        explosionSur(dado6, dado8);
        explosionEste(dado6, dado8);
        explosionOeste(dado6, dado8);
    }

    /***La explosion se propaga por una arista, y devuelve true en caso de
     * que la propagacion se para a causa de que encuentra una puerta o una
     * pared.
     * @param a arista por la que pasa la explosión.
     * @return 
     */
    private boolean explosionPasaPorArista(Arista a){
        boolean seParaLaExplosion = false;
        Obstaculo obs = a.getObstaculo();
        if(obs != Obstaculo.PUERTA_ABIERTA && obs != Obstaculo.PARED_ROTA && obs !=Obstaculo.PUERTA_EXPLOTADA){
            seParaLaExplosion = true;
            if(obs == Obstaculo.PARED){
                a.setObstaculo(Obstaculo.PARED_DAÑADA);
                aumentarDaños();
            }
            if(obs == Obstaculo.PARED_DAÑADA){
                a.setObstaculo(Obstaculo.PARED_ROTA);
                aumentarDaños();
            }
            if(obs == Obstaculo.PUERTA_CERRADA)
                a.setObstaculo(Obstaculo.PUERTA_EXPLOTADA);
        }
        return seParaLaExplosion;
    }
    /*Propaga una explosión en el sentido norte del tablero
     */
    private void explosionNorte(int dado6, int dado8) {
        boolean muro = false;
        for (Arista a : aristas) {
            if (a.aristaDeCasillas(dado6 - 1, dado8, dado6, dado8)) 
                if(explosionPasaPorArista(a))
                    muro = true;
        }
        if (!muro) { //No encontro un muro inmediatamente arriba
            if (getCasilla(dado6 - 1, dado8) == NivelFuego.SIN_FUEGO) {
                setCasillaEnFuego(dado6 - 1, dado8);
            } else {
                explosionNorte(dado6 - 1, dado8);
            }
        }
    }

    /*Propaga una explosión en el sentido sur del tablero
     */
    private void explosionSur(int dado6, int dado8) {
         boolean muro = false;
        for (Arista a : aristas) {
            if (a.aristaDeCasillas(dado6 + 1, dado8, dado6, dado8)) 
                if(explosionPasaPorArista(a))
                    muro = true;
        }
        if (!muro) { //No encontro un muro inmediatamente arriba
            if (getCasilla(dado6 + 1, dado8) == NivelFuego.SIN_FUEGO) {
                setCasillaEnFuego(dado6 + 1, dado8);
            } else {
                explosionNorte(dado6 + 1, dado8);
            }
        }
    }

    /*Propaga una explosión en el sentido este del tablero
     */
    private void explosionEste(int dado6, int dado8) {
        boolean muro = false;
        for (Arista a : aristas) {
            if (a.aristaDeCasillas(dado6, dado8+1, dado6, dado8)) 
                if(explosionPasaPorArista(a))
                    muro = true;
        }
        if (!muro) { //No encontro un muro inmediatamente arriba
            if (getCasilla(dado6, dado8 + 1) == NivelFuego.SIN_FUEGO) {
                setCasillaEnFuego(dado6, dado8 + 1);
            } else {
                explosionNorte(dado6, dado8 + 1);
            }
        }
    }

    /*Propaga una explosión en el sentido oeste del tablero
     */
    private void explosionOeste(int dado6, int dado8) {
        boolean muro = false;
        for (Arista a : aristas) {
            if (a.aristaDeCasillas(dado6, dado8-1, dado6, dado8)) 
                if(explosionPasaPorArista(a))
                    muro = true;
        }
        if (!muro) { //No encontro un muro inmediatamente arriba
            if (getCasilla(dado6, dado8-1) == NivelFuego.SIN_FUEGO) {
                setCasillaEnFuego(dado6, dado8-1);
            } else {
                explosionNorte(dado6, dado8-1);
            }
        }
    }

    /*Devuelve el contenido de una casilla (vacia, humo o fuego)
     */
    public NivelFuego getCasilla(int dado6, int dado8) {
        if (dado6 < 0 || dado6 > 7 || dado8 < 0 || dado8 > 9) {
            return NivelFuego.SIN_FUEGO;
        }
        return tablero[dado6][dado8].getNivelDeFuego();
    }

    public void extinguirCasilla(int dado6, int dado8) {
        if (dado6 > 0 && dado6 < 7 && dado8 > 0 && dado8 < 9) {
            if (getCasilla(dado6, dado8) == NivelFuego.HUMO) {
                tablero[dado6][dado8].setNivelDeFuego(NivelFuego.SIN_FUEGO);
            }
            if (getCasilla(dado6, dado8) == NivelFuego.FUEGO) {
                tablero[dado6][dado8].setNivelDeFuego(NivelFuego.HUMO);
            }
        }
    }

    private void setCasillaEnHumo(int dado6, int dado8) {
        if (dado6 > 0 && dado6 < 7 && dado8 > 0 && dado8 < 9) {
            tablero[dado6][dado8].setNivelDeFuego(NivelFuego.HUMO);
        }
    }

    private void setCasillaEnFuego(int dado6, int dado8) {
        if (dado6 > 0 && dado6 < 7 && dado8 > 0 && dado8 < 9) {
            tablero[dado6][dado8].setNivelDeFuego(NivelFuego.FUEGO);
        }
    }

    /**
     * *
     * Convierte las casillas de humo adyacentes a un fuego en fuego. Para ello
     * hay que hacer un método recursivo.
     */
    private void lanzaderaLlamarada() {
        boolean[][] comp;
        comp = new boolean[8][10];
        for (int i = 1; i < 7; i++) {
            for (int j = 1; j < 9; j++) {
                if (getCasilla(i, j) == NivelFuego.FUEGO) { //Si hay fuego, comprobamos si sus
                    //adyacentes son de humo, y si lo son, volvemos a comprobar sus adyacentes...
                    llamarada(i, j, comp);
                }
            }
        }
    }

    /*Convierte las casillas en humo que estan al lado de un fuego en fuego.
  
     */
    private void llamarada(int dado6, int dado8, boolean[][] comp) {
        //Miramos que no se haya chequeado ya en esta casilla
        if (comp[dado6][dado8] == false) {
            comp[dado6][dado8] = true;
            //Comprobacion en los cuatro sentidos:
            if (getCasilla(dado6 - 1, dado8) == NivelFuego.HUMO) {
                if (adyacentes(dado6, dado8, dado6 - 1, dado8)) {
                    setCasillaEnFuego(dado6 - 1, dado8);
                    llamarada(dado6 - 1, dado8, comp);
                }
            }
            if (getCasilla(dado6 + 1, dado8) == NivelFuego.HUMO) {
                if (adyacentes(dado6, dado8, dado6 + 1, dado8)) {
                    setCasillaEnFuego(dado6 + 1, dado8);
                    llamarada(dado6 + 1, dado8, comp);
                }
            }
            if (getCasilla(dado6, dado8 - 1) == NivelFuego.HUMO) {
                if (adyacentes(dado6, dado8, dado6, dado8 - 1)) {
                    setCasillaEnFuego(dado6, dado8 - 1);
                    llamarada(dado6, dado8 - 1, comp);
                }
            }
            if (getCasilla(dado6, dado8 + 1) == NivelFuego.HUMO) {
                if (adyacentes(dado6, dado8, dado6, dado8 + 1)) {
                    setCasillaEnFuego(dado6, dado8 + 1);
                    llamarada(dado6, dado8 + 1, comp);
                }
            }
        }
    }

    /*Devuelve un arrayList con las aristas del tablero
     */
    public ArrayList<Arista> getAristas() {
        return aristas;
    }

    /*Devuelve si es posible moverse entre dos casillas (es decir, no hay puerta cerrada 
     ni pared entre ellas) Tambien indica si el humo se expande por contacto entre esas
     dos casillas
     */
    public boolean adyacentes(int cas1X, int cas1Y, int cas2X, int cas2Y) {
        boolean ady = true;
        if (abs(cas1X - cas2X) > 1 || abs(cas1Y - cas2Y) > 1) {
            ady = false;
        }
        for (Arista a : aristas) {
            if (a.aristaDeCasillas(cas1X, cas1Y, cas2X, cas2Y)) {
                Obstaculo obs = a.getObstaculo();
                if (obs == Obstaculo.PARED || obs == Obstaculo.PARED_DAÑADA || obs == Obstaculo.PUERTA_CERRADA) {
                    ady = false;
                }
            }
        }
        return ady;
    }
    /*
     @Override
     public String toString(){
     String res = "";
     for (int j = 7; j >=0; j--){
     for(int i = 9; i >=0 ; i--){
     boolean encontrado = false;
     Obstaculo obs =Obstaculo.PARED;
     for(Arista a: aristas){
     if(a.aristaDeCasillas(j,i,j,i+1)){
     obs=a.getObstaculo();
     encontrado = true;
     }
     }
     if(encontrado){ 
     if( obs == Obstaculo.PUERTA_CERRADA) res+="D";
     if( obs == Obstaculo.PARED_DAÑADA) res+="{";
     if (obs == Obstaculo.PARED) res+="|";
     if( obs == Obstaculo.PUERTA_ABIERTA) res+=" ";
     }
     else res+=" ";
                
     if(tablero[j][i]==Casilla.VACIO) res+=" \u00B7 ";
     if(tablero[j][i]==Casilla.HUMO) res+=" h ";
     if(tablero[j][i]==Casilla.FUEGO) res+=" F ";
     for(int k = 0; k < numeroDeBomberos; k++){
     if(bomberos[k].misCoord(j, i) ) res = res.substring(0, res.length()-2) + k + " ";
     }
     }
     res+="\n";
     for(int i=9;i>=0;i--){
     boolean encontrado = false;
     for(Arista a : aristas){
     if(a.aristaDeCasillas(j, i, j-1, i)){
     encontrado=true;
     if(a.getObstaculo()==Obstaculo.PARED) res+="----";
     if(a.getObstaculo()==Obstaculo.PARED_DAÑADA) res+="~~~~";
     if(a.getObstaculo()==Obstaculo.PUERTA_CERRADA) res+="-|\\|";
     if(a.getObstaculo()==Obstaculo.PUERTA_ABIERTA) res+="-| |";
     }
     }
     if(!encontrado) res+="    ";
     }
     res+="\n";
     }
     return res;
        
     }*/

    public void nuevoTurno() {
        propagar(false); //Propagamos el fuego
        numDeTurnos++;
        bomberos.get(getTurno()).realizarAccion(AccionBombero.NUEVO_TURNO);
    }

    public int getTurno() {
        return numDeTurnos % numeroDeBomberos;
    }

    public ArrayList<Imagen> getImagenes() {
        ArrayList<Imagen> ret = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 10; j++) {
                if(getCasilla(i, j) == NivelFuego.FUEGO)
                    ret.add(new Imagen("src\\recursos\\Fuego.png",105+ 72*j,20 + 73*i));
                if(getCasilla(i,j) == NivelFuego.HUMO)
                    ret.add(new Imagen("src\\recursos\\Humo.png",105+ 72*j,20 + 73*i));

            }
        }
        for(int i = 0; i < numeroDeBomberos; i++){
            ret.add(new Imagen("src\\recursos\\FireCaptain"+i+".png",105+ 72*bomberos.get(i).getY(),20 + 73*bomberos.get(i).getX()));
        }
        return ret;
    }
}
