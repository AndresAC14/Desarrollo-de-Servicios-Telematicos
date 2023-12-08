package cliente;

import java.io.*;
import java.net.*;

// Código realizado por Andres Amo Caballero

public class Cliente {

	// Socket
	private static Socket socket;
	private static InetAddress ip;
	private static int puerto;
	
	public static void main(String[] args) {
		try {
		
			// Crea socket
			creaSocket();

			// Con PrintWriter enviamos
	        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);

			// Leemos los atributos de consola		
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			// Usuario introduce su identificador en la terminal
			System.out.println("Introducir dni de cliente");
			int dni = Integer.parseInt(stdIn.readLine());
			
			// Usuario introduce su nombre en la terminal
			System.out.println("Introducir nombre de cliente");
			String nombreCliente = stdIn.readLine();
			stdIn.close();
			
			// Devuelve la suma de los valores ascii de cada caracter del nombre del cliente
			int valorAscii = 0;
			
			for (int i = 0; i < nombreCliente.length(); i++) {
	            char caracter = nombreCliente.charAt(i);
	            valorAscii += (int) caracter;

	        }
			
			// Creación identificador ( Es una funcion inventada )
			int identificador = (dni / 150) + valorAscii;
			
			System.out.printf("Identificador obtenido por el cliente %s: %d \n", nombreCliente, identificador);
			
			// Envio del mensaje
			String contenido = ""+identificador;
			System.out.println("Enviando identificador...");
			out.println(contenido);
			
			Thread.sleep(3000);			
			// Con BufferedReader leemos lo que recibimos
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			// Espera a recibir
			String mensajeRecibido = null;
			do {
				mensajeRecibido = in.readLine();
				
			}while(mensajeRecibido == null);
			

			// Mostrar mensaje recibido
			System.out.println("Mensaje recibido con contenido: " + mensajeRecibido);
			
			String[] partes = mensajeRecibido.split(";");
			
			int accesoN = Integer.parseInt(partes[0]);
			String asiento = partes[1];
			
			System.out.printf("Los credenciales proporcionados al usuario son: \n AccesoN: %d \n Asiento: %s \n", accesoN, asiento);
			
			in.close();
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void creaSocket() throws IOException {
		// Direccion de envio
		ip = InetAddress.getByName("localhost"); // 192.168.18.39
		
		// Puerto de envio
		puerto = 3000;

		// Creacion del socket TCP
		socket = new Socket(ip, puerto);
	}

}