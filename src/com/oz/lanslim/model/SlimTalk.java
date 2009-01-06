package com.oz.lanslim.model;

import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.StringConstants;
import com.oz.lanslim.gui.SlimIcon;
import com.oz.lanslim.message.SlimExcludeTalkMessage;
import com.oz.lanslim.message.SlimExitTalkMessage;
import com.oz.lanslim.message.SlimInviteTalkMessage;
import com.oz.lanslim.message.SlimNewTalkMessage;
import com.oz.lanslim.message.SlimUpdateTalkMessage;

public class SlimTalk {

	public static final String[] SMILEY_TEXT = new String[] {
		":)", ":(", "=(", ":D", ";)", ":o", ":[", ":#", ":*", "<3"  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
	};

	public static final String[] SMILEY_REG_EXP = new String[] {
		"\\:\\)", "\\:\\(", "\\=\\(", "\\:D", ";\\)", "\\:o", "\\:\\[", "\\:\\#", "\\:\\*", "<3"  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
	};

	public static final String NEW_LINE_REG_EXP = "\\n"; //$NON-NLS-1$

	private static final String PEOPLE_SEPARATOR= ", "; //$NON-NLS-1$

	private String title = null;
	private String id = null;
	private List peopleIn = null;
	private String messageFontColor = null;
	private String messageFontSize = null;
	private String text = null;
	private SlimTalkListener listener = null;
	private SlimModel model = null;
	private boolean leader = false;

	public SlimTalk(SlimModel pModel, String pTitle, String pId, List pPeople, SlimUserContact pLeader, 
			String pDate) {
		model = pModel;
		title = pTitle;
		peopleIn = pPeople;
		id = pId;
		messageFontColor = pModel.getSettings().getColor();
		messageFontSize = String.valueOf(pModel.getSettings().getFontSize());
		SlimUserContact lYou = pModel.getContacts().getSettingsUser();
		
		if (pLeader.equals(lYou)) {
			leader = true;
			text = HTMLConstants.getHeader(HTMLConstants.SYSTEM, pDate) + HTMLConstants.BOLD 
				+ Externalizer.getString("LANSLIM.27") //$NON-NLS-1$
				+ HTMLConstants.ENDBOLD + HTMLConstants.NEWLINE;
		}
		else {
			leader = false;
			text = HTMLConstants.getHeader(HTMLConstants.SYSTEM, pDate) + HTMLConstants.BOLD 
				+ Externalizer.getString("LANSLIM.175",	lYou.getName(), pLeader.getName()) //$NON-NLS-1$
				+ HTMLConstants.ENDBOLD + HTMLConstants.NEWLINE;
		}
	}

	public SlimTalk(SlimModel pModel, String pTitle, List pPeople) {
		this(pModel, pTitle, (pTitle + (int)Math.round(Math.random() * 1000000)), pPeople, 
				pModel.getContacts().getSettingsUser(), HTMLConstants.TIME_FORMAT.format(new Date()));
	}
	
	public String getId() {
		return id;
	}

	public boolean isLeader() {
		return leader;
	}
	
	// gestion des membres
	public synchronized void addPeople(SlimUserContact pInvitedContact) throws SlimException {
		
		if (!peopleIn.contains(pInvitedContact)) {
			String date = HTMLConstants.TIME_FORMAT.format(new Date());
			sendInviteTalkMessage(pInvitedContact, date);
			text = text + HTMLConstants.getHeader(HTMLConstants.SYSTEM, date) + HTMLConstants.BOLD 
				+ Externalizer.getString("LANSLIM.175",	//$NON-NLS-1$
						pInvitedContact.getName(), model.getSettings().getContactInfo().getName())
				+ HTMLConstants.ENDBOLD + HTMLConstants.NEWLINE;
			if (listener !=  null) {
				listener.notifyTextTalkUpdate(this);
			}
			peopleIn.add(pInvitedContact);
			sendNewTalkMessage(pInvitedContact);

			if (listener !=  null) {
				listener.notifyTextTalkUpdate(this);
			}
		}
		
	}

	public synchronized void removePeople(SlimUserContact pExcludedContacts) throws SlimException {
		
		if (peopleIn.contains(pExcludedContacts)) {
			String date = HTMLConstants.TIME_FORMAT.format(new Date());
			sendExcludeTalkMessage(pExcludedContacts, date);
			text = text + HTMLConstants.getHeader(HTMLConstants.SYSTEM, date) + HTMLConstants.BOLD 
				+ Externalizer.getString("LANSLIM.177",	//$NON-NLS-1$
						pExcludedContacts.getName(), model.getSettings().getContactInfo().getName())
				+ HTMLConstants.ENDBOLD + HTMLConstants.NEWLINE;
			if (model.getSettings().getContactInfo().equals(pExcludedContacts)) {
				text = text + HTMLConstants.getHeader(HTMLConstants.SYSTEM, date) + HTMLConstants.BOLD 
					+ Externalizer.getString("LANSLIM.178") //$NON-NLS-1$
					+ HTMLConstants.ENDBOLD + HTMLConstants.NEWLINE;
				peopleIn.clear();
			}
			else {
				peopleIn.remove(pExcludedContacts) ;
			}
			if (listener !=  null) {
				listener.notifyTextTalkUpdate(this);
			}
		}
	}

