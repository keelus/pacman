package org.hugom;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Rojo extends Fantasma{
    public Rojo(Posicion posicion, String direccion, HojaSprites hojaSprites, long siguienteFrame, int frameActual, long siguienteMovimiento, Posicion objetivo, EstadosFantasma estado, Posicion objetivoDispersion, Posicion posicionSpawnOjos) {
        super(posicion, direccion, hojaSprites, siguienteFrame, frameActual, siguienteMovimiento, Color.RED, objetivo, estado, objetivoDispersion, posicionSpawnOjos, "rojo", -1);
    }


    @Override
    void establecerObjetivoAtaque(Jugador jugador, Rojo rojo){
        if(getEstado() != EstadosFantasma.ATAQUE)
            return;
        setObjetivo(new Posicion(jugador.getPosicion().getX(), jugador.getPosicion().getY()));

    }


}
