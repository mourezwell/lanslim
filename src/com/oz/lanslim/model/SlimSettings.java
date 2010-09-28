package com.oz.lanslim.model;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.StringConstants;

public class SlimSettings {

	protected static final String[] AVAILABLE_LANGUAGE = new String[] {
		"EN", "FR" //$NON-NLS-1$ //$NON-NLS-2$
	};
	
	protected static final String UNLOCK_PORT_SYSTEM_PROPERTY_KEY = "unlockPort"; //$NON-NLS-1$
	
	private static final String SLIM_SETTINGS_PROPS_PREFIX = "slim.settings."; //$NON-NLS-1$
	private static final String LANGUAGE_PROP = SLIM_SETTINGS_PROPS_PREFIX + "language"; //$NON-NLS-1$
	private static final String NAME_PROP = SLIM_SETTINGS_PROPS_PREFIX + "name"; //$NON-NLS-1$
	private static final String HOST_PROP = SLIM_SETTINGS_PROPS_PREFIX + "host"; //$NON-NLS-1$
	private static final String PORT_PROP = SLIM_SETTINGS_PROPS_PREFIX + "port"; //$NON-NLS-1$
	private static final String COLOR_PROP = SLIM_SETTINGS_PROPS_PREFIX + "color"; //$NON-NLS-1$
	private static final String UNDERLINE_PROP = SLIM_SETTINGS_PROPS_PREFIX + "underline"; //$NON-NLS-1$
	private static final String ITALIC_PROP = SLIM_SETTINGS_PROPS_PREFIX + "italic"; //$NON-NLS-1$
	private static final String BOLD_PROP = SLIM_SETTINGS_PROPS_PREFIX + "bold"; //$NON-NLS-1$
	private static final String SIZE_PROP = SLIM_SETTINGS_PROPS_PREFIX + "size"; //$NON-NLS-1$
	private static final String FACE_PROP = SLIM_SETTINGS_PROPS_PREFIX + "face"; //$NON-NLS-1$
	private static final String START_PROP = SLIM_SETTINGS_PROPS_PREFIX + "startAsTray"; //$NON-NLS-1$
	private static final String CLOSE_PROP = SLIM_SETTINGS_PROPS_PREFIX + "closeAsTray"; //$NON-NLS-1$
	private static final String HIDEGRP_PROP = SLIM_SETTINGS_PROPS_PREFIX + "hideGroup"; //$NON-NLS-1$
	private static final String HIDEOFF_PROP = SLIM_SETTINGS_PROPS_PREFIX + "hideOffline"; //$NON-NLS-1$
	private static final String CONTACT_TREE_PROP = SLIM_SETTINGS_PROPS_PREFIX + "contactTree"; //$NON-NLS-1$
	private static final String CONTACT_QUICK_DND = SLIM_SETTINGS_PROPS_PREFIX + "quickDND"; //$NON-NLS-1$
	private static final String FRAME_X = SLIM_SETTINGS_PROPS_PREFIX + "x"; //$NON-NLS-1$
	private static final String FRAME_Y = SLIM_SETTINGS_PROPS_PREFIX + "y"; //$NON-NLS-1$
	private static final String FRAME_W = SLIM_SETTINGS_PROPS_PREFIX + "w"; //$NON-NLS-1$
	private static final String FRAME_H = SLIM_SETTINGS_PROPS_PREFIX + "h"; //$NON-NLS-1$
	private static final String SHORTCUT_PROP_PREFIX = SLIM_SETTINGS_PROPS_PREFIX + "shortcut."; //$NON-NLS-1$
	private static final String AUTO_CHECK_VERSION_PROP = SLIM_SETTINGS_PROPS_PREFIX + "autoCheckVersion"; //$NON-NLS-1$
	private static final String PROXY_PREFIX = SLIM_SETTINGS_PROPS_PREFIX + "proxy."; //$NON-NLS-1$
	private static final String PROXY_NEEDED_PROP = PROXY_PREFIX + "needed"; //$NON-NLS-1$
	private static final String PROXY_HOST_PROP = PROXY_PREFIX + "host"; //$NON-NLS-1$
	private static final String PROXY_PORT_PROP = PROXY_PREFIX + "port"; //$NON-NLS-1$
	private static final String CRYPTO_PROP = SLIM_SETTINGS_PROPS_PREFIX + "crypto"; //$NON-NLS-1$
	private static final String AUTOREFRESH_PROP = SLIM_SETTINGS_PROPS_PREFIX + "autorefresh"; //$NON-NLS-1$
	private static final String NOTIFICATION_PREFIX = SLIM_SETTINGS_PROPS_PREFIX + "notification."; //$NON-NLS-1$
	private static final String NOTIFICATION_BUBBLE_PREFIX = NOTIFICATION_PREFIX + "bubble."; //$NON-NLS-1$
	private static final String NOTIFICATION_BLINK_PREFIX = NOTIFICATION_PREFIX + "blink."; //$NON-NLS-1$
	private static final String NOTIFICATION_BEEP_PREFIX = NOTIFICATION_PREFIX + "beep."; //$NON-NLS-1$
	private static final String NOTIFICATION_AVAILABILTY_BUBBLE = NOTIFICATION_BUBBLE_PREFIX + "availability"; //$NON-NLS-1$
	private static final String NOTIFICATION_AVAILABILTY_BLINK = NOTIFICATION_BLINK_PREFIX + "availability"; //$NON-NLS-1$
	private static final String NOTIFICATION_AVAILABILTY_BEEP = NOTIFICATION_BEEP_PREFIX + "availability"; //$NON-NLS-1$
	private static final String NOTIFICATION_NEWTALK_BUBBLE = NOTIFICATION_BUBBLE_PREFIX + "newTalk"; //$NON-NLS-1$
	private static final String NOTIFICATION_NEWTALK_BLINK = NOTIFICATION_BLINK_PREFIX + "newTalk"; //$NON-NLS-1$
	private static final String NOTIFICATION_NEWTALK_BEEP = NOTIFICATION_BEEP_PREFIX + "newTalk"; //$NON-NLS-1$
	private static final String NOTIFICATION_NEWMESSAGE_BUBBLE = NOTIFICATION_BUBBLE_PREFIX + "newMessage"; //$NON-NLS-1$
	private static final String NOTIFICATION_NEWMESSAGE_BLINK = NOTIFICATION_BLINK_PREFIX + "newMessage"; //$NON-NLS-1$
	private static final String NOTIFICATION_NEWMESSAGE_BEEP = NOTIFICATION_BEEP_PREFIX + "newMessage"; //$NON-NLS-1$
	private static final String NOTIFICATION_PEOPLEIN_BUBBLE = NOTIFICATION_BUBBLE_PREFIX + "peopleIn"; //$NON-NLS-1$
	private static final String NOTIFICATION_PEOPLEIN_BLINK = NOTIFICATION_BLINK_PREFIX + "peopleIn"; //$NON-NLS-1$
	private static final String NOTIFICATION_PEOPLEIN_BEEP = NOTIFICATION_BEEP_PREFIX + "peopleIn"; //$NON-NLS-1$
	private static final String SOUND_PROP = SLIM_SETTINGS_PROPS_PREFIX + "sound"; //$NON-NLS-1$
	private static final String MOOD_PROP = SLIM_SETTINGS_PROPS_PREFIX + "mood"; //$NON-NLS-1$
	private static final String TEXTBUTTON_PROP = SLIM_SETTINGS_PROPS_PREFIX + "textButton"; //$NON-NLS-1$
	private static final String DOWNLOAD_PROP = SLIM_SETTINGS_PROPS_PREFIX + "download"; //$NON-NLS-1$
	
