package com.oz.lanslim.gui;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.StringConstants;

public class AboutDialog extends JOptionPane {
	
	public static final String VERSION = "1.04"; //$NON-NLS-1$
	private static final String AUTHOR_NAME = "Olivier Mourez"; //$NON-NLS-1$
	private static final String AUTHOR_ADDRESS = "mourezwell@users.sourceforge.net"; //$NON-NLS-1$
	private static final String PROJECT_HOME = "http://sourceforge.net/projects/lanslim/"; //$NON-NLS-1$
	
	public static void showDialog(Component parentComponent) {
		
		showMessageDialog(parentComponent,
			Externalizer.getString("LANSLIM.113", VERSION) + StringConstants.NEW_LINE + //$NON-NLS-1$
		    Externalizer.getString("LANSLIM.114", AUTHOR_NAME) + StringConstants.NEW_LINE + //$NON-NLS-1$
		    Externalizer.getString("LANSLIM.115") + StringConstants.NEW_LINE +  //$NON-NLS-1$
		    Externalizer.getString("LANSLIM.116", AUTHOR_ADDRESS) + StringConstants.NEW_LINE +  //$NON-NLS-1$
		    Externalizer.getString("LANSLIM.117", PROJECT_HOME), //$NON-NLS-1$
		    Externalizer.getString("LANSLIM.118"), //$NON-NLS-1$
		    JOptionPane.INFORMATION_MESSAGE);
	}

}
