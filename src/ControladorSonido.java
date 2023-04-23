import javafx.scene.media.AudioClip;

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
        String rutaBase = "file:src/media/audio/";
        jugadorComer = new AudioClip(rutaBase + "comer.wav");
        jugadorComerFruta = new AudioClip(rutaBase + "eat_fruit.wav");
        jugadorComerFantasma = new AudioClip(rutaBase + "eat_ghost.wav");
        huidaFantasmas = new AudioClip(rutaBase + "power_pellet.wav");
        muerteJugador = new AudioClip(rutaBase + "muerte.wav");
        vueltaSpawnFantasma = new AudioClip(rutaBase + "retreating.wav");
        inicioJuego = new AudioClip(rutaBase + "game_start.wav");
    }

}
