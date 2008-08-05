package com.oz.lanslim.message;

import java.io.Serializable;
import java.util.Map;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimUserContact;

public class SlimExitTalkMessage extends SlimTalkMessage implements Serializable {

	public SlimExitTalkMessage(SlimUserContact pSender, String pTalkId) {
		super(pSender, SlimMessageTypeEnum.EXIT_TALK, pTalkId);
	}

	public static SlimExitTalkMessage fromStringItems(Map pItems) throws SlimException {
		SlimUserContact suc = 
			SlimUserContact.fromString((String)pItems.get(SENDER_ITEM));
		String tid = (String)pItems.get(TALKID_ITEM);
		return new SlimExitTalkMessage(suc, tid);
	}

}
