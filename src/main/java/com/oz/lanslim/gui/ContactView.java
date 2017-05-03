package com.oz.lanslim.gui;

import java.awt.event.MouseEvent;

import com.oz.lanslim.model.SlimContact;

public interface ContactView {

	public SlimContact[] getSelectedContacts();
	
    public void filter(String pPrefix, boolean pHideGroup, boolean pHideOffline);
    
	public String[] getSelectedCategories();

	public boolean consolidateClickPoint(MouseEvent e);

}
