import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class Utilidades {

    public static WritableImage aplicarTransparencia(WritableImage imagen) {

        PixelReader pixelReader = imagen.getPixelReader();
        WritableImage imagenTransparente = new WritableImage(pixelReader, (int) imagen.getWidth(), (int) imagen.getHeight());
        PixelWriter pixelWriter = imagenTransparente.getPixelWriter();
        for (int y = 0; y < imagenTransparente.getHeight(); y++) {
            for (int x = 0; x < imagenTransparente.getWidth(); x++) {
                if (pixelReader.getArgb(x, y) == 0xFF_000000) {
                    pixelWriter.setArgb(x, y, 0);
                } else {
                    pixelWriter.setArgb(x, y, pixelReader.getArgb(x, y));
                }
            }
        }
        return imagenTransparente;
    }
}
