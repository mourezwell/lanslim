package com.oz.lanslim.gui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.oz.lanslim.StringConstants;
import com.oz.lanslim.model.SlimTalk;

public class SmileyComboBoxRenderer extends JLabel implements ListCellRenderer {

	public static final ImageIcon[] SMILLEYS = {
		new SlimIcon(0), 
		new SlimIcon(1), 
		new SlimIcon(2), 
		new SlimIcon(3), 
		new SlimIcon(4), 
		new SlimIcon(5), 
		new SlimIcon(6), 
		new SlimIcon(7), 
		new SlimIcon(8), 
		new SlimIcon(9) 
	};

	private boolean enable = false;
	
	public SmileyComboBoxRenderer(boolean pEnabled) {
		enable = pEnabled;
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
	}

	/*
	 * This method finds the image and text corresponding to the selected value
	 * and returns the label, set up to display the text and image.
	 */
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		// Get the selected index. (The index param isn't
		// always valid, so just use the value.)
		int selectedIndex = ((Integer) value).intValue();

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		// Set the icon and text. If icon was null, say so.
		setText(StringConstants.SPACE + SlimTalk.SMILEY_TEXT[selectedIndex]);
		if (enable) {
			setIcon(SMILLEYS[selectedIndex]);
		}
		return this;
	}

}
