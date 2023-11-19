package servidor;

import java.io.*;
import java.net.*;
import java.util.*;
import mensaje.Mensaje;

public class ServidorThreadTrama3 implements Runnable{

    // Socket
    private static InetAddress ip;
	private static int puerto;
    private static DatagramSocket socket;
    
    // Variables mensaje
	private static String codigoMensaje;
    private static InetAddress ipServidor;

    // Tramas
    private byte[] recibido;
    private static DatagramPacket envio;
    private static Mensaje mensaje1;
    private static Mensaje mensaje2;

    public ServidorThreadTrama3(byte[] recibido){

        this.recibido = recibido;

    }
    
    public void run(){

        try{
            // Creamos nuevo mensaje en el que almacenar la trama
            mensaje1 = new Mensaje();

            // Decodificamos el mensaje
            mensaje1.decodificarMensaje(recibido);
            System.out.println("Mensaje decodificado");
            
            // Mostramos el mensaje
            System.out.println("Mostrando mensaje 3 recibido...");
            System.out.println(mensaje1.toString());

            // Creamos el mensaje que llevará la trama 2
            mensaje2 = new Mensaje();
            
            // Mensaje
            codigoMensaje = "4_Servidor_Confirma_Asignacion";
            ipServidor = InetAddress.getLocalHost();

            // Lo unico diferente respecto a la trama recibida es el codigo del mensaje
            mensaje2.establecerAtributos(mensaje1.getIdCliente(), mensaje1.getIdServidor(), mensaje1.getIpCliente(), mensaje1.getIpServidor(),
                                            mensaje1.getNombreCliente(), mensaje1.getNombreServidor(), codigoMensaje, mensaje1.getCodigoServidorAceptado(),
                                            mensaje1.getNombreServidorAceptado(), mensaje1.getAccesoN(), mensaje1.getAsiento(), mensaje1.isAceptado(), 
                                            mensaje1.isEncontrado());

            // Codifica el mensaje
            byte[] servidor_confirma_asignacion = mensaje2.codificarMensaje();
            
            System.out.println("Mostrando mensaje antes del envio \n" + mensaje2.toString());
            
            // Creamos el socket por el que se enviará
            creaSocket();

            // Creamos el datagrama que se enviara
            envio = new DatagramPacket(servidor_confirma_asignacion, servidor_confirma_asignacion.length, ip, puerto);
            
            System.out.println("Enviando trama con ServidorThreadTrama3....");

            // Enviamos la trama
            socket.send(envio);

            // Cerramos el socket
            socket.close();

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
		//socketEnvio = new DatagramSocket();
		socket = new DatagramSocket(puerto);
	}

}
