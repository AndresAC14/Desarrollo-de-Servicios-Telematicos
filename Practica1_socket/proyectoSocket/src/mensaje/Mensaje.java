package mensaje;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;

// Código realizado por Andres Amo Caballero

public class Mensaje {
	
	private int idCliente;
	private int idServidor; // Seria codigoServidor
	private InetAddress ipCliente;
	private InetAddress ipServidor;
	private String nombreCliente;
	private String nombreServidor;
	private String codigoMensaje;
	private int codigoServidorAceptado; 
	private String nombreServidorAceptado; 
	private int accesoN;
	private String asiento;
	private boolean aceptado; // Seria servidorAcepta 
	private boolean encontrado;

	// Constructor por defecto
    public Mensaje() {
        
    }

    // Constructor con parámetros para establecer los atributos al crear una instancia
    public Mensaje(int idCliente, int idServidor, InetAddress ipCliente, InetAddress ipServidor,
                   String nombreCliente, String nombreServidor, String codigoMensaje,
                   int codigoServidorAceptado, String nombreServidorAceptado,
                   int accesoN, String asiento, boolean aceptado, boolean encontrado) {

        this.idCliente = idCliente;
        this.idServidor = idServidor;
        this.ipCliente = ipCliente;
        this.ipServidor = ipServidor;
        this.nombreCliente = nombreCliente;
        this.nombreServidor = nombreServidor;
        this.codigoMensaje = codigoMensaje;
        this.codigoServidorAceptado = codigoServidorAceptado;
        this.nombreServidorAceptado = nombreServidorAceptado;
        this.accesoN = accesoN;
        this.asiento = asiento;
        this.aceptado = aceptado;
        this.encontrado = encontrado;
    }
	
	// Get y Set de cada variable
	public int getIdCliente() {	return idCliente; }
	public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
	
	public int getIdServidor() { return idServidor;	}
	public void setIdServidor(int idServidor) {	this.idServidor = idServidor; }
	
	public InetAddress getIpCliente() {	return ipCliente; }
	public void setIpCliente(InetAddress ipCliente) { this.ipCliente = ipCliente; }

	public InetAddress getIpServidor() { return ipServidor; }
	public void setIpServidor(InetAddress ipServidor) { this.ipServidor = ipServidor; }
	
	public String getNombreCliente() { return nombreCliente; }
	public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
	
	public String getNombreServidor() { return nombreServidor; }
	public void setNombreServidor(String nombreServidor) { this.nombreServidor = nombreServidor; }
	
	public String getCodigoMensaje() { return codigoMensaje; }
	public void setCodigoMensaje(String codigoMensaje) { this.codigoMensaje = codigoMensaje; }

	public int getCodigoServidorAceptado() {	return codigoServidorAceptado; }
	public void setCodigoServidorAceptado(int codigoServidorAceptado) { this.codigoServidorAceptado = codigoServidorAceptado; }

	public String getNombreServidorAceptado() { return nombreServidorAceptado; }
	public void setNombreServidorAceptado(String nombreServidorAceptado) { this.nombreServidorAceptado = nombreServidorAceptado; }
	
	public int getAccesoN() { return accesoN; }
	public void setAccesoN(int accesoN) { this.accesoN = accesoN; }
	
	public String getAsiento() { return asiento; }
	public void setAsiento(String asiento) { this.asiento = asiento; }
	
	public boolean isAceptado() { return aceptado;	}
	public void setAceptado(boolean aceptado) {	this.aceptado = aceptado; }

	public boolean isEncontrado() { return encontrado;	}
	public void setEncontrado(boolean encontrado) {	this.encontrado = encontrado; }
	
	// Establece todos los atributos de la trama
	public void establecerAtributos(int idCliente,int idServidor, 
		InetAddress ipCliente, InetAddress ipServidor, 
		String nombreCliente, String nombreServidor, 
		String codigoMensaje, int codigoServidorAceptado, String nombreServidorAceptado, 
		int accesoN, String asiento, 
		boolean aceptado, boolean encontrado) {
					
			setIdCliente(idCliente);
			setIdServidor(idServidor);
			setIpCliente(ipCliente);
			setIpServidor(ipServidor);
			setNombreCliente(nombreCliente);
			setNombreServidor(nombreServidor);
			setCodigoMensaje(codigoMensaje);
			setCodigoServidorAceptado(codigoServidorAceptado);
			setNombreServidorAceptado(nombreServidorAceptado);
			setAccesoN(accesoN);
			setAsiento(asiento);
			setAceptado(aceptado);
			setEncontrado(encontrado);
	}

