package org.hugom;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class Utilidades {

    public static WritableImage aplicarTransparencia(WritableImage imagen) {
        PixelReader lectorPixelesImagen = imagen.getPixelReader();
        WritableImage imagenTransparente = new WritableImage(lectorPixelesImagen, (int) imagen.getWidth(), (int) imagen.getHeight());
        PixelWriter escritorPixelesImagen = imagenTransparente.getPixelWriter();
        for (int y = 0; y < imagenTransparente.getHeight(); y++) {
            for (int x = 0; x < imagenTransparente.getWidth(); x++) {
                // Si el color es negro (o sea, #000000 en hex), queremos establecer el pixel transparente.
                if (lectorPixelesImagen.getArgb(x, y) == 0xFF_000000) {
                    escritorPixelesImagen.setArgb(x, y, 0);
                } else {
                    escritorPixelesImagen.setArgb(x, y, lectorPixelesImagen.getArgb(x, y));
                }
            }
        }
        return imagenTransparente;
    }
}
