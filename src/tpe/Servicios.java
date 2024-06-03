package tpe;

import tpe.utils.CSVReader;

import java.util.*;

/**
 * NO modificar la interfaz de esta clase ni sus métodos públicos.
 * Sólo se podrá adaptar el nombre de la clase "Tarea" según sus decisiones
 * de implementación.
 */
public class Servicios {

	private Map<String, Procesador> procesadores;
	private Map<String, Tarea> tareas;
	private LinkedList<Tarea> tareasEnProcesador;
	private HashMap<Integer,LinkedList<Procesador>> mejorSolucion;
	int mejorTiempo;
	private int estadosGenerados;

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
		tareasEnProcesador = new LinkedList<Tarea>(reader.getTareas().values());
		mejorTiempo = 0;
		mejorSolucion = new HashMap<Integer,LinkedList<Procesador>>();
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

	//Parte 2 - Backtracking

	public HashMap<Integer,LinkedList<Procesador>> asignarTareasBacktracking(Integer tiempoMaxNoRefrigerado) {
		HashMap<Integer,LinkedList<Procesador>> resultado = asignarTareasBacktracking(tareasEnProcesador, 0, tiempoMaxNoRefrigerado);
		System.out.println("Cantidad de estados generados: " + estadosGenerados);
		return resultado;
	}

	private HashMap<Integer,LinkedList<Procesador>> asignarTareasBacktracking(LinkedList<Tarea> tareas, int indexTarea, int tiempoMaxNoRefrigerado) {
		estadosGenerados++;
		if (indexTarea >= tareas.size()) {
			int tiempoActual = getTiempoMaximo(procesadores.values(), tiempoMaxNoRefrigerado);
			if (tiempoActual < mejorTiempo || mejorTiempo == 0 ) {
				mejorTiempo = tiempoActual;
				mejorSolucion.clear();
				mejorSolucion.put(mejorTiempo,clonarProcesadores(procesadores)); //Se clonan los procesadores a la mejorSolucion para guaradar el resultado y que no se pierda
				//System.out.println("Nueva mejor solución encontrada: " + mejorSolucion + ". Que tiene un tiempo de ejecucion maximo de: " + mejorTiempo);
			}
			return mejorSolucion;
		}

		Tarea tarea = tareas.get(indexTarea);

		for (Procesador procesador : procesadores.values()) {
			if (puedeAsignarTarea(procesador, tarea)) {
				procesador.addTarea(tarea);
				asignarTareasBacktracking(tareas, indexTarea + 1,tiempoMaxNoRefrigerado);
				procesador.deleteTarea(tarea);
			}
		}
		return mejorSolucion;
	}

	//Parte 2 - Greedy

	public HashMap<Integer, LinkedList<Procesador>> asignarTareasGreedy(Integer tiempoMaxNoRefrigerado) {
		return asignarTareasGreedy(tareasEnProcesador, 0, tiempoMaxNoRefrigerado);
	}

	private HashMap<Integer, LinkedList<Procesador>> asignarTareasGreedy(LinkedList<Tarea> tareas, int indexTarea, int tiempoMaxNoRefrigerado) {
		LinkedList<Procesador> resultado = new LinkedList<>(procesadores.values());
		estadosGenerados = 0;

		// Ordenar las tareas por nivel de prioridad de mayor a menor (Greedy strategy)
		Collections.sort(tareasEnProcesador, (t1, t2) -> Integer.compare(t2.getTiempoEjecucion(), t1.getTiempoEjecucion()));

		for (Tarea tarea : tareasEnProcesador) {
			// Encontrar el procesador adecuado para asignar la tarea
			Procesador mejorProcesador = null;
			int menorTiempo = Integer.MAX_VALUE;

			for (Procesador procesador : resultado) {
				if (puedeAsignarTarea(procesador, tarea)) {
					int tiempoActual = procesador.getTiempoTotalEjecucion(tiempoMaxNoRefrigerado) + tarea.getTiempoEjecucion();
					if (tiempoActual < menorTiempo) {
						menorTiempo = tiempoActual;
						mejorProcesador = procesador;
					}
				}
			}

			if (mejorProcesador != null) {
				mejorProcesador.addTarea(tarea);
			}
			estadosGenerados++;
		}
		mejorTiempo = getTiempoMaximo(resultado, tiempoMaxNoRefrigerado);
		HashMap<Integer, LinkedList<Procesador>> solucionGreedy = new HashMap<>();
		solucionGreedy.put(mejorTiempo, clonarProcesadores(procesadores));
		System.out.println("Cantidad de estados generados: " + estadosGenerados);
		System.out.println("Tiempo máximo de ejecución: " + mejorTiempo);
		return solucionGreedy;
	}

	private boolean puedeAsignarTarea(Procesador procesador, Tarea tarea) {
		if (tarea.isCritica()) {
			procesador.setCriticasPermitidasAlMomento(procesador.getCriticasPermitidasAlMomento() - 1); //preguntar a profesor si esta bien
		}
        return procesador.getCriticasPermitidasAlMomento() >= 0;
    }

	private LinkedList<Procesador> clonarProcesadores(Map<String, Procesador> procesadores) {
		LinkedList<Procesador> clon = new LinkedList<>();
		for (Procesador procesador : procesadores.values()) {
			clon.add(procesador.clonar());
		}
		return clon;
	}


	private int getTiempoMaximo(Collection<Procesador> solucionActual, Integer tiempoMaxNoRefrigerado) {
		int tiempoMax = 0;
		for (Procesador procesador : solucionActual) {
			tiempoMax = Math.max(tiempoMax, procesador.getTiempoTotalEjecucion(tiempoMaxNoRefrigerado)); //La función Math.max toma dos argumentos y devuelve el mayor de los dos.
		}
		return tiempoMax;
	}

}
