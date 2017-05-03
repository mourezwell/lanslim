package com.oz.lanslim.gui;

import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimStateEnum;
import com.oz.lanslim.model.SlimUserContact;

public class SlimIcon extends ImageIcon {
	
	private final static String IMAGE_PACKAGE = "com/oz/lanslim/icons/";  //$NON-NLS-1$
	private final static String IMAGE_EXTENSION = ".png";  //$NON-NLS-1$
	
	private static final ImageIcon GROUP = new SlimIcon("info.png");  //$NON-NLS-1$
	private static final ImageIcon OFFLINE = new SlimIcon("remove.png"); //$NON-NLS-1$
	
	private static final ImageIcon ONLINE = new SlimIcon("accept.png"); //$NON-NLS-1$
	private static final ImageIcon LOCKED = new SlimIcon("accept_locked.png");  //$NON-NLS-1$
	private static final ImageIcon BUSY = new SlimIcon("accept_busy.png"); //$NON-NLS-1$
	private static final ImageIcon BUSY_LOCKED = new SlimIcon("accept_busy_locked.png"); //$NON-NLS-1$
	private static final ImageIcon AWAY = new SlimIcon("accept_away.png"); //$NON-NLS-1$
	private static final ImageIcon AWAY_LOCKED = new SlimIcon("accept_away_locked.png"); //$NON-NLS-1$
	private static final ImageIcon BLOCKED = new SlimIcon("blocked.png"); //$NON-NLS-1$

	private static final ImageIcon AVAILABLE_ICON = new SlimIcon("comment_edit.png"); //$NON-NLS-1$
	private static final ImageIcon AVAILABLE_NEG = new SlimIcon("comment_edit_neg.png"); //$NON-NLS-1$
	private static final ImageIcon BUSY_ICON = new SlimIcon("comment_edit_busy.png"); //$NON-NLS-1$
	private static final ImageIcon BUSY_NEG = new SlimIcon("comment_edit_busy_neg.png"); //$NON-NLS-1$
	private static final ImageIcon AWAY_ICON = new SlimIcon("comment_edit_away.png"); //$NON-NLS-1$
	private static final ImageIcon AWAY_NEG = new SlimIcon("comment_edit_away_neg.png"); //$NON-NLS-1$

	public SlimIcon(String value) {
		super();
		URL imageURL = ClassLoader.getSystemResource(IMAGE_PACKAGE + value);
		if (imageURL != null) {
			setImage(Toolkit.getDefaultToolkit().getImage(imageURL));
		}
		else {
			setDescription(value);
		}
	}

	public SlimIcon(int value) {
		super();
		URL imageURL = ClassLoader.getSystemResource(IMAGE_PACKAGE + value + IMAGE_EXTENSION);
		if (imageURL != null) {
			setImage(Toolkit.getDefaultToolkit().getImage(imageURL));
		}
		else {
			setDescription(value + IMAGE_EXTENSION);
		}
	}

	public static URL getURL(String value) {
		return ClassLoader.getSystemResource(IMAGE_PACKAGE + value);
	}

	public static URL getURL(int value) {
		return ClassLoader.getSystemResource(IMAGE_PACKAGE + value + IMAGE_EXTENSION);
	}

	
	public static void setContactIcon(SlimContact pContact, JLabel pLabel) {
		
    	SlimAvailabilityEnum avail = pContact.getAvailability();
    	
        if (avail == SlimAvailabilityEnum.ONLINE) {
    		SlimUserContact user = (SlimUserContact)pContact;
    		boolean isSecured = (user.getKey() != null);
    		if (user.getState() == SlimStateEnum.AVAILABLE) {
	    		if (isSecured) {
    				pLabel.setIcon(SlimIcon.LOCKED);
	    		}
	    		else {
    				pLabel.setIcon(SlimIcon.ONLINE);
	    		}
	    		pLabel.setToolTipText(Externalizer.getString("LANSLIM.35")); //$NON-NLS-1$
    		}
    		else if (user.getState() == SlimStateEnum.BUSY) {
	    		if (isSecured) {
    				pLabel.setIcon(SlimIcon.BUSY_LOCKED);
	    		}
	    		else {
    				pLabel.setIcon(SlimIcon.BUSY);
	    		}
	    		pLabel.setToolTipText(Externalizer.getString("LANSLIM.227")); //$NON-NLS-1$
    		}
    		else { //AWAY
	    		if (isSecured) {
    				pLabel.setIcon(SlimIcon.AWAY_LOCKED);
	    		}
	    		else {
    				pLabel.setIcon(SlimIcon.AWAY);
	    		}
	    		pLabel.setToolTipText(Externalizer.getString("LANSLIM.228")); //$NON-NLS-1$
    		}
        } 
        else if (avail == SlimAvailabilityEnum.OFFLINE) {
    		SlimUserContact user = (SlimUserContact)pContact;
        	if (user.isBlocked()) {
            	pLabel.setIcon(SlimIcon.BLOCKED);
            	pLabel.setToolTipText(Externalizer.getString("LANSLIM.242")); //$NON-NLS-1$
        	}
        	else {
            	pLabel.setIcon(SlimIcon.OFFLINE);
            	pLabel.setToolTipText(Externalizer.getString("LANSLIM.36")); //$NON-NLS-1$
        	}
        }
        else if (avail == SlimAvailabilityEnum.UNKNOWN) { 
        	pLabel.setIcon(SlimIcon.GROUP);
        	pLabel.setToolTipText(Externalizer.getString("LANSLIM.37")); //$NON-NLS-1$
        }
	}

	public static ImageIcon getApplicationIcon(SlimStateEnum pSate, boolean pNegative) {
		
		ImageIcon lIcon = null;
		
		if (pNegative) {
			if (pSate.equals(SlimStateEnum.AVAILABLE)) {
				lIcon = SlimIcon.AVAILABLE_NEG;
			}
			else if (pSate.equals(SlimStateEnum.AWAY)) {
				lIcon = SlimIcon.AWAY_NEG;
			}
			else { //BUSY
				lIcon = SlimIcon.BUSY_NEG;
			}
		}
		else {
			if (pSate.equals(SlimStateEnum.AVAILABLE)) {
				lIcon = SlimIcon.AVAILABLE_ICON;
			}
			else if (pSate.equals(SlimStateEnum.AWAY)) {
				lIcon = SlimIcon.AWAY_ICON;
			}
			else { //BUSY
				lIcon = SlimIcon.BUSY_ICON;
			}
		}

		return lIcon;
	}

	
}
