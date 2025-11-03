package org.cuatrovientos.dam.psp.tamagochis;

import java.util.HashMap;
import java.util.Scanner;

public class Cuidador {

	private static Scanner scanner = new Scanner(System.in);
	private static HashMap<Tamagochi, Thread> guarderia = new HashMap<>();
	private static final String[] NOMBRES_TAMAGOCHIS = { "Pukuku", "Willy", "Pajaroto" };

	public static void main(String[] args) {

		crearTamagochisIniciales();

		System.out.println("¡Bienvenidos a la Guardería Tamagochi!");

		boolean salir = false;

		while (!salir) {

			// Mostramos el menú
			mostrarMenu();

			System.out.print("¿Qué quieres hacer?: ");
			int opcion;

			// Usamos try-catch por si el usuario no introduce un número
			try {
				opcion = Integer.parseInt(scanner.nextLine());

			} catch (NumberFormatException e) {
				System.out.println("Error: Debes introducir un número.");
				continue; // Vuelve al inicio del bucle

			}

			if (opcion == 5) {
				salir = true;
				System.out.println("¡Adiós! La guardería está cerrando...");
				for (Thread hilo : guarderia.values()) {
					hilo.interrupt();
				}

				continue;
			}

			// Pedimos el nombre para las demás opciones (1-4)
			System.out.print("¿Sobre qué Tamagochi?: ");
			String nombre = scanner.nextLine();
			Tamagochi tamagochi = buscarTamagochi(nombre);

			if (tamagochi == null) {
				System.out.println("No se ha encontrado a " + nombre + " en la guardería.");
				continue;
			}

			// Ejecutamos la acción
			switch (opcion) {
			case 1:
				tamagochi.darDeComer();
				break;
			case 2:
				tamagochi.jugar(scanner); // Le pasamos el scanner para que pueda leer la respuesta
				break;
			case 3:
				tamagochi.darUnaDucha();
				break;
			case 4:
				tamagochi.matarlo();
				guarderia.remove(tamagochi);
				break;
			default:
				System.out.println("Opción no válida.");
			}

			if (!hayAlgunTamagochiVivo()) {
				salir = true;
			}

		}

		System.out.println("Saliendo del programa...");
		scanner.close();

	}

	private static void crearTamagochisIniciales() {

		for (String n : NOMBRES_TAMAGOCHIS) {
			Tamagochi tamagochi = new Tamagochi(n);
			Thread hilo = new Thread(tamagochi);

			guarderia.put(tamagochi, hilo);
			hilo.start();

		}

	}

	private static void mostrarMenu() {

		System.out.println("\n------ GUARDERÍA TAMAGOCHI ------");
		System.out.println("1. Dar de comer");
		System.out.println("2. Jugar");
		System.out.println("3. Duchar");
		System.out.println("4. Matar");
		System.out.println("5. Salir");
		System.out.println("---------------------------------");
		mostrarTamagochisVivos(); // Mostramos los tamagochis vivos
		System.out.println("---------------------------------");

	}

	private static void mostrarTamagochisVivos() {

		if (guarderia.isEmpty()) {
			System.out.println("> No hay tamagochis en la guardería");
			return;

		}
		String strTamagochis = "";

		for (Tamagochi t : guarderia.keySet()) {
			if (t.getEstaVivo()) {
				strTamagochis += "> " + t.getNombre() + "  ";

			}

		}

		System.out.println(strTamagochis != "" ? strTamagochis : "> No hay tamagochis vivos");
		

	}

	private static Tamagochi buscarTamagochi(String nombreTamagochi) {

		for (Tamagochi t : guarderia.keySet()) {
			if (t.getNombre().equals(nombreTamagochi)) {
				return t;

			}

		}

		return null;

	}

	private static boolean hayAlgunTamagochiVivo() {

		for (Tamagochi t : guarderia.keySet()) {
			if (t.getEstaVivo() == true) {
				return true;

			}

		}
		
		System.out.println("¡NO QUEDAN TAMAGOCHIS VIVOS!");
		return false;

	}

}