package org.hugom;
import javafx.scene.paint.Color;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Rojo extends Fantasma{
    public Rojo(Posicion posicion, String direccion, Posicion objetivo, EstadosFantasma estado) throws IOException, ParseException {
        super(posicion, direccion, Color.RED, objetivo, estado,"rojo", -1);
    }


    @Override
    void establecerObjetivoAtaque(Jugador jugador, Rojo rojo){
        if(getEstado() != EstadosFantasma.ATAQUE)
            return;
        setObjetivo(jugador.getPosicion());
    }
}
