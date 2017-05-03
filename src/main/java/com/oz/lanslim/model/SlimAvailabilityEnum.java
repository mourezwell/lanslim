package com.oz.lanslim.model;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimLogger;

public class SlimAvailabilityEnum implements Serializable {
	
	private int id = 0;
	private String text = null;
	
	public static final SlimAvailabilityEnum ONLINE = new SlimAvailabilityEnum(0, "ONLINE"); //$NON-NLS-1$

	public static final SlimAvailabilityEnum OFFLINE = new SlimAvailabilityEnum(1, "OFFLINE"); //$NON-NLS-1$
	
	public static final SlimAvailabilityEnum UNKNOWN = new SlimAvailabilityEnum(2, "UNKNOWN"); //$NON-NLS-1$
	
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

	public static SlimAvailabilityEnum fromString(String pType) throws SlimException {
		
		if (ONLINE.toString().equals(pType)) {
			return ONLINE;
		}
		if (OFFLINE.toString().equals(pType)) {
			return OFFLINE;
		}
		if (UNKNOWN.toString().equals(pType)) {
			return UNKNOWN;
		}
		throw new SlimException(Externalizer.getString("LANSLIM.169", pType)); //$NON-NLS-1$
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
				throw new SlimException(Externalizer.getString("LANSLIM.169", new Integer(pType))); //$NON-NLS-1$
		}
	}

	private Object readResolve() throws ObjectStreamException {
		try {
			return fromInt(toInt());
		}
		catch (SlimException se) {
			throw new InvalidObjectException(Externalizer.getString("LANSLIM.170", SlimLogger.shortFormatException(se))); //$NON-NLS-1$
		}
	}
}
