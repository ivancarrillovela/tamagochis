package org.cuatrovientos.dam.psp.tamagochis;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class Cuidador
{
	
	private static Scanner console = new Scanner(System.in);
	
    public static void main( String[] args )
    {	
        System.out.println( "Hello World!" );
    }
    
    private static void jugarConTamagochi(Tamagochi tamagochi) {
    	
    	boolean esCorrecto = false;
    	
    	while (!esCorrecto) {
    		
    		tamagochi.hacerPreguntaJuego();
    		    		
    		int respuesta = Integer.parseInt(console.nextLine());
    		
    		esCorrecto = tamagochi.comprobarRespuestaJuego(respuesta);
    		
    		System.out.println(esCorrecto ? "¡YUHUUUUUU!¡HAS ACERTADO!¡FIN DEL JUEGO!" : "¡OH NO!¡HAS FALLADO!");
    		
    	}
    	
    }
}
