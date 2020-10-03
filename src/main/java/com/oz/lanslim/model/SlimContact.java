package com.oz.lanslim.model;

import java.io.Serializable;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;


public abstract class SlimContact implements Serializable {

	protected static final String HOST_SEPARATOR = "@";   //$NON-NLS-1$
	protected static final String PORT_SEPARATOR = ":";   //$NON-NLS-1$
	protected final static String FORMIDDEN_CHAR = "]"; //$NON-NLS-1$
	
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
			throw new SlimException(Externalizer.getString("LANSLIM.167")); //$NON-NLS-1$
		}
		else if (pName.indexOf(HOST_SEPARATOR) > 0) {
			throw new SlimException(Externalizer.getString("LANSLIM.168")  //$NON-NLS-1$
					+ HOST_SEPARATOR);
		}
		else if (pName.indexOf(FORMIDDEN_CHAR) > 0) {
			throw new SlimException(Externalizer.getString("LANSLIM.168")  //$NON-NLS-1$
					+ FORMIDDEN_CHAR);
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
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SlimContact) {
			SlimContact sc = (SlimContact)obj;
			return name.equalsIgnoreCase(sc.name) && isGroup() == sc.isGroup();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return name == null ? 0 : name.hashCode();
	}

}
