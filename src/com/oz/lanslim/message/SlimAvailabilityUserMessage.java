package com.oz.lanslim.message;

import java.io.Serializable;
import java.util.Map;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimKey;
import com.oz.lanslim.model.SlimUserContact;

public class SlimAvailabilityUserMessage extends SlimMessage implements Serializable {

	private static final String AVAILIBITY_ITEM = "Availability";  //$NON-NLS-1$
	private static final String KEY_ITEM = "Key";  //$NON-NLS-1$

	private SlimAvailabilityEnum availability; 
	private SlimKey key; 
	
	public SlimAvailabilityUserMessage(SlimUserContact pSender, SlimAvailabilityEnum pAvail, SlimKey pKey) {
		super(pSender, SlimMessageTypeEnum.AVAILABILITY);
		availability = pAvail;
		key = pKey;
	}

	public SlimAvailabilityEnum getAvailability() {
		return availability;
	}

	public SlimKey getKey() {
		return key;
	}

	public String toString() {
		
		if (key != null) {
			return super.toString() + itemToString(AVAILIBITY_ITEM, availability.toString()) 
				+ itemToString(KEY_ITEM, key.toString());
		}
		return super.toString() + itemToString(AVAILIBITY_ITEM, availability.toString());
		
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
		return new SlimAvailabilityUserMessage(suc, se, k);
		
	}

}
