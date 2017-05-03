package com.oz.lanslim.message;

import java.io.Serializable;
import java.util.Map;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimKey;
import com.oz.lanslim.model.SlimStateEnum;
import com.oz.lanslim.model.SlimUserContact;

public class SlimAvailabilityUserMessage extends SlimMessage implements Serializable {

	private static final String AVAILIBITY_ITEM = "Availability";  //$NON-NLS-1$
	private static final String KEY_ITEM = "Key";  //$NON-NLS-1$
	private static final String STATE_ITEM = "State";  //$NON-NLS-1$
	private static final String MOOD_ITEM = "Mood";  //$NON-NLS-1$

	private SlimAvailabilityEnum availability; 
	private SlimKey key; 
	private SlimStateEnum state; 
	private String mood; 
	
	public SlimAvailabilityUserMessage(SlimUserContact pSender, SlimAvailabilityEnum pAvail, 
			SlimKey pKey, SlimStateEnum pState, String pMood) {
		
		super(pSender, SlimMessageTypeEnum.AVAILABILITY);
		availability = pAvail;
		key = pKey;
		state = pState;
		mood = pMood;
	}

	public SlimAvailabilityEnum getAvailability() {
		return availability;
	}

	public SlimKey getKey() {
		return key;
	}

	public String getMood() {
		return mood;
	}

	public SlimStateEnum getState() {
		return state;
	}

	public String toString() {
		
		String lMessage = super.toString() 
			+ itemToString(AVAILIBITY_ITEM, availability.toString())
			+ itemToString(STATE_ITEM, state.toString());
		if (key != null) {
			lMessage = lMessage + itemToString(KEY_ITEM, key.toString());
		}
		if (mood != null) {
			lMessage = lMessage + itemToString(MOOD_ITEM, mood);
		}
		return lMessage;
		
	}

	public static SlimAvailabilityUserMessage fromStringItems(Map pItems) throws SlimException {
		SlimAvailabilityEnum se = 
			SlimAvailabilityEnum.fromString((String)pItems.get(AVAILIBITY_ITEM));
		SlimUserContact suc = 
			SlimUserContact.fromString((String)pItems.get(SENDER_ITEM));
		SlimKey k = null;
		String lEncodedKey = (String)pItems.get(KEY_ITEM);
		if (lEncodedKey != null) {
			k = SlimKey.fromString(lEncodedKey);
		}
		String lTemp = (String)pItems.get(STATE_ITEM);
		SlimStateEnum lState = SlimStateEnum.AVAILABLE;
		if (lTemp != null) {
			lState = SlimStateEnum.fromString(lTemp);
		}
		String lMood = (String)pItems.get(MOOD_ITEM);
		
		return new SlimAvailabilityUserMessage(suc, se, k, lState, lMood);
	}

}
