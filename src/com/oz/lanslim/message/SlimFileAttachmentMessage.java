/**
 * 
 */
package com.oz.lanslim.message;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimUserContact;

public class SlimFileAttachmentMessage extends SlimTalkMessage implements Serializable {

	private static final String FILENAME_ITEM = "FileName"; //$NON-NLS-1$
	private static final String CONTENT_ITEM = "Content"; //$NON-NLS-1$
	private static final String PART_ITEM = "Part"; //$NON-NLS-1$
	
	private String fileName;
	private int part;
	private String content;
	
	public SlimFileAttachmentMessage(SlimUserContact pSender, String pTalkId, String pDate, 
			String pFileName, byte[] pContent, int pPart, int pLength) {
		super(pSender, SlimMessageTypeEnum.ATTACHMENT_TALK, pTalkId, pDate);
		
		try {
			content = new String(pContent, 0, pLength, "ISO-8859-1");
			part = pPart;
			fileName = pFileName;
		}
		catch (IOException ioe) {
			fileName = null;
		}

	}

	private SlimFileAttachmentMessage(SlimUserContact pSender, String pTalkId, String pDate, 
			String pFileName, String pContent, int pPart) {
		super(pSender, SlimMessageTypeEnum.ATTACHMENT_TALK, pTalkId, pDate);
		
		content = pContent;
		part = pPart;
		fileName = pFileName;

	}

	public String getFileName() {
		return fileName;
	}

	public byte[] getContent() {
		try {
			return content.getBytes("ISO-8859-1");
		}
		catch (UnsupportedEncodingException lE) {
			return null;
		}
	}

	public int getPart() {
		return part;
	}

	public String toString() {
		
		if (fileName != null) {
			return super.toString() + itemToString(FILENAME_ITEM, fileName)
				+ itemToString(PART_ITEM, String.valueOf(part))
				+ itemToString(CONTENT_ITEM, content);
		}
		return super.toString();
			
	}
	
	public static SlimFileAttachmentMessage fromStringItems(Map pItems) throws SlimException {
		SlimUserContact suc = 
			SlimUserContact.fromString((String)pItems.get(SENDER_ITEM));
		String tid = (String)pItems.get(TALKID_ITEM);
		String date = (String)pItems.get(DATE_ITEM);
		String lFileName = (String)pItems.get(FILENAME_ITEM);
		String lContentStr = (String)pItems.get(CONTENT_ITEM);
		int lPart = Integer.parseInt((String)pItems.get(PART_ITEM));
		return new SlimFileAttachmentMessage(suc, tid, date, lFileName, lContentStr, lPart);
	}
	
	
}
