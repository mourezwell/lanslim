package com.oz.lanslim.gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.oz.lanslim.Externalizer;
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
    		imgLocation = "accept.png"; //$NON-NLS-1$
            setToolTipText(Externalizer.getString("LANSLIM.35")); //$NON-NLS-1$
        } 
        else if (avail == SlimAvailabilityEnum.OFFLINE) {
    		imgLocation = "remove.png"; //$NON-NLS-1$
            setToolTipText(Externalizer.getString("LANSLIM.36")); //$NON-NLS-1$
        }
        else {
    		imgLocation = "info.png"; //$NON-NLS-1$
            setToolTipText(Externalizer.getString("LANSLIM.37")); //$NON-NLS-1$
        }
		setIcon(new SlimIcon(imgLocation));
		
       return this;
    }
}