package tpe;

import java.util.LinkedList;

public class Procesador {
    private String id_procesador;
    private String cod_procesador;
    private boolean is_refrigerado;
    private int anio_funcionamiento;
    private int criticasPermitidasAlMomento = 2;
    private LinkedList<Tarea> tareas;

    public Procesador(String id_procesador, String cod_procesador, boolean is_refrigerado, int anio_funcionamiento) {
        this.id_procesador = id_procesador;
        this.cod_procesador = cod_procesador;
        this.is_refrigerado = is_refrigerado;
        this.anio_funcionamiento = anio_funcionamiento;
        this.tareas = new LinkedList<>();
    }

    public String getIdProcesador() {
        return id_procesador;
    }

    public void setIdProcesador(String id_procesador) {
        this.id_procesador = id_procesador;
    }

    public void setCodProcesador(String cod_procesador) {
        this.cod_procesador = cod_procesador;
    }

    public void setIsRefrigerado(boolean is_refrigerado) {
        this.is_refrigerado = is_refrigerado;
    }

    public void setAnioFuncionamiento(int anio_funcionamiento) {
        this.anio_funcionamiento = anio_funcionamiento;
    }

    public String getCodprocesador() {
        return cod_procesador;
    }

    public boolean isRefrigerado() {
        return is_refrigerado;
    }

    public int getAnioFuncionamiento() {
        return anio_funcionamiento;
    }

    public LinkedList<Tarea> getTareas() {
        return new LinkedList<>(tareas);
    }

    public void setTareas(LinkedList<Tarea> tareas) {
        this.tareas = tareas;
    }

    public void addTarea(Tarea tarea) {
        tareas.add(tarea);
    }

    public void deleteTarea(Tarea tarea) {
        tareas.remove(tarea);
    }

    public int getTiempoTotalEjecucion(Integer tiempoMaxNoRefrigerado) {
        int tiempoTotal = 0;
        for (Tarea tarea : tareas) {
            int tiempoTarea = tarea.getTiempoEjecucion();
            tiempoTotal += tiempoTarea;

            if(!this.is_refrigerado && tiempoTotal > tiempoMaxNoRefrigerado) {
                tiempoTotal -= tiempoTarea;
            }
            // los procesadores no refrigerados no podrán dedicar más de X tiempo de ejecución a las tareas asignadas.
        }
        return tiempoTotal;
    }

    public Procesador clonar() {
        Procesador nuevoProcesador = new Procesador(this.id_procesador, this.cod_procesador,
                this.is_refrigerado, this.anio_funcionamiento);
        for (Tarea tarea : this.tareas) {
            nuevoProcesador.addTarea(tarea);
        }
        return nuevoProcesador;
    }

    public int getCriticasPermitidasAlMomento() {
        return criticasPermitidasAlMomento;
    }

    public void setCriticasPermitidasAlMomento(int criticasPermitidasAlMomento) {
        this.criticasPermitidasAlMomento = criticasPermitidasAlMomento;
    }

    @Override
    public String toString() {
        return "Procesador{" +
                "id=' " + id_procesador + '\'' +
                ", codigo= '" + cod_procesador + '\'' +
                ", refrigerado= " + is_refrigerado +
                ", anio_funcionamiento= " + anio_funcionamiento +
                ", tareas= " + tareas +
                '}';
    }
}
