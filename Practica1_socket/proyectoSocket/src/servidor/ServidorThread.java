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

            // Mostramos el mensaje
            mensaje1.toString();

            // PROCESAR FICHERO
            //1. Comprobar que el usuario se encuentra en el fichero
            //2. Contiene accesoN, asiento e identificador servidor
            //3. Poner como asignadas las credenciales que envia al cliente.
            // IMPORTANTE: Si no encuentra el identificador del cliente
            // en el fichero, manda mensaje "servidor_no_encuentra_cliente"
            // indicando que no existe en el fichero

            //mensaje2.establecerAtributos();

            byte[] servidor_ofrece_credencial = mensaje2.codificarMensaje();

            System.out.println("Enviando mensaje 2....");

        }catch(Exception e){
            e.printStackTrace();
        }   

    }

}
