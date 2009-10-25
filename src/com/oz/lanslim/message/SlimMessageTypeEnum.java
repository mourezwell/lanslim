package com.oz.lanslim.message;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;

public class SlimMessageTypeEnum implements Serializable {
	
	private int id = 0;
	private String text = null;
	
	public static final SlimMessageTypeEnum UPDATE_USER = new SlimMessageTypeEnum(0, "UPDATE_USER"); //$NON-NLS-1$

	public static final SlimMessageTypeEnum AVAILABILITY = new SlimMessageTypeEnum(1, "AVAILABILITY"); //$NON-NLS-1$
	
	public static final SlimMessageTypeEnum NEW_TALK = new SlimMessageTypeEnum(2, "NEW_TALK"); //$NON-NLS-1$

	public static final SlimMessageTypeEnum EXIT_TALK = new SlimMessageTypeEnum(3, "EXIT_TALK"); //$NON-NLS-1$

	public static final SlimMessageTypeEnum INVITE_TALK = new SlimMessageTypeEnum(4, "INVITE_TALK"); //$NON-NLS-1$

	public static final SlimMessageTypeEnum UPDATE_TALK = new SlimMessageTypeEnum(5, "UPDATE_TALK"); //$NON-NLS-1$

	public static final SlimMessageTypeEnum EXCLUDE_TALK = new SlimMessageTypeEnum(6, "EXCLUDE_TALK"); //$NON-NLS-1$

	public static final SlimMessageTypeEnum ATTACHMENT_TALK = new SlimMessageTypeEnum(7, "ATTACHMENT_TALK"); //$NON-NLS-1$

	private SlimMessageTypeEnum(int i, String s) {
		id = i;
		text = s;
	}
	
	public String toString() {
		return text;
	}

	public static SlimMessageTypeEnum fromString(String pType) throws SlimException {
		if (UPDATE_USER.toString().equals(pType)) {
			return UPDATE_USER;
		}
		if (AVAILABILITY.toString().equals(pType)) {
			return AVAILABILITY;
		}
		if (NEW_TALK.toString().equals(pType)) {
			return NEW_TALK;
		}
		if (EXIT_TALK.toString().equals(pType)) {
			return EXIT_TALK;
		}
		if (INVITE_TALK.toString().equals(pType)) {
			return INVITE_TALK;
		}
		if (UPDATE_TALK.toString().equals(pType)) {
			return UPDATE_TALK;
		}
		if (EXCLUDE_TALK.toString().equals(pType)) {
			return EXCLUDE_TALK;
		}
		if (ATTACHMENT_TALK.toString().equals(pType)) {
			return ATTACHMENT_TALK;
		}
		throw new SlimException(Externalizer.getString("LANSLIM.47", pType)); //$NON-NLS-1$
	}
	public int toInt() {
		return id;
	}

	public static SlimMessageTypeEnum fromInt(int pType) throws SlimException {
		switch (pType) {
			case 0:
				return UPDATE_USER;
			case 1:
				return AVAILABILITY;
			case 2:
				return NEW_TALK;
			case 3:
				return EXIT_TALK;
			case 4:
				return INVITE_TALK;
			case 5: 
				return UPDATE_TALK;
			case 6: 
				return EXCLUDE_TALK;
			case 7: 
				return ATTACHMENT_TALK;
			default:
				throw new SlimException(Externalizer.getString("LANSLIM.47", new Integer(pType))); //$NON-NLS-1$
		}
	}

	private Object readResolve() throws ObjectStreamException {
		try {
			return fromInt(toInt());
		}
		catch (SlimException se) {
			throw new InvalidObjectException(Externalizer.getString("LANSLIM.48", se.getMessage())); //$NON-NLS-1$
		}
	}

}

