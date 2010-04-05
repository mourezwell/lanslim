package com.oz.lanslim.gui;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Set;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.model.SlimContactList;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimUserContact;

import javax.swing.JButton;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
*/
public class NewUserFrame extends JDialog implements ActionListener {
	
	private JLabel nameLabel;
	private JTextField nameField;
	private JLabel portLabel;
	private JTextField portField;
	private JLabel hostLabel;
	private JTextField hostField;
	private JLabel categoryLabel;
	private JComboBox categoryComboBox;
	private JPanel buttonPanel;
	private JButton cancelButton;
	private JButton okButton;
	private JCheckBox blockedBox;

	private SlimModel model;
	private SlimUserContact contact;
	private boolean lock;
	
	public NewUserFrame(Frame pParent, SlimModel pModel, SlimUserContact pContact, boolean pLocked) {
		super(pParent, true);
		contact = pContact;
		model = pModel;
		lock = pLocked;
		initGUI();
	}

	private void initGUI() {
		try {
			if (contact == null) {
				setTitle(Externalizer.getString("LANSLIM.126")); //$NON-NLS-1$
			}
			else {
				setTitle(Externalizer.getString("LANSLIM.127")); //$NON-NLS-1$
			}
			FormLayout thisLayout = new FormLayout(
					"max(p;5dlu), max(p;5dlu), max(p;5dlu)",  //$NON-NLS-1$
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)"); //$NON-NLS-1$
			getContentPane().setLayout(thisLayout);
			setSize(190, 160);
			setResizable(false);
			
			{
				nameLabel = new JLabel();
				getContentPane().add(nameLabel, new CellConstraints("1, 1, 1, 1, default, default")); //$NON-NLS-1$
				nameLabel.setText(Externalizer.getString("LANSLIM.7")); //$NON-NLS-1$
				nameLabel.setPreferredSize(new java.awt.Dimension(30, 15));
			}
			{
				nameField = new JTextField();
				getContentPane().add(nameField, new CellConstraints("3, 1, 1, 1, default, default")); //$NON-NLS-1$
				nameField.setPreferredSize(new java.awt.Dimension(140, 20));
			}
			{
				hostLabel = new JLabel();
				getContentPane().add(hostLabel, new CellConstraints("1, 3, 1, 1, default, default")); //$NON-NLS-1$
				hostLabel.setText(Externalizer.getString("LANSLIM.129")); //$NON-NLS-1$
			}
			{
				hostField = new JTextField();
				getContentPane().add(hostField, new CellConstraints("3, 3, 1, 1, default, default")); //$NON-NLS-1$
			}
			{
				portLabel = new JLabel();
				getContentPane().add(portLabel, new CellConstraints("1, 5, 1, 1, default, default")); //$NON-NLS-1$
				portLabel.setText(Externalizer.getString("LANSLIM.130")); //$NON-NLS-1$
			}
			{
				portField = new JTextField();
				getContentPane().add(portField, new CellConstraints("3, 5, 1, 1, default, default")); //$NON-NLS-1$
				portField.setText(Integer.toString(model.getSettings().getContactInfo().getPort()));
				if (model.getSettings().isPortUnlocked()) {
					portField.setEnabled(true);
				}
				else {
					portField.setEnabled(false);
				}
			}
			{
				categoryLabel = new JLabel();
				getContentPane().add(categoryLabel, new CellConstraints("1, 7, 1, 1, default, default")); //$NON-NLS-1$
				categoryLabel.setText(Externalizer.getString("LANSLIM.128")); //$NON-NLS-1$
			}
			{				
				Set lCategoryList = model.getContacts().getAllCategories();
				int length = lCategoryList.size();
				String[] lComboBoxModel = new String[length - 1];
				Iterator it = lCategoryList.iterator();
				int i = 0;
				while (it.hasNext()) {
	                String lCat = (String)it.next();
	                if (!SlimContactList.CATEGORY_GROUP.equals(lCat)) {
	                    lComboBoxModel[i] = lCat;
	                    i = i + 1;
	                }
				}
				
				ComboBoxModel categoryComboBoxModel = 
					new DefaultComboBoxModel(lComboBoxModel);
				categoryComboBox = new JComboBox();
				categoryComboBox.setModel(categoryComboBoxModel);
				categoryComboBox.setMaximumSize(new Dimension(40, 20));
				categoryComboBox.setSelectedIndex(0);
				getContentPane().add(categoryComboBox, new CellConstraints("3, 7, 1, 1, default, default")); //$NON-NLS-1$
			}
			{
				blockedBox = new JCheckBox(Externalizer.getString("LANSLIM.242")); //$NON-NLS-1$
				getContentPane().add(blockedBox, new CellConstraints("3, 9, 1, 1, default, default")); //$NON-NLS-1$
			}

			
			
            String lCat = SlimContactList.CATEGORY_UNDEFINED;
			if (contact != null) {
				nameField.setText(contact.getName());
				hostField.setText(contact.getHost());
				portField.setText(Integer.toString(contact.getPort()));
				
				if (lock) {
					nameField.setEnabled(false);
					hostField.setEnabled(false);
					portField.setEnabled(false);
				}
				
				String lUserCat = model.getContacts().getCategory(contact); 
				if (model.getContacts().getCategory(contact) != null) {
                    lCat = lUserCat;
				}
				Set lCategoryList = model.getContacts().getAllCategories();
				int length = lCategoryList.size();
	            for (int i = 0; i < length; i++) {
	                if (lCat.equalsIgnoreCase((String)categoryComboBox.getItemAt(i))) {
	                    categoryComboBox.setSelectedIndex(i);
	                    break;
	                }
	            }
	            blockedBox.setSelected(contact.isBlocked());
            }
			{
				buttonPanel = new JPanel();
				okButton = new JButton();
				okButton.setText(Externalizer.getString("LANSLIM.15")); //$NON-NLS-1$
				okButton.setActionCommand(NewUserActionCommand.OK);
				okButton.addActionListener(this);
				
				cancelButton = new JButton();
				cancelButton.setText(Externalizer.getString("LANSLIM.16")); //$NON-NLS-1$
				cancelButton.setActionCommand(NewUserActionCommand.CANCEL);
				cancelButton.addActionListener(this);

				buttonPanel.add(okButton);
				buttonPanel.add(cancelButton);
				
				getContentPane().add(buttonPanel, new CellConstraints("3, 11, 1, 1, default, default")); //$NON-NLS-1$
			}
			
		} catch(Exception e) {
			SlimLogger.logException("NewUserFrame.initGUI", e); //$NON-NLS-1$
		}	
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals(NewUserActionCommand.OK)) {
			
			
			if (contact == null) {
				try {
					contact = new SlimUserContact(nameField.getText(), 
							hostField.getText(), portField.getText());
					boolean lAdded = model.getContacts().addContact(contact);
					if (!lAdded) {
						contact = null;
						JOptionPane.showMessageDialog(getRootPane().getParent(),
							    Externalizer.getString("LANSLIM.123"), //$NON-NLS-1$
							    Externalizer.getString("LANSLIM.18"), //$NON-NLS-1$
							    JOptionPane.WARNING_MESSAGE);
					}
					else {
						model.getContacts().moveUserIntoCategory(contact, 
								(String)categoryComboBox.getSelectedItem());
						if (blockedBox.isSelected()) {
							model.getContacts().blockContact(contact);
						}
						model.getContacts().moveUserIntoCategory(contact, 
								(String)categoryComboBox.getSelectedItem());
						setVisible(false);
					}
				}
				catch (SlimException se) {
					JOptionPane.showMessageDialog(getRootPane().getParent(),
						    Externalizer.getString("LANSLIM.124", se.getMessage()), //$NON-NLS-1$
						    Externalizer.getString("LANSLIM.18"), //$NON-NLS-1$
						    JOptionPane.WARNING_MESSAGE);
				}
			}
			else {
				try {
					SlimUserContact newContact = new SlimUserContact(
							nameField.getText(), hostField.getText(), portField.getText());

					boolean lAdded = model.getContacts().updateContact(contact, newContact);
					if (!lAdded) {
						JOptionPane.showMessageDialog(getRootPane().getParent(),
							    Externalizer.getString("LANSLIM.123"), //$NON-NLS-1$
							    Externalizer.getString("LANSLIM.18"), //$NON-NLS-1$
							    JOptionPane.WARNING_MESSAGE);
					}
					else {
						model.getContacts().moveUserIntoCategory(newContact, 
								(String)categoryComboBox.getSelectedItem());
						if (blockedBox.isSelected() != contact.isBlocked()) {
							model.getContacts().blockContact(contact);
						}
						JOptionPane.showMessageDialog(getRootPane().getParent(),
							    Externalizer.getString("LANSLIM.125"), //$NON-NLS-1$
							    Externalizer.getString("LANSLIM.122"), //$NON-NLS-1$
							    JOptionPane.INFORMATION_MESSAGE);
						setVisible(false);
					}
				}
				catch (SlimException se) {
					JOptionPane.showMessageDialog(getRootPane().getParent(),
						    Externalizer.getString("LANSLIM.124", se.getMessage()), //$NON-NLS-1$
						    Externalizer.getString("LANSLIM.18"), //$NON-NLS-1$
						    JOptionPane.WARNING_MESSAGE);
				}
			}
		}
		else if (e.getActionCommand().equals(NewUserActionCommand.CANCEL)) {
			setVisible(false);
		}

	}

	private class NewUserActionCommand {
		
		private static final String OK = "OK"; //$NON-NLS-1$
		
		private static final String CANCEL = "CANCEL"; //$NON-NLS-1$
	}

}
