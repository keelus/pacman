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

    static ArrayList<ArrayList<String>> estructuraVisualMapa;
    static ArrayList<ArrayList<String>> estructuraFuncionalMapa;

    static HojaSprites hojaSprites;
    static HojaSprites hojaSpritesNivel;

    public static int nivelActual;
    public static int puntuacion;
    public static int vidasJugador;

    public static boolean partidaEmpezada = true;

    public static Boolean huidaFantasmas = false;
    public static long finHuidaFantasmas;

    public static ControladorSonido controladorSonido;

    public static HashMap<String, Fantasma> listaFantasmas = new HashMap<>();
    public static Jugador jugador;
    public static double momentoPerder = -1;
    public static Boolean perdido = false;
    public static Boolean restadoPerdido = false;

    public static Boolean juegoEnCurso = false;
    public static double juegoMomentoInicio = ahora() + Constantes.COOLDOWN_INICIO_GAME;

    public static int ventanaActual = 0;



    private Controlador() throws IOException, ParseException {
        ArrayList<ArrayList<ArrayList<String>>> estructurasCargadas = cargar_estructura();
        estructuraVisualMapa = estructurasCargadas.get(0);
        estructuraFuncionalMapa = estructurasCargadas.get(1);

        hojaSpritesNivel = new HojaSprites("/media/imagen/frutas.png", "/datos/indicesSprites/spritesFrutas.json");
        hojaSprites = new HojaSprites("/media/imagen/spritesheet.png", "/datos/indicesSprites/spritesMapa.json");

        puntuacion = 0;
        finHuidaFantasmas = ahora();
        controladorSonido = new ControladorSonido();

        jugador = new Jugador(new Posicion(16, 26), "der", new HojaSprites("/media/imagen/jugador3.png", "/datos/indicesSprites/spritesJugador.json"), 0, 0, 0, 0, 0);
        Rojo rojo       = new Rojo(new Posicion(16, 14), "izq", new HojaSprites("/media/imagen/fantasma.png", "/datos/indicesSprites/spritesFantasmas.json"), 0, 0, 0,  new Posicion(10, 0), EstadosFantasma.ATAQUE, new Posicion(27, -1), new Posicion(14, 17));
        Rosa rosa       = new Rosa(new Posicion(15, 17), "abj", new HojaSprites("/media/imagen/fantasma.png", "/datos/indicesSprites/spritesFantasmas.json"), 0, 0, 0, new Posicion(29, 0), EstadosFantasma.ESPERASPAWNINICIAL, new Posicion(4, -1), new Posicion(14, 17));
        Azul azul       = new Azul(new Posicion(14, 17), "arr", new HojaSprites("/media/imagen/fantasma.png", "/datos/indicesSprites/spritesFantasmas.json"), 0, 0, 0, new Posicion(10, 0), EstadosFantasma.ESPERASPAWNINICIAL, new Posicion(29, 34), new Posicion(14, 17));
        Naranja naranja = new Naranja(new Posicion(17, 17), "arr", new HojaSprites("/media/imagen/fantasma.png", "/datos/indicesSprites/spritesFantasmas.json"), 0, 0, 0, new Posicion(29, 0), EstadosFantasma.ESPERASPAWNINICIAL, new Posicion(2, 34), new Posicion(14, 17));
        listaFantasmas.put("rojo", rojo);
        listaFantasmas.put("rosa", rosa);
        listaFantasmas.put("azul", azul);
        listaFantasmas.put("naranja", naranja);

        vidasJugador = 3;

    }
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
    public static void dibujarFantasmas(GraphicsContext gc){
        if (perdido) return;
        for(String fantasma: listaFantasmas.keySet()){
            listaFantasmas.get(fantasma).dibujar(gc);
        }
    }
    public static void reiniciarPosiciones(double miliSegundosExtra){
        jugador.reiniciar(miliSegundosExtra);
        for(String fantasma: listaFantasmas.keySet())
            listaFantasmas.get(fantasma).reiniciar(miliSegundosExtra);
    }
    public static void moverFantasmas(){
        for(String fantasma: listaFantasmas.keySet()){
            listaFantasmas.get(fantasma).mover();

            listaFantasmas.get(fantasma).detectarColisionJugador(jugador);


            if (listaFantasmas.get(fantasma).estado == EstadosFantasma.ESPERASPAWNINICIAL)
                if (ahora() > listaFantasmas.get(fantasma).momentoSpawnInicial)
                    listaFantasmas.get(fantasma).cambiarEstado(EstadosFantasma.ESPERASPAWN);
        }



        if (huidaFantasmas && ahora() > finHuidaFantasmas){ // Modo HUIDA finalizado
            huidaFantasmas = false;
            controladorSonido.getHuidaFantasmas().stop();
            for(String fantasma: listaFantasmas.keySet()){
                if (listaFantasmas.get(fantasma).getEstado() == EstadosFantasma.HUIDA){
                    if (listaFantasmas.get(fantasma).getEstadoAnterior() == EstadosFantasma.ESPERASPAWN)
                        listaFantasmas.get(fantasma).setEstado(EstadosFantasma.ESPERASPAWN);
                    listaFantasmas.get(fantasma).setEstado(EstadosFantasma.ATAQUE);
                }
            }

        }

    }
    public static void actualizarEstadosFantasmas(EstadosFantasma nuevoEstado){
        listaFantasmas.get("rojo").cambiarEstado((nuevoEstado));
        listaFantasmas.get("rosa").cambiarEstado((nuevoEstado));
        listaFantasmas.get("azul").cambiarEstado((nuevoEstado));
        listaFantasmas.get("naranja").cambiarEstado((nuevoEstado));
    }
    public static void establecerObjetivosFantasmas(){
        for(String fantasma: listaFantasmas.keySet()){
            if(listaFantasmas.get("rojo").estado == EstadosFantasma.ATAQUE)
                ((Rojo)listaFantasmas.get("rojo")).establecerObjetivoAtaque(jugador, null);
            if(listaFantasmas.get("rosa").estado == EstadosFantasma.ATAQUE)
                ((Rosa)listaFantasmas.get("rosa")).establecerObjetivoAtaque(jugador, null);
            if(listaFantasmas.get("azul").estado == EstadosFantasma.ATAQUE)
                ((Azul)listaFantasmas.get("azul")).establecerObjetivoAtaque(jugador, (Rojo)listaFantasmas.get("rojo")); // El fantasma azul es el unico que depende del antasma rojo para su propio movimiento
            if(listaFantasmas.get("naranja").estado == EstadosFantasma.ATAQUE)
                ((Naranja)listaFantasmas.get("naranja")).establecerObjetivoAtaque(jugador, null);
        }
    }
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




    static ArrayList<ArrayList<ArrayList<String>>> cargar_estructura()
    {
        InputStream archivo_ruta = Controlador.class.getResourceAsStream("/datos/mapeado.txt");
        ArrayList<ArrayList<String>> estructuraVisual = new ArrayList<>();
        ArrayList<ArrayList<String>> estructuraFuncional = new ArrayList<ArrayList<String>>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(archivo_ruta))) {
            String line;
            while ((line = br.readLine()) != null) { // Iteramos sobre cada linea del mapeado.txt
                ArrayList<String> caracteresLinea = new ArrayList<String>(Arrays.asList(line.split("(?<=.)"))); // Separamos los caracteres de la linea
                ArrayList<String> estructuraFuncionalLinea = new ArrayList<String>();
                ArrayList<String> elementosTranspasables = new ArrayList<String>(Arrays.asList("_", ".", ":", "-", "+")); // Elementos traspasables del mapa (aire, monedas, etc)

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

        ArrayList<ArrayList<ArrayList<String>>> ambasEstructuras = new ArrayList<ArrayList<ArrayList<String>>>(Arrays.asList(estructuraVisual, estructuraFuncional));


        return ambasEstructuras;
    }


    static void dibujarMapeado(GraphicsContext gc){
        for(int v = 0; v < estructuraVisualMapa.size(); v++){
            for(int h = 0; h < estructuraVisualMapa.get(0).size(); h++){
                double posX = h * Constantes.ESCALADO_SPRITE * Constantes.CUADRICULA_MAPA - 2 * Constantes.CUADRICULA_MAPA * Constantes.ESCALADO_SPRITE;
                double posY = v * Constantes.ESCALADO_SPRITE * Constantes.CUADRICULA_MAPA;

                gc.drawImage(hojaSprites.getSpriteData().get(estructuraVisualMapa.get(v).get(h)), posX, posY, Constantes.ESCALADO_SPRITE * Constantes.CUADRICULA_MAPA, Constantes.ESCALADO_SPRITE * Constantes.CUADRICULA_MAPA);
            }
        }
    }

    public static void dibujarNivel(GraphicsContext gc){
        int posX = 14;
        int posY = 34;
        ArrayList<String> sprites = new ArrayList<String>(Arrays.asList(null, null, null, null, null, null, null));

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
                Image imagen = hojaSpritesNivel.getSpriteData().get(sprites.get(i));
                gc.drawImage(imagen, (posX + (i * 2)) * 8 * Constantes.ESCALADO_SPRITE - 2 * 8 * Constantes.ESCALADO_SPRITE, posY * 8 * Constantes.ESCALADO_SPRITE, imagen.getWidth(), imagen.getHeight());
            }
        }
    }

    public static void dibujarVidas(GraphicsContext gc){
        int posX = 4;
        int posY = 34;

        for (int i=0; i < vidasJugador; i++){
            Image imagen = jugador.getHojaSprites().getSpriteData().get("frame_1");
            gc.drawImage(imagen, (posX + (i * 2)) * 8 * Constantes.ESCALADO_SPRITE - 2 * 8 * Constantes.ESCALADO_SPRITE, posY * 8 * Constantes.ESCALADO_SPRITE, imagen.getWidth(), imagen.getHeight());
        }
    }

    public static void iniciarJuego(){
        juegoMomentoInicio = ahora() + Constantes.COOLDOWN_INICIO_GAME;
        reiniciarPosiciones(Constantes.COOLDOWN_INICIO_GAME);


    }


    public static long ahora(){
        return System.currentTimeMillis();
    }
}
