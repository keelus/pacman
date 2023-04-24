package org.hugom;
import javafx.scene.paint.Color;

public class Naranja extends Fantasma {
    public Naranja(Posicion posicion, String direccion, HojaSprites hojaSprites, long siguienteFrame, int frameActual, long siguienteMovimiento, Posicion objetivo, EstadosFantasma estado, Posicion objetivoDispersion, Posicion posicionSpawnOjos) {
        super(posicion, direccion, hojaSprites, siguienteFrame, frameActual, siguienteMovimiento, Color.ORANGE, objetivo, estado, objetivoDispersion, posicionSpawnOjos, "naranja", 21.0);
    }


    @Override
    void establecerObjetivoAtaque(Jugador jugador, Rojo rojo){
        if (getEstado() != EstadosFantasma.ATAQUE)
            return;


        Posicion posJugador = new Posicion(jugador.getPosicion().getX(), jugador.getPosicion().getY());
        Posicion posActual = new Posicion(getPosicion().getX(), getPosicion().getY());
        double distancia = Posicion.calcularDistancia(posJugador, posActual);


        if (distancia > 7)
            setObjetivo(jugador.getPosicion());
        else
            setObjetivo(new Posicion(2, 35));

    }
}
