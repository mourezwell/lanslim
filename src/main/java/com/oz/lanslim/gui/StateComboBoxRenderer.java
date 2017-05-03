package com.oz.lanslim.gui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.model.SlimStateEnum;

public class StateComboBoxRenderer extends JLabel implements ListCellRenderer {

	public static final ImageIcon[] STATES_ICON = {
		new SlimIcon("accept_big.png"), 
		new SlimIcon("away_big.png"), 
		new SlimIcon("busy_big.png")
	};
	
	public static final String[] STATES_TEXT = {
		Externalizer.getString("LANSLIM.226"), 
		Externalizer.getString("LANSLIM.228"),
		Externalizer.getString("LANSLIM.227") 
	};

	public static final SlimStateEnum[] STATES = {
		SlimStateEnum.AVAILABLE, 
		SlimStateEnum.AWAY, 
		SlimStateEnum.BUSY, 
	};


	public static final int getStateIndex(SlimStateEnum pState) {
		for (int i = 0; i < STATES.length; i++) {
			if (STATES[i].equals(pState)) {
				return i;
			}
		}
		return 0;
	}
	
	private boolean textEnable = true;
	
	public StateComboBoxRenderer(boolean pTextEnable) {
		setOpaque(true);
		textEnable = pTextEnable;
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
		setIcon(STATES_ICON[selectedIndex]);
		if (textEnable) {
			setText(STATES_TEXT[selectedIndex]);
		}
		else {
			setText("");
		}
		setToolTipText(STATES_TEXT[selectedIndex]);
		return this;
	}

	public void setTextEnable(boolean pTextEnable) {
		textEnable = pTextEnable;
	}
}
