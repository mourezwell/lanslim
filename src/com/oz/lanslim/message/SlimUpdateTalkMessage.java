package com.oz.lanslim.message;

import java.io.Serializable;
import java.util.Map;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimUserContact;

public class SlimUpdateTalkMessage extends SlimTalkMessage implements Serializable {

	private static final String MESSAGE_ITEM = "Message"; //$NON-NLS-1$
	private static final String CRYPTO_ITEM = "Encrypted"; //$NON-NLS-1$
	private static final String ATTACHMENT_ITEM = "Attachement"; //$NON-NLS-1$
	private static final String PARTNUMBER_ITEM = "Parts"; //$NON-NLS-1$
	private String message;
	private boolean encrypted;
	private String attachment;
	private int partNumber;
	
	public SlimUpdateTalkMessage(SlimUserContact pSender, String pTalkId, String pNewMessage, 
			String pDate, boolean pEncrypted, String pAttachement, int pPartNumber) {
		super(pSender, SlimMessageTypeEnum.UPDATE_TALK, pTalkId, pDate);
		message = pNewMessage;
		encrypted = pEncrypted;
		encrypted = pEncrypted;
		attachment = pAttachement;
		partNumber = pPartNumber;
	}

	public void setNewMessage(String pMessage) {
		message = pMessage;
	}

	public String getNewMessage() {
		return message;
	}

	public boolean isEncrypted() {
		return encrypted;
	}

	public String getAttachement() {
		return attachment;
	}

	public int getPartNumber() {
		return partNumber;
	}

	public String toString() {
		String lResult = super.toString() + itemToString(CRYPTO_ITEM, String.valueOf(encrypted));
		if (attachment != null) {
			lResult = lResult + itemToString(ATTACHMENT_ITEM, attachment) 
				+ itemToString(PARTNUMBER_ITEM, String.valueOf(partNumber));
		}
		return lResult + itemToString(MESSAGE_ITEM, message.toString()); 
	}
	
	public static SlimUpdateTalkMessage fromStringItems(Map pItems) throws SlimException {
		String msg = (String)pItems.get(MESSAGE_ITEM);
		SlimUserContact suc = 
			SlimUserContact.fromString((String)pItems.get(SENDER_ITEM));
		String tid = (String)pItems.get(TALKID_ITEM);
		String date = (String)pItems.get(DATE_ITEM);
		boolean lEncrypted = false;
		String lEncryptedStr = (String)pItems.get(CRYPTO_ITEM);
		if (lEncryptedStr != null) {
			lEncrypted = Boolean.valueOf(lEncryptedStr).booleanValue();
		}
		String lAttachment = (String)pItems.get(ATTACHMENT_ITEM);
		int lPartNumber = 1;
		if (lAttachment != null) {
			lPartNumber = Integer.parseInt((String)pItems.get(PARTNUMBER_ITEM));
		}
		return new SlimUpdateTalkMessage(suc, tid, msg, date, lEncrypted, lAttachment, lPartNumber);
	}
	
}
