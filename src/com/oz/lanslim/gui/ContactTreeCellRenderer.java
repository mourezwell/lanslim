package com.oz.lanslim.gui;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.StringConstants;
import com.oz.lanslim.model.HTMLConstants;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimStateEnum;
import com.oz.lanslim.model.SlimUserContact;

public class ContactTreeCellRenderer extends DefaultTreeCellRenderer {

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
            boolean expanded, boolean leaf, int row, boolean pFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, pFocus);
		
		if (((DefaultMutableTreeNode)value).getUserObject() instanceof SlimContact) {

			SlimContact sc = (SlimContact)((DefaultMutableTreeNode)value).getUserObject();
	        SlimAvailabilityEnum avail = sc.getAvailability();
	        SlimUserContact user = null;
	        
	        if (avail == SlimAvailabilityEnum.ONLINE) {
	    		user = (SlimUserContact)sc;
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
	        String lText = HTMLConstants.HTML + HTMLConstants.FONTCOLOR + HTMLConstants.BLACK 
			+ HTMLConstants.TAGEND + sc.getName() + HTMLConstants.ENDFONT; 
	        if (user != null && user.getMood() != null) {
	        	lText = lText + HTMLConstants.FONTCOLOR + HTMLConstants.GREY 
					+ HTMLConstants.TAGEND + StringConstants.DASH + user.getMood() + HTMLConstants.ENDFONT; ;
	        }
            setText(lText + HTMLConstants.ENDHTML);
		}
		
		return this;
	}
}

