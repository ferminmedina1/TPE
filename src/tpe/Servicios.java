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
	private LinkedList<Tarea> listaTareas;
	private LinkedList<Procesador> listaProcesadores;
	private LinkedList<Procesador> mejorSolucion;
	int mejorTiempo;

	/*
     * Complejidad del constructor:
     * 0(m + n) donde m es el numero de procesadores y n es el numero de tareas.
     */
	public Servicios(String pathProcesadores, String pathTareas) {
		CSVReader reader = new CSVReader();
		reader.readProcessors(pathProcesadores);
		reader.readTasks(pathTareas);
		procesadores = new HashMap<>(reader.getProcesadores());
		tareas = new HashMap<>(reader.getTareas());
		listaTareas = new LinkedList<>(reader.getTareas().values());
		listaProcesadores = new LinkedList<>(reader.getProcesadores().values());
		mejorTiempo = 0;
		mejorSolucion = new LinkedList<>();
	}

	/*
     * Complejidad:
     * 0(1)
     */
	public Tarea servicio1(String ID) {
		return tareas.get(ID);
	}
    
    /*
     * Complejidad:
     * O(n) donde n es el número total de tareas.
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
     * Complejidad:
     * O(n) donde n es el numero total de tareas
     */
	public List<Tarea> servicio3(int prioridadInferior, int prioridadSuperior) {
		List<Tarea> tareasFiltradas = new LinkedList<>();
		for (Tarea tarea: this.tareas.values()) {
			int prioridad = tarea.getNivelprioridad();
			if (prioridad >= prioridadInferior && prioridad <= prioridadSuperior) {
				tareasFiltradas.add(tarea);
			}
		}
		return tareasFiltradas;
	}

	//Parte 2

	public LinkedList<Procesador> asignarTareasAProcesadores() {
		return asignarTareasAProcesadores(this.listaTareas, 0);
	}

	private LinkedList<Procesador> asignarTareasAProcesadores(LinkedList<Tarea> tareas, int indexTarea) {
		if (indexTarea >= tareas.size()) {
			int tiempoMaximo = getTiempoMaximo(listaProcesadores);
			if (tiempoMaximo < mejorTiempo || mejorTiempo == 0 ) {
				mejorTiempo = tiempoMaximo;
				mejorSolucion.clear();
				//debo clonar los procesadores a mi mejorSolucion para guaradar el resultado y que no se pierda
				for (Procesador procesador : listaProcesadores) {
					mejorSolucion.add(procesador.clonar());
				}
				System.out.println("Nueva mejor solución encontrada: " + mejorSolucion);
			}
			return mejorSolucion;
		}

		Tarea tarea = tareas.get(indexTarea);

		for (Procesador procesador : this.listaProcesadores) {
			procesador.addTarea(tarea);
			asignarTareasAProcesadores(tareas, indexTarea + 1);
			procesador.deleteTarea(tarea);
		}
		return mejorSolucion;
	}


	private int getTiempoMaximo(LinkedList<Procesador> solucionActual) {
		int tiempoMax = 0;
		for (Procesador procesador : solucionActual) {
			tiempoMax = Math.max(tiempoMax, procesador.getTimpoTotalEjecucion());
		}
		return tiempoMax;
	}

}
