package org.hugom;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.util.*;

public class Controlador {
    private static Controlador singletonControlador = null;
    public static void setSingletonControlador(Controlador singletonControlador) {
        Controlador.singletonControlador = singletonControlador;
    }

    public static ControladorSonido controladorSonido = new ControladorSonido();

    public static int ventanaActual = 0;

    public static HashMap<String, Fantasma> listaFantasmas = new HashMap<>();
    public static Jugador jugador;

    static ArrayList<ArrayList<String>> estructuraVisualMapa;
    static ArrayList<ArrayList<String>> estructuraFuncionalMapa;

    static HojaSprites hojaSprites;
    static HojaSprites hojaSprites_blanca;
    static HojaSprites hojaSpritesContadorNivel;

    public static int nivelActual = 0;
    public static int puntuacion = 0;

    public static Boolean juegoEnCurso = false;
    public static double juegoMomentoInicio = ahora() + Constantes.COOLDOWN_INICIO_GAME;

    public static Boolean perdido = false;
    public static Boolean restadoPerdido = false;

    public static boolean nivelFinalizado = false;
    public static double momentoParpadeo = -1;
    public static boolean nivelParpadeando = false;
    public static boolean parpadeoBlanco = false;
    public static double siguienteParpadeo = -1;

    public static boolean partidaFinalizada = false;
    public static boolean partidaFinalizadaMostrado = false;


    public static Boolean huidaFantasmas = false;
    public static long finHuidaFantasmas = 0;

    public static ArrayList<Integer> vidasDadas = new ArrayList<>();

    public static String esperaRazon = "null";
    public static double esperaMomento = -1;



    private Controlador() throws IOException, ParseException {
        estructuraVisualMapa = cargar_estructura().get(0);
        estructuraFuncionalMapa = cargar_estructura().get(1);

        // Cargamos los sprites del mapa (Estructuras, estructuras en blanco [animacion avance de nivel] y frutas para indicar el nivel actual)
        hojaSprites = new HojaSprites("/media/imagen/spritesheet.png", "/datos/indicesSprites/spritesMapa.json");
        hojaSprites_blanca = new HojaSprites("/media/imagen/spritesheet_blanco.png", "/datos/indicesSprites/spritesMapa.json");
        hojaSpritesContadorNivel = new HojaSprites("/media/imagen/frutas.png", "/datos/indicesSprites/spritesFrutas.json");


        jugador         = new Jugador(Constantes.POS_JUGADOR,   "der");
        Rojo rojo       = new Rojo(Constantes.POS_ROJO,         "izq", new Posicion(10, 0), EstadosFantasma.ATAQUE);
        Rosa rosa       = new Rosa(Constantes.POS_ROSA,         "abj",  new Posicion(29, 0), EstadosFantasma.ESPERASPAWNINICIAL);
        Azul azul       = new Azul(Constantes.POS_AZUL,         "arr",   new Posicion(10, 0), EstadosFantasma.ESPERASPAWNINICIAL);
        Naranja naranja = new Naranja(Constantes.POS_NARANJA,   "arr",  new Posicion(29, 0), EstadosFantasma.ESPERASPAWNINICIAL);

        listaFantasmas.put("rojo", rojo);
        listaFantasmas.put("rosa", rosa);
        listaFantasmas.put("azul", azul);
        listaFantasmas.put("naranja", naranja);
    }


    /**
     * Funcion que se encarga de que suene el sonido de muerte mientras un fantasma este muerto y regresando al spawn.
     */
    public static void tocarSonidosMuerteFantasma(){
        boolean algunFantasmaMuerto = false;
        for(String fantasma: listaFantasmas.keySet()) {
            if (listaFantasmas.get(fantasma).getEstado() == EstadosFantasma.MUERTO){
                algunFantasmaMuerto = true;
                break;
            }
        }
        if (algunFantasmaMuerto) {
            if (!controladorSonido.getVueltaSpawnFantasma().isPlaying())
                controladorSonido.getVueltaSpawnFantasma().play();
        } else if (controladorSonido.getVueltaSpawnFantasma().isPlaying())
            controladorSonido.getVueltaSpawnFantasma().stop();
    }


    /**
     * Funcion que se encarga de dibujar todos los fantasmas mediante la funcion {@link Fantasma#dibujar(GraphicsContext)}, solo
     * si se cumplen los requisitos necesarios.
     * @param gc en el que se dibujaran los fantasmas.
     */
    public static void dibujarFantasmas(GraphicsContext gc){
        if (perdido || nivelParpadeando) return; // Si hemos perdido o si el nivel se encuentra en animacion de parpadeo, no se dibujan
        for(String fantasma: listaFantasmas.keySet()){
            listaFantasmas.get(fantasma).dibujar(gc);
        }
    }

