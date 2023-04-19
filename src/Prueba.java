import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Prueba {

    public static HashMap<String, ImageView> loadSprites(String imagePath, HashMap<String, int[][]> spriteData) {
        // Load the sprite sheet image
        Image spriteSheet = new Image(imagePath);

        // Create a HashMap to store the sprite images
        HashMap<String, ImageView> sprites = new HashMap<>();

        // Loop through the sprite data and create an ImageView for each sprite
        for (String key : spriteData.keySet()) {
            int[][] data = spriteData.get(key);

            // Create an ImageView with the sprite sheet as its image source
            ImageView sprite = new ImageView(spriteSheet);

            // Set the viewport to display the correct sprite
            sprite.setViewport(new javafx.geometry.Rectangle2D(data[1][0], data[1][1], data[0][0], data[0][1]));

            // Add the sprite ImageView to the HashMap
            sprites.put(key, sprite);
        }

        // Return the HashMap of sprites
        return sprites;
    }



}