	public synchronized List getPeopleIn() {
		return peopleIn;
	}
	
	public synchronized String getPeopleInListAsString() {
		String lResult = StringConstants.EMPTY; 
		for (Iterator it = peopleIn.iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			lResult = lResult + suc.getName() + PEOPLE_SEPARATOR;
		}
		return lResult;
	}
	
	private void sendInviteTalkMessage(SlimUserContact pContact, String pDate) throws SlimException {
		SlimUserContact sender = model.getSettings().getContactInfo();
		SlimInviteTalkMessage setm = new SlimInviteTalkMessage(sender, getId(), pContact, pDate);
		for (Iterator it = peopleIn.iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (!suc.equals(model.getSettings().getContactInfo())) {
				if (suc.getAvailability() == SlimAvailabilityEnum.OFFLINE) {
					suc.addMessageInQueue(setm);
				}
				else {
					model.getNetworkAdapter().send(setm, suc);
				}
			}
		}
	}

	
	private void sendExcludeTalkMessage(SlimUserContact pContact, String pDate) throws SlimException {
		SlimUserContact sender = model.getSettings().getContactInfo();
		SlimExcludeTalkMessage setm = new SlimExcludeTalkMessage(sender, getId(), pContact, pDate);
		for (Iterator it = peopleIn.iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (!suc.equals(model.getSettings().getContactInfo())) {
				if (suc.getAvailability() == SlimAvailabilityEnum.OFFLINE) {
					suc.addMessageInQueue(setm);
				}
				else {
					model.getNetworkAdapter().send(setm, suc);
				}
			}
		}
	}

	public synchronized void receiveInviteTalkMessage(SlimInviteTalkMessage pMessage) {
		
		if (isLeader()) {
			for (Iterator it = peopleIn.iterator(); it.hasNext();) {
				SlimUserContact suc = (SlimUserContact)it.next();
				if (suc.getAvailability() == SlimAvailabilityEnum.OFFLINE) {
					suc.addMessageInQueue(pMessage);
				}
			}
		}

		SlimUserContact knownSuc = model.getContacts().getOrAddUserByAddress(pMessage.getNewContact());
		if (knownSuc.getAvailability() == SlimAvailabilityEnum.OFFLINE) {
			model.getContacts().sendAvailabiltyMessage(knownSuc, SlimAvailabilityEnum.ONLINE);
		}
		if (!peopleIn.contains(knownSuc)) {
			peopleIn.add(knownSuc);
			text = text + HTMLConstants.getHeader(HTMLConstants.SYSTEM, pMessage.getDate()) 
				+ HTMLConstants.BOLD + Externalizer.getString("LANSLIM.175",	//$NON-NLS-1$
						pMessage.getNewContact().getName(), pMessage.getSender().getName())
				+ HTMLConstants.ENDBOLD + HTMLConstants.NEWLINE;
			if (listener !=  null) {
				listener.notifyTextTalkUpdate(this);
			}
		}
	}
	
	public synchronized void receiveExcludeTalkMessage(SlimExcludeTalkMessage pMessage) {

		if (isLeader()) {
			for (Iterator it = peopleIn.iterator(); it.hasNext();) {
				SlimUserContact suc = (SlimUserContact)it.next();
				if (suc.getAvailability() == SlimAvailabilityEnum.OFFLINE) {
					suc.addMessageInQueue(pMessage);
				}
			}
		}
		
		SlimUserContact knownSuc = model.getContacts().getOrAddUserByAddress(pMessage.getExcludedContact());
		if (peopleIn.contains(knownSuc) || model.getSettings().getContactInfo().equals(knownSuc)) {
			peopleIn.remove(knownSuc);
			text = text + HTMLConstants.getHeader(HTMLConstants.SYSTEM, pMessage.getDate()) 
				+ HTMLConstants.BOLD + Externalizer.getString("LANSLIM.177",	//$NON-NLS-1$
						pMessage.getExcludedContact().getName(), pMessage.getSender().getName())
				+ HTMLConstants.ENDBOLD + HTMLConstants.NEWLINE;
			if (model.getSettings().getContactInfo().equals(knownSuc)) {
				text = text + HTMLConstants.getHeader(HTMLConstants.SYSTEM, pMessage.getDate()) 
					+ HTMLConstants.BOLD + Externalizer.getString("LANSLIM.178") //$NON-NLS-1$
					+ HTMLConstants.ENDBOLD + HTMLConstants.NEWLINE;
				peopleIn.clear();
			}
			if (listener !=  null) {
				listener.notifyTextTalkUpdate(this);
			}
		}
	}

