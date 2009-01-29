package com.oz.lanslim.gui;

import java.awt.Component;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JOptionPane;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.StringConstants;

public class AboutDialog extends JOptionPane {

	private static final String VERSION = "1.2"; //$NON-NLS-1$
	private static final String AUTHOR_NAME = "Olivier Mourez"; //$NON-NLS-1$
	private static final String AUTHOR_ADDRESS = "mourezwell@users.sourceforge.net"; //$NON-NLS-1$
	private static final String PROJECT_HOME = "http://sourceforge.net/projects/lanslim/"; //$NON-NLS-1$
	private static final String WEB_HOME = "http://lanslim.sourceforge.net"; //$NON-NLS-1$
	
	public static void showDialog(Component parentComponent) {
		
		int lResponse = showConfirmDialog(parentComponent,
			Externalizer.getString("LANSLIM.113", VERSION) + StringConstants.NEW_LINE + //$NON-NLS-1$
		    Externalizer.getString("LANSLIM.114", AUTHOR_NAME) + StringConstants.NEW_LINE + //$NON-NLS-1$
		    Externalizer.getString("LANSLIM.115") + StringConstants.NEW_LINE +  //$NON-NLS-1$
		    Externalizer.getString("LANSLIM.116", AUTHOR_ADDRESS) + StringConstants.NEW_LINE +  //$NON-NLS-1$
		    Externalizer.getString("LANSLIM.117", PROJECT_HOME) + StringConstants.NEW_LINE +//$NON-NLS-1$
		    Externalizer.getString("LANSLIM.189", WEB_HOME) + StringConstants.NEW_LINE +//$NON-NLS-1$
		    StringConstants.NEW_LINE + Externalizer.getString("LANSLIM.198"), //$NON-NLS-1$
		    Externalizer.getString("LANSLIM.118"), //$NON-NLS-1$
		    JOptionPane.YES_NO_OPTION,
		    JOptionPane.INFORMATION_MESSAGE);
		
		if (lResponse == JOptionPane.YES_OPTION) {
			checkVersion(parentComponent, false);
		}
	}
	
	public static void checkVersion(Component parentComponent, boolean showOnlyDiff) {
		
	    try {
	        DataInputStream di = null;
	        byte [] b = new byte[1];
	
	        URL url = new URL("http://lanslim.sourceforge.net/version.txt"); //$NON-NLS-1$
	        HttpURLConnection con = (HttpURLConnection) url.openConnection(); //$NON-NLS-1$
	        
	        di = new DataInputStream(con.getInputStream());
	        StringBuffer lBuffer = new StringBuffer();
	        while (-1 != di.read(b, 0, 1)) {
	           lBuffer.append(new String(b));
	        }
	        String lLastStr = lBuffer.toString().trim();
	        float lProduct = Float.parseFloat(VERSION);
	        float lLast = Float.parseFloat(lLastStr);
	        boolean equals = (lProduct == lLast);
	        
	        String lMessage = Externalizer.getString("LANSLIM.199", VERSION, lLastStr); //$NON-NLS-1$
	        if (!equals) {
	        	lMessage =  lMessage + StringConstants.NEW_LINE 
	        		+ Externalizer.getString("LANSLIM.131", VERSION, lLastStr) + StringConstants.NEW_LINE + //$NON-NLS-1$
				    Externalizer.getString("LANSLIM.117", PROJECT_HOME) + StringConstants.NEW_LINE + //$NON-NLS-1$
				    Externalizer.getString("LANSLIM.189", WEB_HOME) + StringConstants.NEW_LINE; //$NON-NLS-1$
	        } 
	        if (!equals || !showOnlyDiff) {
		        showMessageDialog(parentComponent, lMessage,
		        		Externalizer.getString("LANSLIM.118"), //$NON-NLS-1$
		    		    JOptionPane.INFORMATION_MESSAGE);
	        }
	    }
	    catch (NumberFormatException e) {
	    	showMessageDialog(parentComponent, 
	        		Externalizer.getString("LANSLIM.200", SlimLogger.shortFormatException(e)), //$NON-NLS-1$
	        		Externalizer.getString("LANSLIM.118"), //$NON-NLS-1$
	    		    JOptionPane.WARNING_MESSAGE);
	    }
	    catch (IOException e) {
	    	showMessageDialog(parentComponent, 
	        		Externalizer.getString("LANSLIM.200", SlimLogger.shortFormatException(e)), //$NON-NLS-1$
	        		Externalizer.getString("LANSLIM.118"), //$NON-NLS-1$
	    		    JOptionPane.WARNING_MESSAGE);
	    }
	}
	
}
