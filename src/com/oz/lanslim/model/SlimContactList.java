package com.oz.lanslim.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.message.SlimAvailabilityUserMessage;
import com.oz.lanslim.message.SlimUpdateUserMessage;

public class SlimContactList {
	
	private static final String SLIM_CONTACT_PROPS_PREFIX = "slim.contact.";
	
	private static final String USERNAME_SUFFIX = ".username";
	
	private static final String USERIP_SUFFIX =  ".userip";

	private static final String USERPORT_SUFFIX =  ".userport";

	private static final String GROUPNAME_SUFFIX = ".groupname";

	private static final String MEMBERS_PREFIX = ".members.";

	private Map list = null;
	private SlimContactListener listener = null;
	private SlimModel model = null;
	
	public SlimContactList(SlimModel pModel) {
		list = new HashMap();
		model = pModel;
		list.put(model.getSettings().getContactInfo().getName(), model.getSettings().getContactInfo());
		
	}
	
	public SlimContactList(SlimModel pModel, Properties p) {
		this(pModel);
		List groupNb = new ArrayList();
		int i = 1;
		boolean endUser = false;
		while (!endUser) {
			String lKey = SLIM_CONTACT_PROPS_PREFIX + i + GROUPNAME_SUFFIX;
			if (p.getProperty(lKey) != null) {
				groupNb.add(new Integer(i));
			} 
			else {
				lKey = SLIM_CONTACT_PROPS_PREFIX + i + USERNAME_SUFFIX;
				if (p.getProperty(lKey) != null) {
					String lName = p.getProperty(lKey);
					lKey = SLIM_CONTACT_PROPS_PREFIX + i + USERIP_SUFFIX;
					String lIP = p.getProperty(lKey); 
					lKey = SLIM_CONTACT_PROPS_PREFIX + i + USERPORT_SUFFIX;
					String lPortString = null;
					if (model.getSettings().isPortUnlocked()) {
						lPortString = p.getProperty(lKey);
					}
					else {
						lPortString = SlimSettings.DEFAULT_PORT;
					}
					if (lIP != null && lPortString != null) {
						try {
							SlimUserContact suc = new SlimUserContact(lName.trim(), 
									lIP.trim(), lPortString.trim());
							list.put(lName.trim(), suc);
						}
						catch (SlimException e) {
							SlimLogger.log(e + ":" + e.getMessage() + " at SlimContactList()");

						}
					}
				}
				else {
					endUser = true;
				}
			} 
			i = i + 1;
		}
		for (Iterator it = groupNb.iterator(); it.hasNext();) {
			int j = ((Integer)it.next()).intValue();
			String lKey = SLIM_CONTACT_PROPS_PREFIX + j + GROUPNAME_SUFFIX;
			String lName = p.getProperty(lKey);
			try {
				SlimGroupContact g = new SlimGroupContact(lName, new ArrayList());
				int k = 1;
				endUser = false;
				while (!endUser) {
					lKey = SLIM_CONTACT_PROPS_PREFIX + j + MEMBERS_PREFIX + k  + USERNAME_SUFFIX;
					lName = p.getProperty(lKey);
					if (lName == null) {
						endUser = true;
					}
					else {
						if (list.containsKey(lName.trim()) 
								&& !((SlimContact)list.get(lName.trim())).isGroup()) {
							g.addMembers((SlimUserContact)list.get(lName.trim()));
							list.put(g.getName(), g);
						}
						else {
							SlimLogger.log("Unknown user " + lName + " in group " +  g.getName());
						}
					}
					k = k + 1;
				}					
			}
			catch (SlimException e) {
				SlimLogger.log(e + ":" + e.getMessage() + " at SlimContactList()");
			}
		}
	}
	
	public synchronized Object[][] getTableModelData() {
		
		Object[][] datas = new Object[list.size() - 1][2];
		int i = 0;
		for (Iterator it = list.values().iterator(); it.hasNext();) {
			SlimContact c = (SlimContact)it.next();
			if (!(c.equals(model.getSettings().getContactInfo()))) {
				datas[i] = new Object[] { c.getName(), c.getAvailability() };
				i++;
			}
		}
		return datas;
	}
	
