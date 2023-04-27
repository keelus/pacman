package org.hugom;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.*;

public abstract class Fantasma extends Entidad {
    Posicion objetivo;
    String direccionContraria;
    EstadosFantasma estado;
    EstadosFantasma estadoAnterior;
    Posicion objetivoDispersion;
    Posicion posicionSpawnOjos;
    String colorFantasma;
    long huidaSiguienteFrame = -1;
    String ultimoFrame = "null";
    double esperaSpawnInicial = -1;
    double momentoSpawnInicial = -1;

    /* Inicio Setters y getters */
    public Posicion getObjetivo() {
        return objetivo;
    }
    public void setObjetivo(Posicion objetivo) {
        this.objetivo = objetivo;
    }
    public String getDireccionContraria() {
        return direccionContraria;
    }
    public void setDireccionContraria(String direccionContraria) {
        this.direccionContraria = direccionContraria;
    }
    public EstadosFantasma getEstado() {
        return estado;
    }
    public void setEstado(EstadosFantasma estado) {
        this.estado = estado;
    }
    public EstadosFantasma getEstadoAnterior() {
        return estadoAnterior;
    }
    public String getColorFantasma() {
        return colorFantasma;
    }
    /* Fin setters y getters */


    public Fantasma(Posicion posicion, String direccion, HojaSprites hojaSprites, long siguienteFrame, int frameActual, long siguienteMovimiento, Color colorDebug, Posicion objetivo, EstadosFantasma estado, Posicion objetivoDispersion, Posicion posicionSpawnOjos, String colorFantasma, double esperaSpawnInicial) {
        super(posicion, direccion, hojaSprites, siguienteFrame, frameActual, siguienteMovimiento, colorDebug);
        this.objetivo = objetivo;
        switch (direccion) {
            case "izq": this.direccionContraria = "der"; break;
            case "der": this.direccionContraria = "izq"; break;
            case "arr": this.direccionContraria = "abj"; break;
            case "abj": this.direccionContraria = "arr"; break;
        }
        this.estado = estado;
        this.objetivoDispersion = objetivoDispersion;
        this.posicionSpawnOjos = posicionSpawnOjos;
        this.colorFantasma = colorFantasma;
        this.esperaSpawnInicial = esperaSpawnInicial;
        if(!colorFantasma.equals("rojo"))
            this.momentoSpawnInicial = Controlador.ahora() + esperaSpawnInicial * 1000 + Constantes.COOLDOWN_INICIO_GAME;
    }

