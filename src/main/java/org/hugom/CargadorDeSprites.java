package org.hugom;

import java.util.HashMap;
import javafx.scene.image.*;
import javafx.scene.image.Image;

public class CargadorDeSprites {

    /**
     * Funcion que carga los sprites desde una ubicacion, recortandolo en pedazos y devolviendolos en sus respectivos indices.
     * @param ubicacionDeSprite Ubicacion del sprite que queremos cargar.
     * @param datosEnJSON Hashmap sobre los indices con sus tamaños y coordenadas sobre los que se cargara la imagen.
     * @return Un hashmap con los indices, pero esta vez con los valores del contenido de las imagenes en vez de los tamaños y coordenadas
     */
    public static HashMap<String, Image> cargarSprites(String ubicacionDeSprite, HashMap<String, HojaSprites.DatoSprite> datosEnJSON) {
        Image spritesheetTemporal = new Image(CargadorDeSprites.class.getResourceAsStream(ubicacionDeSprite)); // Cargamos este unicamente para conseguir el ancho y alto, y asi poder escalarlo correctamente,

        Image spritesheetRedimensionado = new Image(CargadorDeSprites.class.getResourceAsStream(ubicacionDeSprite), spritesheetTemporal.getWidth() * Constantes.ESCALADO_SPRITE, spritesheetTemporal.getHeight() * Constantes.ESCALADO_SPRITE, false, false);

        HashMap<String, Image> sprites = new HashMap<>();

        for (String indice : datosEnJSON.keySet()) {
            int ancho = datosEnJSON.get(indice).getDimension().getW() * (int) Constantes.ESCALADO_SPRITE;
            int alto = datosEnJSON.get(indice).getDimension().getH() * (int) Constantes.ESCALADO_SPRITE;

            int coordX = datosEnJSON.get(indice).getPosicion().getX() * (int) Constantes.ESCALADO_SPRITE;
            int coordY = datosEnJSON.get(indice).getPosicion().getY() * (int) Constantes.ESCALADO_SPRITE;


            PixelReader pixelesDeSpritesheet = spritesheetRedimensionado.getPixelReader();
            WritableImage porcionSprite = null;

            try {
                porcionSprite = new WritableImage(pixelesDeSpritesheet, coordX, coordY, ancho, alto);
            } catch (Exception e){
                System.out.println("[ERROR] Bounds fuera de la imagen.");
                System.exit(-1);
            }

            sprites.put(indice, porcionSprite);
        }
        return sprites;
    }
}
