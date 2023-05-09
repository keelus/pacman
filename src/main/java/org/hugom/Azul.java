package org.hugom;

import javafx.scene.paint.Color;
import org.json.simple.parser.ParseException;
import java.io.IOException;


public class Azul extends Fantasma{
    public Azul(Posicion posicion, String direccion , Posicion objetivo, EstadosFantasma estado) throws IOException, ParseException {
        super(posicion, direccion, Color.CYAN, objetivo, estado, "azul", 14.0);
    }

    @Override
    void establecerObjetivoAtaque(Jugador jugador, Rojo rojo){
        if (getEstado() != EstadosFantasma.ATAQUE)
            return;

        Posicion posJugador = jugador.getPosicion().copiar();
        String dirJugador = jugador.getDireccion();

        Posicion posRojo = rojo.getPosicion().copiar();

        switch(dirJugador){
            case "izq":
                posJugador.setX(posJugador.getX()-2);
                break;
            case "der":
                posJugador.setX(posJugador.getX()+2);
                break;
            case "abj":
                posJugador.setY(posJugador.getY()+2);
                break;
            case "arr":
                posJugador.setX(posJugador.getX()-2); // Emulamos el error de desbordamiendo de hexadecimal del juego original
                posJugador.setY(posJugador.getY()-2);
                break;
        }
        Posicion posicionFinal = Posicion.rotar180Grados(posJugador, posRojo);
        setObjetivo(posicionFinal);
    }
}
