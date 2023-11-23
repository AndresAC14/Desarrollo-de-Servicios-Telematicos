package cliente;

import java.io.*;
import java.net.*;

import mensaje.Mensaje;

public class Cliente {

	// Tam. maximo de mensaje
	private static final int ECHOMAX = 1024;

	// Socket
	private static DatagramSocket socket;
	private static InetAddress ip;
	private static int puerto;

	
	public static void main(String[] args) throws IOException{
						
		try {
			/*
			 * Creacion trama 1
			 */

			// Leemos los atributos de consola		
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			// Usuario introduce su identificador en la terminal
			System.out.println("Introducir identificador del cliente");
			int idCliente = Integer.parseInt(stdIn.readLine());
			
			// Usuario introduce su nombre en la terminal
			//System.out.println("Introducir nombre de usuario");
			String nombreCliente = "Andres"; //stdIn.readLine();

			// Codigo del mensaje 1
			String codigoMensaje = "1_Cliente_Solicita_Credencial";

			// Ip cliente y servidor
			InetAddress ipCliente = InetAddress.getLocalHost();
			InetAddress ipServidor = InetAddress.getByName("0.0.0.0"); // Inicializar a 0.0.0.0
						
			// Creacion del mensaje
			Mensaje mensaje1 =  new Mensaje();
			mensaje1.establecerAtributos(idCliente, 0, 
				ipCliente, ipServidor, 
				nombreCliente, "sd", codigoMensaje, 
				0, "sd", 0, "sd", 
				false, false);
			
			// Mostrar el mensaje que se va a enviar
			System.out.println(mensaje1.toString());
					
			// Codificacion mensaje antes de enviar
			byte[] cliente_solicita_credencial = mensaje1.codificarMensaje();
			
			creaSocket();

			// Creacion del datagrama
			DatagramPacket envio1 = new DatagramPacket(cliente_solicita_credencial, cliente_solicita_credencial.length, ip, puerto); 
			
			System.out.println("Enviando mensaje 1....");

			// Cliente envia en broadcast a todos lo servidores
			socket.send(envio1);
			socket.close();
			
			/* 
			 * Recepcion trama 2
			 */ 

			// Crea socket en el que recibirá
			creaSocket();

			// Creacion del datagrama
			DatagramPacket recibo2 = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
			
			// Se queda esperando hasta que recibe la respuesta de uno de los servidores
			System.out.println("Esperando recepcion mensaje 2...");
			socket.receive(recibo2);

			// Creacion del mensaje
			Mensaje mensaje2 = new Mensaje();

			// Decodificar mensaje recibido
			mensaje2.decodificarMensaje(recibo2.getData());

			// Mostrar mensaje recibido
			System.out.println("Mostrando mensaje 2...");
			System.out.println(mensaje2.toString());

			socket.close();

			// Si recibe la trama con el codigo 5 termina la ejecucion
			if(mensaje2.getCodigoMensaje().equals("5_Servidor_No_Encuentra_Cliente")){
				// Fin del programa de cliente
				System.out.println("FIN CLIENTE");
				System.exit(0);
			}
			
			/*
			 * Creacion trama 3
			 */

			int idServidor = mensaje2.getIdServidor(); 
			ipServidor = mensaje2.getIpServidor();
			String nombreServidor = mensaje2.getNombreServidor();
			int codigoServidorAceptado = mensaje2.getIdServidor();
			String nombreServidorAceptado = mensaje2.getNombreServidor();
			int accesoN = mensaje2.getAccesoN();
			String asiento = mensaje2.getAsiento();
			String codigoMensaje2 = "3_Cliente_Acepta_Credencial";

			// Creacion del mensaje
			Mensaje mensaje3 = new Mensaje();
			mensaje3.establecerAtributos(idCliente, idServidor, 
				ipCliente, ipServidor, nombreCliente, nombreServidor, 
				codigoMensaje2, codigoServidorAceptado, nombreServidorAceptado,
				accesoN, asiento, true, true);


			// Mostrar el mensaje que se va a enviar
			System.out.println("Mostrando mensaje 3...");
			System.out.println(mensaje3.toString());
					
			// Codificacion mensaje antes de enviar
			byte[] cliente_acepta_credencial = mensaje3.codificarMensaje();

			// Retardo
			Thread.sleep(1000);
						
			creaSocket();

			// Creacion del datagrama
			DatagramPacket envio3 = new DatagramPacket(cliente_acepta_credencial, cliente_acepta_credencial.length, ip, puerto);

			System.out.println("Enviando mensaje 3....");
			socket.send(envio3);
			socket.close();

			/*
			 * Recepcion trama 4
			 */

			// Creacion del datagrama
			DatagramPacket recibo4 = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
			
			// Se queda esperando hasta que recibe la respuesta
			creaSocket();
			socket.receive(recibo4);
			socket.close();

			// Decodificar mensaje recibido
			Mensaje mensaje4 = new Mensaje();
			mensaje4.decodificarMensaje(recibo4.getData());

			// Mostrar mensaje recibido
			System.out.println("Mostrando mensaje 4...");
			System.out.println(mensaje4.toString());

			// Fin del programa de cliente
			System.out.println("FIN CLIENTE");
						
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

	public static void creaSocket() throws IOException {
		// Direccion de envio -> Broadcast
		ip = InetAddress.getByName("192.168.18.255");
		
		// Puerto de envio
		puerto = 3000;

		// Creacion del socket UDP
		socket = new DatagramSocket(puerto);
	}

}
