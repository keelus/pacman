import javafx.scene.media.AudioClip;

public class ControladorSonido {

    private AudioClip jugadorComer;
    private AudioClip jugadorComerFruta;
    private AudioClip huidaFantasmas;

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


    public ControladorSonido() {
        String rutaBase = "file:src/media/audio/";
        jugadorComer = new AudioClip(rutaBase + "comer.wav");
        jugadorComerFruta = new AudioClip(rutaBase + "eat_fruit.wav");
        huidaFantasmas = new AudioClip(rutaBase + "power_pellet.wav");
    }
}
