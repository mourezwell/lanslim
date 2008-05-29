package com.oz.lanslim.model;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;

import com.oz.lanslim.SlimException;

public class SlimSettings {
	
	protected static final String UNLOCK_PORT_SYTEM_PROPERTY_KEY = "unlockPort";
	
	private static final String SLIM_SETTINGS_PROPS_PREFIX = "slim.settings.";
	private static final String NAME_PROP = SLIM_SETTINGS_PROPS_PREFIX + "name";
	private static final String HOST_PROP = SLIM_SETTINGS_PROPS_PREFIX + "host";
	private static final String PORT_PROP = SLIM_SETTINGS_PROPS_PREFIX + "port";
	private static final String COLOR_PROP = SLIM_SETTINGS_PROPS_PREFIX + "color";
	private static final String START_PROP = SLIM_SETTINGS_PROPS_PREFIX + "startAsTray";
	private static final String CLOSE_PROP = SLIM_SETTINGS_PROPS_PREFIX + "closeAsTray";
	private static final String FORCED_PROP = SLIM_SETTINGS_PROPS_PREFIX + "closeForced";
	private static final String HIDEGRP_PROP = SLIM_SETTINGS_PROPS_PREFIX + "hideGroup";
	private static final String HIDEOFF_PROP = SLIM_SETTINGS_PROPS_PREFIX + "hideOffline";

	public static final String DEFAULT_PORT = "17000";
	
	private static final String DEFAULT_NAME = "yourAlias";
	private static final String DEFAULT_COLOR = "000000";
	private static final boolean DEFAULT_START = false;
	private static final boolean DEFAULT_STOP = false;
	private static final boolean DEFAULT_FORCED = false;
	private static final boolean DEFAULT_TRAY_ENABLE = true;
	private static final boolean DEFAULT_NETWORK_VALID = false;
	private static final boolean DEFAULT_UNLOCK_PORT = false;
	private static final boolean DEFAULT_HIDE_GROUP = false;
	private static final boolean DEFAULT_HIDE_OFFLINE = false;
	
	private  SlimModel model = null;
	private  String color = DEFAULT_COLOR;
	
	private  boolean networkValid = DEFAULT_NETWORK_VALID;
	private  boolean unlockPort = DEFAULT_UNLOCK_PORT;
	
	private  boolean trayEnable = DEFAULT_TRAY_ENABLE;
	private  boolean startAsTrayForced = DEFAULT_START;
	private  boolean closeAsTrayForced = DEFAULT_STOP;
	private  boolean closeForced = DEFAULT_FORCED;
	private  boolean groupHidden = DEFAULT_HIDE_GROUP;
	private  boolean offlineHidden = DEFAULT_HIDE_OFFLINE;
	
	private SlimUserContact contactInfo = null;

	
	public SlimSettings(SlimModel pModel) throws SlimException, UnknownHostException {
		
		contactInfo = new SlimUserContact(DEFAULT_NAME, InetAddress.getLocalHost().getHostName(), DEFAULT_PORT);
		color = DEFAULT_COLOR;
		model = pModel;
		networkValid = DEFAULT_NETWORK_VALID;
		unlockPort = Boolean.getBoolean(UNLOCK_PORT_SYTEM_PROPERTY_KEY);
		trayEnable = DEFAULT_TRAY_ENABLE;
		startAsTrayForced = DEFAULT_START;
		closeAsTrayForced = DEFAULT_STOP;
		closeForced = DEFAULT_FORCED;
		groupHidden = DEFAULT_HIDE_GROUP;
		offlineHidden = DEFAULT_HIDE_OFFLINE;
	}

	public boolean isPortUnlocked() {
		return unlockPort;
	}
	
	public SlimSettings(SlimModel pModel, Properties p) throws SlimException, UnknownHostException {
		this(pModel);
		String lTemp = p.getProperty(NAME_PROP);
		try {
			contactInfo.setName(lTemp);
		}
		catch (SlimException se) {
			// ignore start exception
		}
		
		lTemp = p.getProperty(HOST_PROP);
		try {
			contactInfo.setHost(lTemp);
		}
		catch (SlimException se) {
			// ignore start exception
		}
	
		if (unlockPort) {
			lTemp = p.getProperty(PORT_PROP);
			try {
				contactInfo.setPort(lTemp);
			}
			catch (SlimException se) {
				// ignore start exception
			}
		}
		
		lTemp = p.getProperty(COLOR_PROP);
		if (lTemp != null) {
			try {
				setColor(lTemp);
			}
			catch (SlimException se) {
				// ignore start exception
			}
		}
		
		lTemp = p.getProperty(START_PROP);
		if (lTemp != null) {
			setStartAsTrayForced(Boolean.valueOf(lTemp).booleanValue());
		}

		lTemp = p.getProperty(CLOSE_PROP);
		if (lTemp != null) {
			setCloseAsTrayForced(Boolean.valueOf(lTemp).booleanValue());
		}

		lTemp = p.getProperty(FORCED_PROP);
		if (lTemp != null) {
			setCloseForced(Boolean.valueOf(lTemp).booleanValue());
		}

		lTemp = p.getProperty(HIDEGRP_PROP);
		if (lTemp != null) {
			setGroupHidden(Boolean.valueOf(lTemp).booleanValue());
		}
		
		lTemp = p.getProperty(HIDEOFF_PROP);
		if (lTemp != null) {
			setOfflineHidden(Boolean.valueOf(lTemp).booleanValue());
		}
		
	}

