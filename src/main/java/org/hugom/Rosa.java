package org.hugom;
import javafx.scene.paint.Color;

public class Rosa extends Fantasma{
    public Rosa(Posicion posicion, String direccion, Posicion objetivo, EstadosFantasma estado) {
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

    @Override
    void reiniciar(double miliSegundosExtra, boolean reiniciarCompletamente){
        setDireccion("abj");
        setDireccionContraria(direccionContrariaDe("abj"));
        setPosicion(Constantes.POS_ROSA.copiar());
        setEstado(EstadosFantasma.ESPERASPAWNINICIAL);

        this.setMomentoSpawnInicial(Controlador.ahora() + 1000 + miliSegundosExtra); // Añadimos un segundo unicamente a la espera, en vez de su espera original.
        this.setObjetivo(new Posicion(-1, -1));

        if (reiniciarCompletamente || !this.isHaSpawneado()) {
            this.setMomentoSpawnInicial(Controlador.ahora() + this.getEsperaSpawnInicial() * 1000 + miliSegundosExtra);

            if (reiniciarCompletamente)
                this.setHaSpawneado(false);
        }
    }
}
