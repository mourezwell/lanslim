package com.oz.lanslim.model;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.message.SlimTalkMessage;

public class SlimUserContact extends SlimContact implements Serializable {

	private static final Pattern USER_PATTERN = Pattern.compile(
			"([^" + HOST_SEPARATOR + "]*)" + HOST_SEPARATOR +  //$NON-NLS-1$ //$NON-NLS-2$
			"([^\\" + PORT_SEPARATOR + "]*)" + PORT_SEPARATOR + "([0-9]{1,6})");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	private String host = null;
	private int port = 0;

	private LinkedList messageQueue = null;
	
	public SlimUserContact(String pName, String pIp, String pPort) throws SlimException {
		super(pName);

		setPort(pPort);
		setHost(pIp);
		messageQueue = new LinkedList();
	}

	public synchronized SlimTalkMessage getOldestMessageInQueue() {
		SlimTalkMessage lMessage = null;
		try {
			lMessage = (SlimTalkMessage)messageQueue.removeFirst();
		}
		catch (NoSuchElementException lE) {
			// ignored  cause it means list is empty
		}
		return lMessage;
	}

	public synchronized void addMessageInQueue(SlimTalkMessage pMessage) {
		messageQueue.addLast(pMessage);
	}
	
	public boolean isGroup() {
		return false;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public void setHost(String pIp) throws SlimException {
		InetSocketAddress address = new InetSocketAddress(pIp, port);
		if (address.getAddress() == null) {
			throw new SlimException(Externalizer.getString("LANSLIM.161", pIp)); //$NON-NLS-1$
		}
		else if (address.getAddress().isMulticastAddress()) {
			throw new SlimException(Externalizer.getString("LANSLIM.162", pIp)); //$NON-NLS-1$
		}
		else if (address.getAddress().isLoopbackAddress() 
				&& !Boolean.getBoolean(SlimSettings.UNLOCK_PORT_SYSTEM_PROPERTY_KEY)) {
			throw new SlimException(Externalizer.getString("LANSLIM.163", pIp)); //$NON-NLS-1$
		} 
		else if (address.getAddress().isAnyLocalAddress()) {
			throw new SlimException(Externalizer.getString("LANSLIM.164", pIp)); //$NON-NLS-1$
		}
		else if (pIp.indexOf(PORT_SEPARATOR) > 0) {
			throw new SlimException(Externalizer.getString("LANSLIM.165", pIp, PORT_SEPARATOR)); //$NON-NLS-1$
		}
		else if (pIp.indexOf(FORMIDDEN_CHAR) > 0) {
			throw new SlimException(Externalizer.getString("LANSLIM.165", pIp, FORMIDDEN_CHAR)); //$NON-NLS-1$
		}
		host = pIp;
	}

	public void setPort(String pPort) throws SlimException {
		try {
			int lPort = Integer.parseInt(pPort);
			if (isValidPort(lPort)) {
				port = lPort;
			} 
			else {
				throw new SlimException(Externalizer.getString("LANSLIM.166", pPort)); //$NON-NLS-1$
			}
		}
		catch (NumberFormatException nfe) {
			throw new SlimException(Externalizer.getString("LANSLIM.166", pPort)); //$NON-NLS-1$
		}
	}

	public int hashCode() {
		return host.hashCode();
	}
	
	public boolean equals(Object pCompareTo) {
		
		if (pCompareTo instanceof SlimUserContact) {
			SlimUserContact suc = (SlimUserContact)pCompareTo;
			boolean lResult = super.equals(pCompareTo);
			return lResult && (port == suc.port) && host.equalsIgnoreCase(suc.host);
		}
		return false;
	}
	
	private boolean isValidPort(int pPort) {
		
		return (pPort > 0  && pPort < 65536);
	}
	
	public String toString() {
		return (getName() + HOST_SEPARATOR + host + PORT_SEPARATOR + port);
	}
	
	public static SlimUserContact fromString(String pString) throws SlimException {
		Matcher lMatch = USER_PATTERN.matcher(pString);
		if (lMatch.matches()) {
			return new SlimUserContact(lMatch.group(1), lMatch.group(2), lMatch.group(3));
		}
		throw new SlimException(Externalizer.getString("LANSLIM.49", pString)); //$NON-NLS-1$
	}

}
