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
	// Crear variables para reenvio, timeout, retrasos

	// Información servidor
    private static String nombreServidor = "S1"; // Cambiar en los diferentes pc en los que se ejecute el programa  
    private static int codigoServidor = 1;

	public static void main(String[] args) throws IOException{
		Mensaje mensaje1 = new Mensaje();
		Mensaje trama3 = new Mensaje();
		  
		try {
			
			// Crea socket en el que recibirá mensaje
			creaSocket();

			System.out.println("Socket creado");

			// Cuidado con el tam
			DatagramPacket recibo1 = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
			
			// Recibe el servidor y redirige el paquete a la hebra
			// Espera a que llegue el mensaje
			socket.receive(recibo1);

			//System.out.println("Paquete recibido, creando hebra...");

			/* DOS OPCIONES
			 * Sacar el codigo_mensaje de la trama -> if (es t1) mandar a la hebra t1 elseif(es t3) mandar a la hebra t3
			 * 
			 * 
			 * Si no ninguna de esas 2 tramas NO es el servidor aceptado -> Poner como disponibles las credenciales, es decir, 
			 * 								de la t1 coge idCliente y hacer algo en el fichero
			 * 
			 * Poner metodo selector tipo, recibo trama llama al metodo selector y ahi ya eliges
			 */

			socket.close(); // Sino peta la hebra al mandar por el mismo puerto

			mensaje1.decodificarMensaje(recibo1.getData());

			boolean servidorAceptado = (mensaje1.getCodigoServidorAceptado() == codigoServidor) 
                                    && (mensaje1.getNombreServidorAceptado().equals(nombreServidor));

			if(mensaje1.getCodigoMensaje().equals("1_Cliente_Solicita_Credencial")){

				ServidorThreadTrama1 servidorThread = new ServidorThreadTrama1(recibo1.getData());
				new Thread(servidorThread).start();

			}else if(mensaje1.getCodigoMensaje().equals("3_Cliente_Acepta_Credencial") && servidorAceptado){

				ServidorThreadTrama3 servidorThread = new ServidorThreadTrama3(recibo1.getData());
				new Thread(servidorThread).start();

			}else{
				// NO es el servidor aceptado -> Poner como disponibles las credenciales, es decir, 
				// coge idCliente y hacer algo en el fichero
			}	

			Thread.sleep(1000); // 1 segundo
			System.out.println("Servidor en espera de que termine de procesarse el mensaje....");
			Thread.sleep(1000); // 1 segundo

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
		//socketEnvio = new DatagramSocket();
		socket = new DatagramSocket(puerto);
	}

}
