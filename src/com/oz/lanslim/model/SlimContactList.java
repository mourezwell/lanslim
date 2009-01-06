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
import java.util.Set;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.StringConstants;
import com.oz.lanslim.message.SlimAvailabilityUserMessage;
import com.oz.lanslim.message.SlimTalkMessage;
import com.oz.lanslim.message.SlimUpdateUserMessage;

public class SlimContactList {
	
	private static final String SLIM_CONTACT_PROPS_PREFIX = "slim.contact."; //$NON-NLS-1$
	private static final String USERNAME_SUFFIX = ".username"; //$NON-NLS-1$
	private static final String USERIP_SUFFIX =  ".userip"; //$NON-NLS-1$
	private static final String USERPORT_SUFFIX =  ".userport"; //$NON-NLS-1$
	private static final String GROUPNAME_SUFFIX = ".groupname"; //$NON-NLS-1$
	private static final String MEMBERS_PREFIX = ".members."; //$NON-NLS-1$
	private static final String CATEGORYNAME_SUFFIX = ".categoryname"; //$NON-NLS-1$
	private static final String CATEGORYMEMBERS_PREFIX = ".categorymembers."; //$NON-NLS-1$
	private static final String CATEGORYEXPANDED_SUFFIX = ".categoryexpanded"; //$NON-NLS-1$
	
	public static String CATEGORY_GROUP = "Groups"; //$NON-NLS-1$
	public static String CATEGORY_UNDEFINED = "Undefined"; //$NON-NLS-1$

	public static final String USER_HOST_SEPARATOR = "/"; //$NON-NLS-1$
	public static final String TAG_START = "<"; //$NON-NLS-1$
	public static final String TAG_END = ">"; //$NON-NLS-1$
	
	public static final String IMPORT_COMMENT = "#"; //$NON-NLS-1$
	public static final String IMPORT_SEPARATOR = ";"; //$NON-NLS-1$
	public static final String IMPORT_USER = "USER"; //$NON-NLS-1$
	public static final String IMPORT_GROUP = "GROUP"; //$NON-NLS-1$
	public static final String IMPORT_OPTIONNAL_START = "["; //$NON-NLS-1$
	public static final String IMPORT_OPTIONNAL_END = "]"; //$NON-NLS-1$

	private Map list = null;
	private SlimContactListener listener = null;
	private SlimCategoryListener catListener = null;
	private PeopleInAvailabilityListener peopleInListener = null;
	private SlimModel model = null;
	
	private Map categoryByContact = null;
	private Map categories = null;

	
	public SlimContactList(SlimModel pModel) {
		model = pModel;
		list = new HashMap();
		list.put(model.getSettings().getContactInfo().getName(), model.getSettings().getContactInfo());
		categoryByContact = new HashMap();
		categories = new HashMap();

		CATEGORY_GROUP = Externalizer.getString("LANSLIM.181"); //$NON-NLS-1$
		CATEGORY_UNDEFINED = Externalizer.getString("LANSLIM.182"); //$NON-NLS-1$
		
		categories.put(CATEGORY_GROUP, new Boolean(false));
		categories.put(CATEGORY_UNDEFINED, new Boolean(false));
		
	}
	
