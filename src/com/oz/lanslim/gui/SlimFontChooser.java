package com.oz.lanslim.gui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.model.HTMLConstants;

public class SlimFontChooser extends JDialog implements ActionListener {

	private JLabel preview;
	private JComboBox fonts;
	private JButton cancelButton;
	private JButton okButton;

	private String currentFont;
	
	public SlimFontChooser(Dialog pParent, String pCurrentFont) {
		super(pParent, true);
		currentFont = pCurrentFont;
		initGUI();
	}

	public SlimFontChooser(Frame pParent, String pCurrentFont) {
		super(pParent, true);
		currentFont = pCurrentFont;
		initGUI();
	}

	private void initGUI() {
		
		try {
			setTitle(Externalizer.getString("LANSLIM.220")); //$NON-NLS-1$
			
			FormLayout thisLayout = new FormLayout(
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)",  //$NON-NLS-1$
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)"); //$NON-NLS-1$
			getContentPane().setLayout(thisLayout);
			setSize(200, 410);
			setResizable(false);

		    GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    String envfonts[] = gEnv.getAvailableFontFamilyNames();
		    Vector vector = new Vector();
		    for (int i = 1; i < envfonts.length; i++) {
		      vector.addElement(envfonts[i]);
		    }

		    JLabel lTypeLabel = new JLabel(Externalizer.getString("LANSLIM.221")); //$NON-NLS-1$
			getContentPane().add(lTypeLabel, new CellConstraints("2, 2, 1, 1, default, default"));  //$NON-NLS-1$
		    fonts = new JComboBox(vector);
		    fonts.setSelectedItem(currentFont);
		    fonts.addActionListener(this);
		    fonts.setActionCommand(ShortcutFrameActionCommand.FONT);
		    getContentPane().add(fonts, new CellConstraints("4, 2, 1, 1, default, default")); //$NON-NLS-1$);

		    preview = new JLabel(); 
			preview.setText(HTMLConstants.HTML + HTMLConstants.FONTCOLOR + HTMLConstants.BLACK 
					+ HTMLConstants.FONTSIZE + HTMLConstants.DEFAULT_SIZE 
					+ HTMLConstants.FONTFACE + fonts.getSelectedItem().toString() + HTMLConstants.TAGEND 
					+ Externalizer.getString("LANSLIM.222") + HTMLConstants.ENDFONT + HTMLConstants.ENDHTML); //$NON-NLS-1$
			getContentPane().add(preview, new CellConstraints("4, 4, 1, 1, default, default"));  //$NON-NLS-1$

			{
				JPanel buttonPanel = new JPanel();

				okButton = new JButton();
				okButton.setText(Externalizer.getString("LANSLIM.15")); //$NON-NLS-1$
				okButton.setActionCommand(ShortcutFrameActionCommand.OK);
				okButton.addActionListener(this);
				buttonPanel.add(okButton);
				
				cancelButton = new JButton();
				cancelButton.setText(Externalizer.getString("LANSLIM.16")); //$NON-NLS-1$
				cancelButton.setActionCommand(ShortcutFrameActionCommand.CANCEL);
				cancelButton.addActionListener(this);
				buttonPanel.add(cancelButton);
				
				getContentPane().add(buttonPanel, new CellConstraints("4, 6, 1, 1, left, default")); //$NON-NLS-1$

			}
		} catch(Exception e) {
			SlimLogger.logException("ShortcutFrame.initGUI", e); //$NON-NLS-1$
		}	
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals(ShortcutFrameActionCommand.OK)) {
			currentFont = fonts.getSelectedItem().toString();
			setVisible(false);
		}
		else if (e.getActionCommand().equals(ShortcutFrameActionCommand.CANCEL)) {
			setVisible(false);
		}
		else if (e.getActionCommand().equals(ShortcutFrameActionCommand.FONT)) {
			preview.setText(HTMLConstants.HTML + HTMLConstants.FONTCOLOR + HTMLConstants.BLACK 
					+ HTMLConstants.FONTSIZE + HTMLConstants.DEFAULT_SIZE 
					+ HTMLConstants.FONTFACE + fonts.getSelectedItem().toString() + HTMLConstants.TAGEND 
					+ Externalizer.getString("LANSLIM.74") + HTMLConstants.ENDFONT + HTMLConstants.ENDHTML);
		}
	}
	
	private class ShortcutFrameActionCommand {
		
		public static final String OK = "ok"; //$NON-NLS-1$
		
		public static final String CANCEL = "cancel"; //$NON-NLS-1$
		
		public static final String FONT = "font"; //$NON-NLS-1$
	}

	public String getCurrentFont() {
		return currentFont;
	}

}