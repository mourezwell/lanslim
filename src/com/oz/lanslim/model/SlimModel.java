package com.oz.lanslim.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimIconListener;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.message.SlimAvailabilityUserMessage;
import com.oz.lanslim.message.SlimExcludeTalkMessage;
import com.oz.lanslim.message.SlimInviteTalkMessage;
import com.oz.lanslim.message.SlimMessage;
import com.oz.lanslim.message.SlimMessageTypeEnum;
import com.oz.lanslim.network.SlimNetworkAdapter;

public class SlimModel {
	
	private static final String INI_FILE_HEADER = "LANSLIM settings File"; //$NON-NLS-1$

	private static final String INI_FILE_NAME = "lanslim.ini"; //$NON-NLS-1$
	private static final String INI_FILE_BAK = "lanslim.ini.bak"; //$NON-NLS-1$
	
	private SlimContactList contacts = null;
	private SlimTalkList talks = null;
	private SlimSettings settings = null;
	private SlimNetworkAdapter networkAdapter = null;
	private SlimIconListener iconListener = null;
	private WavePlayer beep = null;
	private IdleTimeMonitor idleMonitor = null;
	
	
	
	private boolean settingsLoaded = false;
	
	public SlimModel() throws IOException, SlimException {
		talks = new SlimTalkList(this);
		settingsLoaded = false;
		
		File iniFile = new File(
				System.getProperty("user.home") +  File.separator + INI_FILE_NAME); //$NON-NLS-1$
		if (iniFile.exists() && iniFile.isFile()) {
			loadSettings(iniFile);
		}
		else {
			settings = new SlimSettings(this);
			networkAdapter = new SlimNetworkAdapter(this);
			contacts = new SlimContactList(this);
		}
		File lSoundFile = new File(getClass().getResource("/com/oz/lanslim/sounds/Type.wav").getFile());
		beep = new WavePlayer(lSoundFile);
		settingsLoaded = true;
		idleMonitor = new IdleTimeMonitor(settings.getContactInfo());
		new Thread(idleMonitor).start();
	}

	public void loadSettings(File pIniFile) throws SlimException, IOException {
		Properties p = new Properties();
		FileInputStream fis = new FileInputStream(pIniFile);
		p.load(fis);
		fis.close();
		
		settings = new SlimSettings(this, p);
		networkAdapter = new SlimNetworkAdapter(this);
		contacts = new SlimContactList(this, p);
		contacts.sendWelcomeMessage();
	}

	public void exit() throws SlimException {
		idleMonitor.stop();
		talks.sendExitMessage();
		contacts.sendExitMessage();
		// send all unavailability message
		networkAdapter.stop();
		save();
	}
	
	public synchronized void save() {
		
		if (settingsLoaded) {
    		try {
				Properties p = new Properties();
				p.putAll(settings.toProperties());
				p.putAll(contacts.toProperties());
				
				String path = System.getProperty("user.home") +  File.separator + INI_FILE_NAME; //$NON-NLS-1$
				File ini = new File(path);
				String pathBak = System.getProperty("user.home") +  File.separator + INI_FILE_BAK; //$NON-NLS-1$
				File bak = new File(pathBak);
				if (bak.exists()) {
					bak.delete();
				}
				if (ini.exists()) {
					ini.renameTo(new File(pathBak));
				}
				
				FileOutputStream fos = new FileOutputStream(new File(path));
				p.store(fos, INI_FILE_HEADER);
				
				fos.flush();
				fos.close();
    		}
    		catch (IOException ex) {
    			SlimLogger.logException("model.save", ex); //$NON-NLS-1$
    		}
		}
	}
	
	public void storeSettings() {
		
        Thread t = new Thread(new Runnable() {
            public void run() {
            	save();
            }
        });
        t.start();
	}

	public SlimContactList getContacts() {
		return contacts;
	}

	public SlimSettings getSettings() {
		return settings;
	}

	public SlimTalkList getTalks() {
		return talks;
	}

