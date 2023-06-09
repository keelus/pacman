package org.hugom;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public abstract class Entidad {
    private Posicion posicion;
    private String direccion;
    private final HojaSprites hojaSprites;
    private long siguienteFrame = 0;
    private int frameActual = 0;
    private long siguienteMovimiento = 0;
    private final Color colorDebug;

    abstract void dibujar(GraphicsContext gc);
    abstract void mover();

    public Posicion getPosicion() {
        return posicion;
    }
    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public HojaSprites getHojaSprites() {
        return hojaSprites;
    }
    public long getSiguienteFrame() {
        return siguienteFrame;
    }
    public void setSiguienteFrame(long siguienteFrame) {
        this.siguienteFrame = siguienteFrame;
    }
    public int getFrameActual() {
        return frameActual;
    }
    public void setFrameActual(int frameActual) {
        this.frameActual = frameActual;
    }
    public long getSiguienteMovimiento() {
        return siguienteMovimiento;
    }
    public void setSiguienteMovimiento(long siguienteMovimiento) {
        this.siguienteMovimiento = siguienteMovimiento;
    }

    public Entidad(Posicion posicion, String direccion, Color colorDebug) {
        if (this instanceof Jugador)
            hojaSprites = new HojaSprites("/media/imagen/jugador3.png", "/datos/indicesSprites/spritesJugador.json");
        else
            hojaSprites = new HojaSprites("/media/imagen/fantasma.png", "/datos/indicesSprites/spritesFantasmas.json");

        this.posicion = posicion;
        this.direccion = direccion;
        this.colorDebug = colorDebug;
    }


    public static Posicion posicionEnDireccion(Posicion posicionActual, String direccion){
        Posicion posicionNueva = new Posicion(posicionActual.getX(), posicionActual.getY());

        switch(direccion){
            case "izq":
                if (posicionActual.getX() == 0) // Si el jugador sobre pasa los bordes visibles, se teletransporta a la derecha del mapa
                    posicionNueva.setX(31);
                else
                    posicionNueva.setX(posicionActual.getX()-1);
                break;
            case "der":
                if (posicionActual.getX() == 31) // Si el jugador sobre pasa los bordes visibles, se teletransporta a la izquierda del mapa
                    posicionNueva.setX(0);
                else
                    posicionNueva.setX(posicionActual.getX()+1);
                break;
            case "arr":
                posicionNueva.setY(posicionActual.getY() - 1);
                break;
            case "abj":
                posicionNueva.setY(posicionActual.getY() + 1);
                break;
        }
        return posicionNueva;
    }


    abstract void reiniciar(double miliSegundosExtra, boolean reiniciarCompletamente);
}
