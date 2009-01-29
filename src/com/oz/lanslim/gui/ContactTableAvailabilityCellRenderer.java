package com.oz.lanslim.gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimUserContact;

public class ContactTableAvailabilityCellRenderer extends JLabel implements TableCellRenderer {

	public ContactTableAvailabilityCellRenderer() {
		// empty constructor
    }

    public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {
    	
    	SlimContact contact = (SlimContact)value;
    	SlimAvailabilityEnum avail = contact.getAvailability();
    	
        String imgLocation = null;
        
        if (avail == SlimAvailabilityEnum.ONLINE) {
    		SlimUserContact user = (SlimUserContact)contact;
    		boolean isSecured = (user.getKey() != null);
    		if (isSecured) {
        		imgLocation = "accept_locked.png"; //$NON-NLS-1$
    		}
    		else {
    			imgLocation = "accept.png"; //$NON-NLS-1$
    		}
    		
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