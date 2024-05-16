package tpe;

public class Procesador {
    private String id_procesador;
    private int cod_procesador;
    private boolean is_refrigerado;
    private int anio_funcionamiento;

    public Procesador(String id_procesador, int cod_procesador, boolean is_refrigerado, int anio_funcionamiento) {
        this.id_procesador = id_procesador;
        this.cod_procesador = cod_procesador;
        this.is_refrigerado = is_refrigerado;
        this.anio_funcionamiento = anio_funcionamiento;
    }

    public String getId_procesador() {
        return id_procesador;
    }

    public void setId_procesador(String id_procesador) {
        this.id_procesador = id_procesador;
    }

    public void setCod_procesador(int cod_procesador) {
        this.cod_procesador = cod_procesador;
    }

    public void setIs_refrigerado(boolean is_refrigerado) {
        this.is_refrigerado = is_refrigerado;
    }

    public void setAnio_funcionamiento(int anio_funcionamiento) {
        this.anio_funcionamiento = anio_funcionamiento;
    }

    public int getCod_procesador() {
        return cod_procesador;
    }

    public boolean isIs_refrigerado() {
        return is_refrigerado;
    }

    public int getAnio_funcionamiento() {
        return anio_funcionamiento;
    }
}
