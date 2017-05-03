package com.oz.lanslim.model;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimLogger;

public class SlimStateEnum implements Serializable {

	public static final int NUMBER = 3;

	private int id = 0;
	private String text = null;
	
	public static final SlimStateEnum AVAILABLE = new SlimStateEnum(0, "AVAILABLE"); //$NON-NLS-1$

	public static final SlimStateEnum AWAY = new SlimStateEnum(1, "AWAY"); //$NON-NLS-1$
	
	public static final SlimStateEnum BUSY = new SlimStateEnum(2, "BUSY"); //$NON-NLS-1$

	private SlimStateEnum(int i, String s) {
		id = i;
		text = s;
	}
	
	public String toString() {
		return text;
	}

	public int toInt() {
		return id;
	}

	public static SlimStateEnum fromString(String pType) throws SlimException {
		
		if (AVAILABLE.toString().equals(pType)) {
			return AVAILABLE;
		}
		if (AWAY.toString().equals(pType)) {
			return AWAY;
		}
		if (BUSY.toString().equals(pType)) {
			return BUSY;
		}
		throw new SlimException(Externalizer.getString("LANSLIM.229", pType)); //$NON-NLS-1$
	}

	public static SlimStateEnum fromInt(int pType) throws SlimException {
		switch (pType) {
			case 0:
				return AVAILABLE;
			case 1:
				return AWAY;
			case 2:
				return BUSY;
			default:
				throw new SlimException(Externalizer.getString("LANSLIM.229", new Integer(pType))); //$NON-NLS-1$
		}
	}

	private Object readResolve() throws ObjectStreamException {
		try {
			return fromInt(toInt());
		}
		catch (SlimException se) {
			throw new InvalidObjectException(Externalizer.getString("LANSLIM.230", SlimLogger.shortFormatException(se))); //$NON-NLS-1$
		}
	}

}
