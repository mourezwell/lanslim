package com.oz.lanslim.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.gui.SlimIcon;
import com.oz.lanslim.message.SlimExcludeTalkMessage;
import com.oz.lanslim.message.SlimExitTalkMessage;
import com.oz.lanslim.message.SlimInviteTalkMessage;
import com.oz.lanslim.message.SlimNewTalkMessage;
import com.oz.lanslim.message.SlimUpdateTalkMessage;

public class SlimTalk {

	public static final String[] smileyText = new String[] {
			":)", ":(", "=(", ":D", ";)", ":o", ":[", ":#", ":*", "<3" 
	};

	public static final String[] smileyTextRegExp = new String[] {
			"\\:\\)", "\\:\\(", "\\=\\(", "\\:D", ";\\)", "\\:o", "\\:\\[", "\\:\\#", "\\:\\*", "<3" 
	};


	private String title = null;
	private String id = null;
	private List peopleIn = null;
	private String messageFontColor = null;
	private String messageFontSize = null;
	private String text = null;
	private SlimTalkListener listener = null;
	private SlimModel model = null;

	public SlimTalk(SlimModel pModel, String pTitle, String pId, List pPeople) {
		model = pModel;
		title = pTitle;
		peopleIn = pPeople;

		id = pId;
		messageFontColor = pModel.getSettings().getColor();
		messageFontSize = String.valueOf(pModel.getSettings().getFontSize());
		text = "";
	}

	public SlimTalk(SlimModel pModel, String pTitle, List pPeople) {
		this(pModel, pTitle, (pTitle + (int)Math.round(Math.random() * 1000000)), pPeople);
	}
	
	public String getId() {
		return id;
	}
	
	// gestion des membres
	public synchronized void addPeople(List pInvitedContacts) throws SlimException {
		
		List newPeopleIn = new ArrayList();
		newPeopleIn.addAll(peopleIn);
		for (Iterator it = pInvitedContacts.iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (!newPeopleIn.contains(suc)) {
				newPeopleIn.add(suc);
				sendNewTalkMessage(suc, newPeopleIn);
				sendInviteTalkMessage(suc);
				text = text + HTMLConstants.getHeader(HTMLConstants.SYSTEM) + HTMLConstants.BOLD 
					+ suc.getName() + " has been invited to join the talk by you" 
					+ HTMLConstants.ENDBOLD + HTMLConstants.NEWLINE;
				if (listener !=  null) {
					listener.notifyTextTalkUpdate(this);
				}
			}
		}
		peopleIn = newPeopleIn;
		if (listener !=  null) {
			listener.notifyTextTalkUpdate(this);
		}
	}

	public synchronized void removePeople(Object[] pExcludedContacts) throws SlimException {
		
		List newPeopleIn = new ArrayList();
		newPeopleIn.addAll(peopleIn);
		SlimUserContact suc = null;
		for (int i = 0; i < pExcludedContacts.length; i++) {;
			if (newPeopleIn.contains(pExcludedContacts[i])) {
				suc = (SlimUserContact)pExcludedContacts[i];
				newPeopleIn.remove(suc);
				sendExcludeTalkMessage(suc);
				text = text + HTMLConstants.getHeader(HTMLConstants.SYSTEM) + HTMLConstants.BOLD 
					+ suc.getName() + " has been excluded from the talk by you"
					+ HTMLConstants.ENDBOLD + HTMLConstants.NEWLINE;
				if (listener !=  null) {
					listener.notifyTextTalkUpdate(this);
				}
			}
		}
		peopleIn = newPeopleIn;
		if (listener !=  null) {
			listener.notifyTextTalkUpdate(this);
		}
	}

	public synchronized List getPeopleIn() {
		return peopleIn;
	}
	
	public synchronized String getPeopleInListAsString() {
		String lResult = ""; 
		for (Iterator it = peopleIn.iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			lResult = lResult + suc.getName() + ", ";
		}
		return lResult;
	}
	
	private void sendInviteTalkMessage(SlimUserContact pContact) throws SlimException {
		SlimUserContact sender = model.getSettings().getContactInfo();
		SlimInviteTalkMessage setm = new SlimInviteTalkMessage(sender, getId(), pContact);
		for (Iterator it = peopleIn.iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (!suc.equals(model.getSettings().getContactInfo())) {
				model.getNetworkAdapter().send(setm, suc);
			}
		}
	}

	
	private void sendExcludeTalkMessage(SlimUserContact pContact) throws SlimException {
		SlimUserContact sender = model.getSettings().getContactInfo();
		SlimExcludeTalkMessage setm = new SlimExcludeTalkMessage(sender, getId(), pContact);
		for (Iterator it = peopleIn.iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (!suc.equals(model.getSettings().getContactInfo())) {
				model.getNetworkAdapter().send(setm, suc);
			}
		}
	}

	public synchronized void receiveInviteTalkMessage(SlimInviteTalkMessage pMessage) {
		
		SlimUserContact knownSuc = model.getContacts().getOrAddUserByAddress(pMessage.getNewContact());
		if (!peopleIn.contains(knownSuc)) {
			peopleIn.add(knownSuc);
			text = text + HTMLConstants.getHeader(HTMLConstants.SYSTEM) + HTMLConstants.BOLD 
				+ pMessage.getNewContact().getName() + " has been invited to join the talk by " 
				+ pMessage.getSender().getName() + HTMLConstants.ENDBOLD + HTMLConstants.NEWLINE;
			if (listener !=  null) {
				listener.notifyTextTalkUpdate(this);
			}
		}
	}
	
