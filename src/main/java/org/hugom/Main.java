package org.hugom;

import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.image.ImageView ;


public class Main extends Application {
    Image logoSource = new Image(getClass().getResourceAsStream("/media/imagen/logo.png"));
    ImageView logo = new ImageView(logoSource);
    Image iconoApp = new Image(getClass().getResourceAsStream("/media/imagen/icono.png"));


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage ventana) throws IOException, ParseException {
        ventana.setTitle("Pacman - por Hugo Moreda");
        ventana.getIcons().add(iconoApp);
        ventana.setResizable(false);

        Font fuente = Font.loadFont(getClass().getResourceAsStream("/media/tipografia/fuente.ttf"), Constantes.ESCALADO_SPRITE * Constantes.CUADRICULA_MAPA);


        Text textoPuntuacion = new Text(0, 30, "HIGH SCORE\n" + 0 + "  ");
        textoPuntuacion.setFont(fuente);
        textoPuntuacion.setX(Constantes.ANCHOVENTANA/2 - textoPuntuacion.getLayoutBounds().getWidth() / 2 - 15);
        textoPuntuacion.setFill(Color.WHITE);

        Text textoGameOver = new Text(0, 500, "GAME OVER");
        textoGameOver.setFont(fuente);
        textoGameOver.setX(Constantes.ANCHOVENTANA/2 - textoGameOver.getLayoutBounds().getWidth() / 2);
        textoGameOver.setFill(Color.RED);

        Text textoGameOver2 = new Text(0, 500, "[R]EINICIAR");
        textoGameOver2.setFont(fuente);
        textoGameOver2.setX(10);
        textoGameOver2.setY(Constantes.ALTOVENTANA - 13);
        textoGameOver2.setFill(Color.RED);

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


        Text textoControles = new Text(0, 400, "CONTROLES:\n\nWASD - MOVIMIENTO\nR - REINICIAR\nESC - SALIR");
        textoControles.setTextAlignment(TextAlignment.CENTER);
        textoControles.setFont(fuente);
        textoControles.setX(Constantes.ANCHOVENTANA/2 - textoControles.getLayoutBounds().getWidth() / 2);
        textoControles.setFill(Color.WHITE);

        Controlador.getInstance();



        Pane root = new Pane();
        Scene scene = new Scene(root, Constantes.ANCHOVENTANA, Constantes.ALTOVENTANA);
        scene.setFill(Color.BLACK);

        Canvas canvas = new Canvas(scene.getWidth(), scene.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();

        logo.setX(Constantes.ANCHOVENTANA / 2 - 512 / 2);
        logo.setY(50);


        root.getChildren().add(canvas);
        root.getChildren().add(textoPuntuacion);
        root.getChildren().add(textoReady);
        root.getChildren().add(textoGameOver);
        root.getChildren().add(textoGameOver2);
        root.getChildren().add(textoPulsarBoton);
        root.getChildren().add(textoAutor);
        root.getChildren().add(textoControles);
        root.getChildren().add(logo);



        // Teclas
        scene.setOnKeyPressed(event -> {

            // MOVIMIENTO DEL JUGADOR
            if(Controlador.juegoEnCurso && Controlador.jugador.isConVida())
                if (event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP) Controlador.jugador.actualizarDireccion("arr", true);
                else if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) Controlador.jugador.actualizarDireccion("izq", true);
                else if (event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN) Controlador.jugador.actualizarDireccion("abj", true);
                else if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) Controlador.jugador.actualizarDireccion("der", true);


            // TECLAS VARIAS
            if (Controlador.ventanaActual == 0)
                if (event.getCode() == KeyCode.ESCAPE)
                    Platform.exit();
                else Controlador.ventanaActual += 1;

            if (Controlador.partidaFinalizadaMostrado && event.getCode() == KeyCode.R)
                    Controlador.reiniciarJuego();



            // TECLAS DEBUG
            if (Constantes.MODODEBUG){
                if (event.getCode() == KeyCode.O) Controlador.nivelActual++;
                else if (event.getCode() == KeyCode.K) Controlador.nivelActual--;

                else if (event.getCode() == KeyCode.DIGIT1) Controlador.forzarHuidaFantasmas();
                else if (event.getCode() == KeyCode.DIGIT2) Controlador.actualizarEstadosFantasmas(EstadosFantasma.ATAQUE);
                else if (event.getCode() == KeyCode.DIGIT3) Controlador.actualizarEstadosFantasmas(EstadosFantasma.MUERTO);
                else if (event.getCode() == KeyCode.DIGIT4)
                    Controlador.actualizarEstadosFantasmas(EstadosFantasma.ESPERASPAWN);

                else if (event.getCode() == KeyCode.DIGIT8) {
                    int frame = Controlador.jugador.getFrameActual() - 1;
                    if (Controlador.jugador.getFrameActual() == 0)
                        frame = 13;
                    Controlador.jugador.setFrameActual(frame);
                } else if (event.getCode() == KeyCode.DIGIT9) {
                    int frame = Controlador.jugador.getFrameActual() + 1;
                    if (Controlador.jugador.getFrameActual() == 13)
                        frame = 0;
                    Controlador.jugador.setFrameActual(frame);
                }
            }
        });

        // Frames
        AnimationTimer timer = new AnimationTimer(){
            @Override
            public void handle(long now) {
                if (Controlador.ventanaActual == 0){
                    // Elementos de interfaz
                    gc.setFill(Color.BLACK);
                    textoPuntuacion.setVisible(false);
                    textoReady.setVisible(false);
                    textoGameOver.setVisible(false);
                    textoGameOver2.setVisible(false);
                    textoPulsarBoton.setVisible(true);
                    textoAutor.setVisible(true);
                    textoControles.setVisible(true);
                    logo.setVisible(true);
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                }
                else if  (Controlador.ventanaActual == 1){
                    // Elementos de interfaz
                    textoPuntuacion.setVisible(true);
                    textoReady.setVisible(true);
                    textoPulsarBoton.setVisible(false);
                    textoAutor.setVisible(false);
                    textoControles.setVisible(false);
                    logo.setVisible(false);



                    // Juego no estara en curso cuando:
                    // 1) Antes de iniciar por primera vez la partida.
                    // 2) Cuando se pierda una vida, durante el cooldown.
                    // 3) Cuando se pierdan todas las vidas y pulse R, actuara como 1)
                    if (!Controlador.juegoEnCurso) {
                        textoReady.setText("Ready!");
                        if (!Controlador.controladorSonido.getInicioJuego().isPlaying()){
                            Controlador.controladorSonido.getInicioJuego().play();
                            Controlador.iniciarJuego();
                        }
                        if (Controlador.ahora() > Controlador.juegoMomentoInicio) {
                            Controlador.juegoEnCurso = true;
                            textoReady.setText("");
                        }
                    }

                    if (Controlador.perdido && !Controlador.jugador.isConVida() && !Controlador.partidaFinalizada) {
                        // Restado perdido = Cooldown hasta que se reste la vida del jugador (animacion)
                        if (!Controlador.restadoPerdido) {
                            if (Controlador.jugador.getVidasRestantes() > 0) { // Si sus vidas son mayores que 0, continuamos
                                Controlador.restadoPerdido = true;
                                Controlador.juegoMomentoInicio = Controlador.ahora() + 2000;
                            } else { // Si sus vidas son 0 y pierde, fin de la partida
                                Controlador.partidaFinalizada = true;


                                Controlador.esperaRazon = "finalizar";
                                Controlador.esperaMomento = Controlador.ahora() + 2000;
                            }
                        }
                        // Cuando sea el momento de re-spawnear el jugador, restamos vida, reiniciamos posiciones, etc
                        if (Controlador.ahora() > Controlador.juegoMomentoInicio && !Controlador.partidaFinalizada) {
                            Controlador.controladorSonido.getHuidaFantasmas().stop();
                            Controlador.controladorSonido.getVueltaSpawnFantasma().stop();
                            Controlador.jugador.setConVida(true);
                            Controlador.perdido = false;
                            Controlador.reiniciarPosiciones(0, false);
                            Controlador.restadoPerdido = false;
                            Controlador.jugador.setVidasRestantes(Controlador.jugador.getVidasRestantes() - 1); // Restamos 1 vida
                        }
                    }

                    switch (Controlador.esperaRazon) {
                        case "finalizar":
                            if (!Controlador.partidaFinalizadaMostrado)
                                if (Controlador.ahora() > Controlador.esperaMomento) {
                                    Controlador.partidaFinalizadaMostrado = true;
                                    textoGameOver.setVisible(true);
                                    textoGameOver2.setVisible(true);
                                }
                            break;
                        case "perdido":
                            if (!Controlador.jugador.isConVida() && !Controlador.perdido)
                                if (Controlador.ahora() > Controlador.esperaMomento) {
                                    Controlador.perdido = true;
                                    Controlador.controladorSonido.getMuerteJugador().play();
                                }
                            break;
                        case "avance":
                            if (Controlador.nivelFinalizado)
                                if (Controlador.ahora() > Controlador.esperaMomento)
                                    Controlador.siguienteNivel();
                            break;
                    }


                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    Controlador.dibujarMapeado(gc);

                    Controlador.jugador.dibujar(gc);

                    if (Controlador.juegoEnCurso && Controlador.jugador.isConVida() && !Controlador.nivelFinalizado)
                        Controlador.jugador.mover();


                    Controlador.establecerObjetivosFantasmas();

                    if (Controlador.juegoEnCurso && Controlador.jugador.isConVida() && !Controlador.nivelFinalizado)
                        Controlador.moverFantasmas();

                    Controlador.dibujarFantasmas(gc);
                    Controlador.tocarSonidosMuerteFantasma();


                    if (Controlador.ahora() > Controlador.momentoParpadeo && Controlador.nivelFinalizado && !Controlador.nivelParpadeando){
                        Controlador.nivelParpadeando = true;
                    }

                    if (Controlador.nivelParpadeando){
                        if (Controlador.ahora() > Controlador.siguienteParpadeo){
                            Controlador.parpadeoBlanco = !Controlador.parpadeoBlanco;
                            Controlador.siguienteParpadeo = Controlador.ahora() + 250;
                        }
                    }

                    textoPuntuacion.setTextAlignment(TextAlignment.RIGHT);
                    textoPuntuacion.setText("HIGH SCORE\n" + Controlador.puntuacion + "  ");

                    Controlador.dibujarVidas(gc);
                    Controlador.dibujarContadorNivel(gc);
                }
            }
        };

        timer.start();
        ventana.setScene(scene);
        ventana.show();
    }

}
