package com.oz.lanslim.message;

import java.io.Serializable;

import com.oz.lanslim.model.SlimUserContact;

public abstract class SlimTalkMessage extends SlimMessage implements Serializable {

	protected static final String TALKID_ITEM = "TalkId";  //$NON-NLS-1$
	protected static final String DATE_ITEM = "Date";  //$NON-NLS-1$

	private String talkId;
	private String date = null;

	public SlimTalkMessage(SlimUserContact pSender, SlimMessageTypeEnum pType, String pTalkId, String pDate) {
		super(pSender, pType);
		talkId = pTalkId;
		date = pDate;
	}
	
	public String getDate() {
		return date;
	}

	public String getTalkId() {
		return talkId;
	}
	
	public String toString() {
		return super.toString() + itemToString(TALKID_ITEM, talkId) + itemToString(DATE_ITEM, date);
	}
	
}
