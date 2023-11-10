package cliente;

import java.io.*;
import java.net.*;
import java.util.*;

import mensaje.Mensaje;

public class Cliente {

	// Tam. maximo de mensaje
	private static final int ECHOMAX = 255;
	private static DatagramSocket socketEnvio;
	private static DatagramSocket socketRecibo;
	private static InetAddress ip;
	private static int puerto;
	// Crear variables para reenvio, timeout, retrasos
	
	public static void main(String[] args) throws IOException{
		Mensaje mensaje1 = new Mensaje();
		Mensaje mensaje2 = new Mensaje();
		Mensaje mensaje3 = new Mensaje();
				
		try {
			
			creaSocketEnvio();
			
			// Creacion trama 1

			// Leemos los atributos de consola		
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			// Usuario introduce su identificador en la terminal
			System.out.println("Introducir identificador del cliente");
			int idCliente = Integer.parseInt(stdIn.readLine());
			
			// Usuario introduce su nombre en la terminal
			System.out.println("Introducir nombre de usuario");
			String nombreCliente = stdIn.readLine();

			// Codigo del mensaje 1
			String codigoMensaje = "1_Cliente_Solicita_Credencial";

			// Ip cliente y servidor
			InetAddress ipCliente = InetAddress.getLocalHost();
			InetAddress ipServidor = InetAddress.getByName("0.0.0.0"); // Inicializar a 0.0.0.0
						
			// Creacion del mensaje
			mensaje1.establecerAtributos(idCliente, 0, 
				ipCliente, ipServidor, 
				nombreCliente, "", codigoMensaje, 0, "", 
				false, false);
			
			// Mostrar el mensaje que se va a enviar
			mensaje1.toString();
					
			// Codificacion mensaje antes de enviar
			byte[] cliente_solicita_credencial = mensaje1.codificarMensaje();
			
			// Creacion del datagrama
			DatagramPacket envio = new DatagramPacket(cliente_solicita_credencial, cliente_solicita_credencial.length, ip, puerto); 
			
			System.out.println("Enviando mensaje 1....");

			// Cliente envia en broadcast a todos lo servidores
			socketEnvio.send(envio);

			// IMPORTANTE -> No se si hace falta cerrar el socket aqui
			socketEnvio.close();
			
			// Recepcion trama 2

			// Crea socket en el que recibirá
			creaSocketRecibo(3000);

			// Ajustar el tamaño del paquete??
			DatagramPacket recibo = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
			
			// Se queda esperando hasta que recibe la respuesta de uno de los servidores
			socketRecibo.receive(recibo);
			socketRecibo.close();
			
			// Decodificar mensaje recibido
			mensaje2.decodificarMensaje(recibo.getData());

			// Mostrar mensaje recibido
			mensaje2.toString();


			// Creacion trama 3
			int idServidor = mensaje2.getIdServidor(); 
			ipServidor = mensaje2.getIpServidor();
			String nombreServidor = mensaje2.getNombreServidor();
			int accesoN = mensaje2.getAccesoN();
			String asiento = mensaje2.getAsiento();

			String codigoMensaje2 = "3_Cliente_Acepta_Credencial";

			mensaje3.establecerAtributos(idCliente, idServidor, 
				ipCliente, ipServidor, nombreCliente, nombreServidor, 
				codigoMensaje2, accesoN, asiento, true, false);


			// Mostrar el mensaje que se va a enviar
			mensaje3.toString();
					
			// Codificacion mensaje antes de enviar
			byte[] cliente_acepta_credencial = mensaje3.codificarMensaje();
			
			// Crear de nuevo el socket por el que se enviará
			creaSocketEnvio();
			
			// Creacion del datagrama
			envio = new DatagramPacket(cliente_acepta_credencial, cliente_acepta_credencial.length, ip, puerto);

			System.out.println("Enviando mensaje 3....");
			
			socketEnvio.send(envio);
			socketEnvio.close();
			
			
		}catch(Exception e) {
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
	}
	
}
