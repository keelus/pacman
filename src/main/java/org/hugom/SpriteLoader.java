package org.hugom;

import java.util.HashMap;
import javafx.scene.image.*;
import javafx.scene.image.Image;

public class SpriteLoader {
    public static HashMap<String, Image> loadSprites(String imagePath, HashMap<String, HojaSprites.DatoSprite> spriteData) {
        Image spriteSheetOriginal = new Image(SpriteLoader.class.getResourceAsStream(imagePath));
        Image spriteSheet = new Image(SpriteLoader.class.getResourceAsStream(imagePath), spriteSheetOriginal.getWidth() * Constantes.ESCALADO_SPRITE, spriteSheetOriginal.getHeight() * Constantes.ESCALADO_SPRITE, false, false);

        HashMap<String, Image> sprites = new HashMap<>();

        for (String key : spriteData.keySet()) {
            int ancho = spriteData.get(key).getDimension().getW() * (int) Constantes.ESCALADO_SPRITE;
            int alto = spriteData.get(key).getDimension().getH() * (int) Constantes.ESCALADO_SPRITE;

            int coordX = spriteData.get(key).getPosicion().getX() * (int) Constantes.ESCALADO_SPRITE;
            int coordY = spriteData.get(key).getPosicion().getY() * (int) Constantes.ESCALADO_SPRITE;

            PixelReader reader = spriteSheet.getPixelReader();

            WritableImage subImage = null;

            try {
                subImage = new WritableImage(reader, coordX, coordY, ancho, alto);
            } catch (Exception e){
                System.out.println("[ERROR] Bounds fuera de la imagen.");
                System.exit(-1);
            }

            sprites.put(key, subImage);
        }
        return sprites;
    }
}
