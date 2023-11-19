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
    
    // Información servidor
    private static String nombreServidor = "S1"; // Cambiar en los diferentes pc en los que se ejecute el programa  
    private static int codigoServidor = 1;
    
    // Variables mensaje
    private static int idCliente;
	private static InetAddress ipServidor;
	private static String codigoMensaje;
	
    // Información del fichero
	private static int accesoN;
	private static String asiento;
	private static boolean encontrado;
    private static char asignado;
    private static int idFichero;

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
            idCliente = mensaje1.getIdCliente();
            encontrado = false;
                 
            // Procesa el fichero
            procesarFichero();
            
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
		
		// Puerto de envio, elegimos el 3000 pero habra que cambiarlo
		puerto = 3000;

		// Creacion del socket UDP
		socket = new DatagramSocket(puerto);
	}

    // Busca en el fichero si el idCliente está asignado
    public static void procesarFichero(){

        try(Scanner sc = new Scanner(new File(System.getProperty("user.dir") + "/proyectoSocket/src/servidor/BaseDatos.txt"))) {
            
            while(sc.hasNextLine() && !encontrado){
                String linea = sc.nextLine();
                        
                String[] partes = linea.split(";");
    
                //2. Mensaje 2 -> Contiene accesoN, asiento e identificador servidor
                accesoN = Integer.parseInt(partes[0]);
                asiento = partes[1];
                // Segun chatGPT se obtiene asi, sino pone del tiron
                asignado = partes[2].charAt(0); // esto tiene que servir para algo
                //Id del fichero
                idFichero = Integer.parseInt(partes[3]);
    
                encontrado = (idCliente == idFichero);               
            }
            
            // FALTAN COSAS -> Poner como asignadas las credenciales es borrar la linea
            // if(encontrado) asignarCredenciales(int linea);
            if(!encontrado) accesoN = 0; asiento = "sd";
                    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
