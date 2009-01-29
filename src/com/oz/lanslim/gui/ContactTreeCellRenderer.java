package com.oz.lanslim.gui;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimUserContact;

public class ContactTreeCellRenderer extends DefaultTreeCellRenderer {
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
            boolean expanded, boolean leaf, int row, boolean pFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, pFocus);
		
		if (((DefaultMutableTreeNode)value).getUserObject() instanceof SlimContact) {
			SlimContact sc = (SlimContact)((DefaultMutableTreeNode)value).getUserObject();
	        SlimAvailabilityEnum avail = sc.getAvailability();
	        String imgLocation = null;
	        
	        if (avail == SlimAvailabilityEnum.ONLINE) {
	    		SlimUserContact user = (SlimUserContact)sc;
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
            setText(sc.getName());
		}
		
		return this;
	}
}

