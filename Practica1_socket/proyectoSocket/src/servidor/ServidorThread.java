package servidor;

import java.io.*;
import java.net.*;
import java.util.*;
import mensaje.Mensaje;

public class ServidorThread extends Thread{

    // Informacion del servidor
    private static InetAddress ip;
	private static int puerto;
    private static DatagramSocket socket;
    
    // Variables mensaje
    private static String nombreServidor = "S1"; // Cambiar en los diferentes pc en los que se ejecute el programa  
    private static int codigoServidor = 1;

    private static int idCliente;
	private static int idServidor; // este es el recibido
	private static InetAddress ipServidor;
	
	private static String codigoMensaje;
	
	private static int accesoN;
	private static String asiento;
	private static boolean aceptado; // Seria servidorAcepta
	private static boolean encontrado;
    private static char asignado;
    private static int idFichero;

    // Tramas
    private DatagramPacket recibido;
    private static DatagramPacket envio;
    private static Mensaje mensaje1;
    private static Mensaje mensaje2;

    public ServidorThread(DatagramPacket recibido){

        this.recibido = recibido;

    }
    
    public void run(){

        try{
            // Decodificamos el mensaje
            mensaje1.decodificarMensaje(recibido.getData());
            System.out.println("Mensaje decodificado");

            // Primero pillar el id de servidor -> 
            idServidor = mensaje1.getIdServidor();
            // si esta asignado y es distinto poner como disponibles las credenciales que ofrecieron
            // si esta asignado y es igual, mandar mensaje con 4.Servidor confirma asignacion
            ipServidor = InetAddress.getLocalHost();

            System.out.println("Mostrando mensaje...");
            // Mostramos el mensaje
            mensaje1.toString();

            //1. Comprobar que el id cliente se encuentra en el fichero
            idCliente = mensaje1.getIdCliente();
            encontrado = false;

            boolean servidorAceptado = (mensaje1.getCodigoServidorAceptado() == codigoServidor) 
                                    && (mensaje1.getNombreServidorAceptado().equals(nombreServidor));

            if(!servidorAceptado){
                // Enviar el codigo 4
                codigoMensaje = "4_Servidor_Confirma_Asignacion";

            }else{

                // PROCESAR FICHERO
                procesarFichero();
                
                //3. Poner como asignadas las credenciales que envia al cliente. NI IDEA

                // IMPORTANTE: Si no encuentra el identificador del cliente en el fichero, manda mensaje "5.servidor_no_encuentra_cliente"
                codigoMensaje = encontrado ? "2_Servidor_Ofrece_Credencial" : "5_Servidor_No_Encuentra_Cliente";
                
                // Rellenamos los campos necesarios en la trama
                mensaje2.establecerAtributos(idCliente, codigoServidor, mensaje1.getIpCliente(), ipServidor,
                    mensaje1.getNombreCliente(), nombreServidor, codigoMensaje, 
                    0, "sd", 
                    accesoN, asiento, false, encontrado);
            }
            
            byte[] servidor_ofrece_credencial = mensaje2.codificarMensaje();

            envio = new DatagramPacket(servidor_ofrece_credencial, servidor_ofrece_credencial.length, ip, puerto);
            
            System.out.println("Enviando mensaje con Hebra....");

            DatagramSocket socket = new DatagramSocket();
            socket.send(envio);

        }catch(Exception e){
            e.printStackTrace();
        }   

    }

    public static void creaSocket() throws IOException {
		// Direccion de envio -> Broadcast
		ip = InetAddress.getByName("192.168.167.255");
		
		// Puerto de envio, elegimos el 3000 pero habra que cambiarlo
		puerto = 3000;

		// Creacion del socket UDP
		//socketEnvio = new DatagramSocket();
		socket = new DatagramSocket(puerto);
	}

    public static void procesarFichero(){

        try(Scanner sc = new Scanner("BaseDatos.txt")) {
            
            while(sc.hasNextLine() && !encontrado){
                String linea = sc.nextLine();
                        
                String[] partes = linea.split(";");
    
                //2. Mensaje 2 -> Contiene accesoN, asiento e identificador servidor
                accesoN = Integer.parseInt(partes[0]);
                asiento = partes[1];
                // Segun chatGPT se obtiene asi, sino pone del tiron
                asignado = partes[2].charAt(0); 
                idFichero = Integer.parseInt(partes[3]);
    
                // FALTAN COSAS
    
                encontrado = (idCliente == idFichero);               
            }
                    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
