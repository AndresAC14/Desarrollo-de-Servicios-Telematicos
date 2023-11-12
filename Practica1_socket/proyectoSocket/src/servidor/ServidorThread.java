package servidor;

import java.io.*;
import java.io.FileReader;
import java.net.*;
import java.util.*;
import mensaje.Mensaje;

public class ServidorThread extends Thread{

    private DatagramPacket recibido;
    private DatagramPacket envio;
    private Mensaje mensaje1;
    private Mensaje mensaje2;

    public ServidorThread(DatagramPacket recibido){

        this.recibido = recibido;

    }
    
    public void run(){

        try{
            // Decodificamos el mensaje
            mensaje1.decodificarMensaje(recibido.getData());

            // Primero pillar el id de servidor -> 
            int idServidor = mensaje1.getIdServidor();
            // si esta asignado y es distinto poner como disponibles las credenciales que ofrecieron
            // si esta asignado y es igual, mandar mensaje con 4.Servidor confirma asignacion

            // Mostramos el mensaje
            mensaje1.toString();

            //1. Comprobar que el id cliente se encuentra en el fichero
            int idCliente = mensaje1.getIdCliente();
            boolean encontrado = false;

            // PROCESAR FICHERO
            try(Scanner sc = new Scanner("BaseDatos.txt")) {
                while(sc.hasNextLine()){
                    String linea = sc.nextLine();
                    
                    String[] partes = linea.split(";");

                    //2. Mensaje 2 -> Contiene accesoN, asiento e identificador servidor
                    int accesoN = Integer.parseInt(partes[0]);
                    String asiento = partes[1];
                    // Segun chatGPT se obtiene asi, sino pone del tiron
                    char asignado = partes[2].charAt(0); 

                    
                     
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            
            // IMPORTANTE: Si no encuentra el identificador del cliente en el fichero, manda mensaje "5.servidor_no_encuentra_cliente"
            //3. Poner como asignadas las credenciales que envia al cliente.
            
            //mensaje2.establecerAtributos();

            byte[] servidor_ofrece_credencial = mensaje2.codificarMensaje();

            System.out.println("Enviando mensaje con Hebra....");

            DatagramSocket socket = new DatagramSocket();
            socket.send(envio);

        }catch(Exception e){
            e.printStackTrace();
        }   

    }

}