    @Override
    public void mover() {
        String direccion = null;



        if (getEstado() != EstadosFantasma.HUIDA){
            direccion = calcularDistancias();
        }

        if(Controlador.ahora() < getSiguienteMovimiento()){
            if(Controlador.ahora() > getSiguienteFrame()){
                if(getFrameActual() == 1)
                    setFrameActual(0);
                else setFrameActual(1);

                setSiguienteFrame(Controlador.ahora() + 100);
            }
            return;
        }

        if (getEstado() == EstadosFantasma.ESPERASPAWNINICIAL){
            this.objetivo = new Posicion(new Random().nextInt(3) + 14, 16);
        }

        if (this.estado == EstadosFantasma.DISPERSION) setSiguienteMovimiento(Controlador.ahora() + Constantes.COOLDOWN_MOVIMIENTO_FANTASMA_DISPERSION);
        else if (this.estado == EstadosFantasma.HUIDA) setSiguienteMovimiento(Controlador.ahora() + Constantes.COOLDOWN_MOVIMIENTO_FANTASMA_HUIDA);
        else if (this.estado == EstadosFantasma.MUERTO) setSiguienteMovimiento(Controlador.ahora() + Constantes.COOLDOWN_MOVIMIENTO_FANTASMA_MUERTO);
        else setSiguienteMovimiento(Controlador.ahora() + Constantes.COOLDOWN_MOVIMIENTO_FANTASMA);

        if(getEstado() == EstadosFantasma.HUIDA) {
            setObjetivo(new Posicion(-1, -1));
            HashMap<String, Boolean> posiciones = posicionesValidas();
            ArrayList<String> soloValidas = new ArrayList<>();

            for (String posicion : posicionesValidas().keySet()) {
                if (posicionesValidas().get(posicion) == false)
                    continue;
                soloValidas.add(posicion);
            }
            int aleatorio = new Random().nextInt(soloValidas.size());
            String direccionAleatoria = soloValidas.get(aleatorio);
            setDireccion(direccionAleatoria);
            direccion = direccionAleatoria;

        }


        switch(direccion){
            case "izq":
                setDireccionContraria("der");
                getPosicion().setX(getPosicion().getX()-1);
            break;
            case "der":
                setDireccionContraria("izq");
                getPosicion().setX(getPosicion().getX()+1);
            break;
            case "arr":
                setDireccionContraria("abj");
                getPosicion().setY(getPosicion().getY()-1);
            break;
            case "abj":
                setDireccionContraria("arr");
                getPosicion().setY(getPosicion().getY()+1);
            break;
        }
        if (getPosicion().getX() == 32)
            getPosicion().setX(0);
        else if (getPosicion().getX() == -1)
            getPosicion().setX(31);

        if (getPosicion().getX() == this.objetivo.getX() && getPosicion().getY() == this.objetivo.getY()){
            if(this.estado == EstadosFantasma.MUERTO){
                System.out.println("Fantasma ha llegado al spawn.");
                    this.cambiarEstado(EstadosFantasma.ESPERASPAWN);
            } else if (this.estado == EstadosFantasma.ESPERASPAWN){
                System.out.println("Fantasma ha salido del spawn.");
                    this.cambiarEstado(EstadosFantasma.ATAQUE);
            }
        }
    }

    public HashMap<String, Boolean> posicionesValidas(){
        HashMap<String, Boolean> direcciones = new HashMap<String, Boolean>(){{
            put("arr", true);
            put("izq", true);
            put("abj", true);
            put("der", true);
        }};


        // Comprobamos las cuatro direcciones
        for (String direccion: direcciones.keySet()){
            Posicion posicion = posicionEnDireccion(getPosicion(), direccion);

            // Evitamos que salga del mapeado
            if (posicion.getX() == 32)
                posicion.setX(0);
            else if (posicion.getX() == -1)
                posicion.setX(31);

            if (direccion.equals(getDireccionContraria()))
                direcciones.replace(direccion, false);
            else {
                if(Controlador.estructuraFuncionalMapa.get(posicion.getY()).get(posicion.getX()).equals("#"))
                    direcciones.replace(direccion, false);
                else if (Controlador.estructuraFuncionalMapa.get(posicion.getY()).get(posicion.getX()).equals("G"))
                    if (this.estado != EstadosFantasma.MUERTO && this.estado != EstadosFantasma.ESPERASPAWN) // La "G" => Puerta del SPAWN. Solo traspasable en estado MUERTO o de ESPERA
                        direcciones.replace(direccion, false);
            }
        }

        return direcciones;
    }

