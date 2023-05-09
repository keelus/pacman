package org.hugom;
import javafx.scene.paint.Color;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Rosa extends Fantasma{
    public Rosa(Posicion posicion, String direccion, Posicion objetivo, EstadosFantasma estado) throws IOException, ParseException {
        super(posicion, direccion, Color.PINK, objetivo, estado, "rosa", 7.0);
    }


    @Override
    void establecerObjetivoAtaque(Jugador jugador, Rojo rojo){
        if(getEstado() != EstadosFantasma.ATAQUE)
            return;

        Posicion posJugador = jugador.getPosicion().copiar();
        String dirJugador = jugador.getDireccion();

        switch(dirJugador){
            case "izq":
                posJugador.setX(posJugador.getX()-4);
                break;
            case "der":
                posJugador.setX(posJugador.getX()+4);
                break;
            case "abj":
                posJugador.setY(posJugador.getY()+4);
                break;
            case "arr":
                posJugador.setX(posJugador.getX()-4); // Emulamos el error de desbordamiendo de hexadecimal del juego original
                posJugador.setY(posJugador.getY()-4);
                break;
        }
        setObjetivo(posJugador);
    }
}
