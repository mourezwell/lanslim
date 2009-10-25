package com.oz.lanslim.gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimStateEnum;
import com.oz.lanslim.model.SlimUserContact;

public class PeopleInCellRenderer extends JLabel implements ListCellRenderer {

	public PeopleInCellRenderer() {
		setOpaque(true);
		setHorizontalAlignment(LEFT);
		setVerticalAlignment(CENTER);
	}

	/*
	 * This method finds the image and text corresponding to the selected value
	 * and returns the label, set up to display the text and image.
	 */
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} 
		else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		SlimUserContact lContact = (SlimUserContact)value;
		setText(lContact.getName());
		
		if (lContact.getAvailability() == SlimAvailabilityEnum.ONLINE) {
    		boolean isSecured = (lContact.getKey() != null);
    		if (lContact.getState() == SlimStateEnum.AVAILABLE) {
	    		if (isSecured) {
	        		setIcon(SlimIcon.LOCKED);
	    		}
	    		else {
	    			setIcon(SlimIcon.ONLINE);
	    		}
	            setToolTipText(Externalizer.getString("LANSLIM.35")); //$NON-NLS-1$
    		}
    		else if (lContact.getState() == SlimStateEnum.BUSY) {
	    		if (isSecured) {
	        		setIcon(SlimIcon.BUSY_LOCKED);
	    		}
	    		else {
	    			setIcon(SlimIcon.BUSY);
	    		}
	            setToolTipText(Externalizer.getString("LANSLIM.227")); //$NON-NLS-1$
    		}
    		else { //AWAY
	    		if (isSecured) {
	        		setIcon(SlimIcon.AWAY_LOCKED);
	    		}
	    		else {
	    			setIcon(SlimIcon.AWAY);
	    		}
	            setToolTipText(Externalizer.getString("LANSLIM.228")); //$NON-NLS-1$
    		}
		}
		else {
			setIcon(SlimIcon.OFFLINE); //$NON-NLS-1$
		}
		
		return this;
	}

}
