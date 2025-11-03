package org.cuatrovientos.dam.psp.tamagochis;

import java.util.Random;

public class Tamagochi implements Runnable {

	public enum Estado {
		COMIENDO, JUGANDO, DUCHANDOSE, ESPERANDO,
	}

	private Random rnd = new Random();
	private String nombre;
	private long horaNacimiento = System.currentTimeMillis();
	private Estado estadoActual = Estado.ESPERANDO;
	private boolean estaVivo = true;

	private final int MAX_TIEMPO_PARA_COMER = 120;

	public Tamagochi(String nombre) {
		super();
		this.nombre = nombre;
	}

	@Override
	public void run() {

	}

	public synchronized void darDeComer() {

		if (comprobarSiEstaOcupado()) return;

		try {

			int tiempoEnComer = rnd.nextInt(MAX_TIEMPO_PARA_COMER);
			estadoActual = Estado.COMIENDO;

			System.out.println("¡" + nombre + "ha empezado a comer!");
			Thread.sleep(tiempoEnComer);
			System.out.println("¡" + nombre + "ha terminado de comer!");

		} catch (InterruptedException e) {

			System.out.println(nombre + "ha sido interrumpido mientras comía...");

		} finally {

			estadoActual = Estado.ESPERANDO;

		}

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
