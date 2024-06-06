package tpe;

import java.util.LinkedList;

public class Solucion {
    private LinkedList<Procesador> mapaSolucion;
    private Integer mejorTiempo;

    public Solucion() {
        this.mapaSolucion = new LinkedList<>();
        this.mejorTiempo = 0;
    }

    public LinkedList<Procesador> getSolucion() {
        return mapaSolucion;
    }

    public Integer getMejorTiempo() {
        return mejorTiempo;
    }

    public void setMejorTiempo(Integer mejorTiempo) {
        this.mejorTiempo = mejorTiempo;
    }

    public void addProcesador(Procesador p1) {
        if (!mapaSolucion.contains(p1)) {
            mapaSolucion.add(p1);
        }
    }

    public void addAll(LinkedList<Procesador> procesadores) {
        mapaSolucion.clear();
        mapaSolucion.addAll(procesadores);
    }


    public void clearSolucion() {
        this.mejorTiempo = 0;
        this.mapaSolucion.clear();
    }

    @Override
    public String toString() {
        return "Solucion con un tiempo de: " + mejorTiempo +
                ". La lista de procesadores es {" + mapaSolucion +
                '}';
    }
}
