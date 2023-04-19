import javafx.application.Application;
import javafx.scene.Scene;
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

public class Main extends Application {



    public Jugador jugador = new Jugador(new Posicion(8, 7), "abj", new HojaSprites("src/media/imagen/jugador.png", "src/datos/indicesSprites/spritesJugador.json"), 0, 0, 0, "rojo", 0, 0);

    public Main() throws IOException, ParseException {
    }


    @Override
    public void start(Stage primaryStage) throws IOException, ParseException {
        Text texto = new Text(0, 30, "SCORE: 0!");


        //Font fuente = new Font("Serif", 25);
        Font fuente = Font.loadFont("file:src/media/tipografia/fuente.ttf", Constantes.ESCALADO_SPRITE * Constantes.CUADRICULA_MAPA);

        texto.setFont(fuente);

        //texto.getStrokeWidth();
        texto.setX(Constantes.ANCHOVENTANA/2 - texto.getLayoutBounds().getWidth() / 2 - 15);




        texto.setFill(Color.WHITE);
        Controlador.getInstance();



        Pane root = new Pane();
        Scene scene = new Scene(root, Constantes.ANCHOVENTANA, Constantes.ALTOVENTANA);

        Canvas canvas = new Canvas(scene.getWidth(), scene.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        root.getChildren().add(texto);






        // Teclas
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.W) {
                jugador.actualizarDireccion("arr", true);
            } else if (event.getCode() == KeyCode.A) {
                jugador.actualizarDireccion("izq", true);
            } else if (event.getCode() == KeyCode.S) {
                jugador.actualizarDireccion("abj", true);
            } else if (event.getCode() == KeyCode.D) {
                jugador.actualizarDireccion("der", true);
            } else if (event.getCode() == KeyCode.O){
                Controlador.nivelActual++;
            } else if (event.getCode() == KeyCode.K){
                Controlador.nivelActual--;
            }
        });

        // Frames
        AnimationTimer timer = new AnimationTimer(){

            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                Controlador.dibujarMapeado(gc);

                jugador.dibujar(gc);
                jugador.mover();
                texto.setTextAlignment(TextAlignment.RIGHT);
                texto.setText("HIGH SCORE\n" + Controlador.puntuacion + "  ");

                Controlador.dibujarNivel(gc);

                Controlador.estadoGlobal = EstadosFantasma.HUIDA;
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
