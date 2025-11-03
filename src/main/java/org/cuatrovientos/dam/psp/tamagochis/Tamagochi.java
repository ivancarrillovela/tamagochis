package org.cuatrovientos.dam.psp.tamagochis;

import java.util.Random;
import java.util.Scanner;

public class Tamagochi implements Runnable {

	public enum Estado {
		COMIENDO, JUGANDO, DUCHANDOSE, ESPERANDO,
	}

	private final int TIEMPO_MAX_DE_VIDA = 1000 * 60 * 5;
	private final int TIEMPO_SUCIEDAD_INTERMEDIA = 1000 * 20 * 5;
	private final int TIEMPO_SUCIEDAD_MAXIMA = 1000 * 20 * 10;
	private final int MAX_TIEMPO_PARA_COMER = 120;
	private final int NUMERO_MAX_PARA_JUGAR = 10;

	private Random rnd = new Random();
	private String nombre;
	private long tiempoNacimiento;
	private long tiempoSuciedad;
	private Estado estadoActual = Estado.ESPERANDO;
	boolean avisoSuciedad = false;
	private boolean estaVivo = true;

	public Tamagochi(String nombre) {

		this.nombre = nombre;

	}

	@Override
	public void run() {

		tiempoNacimiento = System.currentTimeMillis();
		tiempoSuciedad = tiempoNacimiento;

		while (estaVivo) {

			comprobarSuciedad();

			comprobarTiempoDeVida();

		}

	}

	private void comprobarSuciedad() {

		long tiempoTranscurrido = System.currentTimeMillis() - tiempoSuciedad;
		

		if (tiempoTranscurrido >= TIEMPO_SUCIEDAD_MAXIMA) {

			System.out.println("Huele como a muerto...");

			matarlo();

			return;

		}

		if (tiempoTranscurrido >= TIEMPO_SUCIEDAD_INTERMEDIA && !avisoSuciedad) {
			
			avisoSuciedad = true;
			System.out.println("¡" + nombre + " esta empezando a estar muy sucio!");

		}

	}

	private void comprobarTiempoDeVida() {

		long tiempoVivido = System.currentTimeMillis() - tiempoNacimiento;

		if (tiempoVivido >= TIEMPO_MAX_DE_VIDA) {

			System.out.println("Parece que " + nombre + " ya es un anciano");
			matarlo();

		}
	}

	public void darDeComer() {

		if (comprobarSiEstaOcupado()) {

			return;

		}

		try {

			int tiempoEnComer = rnd.nextInt(MAX_TIEMPO_PARA_COMER);
			estadoActual = Estado.COMIENDO;

			System.out.println("¡" + nombre + " ha empezado a comer!");
			Thread.sleep(tiempoEnComer);
			System.out.println("¡" + nombre + " ha terminado de comer!");

		} catch (InterruptedException e) {

			System.out.println(nombre + " ha sido interrumpido mientras comía...");

		} finally {

			estadoActual = Estado.ESPERANDO;

		}

	}

	public void jugar(Scanner scanner) {

		if (estadoActual != Estado.ESPERANDO || estadoActual != Estado.JUGANDO) {

			System.out.println(
					nombre + " ahora mismo esta " + estadoActual.toString() + " ¡Tienes que esperar a que termine!");
			return;

		}

		try {

			estadoActual = Estado.JUGANDO;
			System.out.println("¡" + nombre + " ha empezado a jugar!");

			int num1 = rnd.nextInt(1, NUMERO_MAX_PARA_JUGAR);
			int num2 = rnd.nextInt(NUMERO_MAX_PARA_JUGAR - num1);
			int respuestaCorrecta = num1 + num2;
			int respuestaCuidador;

			boolean esCorrecto = false;

			while (!esCorrecto) {

				System.out.println("¿Cuánto es " + num1 + " + " + num2 + "?: ");
				respuestaCuidador = Integer.parseInt(scanner.nextLine());

				esCorrecto = respuestaCuidador == respuestaCorrecta;

				System.out.println(esCorrecto ? "¡YUHUUUUUU!¡HAS ACERTADO!¡FIN DEL JUEGO!"
						: "¡OH NO!¡HAS FALLADO!¡VUELVE A INTENTARLO!");

			}

		} catch (Exception e) {
			
			System.out.println(nombre + " ha sido interrumpido mientras jugabais...");
			
		} finally {
			
			estadoActual = Estado.ESPERANDO;
			
		}

	}

	public void darUnaDucha() {

		if (comprobarSiEstaOcupado()) {

			return;

		}

		try {

			estadoActual = Estado.DUCHANDOSE;

			System.out.println("¡" + nombre + " se está dando una ducha!");
			Thread.sleep(5);
			tiempoSuciedad = System.currentTimeMillis();
			System.out.println("¡" + nombre + " ha terminado de ducharse!");

		} catch (InterruptedException e) {

			System.out.println(nombre + " ha sido interrumpido mientras se daba una ducha... ¡Vaya vergüenza!");

		} finally {

			estadoActual = Estado.ESPERANDO;

		}

	}

	public void matarlo() {

		if (!estaVivo) {

			System.out.println(nombre + " ya esta muerto ¡Dejalo descansar en paz!");
			return;

		}

		estaVivo = false;

		System.out.println("R.I.P " + nombre + " acaba de morir...");

	}

	private boolean comprobarSiEstaOcupado() {

		if (estadoActual != Estado.ESPERANDO) {

			System.out.println(
					nombre + " ahora mismo esta " + estadoActual.toString() + " ¡Tienes que esperar a que termine!");

			return true;

		}

		return false;

	}

}