	public static final String DEFAULT_LANGUAGE = "EN"; //$NON-NLS-1$
	public static final String DEFAULT_PORT = "17000"; //$NON-NLS-1$
	private static final String DEFAULT_NAME = "yourAlias"; //$NON-NLS-1$
	private static final String DEFAULT_COLOR = "000000"; //$NON-NLS-1$
	private static final String COLOR_PATTERN = "[0-9A-F]{6}";  //$NON-NLS-1$
	private static final int DEFAULT_SIZE = 4;
	private static final String DEFAULT_FACE = "Default";
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
	private static final String DEFAULT_SHORCUT = StringConstants.EMPTY;
	public static final int SHORTCUT_NUMBER = 12;
	private static final boolean DEFAULT_AUTO_CHECK = false;
	private static final boolean DEFAULT_NEED_PROXY = false;
	private static final boolean DEFAULT_CRYPTO = false;
	private static final boolean DEFAULT_AUTOREFRESH = false;
	private static final boolean DEFAULT_AVAILABILTY_BUBBLE = false;
	private static final boolean DEFAULT_AVAILABILTY_BLINK = false;
	private static final boolean DEFAULT_AVAILABILTY_BEEP = false;
	private static final boolean DEFAULT_NEWTALK_BUBBLE = false;
	private static final boolean DEFAULT_NEWTALK_BLINK = false;
	private static final boolean DEFAULT_NEWTALK_BEEP = false;
	private static final boolean DEFAULT_NEWMESSAGE_BUBBLE = true;
	private static final boolean DEFAULT_NEWMESSAGE_BLINK = true;
	private static final boolean DEFAULT_NEWMESSAGE_BEEP = false;
	private static final boolean DEFAULT_PEOPLEIN_BUBBLE = false;
	private static final boolean DEFAULT_PEOPLEIN_BLINK = false;
	private static final boolean DEFAULT_PEOPLEIN_BEEP = false;
	private static final boolean DEFAULT_TEXT_WITH_BUTTON = true;
	private static final String DEFAULT_MOOD = "";
	private static final boolean DEFAULT_SOUND = true;
	private static final String DEFAULT_DOWNLOAD_DIR = System.getProperty("user.home");
	
