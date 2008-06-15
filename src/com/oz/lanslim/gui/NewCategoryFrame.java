package com.oz.lanslim.gui;	
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.model.SlimContactList;
import com.oz.lanslim.model.SlimModel;

import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
*/
public class NewCategoryFrame extends JDialog implements ActionListener {
	
	private JLabel nameLabel;
	private JTextField nameField;
	private JPanel buttonPanel;
	private JButton cancelButton;
	private JButton okButton;

	private SlimModel model;
	private String categoryName;
	
	public NewCategoryFrame(Frame pParent, SlimModel pModel, String pCategoryName) {
		super(pParent, true);
		model = pModel;
		categoryName = pCategoryName;
		initGUI();
	}

	private void initGUI() {
		try {
			if (categoryName == null) {
				setTitle("New Category Contact");
			}
			else {
				setTitle("Edit Category Contact");
			}
			
			FormLayout thisLayout = new FormLayout(
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)", 
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)");
			getContentPane().setLayout(thisLayout);
			//this.setPreferredSize(new java.awt.Dimension(250, 225));
			this.setSize(250, 200);
			this.setResizable(false);
			{
				nameLabel = new JLabel();
				getContentPane().add(nameLabel, new CellConstraints("2, 2, 1, 1, default, default"));
				nameLabel.setText("Name");
				nameLabel.setPreferredSize(new java.awt.Dimension(30, 14));
			}
			{
				nameField = new JTextField();
				getContentPane().add(nameField, new CellConstraints("4, 2, 1, 1, default, default"));
				nameField.setPreferredSize(new java.awt.Dimension(150, 20));
				if (categoryName != null) {
					nameField.setText(categoryName);
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
				
				getContentPane().add(buttonPanel, new CellConstraints("4, 4, 1, 1, default, default"));
			}
		} catch(Exception e) {
			SlimLogger.log(e + ":" + e.getMessage() + " at NewGroupFrame.initGUI");
		}	
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals("OK")) {
			
			String lNewName = nameField.getText();
			if (lNewName == null || lNewName.length() == 0) {
				JOptionPane.showMessageDialog(this,
					    "Name field must not empty",
					    "Invalid Entries",
					    JOptionPane.WARNING_MESSAGE);
			}
			else {
				if (lNewName.equals(SlimContactList.CATEGORY_GROUP) 
						|| lNewName.equals(SlimContactList.CATEGORY_UNDEFINED)) {
					JOptionPane.showMessageDialog(this,
						    "Name field must filled with either of the two following reserved catgory names " 
							+ SlimContactList.CATEGORY_GROUP + " or " + SlimContactList.CATEGORY_UNDEFINED,
						    "Invalid Entries",
						    JOptionPane.WARNING_MESSAGE);
				}
				else {
					if (model.getContacts().getAllCategories().contains(lNewName)) {
						JOptionPane.showMessageDialog(this,
							    "Name field must not contain already existing category name",
							    "Invalid Entries",
							    JOptionPane.WARNING_MESSAGE);
					}
					else {
						if (categoryName == null) {
							model.getContacts().addCategory(lNewName, new Boolean(true));
						}
						else {
							model.getContacts().renameCategory(categoryName, lNewName);
						}
						setVisible(false);
					}
				}
			}
		}
		else if (e.getActionCommand().equals("Cancel")) {
			setVisible(false);
		}

	}
	


}
