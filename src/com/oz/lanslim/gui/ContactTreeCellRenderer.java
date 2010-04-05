package com.oz.lanslim.gui;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.oz.lanslim.StringConstants;
import com.oz.lanslim.model.HTMLConstants;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimUserContact;

public class ContactTreeCellRenderer extends DefaultTreeCellRenderer {

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
            boolean expanded, boolean leaf, int row, boolean pFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, pFocus);
		
		if (((DefaultMutableTreeNode)value).getUserObject() instanceof SlimContact) {

			SlimContact sc = (SlimContact)((DefaultMutableTreeNode)value).getUserObject();
	        
			SlimIcon.setContactIcon(sc, this);
	        
	        String lColor = HTMLConstants.BLACK;
	        if (sel) {
	        	lColor = HTMLConstants.WHITE;
	        }
	        String lText = HTMLConstants.HTML + HTMLConstants.FONTCOLOR + lColor
				+ HTMLConstants.TAGEND + sc.getName() + HTMLConstants.ENDFONT;
	        
	        if (sc.getAvailability() == SlimAvailabilityEnum.ONLINE 
	        		&& ((SlimUserContact)sc).getMood() != null) {
	        	lText = lText + HTMLConstants.FONTCOLOR + HTMLConstants.GREY 
					+ HTMLConstants.TAGEND + StringConstants.DASH 
					+ ((SlimUserContact)sc).getMood() + HTMLConstants.ENDFONT; ;
	        }
            setText(lText + HTMLConstants.ENDHTML);
		}
		
		return this;
	}
}

