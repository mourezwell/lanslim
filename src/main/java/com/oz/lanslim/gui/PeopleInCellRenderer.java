package com.oz.lanslim.gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

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
		
		SlimIcon.setContactIcon(lContact, this);
		
		return this;
	}

}
