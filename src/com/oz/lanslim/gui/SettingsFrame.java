package com.oz.lanslim.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimUserContact;

import javax.swing.JButton;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;


public class SettingsFrame extends JFrame implements ActionListener {
	
	private JLabel nameLabel;
	private JTextField nameField;
	private JLabel portLabel;
	private JTextField portField;
	private JTextField hostField;
	private JLabel contactLabel;
	private JButton importButton;
	private JButton exportButton;
	private JButton cancelButton;
	private JButton okButton;
	private JLabel hostLabel;
	private JLabel colorLabel;
	private JButton colorButton;
	private JCheckBox startAsTrayButton;
	private JCheckBox resetClosePrefButton;
	private FileDialog fileChooser; 
	
	private SlimModel model;
	
	public SettingsFrame(SlimModel pModel) {
		super();
		model = pModel;
		initGUI();
	}

	private void initGUI() {
		try {
			setTitle("Settings");
			setIconImage(new SlimIcon("comment_edit.png").getImage());
			
			FormLayout thisLayout = new FormLayout(
					"max(p;5dlu), max(p;5dlu), max(p;5dlu)", 
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)");
			getContentPane().setLayout(thisLayout);
			this.setSize(270, 200);
			this.setResizable(false);
			
			{
				nameLabel = new JLabel();
				nameLabel.setText("Name");
				nameLabel.setPreferredSize(new java.awt.Dimension(30, 15));
				getContentPane().add(nameLabel, new CellConstraints("1, 1, 1, 1, default, default"));
			}
			{
				nameField = new JTextField();
				nameField.setPreferredSize(new java.awt.Dimension(140, 20));
				nameField.setText(model.getSettings().getContactInfo().getName());
				getContentPane().add(nameField, new CellConstraints("3, 1, 1, 1, default, default"));
			}
			{
				hostLabel = new JLabel();
				hostLabel.setText("Host");
				getContentPane().add(hostLabel, new CellConstraints("1, 3, 1, 1, default, default"));
			}
			{
				hostField = new JTextField();
				hostField.setText(model.getSettings().getContactInfo().getHost());
				getContentPane().add(hostField, new CellConstraints("3, 3, 1, 1, default, default"));
			}
			{
				portLabel = new JLabel();
				portLabel.setText("Port");
				getContentPane().add(portLabel, new CellConstraints("1, 5, 1, 1, default, default"));
			}
			{
				portField = new JTextField();
				portField.setText(Integer.toString(model.getSettings().getContactInfo().getPort()));
				getContentPane().add(portField, new CellConstraints("3, 5, 1, 1, default, default"));
				portField.setEnabled(model.getSettings().isPortUnlocked());
			}
			{
				colorLabel = new JLabel();
				colorLabel.setText("Color");
				getContentPane().add(colorLabel, new CellConstraints("1, 7, 1, 1, default, default"));
			}
			{
				colorButton = new JButton();
				colorButton.setText("<html><font color=\"" + model.getSettings().getColor() 
						+ "\"><b>Default Color</b></font></html>");
				colorButton.setMaximumSize(new Dimension(140, 20));
				colorButton.addActionListener(this); 
				colorButton.setActionCommand("Color");
				colorButton.setToolTipText("Default Color");
				getContentPane().add(colorButton, new CellConstraints("3, 7, 1, 1, default, default"));
			}
			{
				JPanel closeActionGroupPanel = new JPanel(new GridLayout(0, 1));
			    Border border = BorderFactory.createTitledBorder("System Tray preference");
			    closeActionGroupPanel.setBorder(border);
				getContentPane().add(closeActionGroupPanel, 
						new CellConstraints("3, 9, 1, 1, default, default"));
				if (!model.getSettings().isTrayEnable()) {
					JLabel trayLabel = new JLabel();
					trayLabel.setIcon(new SlimIcon("info.png"));
					closeActionGroupPanel.add(trayLabel);
					trayLabel.setText("<html>SystemTrayIcon disabled, please check that <br>" 
							+ " tray.dll(or so) is present in the executable directory</html>");
				}
				else {
					startAsTrayButton = new JCheckBox("Start As Tray");
					closeActionGroupPanel.add(startAsTrayButton);
				    startAsTrayButton.setSelected(model.getSettings().isStartAsTrayForced());
				}
			    resetClosePrefButton = new JCheckBox("Reset close prefered action");
			    closeActionGroupPanel.add(resetClosePrefButton);
			}
			{
				contactLabel = new JLabel();
				contactLabel.setText("Contact List");
				getContentPane().add(contactLabel, new CellConstraints("1, 11, 1, 1, default, default"));
			}
			{
				JPanel buttonPanel = new JPanel();
				importButton = new JButton();
				importButton.setText("Import");
				importButton.setIcon(new SlimIcon("folder_previous.png"));
				importButton.setActionCommand("Import");
				importButton.addActionListener(this);
				
				exportButton = new JButton();
				exportButton.setText("Export");
				exportButton.setIcon(new SlimIcon("folder_next.png"));
				exportButton.setActionCommand("Export");
				exportButton.addActionListener(this);

				buttonPanel.add(importButton);
				buttonPanel.add(exportButton);
				
				getContentPane().add(buttonPanel, new CellConstraints("3, 11, 1, 1, default, center"));
			}
			{
				JPanel buttonPanel = new JPanel();
				okButton = new JButton();
				okButton.setText("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				
				cancelButton = new JButton();
				cancelButton.setText("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);

				buttonPanel.add(okButton);
				buttonPanel.add(cancelButton);
				
				getContentPane().add(buttonPanel, new CellConstraints("3, 13, 1, 1, default, center"));
			}
			
		} catch(Exception e) {
			SlimLogger.log(e + ":" + e.getMessage() + " at SettingsFrame.initGUI");
		}	
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals("OK")) {
			
			if (resetClosePrefButton.isSelected()) {
				model.getSettings().setCloseForced(false);
				model.getSettings().setCloseAsTrayForced(false);
			}
			if (startAsTrayButton != null) {
				model.getSettings().setStartAsTrayForced(startAsTrayButton.isSelected());
			}
			
			try {
				SlimUserContact contact = 
					new SlimUserContact(nameField.getText(), hostField.getText(), portField.getText());
				
				model.getSettings().updateSettings(contact);
				setVisible(false);
			}
			catch (SlimException se) {
				JOptionPane.showMessageDialog(this,
				    "At least one parametrs is invalid : " + se.getMessage(),
				    "Invalid Entries",
				    JOptionPane.WARNING_MESSAGE);
			}
		}
		else if (e.getActionCommand().equals("Cancel")) {
			setVisible(false);
		}
		else if (e.getActionCommand().equals("Color")) {
		    Color newColor = JColorChooser.showDialog(
		    		this,
		            "Choose Default Text Color",
		            Color.black);
			 if (newColor != null) {
				 try {
					 model.getSettings().setColor(TalkPane.toDoubleHex(newColor.getRed()) 
							 + TalkPane.toDoubleHex(newColor.getGreen()) 
							 + TalkPane.toDoubleHex(newColor.getBlue()));
					 colorButton.setText("<html><font color=\"" + model.getSettings().getColor() 
							 + "\"><b>Default Color</b></font></html>");
				 }
				 catch (SlimException se) {
					 // impossible case due to toDoubleHex method
				 }
			 }
		}
		else if (e.getActionCommand().equals("Export")) {
			fileChooser = new FileDialog(this, "Export Contact", FileDialog.SAVE);
			fileChooser.show();
            String lFileName = fileChooser.getFile();
            if (lFileName != null) {
            	File file = new File(fileChooser.getDirectory() + File.separator + lFileName);
                try {
                	model.getContacts().exportContacts(file);
                }
                catch (IOException ioe) {
    				JOptionPane.showMessageDialog(this,
					    "Unable to export contacts due to : " + ioe + ":" + ioe.getMessage(),
					    "Action Error",
					    JOptionPane.ERROR_MESSAGE);
                }
            }
		}
		else if (e.getActionCommand().equals("Import")) {
			fileChooser = new FileDialog(this, "Import Contact", FileDialog.LOAD);
			fileChooser.show();
            String lFileName = fileChooser.getFile();
            if (lFileName != null) {
                try {
                    File file = new File(fileChooser.getDirectory() + File.separator + lFileName);
                	boolean lError = model.getContacts().importContacts(file);
                	if (lError) {
        				JOptionPane.showMessageDialog(this,
    					    "Unable to import all contacts, check log in home directory for more details",
    					    "Action Incomplete",
    					    JOptionPane.WARNING_MESSAGE);
                	}
                }
                catch (IOException ioe) {
    				JOptionPane.showMessageDialog(this,
					    "Unable to import contacts due to : " + ioe + ":" + ioe.getMessage(),
					    "Action Error",
					    JOptionPane.ERROR_MESSAGE);
                }
            } 
		}
	}

}
