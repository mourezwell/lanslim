package com.oz.lanslim.gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.oz.lanslim.model.SlimAvailabilityEnum;
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
		
        String imgLocation = null;
		if (lContact.getAvailability() == SlimAvailabilityEnum.ONLINE) {
    		boolean isSecured = (lContact.getKey() != null);
    		if (isSecured) {
        		imgLocation = "accept_locked.png"; //$NON-NLS-1$
    		}
    		else {
    			imgLocation = "accept.png"; //$NON-NLS-1$
    		}
		}
		else {
			imgLocation = "remove.png"; //$NON-NLS-1$
		}
		setIcon(new SlimIcon(imgLocation));
		
		return this;
	}

}
