package com.oz.lanslim.gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.StringConstants;

public class AboutDialog extends JDialog implements ActionListener {

	private static final String VERSION = "1.5.0"; //$NON-NLS-1$
	private static final String AUTHOR_NAME = "Olivier Mourez"; //$NON-NLS-1$
	private static final String AUTHOR_ADDRESS = "mourezwell@users.sourceforge.net"; //$NON-NLS-1$
	private static final String MAIL_LINK = "mailto:"; //$NON-NLS-1$
	private static final String PROJECT_HOME = "http://sourceforge.net/projects/lanslim/"; //$NON-NLS-1$
	private static final String WEB_HOME = "http://lanslim.sourceforge.net"; //$NON-NLS-1$
	
	public AboutDialog(Frame pParent) {
		super(pParent, true);
		initGUI();
	}

	private void initGUI() {
		try {
			setTitle(Externalizer.getString("LANSLIM.118")); //$NON-NLS-1$
			
			FormLayout thisLayout = new FormLayout(
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)",  //$NON-NLS-1$
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)"); //$NON-NLS-1$
			getContentPane().setLayout(thisLayout);
			setSize(270, 200);
			setResizable(false);
			{
				JLabel versionLabel = new JLabel(Externalizer.getString("LANSLIM.113", VERSION)); //$NON-NLS-1$
				//versionLabel.setPreferredSize(new java.awt.Dimension(30, 15));
				getContentPane().add(versionLabel, new CellConstraints("2, 2, 2, 1, default, default")); //$NON-NLS-1$
			}
			{
				JLabel authorLabel = new JLabel(Externalizer.getString("LANSLIM.114", AUTHOR_NAME)); //$NON-NLS-1$
				getContentPane().add(authorLabel, new CellConstraints("2, 4, 2, 1, default, default")); //$NON-NLS-1$
			}
			{
				JLabel questionsLabel = new JLabel(Externalizer.getString("LANSLIM.115")); //$NON-NLS-1$
				getContentPane().add(questionsLabel, new CellConstraints("2, 6, 2, 1, default, default")); //$NON-NLS-1$
			}
			{
				JLabel adressLabel = new JLabel(Externalizer.getString("LANSLIM.116")); //$NON-NLS-1$
				getContentPane().add(adressLabel, new CellConstraints("2, 8, 1, 1, default, default")); //$NON-NLS-1$
				JLabel adressLink = new SlimHyperLink(AUTHOR_ADDRESS, MAIL_LINK + AUTHOR_ADDRESS);
				getContentPane().add(adressLink, new CellConstraints("3, 8, 1, 1, default, default")); //$NON-NLS-1$
			}
			{
				JLabel homeLabel = new JLabel(Externalizer.getString("LANSLIM.117")); //$NON-NLS-1$
				getContentPane().add(homeLabel, new CellConstraints("2, 9, 1, 1, default, default")); //$NON-NLS-1$
				JLabel homeLink = new SlimHyperLink(PROJECT_HOME);
				getContentPane().add(homeLink, new CellConstraints("3, 9, 1, 1, default, default")); //$NON-NLS-1$
			}
			{
				JLabel webLabel = new JLabel(Externalizer.getString("LANSLIM.189")); //$NON-NLS-1$
				getContentPane().add(webLabel, new CellConstraints("2, 10, 1, 1, default, default")); //$NON-NLS-1$
				JLabel webLink = new SlimHyperLink(WEB_HOME);
				getContentPane().add(webLink, new CellConstraints("3, 10, 1, 1, default, default")); //$NON-NLS-1$
			}
			{
				JLabel emptyLabel = new JLabel(StringConstants.SPACE);
				getContentPane().add(emptyLabel, new CellConstraints("2, 11, 3, 1, default, default")); //$NON-NLS-1$
			}
			{
				JLabel checkLabel = new JLabel(Externalizer.getString("LANSLIM.198")); //$NON-NLS-1$
				getContentPane().add(checkLabel, new CellConstraints("2, 12, 3, 1, default, default")); //$NON-NLS-1$
			}
			{
				JButton yesButton = new JButton();
				yesButton.setText(Externalizer.getString("LANSLIM.213")); //$NON-NLS-1$
				yesButton.setActionCommand(AboutActionCommand.YES);
				yesButton.addActionListener(this);
				getContentPane().add(yesButton, new CellConstraints("2, 14, 1, 1, center, center")); //$NON-NLS-1$
				
				JButton noButton = new JButton();
				noButton.setText(Externalizer.getString("LANSLIM.214")); //$NON-NLS-1$
				noButton.setActionCommand(AboutActionCommand.NO);
				noButton.addActionListener(this);
				getContentPane().add(noButton, new CellConstraints("3, 14, 1, 1, center, center")); //$NON-NLS-1$
				
			}
			
		} catch (Exception e) {
			SlimLogger.logException("AboutDialog.initGUI", e); //$NON-NLS-1$
		}	
	}

	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals(AboutActionCommand.YES)) {
			checkVersion(false);
		}
	    setVisible(false);
	}
	
	public void checkVersion(boolean showOnlyDiff) {
		
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
	        boolean equals = VERSION.equals(lLastStr);
	        
	        String lMessage = Externalizer.getString("LANSLIM.199", VERSION, lLastStr); //$NON-NLS-1$
	        if (!equals) {
	        	lMessage =  lMessage + StringConstants.NEW_LINE 
	        		+ Externalizer.getString("LANSLIM.131") + StringConstants.NEW_LINE; //$NON-NLS-1$
	        } 
	        if (!equals || !showOnlyDiff) {
		        JOptionPane.showMessageDialog(getRootPane().getParent(), lMessage,
		        		Externalizer.getString("LANSLIM.118"), //$NON-NLS-1$
		    		    JOptionPane.INFORMATION_MESSAGE);
	        }
	    }
	    catch (NumberFormatException e) {
	    	JOptionPane.showMessageDialog(getRootPane().getParent(), 
	        		Externalizer.getString("LANSLIM.200", SlimLogger.shortFormatException(e)), //$NON-NLS-1$
	        		Externalizer.getString("LANSLIM.118"), //$NON-NLS-1$
	    		    JOptionPane.WARNING_MESSAGE);
	    }
	    catch (IOException e) {
	    	JOptionPane.showMessageDialog(getRootPane().getParent(), 
	        		Externalizer.getString("LANSLIM.200", SlimLogger.shortFormatException(e)), //$NON-NLS-1$
	        		Externalizer.getString("LANSLIM.118"), //$NON-NLS-1$
	    		    JOptionPane.WARNING_MESSAGE);
	    }
	}
	
	private class AboutActionCommand {
		
		private static final String YES = "YES"; //$NON-NLS-1$
		
		private static final String NO = "NO"; //$NON-NLS-1$
	}

}
