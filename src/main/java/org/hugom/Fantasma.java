package org.hugom;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.util.*;

public abstract class Fantasma extends Entidad {
    private Posicion objetivo;
    private String direccionContraria;
    private EstadosFantasma estado;
    private Posicion posicionSpawnOjos;
    private String colorFantasma;
    private long huidaSiguienteFrame = -1;
    private String ultimoFrame = "null";
    private double esperaSpawnInicial = -1;
    private double momentoSpawnInicial = -1;
    private boolean haSpawneado = false;

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
    public String getColorFantasma() {
        return colorFantasma;
    }
    public double getEsperaSpawnInicial() {
        return esperaSpawnInicial;
    }
    public double getMomentoSpawnInicial() {
        return momentoSpawnInicial;
    }
    public void setMomentoSpawnInicial(double momentoSpawnInicial) {
        this.momentoSpawnInicial = momentoSpawnInicial;
    }
    public boolean isHaSpawneado() {
        return haSpawneado;
    }
    public void setHaSpawneado(boolean haSpawneado) {
        this.haSpawneado = haSpawneado;
    }

    public Fantasma(Posicion posicion, String direccion , Color colorDebug, Posicion objetivo, EstadosFantasma estado, String colorFantasma, double esperaSpawnInicial) {
        super(posicion, direccion, colorDebug);
        this.objetivo = objetivo;
        this.direccionContraria = direccionContrariaDe(direccion);
        this.estado = estado;
        this.posicionSpawnOjos = new Posicion(14, 17); // Ubicacion donde iran los ojos, una vez el fantasma muera
        this.colorFantasma = colorFantasma;
        this.haSpawneado = false;
        if(!colorFantasma.equals("rojo")) {
            this.haSpawneado = true;
            this.esperaSpawnInicial = esperaSpawnInicial;
            this.momentoSpawnInicial = Controlador.ahora() + esperaSpawnInicial * 1000 + Constantes.COOLDOWN_INICIO_GAME;
        }
    }


    /**
     * Funcion que se encarga de mover al fantasma. Movera una posicion sobre la coordenada X o Y, dependiendo del estado del fantasma, direccion y posicion de este.
     * Tambien, esta funcion se encarga de sumar 1 al frame en la animacion del fantasma, pues queremos que se actualice como maximo 1 vez por movimiento.
     */
    @Override
    public void mover() {
        String direccion;

        if(Controlador.ahora() < getSiguienteMovimiento()){ // Si aun no tiene que moverse, comprobaremos si al menos podemos cambiar de frame
            if(Controlador.ahora() > getSiguienteFrame()){
                setFrameActual(getFrameActual() == 0 ? 1 : 0);
                setSiguienteFrame(Controlador.ahora() + Constantes.COOLDOWN_FRAME);
            }
            return;
        }

        if (getEstado() == EstadosFantasma.ESPERASPAWNINICIAL){
            this.objetivo = new Posicion(new Random().nextInt(3) + 14, 16);
        }

        // El cooldown de movimiento (rapidez) dependera del estado en el que se encuentra el fantasma
        switch(this.estado){
            case HUIDA:
                setSiguienteMovimiento(Controlador.ahora() + Constantes.COOLDOWN_MOVIMIENTO_FANTASMA_HUIDA);
                break;
            case MUERTO:
                setSiguienteMovimiento(Controlador.ahora() + Constantes.COOLDOWN_MOVIMIENTO_FANTASMA_MUERTO);
                break;
            default:
                setSiguienteMovimiento(Controlador.ahora() + Constantes.COOLDOWN_MOVIMIENTO_FANTASMA);
                break;
        }

        if(getEstado() == EstadosFantasma.HUIDA) {
            setObjetivo(new Posicion(-1, -1));
            ArrayList<String> soloValidas = new ArrayList<>();

            for (String posicion : posicionesValidas().keySet()) {
                if (!posicionesValidas().get(posicion))
                    continue;
                soloValidas.add(posicion);
            }
            int aleatorio = new Random().nextInt(soloValidas.size());
            String direccionAleatoria = soloValidas.get(aleatorio);
            setDireccion(direccionAleatoria);
            direccion = direccionAleatoria;
        } else {
            direccion = calcularDistancias();
        }


        setDireccionContraria(direccionContrariaDe(direccion));
        switch(direccion){
            case "izq":
                getPosicion().setX(getPosicion().getX()-1);
                break;
            case "der":
                getPosicion().setX(getPosicion().getX()+1);
                break;
            case "arr":
                getPosicion().setY(getPosicion().getY()-1);
                break;
            case "abj":
                getPosicion().setY(getPosicion().getY()+1);
                break;
        }

        if (getPosicion().getX() == 32) getPosicion().setX(0);
        else if (getPosicion().getX() == -1) getPosicion().setX(31);

        if (getPosicion().getX() == this.objetivo.getX() && getPosicion().getY() == this.objetivo.getY()){
            // Si llega a su objetivo y esta muerto o a la espera de spawnear:
            if(this.estado == EstadosFantasma.MUERTO) this.cambiarEstado(EstadosFantasma.ESPERASPAWN);          // Ya habra llegado al spawn
            else if (this.estado == EstadosFantasma.ESPERASPAWN) this.cambiarEstado(EstadosFantasma.ATAQUE);    // Ya habra salido del spawn
        }
    }

    /**
     * Funcion que devuelve las posiciones a las que puede moverse un fantasma desde la posicion en la que esta, en las 4 direcciones.
     * Un fantasma nunca podra moverse a:
     *    - Una posicion en la que haya estructura (pared)
     *    - Atras suyo (vuelta de 180º solo permitida al cambiar de estado)
     * @return HashMap que contiene las posiciones a las que puede moverse.
     */
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
            if (posicion.getX() == 32) posicion.setX(0);
            else if (posicion.getX() == -1) posicion.setX(31);

