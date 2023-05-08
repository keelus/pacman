package org.hugom;
import javafx.scene.paint.Color;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Rosa extends Fantasma{
    public Rosa(Posicion posicion, String direccion, Posicion objetivo, EstadosFantasma estado, Posicion objetivoDispersion) throws IOException, ParseException {
        super(posicion, direccion, Color.PINK, objetivo, estado, objetivoDispersion, "rosa", 7.0);
    }


    @Override
    void establecerObjetivoAtaque(Jugador jugador, Rojo rojo){
        if(getEstado() != EstadosFantasma.ATAQUE)
            return;

     Posicion posJugador = new Posicion(jugador.getPosicion().getX(), jugador.getPosicion().getY());
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