	public SlimContactList(SlimModel pModel, Properties p) {
		this(pModel);
		List groupNb = new ArrayList();
		int i = 1;
		boolean endContact = false;
		while (!endContact) {
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
							SlimLogger.logException("ContactList.constructor", e); //$NON-NLS-1$
						}
					}
				}
				else {
					lKey = SLIM_CONTACT_PROPS_PREFIX + i + CATEGORYNAME_SUFFIX;
					if (p.getProperty(lKey) != null) {
						String lCatName = p.getProperty(lKey);
						lKey = SLIM_CONTACT_PROPS_PREFIX + i + CATEGORYEXPANDED_SUFFIX;
						String lCatExpanded = p.getProperty(lKey);
						categories.put(lCatName, Boolean.valueOf(lCatExpanded));
						int j = 1;
						boolean endCategory = false;
						while (!endCategory) { 
							lKey = SLIM_CONTACT_PROPS_PREFIX + i + CATEGORYMEMBERS_PREFIX + j  + USERNAME_SUFFIX;
							String lUserName = p.getProperty(lKey);
							if (lUserName == null) {
								endCategory = true;
							}
							else {
								categoryByContact.put(lUserName, lCatName);
								j = j + 1;
							}
						}
					}
					else {
						endContact = true;
					}
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
				boolean endGroup = false;
				while (!endGroup) {
					lKey = SLIM_CONTACT_PROPS_PREFIX + j + MEMBERS_PREFIX + k  + USERNAME_SUFFIX;
					lName = p.getProperty(lKey);
					if (lName == null) {
						endGroup = true;
					}
					else {
						if (list.containsKey(lName.trim()) && !((SlimContact)list.get(lName.trim())).isGroup()) {
							g.addMember((SlimUserContact)list.get(lName.trim()));
							list.put(g.getName(), g);
						}
						else {
							SlimLogger.log(Externalizer.getString("LANSLIM.180", lName, g.getName())); //$NON-NLS-1$
						}
					}
					k = k + 1;
				}					
			}
			catch (SlimException e) {
				SlimLogger.logException("ContactList.constructor", e); //$NON-NLS-1$
			}
		}
		
		// in case they are not already defined
		if (!categories.containsKey(CATEGORY_GROUP)) {
			categories.put(CATEGORY_GROUP, new Boolean(true));
		}
		if (!categories.containsKey(CATEGORY_UNDEFINED)) {
			categories.put(CATEGORY_UNDEFINED, new Boolean(true));
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
	
	public synchronized List getAllContacts() {
		List l = new ArrayList();
		for (Iterator it = list.values().iterator(); it.hasNext();) {
			SlimContact c = (SlimContact)it.next();
			if (!(c.equals(model.getSettings().getContactInfo()))) {
				l.add(c);
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

		SlimContact existingContactWithSameName = (SlimContact)list.get(pContact.getName());
		if (existingContactWithSameName != null) {
			if (pContact.isGroup() || existingContactWithSameName.isGroup()) {
				return false;
			}
			if (existingContactWithSameName.getAvailability() != SlimAvailabilityEnum.OFFLINE) {
				return false;
			}
			try {
				((SlimUserContact)existingContactWithSameName).setHost(
						((SlimUserContact)pContact).getHost());
				((SlimUserContact)existingContactWithSameName).setPort(
						String.valueOf(((SlimUserContact)pContact).getPort()));
			}
			catch (SlimException se) {
				return false;
			}
		}
		else {
			existingContactWithSameName = pContact;
			list.put(pContact.getName(), pContact);
		}
		
		model.storeSettings();
		updateListener();
		if (existingContactWithSameName.getAvailability() != SlimAvailabilityEnum.UNKNOWN) {  
			sendAvailabiltyMessage((SlimUserContact)pContact, SlimAvailabilityEnum.ONLINE);
		}
		return true;
	}

	public synchronized boolean updateContact(SlimContact pOldContact, SlimContact pNewContact) {
		
		SlimContact sc = (SlimContact)list.get(pNewContact.getName());
		if (sc != null && !sc.equals(pOldContact) 
				&& sc.getAvailability().equals(SlimAvailabilityEnum.ONLINE)) {
			return false;
		}

		String lOldName = pOldContact.getName();
		try {
			pOldContact.setName(pNewContact.getName());
			pOldContact.setAvailability(pNewContact.getAvailability());
		
			if (pOldContact.isGroup()) {
				((SlimGroupContact)pOldContact).updateMembers((((SlimGroupContact)pNewContact).getMembers()));
			}
			else {
				((SlimUserContact)pOldContact).setHost(((SlimUserContact)pNewContact).getHost());
				((SlimUserContact)pOldContact).setPort(Integer.toString(((SlimUserContact)pNewContact).getPort()));
			}
		}
		catch (SlimException e) {
			// attributes already validated since they are extracted from valid contact
		}
		
		list.remove(lOldName);
		list.put(pOldContact.getName(), pOldContact);
		
		model.storeSettings();
		updateListener();
		
		if (pOldContact.getAvailability() != SlimAvailabilityEnum.UNKNOWN) {  
			sendAvailabiltyMessage((SlimUserContact)pOldContact, SlimAvailabilityEnum.ONLINE);
		}
		return true;
	}

	public synchronized void removeContactByName(String pContactName) {
		SlimContact lRemoved = (SlimContact)list.remove(pContactName);
		if (lRemoved != null) {
			List lGroupList = getAllGroupContact();
			for (Iterator it = lGroupList.iterator(); it.hasNext();) {
				SlimGroupContact sgc = (SlimGroupContact)it.next();
				sgc.removeMember(lRemoved);
			}
		}		
		model.storeSettings();
		updateListener();
	}

	
	public synchronized Properties toProperties() {
		Map contactByCategory = new HashMap();
		for (Iterator it = categories.keySet().iterator(); it.hasNext();) {
			contactByCategory.put(it.next(), new ArrayList());
		}

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
					p.put(SLIM_CONTACT_PROPS_PREFIX + i + MEMBERS_PREFIX + j + USERNAME_SUFFIX, m.getName());
					j = j + 1;
				}					
			}
			else {
				SlimUserContact uc = (SlimUserContact)c;
				p.put(SLIM_CONTACT_PROPS_PREFIX + i + USERNAME_SUFFIX, uc.getName());
				p.put(SLIM_CONTACT_PROPS_PREFIX + i + USERIP_SUFFIX, uc.getHost());
				p.put(SLIM_CONTACT_PROPS_PREFIX + i + USERPORT_SUFFIX, Integer.toString(uc.getPort()));
				
				if (categoryByContact.containsKey(uc.getName())) {
					String lCat = (String)categoryByContact.get(uc.getName());
					List lMemb = (List)contactByCategory.get(lCat);
					lMemb.add(uc.getName());
				}
			}
			i = i + 1;
		}
		
		for (Iterator it = categories.keySet().iterator(); it.hasNext();) {
			String cat = (String)it.next();
			if (!CATEGORY_GROUP.equals(cat) && !CATEGORY_UNDEFINED.equals(cat)) {
				List members = (List)contactByCategory.get(cat);
				p.put(SLIM_CONTACT_PROPS_PREFIX + i + CATEGORYNAME_SUFFIX, cat);
				p.put(SLIM_CONTACT_PROPS_PREFIX + i + CATEGORYEXPANDED_SUFFIX, categories.get(cat).toString());
				int j = 1; 
				for (Iterator it2 = members.iterator(); it2.hasNext();) {
					String uc = (String)it2.next();
					p.put(SLIM_CONTACT_PROPS_PREFIX + i + CATEGORYMEMBERS_PREFIX + j + USERNAME_SUFFIX, uc);
					j = j + 1;
				}
				i = i + 1;
			}
		}

		return p;
	}

	public synchronized void updateListener() {
		if (listener != null) {
			listener.updateContacts();
		}
		if (peopleInListener != null) {
			peopleInListener.updateAvailabilities();
		}
	}

	public void registerPeopleInListener(PeopleInAvailabilityListener pListener) {
		peopleInListener = pListener;
	}

	public void registerContactListener(SlimContactListener pListener) {
		listener = pListener;
	}
	
	public void registerCategoryListener(SlimCategoryListener pListener) {
		catListener = pListener;
	}
	

	protected void sendAvailabiltyMessage(SlimUserContact pContact, SlimAvailabilityEnum pStatus) {
		
		try {
			SlimUserContact sender = model.getSettings().getContactInfo();
			SlimAvailabilityUserMessage saum = new SlimAvailabilityUserMessage(sender, pStatus);
			model.getNetworkAdapter().send(saum, pContact);
		}
		catch (SlimException lException) {
			// should not happen unless setting badly set
		}
	}

	
	private void sendEnqueuedMessage(SlimUserContact pContact) {
		
		try {
			SlimTalkMessage lMsg = pContact.getOldestMessageInQueue();
			while (lMsg != null) {
				model.getNetworkAdapter().send(lMsg, pContact);
				lMsg = pContact.getOldestMessageInQueue();
			}
		}
		catch (SlimException lException) {
			// should not happen unless setting badly set
		}
	}

	
	public synchronized void receiveAvailabiltyMessage(SlimAvailabilityUserMessage pMessage) {
		
		SlimUserContact knownSuc = getOrAddUserByAddress(pMessage.getSender());
		
		SlimAvailabilityEnum lNew = pMessage.getAvailability();
		SlimAvailabilityEnum lOld = knownSuc.getAvailability();
		
		if (lNew == SlimAvailabilityEnum.ONLINE) {
			knownSuc.setAvailability(SlimAvailabilityEnum.ONLINE);
			sendAvailabiltyMessage(knownSuc, SlimAvailabilityEnum.UNKNOWN);
		}
		else if (lNew == SlimAvailabilityEnum.UNKNOWN) {
			knownSuc.setAvailability(SlimAvailabilityEnum.ONLINE);
		}
		else { // offline
			knownSuc.setAvailability(lNew);
		}
		
		if (lNew != SlimAvailabilityEnum.OFFLINE && lOld == SlimAvailabilityEnum.OFFLINE) {
			sendEnqueuedMessage(knownSuc);
		}
		updateListener();
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

	public synchronized void receiveUpdateUserMessage(SlimUpdateUserMessage pMessage) {
		
		SlimUserContact oldSuc = getOrAddUserByAddress(pMessage.getOldSettings());
		
		boolean lUpdated = updateContact(oldSuc, pMessage.getSender());
		
		if (lUpdated) {
			sendAvailabiltyMessage(oldSuc, SlimAvailabilityEnum.ONLINE);
		}
	}

	
	protected synchronized SlimUserContact getOrAddUserByAddress(SlimUserContact pSuc) {
		
		SlimUserContact knownUserByAdress = null;
		for (Iterator it = getAllUserContact().iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (suc.getHost().equalsIgnoreCase(pSuc.getHost()) 
					&& suc.getPort() == pSuc.getPort()) {
				knownUserByAdress = suc;
				break;
			}
		}
		
		if (knownUserByAdress == null) {
			knownUserByAdress = pSuc;
			boolean lResult = false;
			String lOrignalName = pSuc.getName();
			lResult = addContact(pSuc);
			if (!lResult) {
				try {
					pSuc.setName(lOrignalName + USER_HOST_SEPARATOR + pSuc.getHost());
					addContact(pSuc);
				}
				catch (SlimException se) {
					SlimLogger.log(Externalizer.getString("LANSLIM.183", pSuc.getName())); //$NON-NLS-1$
				}
			}
		}
		else {
			if (!knownUserByAdress.getName().equals(pSuc.getName())) {
				updateContact(knownUserByAdress, pSuc);
			}
		}
		return knownUserByAdress;
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
		Map nameTranslation = new HashMap();
		while (lLine != null) {
			if (lLine.trim().length() != 0 && !lLine.startsWith(IMPORT_COMMENT)) {
				String[] splittedLine = lLine.split(IMPORT_SEPARATOR);
				if (splittedLine.length < 3) {
					SlimLogger.log(Externalizer.getString("LANSLIM.184", lLine) //$NON-NLS-1$
						+ StringConstants.SPACE	+ IMPORT_USER + Externalizer.getString("LANSLIM.191")  //$NON-NLS-1$
						+ StringConstants.SPACE + Externalizer.getString("LANSLIM.69")  //$NON-NLS-1$
						+ StringConstants.SPACE + IMPORT_GROUP + Externalizer.getString("LANSLIM.192")  //$NON-NLS-1$
						+ StringConstants.SPACE + Externalizer.getString("LANSLIM.193")); //$NON-NLS-1$
					lError = true;
				}
				else {
					if (splittedLine[0].equalsIgnoreCase(IMPORT_USER)) {
						String lPort = SlimSettings.DEFAULT_PORT;
						if (splittedLine.length == 4 && model.getSettings().isPortUnlocked()) {
							lPort = splittedLine[3];
						}
						try {
							String lName = splittedLine[1].trim();
							SlimUserContact suc = getOrAddUserByAddress(new SlimUserContact(lName, splittedLine[2].trim(), lPort.trim()));
							if (!suc.getName().equals(lName)) {
								nameTranslation.put(lName, suc.getName());
							}
						}
						catch (SlimException se) {
							SlimLogger.log(Externalizer.getString("LANSLIM.194", lLine,  se.getMessage()) //$NON-NLS-1$
									+ StringConstants.SPACE + Externalizer.getString("LANSLIM.193")); //$NON-NLS-1$ 
							lError = true;
						}
					}
					else if (splittedLine[0].equalsIgnoreCase(IMPORT_GROUP)) {
						SlimContact sc = (SlimContact)list.get(splittedLine[1]);
						if (sc != null && !sc.isGroup()) {
							SlimLogger.log(Externalizer.getString("LANSLIM.186", lLine,  Externalizer.getString("LANSLIM.123"))//$NON-NLS-1$ //$NON-NLS-2$
									+ StringConstants.SPACE + Externalizer.getString("LANSLIM.193")); //$NON-NLS-1$ 
							lError = true;
						}
						else {
							try {
								if (sc == null) {
									sc = new SlimGroupContact(splittedLine[1].trim(), new ArrayList());
								}
								for (int i= 2; i < splittedLine.length; i++) {
									String lUserName = splittedLine[i];
									String lLocalName = (String)nameTranslation.get(lUserName);
									if (lLocalName != null) {
										lUserName = lLocalName;
									}
									SlimContact scm = (SlimContact)list.get(lUserName);
									if (scm == null || scm.isGroup()) {
										SlimLogger.log(Externalizer.getString("LANSLIM.188", lLine, splittedLine[i]) //$NON-NLS-1$
												+ StringConstants.SPACE + Externalizer.getString("LANSLIM.193")); //$NON-NLS-1$
										lError = true;
									}
									else {
										((SlimGroupContact)sc).addMember((SlimUserContact)scm);
									}
								}
								if (((SlimGroupContact)sc).getMembers().size() == 0) {
									SlimLogger.log(Externalizer.getString("LANSLIM.186", lLine, Externalizer.getString("LANSLIM.190")) //$NON-NLS-1$  //$NON-NLS-2$ 
											+ StringConstants.SPACE + Externalizer.getString("LANSLIM.193")); //$NON-NLS-1$ 
								lError = true;
								}
								else {
									list.put(splittedLine[1].trim(), sc);
								}
							}
							catch (SlimException se) {
								SlimLogger.log(Externalizer.getString("LANSLIM.186", lLine,  se.getMessage()) //$NON-NLS-1$
										+ StringConstants.SPACE + Externalizer.getString("LANSLIM.193")); //$NON-NLS-1$ 
								lError = true;
							}
						}
					}
					else  {
						SlimLogger.log(Externalizer.getString("LANSLIM.184", lLine) //$NON-NLS-1$
								+ StringConstants.SPACE	+ IMPORT_USER + Externalizer.getString("LANSLIM.191")  //$NON-NLS-1$
								+ StringConstants.SPACE + Externalizer.getString("LANSLIM.69")  //$NON-NLS-1$
								+ StringConstants.SPACE + IMPORT_GROUP + Externalizer.getString("LANSLIM.192")  //$NON-NLS-1$
								+ StringConstants.SPACE + Externalizer.getString("LANSLIM.193")); //$NON-NLS-1$
						lError = true;
					}
				}
			}
			lLine = reader.readLine();
		}
		reader.close();
		model.storeSettings();
		return lError;
	}

	public synchronized void exportContacts(File contactFile) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(contactFile));
		writer.write(IMPORT_COMMENT + Externalizer.getString("LANSLIM.195", IMPORT_COMMENT) + StringConstants.LINE_SEPARATOR); //$NON-NLS-1$
		writer.write(IMPORT_COMMENT + IMPORT_USER + Externalizer.getString("LANSLIM.191") + StringConstants.LINE_SEPARATOR); //$NON-NLS-1$
		writer.write(IMPORT_COMMENT + IMPORT_GROUP + Externalizer.getString("LANSLIM.192") + StringConstants.LINE_SEPARATOR); //$NON-NLS-1$
		writer.write(IMPORT_COMMENT + Externalizer.getString("LANSLIM.196") + StringConstants.LINE_SEPARATOR //$NON-NLS-1$
				 + StringConstants.LINE_SEPARATOR);
		
		Iterator it = getAllUserContact().iterator();
		while (it.hasNext()) {
			SlimUserContact suc = (SlimUserContact)it.next();
			writer.write(IMPORT_USER + IMPORT_SEPARATOR + suc.getName() + IMPORT_SEPARATOR 
					+ suc.getHost() + IMPORT_SEPARATOR + suc.getPort() + StringConstants.LINE_SEPARATOR);
		}
		it = getAllGroupContact().iterator();
		while (it.hasNext()) {
			SlimGroupContact sgc = (SlimGroupContact)it.next();
			writer.write(IMPORT_GROUP + IMPORT_SEPARATOR + sgc.getName() + IMPORT_SEPARATOR);
			for (Iterator it2 = sgc.getMembers().iterator(); it2.hasNext();) {
				SlimContact m = (SlimContact)it2.next();
				writer.write(m.getName() + IMPORT_SEPARATOR);
			}
			writer.write(StringConstants.LINE_SEPARATOR);
		}
		writer.flush();
		writer.close();
	}

	// CATEGORY MANAGEMENT
	
	public synchronized String getCategory(SlimUserContact pContact) {
		return (String)categoryByContact.get(pContact.getName());
	}

	public synchronized Set getAllCategories() {
		return categories.keySet();
	}
	
	public synchronized Boolean isExpanded(String pCat) {
		return (Boolean)categories.get(pCat);
	}
	
	public synchronized void setExpanded(String pCat, Boolean pExpanded) {
		if (!pExpanded.equals(categories.get(pCat))) { 
			categories.put(pCat, pExpanded);
			model.storeSettings();
		}
	}


	public synchronized boolean addCategory(String pNewCat, Boolean pExpanded) {
		if (categories.containsKey(pNewCat)) {
			return false;
		}
		categories.put(pNewCat, pExpanded);
		if (catListener != null) {
			catListener.addCategory(pNewCat, pExpanded);
		}
		model.storeSettings();
		return true;
	}
	
	public synchronized void moveUserIntoCategory(SlimUserContact pContact, String pCat) {
		String lOldCat = (String)categoryByContact.get(pContact.getName());
		categoryByContact.put(pContact.getName(), pCat);
		if (catListener != null) {
			catListener.moveUserIntoCategory(pContact, lOldCat, pCat);
		}
		model.storeSettings();
	}

	public synchronized boolean removeCategory(String pCat) {
		if (pCat.equals(CATEGORY_GROUP) || pCat.equals(CATEGORY_UNDEFINED)) {
			return false;
		}
		String userName = null;
		Iterator it = categoryByContact.keySet().iterator();
		while (it.hasNext()) {
			userName = (String)it.next();
			if (categoryByContact.get(userName).equals(pCat)) {
				categoryByContact.remove(userName);
			}
		}
		categories.remove(pCat);
		if (catListener != null) {
			catListener.removeCategory(pCat);
		}
		model.storeSettings();
		return true;
	}

	public synchronized boolean renameCategory(String pOldCat, String pNewCat) {
		if (categories.containsKey(pNewCat) || pOldCat.equalsIgnoreCase(CATEGORY_GROUP) 
				|| pOldCat.equalsIgnoreCase(CATEGORY_UNDEFINED)) {
			return false;
		}
		categories.put(pNewCat, categories.get(pOldCat));
		String userName = null;
		Iterator it = categoryByContact.keySet().iterator();
		while (it.hasNext()) {
			userName = (String)it.next();
			if (categoryByContact.get(userName).equals(pOldCat)) {
				categoryByContact.put(userName, pNewCat);
			}
		}
		categories.remove(pOldCat);
		if (catListener != null) {
			catListener.renameCategory(pOldCat, pNewCat);
		}
		model.storeSettings();
		return true;
	}
	
	public SlimUserContact getSettingsUser() {
		return model.getSettings().getContactInfo();
	}
	
}
