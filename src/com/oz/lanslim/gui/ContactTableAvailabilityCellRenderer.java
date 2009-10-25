package com.oz.lanslim.gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimStateEnum;
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
    	
        if (avail == SlimAvailabilityEnum.ONLINE) {
    		SlimUserContact user = (SlimUserContact)contact;
    		boolean isSecured = (user.getKey() != null);
    		if (user.getState() == SlimStateEnum.AVAILABLE) {
	    		if (isSecured) {
	        		setIcon(SlimIcon.LOCKED);
	    		}
	    		else {
	    			setIcon(SlimIcon.ONLINE);
	    		}
	            setToolTipText(Externalizer.getString("LANSLIM.35")); //$NON-NLS-1$
    		}
    		else if (user.getState() == SlimStateEnum.BUSY) {
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
        else if (avail == SlimAvailabilityEnum.OFFLINE) {
    		setIcon(SlimIcon.OFFLINE);
            setToolTipText(Externalizer.getString("LANSLIM.36")); //$NON-NLS-1$
        }
        else {
    		setIcon(SlimIcon.GROUP);
            setToolTipText(Externalizer.getString("LANSLIM.37")); //$NON-NLS-1$
        }
		
       return this;
    }
}