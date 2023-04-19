public class Posicion  {
    private int x;
    private int y;

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public Posicion() {
        this.setX(0);
        this.setY(0);
    }

    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static double calcularDistancia(Posicion punto1, Posicion punto2){
        //distancia = np.around(np.linalg.norm(pos2 - pos1), 2);
        return Math.sqrt((Math.pow(punto2.x -  punto1.x, 2) + Math.pow(punto2.y - punto1.y, 2)));

    }


    public static Posicion rotar180Grados(Posicion puntoOrigen, Posicion punto){
        Posicion desplazamiento = new Posicion(punto.x - puntoOrigen.x, punto.y - puntoOrigen.y);

        // rotamos el vector desplazamiento 180 grados
        desplazamiento.x *= -1;
        desplazamiento.y *= -1;

        Posicion puntoRotado = new Posicion(puntoOrigen.x + desplazamiento.x, puntoOrigen.y + desplazamiento.y);

        return puntoRotado;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

}



