package org.hugom;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

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

    public HojaSprites(String rutaImagen, String rutaIndices) {
        this.rutaImagen = rutaImagen;
        this.rutaIndices = rutaIndices;

        this.setSpriteData(cargarSprites());
    }

    /**
     * Funcion que carga los sprites de las imagenes, usando la propia ruta del JSON cargado, y la propia ruta de la imagen.
     * @return Un hashmap con los indices del JSON como indices, y directamente las imagenes cargadas en vez de los valores de las coordenadas y tamaños.
     */
    public HashMap<String, Image> cargarSprites() {
        HashMap<String, DatoSprite> datosJSON = leerJSON(this.rutaIndices);
        HashMap<String, Image> sprites = new HashMap<>(CargadorDeSprites.cargarSprites(this.rutaImagen, datosJSON));

        System.out.println("[CARGA] Cargado sprite con el nombre \"" + rutaImagen + "\"");

        return sprites;
    }


    /**
     * Funcion para leer los JSON con los indices y coordenadas de los sprites desde un archivo JSON, devolviendo un hashmap
     * con estos valores, cada uno en su llave correspondiente.
     * @param rutaJSON Ruta del archivo JSON que se quiere cargar
     * @return Un hashmap del estilo del JSON cargado, con sus llaves y sus valores
     */
    public HashMap<String, DatoSprite> leerJSON(String rutaJSON) {
        HashMap<String, DatoSprite> spritesCargados = new HashMap<>();
        InputStream streamRutaJSON = getClass().getResourceAsStream(rutaJSON);

        if (streamRutaJSON == null) {
            System.out.println("[CARGA] Error al cargar el archivo \""+ rutaJSON +"\"");
            System.exit(-1);
        }

        try {
            InputStreamReader jsonCargado = new InputStreamReader(streamRutaJSON);
            Object o = new JSONParser().parse(jsonCargado);
            JSONObject j = (JSONObject) o;

            for(Object llave: j.keySet()) {
                JSONObject objetoActual = (JSONObject) j.get(llave);


                ArrayList<Long> dimensiones = (ArrayList<Long>) objetoActual.get("dimensiones");
                ArrayList<Long> coordenadas = (ArrayList<Long>) objetoActual.get("coordenadas");

                Dimension vectorDimensiones = new Dimension(dimensiones.get(0).intValue(), dimensiones.get(1).intValue());
                Posicion vectorCoordenadas = new Posicion(coordenadas.get(0).intValue(), coordenadas.get(1).intValue());

                DatoSprite datoNuevo = new DatoSprite(vectorDimensiones, vectorCoordenadas);

                spritesCargados.put(String.valueOf(llave), datoNuevo);
            }

            System.out.println("[CARGA] Cargado JSON con el nombre \"" + rutaJSON + "\"" + "\n\t•" + j.size() + " indices");
        } catch (Exception e) {
            System.out.println("[CARGA] Error al cargar el archivo \""+ rutaJSON +"\"");
            System.exit(-1);
        }

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
