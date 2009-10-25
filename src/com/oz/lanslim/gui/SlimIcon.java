package com.oz.lanslim.gui;

import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;

public class SlimIcon extends ImageIcon {
	
	public final static String IMAGE_PACKAGE = "com/oz/lanslim/icons/";  //$NON-NLS-1$
	public final static String IMAGE_EXTENSION = ".png";  //$NON-NLS-1$
	
	public static final ImageIcon GROUP = new SlimIcon("info.png");  //$NON-NLS-1$
	public static final ImageIcon OFFLINE = new SlimIcon("remove.png"); //$NON-NLS-1$
	public static final ImageIcon ONLINE = new SlimIcon("accept.png"); //$NON-NLS-1$
	public static final ImageIcon LOCKED = new SlimIcon("accept_locked.png");  //$NON-NLS-1$
	public static final ImageIcon BUSY = new SlimIcon("accept_busy.png"); //$NON-NLS-1$
	public static final ImageIcon BUSY_LOCKED = new SlimIcon("accept_busy_locked.png"); //$NON-NLS-1$
	public static final ImageIcon AWAY = new SlimIcon("accept_away.png"); //$NON-NLS-1$
	public static final ImageIcon AWAY_LOCKED = new SlimIcon("accept_away_locked.png"); //$NON-NLS-1$
	public static final ImageIcon AVAILABLE_ICON = new SlimIcon("comment_edit.png"); //$NON-NLS-1$
	public static final ImageIcon AVAILABLE_NEG = new SlimIcon("comment_edit_neg.png"); //$NON-NLS-1$
	public static final ImageIcon BUSY_ICON = new SlimIcon("comment_edit_busy.png"); //$NON-NLS-1$
	public static final ImageIcon BUSY_NEG = new SlimIcon("comment_edit_busy_neg.png"); //$NON-NLS-1$
	public static final ImageIcon AWAY_ICON = new SlimIcon("comment_edit_away.png"); //$NON-NLS-1$
	public static final ImageIcon AWAY_NEG = new SlimIcon("comment_edit_away_neg.png"); //$NON-NLS-1$

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

}
