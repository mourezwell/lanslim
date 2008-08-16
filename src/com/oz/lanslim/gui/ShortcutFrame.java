package com.oz.lanslim.gui;	
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.model.SlimSettings;

import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
*/
public class ShortcutFrame extends JDialog implements ActionListener {
	
	private JLabel[] shortcutLabels;
	private JTextField[] shortcutFields;
	private JPanel buttonPanel;
	private JButton cancelButton;
	private JButton okButton;

	private SlimSettings model;
	
	public ShortcutFrame(Dialog pParent, SlimSettings pSettings) {
		super(pParent, true);
		model = pSettings;
		initGUI();
	}

	private void initGUI() {
		try {
			setTitle(Externalizer.getString("LANSLIM.112")); //$NON-NLS-1$
			
			FormLayout thisLayout = new FormLayout(
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)",  //$NON-NLS-1$
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)"); //$NON-NLS-1$
			getContentPane().setLayout(thisLayout);
			setSize(200, 410);
			setResizable(false);
			shortcutLabels = new JLabel[SlimSettings.SHORTCUT_NUMBER];
			shortcutFields = new JTextField[SlimSettings.SHORTCUT_NUMBER];
			for (int i = 0; i < SlimSettings.SHORTCUT_NUMBER; i++) {
				shortcutLabels[i] =  new JLabel();
				shortcutLabels[i].setText("F" + (i + 1)); //$NON-NLS-1$
				shortcutLabels[i].setPreferredSize(new java.awt.Dimension(20, 14));
				getContentPane().add(shortcutLabels[i], new CellConstraints("2, " + (2*i+2) + ", 1, 1, default, default")); //$NON-NLS-1$ //$NON-NLS-2$

				shortcutFields[i] = new JTextField();
				shortcutFields[i].setPreferredSize(new java.awt.Dimension(140, 20));
				shortcutFields[i].setText(model.getShortcuts()[i]);
				getContentPane().add(shortcutFields[i], new CellConstraints("4, " + (2*i+2) + ", 1, 1, default, default")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			{
				buttonPanel = new JPanel();
				okButton = new JButton();
				okButton.setText(Externalizer.getString("LANSLIM.15")); //$NON-NLS-1$
				okButton.setActionCommand(ShortcutFrameActionCommand.OK);
				okButton.addActionListener(this);
				
				cancelButton = new JButton();
				cancelButton.setText(Externalizer.getString("LANSLIM.16")); //$NON-NLS-1$
				cancelButton.setActionCommand(ShortcutFrameActionCommand.CANCEL);
				cancelButton.addActionListener(this);

				buttonPanel.add(okButton);
				buttonPanel.add(cancelButton);
				
				getContentPane().add(buttonPanel, new CellConstraints("4, 26, 1, 1, default, default")); //$NON-NLS-1$
			}
		} catch(Exception e) {
			SlimLogger.logException("ShortcutFrame.initGUI", e); //$NON-NLS-1$
		}	
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals(ShortcutFrameActionCommand.OK)) {

			String[] lShortcuts = new String[SlimSettings.SHORTCUT_NUMBER];
			for (int i = 0; i < SlimSettings.SHORTCUT_NUMBER; i++) {
				lShortcuts[i] = shortcutFields[i].getText(); 
			}
			model.setShortcuts(lShortcuts);
			setVisible(false);
		}
		else if (e.getActionCommand().equals(ShortcutFrameActionCommand.CANCEL)) {
			setVisible(false);
		}

	}
	
	private class ShortcutFrameActionCommand {
		
		public static final String OK = "ok"; //$NON-NLS-1$
		
		public static final String CANCEL = "cancel"; //$NON-NLS-1$
		
	}


}
