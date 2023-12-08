package servidor;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import mensaje.Mensaje;

// Código realizado por Andres Amo Caballero

public class Servidor {

	// Tam. maximo de mensaje
	private static final int ECHOMAX = 1024;
	private static DatagramSocket socket;
	private static InetAddress ip;
	private static int puerto;

	// Información servidor -> Cambiar en los diferentes pc en los que se ejecute el programa
    private static String nombreServidor = "S1";  
    private static int codigoServidor = 1;

	public static void main(String[] args) throws IOException{
				  
		try {
			
			while(true){
				
				// Crea socket
				creaSocket();

				System.out.println("Socket creado, esperando trama...");

				// Datagrama de la trama 1
				DatagramPacket recibo = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
				
				// Espera a que llegue el mensaje
				socket.receive(recibo);

				// Cerramos para que no haya problemas
				socket.close(); 

				// Copia del fichero, en caso de que finalmente no sea seleccionado este servidor, revertir los cambios
				String nombreArchivo = System.getProperty("user.dir") + "/proyectoSocket/src/servidor/BD1.txt";
				String copiaTemporal = "copiaTemp.txt";
				
				// Crear copia temporal
				copiarArchivo(nombreArchivo, copiaTemporal);

				// Crea mensaje nuevo
				Mensaje mensaje = new Mensaje();
				mensaje.decodificarMensaje(recibo.getData());

				// Comprobamos que es el servidor aceptado
				boolean servidorAceptado = (mensaje.getCodigoServidorAceptado() == codigoServidor) 
										&& (mensaje.getNombreServidorAceptado().equals(nombreServidor));

				// Con estos if evitamos que lleguen los mensajes de otro servidor
				if(mensaje.getCodigoMensaje().equals("1_Cliente_Solicita_Credencial")){

					ServidorThreadTrama1 servidorThread = new ServidorThreadTrama1(recibo.getData(), nombreServidor, codigoServidor);
					new Thread(servidorThread).start();

				}else if(mensaje.getCodigoMensaje().equals("3_Cliente_Acepta_Credencial") && servidorAceptado){

					ServidorThreadTrama3 servidorThread = new ServidorThreadTrama3(recibo.getData());
					new Thread(servidorThread).start();

				}else{
					System.out.println("NO es el servidor aceptado");

					// Restaurar archivo original desde la copia temporal
					restaurarDesdeCopiaTemporal(copiaTemporal, nombreArchivo);
				}	

				// Retardo
				Thread.sleep(2000);
				System.out.println("FIN");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void creaSocket() throws IOException {
		// Direccion de recepcion -> Broadcast
		ip = InetAddress.getByName("192.168.18.255");
		
		// Puerto de envio
		puerto = 3000;

		// Creacion del socket UDP
		socket = new DatagramSocket(puerto);
	}

	// Metodos auxiliares para crear y restaurar el fichero en caso de no ser el servidor elegido
	private static void copiarArchivo(String origen, String destino) throws IOException {
        Path origenPath = Paths.get(origen);
        Path destinoPath = Paths.get(destino);

        // Copiar el archivo
        Files.copy(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
    }

    private static void restaurarDesdeCopiaTemporal(String copiaTemporal, String destino) throws IOException {
        Path copiaTemporalPath = Paths.get(copiaTemporal);
        Path destinoPath = Paths.get(destino);

        // Restaurar el archivo desde la copia temporal
        Files.copy(copiaTemporalPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
    }

}