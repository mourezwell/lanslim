package com.oz.lanslim.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.network.SlimNetworkAdapter;

public class SlimModel {
	
	private static final String INI_FILE_COMMENTS = "LANSLIM settings File";

	private static final String INI_FILE_NAME = "lanslim.ini";
	
	private SlimContactList contacts = null;
	private SlimTalkList talks = null;
	private SlimSettings settings = null;
	private SlimNetworkAdapter networkAdapter = null;
	private boolean settingsLoaded = false;
	
	public SlimModel(SlimIconListener pIconListener) throws IOException, SlimException {
		talks = new SlimTalkList(this);
		settingsLoaded = false;
		
		File iniFile = new File(
				System.getProperty("user.home") +  File.separator + INI_FILE_NAME);
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

	public void exit() throws SlimException, IOException {
		talks.sendExitMessage();
		contacts.sendExitMessage();
		// send all unavailability message
		networkAdapter.stop();
		storeSettings();
	}
	
	public void storeSettings() throws IOException {
		if (settingsLoaded) {
			Properties p = new Properties();
			p.putAll(settings.toProperties());
			p.putAll(contacts.toProperties());
			
			String path = System.getProperty("user.home") +  File.separator + INI_FILE_NAME;
			FileOutputStream fos = new FileOutputStream(new File(path));
			p.store(fos, INI_FILE_COMMENTS);
			
			fos.flush();
			fos.close();
		}
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