    /**
     * Metodo que cuenta y devuelve la cantidad de frutas/potenciadores del mapa.
     * @return la cantidad de frutas/potenciadores, en integer.
     */
    public static int comprobarFrutas(){
        int contadorFrutas = 0;
        ArrayList<String> simbolosFruta = new ArrayList<>(Arrays.asList(".", ":", "+"));
        for(ArrayList<String> fila: estructuraFuncionalMapa)
            for(String elemento: fila)
                if(simbolosFruta.contains(elemento))
                    contadorFrutas+=1;
        return contadorFrutas;
    }

    public static void siguienteNivel(){
        ArrayList<ArrayList<ArrayList<String>>> estructurasCargadas = cargar_estructura();
        estructuraVisualMapa = estructurasCargadas.get(0);
        estructuraFuncionalMapa = estructurasCargadas.get(1);

        nivelActual += 1;
        nivelFinalizado = false;
        nivelParpadeando = false;
        reiniciarPosiciones(2000);
        juegoEnCurso = false;
        juegoMomentoInicio = ahora() + 2000;
    }


    /**
     * Funcion para reiniciar solamente las posiciones y variables basicas del jugador y fantasmas.
     * @param miliSegundosExtra que se quieren añadir al tiempo de spawn de cada fantasma.
     */
    public static void reiniciarPosiciones(double miliSegundosExtra){
        jugador.reiniciar(miliSegundosExtra);
        for(String fantasma: listaFantasmas.keySet())
            listaFantasmas.get(fantasma).reiniciar(miliSegundosExtra);
    }


    /**
     * Funcion que ayuda a mover todos los fantasmas a la vez. Tambien, se encarga de sacar a los fantasmas del spawn
     * cuando sea su momento.
     * TODO mejorarlo
     */
    public static void moverFantasmas(){
        for(String fantasma: listaFantasmas.keySet()){
            listaFantasmas.get(fantasma).mover();
            listaFantasmas.get(fantasma).detectarColisionJugador(jugador);

            if (listaFantasmas.get(fantasma).estado == EstadosFantasma.ESPERASPAWNINICIAL)
                if (ahora() > listaFantasmas.get(fantasma).momentoSpawnInicial) // Si es el momento en el que el fantasma le toca salir
                    listaFantasmas.get(fantasma).cambiarEstado(EstadosFantasma.ESPERASPAWN);
        }



        if (huidaFantasmas && ahora() > finHuidaFantasmas){ // Modo HUIDA finalizado
            huidaFantasmas = false;
            controladorSonido.getHuidaFantasmas().stop();
            for(String fantasma: listaFantasmas.keySet()){
                if (listaFantasmas.get(fantasma).getEstado() == EstadosFantasma.HUIDA){
                    listaFantasmas.get(fantasma).setEstado(EstadosFantasma.ATAQUE);
                }
            }
        }
    }

    /**
     * Funcion para forzar a que todos los fantasmas adopten el mismo estado. Se usara para DEBUG (teclas 1-4) y cuando el jugador
     * come un potenciador, haciendo que todos huyan de él.
     * @param nuevoEstado al que se quiere forzar.
     */
    public static void actualizarEstadosFantasmas(EstadosFantasma nuevoEstado){
        for(String fantasma: listaFantasmas.keySet())
            listaFantasmas.get(fantasma).cambiarEstado((nuevoEstado));
    }


    /**
     * Funcion para establecer los objetivos de todos los fantasmas, el cual dependera el estado de cada uno.
     */
    public static void establecerObjetivosFantasmas(){
        for(String fantasma: listaFantasmas.keySet()){
            if (listaFantasmas.get(fantasma).estado == EstadosFantasma.ATAQUE){
                if (fantasma.equals("azul"))
                    (listaFantasmas.get("azul")).establecerObjetivoAtaque(jugador, (Rojo)listaFantasmas.get("rojo")); // El fantasma azul es el unico que depende del antasma rojo para su propio movimiento
                else
                    listaFantasmas.get(fantasma).establecerObjetivoAtaque(jugador, null);
            }
        }
    }


