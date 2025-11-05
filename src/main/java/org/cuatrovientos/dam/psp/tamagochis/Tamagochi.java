package org.cuatrovientos.dam.psp.tamagochis;

import java.util.Random;
import java.util.Scanner;

/**
 * Clase que representa un Tamagochi. Implementa Runnable para que cada
 * Tamagochi se ejecute en su propio hilo simulando su vida de forma
 * concurrente.
 */
public class Tamagochi implements Runnable {

	/**
	 * Enumeración de los posibles estados en los que se puede encontrar el
	 * Tamagochi.
	 */
	public enum Estado {
		COMIENDO, JUGANDO, DUCHANDOSE, ESPERANDO,
	}

	// --- CONSTANTES DE TIEMPO ---
	private final int UN_SEG_EN_MILIS = 1000; // 1 segundo en milisegundos
	private final int TIEMPO_DE_VIDA = UN_SEG_EN_MILIS * 60 * 5; // 5 minutos de vida total
	private final int TIEMPO_SUCIEDAD_INTERMEDIA = UN_SEG_EN_MILIS * 20 * 5; // 20 segundos * 5 puntos para el primer
																				// aviso de suciedad
	private final int TIEMPO_SUCIEDAD_MAXIMA = UN_SEG_EN_MILIS * 20 * 10; // 20 segundos * 10 puntos para morir de
																			// suciedad
	private final int MAX_TIEMPO_PARA_COMER = UN_SEG_EN_MILIS * 30; // Tiempo máximo que puede tardar en comer (30 seg)
	private final int DURACION_DUCHA = UN_SEG_EN_MILIS * 5; // Tiempo fijo que tarda en ducharse (5 seg)
	private final int NUMERO_MAX_PARA_JUGAR = 10; // Límite para los números aleatorios del juego

	// --- ATRIBUTOS DE INSTANCIA ---
	private Random rnd = new Random(); // Generador de números aleatorios (para comer, jugar)
	private String nombre; // Nombre del Tamagochi
	private long tiempoNacimiento; // Marca de tiempo (timestamp) de cuándo nació (se ejecuta run())
	private long tiempoSuciedad; // Marca de tiempo (timestamp) para controlar la suciedad
	private Estado estadoActual = Estado.ESPERANDO; // Estado actual del Tamagochi (por defecto esperando)
	boolean avisoSuciedad = false; // Booleano para controlar que el aviso de suciedad solo salga una vez
	private boolean estaVivo = true; // Booleano para controlar el ciclo de vida (el bucle run())

	/**
	 * Constructor del Tamagochi.
	 * 
	 * @param nombre El nombre que tendrá el Tamagochi.
	 */
	public Tamagochi(String nombre) {
		this.nombre = nombre.toUpperCase();
	}

	/**
	 * Devuelve el nombre del Tamagochi.
	 * 
	 * @return el nombre.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Devuelve si el Tamagochi sigue vivo.
	 * 
	 * @return true si está vivo, false si ha muerto.
	 */
	public boolean getEstaVivo() {
		return estaVivo;
	}

	/**
	 * Método principal del hilo (Thread). Contiene el ciclo de vida del Tamagochi.
	 * Se ejecuta una vez por segundo (Thread.sleep) mientras esté vivo.
	 */
	@Override
	public void run() {

		// Al nacer se establecen las marcas de tiempo iniciales para controlar su
		// timempo de vida y
		// el tiempo que puede estar sin ducharse (controlaremos si mandar aviso o
		// matarlo)
		tiempoNacimiento = System.currentTimeMillis();
		tiempoSuciedad = System.currentTimeMillis();

		try {
			// Bucle principal de vida: se ejecuta mientras estaVivo sea true
			while (estaVivo) {

				// "Latido" del Tamagochi: espera 1 segundo antes de cada ciclo para que se
				// ejecute de forma lógica
				Thread.sleep(UN_SEG_EN_MILIS);

				// Comprueba si el estado ha cambiado (p.ej., el Cuidador lo mandó a comer)
				// Si es así, ejecuta la acción interna correspondiente
				if (estadoActual == Estado.COMIENDO) {
					this.darDeComer(); // Ejecuta la lógica de comer (que tiene su propio sleep)
				}

				if (estadoActual == Estado.DUCHANDOSE) {
					this.darUnaDucha(); // Ejecuta la lógica de ducharse (que tiene su propio sleep)
				}

				// En cada ciclo (cada segundo), comprueba su estado de higiene y edad
				comprobarSuciedad();
				comprobarTiempoDeVida();

			}
			// Esta excepción nos interesa para que salte por que el Cuidador ha usado
			// .interrupt() para salir del programa.
			// También saltará con cualquier tipo de error inesperado
		} catch (Exception e) {
			System.out.println(nombre + " se a ido a dormir...");
		} finally {
			// Pase lo que pase (muerte natural, error o interrupción),
			// antes de salir nos aseguramos de que el Tamagochi quede marcado como muerto.
			estaVivo = false;
		}

	}

