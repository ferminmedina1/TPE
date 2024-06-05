package tpe;

import java.util.LinkedList;

public class Solucion {
    private LinkedList<Procesador> solucion;
    private Integer mejorTiempo;

    public Solucion() {
        this.solucion = new LinkedList<>();
        this.mejorTiempo = 0;
    }

    public LinkedList<Procesador> getSolucion() {
        return solucion;
    }

    public Integer getMejorTiempo() {
        return mejorTiempo;
    }

    public void setMejorTiempo(Integer mejorTiempo) {
        this.mejorTiempo = mejorTiempo;
    }

    public void addProcesador(Procesador p1) {
        if (!solucion.contains(p1)) {
            solucion.add(p1);
        }
    }

    public void addAll(LinkedList<Procesador> procesadores) {
        solucion.clear();
        solucion.addAll(procesadores);
    }


    public void clearSolucion() {
        this.mejorTiempo = 0;
        this.solucion.clear();
    }

    @Override
    public String toString() {
        return "Solucion con un tiempo de: " + mejorTiempo +
                ". La lista de procesadores es {" + solucion +
                '}';
    }
}