    public String calcularDistancias(){
        HashMap<String, Boolean> direccionesValidas = posicionesValidas();
        HashMap<String, Double> direccionesValores = new HashMap<>();

        for(String direccion: direccionesValidas.keySet()){
            if(!direccionesValidas.get(direccion)){
                direccionesValores.put(direccion, null);
                continue;
            }

            Posicion posInicial = posicionEnDireccion(getPosicion(), direccion);

//            ## DEBUG LINEA
//            gc.setStroke(getColorDebug());
//            gc.setLineWidth(2);
//            Posicion pos1Linea = new Posicion(pos1.getX(), pos1.getY());
//            Posicion pos2Linea = new Posicion(getObjetivo().getX(), getObjetivo().getY());
//
//            pos1Linea.setX(Math.round(pos1Linea.getX() * 8 * Constantes.ESCALADO_SPRITE + 8 * Constantes.ESCALADO_SPRITE / 2 - 2 * 8 * Constantes.ESCALADO_SPRITE));
//            pos1Linea.setY(Math.round(pos1Linea.getY() * 8 * Constantes.ESCALADO_SPRITE + 8 * Constantes.ESCALADO_SPRITE / 2));
//
//            pos2Linea.setX(Math.round(pos2Linea.getX() * 8 * Constantes.ESCALADO_SPRITE + 8 * Constantes.ESCALADO_SPRITE / 2 - 2 * 8 * Constantes.ESCALADO_SPRITE));
//            pos2Linea.setY(Math.round(pos2Linea.getY() * 8 * Constantes.ESCALADO_SPRITE + 8 * Constantes.ESCALADO_SPRITE / 2));
//
//            if (estado != EstadosFantasma.HUIDA)
//                gc.strokeLine(pos1Linea.getX(), pos1Linea.getY(), pos2Linea.getX(), pos2Linea.getY());



            double distancia = Posicion.calcularDistancia(posInicial, getObjetivo());
            direccionesValores.put(direccion, distancia);
        }


        double distanciaMinimaVal = 0.0;
        String distanciaMinimaDir = null;

        for(String direccion: direccionesValores.keySet()){
            if(direccionesValores.get(direccion) == null)
                continue;
            if(direccionesValores.get(direccion) < distanciaMinimaVal || distanciaMinimaDir == null){
                distanciaMinimaVal = direccionesValores.get(direccion);
                distanciaMinimaDir = direccion;
            }

        }

        setDireccion(distanciaMinimaDir);
        return distanciaMinimaDir;


    }


    @Override
    void dibujar(GraphicsContext gc) {
        calcularDistancias();
        String sprite = "";

        double dimensionFantasma = getHojaSprites().getSpriteData().get(getColorFantasma() + "_" + getDireccion() + "_" + getFrameActual()).getWidth();
        double posicionX = this.getPosicion().getX() * Constantes.ESCALADO_SPRITE * 8 + Math.floor(8 * Constantes.ESCALADO_SPRITE / 2) - Math.floor(13 / 2* Constantes.ESCALADO_SPRITE)  - 2 * 8 * Constantes.ESCALADO_SPRITE;
        double posicionY = this.getPosicion().getY() * Constantes.ESCALADO_SPRITE * 8 + Math.floor(8 * Constantes.ESCALADO_SPRITE / 2) - Math.floor(13 / 2* Constantes.ESCALADO_SPRITE) ;



        switch(this.estado){
            case MUERTO:
                sprite = "ojos_" + getDireccion();
            break;
            case HUIDA:
                double tiempoRestante = Controlador.finHuidaFantasmas - Controlador.ahora();
                if (tiempoRestante < 3500){
                    if (huidaSiguienteFrame == -1){
                        huidaSiguienteFrame = Controlador.ahora() + 500;
                    }
                    if (Controlador.ahora() > huidaSiguienteFrame){
                        if (ultimoFrame.contains("azul")){
                            sprite = "huida_blanco_" + getFrameActual();
                        } else {
                            sprite = "huida_azul_" + getFrameActual();
                        }
                        huidaSiguienteFrame = Controlador.ahora() + 300;
                    } else {
                        sprite = ultimoFrame;
                    }
                } else {
                    sprite = "huida_azul_" + getFrameActual();
                }
            break;
            default:
                sprite = getColorFantasma() + "_" + getDireccion() + "_" + getFrameActual();
            break;


        }


        this.ultimoFrame = sprite;
        ImageView spriteJugador = new ImageView(getHojaSprites().getSpriteData().get(sprite));


        // Generamos una captura de la imagen y la guardamos
        WritableImage spriteFinal = new WritableImage((int)spriteJugador.getBoundsInLocal().getWidth(), (int)spriteJugador.getBoundsInLocal().getHeight());
        spriteJugador.snapshot(null, spriteFinal);

        spriteFinal = Utilidades.aplicarTransparencia(spriteFinal);

        //gc.drawImage(getHojaSprites().getSpriteData().get("frame_1"), posX_fantasma, posY_fantasma, dim_fantasma, dim_fantasma);
        gc.drawImage(spriteFinal, posicionX, posicionY, dimensionFantasma, dimensionFantasma);
    }

