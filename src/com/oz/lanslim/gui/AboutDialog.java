package com.oz.lanslim.gui;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.oz.lanslim.LANSLIMMain;

public class AboutDialog extends JOptionPane {
	
	
	public static void showDialog(Component parentComponent) {
		
		showMessageDialog(parentComponent,
			"Version " + LANSLIMMain.VERSION + " \n" +
		    "This little tool has been created by Olivier Mourez\n" +
		    "If you have any features to propose or any issues to report\n" + 
		    " - contact me at olivier.mourez@gmail.com \n" +
		    " - use the project page hosted by sourceforge (http://sourceforge.net/projects/lanslim/)",
		    "About LANSLIM (Local Area Network Server Less Instant Messaging)",
		    JOptionPane.INFORMATION_MESSAGE);
	}

}
