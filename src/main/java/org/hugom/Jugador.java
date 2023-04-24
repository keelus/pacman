package org.hugom;

import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Jugador extends Entidad {
    boolean intentandoNuevaDireccion;
    String intentandoNuevaDireccionDir;
    int intentandoNuevaDireccionRestantes;

    long siguienteWaka;

    boolean conVida = true;
    boolean animacionMuerteEmpezada = false;
    double animacionMuerteInicio = -1;
    boolean animacionMuerteFinalizada = false;





    long intervalo2 = 100; // ms * 1M = ns

    public boolean isIntentandoNuevaDireccion() {
        return intentandoNuevaDireccion;
    }
    public void setIntentandoNuevaDireccion(boolean intentandoNuevaDireccion) {
        this.intentandoNuevaDireccion = intentandoNuevaDireccion;
    }
    public String getIntentandoNuevaDireccionDir() {
        return intentandoNuevaDireccionDir;
    }
    public void setIntentandoNuevaDireccionDir(String intentandoNuevaDireccionDir) {
        this.intentandoNuevaDireccionDir = intentandoNuevaDireccionDir;
    }
    public int getIntentandoNuevaDireccionRestantes() {
        return intentandoNuevaDireccionRestantes;
    }
    public void setIntentandoNuevaDireccionRestantes(int intentandoNuevaDireccionRestantes) {
        this.intentandoNuevaDireccionRestantes = intentandoNuevaDireccionRestantes;
    }
    public long getSiguienteWaka() {
        return siguienteWaka;
    }
    public void setSiguienteWaka(long siguienteWaka) {
        this.siguienteWaka = siguienteWaka;
    }
    public boolean isConVida() {
        return conVida;
    }
    public void setConVida(boolean conVida) {
        this.conVida = conVida;
    }




    public Jugador(Posicion posicion, String direccion, HojaSprites hojaSprites, long siguienteFrame, int frameActual, long siguienteMovimiento, long siguienteWaka, int puntuacion) {
        super(posicion, direccion, hojaSprites, siguienteFrame, frameActual, siguienteMovimiento, Color.WHITE);
        this.intentandoNuevaDireccion = false;
        this.intentandoNuevaDireccionDir = null;
        this.intentandoNuevaDireccionRestantes = -1;
        this.siguienteWaka = siguienteWaka;

        this.setSiguienteMovimiento(Controlador.ahora());
        this.setSiguienteFrame(Controlador.ahora());


    }

    @Override
    void dibujar(GraphicsContext gc) {

        //System.out.println(getHojaSprites());
        double dim_fantasma = getHojaSprites().getSpriteData().get("frame_" + getFrameActual()).getWidth();
        double posX_fantasma = this.getPosicion().getX() * Constantes.ESCALADO_SPRITE * 8 + Math.floor(8 * Constantes.ESCALADO_SPRITE / 2) - Math.floor(15 / 2* Constantes.ESCALADO_SPRITE)  - 2 * 8 * Constantes.ESCALADO_SPRITE;
        double posY_fantasma = this.getPosicion().getY() * Constantes.ESCALADO_SPRITE * 8 + Math.floor(8 * Constantes.ESCALADO_SPRITE / 2) - Math.floor(16 / 2* Constantes.ESCALADO_SPRITE) ;


        String sprite = "";
        sprite = "frame_" + getFrameActual();

        //frames van de 3 a 13
        //frames van de 0 a 10
        if (!this.conVida && Controlador.perdido){
            if (!this.animacionMuerteEmpezada) {
                this.animacionMuerteEmpezada = true;
                this.animacionMuerteInicio = Controlador.ahora() + 333;
                setFrameActual(3);
            } else if (!this.animacionMuerteFinalizada){
                if (Controlador.ahora() + 100 > this.animacionMuerteInicio + 100 * (getFrameActual() - 3))
                    this.setFrameActual(this.getFrameActual() + 1);

                if (getFrameActual() == 14)
                    animacionMuerteFinalizada = true;
            }
        }


        // Sprite original del jugador, mirando hacia la derecha
        ImageView spriteJugador = new ImageView(getHojaSprites().getSpriteData().get(sprite));



        // Giramos la imagen si el jugador apunta hacia un lado que no sea la derecha


        if (this.conVida)
        if (getDireccion() == "izq")
            spriteJugador.setRotate(180);
        else if (getDireccion() == "arr")
            spriteJugador.setRotate(-90);
        else if (getDireccion() == "abj")
            spriteJugador.setRotate(90);

        // Generamos una captura de la imagen y la guardamos
        WritableImage spriteFinal = new WritableImage((int)spriteJugador.getBoundsInLocal().getWidth(), (int)spriteJugador.getBoundsInLocal().getHeight());
        spriteJugador.snapshot(null, spriteFinal);

        spriteFinal = Utilidades.aplicarTransparencia(spriteFinal);

        //gc.drawImage(getHojaSprites().getSpriteData().get("frame_1"), posX_fantasma, posY_fantasma, dim_fantasma, dim_fantasma);
        gc.drawImage(spriteFinal, posX_fantasma, posY_fantasma, dim_fantasma, dim_fantasma);



    }

    @Override
        public void mover() {
            Posicion posicionDeseada = posicionEnDireccion(getPosicion(), getDireccion());
            if (validarMovimiento(posicionDeseada) && Controlador.ahora() > getSiguienteFrame()){
                if(getFrameActual() == 0)
                    setFrameActual(1);
                else if (getFrameActual() == 1)
                    setFrameActual(2);
                else
                    setFrameActual(0);

                setSiguienteFrame(Controlador.ahora() + intervalo2);
            }

            if (Controlador.ahora() > getSiguienteMovimiento()){
                setSiguienteMovimiento(Controlador.ahora() + Constantes.COOLDOWN_MOVIMIENTO_JUGADOR);

                String direccion = intentandoNuevaDireccionDir;
                if (intentandoNuevaDireccion && intentarNuevaDireccion(direccion))
                    System.out.println("Direccion cambiada correctamente.");
                else{
                    if (validarMovimiento(posicionDeseada))
                        actualizarPosicion(posicionDeseada);
                }
            }

            detectarComida();


        }

    void detectarComida() {
        // TODO Añadir sonidos
        ArrayList<String> comidaPeque = new ArrayList<String>(Arrays.asList(".", "+"));

        ArrayList<ArrayList<String>> estructuraFuncionalNueva = Controlador.estructuraFuncionalMapa;
        ArrayList<ArrayList<String>> estructuraVisualNueva = Controlador.estructuraVisualMapa;

        int posX = getPosicion().getX();
        int posY = getPosicion().getY();
        String elementoEnPosicion = Controlador.estructuraFuncionalMapa.get(posY).get(posX);

        // TODO Hacer mas bonita esta parte
        if (elementoEnPosicion.equals(".") || elementoEnPosicion.equals("+") ){ // es una fruta pequeña (Si es . es fruta pequeña normal. Si es + sera fruta pequeña especial (visual igua, pero funcionamiento diferente de fantasmas, zona spawn))
            Controlador.puntuacion += Constantes.PUNTUACION_FRUTA_PEQ;
            if (Controlador.ahora() > siguienteWaka){
                Controlador.controladorSonido.getJugadorComer().play();
                siguienteWaka = Controlador.ahora() + 200;
            }
            if (elementoEnPosicion.equals(".")){
                estructuraFuncionalNueva.get(posY).set(posX, "_");
                estructuraVisualNueva.get(posY).set(posX, "_");
            }
            else{
                estructuraFuncionalNueva.get(posY).set(posX, "-");
                estructuraVisualNueva.get(posY).set(posX, "-");
            }
        } else if (elementoEnPosicion.equals(":")){
            Controlador.puntuacion += Constantes.PUNTUACION_FRUTA_GRA;
            Controlador.forzarHuidaFantasmas();
            estructuraFuncionalNueva.get(posY).set(posX, "_");
            estructuraVisualNueva.get(posY).set(posX, "_");

        }

        Controlador.estructuraFuncionalMapa = estructuraFuncionalNueva;
        Controlador.estructuraVisualMapa = estructuraVisualNueva;

    }

    void actualizarPosicion(Posicion nuevaPosicion){
        //System.out.println("Posicion actualizada a " + nuevaPosicion);
        setPosicion(nuevaPosicion);
    }

    public boolean actualizarDireccion(String nuevaDireccion, boolean actualizadaManual){
        if (validarMovimiento(posicionEnDireccion(getPosicion(), nuevaDireccion))){
            setDireccion(nuevaDireccion);
            return true;
        } else {
            if (actualizadaManual)
                intentarNuevaDireccion(nuevaDireccion);
        }
        return false;

    }


    boolean validarMovimiento(Posicion posicionDeseada){
        ArrayList<String> obstaculos = new ArrayList<String>(Arrays.asList("#", "G"));

        // Si hay un obstauclo en la posicion, devuelve false. Si no lo hay, devuelve true, pues seria un movimiento valido
        return !obstaculos.contains(Controlador.estructuraFuncionalMapa.get(posicionDeseada.getY()).get(posicionDeseada.getX()));
    }

    boolean intentarNuevaDireccion(String direccionAIntentar){
        if (intentandoNuevaDireccion) {
            System.out.println("Intentando actualizar direccion previa...");
            intentandoNuevaDireccionRestantes--;

            if (actualizarDireccion(intentandoNuevaDireccionDir, false) || intentandoNuevaDireccionRestantes == -1) { // Si se ha conseguido actualizar la direccion, o no quedan intentos restantes
                intentandoNuevaDireccion = false;
                intentandoNuevaDireccionRestantes = -1;
                intentandoNuevaDireccionDir = null;

                // TODO de alguna manera hacer el now global, o handlear el timing desde Globales.
                mover();
                setSiguienteMovimiento(Controlador.ahora()); // Revisar
                return true;
            }
        }
        else { // Se inicia el intento automatico de cambiar la direccion a la que el jugador presiono (asi, si chocaria contra una pared por timing de milisegundos, se reintenta un par de veces, asi la experiencia de control es mejor
            System.out.println("Intento automatico iniciado");
            intentandoNuevaDireccionDir = direccionAIntentar;
            intentandoNuevaDireccion = true;
            intentandoNuevaDireccionRestantes = 4;
        }

        return false; // Devolvemos que no se ha conseguido cambiar por ahora
    }

    @Override
    void reiniciar(double miliSegundosExtra){
        setPosicion(new Posicion(16, 26));
        setDireccion("der");
        setFrameActual(0);
        this.intentandoNuevaDireccion = false;
        this.intentandoNuevaDireccionDir = null;
        this.intentandoNuevaDireccionRestantes = -1;
        this.animacionMuerteEmpezada = false;
        animacionMuerteFinalizada = false;
        this.setSiguienteMovimiento(Controlador.ahora());
        this.setSiguienteFrame(Controlador.ahora());
    }


}
