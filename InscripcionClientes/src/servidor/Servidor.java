package servidor;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

// Código realizado por Andres Amo Caballero

public class Servidor {
	
	// Socket
	private static Socket socket;
	private static InetAddress ip;
	private static int puerto;

	// Información del fichero
	private static int accesoN;
	private static String asiento;

	public static void main(String[] args) {

		try {
			
			// Crea socket
			creaSocket();
			boolean terminar = false;

			while(!terminar){

				// Con BufferedReader leemos lo que recibimos
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	
				// Con PrintWriter enviamos
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
				
				System.out.println("Servidor iniciado");			
				System.out.println("Esperando mensaje....");
				
				String mensajeRecibido = in.readLine();
		
				// Decodificar id
				int idCliente = Integer.parseInt(mensajeRecibido);
		
				System.out.println("Mensaje recibido con contenido: " + idCliente);
		
				// Escribe en el fichero el id que ha recibido
				procesarFichero(idCliente);
				
				// Codificacion mensaje antes de enviar
				String mensajeEnvio = accesoN + ";" + asiento;
		
				out.println(mensajeEnvio);

				// Cerramos el envio y la recepcion
				out.close();
				in.close();
			}
	
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void creaSocket() throws IOException {
		// Direccion de envio -> Broadcast
		ip = InetAddress.getByName("192.168.226.255"); // 192.168.56.255
		
		// Puerto de envio
		puerto = 3000;

		// Creacion del socket TCP
		socket = new Socket(ip, puerto);
	}

	public static void procesarFichero(int id){

		String archivoEntrada = System.getProperty("user.dir") + "/src/servidor/inscripcion.txt";

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

