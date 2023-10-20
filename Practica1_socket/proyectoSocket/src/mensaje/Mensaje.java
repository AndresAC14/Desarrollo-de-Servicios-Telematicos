package mensaje;

import java.net.*;
import java.io.*;

public class Mensaje {
	
	private int idCliente;
	private int idServidor;
	private InetAddress ipCliente;
	private InetAddress ipServidor;
	private String nombreCliente;
	private String nombreServidor;
	private String codigoMensaje;
	private int accesoN;
	private String asiento;
	private boolean aceptado;
	private boolean encontrado;
	
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
	
	public int getAccesoN() { return accesoN; }
	public void setAccesoN(int accesoN) { this.accesoN = accesoN; }
	
	public String getAsiento() { return asiento; }
	public void setAsiento(String asiento) { this.asiento = asiento; }
	
	public boolean isAceptado() { return aceptado;	}
	public void setAceptado(boolean aceptado) {	this.aceptado = aceptado; }

	public boolean isEncontrado() { return encontrado;	}
	public void setEncontrado(boolean encontrado) {	this.encontrado = encontrado; }
	
	
	public void establecerAtributos(int idCliente,int idServidor, 
		InetAddress ipCliente, InetAddress ipServidor, 
		String nombreCliente, String nombreServidor, String codigoMensaje, 
		int accesoN, String asiento, 
		boolean aceptado, boolean encontrado) {
					
			setIdCliente(idCliente);
			setIdServidor(idServidor);
			setIpCliente(ipCliente);
			setIpServidor(ipServidor);
			setNombreCliente(nombreCliente);
			setNombreServidor(nombreServidor);
			setCodigoMensaje(codigoMensaje);
			setAccesoN(accesoN);
			setAsiento(asiento);
			setAceptado(aceptado);
			setEncontrado(encontrado);
	}


	public byte[] codificarMensaje() throws IOException{

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		
		out.writeInt(this.idCliente);
		out.writeInt(this.idServidor);
		out.write(this.ipCliente.getAddress(), 0, 4);
		out.write(this.ipServidor.getAddress(), 0, 4);
		out.write(this.nombreCliente.getBytes(), 0, 64);
		out.write(this.nombreServidor.getBytes(), 0, 64);
		out.write(this.codigoMensaje.getBytes(), 0, 64);
		out.writeInt(this.accesoN);
		out.write(this.asiento.getBytes(), 0, 64);
		out.writeBoolean(aceptado);
		out.writeBoolean(encontrado);
		
		return byteStream.toByteArray();
	}
	
	public void decodificarMensaje(byte[] mensaje) throws IOException{

		ByteArrayInputStream byteStream = new ByteArrayInputStream(mensaje);
		DataInputStream in = new DataInputStream(byteStream);

		setIdCliente(in.readInt()) ;
		setIdServidor(in.readInt());
		setIpCliente(InetAddress.getByAddress(in.readNBytes(4)));
		setIpServidor(InetAddress.getByAddress(in.readNBytes(4)));
		setNombreCliente(new String(in.readNBytes(64)));
		setNombreServidor(new String(in.readNBytes(64)));
		setCodigoMensaje(new String(in.readNBytes(64)));
		setAccesoN(in.readInt());
		setAsiento(new String(in.readNBytes(64)));
		setAceptado(in.readBoolean());
		setEncontrado(in.readBoolean());

	}

	// Posible hacer un metodo para mostrar el contenido de la trama (forma de tabla o algo)
}