	protected synchronized void sendExitTalkMessage() throws SlimException {

		if (isLeader()) {
			for (Iterator it = peopleIn.iterator(); it.hasNext();) {
				SlimUserContact suc = (SlimUserContact)it.next();
				if (suc.getAvailability() == SlimAvailabilityEnum.OFFLINE) {
					removePeople(suc);
				}
			}
		}
		
		String date = HTMLConstants.TIME_FORMAT.format(new Date());
		SlimUserContact sender = model.getSettings().getContactInfo();
		SlimExitTalkMessage setm = new SlimExitTalkMessage(sender, getId(), date);
		for (Iterator it = peopleIn.iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (!suc.equals(model.getSettings().getContactInfo())) {
				if (suc.getAvailability() == SlimAvailabilityEnum.OFFLINE) {
					suc.addMessageInQueue(setm);
				}
				else {
					model.getNetworkAdapter().send(setm, suc);
				}
			}
		}
	}
	
	public synchronized void receiveExitTalkMessage(SlimExitTalkMessage pMessage) {

		if (isLeader()) {
			for (Iterator it = peopleIn.iterator(); it.hasNext();) {
				SlimUserContact suc = (SlimUserContact)it.next();
				if (suc.getAvailability() == SlimAvailabilityEnum.OFFLINE) {
					suc.addMessageInQueue(pMessage);
				}
			}
		}

		SlimUserContact knownSuc = model.getContacts().getOrAddUserByAddress(pMessage.getSender());
		if (peopleIn.contains(knownSuc)) {
			peopleIn.remove(knownSuc);
			text = text + HTMLConstants.getHeader(HTMLConstants.SYSTEM, pMessage.getDate()) 
				+ HTMLConstants.BOLD + Externalizer.getString("LANSLIM.179", pMessage.getSender().getName()) //$NON-NLS-1$
				+ HTMLConstants.ENDBOLD + HTMLConstants.NEWLINE;
			if (listener !=  null) {
				listener.notifyTextTalkUpdate(this);
			}
		}
	}

	protected synchronized void sendNewTalkMessage() throws SlimException {
		for (Iterator it = peopleIn.iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (!suc.equals(model.getSettings().getContactInfo())) {
				sendNewTalkMessage(suc);
			}
		}
	}
	
	// receiveNewTalkMessage is located in TalkList class because it is call before Talk creation  
	private void sendNewTalkMessage(SlimUserContact pContact) throws SlimException {
		String date = HTMLConstants.TIME_FORMAT.format(new Date());
		SlimUserContact sender = model.getSettings().getContactInfo();
		SlimNewTalkMessage sntm = new SlimNewTalkMessage(sender, getId(), getTitle(), peopleIn, date);

		if (pContact.getAvailability() == SlimAvailabilityEnum.OFFLINE) {
			pContact.addMessageInQueue(sntm);
		}
		else {
			model.getNetworkAdapter().send(sntm, pContact);
		}
	}
	
	public synchronized void sendUpdateTalkMessage(String pRawMessage, boolean pBuildBefore) 
		throws SlimException {
		
		String lMessageToSend = pRawMessage;
		String date = HTMLConstants.TIME_FORMAT.format(new Date());

		if (pBuildBefore) {
			lMessageToSend = prepareMessageBeforeSending(pRawMessage);
		}
		SlimUserContact sender = model.getSettings().getContactInfo();
		text = text + buildMessageBeforeDisplaying(lMessageToSend, sender, date);
		if (listener !=  null) {
			listener.notifyTextTalkUpdate(this);
		}
		SlimUpdateTalkMessage sutm = new SlimUpdateTalkMessage(sender, getId(), lMessageToSend, date);
		for (Iterator it = peopleIn.iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (!suc.equals(model.getSettings().getContactInfo())) {
				if (suc.getAvailability() == SlimAvailabilityEnum.OFFLINE) {
					suc.addMessageInQueue(sutm);
				}
				else {
					model.getNetworkAdapter().send(sutm, suc);
				}
			}
		}
	}
	
	public synchronized void receiveUpdateTalkMessage(SlimUpdateTalkMessage pMessage) {

		if (isLeader()) {
			for (Iterator it = peopleIn.iterator(); it.hasNext();) {
				SlimUserContact suc = (SlimUserContact)it.next();
				if (suc.getAvailability() == SlimAvailabilityEnum.OFFLINE) {
					suc.addMessageInQueue(pMessage);
				}
			}
		}

		SlimUserContact knownSuc = model.getContacts().getOrAddUserByAddress(pMessage.getSender());
		if (!peopleIn.contains(knownSuc)) {
			peopleIn.add(knownSuc);
		} 
		text = text + buildMessageBeforeDisplaying(pMessage.getNewMessage(), pMessage.getSender(), pMessage.getDate());
		if (listener !=  null) {
			listener.notifyTextTalkUpdate(this);
		}
	}

