package com.oz.lanslim.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Map;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.message.SlimAvailabilityUserMessage;
import com.oz.lanslim.message.SlimExcludeTalkMessage;
import com.oz.lanslim.message.SlimExitTalkMessage;
import com.oz.lanslim.message.SlimInviteTalkMessage;
import com.oz.lanslim.message.SlimMessage;
import com.oz.lanslim.message.SlimMessageTypeEnum;
import com.oz.lanslim.message.SlimNewTalkMessage;
import com.oz.lanslim.message.SlimUpdateTalkMessage;
import com.oz.lanslim.message.SlimUpdateUserMessage;
import com.oz.lanslim.model.SlimUserContact;

public class SlimSocket {

	private static final int MAX_MESSAGE_SIZE = 32767;
	private DatagramSocket socket;
	
	public SlimSocket(String pHost, int pPort) throws SocketException {
		InetSocketAddress address = new InetSocketAddress(pHost, pPort);
		socket = new DatagramSocket(address);
	}

	public void close() {
		socket.close();
	}
	
/* Serilaization Solution 
 
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
				SlimLogger.logException("socket.receive", chfe);
				return null;
			}
			catch (ClassCastException cce){
				SlimLogger.logException("socket.receive", cce);
				return null;
			}
		}
		catch (IOException ioe) {
			SlimLogger.logException("socket.receive", ioe);
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
 */

	/* without serialization */
	public SlimMessage receive() throws IOException {
		DatagramPacket dp = new DatagramPacket(new byte[MAX_MESSAGE_SIZE], MAX_MESSAGE_SIZE);
		socket.receive(dp);
		String lMessage = null;
		try {
			lMessage = new String(dp.getData(), 0, dp.getLength(), "ISO-8859-1");
			Map lItems = SlimMessage.itemsFromString(lMessage);
			SlimMessageTypeEnum type = SlimMessage.getType(lItems);
			
			if (type == SlimMessageTypeEnum.AVAILABILITY) {
				return SlimAvailabilityUserMessage.fromStringItems(lItems);
			} 
			else if (type == SlimMessageTypeEnum.EXCLUDE_TALK){
				return SlimExcludeTalkMessage.fromStringItems(lItems);
			}				
			else if (type == SlimMessageTypeEnum.EXIT_TALK) { 
				return SlimExitTalkMessage.fromStringItems(lItems);
			}
			else if (type == SlimMessageTypeEnum.INVITE_TALK){
				return SlimInviteTalkMessage.fromStringItems(lItems);
			}				
			else if (type == SlimMessageTypeEnum.NEW_TALK){
				return SlimNewTalkMessage.fromStringItems(lItems);
			}				
			else if (type == SlimMessageTypeEnum.UPDATE_USER){
				return SlimUpdateUserMessage.fromStringItems(lItems);
			}				
			else if (type == SlimMessageTypeEnum.UPDATE_TALK){
				return SlimUpdateTalkMessage.fromStringItems(lItems);
			}				
			else {
				SlimLogger.log(Externalizer.getString("LANSLIM.38", type)); //$NON-NLS-1$
				return null;
			}
		}
		catch (Throwable e) {
			SlimLogger.logException(Externalizer.getString("LANSLIM.39", lMessage), e); //$NON-NLS-1$
			return null;
		}
	}

	public void send(SlimMessage pMessage, SlimUserContact pContact) throws IOException {
		String lMessageAstring = pMessage.toString();
		byte[] ba = lMessageAstring.getBytes("ISO-8859-1");
		if (ba.length > MAX_MESSAGE_SIZE) {
			throw new IOException(Externalizer.getString("LANSLIM.29"));
		}
		DatagramPacket dp = new DatagramPacket(ba, ba.length);
		dp.setSocketAddress(new InetSocketAddress(pContact.getHost(), pContact.getPort()));
		socket.send(dp);
	}

	/* */
}
