package com.oz.lanslim.message;

import java.io.Serializable;
import java.util.Map;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimUserContact;

public class SlimInviteTalkMessage extends SlimTalkMessage implements Serializable {

	private SlimUserContact newContact;
	private static final String CONTACT_ITEM = "Contact"; //$NON-NLS-1$
	
	public SlimInviteTalkMessage(SlimUserContact pSender, String pTalkId, SlimUserContact pNewContact, String pDate) {
		super(pSender, SlimMessageTypeEnum.INVITE_TALK, pTalkId, pDate);
		newContact = pNewContact;
	}
	
	public SlimUserContact getNewContact() {
		return newContact;
	}
	
	public String toString() {
		return super.toString() + itemToString(CONTACT_ITEM, newContact.toString());
	}

	public static SlimInviteTalkMessage fromStringItems(Map pItems) throws SlimException {
		SlimUserContact ex = 
			SlimUserContact.fromString((String)pItems.get(CONTACT_ITEM));
		SlimUserContact suc = 
			SlimUserContact.fromString((String)pItems.get(SENDER_ITEM));
		String tid = (String)pItems.get(TALKID_ITEM);
		String date = (String)pItems.get(DATE_ITEM);
		return new SlimInviteTalkMessage(suc, tid, ex, date);
	}

}
