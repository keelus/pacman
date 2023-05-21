package org.hugom;
import javafx.scene.paint.Color;

public class Naranja extends Fantasma {
    public Naranja(Posicion posicion, String direccion, Posicion objetivo, EstadosFantasma estado) {
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

    @Override
    void reiniciar(double miliSegundosExtra, boolean reiniciarCompletamente){
        setDireccion("arr");
        setDireccionContraria(direccionContrariaDe("arr"));
        setPosicion(Constantes.POS_NARANJA.copiar());
        setEstado(EstadosFantasma.ESPERASPAWNINICIAL);

        this.setMomentoSpawnInicial(Controlador.ahora() + 5000 + miliSegundosExtra); // Añadimos cinco segundos unicamente a la espera, en vez de su espera original.
        this.setObjetivo(new Posicion(-1, -1));

        if (reiniciarCompletamente || !this.isHaSpawneado()) {
            this.setMomentoSpawnInicial(Controlador.ahora() + this.getEsperaSpawnInicial() * 1000 + miliSegundosExtra);

            if (reiniciarCompletamente)
                this.setHaSpawneado(false);
        }
    }
}
