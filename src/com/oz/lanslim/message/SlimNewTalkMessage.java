package com.oz.lanslim.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimUserContact;

public class SlimNewTalkMessage extends SlimTalkMessage implements Serializable {
	
	private String title;
	private List participants;
	private static final String TITLE_ITEM = "Title"; //$NON-NLS-1$
	private static final String PARTICIPANTS_ITEM = "Participants"; //$NON-NLS-1$
	
	public SlimNewTalkMessage(SlimUserContact pSender, String pId, String pTitle, List pAttendees, String pDate) {
		super(pSender, SlimMessageTypeEnum.NEW_TALK, pId, pDate);
		title = pTitle;
		participants = pAttendees;
	}
	
	public String getTitle() {
		return title;
	}

	public List getParticipants() {
		return participants;
	}

	public String toString() {
		return super.toString() + itemToString(TITLE_ITEM, title) 
		+ itemToString(PARTICIPANTS_ITEM, listToString(participants));
	}

	public static SlimNewTalkMessage fromStringItems(Map pItems) throws SlimException {
		String titl = (String)pItems.get(TITLE_ITEM);
		SlimUserContact suc = 
			SlimUserContact.fromString((String)pItems.get(SENDER_ITEM));
		String tid = (String)pItems.get(TALKID_ITEM);
		String[] attendees = listFromString((String)pItems.get(PARTICIPANTS_ITEM));
		String date = (String)pItems.get(DATE_ITEM);
		List part = new ArrayList(attendees.length);
		for (int i = 0; i < attendees.length; i++) {
			part.add(SlimUserContact.fromString(attendees[i]));
		}
		return new SlimNewTalkMessage(suc, tid, titl, part, date);
	}

}
