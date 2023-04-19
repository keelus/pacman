import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javafx.scene.image.Image;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class HojaSprites {
    private String rutaImagen;
    private String rutaIndices;

    private HashMap<String, Image> spriteData;

    public String getRutaImagen() {
        return rutaImagen;
    }
    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
    public String getRutaIndices() {
        return rutaIndices;
    }
    public void setRutaIndices(String rutaIndices) {
        this.rutaIndices = rutaIndices;
    }
    public HashMap<String, Image> getSpriteData() {
        return spriteData;
    }
    public void setSpriteData(HashMap<String, Image> spriteData) {
        this.spriteData = spriteData;
    }

    public HojaSprites(String rutaImagen, String rutaIndices) throws IOException, ParseException {
        this.rutaImagen = rutaImagen;
        this.rutaIndices = rutaIndices;

        this.setSpriteData(cargarSprites());
    }

    public HashMap<String, Image> cargarSprites() throws IOException, ParseException {
        HashMap<String, Image> sprites = new HashMap<>();

        HashMap<String, DatoSprite> datosJSON = leerJSON(this.rutaIndices);
        sprites.putAll(SpriteLoader.loadSprites(this.rutaImagen, datosJSON));

        System.out.println("[CARGA] Cargado sprite con el nombre \"" + rutaImagen + "\"");

        return sprites;
    }


    public HashMap<String, DatoSprite> leerJSON(String rutaJSON) throws IOException, ParseException {

        HashMap<String, DatoSprite> spritesCargados = new HashMap<String, DatoSprite>();


        FileReader archivoJSON = null;
        try {
            archivoJSON = new FileReader(rutaJSON);
        } catch (Exception e) {
            System.out.println("[CARGA] Error al cargar el archivo \""+ rutaJSON +"\"");
            System.exit(-1);
        }

        Object o = new JSONParser().parse(archivoJSON);
        JSONObject j = (JSONObject) o;

        Iterator<String> llaves = j.keySet().iterator();

        //System.out.println(llaves.toArray())


        while (llaves.hasNext()) {
            String llave = llaves.next();
            JSONObject objetoActual = (JSONObject) j.get(llave);


            ArrayList<Long> dimensiones = (ArrayList<Long>) objetoActual.get("dimensiones");
            ArrayList<Long> coordenadas = (ArrayList<Long>) objetoActual.get("coordenadas");

            Dimension vectorDimensiones = new Dimension(dimensiones.get(0).intValue(), dimensiones.get(1).intValue());
            Posicion vectorCoordenadas = new Posicion(coordenadas.get(0).intValue(), coordenadas.get(1).intValue());

            DatoSprite datoNuevo = new DatoSprite(vectorDimensiones, vectorCoordenadas);


            spritesCargados.put(llave, datoNuevo);

        }


        System.out.println("[CARGA] Cargado JSON con el nombre \"" + rutaJSON + "\"" + "\n\t•" + j.size() + " indices");
        return spritesCargados;
    }

    public class DatoSprite{
        private Dimension dimension;
        private Posicion posicion;

        public Dimension getDimension() {
            return dimension;
        }
        public void setDimension(Dimension dimension) {
            this.dimension = dimension;
        }
        public Posicion getPosicion() {
            return posicion;
        }
        public void setPosicion(Posicion posicion) {
            this.posicion = posicion;
        }

        public DatoSprite() {
        }

        public DatoSprite(Dimension dimension, Posicion posicion) {
            this.dimension = dimension;
            this.posicion = posicion;
        }
    }
}
