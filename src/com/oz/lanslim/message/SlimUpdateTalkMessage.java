package com.oz.lanslim.message;

import java.io.Serializable;
import java.util.Map;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimUserContact;

public class SlimUpdateTalkMessage extends SlimTalkMessage implements Serializable {

	private static final String MESSAGE_ITEM = "Message"; //$NON-NLS-1$
	private String message;
	
	public SlimUpdateTalkMessage(SlimUserContact pSender, String pTalkId, String pNewMessage, String pDate) {
		super(pSender, SlimMessageTypeEnum.UPDATE_TALK, pTalkId, pDate);
		message = pNewMessage;
	}
	
	public String getNewMessage() {
		return message;
	}

	public String toString() {
		return super.toString() + itemToString(MESSAGE_ITEM, message.toString());
	}
	
	public static SlimUpdateTalkMessage fromStringItems(Map pItems) throws SlimException {
		String msg = (String)pItems.get(MESSAGE_ITEM);
		SlimUserContact suc = 
			SlimUserContact.fromString((String)pItems.get(SENDER_ITEM));
		String tid = (String)pItems.get(TALKID_ITEM);
		String date = (String)pItems.get(DATE_ITEM);
		return new SlimUpdateTalkMessage(suc, tid, msg, date);
	}
	
}
