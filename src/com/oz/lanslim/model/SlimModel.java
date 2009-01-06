package com.oz.lanslim.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimIconListener;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.network.SlimNetworkAdapter;

public class SlimModel {
	
	private static final String INI_FILE_HEADER = "LANSLIM settings File"; //$NON-NLS-1$

	private static final String INI_FILE_NAME = "lanslim.ini"; //$NON-NLS-1$
	private static final String INI_FILE_BAK = "lanslim.ini.bak"; //$NON-NLS-1$
	
	private SlimContactList contacts = null;
	private SlimTalkList talks = null;
	private SlimSettings settings = null;
	private SlimNetworkAdapter networkAdapter = null;
	private boolean settingsLoaded = false;
	
	public SlimModel(SlimIconListener pIconListener) throws IOException, SlimException {
		talks = new SlimTalkList(this);
		settingsLoaded = false;
		
		File iniFile = new File(
				System.getProperty("user.home") +  File.separator + INI_FILE_NAME); //$NON-NLS-1$
		if (iniFile.exists() && iniFile.isFile()) {
			loadSettings(iniFile, pIconListener);
		}
		else {
			settings = new SlimSettings(this);
			networkAdapter = new SlimNetworkAdapter(this, pIconListener);
			contacts = new SlimContactList(this);
		}
		settingsLoaded = true;
	}

	public void loadSettings(File pIniFile, SlimIconListener pIconListener) throws SlimException, IOException {
		Properties p = new Properties();
		FileInputStream fis = new FileInputStream(pIniFile);
		p.load(fis);
		fis.close();
		
		settings = new SlimSettings(this, p);
		networkAdapter = new SlimNetworkAdapter(this, pIconListener);
		contacts = new SlimContactList(this, p);
		contacts.sendWelcomeMessage();
	}

	public void exit() throws SlimException {
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

}
