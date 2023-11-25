package servidor;

import java.io.*;
import java.net.*;
import java.util.*;
import mensaje.Mensaje;

public class ServidorThreadTrama1 implements Runnable{

    // Socket
    private static InetAddress ip;
	private static int puerto;
    private static DatagramSocket socket;
    
    // Información servidor -> Cambiar en los diferentes pc en los que se ejecute el programa  
    private static String nombreServidor = "S1";
    private static int codigoServidor = 1;
    
    // Variables mensaje
	private static InetAddress ipServidor;
	private static String codigoMensaje;
	
    // Información del fichero
	private static int accesoN;
	private static String asiento;

    // Tramas
    private byte[] recibido;
    private static DatagramPacket envio;
    
    public ServidorThreadTrama1(byte[] recibido){

        this.recibido = recibido;

    }
    
    public void run(){

        try{
            // Creamos nuevo mensaje en el que almacenar la trama
            Mensaje mensaje1 = new Mensaje();

            // Decodificamos el mensaje
            mensaje1.decodificarMensaje(recibido);
            System.out.println("Mensaje decodificado");
            
            // Mostramos el mensaje
            System.out.println("Mostrando mensaje 1 recibido...");
            System.out.println(mensaje1.toString());

            // Comprobar que el id cliente se encuentra en el fichero
            int idCliente = mensaje1.getIdCliente();
            boolean encontrado = estaInscrito(idCliente); 
                 
            // Procesa el fichero
            if(encontrado) procesarFichero(idCliente);
            
            // Creamos el mensaje que llevará la trama 2
            Mensaje mensaje2 = new Mensaje();

            // Si no encuentra el identificador del cliente en el fichero, manda mensaje "5.servidor_no_encuentra_cliente"
            codigoMensaje = encontrado ? "2_Servidor_Ofrece_Credencial" : "5_Servidor_No_Encuentra_Cliente";
            ipServidor = InetAddress.getLocalHost();
            
            // Rellenamos los campos necesarios en la trama
            mensaje2.establecerAtributos(idCliente, codigoServidor, mensaje1.getIpCliente(), ipServidor,
                mensaje1.getNombreCliente(), nombreServidor, codigoMensaje, 
                0, "sd", 
                accesoN, asiento, false, encontrado);
        
            // Codificacion mensaje antes de enviar
            byte[] servidor_ofrece_credencial = mensaje2.codificarMensaje();
            
            System.out.println("Mostrando mensaje antes del envio \n" + mensaje2.toString());
            
            // Creamos el socket por el que se enviará
            creaSocket();

            // Creacion del datagrama
            envio = new DatagramPacket(servidor_ofrece_credencial, servidor_ofrece_credencial.length, ip, puerto);
            
            System.out.println("Enviando trama con ServidorThreadTrama1....");

            // Enviamos la trama
            socket.send(envio);

            // Cerramos el socket
            socket.close();

            if(codigoMensaje.equals("5_Servidor_No_Encuentra_Cliente")){
                System.out.println("FIN");
                System.exit(0);
            }

        }catch(Exception e){
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

    // Busca en el fichero si el idCliente está asignado
    public static boolean estaInscrito(int idCliente){

        boolean encontrado = false;
        int id = -1;
        
        try(Scanner sc = new Scanner(new File(System.getProperty("user.dir") + "/proyectoSocket/src/servidor/Inscritos.txt"))) {
            
            while(sc.hasNextLine() && !encontrado){
                String linea = sc.nextLine();
                        
                String[] partes = linea.split(";");
    
                //Id del fichero
                id = Integer.parseInt(partes[0]);
    
                encontrado = (idCliente == id);               
            }
                    
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encontrado;
    }

    // Guarda accesoN y asiento segun el id indicado
    public static void procesarFichero(int idCliente){

        boolean encontrado = false;
        int id = -1;
        int acc = 0;
        String asi = "sd";
        char asignado;
        String archivoEntrada = System.getProperty("user.dir") + "/proyectoSocket/src/servidor/BD1.txt";
        
        try {
            // Abrir el archivo de entrada
            BufferedReader br = new BufferedReader(new FileReader(archivoEntrada));

            // Crear un archivo temporal
            String archivoTemporal = "archivo_temporal.txt";
            BufferedWriter bw = new BufferedWriter(new FileWriter(archivoTemporal));

            // Leer el archivo línea por línea
            String linea;
            while ((linea = br.readLine()) != null) {
                // Dividir la línea en partes usando un punto y coma
                String[] partes = linea.split(";");

                // Obtener los valores
                acc = Integer.parseInt(partes[0].trim());
                asi = partes[1].trim();
                asignado = partes[2].trim().charAt(0);

                // Modificar la línea si es 'N'
                if (asignado == 'N' && !encontrado) {
                    // Añadir el identificador
                    linea = String.format("%d;%s;%c;%d;", acc, asi, asignado, idCliente);

                    // Guardar accesoN y asiento para la trama
                    accesoN = acc;
                    asiento = asi;
                    encontrado = true;
                }

                // Escribir la línea en el archivo temporal
                bw.write(linea);
                bw.newLine();
            }

            // Cerrar los archivos
            br.close();
            bw.close();

            // Renombrar el archivo temporal al archivo de entrada original
            File archivoOriginal = new File(archivoEntrada);
            File archivoRenombrado = new File(archivoTemporal);

            if (archivoRenombrado.renameTo(archivoOriginal)) {
                System.out.println("Archivo modificado con éxito.");
            } else {
                System.out.println("Error al renombrar el archivo.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}
