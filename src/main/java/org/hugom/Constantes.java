package org.hugom;

public class Constantes {

    // MAPEADO
    final static int FILAS = 28 + 4 - 4;
    final static int COLUMNAS = 32 + 4;
    final static float ESCALADO_SPRITE = 3;
    final static int CUADRICULA_MAPA = 8;

    // VENTANA
    final static float ANCHOVENTANA = FILAS * ESCALADO_SPRITE * CUADRICULA_MAPA;
    final static float ALTOVENTANA = COLUMNAS * ESCALADO_SPRITE * CUADRICULA_MAPA;

    // PUNTUACIONES
    final static int PUNTUACION_FRUTA_PEQ = 10;
    final static int PUNTUACION_FRUTA_GRA = 50;
    final static int PUNTUACION_FANTASMA_BASE = 200;
    final static int PUNTUACION_FANTASMA_MULTIPLICADOR = 2;

    // COOLDOWNS
    final static int COOLDOWN_FRAME = 100;
    final static int COOLDOWN_MOVIMIENTO_JUGADOR = 140;
    final static int COOLDOWN_MOVIMIENTO_FANTASMA = 140;
    final static int COOLDOWN_MOVIMIENTO_FANTASMA_HUIDA = 200;
    final static int COOLDOWN_MOVIMIENTO_FANTASMA_MUERTO = 30;
    final static int COOLDOWN_MUERTE_JUGADOR = 1000;
    final static int COOLDOWN_MUERTE_JUGADOR_ANIMACION = 100;
    final static int COOLDOWN_INICIO_GAME = 4000;

    // JUGADOR
    final static int VIDAPORPUNTUACION = 5000;
    final static int VIDASMAX = 5;
    final static int VIDAS_INICIALES = 3;

    // POSICIONES
    final static Posicion POS_JUGADOR = new Posicion(16, 26);
    final static Posicion POS_ROJO = new Posicion(16, 14);
    final static Posicion POS_ROSA = new Posicion(15, 17);
    final static Posicion POS_AZUL = new Posicion(14, 17);
    final static Posicion POS_NARANJA = new Posicion(17, 17);

}
