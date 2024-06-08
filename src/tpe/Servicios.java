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

	//Parte 2 - Backtracking - Exponencial O(a^x)
	/*
	* a: cantidad de procesadores que tenés para elegir en cada paso.
	* x: cantidad total de tareas que deben ser asignadas.
	*
	* En esta tecnica se intentan buscar todas las posibles maneras de asignar tareas
	* a procesadores. Cada vez que se asigna una tarea se avanza recursivamente
	* hasta que todas las tareas hayan sido asignadas (caso base). Tambien hay una poda,
	* la cual controla que se siga buscando una mejor solucion siempre y cuando el mayor tiempo
	* de ejecucion hasta el moemnto sea menor al de la mejor solucion encontrada.
	* */
	public Solucion asignarTareasBacktracking(Integer tiempoMaxNoRefrigerado) {
		vaciarEstados();
		Solucion resultado = asignarTareasBacktracking(tareasEnProcesador, 0, tiempoMaxNoRefrigerado);
		System.out.println("Cantidad de estados generados en backtracking: " + estadosGenerados);
		return resultado;
	}

	private Solucion asignarTareasBacktracking(LinkedList<Tarea> tareas, int indexTarea, int tiempoMaxNoRefrigerado) {
		estadosGenerados++;
		if (indexTarea >= tareas.size()) {
			mejorSolucion.clearSolucion();
			mejorSolucion.setMayorTiempo(getTiempoMaximo(procesadores.values()));
			mejorSolucion.addAll(clonarProcesadores(procesadores));
			return mejorSolucion;
		}

		Tarea tarea = tareas.get(indexTarea);

		for (Procesador procesador : procesadores.values()) {
			if (puedeAsignarTarea(procesador, tarea, tiempoMaxNoRefrigerado)) {
				procesador.addTarea(tarea);
				//poda
				if (getTiempoMaximo(procesadores.values()) < mejorSolucion.getMejorTiempo() || mejorSolucion.getMejorTiempo() == 0 )
					asignarTareasBacktracking(tareas, indexTarea + 1,tiempoMaxNoRefrigerado);
				procesador.deleteTarea(tarea);
			}
		}
		return mejorSolucion;
	}


	//Parte 2 - Greedy Complejidad Polinomica - O(t×p)
	/*
	* Este método utiliza la técnica greedy para asignar tareas a procesadores de manera óptima.
	* En cada paso, intenta asignar la tarea actual al mejor procesador disponible (el de menor tiempo de ejecucion).
	* El método sigue avanzando hasta que se asignan todas las tareas.
	* */
	public Solucion asignarTareasGreedy(Integer tiempoMaxNoRefrigerado) {
		vaciarEstados();
		mejorSolucion.clearSolucion();
		int tiempoMaximoEjecucion = 0;

		for (Tarea tarea : tareas.values()) {
			Procesador mejorProcesador = encontrarMejorProcesador(tarea,tiempoMaxNoRefrigerado);
			if (mejorProcesador != null) {
				mejorProcesador.addTarea(tarea);
				tiempoMaximoEjecucion = Math.max(tiempoMaximoEjecucion, mejorProcesador.getTiempoTotalEjecucion());
				if (tarea.isCritica()) {
					mejorProcesador.setCriticasPermitidasAlMomento(mejorProcesador.getCriticasPermitidasAlMomento() - 1);
				}
			}
			mejorSolucion.addProcesador(mejorProcesador);
			mejorSolucion.setMayorTiempo(tiempoMaximoEjecucion);
		}
		System.out.println("Cantidad de candidatos considerados en greedy: " + tareas.size() * procesadores.size());
		return mejorSolucion;
	}

	//Metodos auxiliares

	private Procesador encontrarMejorProcesador(Tarea tarea, Integer tiempoMaxNoRefrigerado) {
		int menorTiempoEjecucion = Integer.MAX_VALUE;
		Procesador mejorProcesador = null;
		for (Procesador procesador : procesadores.values()) {
			if (puedeAsignarTarea(procesador, tarea, tiempoMaxNoRefrigerado)) {
				int tiempoEjecucion = procesador.getTiempoTotalEjecucion();
				if (tiempoEjecucion < menorTiempoEjecucion) {
					mejorProcesador = procesador;
					menorTiempoEjecucion = tiempoEjecucion;
				}
			}
		}
		return mejorProcesador;
	}

	public void vaciarEstados() {
		this.estadosGenerados = 0;
	}


	private boolean puedeAsignarTarea(Procesador procesador, Tarea tarea, Integer tiempoMaxNoRefrigerado) {
		// Restriccion 1
		if (!procesador.isRefrigerado() && procesador.getTiempoTotalEjecucion() + tarea.getTiempoEjecucion() > tiempoMaxNoRefrigerado) {
			return false;
		}
		// Restriccion 2
		if (tarea.isCritica() && procesador.getCriticasPermitidasAlMomento() <= 0) {
			procesador.setCriticasPermitidasAlMomento(procesador.getCriticasPermitidasAlMomento() - 1);
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


	private int getTiempoMaximo(Collection<Procesador> solucionActual) {
		int tiempoMax = 0;
		for (Procesador procesador : solucionActual) {
			//buscar el procesador con el mayor tiempo de ejecucion
			tiempoMax = Math.max(tiempoMax, procesador.getTiempoTotalEjecucion());
		}
		return tiempoMax;
	}
}
