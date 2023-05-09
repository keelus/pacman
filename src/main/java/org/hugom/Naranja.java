package org.hugom;
import javafx.scene.paint.Color;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Naranja extends Fantasma {
    public Naranja(Posicion posicion, String direccion, Posicion objetivo, EstadosFantasma estado) throws IOException, ParseException {
        super(posicion, direccion, Color.ORANGE, objetivo, estado, "naranja", 21.0);
    }


    @Override
    void establecerObjetivoAtaque(Jugador jugador, Rojo rojo){
        if (getEstado() != EstadosFantasma.ATAQUE)
            return;

        Posicion posJugador = jugador.getPosicion();
        Posicion posActual = getPosicion();
        double distancia = Posicion.calcularDistancia(posJugador, posActual);

        setObjetivo(distancia > 7 ? posJugador : new Posicion(2, 35));
    }
}
