package org.hugom;

import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
        double dimensionJugador = getHojaSprites().getSpriteData().get("frame_" + getFrameActual()).getWidth();
        double posicionX =this.getPosicion().getX() * Constantes.ESCALADO_SPRITE * 8 + Math.floor(8 * Constantes.ESCALADO_SPRITE / 2) - Math.floor(15 / 2* Constantes.ESCALADO_SPRITE)  - 2 * 8 * Constantes.ESCALADO_SPRITE;
        double posicionY = this.getPosicion().getY() * Constantes.ESCALADO_SPRITE * 8 + Math.floor(8 * Constantes.ESCALADO_SPRITE / 2) - Math.floor(16 / 2* Constantes.ESCALADO_SPRITE) ;


        String sprite = "";
        sprite = "frame_" + getFrameActual();

        // Si el jugador no se encuentra con vida, se estara mostrando la animacion de morir
        // Sprites van desde index 3 al 13
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
            if (getDireccion().equals("izq"))
                spriteJugador.setRotate(180);
            else if (getDireccion().equals("arr"))
                spriteJugador.setRotate(-90);
            else if (getDireccion().equals("abj"))
                spriteJugador.setRotate(90);

        // Generamos una captura de la imagen y la guardamos
        WritableImage spriteFinal = new WritableImage((int)spriteJugador.getBoundsInLocal().getWidth(), (int)spriteJugador.getBoundsInLocal().getHeight());
        spriteJugador.snapshot(null, spriteFinal);

        spriteFinal = Utilidades.aplicarTransparencia(spriteFinal);

        gc.drawImage(spriteFinal, posicionX, posicionY, dimensionJugador, dimensionJugador);
    }

    @Override
        public void mover() {
            Posicion posicionDeseada = posicionEnDireccion(getPosicion(), getDireccion());
            if (validarMovimiento(posicionDeseada) && Controlador.ahora() > getSiguienteFrame()){
                if(getFrameActual() == 0) setFrameActual(1);
                else if (getFrameActual() == 1) setFrameActual(2);
                else setFrameActual(0);

                setSiguienteFrame(Controlador.ahora() + intervalo2);
            }

            if (Controlador.ahora() > getSiguienteMovimiento()){
                setSiguienteMovimiento(Controlador.ahora() + Constantes.COOLDOWN_MOVIMIENTO_JUGADOR);

                String direccion = intentandoNuevaDireccionDir;
                if (!intentandoNuevaDireccion || !intentarNuevaDireccion(direccion))
                    if (validarMovimiento(posicionDeseada))
                        actualizarPosicion(posicionDeseada);
            }

            detectarComida();
        }

    void detectarComida() {
        HashMap<String, ArrayList<Object>> frutasReemplazos = new HashMap<>();
        frutasReemplazos.put(".", new ArrayList<>(Arrays.asList("_", Constantes.PUNTUACION_FRUTA_PEQ)));
        frutasReemplazos.put(":", new ArrayList<>(Arrays.asList("_", Constantes.PUNTUACION_FRUTA_GRA)));


        int posX = getPosicion().getX();
        int posY = getPosicion().getY();
        String elementoEnPosicion = Controlador.estructuraFuncionalMapa.get(posY).get(posX);

        if(frutasReemplazos.containsKey(elementoEnPosicion)){
            Controlador.puntuacion += (int)frutasReemplazos.get(elementoEnPosicion).get(1);


            Controlador.estructuraFuncionalMapa.get(posY).set(posX, (String)frutasReemplazos.get(elementoEnPosicion).get(0));
            Controlador.estructuraVisualMapa.get(posY).set(posX, (String)frutasReemplazos.get(elementoEnPosicion).get(0));


            if (Controlador.ahora() > siguienteWaka){
                Controlador.controladorSonido.getJugadorComer().play();
                siguienteWaka = Controlador.ahora() + 200;
            }

            if(elementoEnPosicion.equals(":")){
                Controlador.forzarHuidaFantasmas();
            }

            if (Controlador.vidasJugador < Constantes.VIDASMAX) {
                int num = Math.round((float) Controlador.puntuacion / Constantes.VIDAPORPUNTUACION) * Constantes.VIDAPORPUNTUACION;
                if (Controlador.puntuacion > Constantes.VIDAPORPUNTUACION && (num) % Constantes.VIDAPORPUNTUACION == 0 && !Controlador.vidasDadas.contains(num)) {
                    Controlador.vidasJugador += 1;
                    Controlador.vidasDadas.add(num);
                    Controlador.controladorSonido.getVidaAnyadida().play();

                }
            }

            if (Controlador.comprobarFrutas() == 0) { // El nivel ha finalizado.
                Controlador.momentoParpadeo = Controlador.ahora() + 2000;
                Controlador.momentoAvance = Controlador.ahora() + 5000;
                Controlador.nivelFinalizado = true;
                Controlador.controladorSonido.getHuidaFantasmas().stop();
            }
        }
    }

    void actualizarPosicion(Posicion nuevaPosicion){
        setPosicion(nuevaPosicion);
    }

    public boolean actualizarDireccion(String nuevaDireccion, boolean actualizadaManual){
        if (validarMovimiento(posicionEnDireccion(getPosicion(), nuevaDireccion))){
            setDireccion(nuevaDireccion);
            return true;
        }
        if (actualizadaManual)
            intentarNuevaDireccion(nuevaDireccion);
        return false;
    }


    boolean validarMovimiento(Posicion posicionDeseada){
        ArrayList<String> obstaculos = new ArrayList<String>(Arrays.asList("#", "G"));

        // Si hay un obstauclo en la posicion, devuelve false. Si no lo hay, devuelve true, pues seria un movimiento valido
        return !obstaculos.contains(Controlador.estructuraFuncionalMapa.get(posicionDeseada.getY()).get(posicionDeseada.getX()));
    }

    /*
    * Funcion  para hacer el control mas suave.
    * Una vez que el jugador pulse una tecla, si la direccion a la que quiere ir es incorrecta (hay un obstaculo)
    * entonces, esta funcion intentara hacer ese mismo movimiento los siguientes 5 frames.
    * Esto ya que, a veces el jugador puede darle una nueva direccion en el frame incorrecto y no funcionar.
    * @param la direccion a la que se quiere intentar mover
    * @return si se ha podido mover a la nueva direccion.
     */
    boolean intentarNuevaDireccion(String direccionAIntentar){
        if (intentandoNuevaDireccion) {
            intentandoNuevaDireccionRestantes--;

            if (actualizarDireccion(intentandoNuevaDireccionDir, false) || intentandoNuevaDireccionRestantes == -1) { // Si se ha conseguido actualizar la direccion, o no quedan intentos restantes
                intentandoNuevaDireccion = false;
                intentandoNuevaDireccionRestantes = -1;
                intentandoNuevaDireccionDir = null;

                // TODO de alguna manera hacer el now global, o handlear el timing desde Globales.
                setSiguienteMovimiento(Controlador.ahora()); // Revisar
                return true;
            }
        }
        else {
            // Si no se esta intentando ya, se inicia un nuevo intento para cambiar automaticamente a esa direccion.
            intentandoNuevaDireccionDir = direccionAIntentar;
            intentandoNuevaDireccion = true;
            intentandoNuevaDireccionRestantes = 4;
        }
        return false;
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