	public synchronized List getOnlineContact() {
		List l = new ArrayList();
		for (Iterator it = list.values().iterator(); it.hasNext();) {
			SlimContact c = (SlimContact)it.next();
			if (c.getAvailability() != SlimAvailabilityEnum.OFFLINE) {
				if (!(c.equals(model.getSettings().getContactInfo()))) {
					l.add(c);
				}
			}
		}
		return l;
	}
	
	public synchronized List getAllUserContact() {
		List l = new ArrayList();
		for (Iterator it = list.values().iterator(); it.hasNext();) {
			SlimContact c = (SlimContact)it.next();
			if (!c.isGroup()) {
				l.add(c);
			}
		}
		return l;
	}

	public synchronized List getAllGroupContact() {
		List l = new ArrayList();
		for (Iterator it = list.values().iterator(); it.hasNext();) {
			SlimContact c = (SlimContact)it.next();
			if (c.isGroup()) {
				l.add(c);
			}
		}
		return l;
	}

	public synchronized SlimContact getContactByName(String pName) {
		return (SlimContact)list.get(pName);
	}
	
	public synchronized boolean addContact(SlimContact pContact) {
		if (list.containsKey(pContact.getName())) {
			return false;
		}
		list.put(pContact.getName(), pContact);
		updateListener(pContact);
		
		if (!pContact.isGroup()) {
			sendAvailabiltyMessage((SlimUserContact)pContact, SlimAvailabilityEnum.ONLINE);
		}
		try {
			model.storeSettings();
		}
		catch (IOException ioe) {
			// ignore it during runtime, exception logged at exit
		}

		return true;
	}

	public synchronized boolean updateContact(SlimContact pOldContact, SlimContact pNewContact) {
		
		SlimContact sc = (SlimContact)list.get(pNewContact.getName());
		
		if (sc != null && !sc.equals(pOldContact)) {
			return false;
		}
		list.remove(pOldContact.getName());
		list.put(pNewContact.getName(), pNewContact);
		updateListener(pNewContact);
		
		if (!pNewContact.isGroup() && !(pNewContact.equals(model.getSettings().getContactInfo()))) {
			sendAvailabiltyMessage((SlimUserContact)pNewContact, SlimAvailabilityEnum.ONLINE);
		}
		try {
			model.storeSettings();
		}
		catch (IOException ioe) {
			// ignore it during runtime, exception logged at exit
		}
		return true;
	}

	public synchronized boolean removeContactByName(String pContactName) {
		SlimContact lRemoved = (SlimContact)list.remove(pContactName);
		if (lRemoved != null) {
			updateListener(lRemoved);
			return true;
		}
		return false;
	}

	
	public Properties toProperties() {
		Properties p = new Properties();
		int i = 1;
		for (Iterator it = list.values().iterator(); it.hasNext();) {
			SlimContact c = (SlimContact)it.next();
			if (c.isGroup()) {
				SlimGroupContact gc = (SlimGroupContact)c;
				p.put(SLIM_CONTACT_PROPS_PREFIX + i + GROUPNAME_SUFFIX, gc.getName());
				int j = 1;
				for (Iterator it2 = gc.getMembers().iterator(); it2.hasNext();) {
					SlimContact m = (SlimContact)it2.next();
					p.put(SLIM_CONTACT_PROPS_PREFIX + i + MEMBERS_PREFIX + j  + USERNAME_SUFFIX, m.getName());
					j = j + 1;
				}					
			}
			else {
				SlimUserContact gc = (SlimUserContact)c;
				p.put(SLIM_CONTACT_PROPS_PREFIX + i + USERNAME_SUFFIX, gc.getName());
				p.put(SLIM_CONTACT_PROPS_PREFIX + i + USERIP_SUFFIX, gc.getHost());
				p.put(SLIM_CONTACT_PROPS_PREFIX + i + USERPORT_SUFFIX, Integer.toString(gc.getPort()));
			}
			i = i + 1;
		}
		return p;
	}

	private void updateListener(SlimContact pContact) {
		if (listener != null) {
			listener.updateContact(pContact);
		}
	}
		
	public void addListener(SlimContactListener pListener) {
		listener = pListener;
	}
	
	

	private void sendAvailabiltyMessage(SlimUserContact pContact, SlimAvailabilityEnum pStatus) {
		
		try {
			SlimUserContact sender = model.getSettings().getContactInfo();
			SlimAvailabilityUserMessage saum = new SlimAvailabilityUserMessage(sender, pStatus);
			model.getNetworkAdapter().send(saum, pContact);
		}
		catch (SlimException lException) {
			// should not happen unless setting badly set
		}
	}