	/**
	 * Comprueba el tiempo transcurrido desde la última ducha. Si pasa el tiempo
	 * máximo, muere. Si pasa el tiempo intermedio, avisa (solo una vez).
	 */
	private void comprobarSuciedad() {

		long tiempoTranscurrido = System.currentTimeMillis() - tiempoSuciedad;

		// Comprobación de muerte por suciedad
		if (tiempoTranscurrido >= TIEMPO_SUCIEDAD_MAXIMA) {
			System.out.println("\n¡HUELE MUY MAL! Como a muerto...");
			matar(); // Llama al método público para gestionar la muerte
			return; // Sale del método
		}

		// Comprobación de aviso por suciedad (y que no se haya avisado ya)
		if (tiempoTranscurrido >= TIEMPO_SUCIEDAD_INTERMEDIA && !avisoSuciedad) {
			avisoSuciedad = true; // Marcamos que ya hemos avisado
			System.out.println("\n¡" + nombre + " esta empezando a estar muy sucio!");
		}

	}

	/**
	 * Comprueba el tiempo transcurrido desde el nacimiento. Si supera el tiempo de
	 * vida máximo muere de viejo.
	 */
	private void comprobarTiempoDeVida() {

		long tiempoVivido = System.currentTimeMillis() - tiempoNacimiento;

		if (tiempoVivido >= TIEMPO_DE_VIDA) {
			System.out.println("\nParece que " + nombre + " ya es un anciano");
			matar(); // Llama al método público para gestionar la muerte
		}
	}

	// --- MÉTODOS PÚBLICOS DE ACCIÓN (Llamados por el Cuidador) ---

	/**
	 * Método PÚBLICO. Es la ORDEN de comer. El Cuidador (hilo principal) llama a
	 * este método. Solo cambia el estado en el método run() y desde ahi se
	 * encargará de ejecutar la acción.
	 */
	public void comer() {
		// Comprueba si está vivo y no está ocupado
		if (comprobarSiEstaEsperando() && comprobarSiEstaVivo()) {
			estadoActual = Estado.COMIENDO; // Cambia el estado
		}
	}

	/**
	 * Método PÚBLICO. Es la ORDEN de duchar. El Cuidador (hilo principal) llama a
	 * este método. Solo cambia el estado; el método run() se encargará de ejecutar
	 * la acción.
	 */
	public void duchar() {
		// Comprueba si está vivo y no está ocupado
		if (comprobarSiEstaEsperando() && comprobarSiEstaVivo()) {
			estadoActual = Estado.DUCHANDOSE; // Cambia el estado
		}
	}

	/**
	 * Método PÚBLICO. Es la ORDEN de jugar. A diferencia de comer/duchar, esta
	 * acción se ejecuta INMEDIATAMENTE en el hilo del Cuidador, bloqueando al
	 * Cuidador hasta que termine el juego.
	 * 
	 * @param scanner El Scanner del hilo principal para leer la respuesta del
	 *                usuario.
	 */
	public void jugar(Scanner scanner) {

		// Comprueba si está vivo y no está ocupado
		if (comprobarSiEstaEsperando() && comprobarSiEstaVivo()) {
			try {

				estadoActual = Estado.JUGANDO; // Pone el estado en JUGANDO (bloquea otras acciones)
				System.out.println("¡" + nombre + " ha empezado a jugar!");

				// Lógica del minijuego de suma
				int num1 = rnd.nextInt(1, NUMERO_MAX_PARA_JUGAR - 1);
				int num2 = rnd.nextInt(1, NUMERO_MAX_PARA_JUGAR - num1);
				int respuestaCorrecta = num1 + num2;
				int respuestaCuidador;

				boolean esCorrecto = false;

				// Bucle hasta que el Cuidador acierte
				while (!esCorrecto) {
					System.out.println("¿Cuánto es " + num1 + " + " + num2 + "?: ");
					respuestaCuidador = Integer.parseInt(scanner.nextLine()); // Lee la entrada

					esCorrecto = respuestaCuidador == respuestaCorrecta;

					System.out.println(esCorrecto ? "¡YUHUUUUUU!¡HAS ACERTADO!¡FIN DEL JUEGO!"
							: "¡OH NO!¡HAS FALLADO!¡VUELVE A INTENTARLO!");
				}

			} catch (Exception e) {
				// Captura errores, por ejemplo, si se interrumpe el hilo principal
				System.out.println(nombre + " ha sido interrumpido mientras jugabais...");

			} finally {
				// Al terminar el juego vuelve al estado de espera
				estadoActual = Estado.ESPERANDO;
			}
		}
	}

