import javafx.scene.paint.Color;

public class Azul extends Fantasma{
    public Azul(Posicion posicion, String direccion, HojaSprites hojaSprites, long siguienteFrame, int frameActual, long siguienteMovimiento, Posicion objetivo, EstadosFantasma estado, Posicion objetivoDispersion, Posicion posicionSpawnOjos) {
        super(posicion, direccion, hojaSprites, siguienteFrame, frameActual, siguienteMovimiento, Color.CYAN, objetivo, estado, objetivoDispersion, posicionSpawnOjos, "azul", 14.0);
    }

    @Override
    void establecerObjetivoAtaque(Jugador jugador, Rojo rojo){
        if (getEstado() != EstadosFantasma.ATAQUE)
            return;


        Posicion posJugador = new Posicion(jugador.getPosicion().getX(), jugador.getPosicion().getY());
        String dirJugador = jugador.getDireccion();

        Posicion posRojo = new Posicion(rojo.getPosicion().getX(), rojo.getPosicion().getY());

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
