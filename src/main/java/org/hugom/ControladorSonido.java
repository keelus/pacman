package org.hugom;
import javafx.scene.media.AudioClip;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;

public class ControladorSonido {

    private AudioClip jugadorComer;
    private AudioClip jugadorComerFruta;
    private AudioClip jugadorComerFantasma;
    private AudioClip huidaFantasmas;
    private AudioClip muerteJugador;
    private AudioClip vueltaSpawnFantasma;
    private AudioClip inicioJuego;

    public AudioClip getJugadorComer() {
        return jugadorComer;
    }
    public void setJugadorComer(AudioClip jugadorComer) {
        this.jugadorComer = jugadorComer;
    }
    public AudioClip getJugadorComerFruta() {
        return jugadorComerFruta;
    }
    public void setJugadorComerFruta(AudioClip jugadorComerFruta) {
        this.jugadorComerFruta = jugadorComerFruta;
    }
    public AudioClip getHuidaFantasmas() {
        return huidaFantasmas;
    }
    public void setHuidaFantasmas(AudioClip huidaFantasmas) {
        this.huidaFantasmas = huidaFantasmas;
    }
    public AudioClip getMuerteJugador() {
        return muerteJugador;
    }
    public void setMuerteJugador(AudioClip muerteJugador) {
        this.muerteJugador = muerteJugador;
    }
    public AudioClip getJugadorComerFantasma() {
        return jugadorComerFantasma;
    }
    public void setJugadorComerFantasma(AudioClip jugadorComerFantasma) {
        this.jugadorComerFantasma = jugadorComerFantasma;
    }
    public AudioClip getVueltaSpawnFantasma() {
        return vueltaSpawnFantasma;
    }
    public void setVueltaSpawnFantasma(AudioClip vueltaSpawnFantasma) {
        this.vueltaSpawnFantasma = vueltaSpawnFantasma;
    }
    public AudioClip getInicioJuego() {
        return inicioJuego;
    }
    public void setInicioJuego(AudioClip inicioJuego) {
        this.inicioJuego = inicioJuego;
    }

    public ControladorSonido() {
        String rutaBase = "file:/media/audio/";

        jugadorComer = new AudioClip(getClass().getResource("/media/audio/comer.wav").toString());
        jugadorComerFruta = new AudioClip(getClass().getResource("/media/audio/eat_fruit.wav").toString());
        jugadorComerFantasma = new AudioClip(getClass().getResource("/media/audio/eat_ghost.wav").toString());
        huidaFantasmas = new AudioClip(getClass().getResource("/media/audio/power_pellet.wav").toString());
        muerteJugador = new AudioClip(getClass().getResource("/media/audio/muerte.wav").toString());
        vueltaSpawnFantasma = new AudioClip(getClass().getResource("/media/audio/retreating.wav").toString());
        inicioJuego = new AudioClip(getClass().getResource("/media/audio/game_start.wav").toString());

//        jugadorComerFruta = new AudioClip(rutaBase + "eat_fruit.wav");
//        jugadorComerFantasma = new AudioClip(rutaBase + "eat_ghost.wav");
//        huidaFantasmas = new AudioClip(rutaBase + "power_pellet.wav");
//        muerteJugador = new AudioClip(rutaBase + "muerte.wav");
//        vueltaSpawnFantasma = new AudioClip(rutaBase + "retreating.wav");
//        inicioJuego = new AudioClip(rutaBase + "game_start.wav");
    }

}
