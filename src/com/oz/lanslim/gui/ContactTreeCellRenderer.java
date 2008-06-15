package com.oz.lanslim.gui;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimContact;

public class ContactTreeCellRenderer extends DefaultTreeCellRenderer {
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
            boolean expanded, boolean leaf, int row, boolean pFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, pFocus);
		
		if (((DefaultMutableTreeNode)value).getUserObject() instanceof SlimContact) {
			SlimContact sc = (SlimContact)((DefaultMutableTreeNode)value).getUserObject();
	        SlimAvailabilityEnum avail = sc.getAvailability();
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
            setText(sc.getName());
		}
		
		return this;
	}
}

