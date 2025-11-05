package org.cuatrovientos.dam.psp.tamagochis;

import java.util.HashMap;
import java.util.Scanner;

public class Cuidador {

	private static Scanner scanner = new Scanner(System.in);
	private static HashMap<Tamagochi, Thread> guarderia = new HashMap<>();
	private static final String[] NOMBRES_TAMAGOCHIS = { "Pukuku", "Willy", "Pajaroto" };

	public static void main(String[] args) {

		// Inicializa y arranca los hilos de los Tamagochis
		crearTamagochisIniciales();

		System.out.println("¡Bienvenidos a la Guardería Tamagochi!");

		// Bucle principal del menú
		boolean salir = false; // Booleano para controlar la salida del bucle

		while (!salir) {

			// Si no queda ninguno vivo, se fuerza la salida del bucle
			if (!hayAlgunTamagochiVivo()) {
				salir = true;
				continue; // Salta al final del bucle (y al ser salir == true terminará)
			}

			// Mostrar menú y pedir opción
			mostrarMenu();

			System.out.print("¿Qué quieres hacer?: ");
			int opcion;

			// Validación de entrada (debe ser un número)
			try {
				opcion = Integer.parseInt(scanner.nextLine());

			} catch (NumberFormatException e) {
				System.out.println("Debes introducir un número.");
				continue; // Vuelve al inicio del bucle
			}

			// Manejo de la opción para salir
			if (opcion == 5) {
				salir = true;
				System.out.println("¡Adiós! La guardería está cerrando...");

				// Interrumpir todos los hilos
				// Esto hará que los 'Thread.sleep()' en el método run() de cada Tamagochi
				// lancen una excepción permitiendo que los hilos terminen
				// limpiamente.
				for (Thread hilo : guarderia.values()) {
					hilo.interrupt();
				}
				continue; // Salta al final del bucle para salir
			}

			// Pedir nombre para las opciones 1-4
			System.out.print("¿Sobre qué Tamagochi?: ");
			String nombre = scanner.nextLine();
			Tamagochi tamagochi = buscarTamagochi(nombre); // Busca el objeto Tamagochi

			// Validación del nombre
			if (tamagochi == null) {
				System.out.println("No se ha encontrado a " + nombre + " en la guardería.");
				continue; // Vuelve al inicio del bucle
			}

			// Ejecutar la acción seleccionada (Switch para opciones 1-4)
			switch (opcion) {
			case 1:
				tamagochi.comer(); // Llama al método público de la instancia Tamagochi
				break;
			case 2:
				tamagochi.jugar(scanner); // Pasa el scanner para que el método pueda leer la respuesta
				break;
			case 3:
				tamagochi.duchar(); // Llama al método público de la instancia Tamagochi
				break;
			case 4:
				tamagochi.matar(); // Llama al método público de la instancia Tamagochi
				break;
			default:
				System.out.println("Opción no válida.");
			}

		}
		scanner.close(); // Cuando salga del bucle cerramos el escaner
	}

	/**
	 * Crea los Tamagochis iniciales al comienzo del programa. Itera sobre una lista
	 * de nombres (NOMBRES_TAMAGOCHIS), crea un objeto Tamagochi y un Hilo (Thread)
	 * para cada uno. Almacena la pareja (Tamagochi, Hilo) en el mapa 'guarderia' y
	 * arranca el hilo, lo que llama al método run() del Tamagochi.
	 */
	private static void crearTamagochisIniciales() {
		// Itera sobre la lista de nombres predefinida
		for (String n : NOMBRES_TAMAGOCHIS) {
			Tamagochi tamagochi = new Tamagochi(n); // Crea el objeto Tamagochi (que es un Runnable)
			Thread hilo = new Thread(tamagochi); // Crea un Hilo y le asigna el Tamagochi como tarea
			guarderia.put(tamagochi, hilo); // Almacena el Tamagochi (clave) y su Hilo (valor) para gestionarlo después
			hilo.start(); // Inicia el hilo. Esto hace que se ejecute el método run() del Tamagochi.
		}
	}

	/**
	 * Muestra el menú principal de acciones que el usuario (Cuidador) puede
	 * realizar. Después de mostrar las opciones, llama a mostrarTamagochisVivos()
	 * para que el usuario vea el estado actual de la guardería.
	 */
	private static void mostrarMenu() {
		System.out.println("------ GUARDERÍA TAMAGOCHI ------");
		System.out.println("1. Dar de comer");
		System.out.println("2. Jugar");
		System.out.println("3. Duchar");
		System.out.println("4. Matar");
		System.out.println("5. Salir");
		System.out.println("---------------------------------");
		mostrarTamagochisVivos(); // Muestra los tamagochis que siguen vivos
		System.out.println("---------------------------------");
	}

	/**
	 * Muestra por consola los nombres de todos los Tamagochis que están vivos.
	 * Itera sobre las claves (objetos Tamagochi) del mapa 'guarderia'.
	 */
	private static void mostrarTamagochisVivos() {
		// Comprobación inicial por si el mapa estuviera vacío
		if (guarderia.isEmpty()) {
			System.out.println("> No hay tamagochis en la guardería");
			return;
		}

		// String para construir la lista de nombres
		String strTamagochis = "";

		// Itera sobre el conjunto de claves (los objetos Tamagochi)
		for (Tamagochi t : guarderia.keySet()) {
			// Pregunta a cada objeto Tamagochi si sigue vivo
			if (t.getEstaVivo()) {
				// Si está vivo, lo añade a la cadena
				strTamagochis += "> " + t.getNombre() + "  ";
			}
		}

		// Operador ternario:
		// Si la cadena no está vacía (alguien vive), la imprime.
		// Si está vacía (nadie vive) imprime un mensaje por defecto.
		System.out.println(strTamagochis != "" ? strTamagochis : "> No hay tamagochis vivos");
	}

	/**
	 * Busca un Tamagochi en la guardería por su nombre.
	 * 
	 * @param nombreTamagochi El nombre (String) del Tamagochi a buscar.
	 * @return El objeto Tamagochi si se encuentra, o null si no existe.
	 */
	private static Tamagochi buscarTamagochi(String nombreTamagochi) {
		// Itera sobre las claves (objetos Tamagochi) del mapa
		for (Tamagochi t : guarderia.keySet()) {
			// Compara el nombre del Tamagochi actual con el buscado
			if (t.getNombre().equals(nombreTamagochi)) {
				return t; // Devuelve el objeto Tamagochi si lo encuentra
			}
		}
		return null; // Devuelve null si el bucle termina sin encontrarlo
	}

	/**
	 * Comprueba si queda al menos un Tamagochi vivo en la guardería. Este método es
	 * usado para saber si el bucle principal del juego debe continuar.
	 * 
	 * @return true si al menos uno está vivo, false si todos han muerto.
	 */
	private static boolean hayAlgunTamagochiVivo() {
		// Itera sobre los Tamagochis
		for (Tamagochi t : guarderia.keySet()) {
			// Comprueba el estado de cada uno
			if (t.getEstaVivo() == true) {
				return true; // Si encuentra uno vivo devuelve true
			}
		}
		// Si el bucle termina significa que no encontró a ninguno vivo
		System.out.println("¡NO QUEDAN TAMAGOCHIS VIVOS!");
		System.out.println("Se cierra la guardería...");
		return false; // Devuelve false
	}

}