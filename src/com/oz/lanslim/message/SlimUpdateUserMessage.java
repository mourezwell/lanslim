package com.oz.lanslim.message;

import java.io.Serializable;
import java.util.Map;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimUserContact;

public class SlimUpdateUserMessage extends SlimMessage implements Serializable {

	private static final String SETTINGS_ITEM = "OldSettings"; 

	private SlimUserContact oldSettings; 
	
	public SlimUpdateUserMessage(SlimUserContact pSender, SlimUserContact pOldSettings) {
		super(pSender, SlimMessageTypeEnum.UPDATE_USER);
		oldSettings = pOldSettings;
	}

	public SlimUserContact getOldSettings() {
		return oldSettings;
	}
	
	public String toString() {
		return super.toString() + itemToString(SETTINGS_ITEM, oldSettings.toString());
	}

	public static SlimUpdateUserMessage fromStringItems(Map pItems) throws SlimException {
		SlimUserContact ex = 
			SlimUserContact.fromString((String)pItems.get(SETTINGS_ITEM));
		SlimUserContact suc = 
			SlimUserContact.fromString((String)pItems.get(SENDER_ITEM));
		return new SlimUpdateUserMessage(suc, ex);
	}

}
