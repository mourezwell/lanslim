package com.oz.lanslim.gui;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Set;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.model.SlimContactList;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimSettings;
import com.oz.lanslim.model.SlimUserContact;

import javax.swing.JButton;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
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

	private SlimModel model;
	private SlimUserContact contact;
	
	public NewUserFrame(Frame pParent, SlimModel pModel, SlimUserContact pContact) {
		super(pParent, true);
		contact = pContact;
		model = pModel;
		initGUI();
	}

	private void initGUI() {
		try {
			if (contact == null) {
				setTitle("New User Contact");
			}
			else {
				setTitle("Edit User Contact");
			}
			FormLayout thisLayout = new FormLayout(
					"max(p;5dlu), max(p;5dlu), max(p;5dlu)", 
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)");
			getContentPane().setLayout(thisLayout);
			//this.setPreferredSize(new java.awt.Dimension(190, 160));
			this.setSize(190, 160);
			this.setResizable(false);
			
			{
				nameLabel = new JLabel();
				getContentPane().add(nameLabel, new CellConstraints("1, 1, 1, 1, default, default"));
				nameLabel.setText("Name");
				nameLabel.setPreferredSize(new java.awt.Dimension(30, 15));
			}
			{
				nameField = new JTextField();
				getContentPane().add(nameField, new CellConstraints("3, 1, 1, 1, default, default"));
				nameField.setPreferredSize(new java.awt.Dimension(140, 20));
			}
			{
				hostField = new JTextField();
				getContentPane().add(hostField, new CellConstraints("3, 3, 1, 1, default, default"));
			}
			{
				hostLabel = new JLabel();
				getContentPane().add(hostLabel, new CellConstraints("1, 3, 1, 1, default, default"));
				hostLabel.setText("Host");
			}
			{
				portLabel = new JLabel();
				getContentPane().add(portLabel, new CellConstraints("1, 5, 1, 1, default, default"));
				portLabel.setText("Port");
			}
			{
				portField = new JTextField();
				getContentPane().add(portField, new CellConstraints("3, 5, 1, 1, default, default"));
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
				getContentPane().add(categoryLabel, new CellConstraints("1, 7, 1, 1, default, default"));
				categoryLabel.setText("Category");
			}
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
			{				
				ComboBoxModel categoryComboBoxModel = 
					new DefaultComboBoxModel(lComboBoxModel);
				categoryComboBox = new JComboBox();
				categoryComboBox.setModel(categoryComboBoxModel);
				categoryComboBox.setMaximumSize(new Dimension(40, 20));
				categoryComboBox.setSelectedIndex(0);
				getContentPane().add(categoryComboBox, new CellConstraints("3, 7, 1, 1, default, default"));
			}
			
            String lCat = SlimContactList.CATEGORY_UNDEFINED;
			if (contact != null) {
				nameField.setText(contact.getName());
				hostField.setText(contact.getHost());
				if (model.getSettings().isPortUnlocked()) {
					portField.setText(Integer.toString(contact.getPort()));
				}
				else {
					portField.setText(SlimSettings.DEFAULT_PORT);
				}
				String lUserCat = model.getContacts().getCategory(contact); 
				if (model.getContacts().getCategory(contact) != null) {
                    lCat = lUserCat;
				}
            }
            for (i = 0; i < length; i++) {
                if (lCat.equalsIgnoreCase(lComboBoxModel[i])) {
                    categoryComboBox.setSelectedIndex(i);
                    break;
                }
            }
			{
				buttonPanel = new JPanel();
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
				
				getContentPane().add(buttonPanel, new CellConstraints("3, 9, 1, 1, default, default"));
			}
			
		} catch(Exception e) {
			SlimLogger.log(e + ":" + e.getMessage() + " at NewUserFrame.initGUI");
		}	
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals("OK")) {
			
			
			if (contact == null) {
				try {
					contact = new SlimUserContact(nameField.getText(), 
							hostField.getText(), portField.getText());
					boolean lAdded = model.getContacts().addContact(contact);
					if (!lAdded) {
						contact = null;
						JOptionPane.showMessageDialog(this,
							    "User already declared please remove it first",
							    "Invalid Entries",
							    JOptionPane.WARNING_MESSAGE);
					}
					else {
						model.getContacts().moveUserIntoCategory(contact, 
								(String)categoryComboBox.getSelectedItem());
						setVisible(false);
					}
				}
				catch (SlimException se) {
					JOptionPane.showMessageDialog(this,
						    "At least one parametrs is invalid : " + se.getMessage(),
						    "Invalid Entries",
						    JOptionPane.WARNING_MESSAGE);
				}
			}
			else {
				try {
					SlimUserContact newContact = new SlimUserContact(
							nameField.getText(), hostField.getText(), portField.getText());

					boolean lAdded = model.getContacts().updateContact(contact, newContact);
					if (!lAdded) {
						JOptionPane.showMessageDialog(this,
							    "UserName already declared please remove it first or choose another one",
							    "Invalid Entries",
							    JOptionPane.WARNING_MESSAGE);
					}
					else {
						model.getContacts().moveUserIntoCategory(newContact, 
								(String)categoryComboBox.getSelectedItem());
						JOptionPane.showMessageDialog(this,
							    "User updated successfully but changes will take effect only on new talks",
							    "Update Info",
							    JOptionPane.INFORMATION_MESSAGE);
						setVisible(false);
					}
				}
				catch (SlimException se) {
					JOptionPane.showMessageDialog(this,
						    "At least one parametrs is invalid : " + se.getMessage(),
						    "Invalid Entries",
						    JOptionPane.WARNING_MESSAGE);
				}
			}
		}
		else if (e.getActionCommand().equals("Cancel")) {
			setVisible(false);
		}

	}

}
