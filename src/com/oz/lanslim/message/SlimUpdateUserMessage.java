package com.oz.lanslim.message;

import java.io.Serializable;

import com.oz.lanslim.model.SlimUserContact;

public class SlimUpdateUserMessage extends SlimMessage implements Serializable {

	private static final String SETTINGS_ITEM = "OldSettings"; 

	private SlimUserContact oldSettings; 
	
	public SlimUpdateUserMessage(SlimUserContact pSender, SlimUserContact pOldSettings) {
		super(pSender, SlimMessageTypeEnum.UPDATE_SETTINGS);
		oldSettings = pOldSettings;
	}

	public SlimUserContact getOldSettings() {
		return oldSettings;
	}
	
	public String toString() {
		return super.toString() + itemToString(SETTINGS_ITEM, oldSettings.toString());
	}

}
