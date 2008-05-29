package com.oz.lanslim.message;

import java.io.Serializable;

import com.oz.lanslim.model.SlimUserContact;

public abstract class SlimMessage implements Serializable {
	
	protected static final String ITEM_START = "[";
	protected static final String ITEM_END = "]";
	protected static final String ITEM_VALUE_SEPARATOR = "=";
	protected static final String LIST_SEPARATOR = ";";

	private static final String SENDER_ITEM = "Sender"; 
	private static final String TYPE_ITEM = "Type"; 
	
	private SlimUserContact sender;
	
	private SlimMessageTypeEnum type;
	
	
	public SlimMessage(SlimUserContact pSender, SlimMessageTypeEnum pType) {
		sender = pSender;
		type = pType;
	}
	
	public SlimUserContact getSender() {
		return sender;
	}
	
	public void setSender(SlimUserContact pSender) {
		sender = pSender;
	}

	
	public SlimMessageTypeEnum getType() {
		return type;
	}

	public String toString() {
		if (sender != null) {
			return itemToString(TYPE_ITEM, type.toString()) 
			+ itemToString(SENDER_ITEM, sender.toString());
			
		}
		return itemToString(TYPE_ITEM, type.toString())	+ itemToString(SENDER_ITEM, null);
	}
	
	
	protected String itemToString(String pName, String pValue) {
		return ITEM_START + pName + ITEM_VALUE_SEPARATOR + pValue + ITEM_END;
	}
} 
