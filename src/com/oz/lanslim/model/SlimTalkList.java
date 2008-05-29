package com.oz.lanslim.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.message.SlimErrorTalkMessage;
import com.oz.lanslim.message.SlimNewTalkMessage;

public class SlimTalkList {

	//private Map<String, SlimTalk> talks = null;
	private Map talks = null;
	private SlimTalkListener listener = null;
	private SlimModel model = null;

	public SlimTalkList(SlimModel pModel) {
		talks =  new HashMap();
		model = pModel;
	}
	
	public synchronized void addTalk(String pTitle, List pContacts) throws SlimException {
		
		SlimTalk lTalk = new SlimTalk(model, pTitle, pContacts);
		talks.put(lTalk.getId(), lTalk);
		for (Iterator it = pContacts.iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (!suc.equals(model.getSettings().getContactInfo())) {
				lTalk.sendNewTalkMessage(suc, pContacts);
			}
		}
		if (listener != null) { // could not happend since event is sent from listener 
			listener.notifyNewTalk(lTalk);
		}
	}
	
	public void registerListener(SlimTalkListener pListener) {
		listener = pListener;
	}
	
	public synchronized void closeTalk(SlimTalk pTalk) throws SlimException {
		pTalk.sendExitTalkMessage();
		talks.remove(pTalk.getId());
	}

	public synchronized boolean receiveNewTalkMessage(SlimNewTalkMessage pMessage) {

		List attendees = new ArrayList();
		for (Iterator it = pMessage.getParticipants().iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();			
			// on recherche dans notre carnet d'adresse les membre ou on les ajoute
			SlimUserContact knownSuc = model.getContacts().getOrAddUserByAddress(suc);
			attendees.add(knownSuc);
		}
		
		if (talks.containsKey(pMessage.getTalkId()) || listener == null) {
			try {
				sendErrorTalkMessage(attendees, pMessage.getTalkId());
			}
			catch (SlimException se) {
				// no network error possible in this case since we have received a message thanks to it
			}
			return false;
		}
		SlimTalk lTalk = new SlimTalk(model, pMessage.getTitle(), pMessage.getTalkId(), attendees);
		talks.put(lTalk.getId(), lTalk);
		listener.notifyNewTalk(lTalk);
		return true;
	}

	private void sendErrorTalkMessage(List pAttendees, String pTalkId) throws SlimException {
		SlimErrorTalkMessage setm = new SlimErrorTalkMessage(model.getSettings().getContactInfo(), pTalkId);
		for (Iterator it = pAttendees.iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (!suc.equals(model.getSettings().getContactInfo())) {
				model.getNetworkAdapter().sendDelayedMessage(setm, suc);
			}
		}
	}
	
	public synchronized void sendExitMessage() throws SlimException {
		
		for (Iterator it = talks.values().iterator(); it.hasNext();) {
			SlimTalk st =  (SlimTalk)it.next();
			st.sendExitTalkMessage();
		}
	}
	
	public synchronized SlimTalk getTalkById(String pId) {
		return (SlimTalk)talks.get(pId);
	}
}
