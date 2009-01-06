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
	
	public synchronized List getMembers() {
		List lResult = new ArrayList();
		for (Iterator it = members.iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			lResult.add(suc);
		}
		return lResult;
	}

	public synchronized List getOnlineMembers() {
		List lResult = new ArrayList();
		for (Iterator it = members.iterator(); it.hasNext();) {
			SlimUserContact suc = (SlimUserContact)it.next();
			if (suc.getAvailability() == SlimAvailabilityEnum.ONLINE) {
				lResult.add(suc);
			}
		}
		return lResult;
	}

	public synchronized void addMember(SlimUserContact pContact) {
		if (!members.contains(pContact)) {
			members.add(pContact);
		}
	}

	public synchronized void removeMember(SlimContact pContact) {
		members.remove(pContact);
	}

	public synchronized void updateMembers(List pMembers) {
		members = pMembers;
	}
	
}