    /**
     * Funcion que fuerza a todos los fantasmas a adoptar el estado de {@link EstadosFantasma#HUIDA} mediante la funcion {@link Fantasma#cambiarEstado(EstadosFantasma)}.
     * Tambien se encarga de calcular el tiempo en el que la huida acaba, sonidos de fondo, etc.
     */
    public static void forzarHuidaFantasmas(){
        huidaFantasmas = true;

        for(String fantasma: listaFantasmas.keySet()){
            EstadosFantasma estado = listaFantasmas.get(fantasma).getEstado();
            if (estado != EstadosFantasma.MUERTO && estado != EstadosFantasma.ESPERASPAWN && estado != EstadosFantasma.ESPERASPAWNINICIAL)
                listaFantasmas.get(fantasma).cambiarEstado(EstadosFantasma.HUIDA);
        }

        finHuidaFantasmas = ahora() + 10 * 1000;

        if(controladorSonido.getHuidaFantasmas().isPlaying()){
            controladorSonido.getHuidaFantasmas().stop();
        }
        controladorSonido.getHuidaFantasmas().play();
    }

    public static Controlador getInstance() throws IOException, ParseException {
        if(singletonControlador == null){
            singletonControlador = new Controlador();
        }
        return singletonControlador;
    }


    /**
     * Funcion que carga la estructura del mapa, tanto la visual (elementos distintos del mapa) como la funcional (elementos indistintos, los cuales
     * seran o pared, o comida, o aire.
     * @return un ArrayList con ambas estructuras, la visual en el indice 0, y la funcional en el indice 1.
     */
    static ArrayList<ArrayList<ArrayList<String>>> cargar_estructura()
    {
        InputStream archivo_ruta = Controlador.class.getResourceAsStream("/datos/mapeado.txt");
        ArrayList<ArrayList<String>> estructuraVisual = new ArrayList<>();
        ArrayList<ArrayList<String>> estructuraFuncional = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(archivo_ruta))) {
            String line;
            while ((line = br.readLine()) != null) { // Iteramos sobre cada linea del mapeado.txt
                ArrayList<String> caracteresLinea = new ArrayList<>(Arrays.asList(line.split("(?<=.)"))); // Separamos los caracteres de la linea
                ArrayList<String> estructuraFuncionalLinea = new ArrayList<>();
                ArrayList<String> elementosTranspasables = new ArrayList<>(Arrays.asList("_", ".", ":", "-", "+")); // Elementos traspasables del mapa (aire, monedas, etc)

                for(String letra: caracteresLinea){ // Iteramos sobre cada caracter de cada linea del mapeado.txt
                    if (!elementosTranspasables.contains(letra)) { // Si el elemento no es traspasable, entonces lo tomaremos como un obstaculo, sea cual sea (una esquina, una vertical, etc)
                        if (letra.equals("G")) // G es un obstaculo para todos, menos para los fantasmas que salen del Spawn
                            estructuraFuncionalLinea.add("G");
                        else
                            estructuraFuncionalLinea.add("#");
                    } else { //Si es traspasable, añadimos su valor propio (asi diferenciamos si es aire, moneda, moneda grande, etc.
                        estructuraFuncionalLinea.add(letra);
                    }
                }
                estructuraFuncional.add(estructuraFuncionalLinea); // Una vez finalizada la linea, la añadimos a la estructura funcional general.
                estructuraVisual.add(caracteresLinea); // También añadimos la estructura visual
            }
        } catch (IOException e) {
            System.err.format("[ERROR] Ha ocurrido un error al leer el mapeado (\"" + archivo_ruta + "\")");
        }
        
        
        return new ArrayList<>(Arrays.asList(estructuraVisual, estructuraFuncional));
    }


    /**
     * Funcion que se encarga de dibujar el mapa, apartir de la estructura visual.
     * @param gc en el que se dibujara el mapa.
     */
    static void dibujarMapeado(GraphicsContext gc){
        for(int v = 0; v < estructuraVisualMapa.size(); v++){
            for(int h = 0; h < estructuraVisualMapa.get(0).size(); h++){
                double posX = h * Constantes.ESCALADO_SPRITE * Constantes.CUADRICULA_MAPA - 2 * Constantes.CUADRICULA_MAPA * Constantes.ESCALADO_SPRITE;
                double posY = v * Constantes.ESCALADO_SPRITE * Constantes.CUADRICULA_MAPA;


                if (parpadeoBlanco)
                    gc.drawImage(hojaSprites_blanca.getSpriteData().get(estructuraVisualMapa.get(v).get(h)), posX, posY, Constantes.ESCALADO_SPRITE * Constantes.CUADRICULA_MAPA, Constantes.ESCALADO_SPRITE * Constantes.CUADRICULA_MAPA);
                else
                    gc.drawImage(hojaSprites.getSpriteData().get(estructuraVisualMapa.get(v).get(h)), posX, posY, Constantes.ESCALADO_SPRITE * Constantes.CUADRICULA_MAPA, Constantes.ESCALADO_SPRITE * Constantes.CUADRICULA_MAPA);
            }
        }
    }


    /**
     * Funcion que se encarga de dibujar el contador del nivel actual en la parte inferior derecha de la pantalla,
     * siguiendo el algoritmo original del juego.
     * @param gc en el que se dibujara el contador.
     */
    public static void dibujarContadorNivel(GraphicsContext gc){
        int posX = 14;
        int posY = 34;
        ArrayList<String> sprites = new ArrayList<>(Arrays.asList(null, null, null, null, null, null, null));

        if (nivelActual < 1) // Asegurarse de que es 1
            nivelActual = 1;

        if (nivelActual < 8) { // Caso A, puede que haya vacios
            for (int i = 1; i < nivelActual + 1; i++) {
                sprites.set(sprites.size() - i, "nivel_" + i);
            }
        } else if (nivelActual <= 18){ // Caso B, 7 elementos, puede que haya repetidos
            for(int i = nivelActual - 6; i < nivelActual + 1; i++) {
                sprites.set(6 - (nivelActual - i), "nivel_" + i);
            }
            Collections.reverse(sprites);
        } else { // Caso C, los 7 elementos son llaves
            for (int i = 0; i < sprites.size(); i++)
                sprites.set(i, "nivel_" + 20);
        }

        for(int i = 0; i < sprites.size(); i++){
            if(sprites.get(i) != null){
                Image imagen = hojaSpritesContadorNivel.getSpriteData().get(sprites.get(i));
                gc.drawImage(imagen, (posX + (i * 2)) * 8 * Constantes.ESCALADO_SPRITE - 2 * 8 * Constantes.ESCALADO_SPRITE, posY * 8 * Constantes.ESCALADO_SPRITE, imagen.getWidth(), imagen.getHeight());
            }
        }
    }

    /**
     * Funcion que se encarga de dibujar la cantidad de vidas restantes del jugador en la parte inferior izquierda
     * de la pantalla, en forma de sprites de Pacman
     * @param gc en el que se dibujaran las vidas restantes.
     */
    public static void dibujarVidas(GraphicsContext gc){
        int posX = 4;
        int posY = 34;

        for (int i=0; i < jugador.getVidasRestantes(); i++){
            Image imagen = jugador.getHojaSprites().getSpriteData().get("frame_1");
            gc.drawImage(imagen, (posX + (i * 2)) * 8 * Constantes.ESCALADO_SPRITE - 2 * 8 * Constantes.ESCALADO_SPRITE, posY * 8 * Constantes.ESCALADO_SPRITE, imagen.getWidth(), imagen.getHeight());
        }
    }


    /**
     * Funcion que se encarga de realizar los cambios necesarios para reiniciar el juego.
     */
    public static void iniciarJuego(){
        System.out.println("INICIAR JUEGO LLAMADO");
        juegoMomentoInicio = ahora() + Constantes.COOLDOWN_INICIO_GAME;
        reiniciarPosiciones(Constantes.COOLDOWN_INICIO_GAME);
    }

    /**
     * Funcion que se encarga de reiniciar TODAS las variables necesarias para jugar de nuevo, desde 0.
     */
    public static void reiniciarJuego(){
        ventanaActual = 0;
        juegoEnCurso = false;
        jugador.setConVida(true);
        cargar_estructura();
        siguienteNivel();
        nivelActual = 0;
        puntuacion = 0;
        jugador.setVidasRestantes(Constantes.VIDAS_INICIALES);
        huidaFantasmas = false;
        finHuidaFantasmas = -1;
        perdido = false;
        restadoPerdido = false;
        nivelFinalizado = false;
        momentoParpadeo = -1;
        nivelParpadeando = false;
        siguienteParpadeo = -1;
        partidaFinalizada = false;
        partidaFinalizadaMostrado = false;
        vidasDadas = new ArrayList<>();
        esperaMomento = -1;
        esperaRazon = "null";
    }


    /**
     * Funcion que devuelve el momento actual en mili segundos.
     * @return cantidad actual en mili segundos (long).
     */
    public static long ahora(){
        return System.currentTimeMillis();
    }
}
