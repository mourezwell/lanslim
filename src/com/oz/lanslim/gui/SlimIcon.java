package com.oz.lanslim.gui;

import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;

public class SlimIcon extends ImageIcon {
	
	public final static String IMAGE_PACKAGE = "com/oz/lanslim/icons/"; 

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
