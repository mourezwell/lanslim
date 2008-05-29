package com.oz.lanslim.model;

import java.io.Serializable;
import java.net.InetSocketAddress;

import com.oz.lanslim.SlimException;

public class SlimUserContact extends SlimContact implements Serializable {

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
		return (getName()+"@"+host+":"+port);
	}
}
