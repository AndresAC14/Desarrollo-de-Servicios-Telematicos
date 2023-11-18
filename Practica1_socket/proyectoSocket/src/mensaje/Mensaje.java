package mensaje;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;

public class Mensaje {
	
	private int idCliente;
	private int idServidor; // Seria codigoServidor
	private InetAddress ipCliente;
	private InetAddress ipServidor;
	private String nombreCliente;
	private String nombreServidor;
	private String codigoMensaje;
	private int codigoServidorAceptado; // NUEVO
	private String nombreServidorAceptado; // NUEVO
	private int accesoN;
	private String asiento;
	private boolean aceptado; // Seria servidorAcepta 
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
        return "Mensaje{" +
                "idCliente=" + idCliente +
                ", idServidor=" + idServidor +
                ", ipCliente=" + ipCliente +
                ", ipServidor=" + ipServidor +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", nombreServidor='" + nombreServidor + '\'' +
                ", codigoMensaje='" + codigoMensaje + '\'' +
                ", codigoServidorAceptado=" + codigoServidorAceptado +
                ", nombreServidorAceptado='" + nombreServidorAceptado + '\'' +
                ", accesoN=" + accesoN +
                ", asiento='" + asiento + '\'' +
                ", aceptado=" + aceptado +
                ", encontrado=" + encontrado +
                '}';
    }

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
