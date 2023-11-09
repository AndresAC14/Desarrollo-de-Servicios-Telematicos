package servidor;

import java.io.*;
import java.net.*;
import java.util.*;

import mensaje.Mensaje;

public class Servidor {

	// Tam. maximo de mensaje
	private static final int ECHOMAX = 255;
	private static DatagramSocket socketEnvio;
	private static DatagramSocket socketRecibo;
	private static InetAddress ip;
	private static int puerto;
	// Crear variables para reenvio, timeout, retrasos

	public static void main(String[] args) throws IOException{
		  
		try {
			
			// Crea socket en el que recibirá mensaje
			creaSocketRecibo(3001);

			DatagramPacket recibo = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
			
			// Recibe el servidor y redirige el paquete a la hebra
			// Espera a que llegue el mensaje
			socketRecibo.receive(recibo);

			// Procesa el paquete la hebra, BUCLE y tiempo de espera importante, para que cada hebra muestre lo suyo y ya despues se vera
			// Cuando se recibe un paquete, crea un hilo para procesarlo
			Thread servidorThread = new Thread(new ServidorThread(recibo));
			servidorThread.start();

			System.out.println("Servidor en espera de que termine de procesarse el mensaje....");
			Thread.sleep(1000); // 1 segundo

			// Como hacer para pillar lo que hay que enviar? porque eso estará en la hebra

			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void creaSocketEnvio() throws IOException {
		// Creacion del socket UDP
		socketEnvio = new DatagramSocket();
		
		// Direccion de envio -> Broadcast
		ip = InetAddress.getByName("192.168.167.255");
		
		// Puerto de envio, elegimos el 3000 pero habra que cambiarlo
		puerto = 3000;
	}

	public static void creaSocketRecibo(int puerto) throws IOException {
		// Creacion del socket UDP
		socketRecibo = new DatagramSocket(puerto);
		
		// Direccion de envio -> Broadcast
		//ip = InetAddress.getByName("192.168.167.255");
		
		// Puerto de envio, elegimos el 3000 pero habra que cambiarlo
		//puerto = 3000;
	}

}
