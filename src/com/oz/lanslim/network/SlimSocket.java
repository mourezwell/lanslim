package com.oz.lanslim.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.message.SlimMessage;
import com.oz.lanslim.model.SlimUserContact;

public class SlimSocket {

	private static final int MAX_MESSAGE_SIZE = 32000;
	private DatagramSocket socket;
	
	public SlimSocket(String pHost, int pPort) throws SocketException {
		InetSocketAddress address = new InetSocketAddress(pHost, pPort);
		socket = new DatagramSocket(address);
	}

	public void close() {
		socket.close();
	}

	public SlimMessage receive() throws IOException {
		DatagramPacket dp = new DatagramPacket(new byte[MAX_MESSAGE_SIZE], MAX_MESSAGE_SIZE);
		socket.receive(dp);
		try {
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dp.getData()));
			try {
				SlimMessage sm = (SlimMessage)ois.readObject();
				return sm;
			}
			catch (ClassNotFoundException chfe){
				SlimLogger.log(chfe + ":" + chfe.getMessage() + " at SlimSocket.receive()");
				return null;
			}
			catch (ClassCastException cce){
				SlimLogger.log(cce + ":" + cce.getMessage() + " at SlimSocket.receive()");
				return null;
			}
		}
		catch (IOException ioe) {
			SlimLogger.log(ioe + ":" + ioe.getMessage() + " at SlimSocket.receive()");
			return null;
		}
	}
	
	public void send(SlimMessage pMessage, SlimUserContact pContact) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(MAX_MESSAGE_SIZE);
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(pMessage);
		byte[] ba = baos.toByteArray();
		DatagramPacket dp = new DatagramPacket(ba, ba.length);
		dp.setSocketAddress(new InetSocketAddress(pContact.getHost(), pContact.getPort()));
		socket.send(dp);
	}
	
}
