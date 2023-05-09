package org.hugom;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javafx.scene.image.Image;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class HojaSprites {
    private final String rutaImagen;
    private final String rutaIndices;

    private HashMap<String, Image> spriteData;

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
        HashMap<String, DatoSprite> datosJSON = leerJSON(this.rutaIndices);
        HashMap<String, Image> sprites = new HashMap<>(SpriteLoader.loadSprites(this.rutaImagen, datosJSON));

        System.out.println("[CARGA] Cargado sprite con el nombre \"" + rutaImagen + "\"");

        return sprites;
    }


    public HashMap<String, DatoSprite> leerJSON(String rutaJSON) throws IOException, ParseException {

        HashMap<String, DatoSprite> spritesCargados = new HashMap<>();

        InputStream streamRutaJSON = getClass().getResourceAsStream(rutaJSON);

//        FileReader archivoJSON = null;
        BufferedReader archivoJSON = null;
        try {
//            archivoJSON = new FileReader(rutaJSON);
            archivoJSON = new BufferedReader(new InputStreamReader(streamRutaJSON));
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

        public DatoSprite(Dimension dimension, Posicion posicion) {
            this.dimension = dimension;
            this.posicion = posicion;
        }
    }
}
