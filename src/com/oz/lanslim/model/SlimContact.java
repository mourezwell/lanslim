package com.oz.lanslim.model;

import java.io.Serializable;

import com.oz.lanslim.SlimException;


public abstract class SlimContact implements Serializable {

	private String name = null;

	private SlimAvailabilityEnum availability = SlimAvailabilityEnum.OFFLINE;
	
	public SlimContact(String pName) throws SlimException {
		setName(pName);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String pName) throws SlimException {
		if (pName == null || pName.trim().length() == 0) {
			throw new SlimException("Invalid User/Group Name which must not be null or empty");
		}
		else if (pName.indexOf("@") > 0) {
			throw new SlimException("Invalid User/Group Name which must not contain @ character");
		}
		name = pName;
	}

	public SlimAvailabilityEnum getAvailability() {
		return availability;
	}

	public void setAvailability(SlimAvailabilityEnum pAvail) {
		availability = pAvail;
	}
	
	public abstract boolean isGroup();
	
	public boolean equals(Object obj) {
		if (obj instanceof SlimContact) {
			SlimContact sc = (SlimContact)obj;
			return name.equalsIgnoreCase(sc.name) && isGroup() == sc.isGroup();
		}
		return false;
	}

}
