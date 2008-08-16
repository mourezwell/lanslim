package com.oz.lanslim.message;

import java.util.Map;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimUserContact;

public class SlimExcludeTalkMessage extends SlimTalkMessage {
	
	private SlimUserContact excludedContact;
	private static final String CONTACT_ITEM = "Contact"; //$NON-NLS-1$
	
	public SlimExcludeTalkMessage(SlimUserContact pSender, String pTalkId, SlimUserContact pExcludedContact) {
		super(pSender, SlimMessageTypeEnum.EXCLUDE_TALK, pTalkId);
		excludedContact = pExcludedContact;
	}
	
	public SlimUserContact getExcludedContact() {
		return excludedContact;
	}
	
	public String toString() {
		return super.toString() + itemToString(CONTACT_ITEM, excludedContact.toString());
	}

	public static SlimExcludeTalkMessage fromStringItems(Map pItems) throws SlimException {
		SlimUserContact ex = 
			SlimUserContact.fromString((String)pItems.get(CONTACT_ITEM));
		SlimUserContact suc = 
			SlimUserContact.fromString((String)pItems.get(SENDER_ITEM));
		String tid = (String)pItems.get(TALKID_ITEM);
		return new SlimExcludeTalkMessage(suc, tid, ex);
	}

}
