import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Controlador {
    private static Controlador singletonControlador = null;

    static ArrayList<ArrayList<String>> estructuraVisualMapa;
    static ArrayList<ArrayList<String>> estructuraFuncionalMapa;

    static HojaSprites hojaSprites;
    static HojaSprites hojaSpritesNivel;

    public static int nivelActual;
    public static int puntuacion;

    public static boolean partidaEmpezada = true;

    public static EstadosFantasma estadoGlobal = null; // Solo se utilizara para conocer si estan en modo huida o no
    public static long finHuidaFantasmas;

    public static ControladorSonido controladorSonido;


    private Controlador() throws IOException, ParseException {
        ArrayList<ArrayList<ArrayList<String>>> estructurasCargadas = cargar_estructura();
        estructuraVisualMapa = estructurasCargadas.get(0);
        estructuraFuncionalMapa = estructurasCargadas.get(1);

        hojaSpritesNivel = new HojaSprites("src/media/imagen/frutas.png", "src/datos/indicesSprites/spritesFrutas.json");
        hojaSprites = new HojaSprites("src/media/imagen/spritesheet.png", "src/datos/indicesSprites/spritesMapa.json");

        puntuacion = 0;
        finHuidaFantasmas = ahora();
        controladorSonido = new ControladorSonido();
    }

    public static void forzarHuidaFantasmas(){
        estadoGlobal = EstadosFantasma.HUIDA;
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
        String archivo_ruta = "src/datos/mapeado.txt";
        ArrayList<ArrayList<String>> estructuraVisual = new ArrayList<>();
        ArrayList<ArrayList<String>> estructuraFuncional = new ArrayList<ArrayList<String>>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo_ruta))) {
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
        } else if (nivelActual > 7 && nivelActual <= 18){ // Caso B, 7 elementos, puede que haya repetidos
            for(int i = nivelActual - 6; i < nivelActual + 1; i++) {
                sprites.set(6 - (nivelActual - i), "nivel_" + i);
            }
            Collections.reverse(sprites);
        } else if (nivelActual > 18) { // Caso C, los 7 elementos son llaves
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


    public static long ahora(){
        return System.currentTimeMillis();
    }
}
