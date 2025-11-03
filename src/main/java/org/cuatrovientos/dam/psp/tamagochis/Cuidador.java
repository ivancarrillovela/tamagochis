package org.cuatrovientos.dam.psp.tamagochis;

import java.util.HashMap;
import java.util.Scanner;

public class Cuidador {

	private static Scanner scanner = new Scanner(System.in);
	private static HashMap<Tamagochi, Thread> guarderia = new HashMap<>();
	private static final String[] NOMBRES_TAMAGOCHIS = { "Pukuku", "Willy", "Pajaroto" };

	public static void main(String[] args) {

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

			System.out.println("> No hay Tamagochis en la guardería");
			return;

		}
		String strTamagochis = "";

		for (Tamagochi t : guarderia.keySet()) {

			if (t.getEstaVivo()) {

				strTamagochis += "> " + t.getNombre() + "  ";

			}

		}

		System.out.println(strTamagochis != "" ? strTamagochis : "> No hay Tamagochis vivos actualmente");

	}

}