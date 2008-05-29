package com.oz.lanslim.message;

import java.io.Serializable;

import com.oz.lanslim.model.SlimUserContact;

public class SlimUpdateTalkMessage extends SlimTalkMessage implements Serializable {

	private static final String MESSAGE_ITEM = "Message";
	private String message;
	
	public SlimUpdateTalkMessage(SlimUserContact pSender, String pTalkId, String pNewMessage) {
		super(pSender, SlimMessageTypeEnum.UPDATE_TALK, pTalkId);
		message = pNewMessage;
	}
	
	public String getNewMessage() {
		return message;
	}

	public String toString() {
		return super.toString() + itemToString(MESSAGE_ITEM, message.toString());
	}
	
	
}
