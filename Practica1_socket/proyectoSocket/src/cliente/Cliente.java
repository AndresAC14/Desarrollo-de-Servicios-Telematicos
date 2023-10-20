package cliente;

import java.io.*;
import java.net.*;
import java.util.*;

import mensaje.Mensaje;

public class Cliente {

	// Tam. maximo de mensaje
	private static final int ECHOMAX = 255;
	private static DatagramSocket socket;
	private static InetAddress ip;
	private static int puerto;
	
	public static void main(String[] args) throws IOException{
		Mensaje mensaje1 = new Mensaje();
		Mensaje mensaje2 = new Mensaje();
		Mensaje mensaje3 = new Mensaje();
		
		try {
			
			creaSocket();
			
			// Creacion del mensaje 
			// -> Aqui llamo a un metodo de Mensaje y le paso los parametros necesarios
		
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("Introducir identificador del cliente");
			mensaje1.setIdCliente(Integer.parseInt(stdIn.readLine()));
			
			System.out.println("Introducir nombre de usuario");
			mensaje1.setNombreCliente(stdIn.readLine());
			
			mensaje1.setIp(InetAddress.getLocalHost()); // Se puede coger con una funcion
			mensaje1.setCodigoMensaje("123"); // Generar de alguna manera o ponerlo yo
			
			
			//PRUEBA -> cambiar luego
			DatagramPacket envio = new DatagramPacket(new byte[ECHOMAX], ECHOMAX); // = mensaje.creaPaquete();
			DatagramPacket recibo = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
			
			socket.send(envio);
			socket.receive(recibo);
			socket.close();
			
			//Cambiar ip y //puerto
			
			socket.send(envio);
			socket.receive(recibo);
			socket.close();
			
			
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

	public static void creaSocket() throws IOException {
		// Creacion del socket UDP
		socket = new DatagramSocket();
		
		// Direccion de envio -> Broadcast
		ip = InetAddress.getByName("192.168.167.255");
		
		// Puerto de envio, elegimos el 3000 pero habra que cambiarlo
		puerto = 3000;
	}
	
}