	public SlimNetworkAdapter getNetworkAdapter() {
		return networkAdapter;
	}
	
	public SlimIconListener getIconListener() {
		return iconListener;
	}

	public void registerIconListener(SlimIconListener pListener) {
		iconListener = pListener;
	}

	public void notifyMessageReceived(SlimMessage pMessage) {
		
		if (iconListener != null) {
			SlimMessageTypeEnum lType = pMessage.getType();
			if (lType.equals(SlimMessageTypeEnum.UPDATE_TALK)) {
				if (settings.isNotifNewMessageBeep() && settings.isSoundEnable()) {
					beep.play();
				}
				iconListener.startIconBlinking(settings.isNotifNewMessageBlink(), 
					settings.isNotifNewMessageBubble(),
					Externalizer.getString("LANSLIM.2", pMessage.getSender().getName()));
			}
			else if (lType.equals(SlimMessageTypeEnum.AVAILABILITY)) {
				SlimAvailabilityUserMessage saum = (SlimAvailabilityUserMessage)pMessage;
				SlimUserContact knownSuc = contacts.getOrAddUserByAddress(pMessage.getSender());
				boolean lChange = !saum.getAvailability().equals(knownSuc.getAvailability());
				if (settings.isNotifAvailabiltyBeep() && settings.isSoundEnable()) {
					beep.play();
				}

				if (lChange && saum.getAvailability().equals(SlimAvailabilityEnum.ONLINE)) {
					iconListener.startIconBlinking(settings.isNotifAvailabiltyBlink(), 
							settings.isNotifAvailabiltyBubble(),
							Externalizer.getString("LANSLIM.211", pMessage.getSender().getName()));
				}
				else if (lChange && saum.getAvailability().equals(SlimAvailabilityEnum.OFFLINE)){
					iconListener.startIconBlinking(settings.isNotifAvailabiltyBlink(), 
						settings.isNotifAvailabiltyBubble(),
						Externalizer.getString("LANSLIM.212", pMessage.getSender().getName()));
				}
			}
			else if (lType.equals(SlimMessageTypeEnum.EXCLUDE_TALK)) {
				if (settings.isNotifPeopleInBeep() && settings.isSoundEnable()) {
					beep.play();
				}
				SlimExcludeTalkMessage setm = (SlimExcludeTalkMessage)pMessage;
				iconListener.startIconBlinking(settings.isNotifPeopleInBlink(), 
					settings.isNotifPeopleInBubble(),
					Externalizer.getString("LANSLIM.210", setm.getExcludedContact().getName()));
			}
			else if (lType.equals(SlimMessageTypeEnum.INVITE_TALK)) {
				if (settings.isNotifPeopleInBeep() && settings.isSoundEnable()) {
					beep.play();
				}
				SlimInviteTalkMessage sitm = (SlimInviteTalkMessage)pMessage;
				iconListener.startIconBlinking(settings.isNotifPeopleInBlink(), 
					settings.isNotifPeopleInBubble(),
					Externalizer.getString("LANSLIM.209", sitm.getNewContact().getName()));
			}
			else if (lType.equals(SlimMessageTypeEnum.EXIT_TALK)) {
				if (settings.isNotifPeopleInBeep() && settings.isSoundEnable()) {
					beep.play();
				}
				iconListener.startIconBlinking(settings.isNotifPeopleInBlink(), 
					settings.isNotifPeopleInBubble(),
					Externalizer.getString("LANSLIM.208", pMessage.getSender().getName()));
			}
			else if (lType.equals(SlimMessageTypeEnum.NEW_TALK)) {
				if (settings.isNotifNewTalkBeep() && settings.isSoundEnable()) {
					beep.play();
				}
				iconListener.startIconBlinking(settings.isNotifNewTalkBlink(), 
					settings.isNotifNewTalkBubble(),
					Externalizer.getString("LANSLIM.207", pMessage.getSender().getName()));
			}
		}
	}
	
}
