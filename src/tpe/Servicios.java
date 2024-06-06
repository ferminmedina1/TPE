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
	private Solucion mejorSolucion;
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
		tareasEnProcesador = new LinkedList<>(reader.getTareas().values());
		mejorSolucion = new Solucion();
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
     * O(n) donde n es el número total de tareas. Se podra minimizar? Comentarlo
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
	public Solucion asignarTareasBacktracking(Integer tiempoMaxNoRefrigerado) {
		restarEstadosGenerados();
		Solucion resultado = asignarTareasBacktracking(tareasEnProcesador, 0, tiempoMaxNoRefrigerado);
		System.out.println("Cantidad de estados generados en backtracking: " + estadosGenerados);
		return resultado;
	}

	private Solucion asignarTareasBacktracking(LinkedList<Tarea> tareas, int indexTarea, int tiempoMaxNoRefrigerado) {
		estadosGenerados++;
		if (indexTarea >= tareas.size()) {
			int tiempoActual = getTiempoMaximo(procesadores.values(), tiempoMaxNoRefrigerado);
			if (tiempoActual < mejorSolucion.getMejorTiempo() || mejorSolucion.getMejorTiempo() == 0 ) {
				mejorSolucion.clearSolucion();
				mejorSolucion.setMejorTiempo(tiempoActual);
				mejorSolucion.addAll(clonarProcesadores(procesadores));
			}
			return mejorSolucion;
		}

		Tarea tarea = tareas.get(indexTarea);

		for (Procesador procesador : procesadores.values()) {
			if (puedeAsignarTarea(procesador, tarea, tiempoMaxNoRefrigerado)) {
				procesador.addTarea(tarea);
				asignarTareasBacktracking(tareas, indexTarea + 1,tiempoMaxNoRefrigerado);
				procesador.deleteTarea(tarea);
			}
		}
		return mejorSolucion;
	}


	//Parte 2 - Greedy Complejidad Polinomica(Falta complejidad computacional) y explicacion de la estrategia
	public Solucion asignarTareasGreedy(Integer tiempoMaxNoRefrigerado, Integer indice) {
		restarEstadosGenerados();
		mejorSolucion.clearSolucion();

		int tiempoMaximoEjecucion = 0;

		for (Tarea tarea : tareas.values()) {
			Procesador mejorProcesador = null;
			int menorTiempoEjecucion = 0;

			for (Procesador procesador : procesadores.values()) {
				if (puedeAsignarTarea(procesador, tarea, tiempoMaxNoRefrigerado)) {
					procesador.addTarea(tarea);
					int tiempoEjecucion = procesador.getTiempoTotalEjecucion(tiempoMaxNoRefrigerado);
					if (tiempoEjecucion < menorTiempoEjecucion || menorTiempoEjecucion == 0) {
						mejorProcesador = procesador;
						menorTiempoEjecucion = tiempoEjecucion;
					}
					procesador.deleteTarea(tarea);
				}
				estadosGenerados++;
			}

			if (mejorProcesador != null) {
				mejorProcesador.addTarea(tarea);
				if (tarea.isCritica()) {
					mejorProcesador.setCriticasPermitidasAlMomento(mejorProcesador.getCriticasPermitidasAlMomento() - 1);
				}
			}
			if(!mejorSolucion.getSolucion().contains(mejorProcesador)){
				tiempoMaximoEjecucion = Math.max(tiempoMaximoEjecucion, menorTiempoEjecucion);
				mejorSolucion.setMejorTiempo(tiempoMaximoEjecucion);
				mejorSolucion.addProcesador(mejorProcesador);
			}
		}
		System.out.println("Cantidad de candidatos considerados en greedy: " + tareas.size());
		System.out.println("Cantidad de estados generados en greedy " +estadosGenerados);

		return mejorSolucion;
	}

	//Metodos auxiliares

	public void restarEstadosGenerados() {
		this.estadosGenerados = 0;
	}


	private boolean puedeAsignarTarea(Procesador procesador, Tarea tarea, Integer tiempoMaxNoRefrigerado) {
		// Restriccion 1
		if (!procesador.isRefrigerado() && procesador.getTiempoTotalEjecucion(tiempoMaxNoRefrigerado) + tarea.getTiempoEjecucion() > tiempoMaxNoRefrigerado) { // esta bien esta poda?
			return false;
		}
		// Restriccion 2
		if (tarea.isCritica() && procesador.getCriticasPermitidasAlMomento() <= 0) {
			procesador.setCriticasPermitidasAlMomento(procesador.getCriticasPermitidasAlMomento() - 1); //preguntar a profesor si esta bien tener esa constante o es mejor ir llevandola a traves de los adds.
			return false;
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