	public void receiveAvailabiltyMessage(SlimAvailabilityUserMessage pMessage) {
		
		SlimUserContact knownSuc = getOrAddUserByAddress(pMessage.getSender());
		
		SlimAvailabilityEnum ase = pMessage.getAvailability();
		if (ase == SlimAvailabilityEnum.ONLINE) {
			knownSuc.setAvailability(ase);
			sendAvailabiltyMessage(knownSuc, SlimAvailabilityEnum.UNKNOWN);
		}
		else if (ase == SlimAvailabilityEnum.UNKNOWN) {
			knownSuc.setAvailability(SlimAvailabilityEnum.ONLINE);
		}
		else { // offline
			knownSuc.setAvailability(ase);
		}
		updateListener(knownSuc);
	}

	protected void sendUpdateUserMessage(SlimUserContact pOldSettings) throws SlimException {
		SlimUserContact sender = model.getSettings().getContactInfo();

		SlimUpdateUserMessage suum = new SlimUpdateUserMessage(sender, pOldSettings);
		boolean updateSentToNetwork = false;
		for (Iterator it = getAllUserContact().iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (!suc.equals(model.getSettings().getContactInfo())) {
				model.getNetworkAdapter().send(suum, suc);
				updateSentToNetwork = true;
			}
		}
		if (!updateSentToNetwork) {
			model.getNetworkAdapter().reset();
		}
	}

	public void receiveUpdateUserMessage(SlimUpdateUserMessage pMessage) {
		SlimUserContact oldSuc = getOrAddUserByAddress(pMessage.getSender());
		try {
			pMessage.getSender().setName(oldSuc.getName());
		}
		catch (SlimException se) {
			// can not happen since name has been set successfuly for sender user
		}
		updateContact(oldSuc, pMessage.getSender());
		
		// acquittement car il se peut qu'il s'agisse de son premier message 
		// en lieu et place du availablity initial
		sendAvailabiltyMessage(pMessage.getSender(), SlimAvailabilityEnum.ONLINE);
	}

	
	protected synchronized SlimUserContact getOrAddUserByAddress(SlimUserContact pSuc) {
		SlimUserContact knownSuc = null;
		for (Iterator it = getAllUserContact().iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (suc.getHost().equalsIgnoreCase(pSuc.getHost()) 
					&& suc.getPort() == pSuc.getPort()) {
				knownSuc = suc;
				break;
			}
		}
		if (knownSuc == null) {
			knownSuc = pSuc;
			boolean lResult = false;
			String lOrignalName = pSuc.getName();
			int i = 0;
			while (!lResult) {
				lResult = addContact(pSuc);
				if (!lResult) {
					try {
						pSuc.setName(lOrignalName + i);
					}
					catch (SlimException se) {
						// should not happen since name has been accepted before
					}
				}
			}
		}
		return knownSuc;
	}
	
	protected void sendExitMessage() {
		for (Iterator it = getAllUserContact().iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (!suc.equals(model.getSettings().getContactInfo())) {
				sendAvailabiltyMessage(suc, SlimAvailabilityEnum.OFFLINE);
			}
		}
	}
	
	protected void sendWelcomeMessage() {
		for (Iterator it = getAllUserContact().iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (!suc.equals(model.getSettings().getContactInfo())) {
				sendAvailabiltyMessage(suc, SlimAvailabilityEnum.ONLINE);
			}
		}
	}

	public synchronized void refresh() {
		for (Iterator it = getAllUserContact().iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (!suc.equals(model.getSettings().getContactInfo())) {
				suc.setAvailability(SlimAvailabilityEnum.OFFLINE);
				sendAvailabiltyMessage(suc, SlimAvailabilityEnum.ONLINE);
			}
		}
	}
	
	
	public synchronized void refresh(SlimContact pContact) {
		if (!pContact.isGroup() && !(pContact.equals(model.getSettings().getContactInfo()))) {
			pContact.setAvailability(SlimAvailabilityEnum.OFFLINE);
			sendAvailabiltyMessage((SlimUserContact)pContact, SlimAvailabilityEnum.ONLINE);
		}
	}

