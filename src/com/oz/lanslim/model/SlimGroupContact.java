package com.oz.lanslim.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.oz.lanslim.SlimException;


public class SlimGroupContact extends SlimContact {

	private List members = null;
	
	public SlimGroupContact(String pName, List pMembers) throws SlimException {
		super(pName);
		setAvailability(SlimAvailabilityEnum.UNKNOWN);
		members = pMembers;
	}

	public boolean isGroup() {
		return true;
	}
	
	public List getMembers() {
		return members;
	}

	public List getOnlineMembers() {
		List lResult = new ArrayList();
		for (Iterator it = members.iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (suc.getAvailability() == SlimAvailabilityEnum.ONLINE) {
				lResult.add(suc);
			}
		}
		return lResult;
	}

	public void addMembers(SlimUserContact pContact) {
		if (!members.contains(pContact)) {
			members.add(pContact);
		}
	}
	
	public void updateMembers(List pMembers) {
		members = pMembers;
	}
	
}