            if (direccion.equals(getDireccionContraria())) direcciones.replace(direccion, false);
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


    /**
     * Funcion que, una vez conseguidas las posiciones validas a las que puede moverse el fantasma, calcula mediante el teorema de pitagoras la distancia exacta desde la posicion desde
     * esa direccion hasta el fantasma, y devuelve la menor de ellas.
     * @return devuelve la direccion a la que va a moverse el fantasma (la menor)
     */
    public String calcularDistancias(){
        HashMap<String, Boolean> direccionesValidas = posicionesValidas();
        HashMap<String, Double> direccionesValores = new HashMap<>();

        for(String direccion: direccionesValidas.keySet()){
            if(!direccionesValidas.get(direccion)){
                direccionesValores.put(direccion, null);
                continue;
            }

            Posicion posInicial = posicionEnDireccion(getPosicion(), direccion);

//            ## DEBUG LINEA ENTRE POSICION DE DIRECCION Y OBJETIVO
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



    /**
     * La funcion dibuja el fantasma sobre el GraphicsContext. El sprite que dibujara sera dependiendo el color del fantasma, y el estado en el que este:<br>
     * <ul>
     * <li> En modo muerto: {@code "ojos_" + la direccion a la que mira}</li>
     * <li> En modo huida: {@code "huida_" + color azul o blanco, dependiendo si debe de parpadear o no + "_" + el frame actual}</li>
     * <li> En el resto: {@code color del fantasma + "_" + la direccion a la que mira + "_" + el frame actual}</li>
     * </ul>
     * @param gc GraphicsContext del Main sobre el que dibujar
     */
    @Override
    void dibujar(GraphicsContext gc) {
        String sprite;

        double dimensionFantasma = getHojaSprites().getSpriteData().get(getColorFantasma() + "_" + getDireccion() + "_" + getFrameActual()).getWidth();
        double posicionX = this.getPosicion().getX() * Constantes.ESCALADO_SPRITE * 8 + Math.floor(8 * Constantes.ESCALADO_SPRITE / 2) - Math.floor(13 / 2 * Constantes.ESCALADO_SPRITE)  - 2 * 8 * Constantes.ESCALADO_SPRITE;
        double posicionY = this.getPosicion().getY() * Constantes.ESCALADO_SPRITE * 8 + Math.floor(8 * Constantes.ESCALADO_SPRITE / 2) - Math.floor(13 / 2 * Constantes.ESCALADO_SPRITE) ;



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

        gc.drawImage(spriteFinal, posicionX, posicionY, dimensionFantasma, dimensionFantasma);
    }

    /**
     * Funcion que se encarga de que cada fantasma cambie de estado correctamente, haciendo los cambios necesarios en cada estado, si es necesario:
     *   - Si cambia a modo {@link EstadosFantasma#MUERTO}, el fantasma tendra de objetivo regresar al spawn.
     *   - Si cambia a modo {@link EstadosFantasma#ESPERASPAWN}, significa que el fantasma muerto ya llego al spawn, y su objetivo es salir de este.
     * @param nuevoEstado el estado al que se va a cambiar el fantasma
     */
    public void cambiarEstado(EstadosFantasma nuevoEstado){
        this.estado = nuevoEstado;

        if (this.estado == EstadosFantasma.MUERTO)
            this.objetivo = new Posicion(this.posicionSpawnOjos.getX(), this.posicionSpawnOjos.getY());
        else if (this.estado == EstadosFantasma.ESPERASPAWN)
            this.objetivo = new Posicion(16, 14);

        String helper = getDireccion();
        setDireccion(direccionContraria);
        this.direccionContraria = helper;
    }



    /**
     * Funcion que detecta si el fantasma y el jugador tienen la misma posicion (coordenadas X e Y iguales), y
     * realiza las acciones dependiendo si el fantasma esta vivo o muerto.
     * @param jugador de la que se usa su posicion
     */
    public void detectarColisionJugador(Jugador jugador){
        if (getPosicion()
    /**
     * Funcion que se encarga de mover al jugador. Movera una posicion sobre la coordenada X o Y, dependiendo la direccion actual del jugador.
     * Tambien, esta funcion se encarga de sumar 1 al frame en la animacion del jugador, pues queremos que se actualice como maximo 1 vez por movimiento.
     */.getX() == jugador.getPosicion().getX() && getPosicion().getY() == jugador.getPosicion().getY()){
            if (this.estado == EstadosFantasma.HUIDA) { // Jugador come a fantasma
                Controlador.controladorSonido.getJugadorComerFantasma().play();
                cambiarEstado(EstadosFantasma.MUERTO);
                Controlador.puntuacion += Constantes.PUNTUACION_FANTASMA_BASE;
                Controlador.comprobarAnyadirVida();
            }
            else if (this.estado != EstadosFantasma.MUERTO){ // Si el fantasma puede matar
                if (Controlador.jugador.isConVida() && !Controlador.perdido){
                    System.out.println("Jugador eliminado!");
                    Controlador.jugador.setConVida(false);

                    Controlador.esperaRazon = "perdido";
                    Controlador.esperaMomento = Controlador.ahora() + Constantes.COOLDOWN_MUERTE_JUGADOR;
                }
            }
        }
    }


    /**
     * Funcion que devuelve la direccion contraria a una direccion.
     * @param direccion original
     * @return direccion contraria a esa direccion
     */
    public String direccionContrariaDe(String direccion){
        switch(direccion){
            case "arr": return "abj";
            case "abj": return "arr";
            case "der": return "izq";
            case "izq": return "der";
            default: return null;
        }
    }

    abstract void establecerObjetivoAtaque(Jugador jugador, Rojo rojo);
}
