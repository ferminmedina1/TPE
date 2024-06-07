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


	/* Parte 2 - Backtracking - Exponencial O(a^x)

	a: cantidad de procesadores que tenés para elegir en cada paso.
	x: cantidad total de tareas que deben ser asignadas.

	En esta tecnica se intentan todas las posibles maneras de asignar tareas
	a los procesadores. Cada vez que se asigna una tarea se avanza recursivamente
	hasta que todas las tareas hayan sido asignadas. Retrocediendo cuando se encuentra
	con una solución que no cumple con las expectativas y guardando la soluciona si
	es la mas optima al momento (tiempoActual < mejorSolucion.getMejorTiempo())
	*/
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
			mejorSolucion.clearSolucion();
			mejorSolucion.setMayorTiempo(getTiempoMaximo(procesadores.values(), tiempoMaxNoRefrigerado));
			mejorSolucion.addAll(clonarProcesadores(procesadores));
			return mejorSolucion;
		}

		Tarea tarea = tareas.get(indexTarea);

		for (Procesador procesador : procesadores.values()) {
			if (puedeAsignarTarea(procesador, tarea, tiempoMaxNoRefrigerado)) {
				procesador.addTarea(tarea);
				if (getTiempoMaximo(procesadores.values(), tiempoMaxNoRefrigerado) < mejorSolucion.getMejorTiempo() || mejorSolucion.getMejorTiempo() == 0 )
					asignarTareasBacktracking(tareas, indexTarea + 1,tiempoMaxNoRefrigerado);
				procesador.deleteTarea(tarea);
			}
		}
		return mejorSolucion;
	}


	//Parte 2 - Greedy Complejidad Polinomica - O(t×p)
	/*
	* Este método utiliza la técnica greedy para asignar tareas a procesadores de manera óptima.
	* En cada paso, intenta asignar la tarea actual al mejor procesador disponible, evaluando
	* todas las posibles asignaciones. La decisión se toma basándose en cuál procesador minimiza
	* el tiempo total de ejecución. El método sigue avanzando hasta que todas las tareas están
	* asignadas y actualiza la mejor solución encontrada si el tiempo de ejecución actual es menor
	* que el mejor tiempo registrado.
	* */
	public Solucion asignarTareasGreedy(Integer tiempoMaxNoRefrigerado) {
		vaciarEstados();
		mejorSolucion.clearSolucion();
		int tiempoMaximoEjecucion = 0;

		for (Tarea tarea : tareas.values()) {
			Procesador mejorProcesador = encontrarMejorProcesador(tarea,tiempoMaxNoRefrigerado);
			if (mejorProcesador != null) {
				mejorProcesador.addTarea(tarea);
				tiempoMaximoEjecucion = Math.max(tiempoMaximoEjecucion, mejorProcesador.getTiempoTotalEjecucion(tiempoMaxNoRefrigerado));
				if (tarea.isCritica()) {
					mejorProcesador.setCriticasPermitidasAlMomento(mejorProcesador.getCriticasPermitidasAlMomento() - 1);
				}
			}
			if(!mejorSolucion.getSolucion().contains(mejorProcesador)){
				mejorSolucion.addProcesador(mejorProcesador);
			}
			mejorSolucion.setMayorTiempo(tiempoMaximoEjecucion);
		}
		System.out.println("Cantidad de candidatos considerados en greedy: " + tareas.size() * procesadores.size());
		return mejorSolucion;
	}

	private Procesador encontrarMejorProcesador(Tarea tarea, Integer tiempoMaxNoRefrigerado) {
		int menorTiempoEjecucion = Integer.MAX_VALUE;
		Procesador mejorProcesador = null;
		for (Procesador procesador : procesadores.values()) {
			if (puedeAsignarTarea(procesador, tarea, tiempoMaxNoRefrigerado)) {
				int tiempoEjecucion = procesador.getTiempoTotalEjecucion(tiempoMaxNoRefrigerado);
				if (tiempoEjecucion < menorTiempoEjecucion) {
					mejorProcesador = procesador;
					menorTiempoEjecucion = tiempoEjecucion;
				}
			}
		}
		return mejorProcesador;
	}

	//Metodos auxiliares

	public void vaciarEstados() {
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
