package com.oz.lanslim.gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.oz.lanslim.model.SlimAvailabilityEnum;

public class ContactTableAvailabilityCellRenderer extends JLabel implements TableCellRenderer {

	public ContactTableAvailabilityCellRenderer() {
		// empty constructor
    }

    public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {
        SlimAvailabilityEnum avail = (SlimAvailabilityEnum)value;
        String imgLocation = null;
        
        if (avail == SlimAvailabilityEnum.ONLINE) {
    		imgLocation = "accept.png";
            setToolTipText(avail.toString());
        } 
        else if (avail == SlimAvailabilityEnum.OFFLINE) {
    		imgLocation = "remove.png";
            setToolTipText(avail.toString());
        }
        else {
    		imgLocation = "info.png";
            setToolTipText("GROUP");
        }
		setIcon(new SlimIcon(imgLocation));
		
       return this;
    }
}