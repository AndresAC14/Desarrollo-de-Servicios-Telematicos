package cliente;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

// Código realizado por Andres Amo Caballero

public class Cliente {

	// Tam. maximo de mensaje
	private static final int ECHOMAX = 255;

	// Socket
	private static DatagramSocket socket;
	private static InetAddress ip;
	private static int puerto;
	
	public static void main(String[] args) {
		try {
		
			// Leemos los atributos de consola		
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			// Usuario introduce su identificador en la terminal
			System.out.println("Introducir dni de cliente");
			int dni = Integer.parseInt(stdIn.readLine());
			
			// Usuario introduce su nombre en la terminal
			System.out.println("Introducir nombre de cliente");
			String nombreCliente = stdIn.readLine();
			
			
			// Devuelve la suma de los valores ascii de cada caracter del nombre del cliente
			int valorAscii = 0;
			
			for (int i = 0; i < nombreCliente.length(); i++) {
	            char caracter = nombreCliente.charAt(i);
	            valorAscii += (int) caracter;

	        }
			
			// Creación identificador ( Es una funcion inventada )
			int identificador = (dni / 150) + valorAscii;
			
			System.out.printf("Identificador obtenido por el cliente %s: %d \n", nombreCliente, identificador);
			
			// Codificacion mensaje antes de enviar
			String contenido = ""+identificador;
			byte[] trama = contenido.getBytes();

			// Crea socket
			creaSocket();

			DatagramPacket envio = new DatagramPacket(trama, trama.length, ip, puerto);

			System.out.println("Enviando identificador...");
			socket.send(envio);
			socket.close();

			// Crea socket
			creaSocket();
	
			// Espera a recibir
			DatagramPacket recibo = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
			
			socket.receive(recibo);
	
			// Cerramos para que no haya problemas
			socket.close(); 

			String mensaje = new String(recibo.getData());
			
			// Mostrar mensaje recibido
			System.out.println("Mensaje recibido con contenido: " + mensaje);
			
			String[] partes = mensaje.split(";");
			
			int accesoN = Integer.parseInt(partes[0]);
			String asiento = partes[1];
			
			System.out.printf("Los credenciales proporcionados al usuario son: \n AccesoN: %d \n Asiento: %s \n", accesoN, asiento);
			
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void creaSocket() throws IOException {
		// Direccion de envio -> Broadcast
		ip = InetAddress.getByName("192.168.226.255"); // 192.168.56.255
		
		// Puerto de envio
		puerto = 3000;

		// Creacion del socket UDP
		socket = new DatagramSocket(puerto);
	}

}