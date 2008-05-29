package com.oz.lanslim.model;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;

import com.oz.lanslim.SlimException;

public class SlimAvailabilityEnum implements Serializable {
	
	private int id = 0;
	private String text = null;
	
	public static final SlimAvailabilityEnum ONLINE = new SlimAvailabilityEnum(0, "ONLINE");

	public static final SlimAvailabilityEnum OFFLINE = new SlimAvailabilityEnum(1, "OFFLINE");
	
	public static final SlimAvailabilityEnum UNKNOWN = new SlimAvailabilityEnum(2, "UNKNOWN");
	
	private SlimAvailabilityEnum(int i, String s) {
		id = i;
		text = s;
	}
	
	public String toString() {
		return text;
	}

	public int toInt() {
		return id;
	}

	public static SlimAvailabilityEnum fromInt(int pType) throws SlimException {
		switch (pType) {
			case 0:
				return ONLINE;
			case 1:
				return OFFLINE;
			case 2:
				return UNKNOWN;
			default:
				throw new SlimException("Invalid Availability " + pType);
		}
	}

	private Object readResolve() throws ObjectStreamException {
		try {
			return fromInt(toInt());
		}
		catch (SlimException se) {
			throw new InvalidObjectException("Unable to unserialize Availabity due to : " + se.getMessage());
		}
	}
}
