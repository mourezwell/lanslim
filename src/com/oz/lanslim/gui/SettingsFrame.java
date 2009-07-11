package com.oz.lanslim.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.StringConstants;
import com.oz.lanslim.model.HTMLConstants;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimSettings;
import com.oz.lanslim.model.SlimUserContact;

import javax.swing.JButton;

import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class SettingsFrame extends JDialog implements ActionListener {
	
	private JTextField nameField;
	private JTextField portField;
	private JTextField hostField;
	private JButton importButton;
	private JButton exportButton;
	private JRadioButton tableButton;
	private JRadioButton treeButton;
	private JRadioButton multipleButton;
	private JRadioButton quickButton;
	private JButton cancelButton;
	private JButton okButton;
	private JButton colorButton;
	private JButton underlineButton;
	private JButton italicButton;
	private JButton boldButton;
	private JComboBox sizeComboBox;
	private JButton shortcutButton;
	private JCheckBox startAsTrayButton;
	private JCheckBox closeAsTrayButton;
	private FileDialog fileChooser; 
	private JComboBox languageComboBox;
	private JCheckBox checkVersionButton;
	private JCheckBox proxyUsedButton;
	private JTextField proxyHostField;
	private JTextField proxyPortField;
	private JCheckBox cryptoButton;
	private JCheckBox refreshButton;
	private JButton notifButton;
	private JCheckBox escapeXMLBox;

	
	private SlimSettings model;
	private boolean userLocked;
	private boolean boldSelected;
	private boolean italicSelected;
	private boolean underSelected;
	
	public SettingsFrame(Frame pParent, SlimSettings pModel, boolean pUserLocked) {
		super(pParent, true);
		model = pModel;
		userLocked = pUserLocked;
		boldSelected = model.isBold();
		italicSelected = model.isItalic();
		underSelected = model.isUnderline();
		initGUI();
	}

	private void initGUI() {
		try {
			setTitle(Externalizer.getString("LANSLIM.55")); //$NON-NLS-1$
			
			FormLayout thisLayout = new FormLayout(
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)",  //$NON-NLS-1$
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)"); //$NON-NLS-1$
			getContentPane().setLayout(thisLayout);
			setSize(270, 200);
			setResizable(false);
			
			{
				JLabel nameLabel = new JLabel();
				nameLabel.setText(Externalizer.getString("LANSLIM.7")); //$NON-NLS-1$
				nameLabel.setPreferredSize(new java.awt.Dimension(30, 15));
				getContentPane().add(nameLabel, new CellConstraints("2, 2, 1, 1, default, default")); //$NON-NLS-1$

				nameField = new JTextField();
				nameField.setPreferredSize(new java.awt.Dimension(140, 20));
				nameField.setText(model.getContactInfo().getName());
				getContentPane().add(nameField, new CellConstraints("4, 2, 1, 1, default, default")); //$NON-NLS-1$
			}
			{
				JLabel hostLabel = new JLabel();
				hostLabel.setText(Externalizer.getString("LANSLIM.129")); //$NON-NLS-1$
				getContentPane().add(hostLabel, new CellConstraints("2, 4, 1, 1, default, default")); //$NON-NLS-1$

				hostField = new JTextField();
				hostField.setText(model.getContactInfo().getHost());
				getContentPane().add(hostField, new CellConstraints("4, 4, 1, 1, default, default")); //$NON-NLS-1$
			}
			{
				JLabel portLabel = new JLabel();
				portLabel.setText(Externalizer.getString("LANSLIM.130")); //$NON-NLS-1$
				getContentPane().add(portLabel, new CellConstraints("2, 6, 1, 1, default, default")); //$NON-NLS-1$

				portField = new JTextField();
				portField.setText(Integer.toString(model.getContactInfo().getPort()));
				getContentPane().add(portField, new CellConstraints("4, 6, 1, 1, default, default")); //$NON-NLS-1$
				portField.setEnabled(model.isPortUnlocked());
			}
			{
				JLabel styleLabel = new JLabel();
				styleLabel.setText(Externalizer.getString("LANSLIM.145")); //$NON-NLS-1$
				getContentPane().add(styleLabel, new CellConstraints("2, 8, 1, 1, default, default")); //$NON-NLS-1$
				
				JPanel buttonPanel = new JPanel();
				boldButton = new JButton();
				boldButton.setIcon(new SlimIcon("bold.png")); //$NON-NLS-1$
				boldButton.addActionListener(this);
				boldButton.setActionCommand(SettingsActionCommand.BOLD);
				boldButton.setToolTipText(Externalizer.getString("LANSLIM.91")); //$NON-NLS-1$
				boldButton.setBorder(SlimButtonBorder.getSelectedBorder(boldSelected));
				buttonPanel.add(boldButton);

				italicButton = new JButton();
				italicButton.setIcon(new SlimIcon("italic.png")); //$NON-NLS-1$
				italicButton.addActionListener(this);
				italicButton.setActionCommand(SettingsActionCommand.ITALIC);
				italicButton.setToolTipText(Externalizer.getString("LANSLIM.92")); //$NON-NLS-1$
				italicButton.setBorder(SlimButtonBorder.getSelectedBorder(italicSelected));
				buttonPanel.add(italicButton);

				underlineButton = new JButton();
				underlineButton.setIcon(new SlimIcon("underline.png")); //$NON-NLS-1$
				underlineButton.addActionListener(this);
				underlineButton.setActionCommand(SettingsActionCommand.UNDERLINE);
				underlineButton.setToolTipText(Externalizer.getString("LANSLIM.93")); //$NON-NLS-1$
				underlineButton.setBorder(SlimButtonBorder.getSelectedBorder(underSelected));
				buttonPanel.add(underlineButton);

				ComboBoxModel sizeComboBoxModel = 
					new DefaultComboBoxModel(
							new String[] { "1", "2", "3", "4", "5", "6", "7" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
				sizeComboBox = new JComboBox();
				sizeComboBox.setModel(sizeComboBoxModel);
				sizeComboBox.setSelectedIndex(model.getFontSize() - 1);
				sizeComboBox.setMaximumSize(new Dimension(40, 20));
				sizeComboBox.setToolTipText(Externalizer.getString("LANSLIM.95")); //$NON-NLS-1$
				buttonPanel.add(sizeComboBox);
				
				colorButton = new JButton();
				colorButton.setText(HTMLConstants.HTML + HTMLConstants.FONTCOLOR 
						+ model.getColor() + HTMLConstants.FONTSIZE 
						+ "3" + HTMLConstants.TAGEND + HTMLConstants.BOLD +  Externalizer.getString("LANSLIM.94")   //$NON-NLS-1$ //$NON-NLS-2$
						+ HTMLConstants.ENDBOLD + HTMLConstants.ENDFONT + HTMLConstants.ENDHTML);
				colorButton.setMaximumSize(new Dimension(140, 20));
				colorButton.addActionListener(this); 
				colorButton.setActionCommand(SettingsActionCommand.COLOR);
				buttonPanel.add(colorButton);
				
				getContentPane().add(buttonPanel, new CellConstraints("4, 8, 1, 1, default, default")); //$NON-NLS-1$
			}
			{
				JLabel shortcutLabel = new JLabel();
				shortcutLabel.setText(Externalizer.getString("LANSLIM.141")); //$NON-NLS-1$
				getContentPane().add(shortcutLabel, new CellConstraints("2, 10, 1, 1, default, default")); //$NON-NLS-1$

				shortcutButton = new JButton();
				shortcutButton.addActionListener(this);
				shortcutButton.setActionCommand(SettingsActionCommand.SHORTCUT);
				shortcutButton.setText(Externalizer.getString("LANSLIM.142")); //$NON-NLS-1$
				getContentPane().add(shortcutButton, new CellConstraints("4, 10, 1, 1, default, default")); //$NON-NLS-1$
			}
			{
				JLabel trayLabel = new JLabel();
				trayLabel.setText(Externalizer.getString("LANSLIM.143")); //$NON-NLS-1$
				getContentPane().add(trayLabel, new CellConstraints("2, 12, 1, 1, default, default")); //$NON-NLS-1$

				JPanel closeActionGroupPanel = new JPanel();
				getContentPane().add(closeActionGroupPanel, new CellConstraints("4, 12, 1, 1, default, default")); //$NON-NLS-1$
				
				if (!model.isTrayEnable()) {
					JLabel trayWarning = new JLabel();
					trayWarning.setIcon(new SlimIcon("info.png")); //$NON-NLS-1$
					closeActionGroupPanel.add(trayWarning);
					trayWarning.setText(HTMLConstants.HTML + Externalizer.getString("LANSLIM.144") + HTMLConstants.ENDHTML); //$NON-NLS-1$
				}
				else {
					startAsTrayButton = new JCheckBox(Externalizer.getString("LANSLIM.146")); //$NON-NLS-1$
					closeActionGroupPanel.add(startAsTrayButton);
				    startAsTrayButton.setSelected(model.isStartAsTray());
				    
				    closeAsTrayButton = new JCheckBox(Externalizer.getString("LANSLIM.147")); //$NON-NLS-1$
				    closeAsTrayButton.setSelected(model.isCloseAsTray());
				    closeActionGroupPanel.add(closeAsTrayButton);
				}
			}
			{
				JLabel contactLabel = new JLabel();
				contactLabel.setText(Externalizer.getString("LANSLIM.148")); //$NON-NLS-1$
				getContentPane().add(contactLabel, new CellConstraints("2, 14, 1, 1, default, default")); //$NON-NLS-1$

				JPanel buttonPanel = new JPanel();
				importButton = new JButton();
				importButton.setText(Externalizer.getString("LANSLIM.149")); //$NON-NLS-1$
				importButton.setIcon(new SlimIcon("folder_previous.png")); //$NON-NLS-1$
				importButton.setActionCommand(SettingsActionCommand.IMPORT);
				importButton.addActionListener(this);
				
				exportButton = new JButton();
				exportButton.setText(Externalizer.getString("LANSLIM.150")); //$NON-NLS-1$
				exportButton.setIcon(new SlimIcon("folder_next.png")); //$NON-NLS-1$
				exportButton.setActionCommand(SettingsActionCommand.EXPORT);
				exportButton.addActionListener(this);

				buttonPanel.add(importButton);
				buttonPanel.add(exportButton);
				
				getContentPane().add(buttonPanel, new CellConstraints("4, 14, 1, 1, default, center")); //$NON-NLS-1$
			}
			{
				JLabel contactDisplayLabel = new JLabel();
				contactDisplayLabel.setText(Externalizer.getString("LANSLIM.151")); //$NON-NLS-1$
				getContentPane().add(contactDisplayLabel, new CellConstraints("2, 16, 1, 1, default, default")); //$NON-NLS-1$

				JPanel lDisplayPanel = new JPanel();
				ButtonGroup lGroup = new ButtonGroup();
				
				treeButton = new JRadioButton();
				treeButton.setText(Externalizer.getString("LANSLIM.152")); //$NON-NLS-1$
				lDisplayPanel.add(treeButton);
				lGroup.add(treeButton);
				
				tableButton = new JRadioButton();
				tableButton.setText(Externalizer.getString("LANSLIM.153")); //$NON-NLS-1$
				lDisplayPanel.add(tableButton);
				lGroup.add(tableButton);
				
				if (model.isContactTreeView()) {
					treeButton.setSelected(true);
				}
				else {
					tableButton.setSelected(true);
				}
				getContentPane().add(lDisplayPanel, new CellConstraints("4, 16, 1, 1, default, center")); //$NON-NLS-1$
			}
			{
				JLabel dragDropLabel = new JLabel();
				dragDropLabel.setText(Externalizer.getString("LANSLIM.154")); //$NON-NLS-1$
				getContentPane().add(dragDropLabel, new CellConstraints("2, 18, 1, 1, default, default")); //$NON-NLS-1$

				JPanel lDisplayPanel = new JPanel();
				ButtonGroup lGroup = new ButtonGroup();
				
				multipleButton = new JRadioButton();
				multipleButton.setText(Externalizer.getString("LANSLIM.155")); //$NON-NLS-1$
				lDisplayPanel.add(multipleButton);
				lGroup.add(multipleButton);
				
				quickButton = new JRadioButton();
				quickButton.setText(Externalizer.getString("LANSLIM.156")); //$NON-NLS-1$
				lDisplayPanel.add(quickButton);
				lGroup.add(quickButton);
				
				if (model.isQuickDnd()) {
					quickButton.setSelected(true);
				}
				else {
					multipleButton.setSelected(true);
				}
				getContentPane().add(lDisplayPanel, new CellConstraints("4, 18, 1, 1, default, center")); //$NON-NLS-1$
			}
			{
				JLabel languageLabel = new JLabel();
				languageLabel.setText(Externalizer.getString("LANSLIM.24")); //$NON-NLS-1$
				getContentPane().add(languageLabel, new CellConstraints("2, 20, 1, 1, default, default")); //$NON-NLS-1$
				
				ComboBoxModel langComboBoxModel = 
					new DefaultComboBoxModel(model.getAvailableLanguage()); 
				languageComboBox = new JComboBox();
				languageComboBox.setModel(langComboBoxModel);
				languageComboBox.setSelectedItem(model.getLanguage());
				languageComboBox.setMaximumSize(new Dimension(40, 20));
				languageComboBox.addActionListener(this);
				languageComboBox.setActionCommand(SettingsActionCommand.LANGUAGE);
				
				if (model.isQuickDnd()) {
					quickButton.setSelected(true);
				}
				else {
					multipleButton.setSelected(true);
				}
				getContentPane().add(languageComboBox, new CellConstraints("4, 20, 1, 1, default, center")); //$NON-NLS-1$
			}
			{
				JLabel checkVersionLabel = new JLabel();
				checkVersionLabel.setText(Externalizer.getString("LANSLIM.133")); //$NON-NLS-1$
				getContentPane().add(checkVersionLabel, new CellConstraints("2, 22, 1, 1, default, default")); //$NON-NLS-1$

				JPanel buttonGroupPanel = new JPanel();
				getContentPane().add(buttonGroupPanel, new CellConstraints("4, 22, 1, 1, default, default")); //$NON-NLS-1$
				
				checkVersionButton = new JCheckBox(Externalizer.getString("LANSLIM.132")); //$NON-NLS-1$
				checkVersionButton.setSelected(model.isAutoCheckVersion());
				buttonGroupPanel.add(checkVersionButton);
			}
			{
				JLabel proxyLabel = new JLabel();
				proxyLabel.setText(Externalizer.getString("LANSLIM.134")); //$NON-NLS-1$
				getContentPane().add(proxyLabel, new CellConstraints("2, 24, 1, 1, default, default")); //$NON-NLS-1$
				
				JPanel buttonGroupPanel = new JPanel();
				getContentPane().add(buttonGroupPanel, new CellConstraints("4, 24, 1, 1, default, default")); //$NON-NLS-1$
				
				proxyUsedButton = new JCheckBox(StringConstants.EMPTY); 
				proxyUsedButton.setSelected(model.isProxyNeeded());
				proxyUsedButton.setActionCommand(SettingsActionCommand.PROXY);
				proxyUsedButton.addActionListener(this);
				buttonGroupPanel.add(proxyUsedButton);
				
				proxyHostField = new JTextField(Externalizer.getString("LANSLIM.129")); //$NON-NLS-1$
				proxyHostField.setPreferredSize(new Dimension(100, 20));
				proxyHostField.setEnabled(proxyUsedButton.isSelected());
				buttonGroupPanel.add(proxyHostField);
				
				JLabel separatorLabel = new JLabel(StringConstants.COLON);
				buttonGroupPanel.add(separatorLabel);
				
				proxyPortField =  new JTextField(Externalizer.getString("LANSLIM.130")); //$NON-NLS-1$
				proxyPortField.setPreferredSize(new Dimension(30, 20));
				proxyPortField.setEnabled(proxyUsedButton.isSelected());
				buttonGroupPanel.add(proxyPortField);

				if (model.isProxyNeeded()) {
	                proxyPortField.setText(model.getProxyPort());
	                proxyHostField.setText(model.getProxyHost());
				}
			}
			{
				JLabel cryptoLabel = new JLabel();
				cryptoLabel.setText(Externalizer.getString("LANSLIM.135")); //$NON-NLS-1$
				getContentPane().add(cryptoLabel, new CellConstraints("2, 26, 1, 1, default, default")); //$NON-NLS-1$
				
				JPanel buttonGroupPanel = new JPanel();
				getContentPane().add(buttonGroupPanel, new CellConstraints("4, 26, 1, 1, default, default")); //$NON-NLS-1$
				
				cryptoButton = new JCheckBox(Externalizer.getString("LANSLIM.187")); //$NON-NLS-1$
				cryptoButton.setSelected(model.isCryptoEnable());
				buttonGroupPanel.add(cryptoButton);
			}
			{
				JLabel notifLabel = new JLabel();
				notifLabel.setText(Externalizer.getString("LANSLIM.63")); //$NON-NLS-1$
				getContentPane().add(notifLabel, new CellConstraints("2, 28, 1, 1, default, default")); //$NON-NLS-1$

				notifButton = new JButton();
				notifButton.addActionListener(this);
				notifButton.setActionCommand(SettingsActionCommand.NOTIFICATION);
				notifButton.setText(Externalizer.getString("LANSLIM.142")); //$NON-NLS-1$
				getContentPane().add(notifButton, new CellConstraints("4, 28, 1, 1, default, default")); //$NON-NLS-1$
			}
			{
				JLabel refreshLabel = new JLabel();
				refreshLabel.setText(Externalizer.getString("LANSLIM.201")); //$NON-NLS-1$
				getContentPane().add(refreshLabel, new CellConstraints("2, 30, 1, 1, default, default")); //$NON-NLS-1$
				
				JPanel buttonGroupPanel = new JPanel();
				getContentPane().add(buttonGroupPanel, new CellConstraints("4, 30, 1, 1, default, default")); //$NON-NLS-1$
				
				refreshButton = new JCheckBox(Externalizer.getString("LANSLIM.202")); //$NON-NLS-1$
				refreshButton.setSelected(model.isAutoRefreshContacts());
				buttonGroupPanel.add(refreshButton);
			}
			{
				JLabel escapeLabel = new JLabel();
				escapeLabel.setText(Externalizer.getString("LANSLIM.216")); //$NON-NLS-1$
				getContentPane().add(escapeLabel, new CellConstraints("2, 32, 1, 1, default, default")); //$NON-NLS-1$
				
				JPanel buttonGroupPanel = new JPanel();
				getContentPane().add(buttonGroupPanel, new CellConstraints("4, 32, 1, 1, default, default")); //$NON-NLS-1$
				
				escapeXMLBox = new JCheckBox(Externalizer.getString("LANSLIM.217")); //$NON-NLS-1$
				escapeXMLBox.setSelected(model.isAutoEscapeXML());
				buttonGroupPanel.add(escapeXMLBox);
			}
			{
				JPanel buttonPanel = new JPanel();
				okButton = new JButton();
				okButton.setText(Externalizer.getString("LANSLIM.15")); //$NON-NLS-1$
				okButton.setActionCommand(SettingsActionCommand.OK);
				okButton.addActionListener(this);
				
				cancelButton = new JButton();
				cancelButton.setText(Externalizer.getString("LANSLIM.16")); //$NON-NLS-1$
				cancelButton.setActionCommand(SettingsActionCommand.CANCEL);
				cancelButton.addActionListener(this);

				buttonPanel.add(okButton);
				buttonPanel.add(cancelButton);
				
				getContentPane().add(buttonPanel, new CellConstraints("4, 34, 1, 1, default, center")); //$NON-NLS-1$
			}
			
			if (userLocked) {
				nameField.setEnabled(false);
				hostField.setEnabled(false);
				portField.setEnabled(false);
			}
			
		} catch(Exception e) {
			SlimLogger.logException("SettingsFrame.initGUI", e); //$NON-NLS-1$
		}	
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals(SettingsActionCommand.OK)) {
			
            if (model.isTrayEnable()) {
    			model.setCloseAsTray(closeAsTrayButton.isSelected());
    			model.setStartAsTray(startAsTrayButton.isSelected());
            }
			model.setContactTreeView(treeButton.isSelected());
			model.setQuickDnd(quickButton.isSelected());

			model.setBold(boldSelected);
			model.setItalic(italicSelected);
			model.setUnderline(underSelected);

			model.setAutoCheckVersion(checkVersionButton.isSelected());
			model.setAutoRefreshContacts(refreshButton.isSelected());
			model.setAutoEscapeXML(escapeXMLBox.isSelected());
			try {
				model.setCryptoEnable(cryptoButton.isSelected());
			}
			catch (SlimException se) {
				JOptionPane.showMessageDialog(getRootPane().getParent(),
				    Externalizer.getString("LANSLIM.136", se.getMessage()), //$NON-NLS-1$
				    Externalizer.getString("LANSLIM.135"), //$NON-NLS-1$
				    JOptionPane.WARNING_MESSAGE);
			}
			
            if (proxyUsedButton.isSelected()) {
                model.setProxyHost(proxyHostField.getText());
                model.setProxyPort(proxyPortField.getText());
            }
            else {
            	model.setProxyHost(null);
            	model.setProxyHost(null);
            }
            model.setProxyNeeded(proxyUsedButton.isSelected());

			try {
				model.setLanguage((String)languageComboBox.getSelectedItem());
				model.setFontSize(sizeComboBox.getSelectedIndex() + 1);
				
				SlimUserContact contact = 
					new SlimUserContact(nameField.getText(), hostField.getText(), portField.getText());
				contact.setAvailability(SlimAvailabilityEnum.ONLINE);
				model.updateSettings(contact);
				setVisible(false);
			}
			catch (SlimException se) {
				JOptionPane.showMessageDialog(getRootPane().getParent(),
				    Externalizer.getString("LANSLIM.124", se.getMessage()), //$NON-NLS-1$
				    Externalizer.getString("LANSLIM.18"), //$NON-NLS-1$
				    JOptionPane.WARNING_MESSAGE);
			}
		}
		else if (e.getActionCommand().equals(SettingsActionCommand.BOLD)) {
			boldSelected = !boldSelected;
			boldButton.setBorder(SlimButtonBorder.getSelectedBorder(boldSelected));
		}
		else if (e.getActionCommand().equals(SettingsActionCommand.UNDERLINE)) {
			underSelected = !underSelected;
			underlineButton.setBorder(SlimButtonBorder.getSelectedBorder(underSelected));
		}
		else if (e.getActionCommand().equals(SettingsActionCommand.ITALIC)) {
			italicSelected = !italicSelected;
			italicButton.setBorder(SlimButtonBorder.getSelectedBorder(italicSelected));
		}
		else if (e.getActionCommand().equals(SettingsActionCommand.CANCEL)) {
			setVisible(false);
		}
		else if (e.getActionCommand().equals(SettingsActionCommand.LANGUAGE)) {
			JOptionPane.showMessageDialog(getRootPane().getParent(),
				    Externalizer.getString("LANSLIM.197"), //$NON-NLS-1$
				    Externalizer.getString("LANSLIM.122"), //$NON-NLS-1$
				    JOptionPane.INFORMATION_MESSAGE);
		}
		else if (e.getActionCommand().equals(SettingsActionCommand.COLOR)) {
		    Color newColor = JColorChooser.showDialog(
		    		this,
		    		Externalizer.getString("LANSLIM.94"), //$NON-NLS-1$
		            Color.black);
			 if (newColor != null) {
				 try {
					 model.setColor(TalkPane.toDoubleHex(newColor.getRed()) 
							 + TalkPane.toDoubleHex(newColor.getGreen()) 
							 + TalkPane.toDoubleHex(newColor.getBlue()));
					 colorButton.setText(HTMLConstants.HTML + HTMLConstants.FONTCOLOR 
								+ model.getColor() + HTMLConstants.FONTSIZE 
								+ "3" + HTMLConstants.TAGEND + HTMLConstants.BOLD + Externalizer.getString("LANSLIM.94")  //$NON-NLS-1$ //$NON-NLS-2$
								+ HTMLConstants.ENDBOLD + HTMLConstants.ENDFONT + HTMLConstants.ENDHTML);
				 }
				 catch (SlimException se) {
					 // impossible case due to toDoubleHex method
				 }
			 }
		}
		else if (e.getActionCommand().equals(SettingsActionCommand.EXPORT)) {
			fileChooser = new FileDialog((Frame)getOwner(), Externalizer.getString("LANSLIM.150"), FileDialog.SAVE); //$NON-NLS-1$
			fileChooser.show();
            String lFileName = fileChooser.getFile();
            if (lFileName != null) {
            	File file = new File(fileChooser.getDirectory() + File.separator + lFileName);
                try {
                	model.exportContacts(file);
                }
                catch (IOException ioe) {
    				JOptionPane.showMessageDialog(getRootPane().getParent(),
					    Externalizer.getString("LANSLIM.157", SlimLogger.shortFormatException(ioe)), //$NON-NLS-1$
					    Externalizer.getString("LANSLIM.110"), //$NON-NLS-1$
					    JOptionPane.ERROR_MESSAGE);
                }
            }
		}
		else if (e.getActionCommand().equals(SettingsActionCommand.IMPORT)) {
			fileChooser = new FileDialog((Frame)getOwner(), Externalizer.getString("LANSLIM.149"), FileDialog.LOAD); //$NON-NLS-1$
			fileChooser.show();
            String lFileName = fileChooser.getFile();
            if (lFileName != null) {
                try {
                    File file = new File(fileChooser.getDirectory() + File.separator + lFileName);
                	boolean lError = model.importContacts(file);
                	if (lError) {
        				JOptionPane.showMessageDialog(getRootPane().getParent(),
    					    Externalizer.getString("LANSLIM.158"), //$NON-NLS-1$
    					    Externalizer.getString("LANSLIM.159"), //$NON-NLS-1$
    					    JOptionPane.WARNING_MESSAGE);
                	}
                }
                catch (IOException ioe) {
    				JOptionPane.showMessageDialog(getRootPane().getParent(),
					    Externalizer.getString("LANSLIM.160", SlimLogger.shortFormatException(ioe)), //$NON-NLS-1$
					    Externalizer.getString("LANSLIM.110"), //$NON-NLS-1$
					    JOptionPane.ERROR_MESSAGE);
                }
            } 
		}
		else if (e.getActionCommand().equals(SettingsActionCommand.SHORTCUT)) {
			ShortcutFrame lShortcutDialog = new ShortcutFrame(this, model);
			lShortcutDialog.pack();
			lShortcutDialog.setLocationRelativeTo(this);
			lShortcutDialog.setVisible(true);
		}
		else if (e.getActionCommand().equals(SettingsActionCommand.PROXY)) {
			proxyHostField.setEnabled(proxyUsedButton.isSelected());
			proxyPortField.setEnabled(proxyUsedButton.isSelected());
		}
		else if (e.getActionCommand().equals(SettingsActionCommand.NOTIFICATION)) {
			NotificationSettingsFrame lNotificationDialog = new NotificationSettingsFrame(this, model);
			lNotificationDialog.pack();
			lNotificationDialog.setLocationRelativeTo(this);
			lNotificationDialog.setVisible(true);
		}
	}

	private class SettingsActionCommand {
		
		public static final String COLOR = "color"; //$NON-NLS-1$
		
		public static final String BOLD = "bold"; //$NON-NLS-1$

		public static final String ITALIC = "italic"; //$NON-NLS-1$

		public static final String UNDERLINE = "underline"; //$NON-NLS-1$
		
		public static final String SHORTCUT = "shortcut"; //$NON-NLS-1$

		public static final String IMPORT = "import"; //$NON-NLS-1$

		public static final String EXPORT = "export"; //$NON-NLS-1$

		public static final String OK = "ok"; //$NON-NLS-1$

		public static final String CANCEL = "cancel"; //$NON-NLS-1$

		public static final String LANGUAGE = "language"; //$NON-NLS-1$

		public static final String PROXY = "proxy"; //$NON-NLS-1$
		
		public static final String NOTIFICATION = "notification"; //$NON-NLS-1$
		

	}
}
