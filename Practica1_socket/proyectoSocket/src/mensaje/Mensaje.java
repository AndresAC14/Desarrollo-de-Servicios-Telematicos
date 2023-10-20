package mensaje;

import java.net.*;

public class Mensaje {
	
	private int idCliente;
	private int idServidor;
	private InetAddress ip;
	private String nombreCliente;
	private String nombreServidor;
	private String codigoMensaje;
	private int accesoN;
	private int asiento;
	private boolean aceptado;
	
	public int getIdCliente() {	return idCliente; }
	public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
	
	public int getIdServidor() { return idServidor;	}
	public void setIdServidor(int idServidor) {	this.idServidor = idServidor; }
	
	public InetAddress getIp() { return ip; }
	public void setIp(InetAddress ip) { this.ip = ip; }
	
	public String getNombreCliente() { return nombreCliente; }
	public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
	
	public String getNombreServidor() { return nombreServidor; }
	public void setNombreServidor(String nombreServidor) { this.nombreServidor = nombreServidor; }
	
	public String getCodigoMensaje() { return codigoMensaje; }
	public void setCodigoMensaje(String codigoMensaje) { this.codigoMensaje = codigoMensaje; }
	
	public int getAccesoN() { return accesoN; }
	public void setAccesoN(int accesoN) { this.accesoN = accesoN; }
	
	public int getAsiento() { return asiento; }
	public void setAsiento(int asiento) { this.asiento = asiento; }
	
	public boolean isAceptado() { return aceptado;	}
	public void setAceptado(boolean aceptado) {	this.aceptado = aceptado; }
	
	
	
	
	
}