	public synchronized boolean importContacts(File contactFile) throws IOException {
		boolean lError = false;
		BufferedReader reader = new BufferedReader(new FileReader(contactFile));
		String lLine = reader.readLine();
		Map nameTransaltion = new HashMap();
		while (lLine != null) {
			if (lLine.trim().length() != 0 && !lLine.startsWith("#")) {
				String[] splittedLine = lLine.split(";");
				if (splittedLine.length < 3) {
					SlimLogger.log("Invalid line format " + lLine + " must be match pattern USER;<Name>;<host>[;<port>] or GROUP;<Name>;<user1Name>[;<user2Name>...]. Line ignored");
					lError = true;
				}
				else {
					if (splittedLine[0].equalsIgnoreCase("USER")) {
						String lPort = SlimSettings.DEFAULT_PORT;
						if (splittedLine.length == 4 && model.getSettings().isPortUnlocked()) {
							lPort = splittedLine[3];
						}
						try {
							String lName = splittedLine[1].trim();
							SlimUserContact suc = getOrAddUserByAddress(new SlimUserContact(lName, splittedLine[2].trim(), lPort.trim()));
							if (!suc.getName().equals(lName)) {
								nameTransaltion.put(lName, suc.getName());
							}
						}
						catch (SlimException se) {
							SlimLogger.log("Invalid Contact on Line " + lLine + " details : " + se.getMessage() + ". Line ignored");
							lError = true;
						}
					}
					else if (splittedLine[0].equalsIgnoreCase("GROUP")) {
						SlimContact sc = (SlimContact)list.get(splittedLine[1]);
						if (sc != null && !sc.isGroup()) {
							SlimLogger.log("Invalid group on Line " + lLine + " details : User with same name allready exists. Line ignored");
							lError = true;
						}
						else {
							try {
								if (sc == null) {
									sc = new SlimGroupContact(splittedLine[1].trim(), new ArrayList());
								}
								for (int i= 2; i < splittedLine.length; i++) {
									String lUserName = splittedLine[i];
									String lLocalName = (String)nameTransaltion.get(lUserName);
									if (lLocalName != null) {
										lUserName = lLocalName;
									}
									SlimContact scm = (SlimContact)list.get(lUserName);
									if (scm == null || scm.isGroup()) {
										SlimLogger.log("Invalid group member on Line " + lLine + " details : " + splittedLine[i] + " is either unknown or a group. Line ignored");
										lError = true;
									}
									else {
										((SlimGroupContact)sc).addMembers((SlimUserContact)scm);
									}
								}
								if (((SlimGroupContact)sc).getMembers().size() == 0) {
									SlimLogger.log("Invalid group on Line " + lLine + " details : no valid members. Line ignored");
									lError = true;
								}
								else {
									list.put(splittedLine[1].trim(), sc);
								}
							}
							catch (SlimException se) {
								SlimLogger.log("Invalid Contact on Line " + lLine + " details : " + se.getMessage() + ". Line ignored");
								lError = true;
							}

						}
					}
				}
			}
			lLine = reader.readLine();
		}
		reader.close();
		try {
			// done here because group are not added with update/addConatct methods
			model.storeSettings();
		}
		catch (IOException ioe) {
			// ignore it during runtime, exception logged at exit
		}
		return lError;
	}

	public synchronized void exportContacts(File contactFile) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(contactFile));
		writer.write("#Lanslim Contact file must match pattern defined below (without # of course)\n");
		writer.write("#USER;<Name>;<host>[;<port>]\n");
		writer.write("#GROUP;<Name>;<user1Name>[;<user2Name>...]\n");
		writer.write("#Be careful to define users before defining group containing them\n\n");
		
		Iterator it = getAllUserContact().iterator();
		while (it.hasNext()) {
			SlimUserContact suc = (SlimUserContact)it.next();
			writer.write("USER;" + suc.getName() + ";" + suc.getHost() + ";" + suc.getPort() + "\n");
		}
		it = getAllGroupContact().iterator();
		while (it.hasNext()) {
			SlimGroupContact sgc = (SlimGroupContact)it.next();
			writer.write("GROUP;" + sgc.getName() + ";");
			for (Iterator it2 = sgc.getMembers().iterator(); it2.hasNext();) {
				SlimContact m = (SlimContact)it2.next();
				writer.write(m.getName() + ";");
			}
			writer.write("\n");
		}
		writer.flush();
		writer.close();
	}

}