    public void cambiarEstado(EstadosFantasma nuevoEstado){
        this.estadoAnterior = this.estado;
        this.estado = nuevoEstado;

        if (this.estado == EstadosFantasma.DISPERSION)
            this.objetivo = new Posicion(this.objetivoDispersion.getX(), this.objetivoDispersion.getY());
        else if (this.estado == EstadosFantasma.MUERTO)
            this.objetivo = new Posicion(this.posicionSpawnOjos.getX(), this.posicionSpawnOjos.getY());
        else if (this.estado == EstadosFantasma.ESPERASPAWN)
                this.objetivo = new Posicion(16, 14);

        String helper = getDireccion();
        setDireccion(direccionContraria);
        this.direccionContraria = helper;
    }

    abstract void establecerObjetivoAtaque(Jugador jugador, Rojo rojo);

    public void detectarColisionJugador(Jugador jugador){
        if (getPosicion().getX() == jugador.getPosicion().getX() && getPosicion().getY() == jugador.getPosicion().getY()){
            if (this.estado == EstadosFantasma.HUIDA) { // Jugador come a fantasma
                Controlador.controladorSonido.getJugadorComerFantasma().play();
                cambiarEstado(EstadosFantasma.MUERTO);
                Controlador.puntuacion += 4000;
            }
            else if (this.estado != EstadosFantasma.MUERTO){ // Si el fantasma puede matar
                if (Controlador.jugador.isConVida() && !Controlador.perdido){
                    System.out.println("Jugador eliminado!");
                    Controlador.jugador.setConVida(false);
                    Controlador.momentoPerder = Controlador.ahora() + Constantes.COOLDOWN_MUERTE_JUGADOR;
                }
            }
        }
    }

    @Override
    void reiniciar(double miliSegundosExtra){
        switch(this.colorFantasma){
            case "rojo":
                setDireccion("izq");
                setDireccionContraria("der");
                setPosicion(new Posicion(16, 14));
                setEstado(EstadosFantasma.ATAQUE);
                this.objetivo = new Posicion(-1, -1);
                this.momentoSpawnInicial = Controlador.ahora() + this.esperaSpawnInicial * 1000 + miliSegundosExtra;
            break;
            case "rosa":
                setDireccion("abj");
                setDireccionContraria("arr");
                setPosicion(new Posicion(15, 17));
                setEstado(EstadosFantasma.ESPERASPAWNINICIAL);
                this.objetivo = new Posicion(-1, -1);
                this.momentoSpawnInicial = Controlador.ahora() + this.esperaSpawnInicial * 1000 + miliSegundosExtra;
            break;
            case "azul":
                setDireccion("arr");
                setDireccionContraria("abj");
                setPosicion(new Posicion(14, 17));
                setEstado(EstadosFantasma.ESPERASPAWNINICIAL);
                this.objetivo = new Posicion(-1, -1);
                this.momentoSpawnInicial = Controlador.ahora() + this.esperaSpawnInicial * 1000 + miliSegundosExtra;
                break;
            case "naranja":
                setDireccion("arr");
                setDireccionContraria("abj");
                setPosicion(new Posicion(17, 17));
                setEstado(EstadosFantasma.ESPERASPAWNINICIAL);
                this.objetivo = new Posicion(-1, -1);
                this.momentoSpawnInicial = Controlador.ahora() + this.esperaSpawnInicial * 1000 + miliSegundosExtra;
                break;
        }
    }
}