	public void updateSettings(SlimUserContact pContact) throws SlimException {
		
		try {
			boolean testNetwork = !networkValid;
			if (!pContact.getHost().equals(contactInfo.getHost()) || pContact.getPort() != contactInfo.getPort()) {
				testNetwork = true;
			}
			if (areValidSettings(pContact, testNetwork)) {
				SlimUserContact pOld = new SlimUserContact(contactInfo.getName(), contactInfo.getHost(), 
						Integer.toString(contactInfo.getPort()));
	
				contactInfo.setName(pContact.getName());
				contactInfo.setHost(pContact.getHost());
				contactInfo.setPort(Integer.toString(pContact.getPort()));
				  
				model.getContacts().updateContact(pOld, contactInfo);
				model.getContacts().sendUpdateUserMessage(pOld);
				try {
					model.storeSettings();
				}
				catch (IOException ioe) {
					// ignore it during runtime, exception logged at exit
				}
			}
			else {
				throw new SlimException("Invalid Name you should not use default");
			}
		}
		catch (SocketException se) {
			throw new SlimException("Unbale to connect socket due to " + se.getMessage(), se);
		}
	}
	
	public boolean areValidSettings() {
		try {
			return areValidSettings(contactInfo, !networkValid);
		}
		catch (SocketException se) {
			return false;
			// no need to explain why
		}
	}
	
	public boolean areValidSettings(SlimUserContact pContact, boolean testNetwork) throws SocketException {
		
		if (testNetwork) {
			DatagramSocket s = new DatagramSocket(new InetSocketAddress(pContact.getHost(), pContact.getPort()));
			s.close();
			networkValid = true;
		}
		return (!pContact.getName().equals(DEFAULT_NAME)); 
	}
	
	public Properties toProperties() {
		
		Properties p = new Properties();
		p.put(NAME_PROP, contactInfo.getName());
		p.put(PORT_PROP, Integer.toString(contactInfo.getPort()));
		p.put(HOST_PROP, contactInfo.getHost());
		p.put(COLOR_PROP, getColor());
		p.put(START_PROP, Boolean.toString(isStartAsTrayForced()));
		p.put(CLOSE_PROP, Boolean.toString(isCloseAsTrayForced()));
		p.put(FORCED_PROP, Boolean.toString(isCloseForced()));
		p.put(HIDEGRP_PROP, Boolean.toString(isGroupHidden()));
		p.put(HIDEOFF_PROP, Boolean.toString(isOfflineHidden()));

		return p;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String pColor) throws SlimException {
		if (pColor.matches("[0-9A-F]{6}")) {
			color = pColor;
			try {
				model.storeSettings();
			}
			catch (IOException ioe) {
				// ignore it during runtime, exception logged at exit
			}

		}
		else {
			throw new SlimException("Color does not match pattern [0-9A-F]{6} : "  + pColor);
		}
	}

	public boolean isCloseAsTrayForced() {
		return closeAsTrayForced;
	}

	public void setCloseAsTrayForced(boolean closeAsTrayForced) {
		this.closeAsTrayForced = closeAsTrayForced;
		try {
			model.storeSettings();
		}
		catch (IOException ioe) {
			// ignore it during runtime, exception logged at exit
		}
	}

	public boolean isCloseForced() {
		return closeForced;
	}

	public void setCloseForced(boolean closeForced) {
		this.closeForced = closeForced;
		try {
			model.storeSettings();
		}
		catch (IOException ioe) {
			// ignore it during runtime, exception logged at exit
		}
	}

	public boolean isStartAsTrayForced() {
		return startAsTrayForced;
	}

	public void setStartAsTrayForced(boolean startAsTrayForced) {
		this.startAsTrayForced = startAsTrayForced;
		try {
			model.storeSettings();
		}
		catch (IOException ioe) {
			// ignore it during runtime, exception logged at exit
		}
	}

	public boolean isTrayEnable() {
		return trayEnable;
	}

	public void setTrayEnable(boolean trayEnable) {
		this.trayEnable = trayEnable;
	}

	public boolean isGroupHidden() {
		return groupHidden;
	}

	public void setGroupHidden(boolean groupHidden) {
		this.groupHidden = groupHidden;
		try {
			model.storeSettings();
		}
		catch (IOException ioe) {
			// ignore it during runtime, exception logged at exit
		}
	}

	public boolean isOfflineHidden() {
		return offlineHidden;
	}

	public void setOfflineHidden(boolean offlineHidden) {
		this.offlineHidden = offlineHidden;
		try {
			model.storeSettings();
		}
		catch (IOException ioe) {
			// ignore it during runtime, exception logged at exit
		}
	}

	public SlimUserContact getContactInfo() {
		return contactInfo;
	}

}