	// Codifica la trama
	public byte[] codificarMensaje() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteStream);

        out.writeInt(this.idCliente);
        out.writeInt(this.idServidor);

        out.write(this.ipCliente.getAddress(), 0, this.ipCliente.getAddress().length);
        out.write(this.ipServidor.getAddress(), 0, this.ipServidor.getAddress().length);

        writeFixedLengthString(out, this.nombreCliente, 64);
        writeFixedLengthString(out, this.nombreServidor, 64);
        writeFixedLengthString(out, this.codigoMensaje, 64);
        writeFixedLengthString(out, this.nombreServidorAceptado, 64);
        writeFixedLengthString(out, this.asiento, 64);

        out.writeInt(this.codigoServidorAceptado);
        out.writeInt(this.accesoN);
        out.writeBoolean(this.aceptado);
        out.writeBoolean(this.encontrado);

        return byteStream.toByteArray();
    }

	// Decodifica la trama
    public void decodificarMensaje(byte[] mensaje) throws IOException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(mensaje);
        DataInputStream in = new DataInputStream(byteStream);

        setIdCliente(in.readInt());
        setIdServidor(in.readInt());

        byte[] ipClienteBytes = new byte[4];
        in.readFully(ipClienteBytes);
        setIpCliente(InetAddress.getByAddress(ipClienteBytes));

        byte[] ipServidorBytes = new byte[4];
        in.readFully(ipServidorBytes);
        setIpServidor(InetAddress.getByAddress(ipServidorBytes));

        setNombreCliente(readFixedLengthString(in, 64));
        setNombreServidor(readFixedLengthString(in, 64));
        setCodigoMensaje(readFixedLengthString(in, 64));
        setNombreServidorAceptado(readFixedLengthString(in, 64));
        setAsiento(readFixedLengthString(in, 64));

        setCodigoServidorAceptado(in.readInt());
        setAccesoN(in.readInt());
        setAceptado(in.readBoolean());
        setEncontrado(in.readBoolean());
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
        sb.append(String.format("| %-30s | %-40s |%n", "Atributo", "Valor"));
        sb.append("|----------------------------------------------------------------------|\n");
        sb.append(String.format("| %-30s | %-40d |%n", "idCliente", idCliente));
        sb.append(String.format("| %-30s | %-40d |%n", "idServidor", idServidor));
        sb.append(String.format("| %-30s | %-40s |%n", "ipCliente", ipCliente));
        sb.append(String.format("| %-30s | %-40s |%n", "ipServidor", ipServidor));
        sb.append(String.format("| %-30s | %-40s |%n", "nombreCliente", nombreCliente));
        sb.append(String.format("| %-30s | %-40s |%n", "nombreServidor", nombreServidor));
        sb.append(String.format("| %-30s | %-40s |%n", "codigoMensaje", codigoMensaje));
        sb.append(String.format("| %-30s | %-40d |%n", "codigoServidorAceptado", codigoServidorAceptado));
        sb.append(String.format("| %-30s | %-40s |%n", "nombreServidorAceptado", nombreServidorAceptado));
        sb.append(String.format("| %-30s | %-40d |%n", "accesoN", accesoN));
        sb.append(String.format("| %-30s | %-40s |%n", "asiento", asiento));
        sb.append(String.format("| %-30s | %-40b |%n", "aceptado", aceptado));
        sb.append(String.format("| %-30s | %-40b |%n", "encontrado", encontrado));
        sb.append("|----------------------------------------------------------------------|\n");
        return sb.toString();
    }

	// Metodos auxiliares para la correcta codificacion y decodificacion de la trama
    private void writeFixedLengthString(DataOutputStream out, String s, int length) throws IOException {
        byte[] bytes = new byte[length];
        byte[] stringBytes = s.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(stringBytes, 0, bytes, 0, Math.min(stringBytes.length, length));
        out.write(bytes, 0, length);
    }

    private String readFixedLengthString(DataInputStream in, int length) throws IOException {
        byte[] bytes = new byte[length];
        in.readFully(bytes);
        return new String(bytes, StandardCharsets.UTF_8).trim();
    }
}
