package org.hugom;
import javafx.scene.media.AudioClip;

public class ControladorSonido {

    private final AudioClip jugadorComer;
    private final AudioClip jugadorComerFruta;
    private final AudioClip jugadorComerFantasma;
    private final AudioClip huidaFantasmas;
    private final AudioClip muerteJugador;
    private final AudioClip vueltaSpawnFantasma;
    private final AudioClip inicioJuego;
    private final AudioClip vidaAnyadida;

    public AudioClip getJugadorComer() {
        return jugadorComer;
    }
    public AudioClip getJugadorComerFruta() {
        return jugadorComerFruta;
    }
    public AudioClip getHuidaFantasmas() {
        return huidaFantasmas;
    }
    public AudioClip getMuerteJugador() {
        return muerteJugador;
    }
    public AudioClip getJugadorComerFantasma() {
        return jugadorComerFantasma;
    }
    public AudioClip getVueltaSpawnFantasma() {
        return vueltaSpawnFantasma;
    }
    public AudioClip getInicioJuego() {
        return inicioJuego;
    }
    public AudioClip getVidaAnyadida() {
        return vidaAnyadida;
    }


    public ControladorSonido() {
        jugadorComer = new AudioClip(getClass().getResource("/media/audio/comer.wav").toString());
        jugadorComerFruta = new AudioClip(getClass().getResource("/media/audio/eat_fruit.wav").toString());
        jugadorComerFantasma = new AudioClip(getClass().getResource("/media/audio/eat_ghost.wav").toString());
        huidaFantasmas = new AudioClip(getClass().getResource("/media/audio/power_pellet.wav").toString());
        muerteJugador = new AudioClip(getClass().getResource("/media/audio/muerte.wav").toString());
        vueltaSpawnFantasma = new AudioClip(getClass().getResource("/media/audio/retreating.wav").toString());
        inicioJuego = new AudioClip(getClass().getResource("/media/audio/game_start.wav").toString());
        vidaAnyadida = new AudioClip(getClass().getResource("/media/audio/extend.wav").toString());
    }

}
