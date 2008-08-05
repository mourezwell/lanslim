package com.oz.lanslim.message;

import java.io.Serializable;
import java.util.Map;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimUserContact;

public class SlimAvailabilityUserMessage extends SlimMessage implements Serializable {

	private static final String AVAILIBITY_ITEM = "Availability"; 

	private SlimAvailabilityEnum availability; 
	
	public SlimAvailabilityUserMessage(SlimUserContact pSender, SlimAvailabilityEnum pAvail) {
		super(pSender, SlimMessageTypeEnum.AVAILABILITY);
		availability = pAvail;
	}

	public SlimAvailabilityEnum getAvailability() {
		return availability;
	}
	
	public String toString() {
		return super.toString() + itemToString(AVAILIBITY_ITEM, availability.toString());
	}

	public static SlimAvailabilityUserMessage fromStringItems(Map pItems) throws SlimException {
		SlimAvailabilityEnum se = 
			SlimAvailabilityEnum.fromString((String)pItems.get(AVAILIBITY_ITEM));
		SlimUserContact suc = 
			SlimUserContact.fromString((String)pItems.get(SENDER_ITEM));
		return new SlimAvailabilityUserMessage(suc, se);
		
	}

}
