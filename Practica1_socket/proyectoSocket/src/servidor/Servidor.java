package servidor;

import java.io.*;
import java.net.*;

import mensaje.Mensaje;

public class Servidor {

	// Tam. maximo de mensaje
	private static final int ECHOMAX = 1024;
	private static DatagramSocket socket;
	private static InetAddress ip;
	private static int puerto;

	// Información servidor
    private static String nombreServidor = "S1"; // Cambiar en los diferentes pc en los que se ejecute el programa  
    private static int codigoServidor = 1;

	public static void main(String[] args) throws IOException{
				  
		try {
			
			// Crea socket
			creaSocket();

			System.out.println("Socket creado, esperando trama...");

			// Datagrama de la trama 1
			DatagramPacket recibo1 = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
			
			// Espera a que llegue el mensaje
			socket.receive(recibo1);

			// Cerramos para que no haya problemas
			socket.close(); 

			// Crea mensaje nuevo
			Mensaje mensaje1 = new Mensaje();
			mensaje1.decodificarMensaje(recibo1.getData());

			// Con estos if evitamos que lleguen los mensajes de otro servidor
			if(mensaje1.getCodigoMensaje().equals("1_Cliente_Solicita_Credencial")){

				ServidorThreadTrama1 servidorThread = new ServidorThreadTrama1(recibo1.getData());
				new Thread(servidorThread).start();

			}
			
			// Retardo para que pueda procesar bien la trama anterior y recibir la nueva
			Thread.sleep(1000);

			// Crea socket
			creaSocket();

			// Datagrama de la trama 3
			DatagramPacket recibo2 = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
			
			// Espera a que llegue el mensaje
			socket.receive(recibo2);

			// Cerramos para que no haya problemas
			socket.close(); 

			// Crea mensaje nuevo
			Mensaje mensaje2 = new Mensaje();
			mensaje2.decodificarMensaje(recibo2.getData());

			// Comprobamos que es el servidor aceptado
			boolean servidorAceptado = (mensaje2.getCodigoServidorAceptado() == codigoServidor) 
									&& (mensaje2.getNombreServidorAceptado().equals(nombreServidor));
			
			
			if(mensaje2.getCodigoMensaje().equals("3_Cliente_Acepta_Credencial") && servidorAceptado){

				ServidorThreadTrama3 servidorThread = new ServidorThreadTrama3(recibo2.getData());
				new Thread(servidorThread).start();

			}else{
				// NO es el servidor aceptado -> Poner como disponibles las credenciales, es decir, coge idCliente y hacer algo en el fichero
				System.out.println("NO es el servidor aceptado");
			}	

			// Retardo
			Thread.sleep(2000);
			System.out.println("FIN");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void creaSocket() throws IOException {
		// Direccion de recepcion -> Broadcast
		ip = InetAddress.getByName("192.168.18.255");
		
		// Puerto de envio, elegimos el 3000 pero habra que cambiarlo
		puerto = 3000;

		// Creacion del socket UDP
		socket = new DatagramSocket(puerto);
	}

}
