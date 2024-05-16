package tpe;

public class Tarea {
    private String id_tarea;
    private String nombre_tarea;
    private int tiempo_ejecucion;
    private boolean critica;
    private int nivel_prioridad;

    public boolean isCritica() {
        return critica;
    }

    public void setIsCritica(boolean critica) {
        this.critica = critica;
    }

    public int getNivelprioridad() {
        return nivel_prioridad;
    }

    public void setNivelprioridad(int nivel_prioridad) {
        this.nivel_prioridad = nivel_prioridad;
    }

    public Tarea(String id_tarea, String nombre_tarea, int tiempo_ejecucion) {
        this.id_tarea = id_tarea;
        this.nombre_tarea = nombre_tarea;
        this.tiempo_ejecucion = tiempo_ejecucion;
    }

    public String getIdTarea() {
        return id_tarea;
    }

    public String getNomTarea() {
        return nombre_tarea;
    }

    public int getTiempoEjecucion() {
        return tiempo_ejecucion;
    }

    public void setIdTarea(String id_tarea) {
        this.id_tarea = id_tarea;
    }

    public void setNomTarea(String nombre_tarea) {
        this.nombre_tarea = nombre_tarea;
    }

    public void setTiempoEjecucion(int tiempo_ejecucion) {
        this.tiempo_ejecucion = tiempo_ejecucion;
    }
}

