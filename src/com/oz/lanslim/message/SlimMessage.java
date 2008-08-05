package com.oz.lanslim.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimUserContact;

public abstract class SlimMessage implements Serializable {
	
	private static final String ITEM_START = "[";
	private static final String ITEM_END = "]";
	private static final String ITEM_VALUE_SEPARATOR = "=";
	protected static final String LIST_SEPARATOR = ";";
	
	private static final Pattern MESSAGE_PATTERN = Pattern.compile(
			"(\\" + ITEM_START + "([^\\" + ITEM_VALUE_SEPARATOR + "]*)\\" + ITEM_VALUE_SEPARATOR
			+ "([^\\" + ITEM_END + "]*)\\" + ITEM_END + ")+[\\s\\S]*"); 
			// stuff at teh end is to accept ITEM_END inside message text
	
	private static final int MESSAGE_ITEM_GROUPINDEX = 1;
	private static final int MESSAGE_ITEMNAME_GROUPINDEX = 2;
	private static final int MESSAGE_ITEMVALUE_GROUPINDEX = 3;

	protected static final String SENDER_ITEM = "Sender"; 
	protected static final String TYPE_ITEM = "Type"; 
	
	private SlimUserContact sender;
	
	private SlimMessageTypeEnum type;
	
	
	public SlimMessage(SlimUserContact pSender, SlimMessageTypeEnum pType) {
		sender = pSender;
		type = pType;
	}
	
	public SlimUserContact getSender() {
		return sender;
	}
	
	public SlimMessageTypeEnum getType() {
		return type;
	}

	public String toString() {
		return itemToString(TYPE_ITEM, type.toString()) 
			+ itemToString(SENDER_ITEM, sender.toString());
	}
	
	
	protected String itemToString(String pName, String pValue) {
		return ITEM_START + pName + ITEM_VALUE_SEPARATOR + pValue + ITEM_END;
	}
	
	protected String listToString(List pList) {
		String s = "";
		for (Iterator it = pList.iterator(); it.hasNext();) {
			Object suc = it.next();
			s = s + suc.toString() + LIST_SEPARATOR;
		}
		return s; // no need to exclude remaing separtor since split will ignore it during extract
	}

	protected static String[] listFromString(String pList) {
		return pList.split(LIST_SEPARATOR);
	}

	public static Map itemsFromString(String pString) throws SlimException {
		Matcher lMatch = MESSAGE_PATTERN.matcher(pString);
		if (lMatch.matches()) {
			Map lItems = new HashMap();
			int start = pString.length();
			while (lMatch.matches()) {
				lItems.put(lMatch.group(MESSAGE_ITEMNAME_GROUPINDEX),	
						pString.subSequence(lMatch.start(MESSAGE_ITEMVALUE_GROUPINDEX), start - ITEM_END.length()));
				start = lMatch.start(MESSAGE_ITEM_GROUPINDEX);
				lMatch = MESSAGE_PATTERN.matcher(pString.subSequence(0, start));
			}
			return lItems;
		}
		throw new SlimException(pString + " does not match pattern for message ");	
	}

	public static SlimMessageTypeEnum getType(Map lItems) throws SlimException {
		return SlimMessageTypeEnum.fromString((String)lItems.get(TYPE_ITEM));
	}

} 
