package com.oz.lanslim.model;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.oz.lanslim.SlimException;

public class SlimUserContact extends SlimContact implements Serializable {

	private static final Pattern USER_PATTERN = Pattern.compile(
			"([^" + HOST_SEPARATOR + "]*)" + HOST_SEPARATOR + 
			"([^\\" + PORT_SEPARATOR + "]*)" + PORT_SEPARATOR + "([0-9]{1,6})");  

	private String host = null;

	private int port = 0;
	
	public SlimUserContact(String pName, String pIp, String pPort) throws SlimException {
		super(pName);

		setPort(pPort);
		setHost(pIp);
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
			throw new SlimException(pIp + " can't be resolved");
		}
		else if (address.getAddress().isMulticastAddress()) {
			throw new SlimException(pIp + " must not be multicast address");
		}
		else if (address.getAddress().isLoopbackAddress() 
				&& !Boolean.getBoolean(SlimSettings.UNLOCK_PORT_SYTEM_PROPERTY_KEY)) {
			throw new SlimException(pIp + " must not be loopback address");
		} 
		else if (address.getAddress().isAnyLocalAddress()) {
			throw new SlimException(pIp + " must not be wildcard address");
		}
		else if (pIp.indexOf(PORT_SEPARATOR) > 0) {
			throw new SlimException(pIp + " must not contain character " + PORT_SEPARATOR );
		}
		else if (pIp.indexOf(FORMIDDEN_CHAR) > 0) {
			throw new SlimException(pIp + " must not contain character " + FORMIDDEN_CHAR );
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
				throw new SlimException(pPort + " is not valid port");
			}
		}
		catch (NumberFormatException nfe) {
			throw new SlimException(pPort + " is not valid port");
		}
	}

	public int hashCode() {
		return host.hashCode();
	}
	
	public boolean equals(Object pCompareTo) {
		
		if (pCompareTo instanceof SlimUserContact) {
			SlimUserContact suc = (SlimUserContact)pCompareTo;
			boolean lResult = super.equals(pCompareTo);
			return lResult && (port == suc.port) && host.equals(suc.host);
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
		throw new SlimException(pString + " does not match user pattern ");
	}

}
