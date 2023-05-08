package org.hugom;
import javafx.scene.paint.Color;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Naranja extends Fantasma {
    public Naranja(Posicion posicion, String direccion, Posicion objetivo, EstadosFantasma estado, Posicion objetivoDispersion) throws IOException, ParseException {
        super(posicion, direccion, Color.ORANGE, objetivo, estado, objetivoDispersion, "naranja", 21.0);
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