	public synchronized void receiveExcludeTalkMessage(SlimExcludeTalkMessage pMessage) {
		
		SlimUserContact knownSuc = model.getContacts().getOrAddUserByAddress(pMessage.getExcludedContact());
		if (peopleIn.contains(knownSuc)) {
			peopleIn.remove(knownSuc);
			text = text + HTMLConstants.getHeader(HTMLConstants.SYSTEM) + HTMLConstants.BOLD 
				+ pMessage.getExcludedContact().getName() + " has been excluded from the talk by " 
				+ pMessage.getSender().getName() + HTMLConstants.ENDBOLD + HTMLConstants.NEWLINE;
			if (model.getSettings().getContactInfo().equals(knownSuc)) {
				text = text + HTMLConstants.getHeader(HTMLConstants.SYSTEM) + HTMLConstants.BOLD 
				+ "This talk tab won't be updated anymore even if you are invited back, which will appear as a new talk for you"
				+ HTMLConstants.ENDBOLD + HTMLConstants.NEWLINE;
				peopleIn.clear();
			}
			if (listener !=  null) {
				listener.notifyTextTalkUpdate(this);
			}
		}
	}

	protected synchronized void sendExitTalkMessage() throws SlimException {
		SlimUserContact sender = model.getSettings().getContactInfo();
		SlimExitTalkMessage setm = new SlimExitTalkMessage(sender, getId());
		for (Iterator it = peopleIn.iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (!suc.equals(model.getSettings().getContactInfo())) {
				model.getNetworkAdapter().send(setm, suc);
			}
		}
	}
	
	public synchronized void receiveExitTalkMessage(SlimExitTalkMessage pMessage) {
		SlimUserContact knownSuc = model.getContacts().getOrAddUserByAddress(pMessage.getSender());
		if (peopleIn.contains(knownSuc)) {
			peopleIn.remove(knownSuc);
			text = text + HTMLConstants.getHeader(HTMLConstants.SYSTEM) + HTMLConstants.BOLD 
				+ pMessage.getSender().getName() + " has left the talk " + HTMLConstants.ENDBOLD 
				+ HTMLConstants.NEWLINE;
			if (listener !=  null) {
				listener.notifyTextTalkUpdate(this);
			}
		}
	}

	// receiveNewTalkMessage is located in TalkList class because it is call before Talk creation  
	protected synchronized void sendNewTalkMessage(SlimUserContact pContact, List pGuests) throws SlimException {
		SlimUserContact sender = model.getSettings().getContactInfo();
		SlimNewTalkMessage sntm = new SlimNewTalkMessage(sender, getId(), getTitle(), pGuests);
		model.getNetworkAdapter().send(sntm, pContact);
	}

	
	public synchronized void sendUpdateTalkMessage(String pRawMessage, boolean pBuildBefore) throws SlimException {
		String lMessageToSend = pRawMessage;
		if (pBuildBefore) {
			lMessageToSend = prepareMessageBeforeSending(pRawMessage);
		}
		SlimUserContact sender = model.getSettings().getContactInfo();
		text = text + buildMessageBeforeDisplaying(lMessageToSend, sender);
		if (listener !=  null) {
			listener.notifyTextTalkUpdate(this);
		}
		SlimUpdateTalkMessage sutm = new SlimUpdateTalkMessage(sender, getId(), lMessageToSend);
		for (Iterator it = peopleIn.iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (!suc.equals(model.getSettings().getContactInfo())) {
				model.getNetworkAdapter().send(sutm, suc);
			}
		}
	}
	
	public synchronized void receiveUpdateTalkMessage(SlimUpdateTalkMessage pMessage) {
		SlimUserContact knownSuc = model.getContacts().getOrAddUserByAddress(pMessage.getSender());
		if (!peopleIn.contains(knownSuc)) {
			peopleIn.add(knownSuc);
		} 
		text = text + buildMessageBeforeDisplaying(pMessage.getNewMessage(), pMessage.getSender());
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
		temp = temp.replaceAll("\\n", HTMLConstants.NEWLINE);
		
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


	
	private String buildMessageBeforeDisplaying(String pMessageArea, SlimUserContact pSender) {
		
		String temp = pMessageArea ;


		// add smilleys
		for (int i = 0; i < smileyTextRegExp.length; i++) {
			temp = temp.replaceAll(smileyTextRegExp[i], getSmileyImgTag(i));
		}
		
		// add hyperlink
		int s = temp.indexOf("http");
		while (s >= 0) {
			if (s > 1 && temp.charAt(s-1) == '=') {
				s = temp.indexOf("http", s + 1);
			}
			else {
				int e = temp.indexOf(" ", s + 1);
				if (e == -1) {
					e = temp.indexOf(HTMLConstants.ENDFONT + HTMLConstants.NEWLINE, s + 1);
				}
				if (e != -1) {
					temp = temp.substring(0, s) + HTMLConstants.LINK + temp.substring(s, e) + HTMLConstants.TAGEND 
						+ temp.substring(s, e) + HTMLConstants.ENDLINK + temp.substring(e, temp.length());
				}
				s = temp.indexOf("http", s + 2*(e-s) + 14);
			}
		}
		
		return HTMLConstants.getHeader(pSender.getName()) + temp;
	}
	
	private String getSmileyImgTag(int smilNb) {
		URL imageURL = ClassLoader.getSystemResource(SlimIcon.IMAGE_PACKAGE + smilNb + ".png");
		if (imageURL != null) {
			return (HTMLConstants.IMAGE + imageURL + HTMLConstants.TAGEND + HTMLConstants.ENDIMAGE);
		}
		SlimLogger.log(smilNb + " is not valid Smiley");
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

}
