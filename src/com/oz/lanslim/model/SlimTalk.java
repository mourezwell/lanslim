package com.oz.lanslim.model;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

	private static final DateFormat myFormat = new SimpleDateFormat("HH:mm:ss");

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
		messageFontSize = "4";
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
				text = text + "<b>SYSTEM [" + myFormat.format(new Date()) + "]>" 
					+ suc.getName() + " has been invited to join the talk by you</b><br>";
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
				text = text + "<b>SYSTEM [" + myFormat.format(new Date()) + "]>" 
					+ suc.getName() + " has been excluded from the talk by you</b><br>";
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
			text = text + "<b>SYSTEM [" + myFormat.format(new Date()) + "]>" 
				+ pMessage.getNewContact().getName() + " has been invited to join the talk by " 
				+ pMessage.getSender().getName() + " </b><br>";
			if (listener !=  null) {
				listener.notifyTextTalkUpdate(this);
			}
		}
	}
	
	public synchronized void receiveExcludeTalkMessage(SlimExcludeTalkMessage pMessage) {
		
		SlimUserContact knownSuc = model.getContacts().getOrAddUserByAddress(pMessage.getExcludedContact());
		if (peopleIn.contains(knownSuc)) {
			peopleIn.remove(knownSuc);
			text = text + "<b>SYSTEM [" + myFormat.format(new Date()) + "]>" 
				+ pMessage.getExcludedContact().getName() + " has been excluded from the talk by " 
				+ pMessage.getSender().getName() + "</b><br>";
			if (model.getSettings().getContactInfo().equals(knownSuc)) {
				text = text + "<font color=\"FF0000\"><b>SYSTEM [" + myFormat.format(new Date()) + "]>" 
				+ "This talk tab won't be updated anymore even if you are invited back, which will appear as a new talk for you</b></font><br>";
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
			text = text + "<b>SYSTEM [" + myFormat.format(new Date()) + "]>" 
				+ pMessage.getSender().getName() + " has left the talk </b><br>";
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
		temp = temp.replaceAll("\\n", "<br>");
		
		return ("<font color=\"" + messageFontColor + "\" size=\"" + messageFontSize + "\">" + temp + "</font><br>");
	}

	private String buildMessageBeforeDisplaying(String pMessageArea, SlimUserContact pSender) {
		
		String temp = pMessageArea ;

		// add smilleys
		int s = temp.indexOf("$");
		while (s >= 0) {
			int e = temp.indexOf("$", s + 1);
			if (e == s+2 || e == s+3) {
				String t = null;	
				try {
					int smilNb = Integer.parseInt(temp.substring(s+1, e));
					t = getSmileyImgTag(smilNb);
				}
				catch (NumberFormatException ex) {
					SlimLogger.log(temp.substring(s+1, e) + " is not valid Number");
				}
				if (t != null) { 
					temp = temp.substring(0, s) 
						+ temp.substring(s, temp.length()).replaceFirst("\\$[^\\$]*\\$", t);
				}
			}
			s = temp.indexOf("$", s + 1);
		}
		
		// add hyperlink
		s = temp.indexOf("http");
		while (s >= 0) {
			if (s > 1 && temp.charAt(s-1) == '=') {
				s = temp.indexOf("http", s + 1);
			}
			else {
				int e = temp.indexOf(" ", s + 1);
				if (e == -1) {
					e = temp.indexOf("</font><br>", s + 1);
				}
				if (e != -1) {
					temp = temp.substring(0, s) + "<a href=" + temp.substring(s, e) + ">" 
						+ temp.substring(s, e) + "</a>" + temp.substring(e, temp.length());
				}
				s = temp.indexOf("http", s + 2*(e-s) + 14);
			}
		}
		
		return "<b>" + pSender.getName() + " [" + myFormat.format(new Date()) + "]</b>> " + temp;
	}
	
	private String getSmileyImgTag(int smilNb) {
		URL imageURL = ClassLoader.getSystemResource(SlimIcon.IMAGE_PACKAGE + smilNb + ".png");
		if (imageURL != null) {
			return ("<img src=\"" + imageURL + "\"></img> ");
		}
		SlimLogger.log(smilNb + " is not valid Smiley");
		return null;
	}

}
