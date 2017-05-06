package com.oz.lanslim.gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.oz.lanslim.model.SlimContact;

public class ContactTableAvailabilityCellRenderer extends JLabel implements TableCellRenderer {

	public ContactTableAvailabilityCellRenderer() {
		// empty constructor
    }

    public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {
    	
		SlimContact lContact = (SlimContact)value;
    	SlimIcon.setContactIcon(lContact, this);
		
    	return this;
    }
}