	private  String language = DEFAULT_LANGUAGE;
	private  String color = DEFAULT_COLOR;
	private  String face = DEFAULT_FACE;
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
	private  boolean contactQuickDnd = DEFAULT_CONTACT_DND;
	private  int x = DEFAULT_X;
	private  int y = DEFAULT_Y;
	private  int w = DEFAULT_W;
	private  int h = DEFAULT_H;
	private  String[] shortcuts = null;
	private  boolean autoCheckVersion= DEFAULT_AUTO_CHECK;
	private  boolean proxyNeeded = DEFAULT_NEED_PROXY;
	private  String proxyHost = null;
	private  String proxyPort = null;
	private  boolean crypto = DEFAULT_CRYPTO;
	private  boolean autoRefreshContacts = DEFAULT_AUTOREFRESH;
	private  boolean notifAvailabiltyBlink = DEFAULT_AVAILABILTY_BLINK;
	private  boolean notifAvailabiltyBubble = DEFAULT_AVAILABILTY_BUBBLE;
	private  boolean notifAvailabiltyBeep = DEFAULT_AVAILABILTY_BEEP;
	private  boolean notifNewTalkBlink = DEFAULT_NEWTALK_BLINK;
	private  boolean notifNewTalkBubble = DEFAULT_NEWTALK_BUBBLE;
	private  boolean notifNewTalkBeep = DEFAULT_NEWTALK_BEEP;
	private  boolean notifNewMessageBlink = DEFAULT_NEWMESSAGE_BLINK;
	private  boolean notifNewMessageBubble = DEFAULT_NEWMESSAGE_BUBBLE;
	private  boolean notifNewMessageBeep = DEFAULT_NEWMESSAGE_BEEP;
	private  boolean notifPeopleInBlink = DEFAULT_PEOPLEIN_BLINK;
	private  boolean notifPeopleInBubble = DEFAULT_PEOPLEIN_BUBBLE;
	private  boolean notifPeopleInBeep = DEFAULT_PEOPLEIN_BEEP;
	private  boolean sound = DEFAULT_SOUND;
	private  boolean textWithButton = DEFAULT_TEXT_WITH_BUTTON;
	private  String downloadDir = DEFAULT_DOWNLOAD_DIR;
	
