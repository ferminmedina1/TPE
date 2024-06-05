package tpe;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String args[]) {
		Servicios servicios = new Servicios("./src/tpe/datasets/Procesadores.csv", "./src/tpe/datasets/Tareas.csv");
		Scanner scanner = new Scanner(System.in);
		/*
		// Probar Servicio 1
		System.out.print("Ingrese el ID de la tarea para obtener su información: ");
		String tareaID = scanner.nextLine().toUpperCase();
		Tarea tarea = servicios.servicio1(tareaID);
		System.out.println("Información de la tarea: " + tarea);

		// Probar Servicio 2
		System.out.print("¿Desea ver todas las tareas críticas? (true/false): ");
		boolean esCritica = scanner.nextBoolean();
		List<Tarea> tareasCriticas = servicios.servicio2(esCritica);
		System.out.println("Listado de tareas: " + tareasCriticas);

		// Probar Servicio 3
		System.out.print("Ingrese el nivel de prioridad inferior: ");
		int prioridadInferior = scanner.nextInt();
		System.out.print("Ingrese el nivel de prioridad superior: ");
		int prioridadSuperior = scanner.nextInt();
		List<Tarea> tareasPorPrioridad = servicios.servicio3(prioridadInferior, prioridadSuperior);
		System.out.println("Listado de tareas entre niveles de prioridad: " + tareasPorPrioridad);

		// Probar asignación de tareas a procesadores con backtracking
		System.out.print("Ingrese el tiempo máximo de ejecución para procesadores no refrigerados (X): ");
		int tiempoMaxNoRefrigerado = scanner.nextInt();

		//Integer = tiempoMaximoDeEjecucion.
		Solucion backtracking = servicios.asignarTareasBacktracking(tiempoMaxNoRefrigerado);
		System.out.println("Asignacion de tareas a procesadores mediante Backtracking. Mejor solución obtenida: " + backtracking);
		*/
		Solucion greedy = servicios.asignarTareasGreedy(50,0);
		System.out.println("Asignacion de tareas a procesadores mediante Greedy. Mejor solución obtenida: " + greedy);
	}
}
