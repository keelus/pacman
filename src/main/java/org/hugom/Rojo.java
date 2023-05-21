package org.hugom;
import javafx.scene.paint.Color;


public class Rojo extends Fantasma{
    public Rojo(Posicion posicion, String direccion, Posicion objetivo, EstadosFantasma estado) {
        super(posicion, direccion, Color.RED, objetivo, estado,"rojo", -1);
    }


    @Override
    void establecerObjetivoAtaque(Jugador jugador, Rojo rojo){
        if(getEstado() != EstadosFantasma.ATAQUE)
            return;
        setObjetivo(jugador.getPosicion());
    }

    @Override
    void reiniciar(double miliSegundosExtra, boolean reiniciarCompletamente){
        setDireccion("izq");
        setDireccionContraria(direccionContrariaDe("izq"));
        setPosicion(Constantes.POS_ROJO.copiar());
        setEstado(EstadosFantasma.ATAQUE);

        this.setObjetivo(new Posicion(-1, -1));
    }
}
