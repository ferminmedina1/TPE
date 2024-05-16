package tpe;

import tpe.utils.CSVReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * NO modificar la interfaz de esta clase ni sus métodos públicos.
 * Sólo se podrá adaptar el nombre de la clase "Tarea" según sus decisiones
 * de implementación.
 */
public class Servicios {

	private HashMap<String, Procesador > procesadores;
	private HashMap<String, Tarea> tareas;

	/*
     * Expresar la complejidad temporal del constructor.
     */
	public Servicios(String pathProcesadores, String pathTareas) {
		CSVReader reader = new CSVReader();
		reader.readProcessors(pathProcesadores);
		reader.readTasks(pathTareas);
	}

	/*
     * Expresar la complejidad temporal del servicio 1.
     * 0(1)
     */
	public Tarea servicio1(String ID) {
		return tareas.get(ID);
	}
    
    /*
     * Expresar la complejidad temporal del servicio 2.
     * o(n) donde n es el número total de tareas
     */
	public List<Tarea> servicio2(boolean esCritica) {
		List<Tarea> tareasFiltradas  = new LinkedList<>();
		for (Tarea tarea: this.tareas.values()) {
			if (tarea.isCritica() == esCritica) {
				tareasFiltradas.add(tarea);
			}
		}
		return tareasFiltradas;
	}

    /*
     * Expresar la complejidad temporal del servicio 3.
     */
	public List<Tarea> servicio3(int prioridadInferior, int prioridadSuperior) {
		List<Tarea> tareasFiltradas = new LinkedList<>();
		for (Tarea tarea: this.tareas.values()) {
			int prioridad = tarea.getNivelprioridad();
			if (prioridad >= prioridadInferior || prioridad <= prioridadSuperior) {
				tareasFiltradas.add(tarea);
			}
		}
		return tareasFiltradas;

	}

}
