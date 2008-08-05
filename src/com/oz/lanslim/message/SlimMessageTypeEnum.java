package com.oz.lanslim.message;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;

import com.oz.lanslim.SlimException;

public class SlimMessageTypeEnum implements Serializable {
	
	private int id = 0;
	private String text = null;
	
	public static final SlimMessageTypeEnum UPDATE_USER = new SlimMessageTypeEnum(0, "UPDATE_USER");

	public static final SlimMessageTypeEnum AVAILABILITY = new SlimMessageTypeEnum(1, "AVAILABILITY");
	
	public static final SlimMessageTypeEnum NEW_TALK = new SlimMessageTypeEnum(2, "NEW_TALK");

	public static final SlimMessageTypeEnum EXIT_TALK = new SlimMessageTypeEnum(3, "EXIT_TALK");

	public static final SlimMessageTypeEnum INVITE_TALK = new SlimMessageTypeEnum(4, "INVITE_TALK");

	public static final SlimMessageTypeEnum UPDATE_TALK = new SlimMessageTypeEnum(5, "UPDATE_TALK");

	public static final SlimMessageTypeEnum EXCLUDE_TALK = new SlimMessageTypeEnum(6, "EXCLUDE_TALK");
	
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
		throw new SlimException("Invald Message type " + pType);
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
			default:
				throw new SlimException("Invald Message type " + pType);
		}
	}

	private Object readResolve() throws ObjectStreamException {
		try {
			return fromInt(toInt());
		}
		catch (SlimException se) {
			throw new InvalidObjectException("Unable to unserialize MessageType due to : " + se.getMessage());
		}
	}

}

