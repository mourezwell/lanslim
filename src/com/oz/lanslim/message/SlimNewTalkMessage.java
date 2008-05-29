package com.oz.lanslim.message;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.oz.lanslim.model.SlimUserContact;

public class SlimNewTalkMessage extends SlimTalkMessage implements Serializable {
	
	private String title;
	private List participants;
	private static final String TITLE_ITEM = "Title";
	private static final String PARTICIPANTS_ITEM = "Participants";
	
	public SlimNewTalkMessage(SlimUserContact pSender, String pId, String pTitle, List pAttendees) {
		super(pSender, SlimMessageTypeEnum.NEW_TALK, pId);
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
		String s = "";
		for (Iterator it = participants.iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			s = s + suc.toString() + LIST_SEPARATOR;
		}
		return super.toString() + itemToString(TITLE_ITEM, title) + itemToString(PARTICIPANTS_ITEM, s);
	}

}
