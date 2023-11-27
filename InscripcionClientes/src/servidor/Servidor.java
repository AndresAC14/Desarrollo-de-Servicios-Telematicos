package servidor;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

// Código realizado por Andres Amo Caballero

public class Servidor {

	// Tam. maximo de mensaje
	private static final int ECHOMAX = 255;
	
	// Socket
	private static DatagramSocket socket;
	private static InetAddress ip;
	private static int puerto;

	// Información del fichero
	private static int accesoN;
	private static String asiento;

	public static void main(String[] args) {

		try {
			
			// Crea socket
			creaSocket();
	
			// Espera a recibir
			DatagramPacket recibo = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
			
			socket.receive(recibo);
	
			// Cerramos para que no haya problemas
			socket.close(); 
	
			// Decodificar id
			int idCliente = ByteBuffer.wrap(recibo.getData()).getInt();
	
			System.out.println("Mensaje recibido con contenido: " + idCliente);
	
			// Escribe en el fichero el id que ha recibido
			procesarFichero(idCliente);
			
			// Codificacion mensaje antes de enviar
			String msg = accesoN + ";" + asiento;
			byte[] trama = msg.getBytes();
	
			// ENVIAR identificador
			creaSocket();
	
			DatagramPacket envio = new DatagramPacket(trama, trama.length, ip, puerto);
	
			socket.send(envio);
	
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void creaSocket() throws IOException {
		// Direccion de recepcion -> Broadcast
		ip = InetAddress.getByName("localhost"); // 192.168.56.255
		
		// Puerto de envio
		puerto = 3000;

		// Creacion del socket UDP
		socket = new DatagramSocket();
	}

	public static void procesarFichero(int id){

		String archivoEntrada = System.getProperty("user.dir") + "/inscripcionClientes/src/servidor/inscripcion.txt";

		// Obtener el último valor de accesoN del archivo
        int ultimoAccesoN = obtenerUltimoAccesoN(archivoEntrada);
        
        // Incrementar el accesoN y construir la nueva línea
        accesoN = ultimoAccesoN + 1;
        asiento = "A" + accesoN;

        String nuevaLinea = id + ";" + accesoN + ";" + asiento;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoEntrada, true))) {
            // Escribir la nueva línea al final del archivo
            writer.newLine();
            writer.write(nuevaLinea);
            System.out.println("Línea añadida al final del archivo: " + nuevaLinea);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
	}

	private static int obtenerUltimoAccesoN(String filePath) {
        int ultimoAccesoN = 0;

        try (Scanner scanner = new Scanner(new File(filePath))) {
            // Leer el archivo para encontrar el último valor de accesoN
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] partes = linea.split(";");
                if (partes.length >= 2) {
                    int accesoN = Integer.parseInt(partes[1]);
                    if (accesoN > ultimoAccesoN) {
                        ultimoAccesoN = accesoN;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado: " + e.getMessage());
        }

        return ultimoAccesoN;
    }

}

