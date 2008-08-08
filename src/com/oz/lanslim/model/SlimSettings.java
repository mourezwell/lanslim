package com.oz.lanslim.model;

import java.awt.Dimension;
import java.awt.Point;
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
	private static final String UNDERLINE_PROP = SLIM_SETTINGS_PROPS_PREFIX + "underline";
	private static final String ITALIC_PROP = SLIM_SETTINGS_PROPS_PREFIX + "italic";
	private static final String BOLD_PROP = SLIM_SETTINGS_PROPS_PREFIX + "bold";
	private static final String SIZE_PROP = SLIM_SETTINGS_PROPS_PREFIX + "size";
	private static final String START_PROP = SLIM_SETTINGS_PROPS_PREFIX + "startAsTray";
	private static final String CLOSE_PROP = SLIM_SETTINGS_PROPS_PREFIX + "closeAsTray";
	private static final String HIDEGRP_PROP = SLIM_SETTINGS_PROPS_PREFIX + "hideGroup";
	private static final String HIDEOFF_PROP = SLIM_SETTINGS_PROPS_PREFIX + "hideOffline";
	private static final String CONTACT_TREE_PROP = SLIM_SETTINGS_PROPS_PREFIX + "contactTree";
	private static final String CONTACT_QUICK_DND = SLIM_SETTINGS_PROPS_PREFIX + "quickDND";
	private static final String FRAME_X = SLIM_SETTINGS_PROPS_PREFIX + "x";
	private static final String FRAME_Y = SLIM_SETTINGS_PROPS_PREFIX + "x";
	private static final String FRAME_W = SLIM_SETTINGS_PROPS_PREFIX + "w";
	private static final String FRAME_H = SLIM_SETTINGS_PROPS_PREFIX + "h";

	public static final String DEFAULT_PORT = "17000";
	
	private static final String DEFAULT_NAME = "yourAlias";
	private static final String DEFAULT_COLOR = "000000";
	private static final int DEFAULT_SIZE = 4;
	private static final boolean DEFAULT_BOLD = false;
	private static final boolean DEFAULT_ITALIC = false;
	private static final boolean DEFAULT_UNDERLINE = false;
	private static final boolean DEFAULT_START = false;
	private static final boolean DEFAULT_STOP = false;
	private static final boolean DEFAULT_TRAY_ENABLE = true;
	private static final boolean DEFAULT_NETWORK_VALID = false;
	private static final boolean DEFAULT_UNLOCK_PORT = false;
	private static final boolean DEFAULT_HIDE_GROUP = false;
	private static final boolean DEFAULT_HIDE_OFFLINE = false;
	private static final boolean DEFAULT_CONTACT_TREE = false;
	private static final boolean DEFAULT_CONTACT_DND = false;
	private static final int DEFAULT_X = 200;
	private static final int DEFAULT_Y = 200;
	private static final int DEFAULT_W = 600;
	private static final int DEFAULT_H = 400;
	
	private  SlimModel model = null;
	private  String color = DEFAULT_COLOR;
	private  int size = DEFAULT_SIZE;
	private  boolean underline = DEFAULT_UNDERLINE;
	private  boolean italic = DEFAULT_ITALIC;
	private  boolean bold = DEFAULT_BOLD;
	
	private  boolean networkValid = DEFAULT_NETWORK_VALID;
	private  boolean unlockPort = DEFAULT_UNLOCK_PORT;
	
	private  boolean trayEnable = DEFAULT_TRAY_ENABLE;
	private  boolean startAsTray = DEFAULT_START;
	private  boolean closeAsTray = DEFAULT_STOP;
	private  boolean groupHidden = DEFAULT_HIDE_GROUP;
	private  boolean offlineHidden = DEFAULT_HIDE_OFFLINE;
	private  boolean contactTree = DEFAULT_CONTACT_TREE;
	private  boolean contactQuickDnd= DEFAULT_CONTACT_DND;
	private  int x = DEFAULT_X;
	private  int y = DEFAULT_Y;
	private  int w = DEFAULT_W;
	private  int h = DEFAULT_H;
	
	private SlimUserContact contactInfo = null;

	private ContactViewListener contactViewListener = null;

	private boolean initOk = false;
	
	public SlimSettings(SlimModel pModel) throws SlimException, UnknownHostException {
		
		contactInfo = new SlimUserContact(DEFAULT_NAME, InetAddress.getLocalHost().getHostName(), DEFAULT_PORT);
		contactInfo.setAvailability(SlimAvailabilityEnum.OFFLINE);
		color = DEFAULT_COLOR;
		size = DEFAULT_SIZE;
		underline = DEFAULT_UNDERLINE;
		italic = DEFAULT_ITALIC;
		bold = DEFAULT_BOLD;
		
		model = pModel;
		networkValid = DEFAULT_NETWORK_VALID;
		unlockPort = Boolean.getBoolean(UNLOCK_PORT_SYTEM_PROPERTY_KEY);
		trayEnable = DEFAULT_TRAY_ENABLE;
		startAsTray = DEFAULT_START;
		closeAsTray = DEFAULT_STOP;
		groupHidden = DEFAULT_HIDE_GROUP;
		offlineHidden = DEFAULT_HIDE_OFFLINE;
		contactTree = DEFAULT_CONTACT_TREE;
		contactQuickDnd = DEFAULT_CONTACT_DND;
		x = DEFAULT_X;
		y = DEFAULT_Y;
		w = DEFAULT_W;
		h = DEFAULT_H;
		
		initOk = true;
	}

	public SlimSettings(SlimModel pModel, Properties p) throws SlimException, UnknownHostException {
		this(pModel);
		initOk = false;
		
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

		lTemp = p.getProperty(SIZE_PROP);
		if (lTemp != null) {
			try {
				setFontSize(Integer.parseInt(lTemp));
			}
			catch (SlimException se) {
				// ignore start exception
			}
		}

		lTemp = p.getProperty(BOLD_PROP);
		if (lTemp != null) {
			setBold(Boolean.valueOf(lTemp).booleanValue());
		}

		lTemp = p.getProperty(ITALIC_PROP);
		if (lTemp != null) {
			setItalic(Boolean.valueOf(lTemp).booleanValue());
		}

		lTemp = p.getProperty(UNDERLINE_PROP);
		if (lTemp != null) {
			setUnderline(Boolean.valueOf(lTemp).booleanValue());
		}

		lTemp = p.getProperty(START_PROP);
		if (lTemp != null) {
			setStartAsTray(Boolean.valueOf(lTemp).booleanValue());
		}

		lTemp = p.getProperty(CLOSE_PROP);
		if (lTemp != null) {
			setCloseAsTray(Boolean.valueOf(lTemp).booleanValue());
		}

		lTemp = p.getProperty(HIDEGRP_PROP);
		if (lTemp != null) {
			setGroupHidden(Boolean.valueOf(lTemp).booleanValue());
		}
		
		lTemp = p.getProperty(HIDEOFF_PROP);
		if (lTemp != null) {
			setOfflineHidden(Boolean.valueOf(lTemp).booleanValue());
		}
		
		lTemp = p.getProperty(CONTACT_TREE_PROP);
		if (lTemp != null) {
			setContactTreeView(Boolean.valueOf(lTemp).booleanValue());
		}

		lTemp = p.getProperty(CONTACT_QUICK_DND);
		if (lTemp != null) {
			setQuickDnd(Boolean.valueOf(lTemp).booleanValue());
		}
		
		lTemp = p.getProperty(FRAME_H);
		if (lTemp != null) {
			setH(Integer.parseInt(lTemp));
		}
		lTemp = p.getProperty(FRAME_W);
		if (lTemp != null) {
			setW(Integer.parseInt(lTemp));
		}
		lTemp = p.getProperty(FRAME_X);
		if (lTemp != null) {
			setX(Integer.parseInt(lTemp));
		}
		lTemp = p.getProperty(FRAME_Y);
		if (lTemp != null) {
			setY(Integer.parseInt(lTemp));
		}
		
		initOk = true;
		saveSettings();
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
				
				boolean lUpdated = model.getContacts().updateContact(contactInfo, pContact);
				  
				if (!lUpdated) {
					throw new SlimException("UserName already declared please remove it first or choose another one");
				}
				model.getContacts().sendUpdateUserMessage(pOld);
				model.storeSettings();
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
		p.put(SIZE_PROP, Integer.toString(getFontSize()));
		p.put(BOLD_PROP, Boolean.toString(isBold()));
		p.put(ITALIC_PROP, Boolean.toString(isItalic()));
		p.put(UNDERLINE_PROP, Boolean.toString(isUnderline()));
		p.put(START_PROP, Boolean.toString(isStartAsTray()));
		p.put(CLOSE_PROP, Boolean.toString(isCloseAsTray()));
		p.put(HIDEGRP_PROP, Boolean.toString(isGroupHidden()));
		p.put(HIDEOFF_PROP, Boolean.toString(isOfflineHidden()));
		p.put(CONTACT_TREE_PROP, Boolean.toString(isContactTreeView()));
		p.put(CONTACT_QUICK_DND, Boolean.toString(isQuickDnd()));
		p.put(FRAME_H, Integer.toString(getH()));
		p.put(FRAME_W, Integer.toString(getW()));
		p.put(FRAME_X, Integer.toString(getX()));
		p.put(FRAME_Y, Integer.toString(getY()));

		return p;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String pColor) throws SlimException {
		if (pColor.matches("[0-9A-F]{6}")) {
			color = pColor;
			saveSettings();
		}
		else {
			throw new SlimException("Color does not match pattern [0-9A-F]{6} : "  + pColor);
		}
	}

	public boolean isCloseAsTray() {
		return closeAsTray;
	}

	public void setCloseAsTray(boolean pCloseAsTray) {
		closeAsTray = pCloseAsTray;
		saveSettings();
	}

	public boolean isStartAsTray() {
		return startAsTray;
	}

	public void setStartAsTray(boolean startAsTray) {
		this.startAsTray = startAsTray;
		saveSettings();
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
		saveSettings();
	}

	public boolean isOfflineHidden() {
		return offlineHidden;
	}

	public void setOfflineHidden(boolean pOfflineHidden) {
		offlineHidden = pOfflineHidden;
		saveSettings();
	}

	public boolean isContactTreeView() {
		return contactTree;
	}

	public void setContactTreeView(boolean pContactTree) {
		contactTree = pContactTree;
		if (contactViewListener != null) {
			contactViewListener.updateContactDisplay(contactTree);
		}
		saveSettings();
	}

	public void setQuickDnd(boolean pQuickDnd) {
		
		boolean old = contactQuickDnd;
		contactQuickDnd = pQuickDnd;

		if (old != contactQuickDnd && contactViewListener != null) {
			if (contactTree ) {
				contactViewListener.updateContactDisplay(false);
				contactViewListener.updateContactDisplay(true);
			}
			else {
				contactViewListener.updateContactDisplay(true);
				contactViewListener.updateContactDisplay(false);
			}
		}
		saveSettings();
	}
	
	public boolean isPortUnlocked() {
		return unlockPort;
	}
	
	public boolean isQuickDnd() {
		return contactQuickDnd;
	}

	public SlimUserContact getContactInfo() {
		return contactInfo;
	}
	
	public void registerContcatViewListener(ContactViewListener pListener) {
		contactViewListener = pListener;
	}

	public int getH() {
		return h;
	}

	public void setH(int pH) {
		h = pH;
		saveSettings();
	}

	public int getW() {
		return w;
	}

	public void setW(int pW) {
		w = pW;
		saveSettings();
	}

	public int getX() {
		return x;
	}

	public void setX(int pX) {
		x = pX;
		saveSettings();
	}

	public int getY() {
		return y;
	}

	public void setY(int pY) {
		y = pY;
		saveSettings();
	}

    public void setLocation(Point pPoint) {
        y = pPoint.y;
        x = pPoint.x;
        saveSettings();
    }

    public void setSize(Dimension pDim) {
        h = pDim.height;
        w = pDim.width;
        
    }

	private void saveSettings() {
		if (initOk) {
            model.storeSettings();
		}
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public boolean isItalic() {
		return italic;
	}

	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	public int getFontSize() {
		return size;
	}

	public void setFontSize(int size) throws SlimException {
		if (size > 0  && size < 8) {
			this.size = size;
		}
		else {
			throw new SlimException("Invalid font size must be between 1 and 7");
		}
	}

	public boolean isUnderline() {
		return underline;
	}

	public void setUnderline(boolean underline) {
		this.underline = underline;
	}
}
