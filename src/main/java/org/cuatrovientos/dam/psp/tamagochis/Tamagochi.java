package org.cuatrovientos.dam.psp.tamagochis;

import java.util.Random;

public class Tamagochi implements Runnable {

	public enum Estado {
		COMIENDO, JUGANDO, DUCHANDOSE, ESPERANDO,
	}

	private final int TIEMPO_MAX_DE_VIDA = 5 * 60 * 1000;
	private final int MAX_TIEMPO_PARA_COMER = 120;
	private final int TIEMPO_QUE_TARDA_EN_ENSUCIARSE = 20;
	private final int NUMERO_MAX_PARA_JUGAR = 10;
	private static final int SUCIEDAD_INTERMEDIA = 5;
	private final int SUCIEDAD_MAXIMA = 10;

	private Random rnd = new Random();
	private String nombre;
	private long horaNacimiento;
	private Estado estadoActual = Estado.ESPERANDO;
	private boolean estaVivo = true;
	private int suciedad = 0;

	private int respuestaCorrectaJuego = 0;

	public Tamagochi(String nombre) {
		super();
		this.nombre = nombre;
	}

	@Override
	public void run() {
		
		horaNacimiento = System.currentTimeMillis();

		while (estaVivo) {

			try {

				Thread.sleep(TIEMPO_QUE_TARDA_EN_ENSUCIARSE);

				comprobarSuciedad();
				
				comprobarTiempoDeVida();

			} catch (InterruptedException e) {

				e.printStackTrace();

			}

		}

	}

	private void comprobarTiempoDeVida() {
		
		long tiempoVivido = System.currentTimeMillis() - horaNacimiento;
		
		if (tiempoVivido > TIEMPO_MAX_DE_VIDA) {
			
			System.out.println("Parece que " + nombre + " ya es un anciano");
			matarlo();
			
		}
	}

	private void comprobarSuciedad() {

		if (suciedad == SUCIEDAD_MAXIMA)
			
			System.out.println("Huele como a muerto...");
			matarlo();

		if (suciedad == SUCIEDAD_INTERMEDIA) {

			System.out.println("¡" + nombre + " esta empezando a estar muy sucio!");

		}

	}

	public void darDeComer() {

		if (comprobarSiEstaOcupado())
			return;

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

	public void hacerPreguntaJuego() {

		if (estadoActual != Estado.ESPERANDO || estadoActual != Estado.JUGANDO) {

			System.out.println(
					nombre + " ahora mismo esta " + estadoActual.toString() + " ¡Tienes que esperar a que termine!");

		} else {

			estadoActual = Estado.JUGANDO;
			System.out.println("¡" + nombre + " ha empezado a jugar!");

			int num1 = rnd.nextInt(1, NUMERO_MAX_PARA_JUGAR);
			int num2 = rnd.nextInt(NUMERO_MAX_PARA_JUGAR - num1);
			respuestaCorrectaJuego = num1 + num2;

			System.out.println("¿Cuánto es " + num1 + " + " + num2 + "?: ");

		}

	}

	public boolean comprobarRespuestaJuego(int respuesta) {

		boolean esCorrecto = (respuesta == respuestaCorrectaJuego);

		if (esCorrecto)
			estadoActual = Estado.ESPERANDO;

		return esCorrecto;

	}

	public void darUnaDucha() {

		if (comprobarSiEstaOcupado())
			return;

		try {

			estadoActual = Estado.DUCHANDOSE;

			System.out.println("¡" + nombre + " se está dando una ducha!");
			Thread.sleep(5);
			suciedad = 0;
			System.out.println("¡" + nombre + " ha terminado de ducharse!");

		} catch (InterruptedException e) {

			System.out.println(nombre + " ha sido interrumpido mientras se daba una ducha... ¡Vaya vergüenza!");

		} finally {

			estadoActual = Estado.ESPERANDO;

		}

	}

	public void matarlo() {

		if (comprobarSiEstaOcupado())
			return;

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