	private SlimModel model = null;
	private SlimUserContact contactInfo = null;
	private ContactViewListener contactViewListener = null;
	private boolean initOk = false;
	private boolean saveLock = false;
	private Timer timer = null;
	private TimerTask saveLocationTask = null;
	private RefreshTask refreshTask = null;
	
	
	public SlimSettings(SlimModel pModel) throws SlimException, UnknownHostException {
		language = DEFAULT_LANGUAGE;
		Externalizer.setLanguage(language);
		
		contactInfo = new SlimUserContact(DEFAULT_NAME, InetAddress.getLocalHost().getHostName(), DEFAULT_PORT);
		contactInfo.setAvailability(SlimAvailabilityEnum.ONLINE);
		contactInfo.setMood(DEFAULT_MOOD);
		
		color = DEFAULT_COLOR;
		size = DEFAULT_SIZE;
		underline = DEFAULT_UNDERLINE;
		italic = DEFAULT_ITALIC;
		bold = DEFAULT_BOLD;
		face = DEFAULT_FACE;
		
		model = pModel;
		networkValid = DEFAULT_NETWORK_VALID;
		unlockPort = Boolean.getBoolean(UNLOCK_PORT_SYSTEM_PROPERTY_KEY);
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
		shortcuts = new String[SHORTCUT_NUMBER];
		for (int i = 0; i < SHORTCUT_NUMBER; i++) {
			shortcuts[i] = DEFAULT_SHORCUT;
		};
		autoCheckVersion= DEFAULT_AUTO_CHECK;
		proxyNeeded = DEFAULT_NEED_PROXY;
		proxyHost = null;
		proxyPort = null;
		crypto = DEFAULT_CRYPTO;
		autoRefreshContacts = DEFAULT_AUTOREFRESH;
		notifAvailabiltyBlink = DEFAULT_AVAILABILTY_BLINK;
		notifAvailabiltyBubble = DEFAULT_AVAILABILTY_BUBBLE;
		notifAvailabiltyBeep = DEFAULT_AVAILABILTY_BEEP;
		notifNewTalkBlink = DEFAULT_NEWTALK_BLINK;
		notifNewTalkBubble = DEFAULT_NEWTALK_BUBBLE;
		notifNewTalkBeep = DEFAULT_NEWTALK_BEEP;
		notifNewMessageBlink = DEFAULT_NEWMESSAGE_BLINK;
		notifNewMessageBubble = DEFAULT_NEWMESSAGE_BUBBLE;
		notifNewMessageBeep = DEFAULT_NEWMESSAGE_BEEP;
		notifPeopleInBlink = DEFAULT_PEOPLEIN_BLINK;
		notifPeopleInBubble = DEFAULT_PEOPLEIN_BUBBLE;
		notifPeopleInBeep = DEFAULT_PEOPLEIN_BEEP;
		sound = DEFAULT_SOUND;
		textWithButton = DEFAULT_TEXT_WITH_BUTTON;
		downloadDir = DEFAULT_DOWNLOAD_DIR;
		
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				Thread.currentThread().setName("Settings Timer"); //$NON-NLS-1$
			}
		}, 0);

		initOk = true;
	}

	public SlimSettings(SlimModel pModel, Properties p) throws SlimException, UnknownHostException {
		this(pModel);
		initOk = false;
		
		String lTemp = p.getProperty(LANGUAGE_PROP);
		if (lTemp != null) {
			setLanguage(lTemp);
			Externalizer.setLanguage(lTemp);
		}

		lTemp = p.getProperty(NAME_PROP);
		try {
			contactInfo.setName(lTemp);
		}
		catch (SlimException se) {
			// ignore start exception
		}
		
		lTemp = p.getProperty(HOST_PROP);
		contactInfo.setHost(lTemp);
	
		if (unlockPort) {
			lTemp = p.getProperty(PORT_PROP);
			try {
				contactInfo.setPort(lTemp);
			}
			catch (SlimException se) {
				// ignore start exception
			}
		}
	
		lTemp = p.getProperty(MOOD_PROP);
		if (lTemp != null) {
			contactInfo.setMood(lTemp);
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

		lTemp = p.getProperty(FACE_PROP);
		if (lTemp != null) {
			setFontFace(lTemp);
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

		String[] lTemps = new String[SHORTCUT_NUMBER];
		for (int i = 0; i < 12; i++) {
			lTemps[i] = p.getProperty(SHORTCUT_PROP_PREFIX + i);
			if (lTemps[i] == null) {
				lTemps[i] = DEFAULT_SHORCUT;
			}
		}
		setShortcuts(lTemps);
		
		lTemp = p.getProperty(AUTO_CHECK_VERSION_PROP);
		if (lTemp != null) {
			setAutoCheckVersion(Boolean.valueOf(lTemp).booleanValue());
		}

		lTemp = p.getProperty(PROXY_HOST_PROP);
		if (lTemp != null) {
			setProxyHost(lTemp);
		}
		lTemp = p.getProperty(PROXY_PORT_PROP);
		if (lTemp != null) {
			setProxyPort(lTemp);
		}
		lTemp = p.getProperty(PROXY_NEEDED_PROP);
		if (lTemp != null) {
			setProxyNeeded(Boolean.valueOf(lTemp).booleanValue());
		}
		
		lTemp = p.getProperty(CRYPTO_PROP);
		if (lTemp != null) {
			try {
				setCryptoEnable(Boolean.valueOf(lTemp).booleanValue());
			}
			catch (SlimException se) {
				// ignore start exception
			}
		}
		
		lTemp = p.getProperty(AUTOREFRESH_PROP);
		if (lTemp != null) {
			setAutoRefreshContacts(Boolean.valueOf(lTemp).booleanValue());
		}
		
		lTemp = p.getProperty(NOTIFICATION_AVAILABILTY_BLINK);
		if (lTemp != null) {
			setNotifAvailabiltyBlink(Boolean.valueOf(lTemp).booleanValue());
		}
		lTemp = p.getProperty(NOTIFICATION_AVAILABILTY_BUBBLE);
		if (lTemp != null) {
			setNotifAvailabiltyBubble(Boolean.valueOf(lTemp).booleanValue());
		}
		lTemp = p.getProperty(NOTIFICATION_AVAILABILTY_BEEP);
		if (lTemp != null) {
			setNotifAvailabiltyBeep(Boolean.valueOf(lTemp).booleanValue());
		}
		lTemp = p.getProperty(NOTIFICATION_NEWMESSAGE_BLINK);
		if (lTemp != null) {
			setNotifNewMessageBlink(Boolean.valueOf(lTemp).booleanValue());
		}
		lTemp = p.getProperty(NOTIFICATION_NEWMESSAGE_BUBBLE);
		if (lTemp != null) {
			setNotifNewMessageBubble(Boolean.valueOf(lTemp).booleanValue());
		}
		lTemp = p.getProperty(NOTIFICATION_NEWMESSAGE_BEEP);
		if (lTemp != null) {
			setNotifNewMessageBeep(Boolean.valueOf(lTemp).booleanValue());
		}
		lTemp = p.getProperty(NOTIFICATION_NEWTALK_BLINK);
		if (lTemp != null) {
			setNotifNewTalkBlink(Boolean.valueOf(lTemp).booleanValue());
		}
		lTemp = p.getProperty(NOTIFICATION_NEWTALK_BUBBLE);
		if (lTemp != null) {
			setNotifNewTalkBubble(Boolean.valueOf(lTemp).booleanValue());
		}
		lTemp = p.getProperty(NOTIFICATION_NEWTALK_BEEP);
		if (lTemp != null) {
			setNotifNewTalkBeep(Boolean.valueOf(lTemp).booleanValue());
		}
		lTemp = p.getProperty(NOTIFICATION_PEOPLEIN_BLINK);
		if (lTemp != null) {
			setNotifPeopleInBlink(Boolean.valueOf(lTemp).booleanValue());
		}
		lTemp = p.getProperty(NOTIFICATION_PEOPLEIN_BUBBLE);
		if (lTemp != null) {
			setNotifPeopleInBubble(Boolean.valueOf(lTemp).booleanValue());
		}
		lTemp = p.getProperty(NOTIFICATION_PEOPLEIN_BEEP);
		if (lTemp != null) {
			setNotifPeopleInBeep(Boolean.valueOf(lTemp).booleanValue());
		}
		
		lTemp = p.getProperty(SOUND_PROP);
		if (lTemp != null) {
			setSoundEnable(Boolean.valueOf(lTemp).booleanValue());
		}

		lTemp = p.getProperty(TEXTBUTTON_PROP);
		if (lTemp != null) {
			setTextWithButton(Boolean.valueOf(lTemp).booleanValue());
		}

		lTemp = p.getProperty(DOWNLOAD_PROP);
		if (lTemp != null) {
			setDownloadDir(lTemp);
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
					throw new SlimException(Externalizer.getString("LANSLIM.123")); //$NON-NLS-1$
				}
				model.getContacts().sendUpdateUserMessage(pOld);
				model.storeSettings();
			}
			else {
				throw new SlimException(Externalizer.getString("LANSLIM.172")); //$NON-NLS-1$
			}
		}
		catch (SocketException se) {
			throw new SlimException(Externalizer.getString("LANSLIM.173", SlimLogger.shortFormatException(se))); //$NON-NLS-1$
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
		p.put(LANGUAGE_PROP, language);
		p.put(NAME_PROP, contactInfo.getName());
		p.put(PORT_PROP, Integer.toString(contactInfo.getPort()));
		p.put(HOST_PROP, contactInfo.getHost());
		p.put(MOOD_PROP, contactInfo.getMood());
		p.put(COLOR_PROP, getColor());
		p.put(FACE_PROP, getFontFace());
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
		for (int i = 0; i < 12; i++) {
			p.put(SHORTCUT_PROP_PREFIX + i, getShortcuts()[i]);
		}
		p.put(AUTO_CHECK_VERSION_PROP, Boolean.toString(isAutoCheckVersion()));
		p.put(PROXY_NEEDED_PROP, Boolean.toString(isProxyNeeded()));
		if (getProxyHost() != null) {
			p.put(PROXY_HOST_PROP, getProxyHost());
		}
		if (getProxyPort() != null) {
			p.put(PROXY_PORT_PROP, getProxyPort());
		}
		p.put(CRYPTO_PROP, Boolean.toString(isCryptoEnable()));
		p.put(AUTOREFRESH_PROP, Boolean.toString(isAutoRefreshContacts()));
		p.put(NOTIFICATION_AVAILABILTY_BLINK, Boolean.toString(isNotifAvailabiltyBlink()));
		p.put(NOTIFICATION_AVAILABILTY_BUBBLE, Boolean.toString(isNotifAvailabiltyBubble()));
		p.put(NOTIFICATION_AVAILABILTY_BEEP, Boolean.toString(isNotifAvailabiltyBeep()));
		p.put(NOTIFICATION_NEWMESSAGE_BLINK, Boolean.toString(isNotifNewMessageBlink()));
		p.put(NOTIFICATION_NEWMESSAGE_BUBBLE, Boolean.toString(isNotifNewMessageBubble()));
		p.put(NOTIFICATION_NEWMESSAGE_BEEP, Boolean.toString(isNotifNewMessageBeep()));
		p.put(NOTIFICATION_NEWTALK_BLINK, Boolean.toString(isNotifNewTalkBlink()));
		p.put(NOTIFICATION_NEWTALK_BUBBLE, Boolean.toString(isNotifNewTalkBubble()));
		p.put(NOTIFICATION_NEWTALK_BEEP, Boolean.toString(isNotifNewTalkBeep()));
		p.put(NOTIFICATION_PEOPLEIN_BLINK, Boolean.toString(isNotifPeopleInBlink()));
		p.put(NOTIFICATION_PEOPLEIN_BUBBLE, Boolean.toString(isNotifPeopleInBubble()));
		p.put(NOTIFICATION_PEOPLEIN_BEEP, Boolean.toString(isNotifPeopleInBeep()));
		p.put(SOUND_PROP, Boolean.toString(isSoundEnable()));
		p.put(TEXTBUTTON_PROP, Boolean.toString(isTextWithButton()));
		p.put(DOWNLOAD_PROP, getDownloadDir());

		return p;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String pColor) throws SlimException {
		if (pColor.matches(COLOR_PATTERN)) {
			color = pColor;
			saveSettings();
		}
		else {
			throw new SlimException(Externalizer.getString("LANSLIM.49", pColor)); //$NON-NLS-1$
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

	public void setStartAsTray(boolean pStartAsTray) {
		startAsTray = pStartAsTray;
		saveSettings();
	}

	public boolean isTrayEnable() {
		return trayEnable;
	}

	public void setTrayEnable(boolean pTrayEnable) {
		trayEnable = pTrayEnable;
	}

	public boolean isGroupHidden() {
		return groupHidden;
	}

	public void setGroupHidden(boolean pGroupHidden) {
		groupHidden = pGroupHidden;
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
        saveSettings();
    }

	private void saveSettings() {
		if (initOk && !saveLock) {
            model.storeSettings();
		}
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean pBold) {
		bold = pBold;
		saveSettings();
	}

	public boolean isItalic() {
		return italic;
	}

	public void setItalic(boolean pItalic) {
		italic = pItalic;
		saveSettings();
	}

	public int getFontSize() {
		return size;
	}

	public void setFontSize(int pSize) throws SlimException {
		if (size > 0  && size < 8) {
			size = pSize;
			saveSettings();
		}
		else {
			throw new SlimException(Externalizer.getString("LANSLIM.171", new Integer(size))); //$NON-NLS-1$
		}
	}

	public String getFontFace() {
		return face;
	}

	public void setFontFace(String pFace) {
		face = pFace;
	}

	public boolean isUnderline() {
		return underline;
	}

	public void setUnderline(boolean pUnderline) {
		underline = pUnderline;
		saveSettings();
	}

	public String[] getShortcuts() {
		return shortcuts;
	}

	public void setShortcuts(String[] pShortcuts) {
		shortcuts = pShortcuts;
		saveSettings();
	}

	public void exportContacts(File pFile) throws IOException {
		model.getContacts().exportContacts(pFile);
	}
	
	public boolean importContacts(File pFile) throws IOException {
		return model.getContacts().importContacts(pFile);
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String pLanguage) {
		language = pLanguage;
		saveSettings();
	}

	public String[] getAvailableLanguage() {
		return AVAILABLE_LANGUAGE;
	}

	public boolean isAutoCheckVersion() {
		return autoCheckVersion;
	}

	public void setAutoCheckVersion(boolean pAutoCheckVersion) {
		autoCheckVersion = pAutoCheckVersion;
		saveSettings();
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String pProxyHost) {
		if (pProxyHost != null && pProxyHost.length() > 0) {
			proxyHost = pProxyHost;
		}
		else {
			proxyHost = null;
		}
		saveSettings();
	}

	public String getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(String pProxyPort) {
		if (pProxyPort != null && pProxyPort.length() > 0) {
			proxyPort = pProxyPort;
		}
		else {
			proxyPort = null;
		}
		saveSettings();
	}

	public boolean isProxyNeeded() {
		return proxyNeeded;
	}

	public void setProxyNeeded(boolean pProxyNeeded) {
		proxyNeeded = pProxyNeeded;
		if (isProxyNeeded() && getProxyHost() != null && getProxyPort() != null) {
	        System.getProperties().put("http.proxyHost", getProxyHost()); //$NON-NLS-1$
	        System.getProperties().put("http.proxyPort", getProxyPort()); //$NON-NLS-1$
		} 
		else {
	        System.getProperties().remove("http.proxyHost"); //$NON-NLS-1$
	        System.getProperties().remove("http.proxyPort"); //$NON-NLS-1$
		}
		saveSettings();
		
        /* PASSWORD MANAGEMENT NOT IMPLEMENTED YET
	    Authenticator.setDefault(new Authenticator() {
	      protected PasswordAuthentication getPasswordAuthentication() {
	        return new PasswordAuthentication("mydomain\\username","password".toCharArray());
	    }});
        // PASSWORD */
	}

	public boolean isCryptoEnable() {
		return crypto;
	}

	public void setCryptoEnable(boolean pCrypto) throws SlimException {
		
		if (pCrypto) {
			if (!crypto) {
				contactInfo.setKey(SlimKey.generateKey());
			}
		}
		else {
			contactInfo.setKey(null);
		}
		crypto = pCrypto;
		saveSettings();
	}

	public boolean isNotifAvailabiltyBlink() {
		return notifAvailabiltyBlink;
	}

	public void setNotifAvailabiltyBlink(boolean pNotifAvailabiltyBlink) {
		notifAvailabiltyBlink = pNotifAvailabiltyBlink;
		saveSettings();
	}

	public boolean isNotifAvailabiltyBubble() {
		return notifAvailabiltyBubble;
	}

	public void setNotifAvailabiltyBubble(boolean pNotifAvailabiltyBubble) {
		notifAvailabiltyBubble = pNotifAvailabiltyBubble;
		saveSettings();
	}

	public void setNotifAvailabiltyBeep(boolean pNotifAvailabiltyBeep) {
		notifAvailabiltyBeep = pNotifAvailabiltyBeep;
		saveSettings();
	}

	public boolean isNotifAvailabiltyBeep() {
		return notifAvailabiltyBeep;
	}

	public boolean isNotifNewMessageBlink() {
		return notifNewMessageBlink;
	}

	public void setNotifNewMessageBlink(boolean pNotifNewMessageBlink) {
		notifNewMessageBlink = pNotifNewMessageBlink;
		saveSettings();
	}

	public boolean isNotifNewMessageBubble() {
		return notifNewMessageBubble;
	}

	public void setNotifNewMessageBubble(boolean pNotifNewMessageBubble) {
		notifNewMessageBubble = pNotifNewMessageBubble;
		saveSettings();
	}

	public void setNotifNewMessageBeep(boolean pNotifNewMessageBeep) {
		notifNewMessageBeep = pNotifNewMessageBeep;
		saveSettings();
	}

	public boolean isNotifNewMessageBeep() {
		return notifNewMessageBeep;
	}

	public boolean isNotifNewTalkBlink() {
		return notifNewTalkBlink;
	}

	public void setNotifNewTalkBlink(boolean pNotifNewTalkBlink) {
		notifNewTalkBlink = pNotifNewTalkBlink;
		saveSettings();
	}

	public boolean isNotifNewTalkBubble() {
		return notifNewTalkBubble;
	}

	public void setNotifNewTalkBubble(boolean pNotifNewTalkBubble) {
		notifNewTalkBubble = pNotifNewTalkBubble;
		saveSettings();
	}

	public void setNotifNewTalkBeep(boolean pNotifNewTalkBeep) {
		notifNewTalkBeep = pNotifNewTalkBeep;
		saveSettings();
	}

	public boolean isNotifNewTalkBeep() {
		return notifNewTalkBeep;
	}

	public boolean isNotifPeopleInBlink() {
		return notifPeopleInBlink;
	}

	public void setNotifPeopleInBlink(boolean pNotifPeopleInBlink) {
		notifPeopleInBlink = pNotifPeopleInBlink;
		saveSettings();
	}

	public boolean isNotifPeopleInBubble() {
		return notifPeopleInBubble;
	}

	public void setNotifPeopleInBubble(boolean pNotifPeopleInBubble) {
		notifPeopleInBubble = pNotifPeopleInBubble;
		saveSettings();
	}

	public void setNotifPeopleInBeep(boolean pNotifPeopleInBeep) {
		notifPeopleInBeep = pNotifPeopleInBeep;
		saveSettings();
	}

	public boolean isNotifPeopleInBeep() {
		return notifPeopleInBeep;
	}

	public boolean isAutoRefreshContacts() {
		return autoRefreshContacts;
	}

	public void setAutoRefreshContacts(boolean pAutoRefresh) {
		if (pAutoRefresh != autoRefreshContacts) {
			autoRefreshContacts = pAutoRefresh;
			if (pAutoRefresh) {
				refreshTask = new RefreshTask();
				timer.schedule(refreshTask, 10000, 3600000);
			}
			else {
				refreshTask.cancel();
				refreshTask = null;
			}
			saveSettings();
		}
	}

	
    public void scheduleLocationSaving(Component pComp) {
    	
		if (saveLocationTask == null) {
			saveLocationTask = new SavingLocationTask(pComp);
			timer.schedule(saveLocationTask, 5000);
		}

    }
	private class SavingLocationTask extends TimerTask {
		
		private Component comp = null;
		
		public SavingLocationTask(Component pC) {
			comp = pC;
		}
		
		public void run() {
			setLocation(comp.getLocation());
			saveLocationTask = null;
		}
	}

	private class RefreshTask extends TimerTask {
		
		public void run() {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					model.getContacts().refresh();
					System.out.println("ref");
				}
			});
		}
	}

	public boolean isSoundEnable() {
		return sound;
	}

	public void setSoundEnable(boolean pSoundEnable) {
		sound = pSoundEnable;
		saveSettings();
	}

	public void saveLock(boolean pLock) {
		saveLock = pLock;
		saveSettings();
	}

	public boolean isTextWithButton() {
		return textWithButton;
	}

	public void setTextWithButton(boolean pTextWithButton) {
		textWithButton = pTextWithButton;
	}

	public String getDownloadDir() {
		return downloadDir;
	}

	public void setDownloadDir(String pDownloadDir) {
		downloadDir = pDownloadDir;
	}


}
