package com.oz.lanslim.message;

import java.io.Serializable;

import com.oz.lanslim.model.SlimUserContact;

public abstract class SlimTalkMessage extends SlimMessage implements Serializable {

	private static final String TALKID_ITEM = "TalkId"; 

	private String talkId;
	
	public SlimTalkMessage(SlimUserContact pSender, SlimMessageTypeEnum pType, String pTalkId) {
		super(pSender, pType);
		talkId = pTalkId;
	}
	
	public String getTalkId() {
		return talkId;
	}
	
	public String toString() {
		return super.toString() + itemToString(TALKID_ITEM, talkId);
	}
	
}