	public void registerListener(SlimTalkListener pListener) {
		listener = pListener;
	}
	
	public String getText() {
		return text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String pTitle) {
		title = pTitle;
		if (listener !=  null) {
			listener.notifyTextTalkUpdate(this);
		}
	}

	public String getMessageFontColor() {
		return messageFontColor;
	}

	public String getMessageFontSize() {
		return messageFontSize;
	}

	public void setMessageFontSize(String pMessageFontSize) {
		messageFontSize = pMessageFontSize;
	}
	
	public void setMessageFontColor(String pMessageFontColor) {
		messageFontColor = pMessageFontColor;
	}

	private String prepareMessageBeforeSending(String pMessageArea) {
		
		String temp = pMessageArea ;
		temp = temp.replaceAll(NEW_LINE_REG_EXP, HTMLConstants.NEWLINE);
		
		int bold = count(temp, HTMLConstants.BOLD);
		int endbold = count(temp, HTMLConstants.ENDBOLD);
		for (int i = 0; i< bold - endbold; i++) {
			temp = temp + HTMLConstants.ENDBOLD;
		}
		int italic = count(temp, HTMLConstants.ITALIC);
		int enditalic = count(temp, HTMLConstants.ENDITALIC);
		for (int i = 0; i< italic - enditalic; i++) {
			temp = temp + HTMLConstants.ENDITALIC;
		}
		int underline = count(temp, HTMLConstants.UNDERLINE);
		int endunderline = count(temp, HTMLConstants.ENDUNDERLINE);
		for (int i = 0; i< underline - endunderline; i++) {
			temp = temp + HTMLConstants.ENDUNDERLINE;
		}
		
		return (HTMLConstants.FONTCOLOR + messageFontColor + HTMLConstants.FONTSIZE + messageFontSize 
				+ HTMLConstants.TAGEND + temp + HTMLConstants.ENDFONT + HTMLConstants.NEWLINE);
	}

	
	private int count(String base, String searchFor) {
	    int len   = searchFor.length();
	    int result = 0;
	  
	    if (len > 0) {  // search only if there is something
	        int start = base.indexOf(searchFor);
	        while (start != -1) {
	            result++;
	            start = base.indexOf(searchFor, start+len);
	        }
	    }
	    return result;
	}


	
	private String buildMessageBeforeDisplaying(String pMessageArea, SlimUserContact pSender, String pDate) {
		
		String temp = pMessageArea ;


		// add smilleys
		for (int i = 0; i < SMILEY_REG_EXP.length; i++) {
			temp = temp.replaceAll(SMILEY_REG_EXP[i], getSmileyImgTag(i));
		}
		
		// add hyperlink
		int s = temp.indexOf(HTMLConstants.HTTP);
		while (s >= 0) {
			if (s > 2 && temp.charAt(s - 2) == '=') {
				s = temp.indexOf(HTMLConstants.HTTP, s + 1);
			}
			else {
				int e = temp.indexOf(StringConstants.SPACE, s + 1);
				if (e == -1) {
					e = temp.indexOf(HTMLConstants.ENDFONT + HTMLConstants.NEWLINE, s + 1);
				}
				if (e != -1) {
					temp = temp.substring(0, s) + HTMLConstants.LINK + temp.substring(s, e) + HTMLConstants.TAGEND 
						+ temp.substring(s, e) + HTMLConstants.ENDLINK + temp.substring(e, temp.length());
				}
				s = temp.indexOf(HTMLConstants.HTTP, s + 2*(e-s) + 14);
			}
		}
		
		return HTMLConstants.getHeader(pSender.getName(), pDate) + temp;
	}
	
	private String getSmileyImgTag(int smilNb) {
		URL imageURL = ClassLoader.getSystemResource(SlimIcon.IMAGE_PACKAGE + smilNb + SlimIcon.IMAGE_EXTENSION);
		if (imageURL != null) {
			return (HTMLConstants.IMAGE + imageURL + HTMLConstants.TAGEND + HTMLConstants.ENDIMAGE);
		}
		SlimLogger.log(Externalizer.getString("LANSLIM.174", new Integer(smilNb))); //$NON-NLS-1$
		return null;
	}

	
	public boolean isDefaultBold() {
		return model.getSettings().isBold();
	}

	public boolean isDefaultItalic() {
		return model.getSettings().isItalic();
	}

	public boolean isDefaultUndeline() {
		return model.getSettings().isUnderline();
	}

	public String getShortcut(int i) {
		return model.getSettings().getShortcuts()[i];
	}

}
