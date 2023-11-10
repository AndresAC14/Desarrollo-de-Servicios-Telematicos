package servidor;

import java.io.*;
import java.net.*;
import java.util.*;
import mensaje.Mensaje;

public class ServidorThread extends Thread{

    private DatagramPacket paquete;
    private Mensaje mensaje1;
    private Mensaje mensaje2;

    public ServidorThread(DatagramPacket paquete){

        this.paquete = paquete;

    }
    
    public void run(){

        try{
            // Decodificamos el mensaje
            mensaje1.decodificarMensaje(paquete.getData());

            // Primero pillar el id de servidor -> 
            // si esta asignado y es distinto poner como disponibles las credenciales que ofrecieron
            // si esta asignado y es igual, mandar mensaje con 4.Servidor confirma asignacion

            // Mostramos el mensaje
            mensaje1.toString();

            // PROCESAR FICHERO
            //1. Comprobar que el id usuario se encuentra en el fichero
            // IMPORTANTE: Si no encuentra el identificador del cliente en el fichero, manda mensaje "5.servidor_no_encuentra_cliente"
            //2. Mensaje 2 -> Contiene accesoN, asiento e identificador servidor
            //3. Poner como asignadas las credenciales que envia al cliente.
            
            //mensaje2.establecerAtributos();

            byte[] servidor_ofrece_credencial = mensaje2.codificarMensaje();

            System.out.println("Enviando mensaje 2....");

        }catch(Exception e){
            e.printStackTrace();
        }   

    }

}