	/**
	 * Método PÚBLICO. Es la ORDEN de matar al Tamagochi. Establece la bandera
	 * estaVivo a false, lo que detendrá el bucle run().
	 */
	public void matar() {
		// Si está vivo lo mata
		if (comprobarSiEstaVivo()) {
			estaVivo = false;
			System.out.println("R.I.P " + nombre + " acaba de morir...");
		}
	}

	// --- MÉTODOS PRIVADOS DE ACCIÓN (Llamados por el método run() u otros métodos)
	// ---

	/**
	 * Método PRIVADO. Ejecuta la lógica de comer. Es llamado por el propio hilo del
	 * Tamagochi (método run) cuando el estado es COMIENDO. Simula el tiempo que
	 * tarda en comer.
	 */
	private void darDeComer() {

		try {
			// Calcula un tiempo aleatorio para comer
			int tiempoEnComer = rnd.nextInt(MAX_TIEMPO_PARA_COMER);

			// El estado ya es COMIENDO (lo puso el método run)
			System.out.println("\n¡" + nombre + " ha empezado a comer!");
			Thread.sleep(tiempoEnComer); // Simula el tiempo comiendo
			System.out.println("\n¡" + nombre + " ha terminado de comer!");

		} catch (InterruptedException e) {
			// Si el hilo es interrumpido mientras come
			System.out.println(nombre + " ha sido interrumpido mientras comía...");

		} finally {
			// Al terminar de comer (o si es interrumpido), vuelve a ESPERANDO
			estadoActual = Estado.ESPERANDO;
		}

	}

	/**
	 * Método PRIVADO. Ejecuta la lógica de la ducha. Es llamado por el propio hilo
	 * del Tamagochi (método run) cuando el estado es DUCHANDOSE. Simula el tiempo
	 * que tarda en ducharse y resetea el contador de suciedad.
	 */
	private void darUnaDucha() {

		try {
			// El estado ya es DUCHANDOSE
			System.out.println("\n¡" + nombre + " se está dando una ducha!");
			Thread.sleep(DURACION_DUCHA); // Simula el tiempo de la ducha

			// Resetea el contador de suciedad a la hora actual
			tiempoSuciedad = System.currentTimeMillis();
			avisoSuciedad = false; // Resetea el booleano de aviso

			System.out.println("\n¡" + nombre + " ha terminado de ducharse!");

		} catch (InterruptedException e) {
			// Si el hilo es interrumpido mientras se ducha
			System.out.println(nombre + " ha sido interrumpido mientras se daba una ducha... ¡Vaya vergüenza!");
		} finally {
			// Al terminar (o ser interrumpido), vuelve a ESPERANDO
			estadoActual = Estado.ESPERANDO;
		}

	}

	// --- MÉTODOS DE COMPROBACIÓN ---

	/**
	 * Comprueba si el Tamagochi está en estado ESPERANDO. Si no lo está, informa al
	 * usuario.
	 * 
	 * @return true si está en ESPERANDO, false si está ocupado.
	 */
	private boolean comprobarSiEstaEsperando() {

		if (estadoActual == Estado.ESPERANDO) {
			return true;
		}
		// Si está ocupado, avisa
		System.out.println(
				nombre + " ahora mismo esta " + estadoActual.toString() + " ¡Tienes que esperar a que termine!");
		return false;
	}

	/**
	 * Comprueba si el Tamagochi está vivo. Si no lo está informa al usuario.
	 * 
	 * @return true si está vivo, false si está muerto.
	 */
	private boolean comprobarSiEstaVivo() {
		if (estaVivo) {
			return true;
		}
		// Si está muerto, avisa
		System.out.println(nombre + " ya esta muerto ¡Dejalo descansar en paz!");
		return false;
	}

}