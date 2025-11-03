package org.cuatrovientos.dam.psp.tamagochis;

import java.util.Random;

public class Tamagochi implements Runnable {
	
	public enum Estado{
		COMIENTO,
		JUGANDO,
		DUCHANDOSE,
		ESPERANDO,
	}
	
	private Random rnd = new Random();
	private String nombre;
	private long horaNacimiento = System.currentTimeMillis();
	private Estado estadoActual = Estado.ESPERANDO;
	private boolean estaVivo = true;
	
	
	public Tamagochi(String nombre) {
		super();
		this.nombre = nombre;
	}

	@Override
	public void run() {

		
	}

}
