package org.hugom;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import java.io.IOException;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.json.simple.parser.ParseException;
import javafx.scene.text.*;
import javafx.scene.image.ImageView;

public class Main extends Application {

    ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/media/imagen/logo.png")));


    public Main() throws IOException, ParseException {
    }


    @Override
    public void start(Stage primaryStage) throws IOException, ParseException {
        Font fuente = Font.loadFont(getClass().getResourceAsStream("/media/tipografia/fuente.ttf"), Constantes.ESCALADO_SPRITE * Constantes.CUADRICULA_MAPA);


        Text textoPuntuacion = new Text(0, 30, "HIGH SCORE\n" + 0 + "  ");
        textoPuntuacion.setX(Constantes.ANCHOVENTANA/2 - textoPuntuacion.getLayoutBounds().getWidth() / 2 - 15);
        textoPuntuacion.setFont(fuente);
        textoPuntuacion.setFill(Color.WHITE);

        Text textoReady = new Text(0, 500, "Ready!");
        textoReady.setFont(fuente);
        textoReady.setX(Constantes.ANCHOVENTANA/2 - textoReady.getLayoutBounds().getWidth() / 2);
        textoReady.setFill(Color.YELLOW);

        // textos ventana 0
        Text textoPulsarBoton = new Text(0, 750, "PRESIONA UNA TECLA PARA\nJUGAR");
        textoPulsarBoton.setTextAlignment(TextAlignment.CENTER);
        textoPulsarBoton.setFont(fuente);
        textoPulsarBoton.setX(Constantes.ANCHOVENTANA/2 - textoPulsarBoton.getLayoutBounds().getWidth() / 2);
        textoPulsarBoton.setFill(Color.WHITE);

        Text textoAutor = new Text(0, 225, "POR HUGO MOREDA");
        textoAutor.setTextAlignment(TextAlignment.CENTER);
        textoAutor.setFont(fuente);
        textoAutor.setX(Constantes.ANCHOVENTANA/2 - textoAutor.getLayoutBounds().getWidth() / 2);
        textoAutor.setFill(Color.WHITE);


        Text textoControles = new Text(0, 400, "CONTROLES:\n\nWASD - MOVIMIENTO\nM - DES/ACTIVAR SONIDO\nESC - SALIR");
        textoControles.setTextAlignment(TextAlignment.CENTER);
        textoControles.setFont(fuente);
        textoControles.setX(Constantes.ANCHOVENTANA/2 - textoControles.getLayoutBounds().getWidth() / 2);
        textoControles.setFill(Color.WHITE);

        Controlador.getInstance();



        Pane root = new Pane();
        Scene scene = new Scene(root, Constantes.ANCHOVENTANA, Constantes.ALTOVENTANA);

        Canvas canvas = new Canvas(scene.getWidth(), scene.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();

        logo.setX(Constantes.ANCHOVENTANA / 2 - 512 / 2);
        logo.setY(50);


        root.getChildren().add(canvas);
        root.getChildren().add(textoPuntuacion);
        root.getChildren().add(textoReady);
        root.getChildren().add(textoPulsarBoton);
        root.getChildren().add(textoAutor);
        root.getChildren().add(textoControles);
        root.getChildren().add(logo);

        Controlador.controladorSonido.getJugadorComer().play();





        // Teclas
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP) {
                Controlador.jugador.actualizarDireccion("arr", true);
            } else if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) {
                Controlador.jugador.actualizarDireccion("izq", true);
            } else if (event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN) {
                Controlador.jugador.actualizarDireccion("abj", true);
            } else if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) {
                Controlador.jugador.actualizarDireccion("der", true);
            }  else if (event.getCode() == KeyCode.ENTER){
                Controlador.ventanaActual += 1;
            }
            // ## TECLAS DEBUG ##
            else if (event.getCode() == KeyCode.O){
                Controlador.nivelActual++;
            } else if (event.getCode() == KeyCode.K){
                Controlador.nivelActual--;
            } else if (event.getCode() == KeyCode.DIGIT1){
                Controlador.forzarHuidaFantasmas();
            } else if (event.getCode() == KeyCode.DIGIT2){
                Controlador.actualizarEstadosFantasmas(EstadosFantasma.DISPERSION);
            } else if (event.getCode() == KeyCode.DIGIT3){
                Controlador.actualizarEstadosFantasmas(EstadosFantasma.ATAQUE);
            } else if (event.getCode() == KeyCode.DIGIT4){
                Controlador.actualizarEstadosFantasmas(EstadosFantasma.MUERTO);
            } else if (event.getCode() == KeyCode.DIGIT5){
                Controlador.actualizarEstadosFantasmas(EstadosFantasma.ESPERASPAWN);
            } else if (event.getCode() == KeyCode.DIGIT8){
                int frame = Controlador.jugador.getFrameActual() - 1;
                if(Controlador.jugador.getFrameActual() == 0)
                    frame = 13;
                Controlador.jugador.setFrameActual(frame);
            } else if (event.getCode() == KeyCode.DIGIT9){
                int frame = Controlador.jugador.getFrameActual() + 1;
                if(Controlador.jugador.getFrameActual() == 13)
                    frame = 0;
                Controlador.jugador.setFrameActual(frame);
            } else if (event.getCode() == KeyCode.Q){
                Controlador.ventanaActual -= 1;
            }
            // ## FIN TECLAS DEBUG ##
        });

        // Frames
        AnimationTimer timer = new AnimationTimer(){
            @Override
            public void handle(long now) {
                if (Controlador.ventanaActual == 0){
                    gc.setFill(Color.BLACK);
                    textoPuntuacion.setVisible(false);
                    textoReady.setVisible(false);
                    textoPulsarBoton.setVisible(true);
                    textoAutor.setVisible(true);
                    textoControles.setVisible(true);
                    logo.setVisible(true);

                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                }
                else if  (Controlador.ventanaActual == 1){
                    textoPuntuacion.setVisible(true);
                    textoReady.setVisible(true);
                    textoPulsarBoton.setVisible(false);
                    textoAutor.setVisible(false);
                    textoControles.setVisible(false);
                    logo.setVisible(false);


                    if (!Controlador.juegoEnCurso) {
                        if (!Controlador.controladorSonido.getInicioJuego().isPlaying()){
                            Controlador.controladorSonido.getInicioJuego().play();
                            Controlador.iniciarJuego();
                        }
                        if (Controlador.ahora() > Controlador.juegoMomentoInicio) {
                            Controlador.juegoEnCurso = true;
                            textoReady.setText("");
                        }
                    }
                    if (Controlador.perdido && !Controlador.jugador.isConVida()) {
                        if (!Controlador.restadoPerdido) {
                            if (Controlador.vidasJugador > 0) {
                                Controlador.restadoPerdido = true;
                                Controlador.juegoMomentoInicio = Controlador.ahora() + 2000;
                            }
                        }
                        if (Controlador.ahora() > Controlador.juegoMomentoInicio) {
                            Controlador.controladorSonido.getHuidaFantasmas().stop();
                            Controlador.controladorSonido.getVueltaSpawnFantasma().stop();
                            Controlador.jugador.setConVida(true);
                            Controlador.perdido = false;
                            Controlador.reiniciarPosiciones(0);
                            Controlador.restadoPerdido = false;
                            Controlador.vidasJugador -= 1;
                        }
                    }

                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    Controlador.dibujarMapeado(gc);

                    Controlador.jugador.dibujar(gc);

                    if (Controlador.juegoEnCurso && Controlador.jugador.isConVida())
                        Controlador.jugador.mover();


                    Controlador.establecerObjetivosFantasmas();

                    if (Controlador.juegoEnCurso && Controlador.jugador.isConVida())
                        Controlador.moverFantasmas();

                    Controlador.dibujarFantasmas(gc);
                    Controlador.tocarSonidosMuerteFantasma();

                    if (!Controlador.jugador.isConVida() && !Controlador.perdido) {
                        if (Controlador.ahora() > Controlador.momentoPerder) {
                            Controlador.perdido = true;
                            Controlador.controladorSonido.getMuerteJugador().play();
                        }
                    }


                    textoPuntuacion.setTextAlignment(TextAlignment.RIGHT);
                    textoPuntuacion.setText("HIGH SCORE\n" + Controlador.puntuacion + "  ");

                    Controlador.dibujarVidas(gc);
                    Controlador.dibujarNivel(gc);

                }
            }
        };

        timer.start();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
