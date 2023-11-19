package cliente;

import java.io.*;
import java.net.*;
//import java.util.*;

import mensaje.Mensaje;

public class Cliente {

	// Tam. maximo de mensaje
	private static final int ECHOMAX = 1024;
	//private static DatagramSocket socketEnvio;
	//private static DatagramSocket socketRecibo;
	private static DatagramSocket socket;
	private static InetAddress ip;
	private static int puerto;
	// Crear variables para reenvio, timeout, retrasos
	// Temporizador de retransmision (ms)
	//***************private static final int TIMEOUT = 3000; 
	// Maximo numero de retransmisiones
	//***************private static final int MAXTRIES = 5; 

	
	public static void main(String[] args) throws IOException{
		Mensaje mensaje1 = new Mensaje();
		Mensaje mensaje2 = new Mensaje();
		Mensaje mensaje3 = new Mensaje();
		Mensaje mensaje4 = new Mensaje();
				
		try {
			
			creaSocket();
			
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
				nombreCliente, "sd", codigoMensaje, 
				0, "sd", 0, "sd", 
				false, false);
			
			// Mostrar el mensaje que se va a enviar
			System.out.println(mensaje1.toString());
					
			// Codificacion mensaje antes de enviar
			//byte[] cliente_solicita_credencial = mensaje1.codificarMensaje();
			
			// Creacion del datagrama
			DatagramPacket envio1 = new DatagramPacket(mensaje1.codificarMensaje(), mensaje1.codificarMensaje().length, ip, puerto); 
			
			System.out.println("Enviando mensaje 1....");

			// Cliente envia en broadcast a todos lo servidores
			//socketEnvio.send(envio);
			socket.send(envio1);

			// IMPORTANTE -> No se si hace falta cerrar el socket aqui
			//socketEnvio.close();
			
			// Recepcion trama 2

			// Crea socket en el que recibirá
			//creaSocketRecibo(3000);

			// Ajustar el tamaño del paquete??
			DatagramPacket recibo2 = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
			
			// Para el timeout de 2 segundos
			//socket.setSoTimeout(TIMEOUT);

			// Se queda esperando hasta que recibe la respuesta de uno de los servidores
			//socketRecibo.receive(recibo);
			//socketRecibo.close();
			System.out.println("Esperando recepcion mensaje 2...");
			socket.receive(recibo2);

			// Decodificar mensaje recibido
			mensaje2 = new Mensaje();
			mensaje2.decodificarMensaje(recibo2.getData());

			// Mostrar mensaje recibido
			System.out.println("Mostrando mensaje 2...");
			System.out.println(mensaje2.toString());


			// Creacion trama 3
			mensaje3 = new Mensaje();
			int idServidor = mensaje2.getIdServidor(); 
			ipServidor = mensaje2.getIpServidor();
			String nombreServidor = mensaje2.getNombreServidor();
			int codigoServidorAceptado = mensaje2.getCodigoServidorAceptado();
			String nombreServidorAceptado = mensaje2.getNombreServidorAceptado();
			int accesoN = mensaje2.getAccesoN();
			String asiento = mensaje2.getAsiento();
			String codigoMensaje2 = "3_Cliente_Acepta_Credencial";

			mensaje3.establecerAtributos(idCliente, idServidor, 
				ipCliente, ipServidor, nombreCliente, nombreServidor, 
				codigoMensaje2, codigoServidorAceptado, nombreServidorAceptado,
				accesoN, asiento, true, false);


			// Mostrar el mensaje que se va a enviar
			System.out.println("Mostrando mensaje 3...");
			System.out.println(mensaje3.toString());
					
			// Codificacion mensaje antes de enviar
			byte[] cliente_acepta_credencial = mensaje3.codificarMensaje();
			
			// Crear de nuevo el socket por el que se enviará
			//creaSocketEnvio();
			
			// Creacion del datagrama
			DatagramPacket envio3 = new DatagramPacket(cliente_acepta_credencial, cliente_acepta_credencial.length, ip, puerto);

			System.out.println("Enviando mensaje 3....");
			
			//socketEnvio.send(envio);
			//socketEnvio.close();
			socket.send(envio3);

			// Recepcion trama 4 o 5

			// Crea socket en el que recibirá
			//creaSocketRecibo(3000);

			// Ajustar el tamaño del paquete??
			DatagramPacket recibo4 = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
			
			// Se queda esperando hasta que recibe la respuesta de uno de los servidores
			//socketRecibo.receive(recibo4);
			//socketRecibo.close();
			socket.receive(recibo4);

			// Decodificar mensaje recibido
			mensaje4 = new Mensaje();
			mensaje4.decodificarMensaje(recibo4.getData());

			// Mostrar mensaje recibido
			System.out.println("Mostrando mensaje 4...");
			System.out.println(mensaje4.toString());

			// Fin del programa de cliente
			socket.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

	public static void creaSocket() throws IOException {
		// Direccion de envio -> Broadcast
		ip = InetAddress.getByName("192.168.18.255");
		
		// Puerto de envio, elegimos el 3000 pero habra que cambiarlo
		puerto = 3000;

		// Creacion del socket UDP
		//socketEnvio = new DatagramSocket();
		socket = new DatagramSocket(puerto);
	}

	/* 
	public static void creaSocketRecibo(int puerto) throws IOException {
		// Creacion del socket UDP
		socketRecibo = new DatagramSocket(puerto);
	}
	*/
}
