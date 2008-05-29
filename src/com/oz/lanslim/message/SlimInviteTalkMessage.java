package com.oz.lanslim.message;

import java.io.Serializable;

import com.oz.lanslim.model.SlimUserContact;

public class SlimInviteTalkMessage extends SlimTalkMessage implements Serializable {

	private SlimUserContact newContact;
	private static final String CONTACT_ITEM = "Contact";
	
	public SlimInviteTalkMessage(SlimUserContact pSender, String pTalkId, SlimUserContact pNewContact) {
		super(pSender, SlimMessageTypeEnum.INVITE_TALK, pTalkId);
		newContact = pNewContact;
	}
	
	public SlimUserContact getNewContact() {
		return newContact;
	}
	
	public String toString() {
		return super.toString() + itemToString(CONTACT_ITEM, newContact.toString());
	}
	
}
