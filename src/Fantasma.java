public class Fantasma {
    Posicion objetivo;
    String direccionContraria;
    EstadosFantasma estado;
    Posicion objetivoDispersion;
    Posicion posicionSpawnOjos;

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
    public Posicion getObjetivoDispersion() {
        return objetivoDispersion;
    }
    public void setObjetivoDispersion(Posicion objetivoDispersion) {
        this.objetivoDispersion = objetivoDispersion;
    }
    public Posicion getPosicionSpawnOjos() {
        return posicionSpawnOjos;
    }
    public void setPosicionSpawnOjos(Posicion posicionSpawnOjos) {
        this.posicionSpawnOjos = posicionSpawnOjos;
    }
    /* Fin setters y getters */

}
