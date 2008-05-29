package com.oz.lanslim.gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimGroupContact;
import com.oz.lanslim.model.SlimModel;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
*/
public class NewGroupFrame extends JFrame implements ActionListener {
	
	private JLabel nameLabel;
	private JTextField nameField;
	private JPanel buttonPanel;
	private JButton cancelButton;
	private JButton okButton;
	private JPanel membersCheckPanel;
	private JScrollPane membersScrollPane;
	private JLabel membersLabel;

	private List membersCheckBoxList;

	private SlimModel model;
	private SlimGroupContact group;
	
	public NewGroupFrame(SlimModel pModel, SlimGroupContact pContact) {
		super();
		model = pModel;
		membersCheckBoxList = new ArrayList();
		group = pContact;
		initGUI();
	}

	private void initGUI() {
		try {
			setTitle("New User Contact");
			setIconImage(new SlimIcon("comment_edit.png").getImage());
			
			FormLayout thisLayout = new FormLayout(
					"max(p;5dlu), max(p;5dlu), max(p;5dlu)", 
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)");
			getContentPane().setLayout(thisLayout);
			//this.setPreferredSize(new java.awt.Dimension(250, 225));
			this.setSize(250, 225);
			this.setResizable(false);
			{
				nameField = new JTextField();
				getContentPane().add(nameField, new CellConstraints("3, 1, 1, 1, default, default"));
				nameField.setPreferredSize(new java.awt.Dimension(140, 20));
				if (group != null) {
					nameField.setText(group.getName());
					nameField.setEditable(false);
				}
			}
			{
				membersLabel = new JLabel();
				getContentPane().add(membersLabel, new CellConstraints("1, 3, 1, 1, default, default"));
				membersLabel.setText("Members");
			}
			{
				membersScrollPane = new JScrollPane();
				getContentPane().add(membersScrollPane, new CellConstraints("3, 3, 1, 1, default, default"));
				{
					membersCheckPanel = new JPanel();
					membersScrollPane.setViewportView(membersCheckPanel);
					BoxLayout jPanel1Layout = new BoxLayout(membersCheckPanel, javax.swing.BoxLayout.Y_AXIS);
					membersCheckPanel.setLayout(jPanel1Layout);
					membersScrollPane.setPreferredSize(new java.awt.Dimension(168, 128));
					for (Iterator it = model.getContacts().getAllUserContact().iterator(); it.hasNext();) {
						SlimContact c =(SlimContact)it.next();
						JCheckBox cb = new JCheckBox();
						membersCheckPanel.add(cb);
						cb.addActionListener(this);
						cb.setText(c.getName());
						membersCheckBoxList.add(cb);
						if (group != null && group.getMembers().contains(c)) {
							cb.setSelected(true);
						}
					}
				}
			}
			{
				nameLabel = new JLabel();
				getContentPane().add(nameLabel, new CellConstraints("1, 1, 1, 1, default, default"));
				nameLabel.setText("Name");
				nameLabel.setPreferredSize(new java.awt.Dimension(20, 14));
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
				
				getContentPane().add(buttonPanel, new CellConstraints("3, 5, 1, 1, default, default"));
			}
		} catch(Exception e) {
			SlimLogger.log(e + ":" + e.getMessage() + " at NewGroupFrame.initGUI");
		}	
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals("OK")) {
			
			List list = new ArrayList();
			for (Iterator it = membersCheckBoxList.iterator(); it.hasNext();) {
				JCheckBox cb = (JCheckBox)it.next();
				if (cb.isSelected()){
					list.add(model.getContacts().getContactByName(cb.getText()));
				}
			}
			if (list.size() == 0) {
				JOptionPane.showMessageDialog(this,
					    "At least one user must be selected as member",
					    "Invalid Entries",
					    JOptionPane.WARNING_MESSAGE);
			}
			else {
				if (group == null) {
					try {
						group = new SlimGroupContact(nameField.getText(), list);
						boolean lAdded = model.getContacts().addContact(group);
						if (!lAdded) {
							group = null;
							JOptionPane.showMessageDialog(this,
								    "Group already declared please remove it first",
								    "Invalid Entries",
								    JOptionPane.WARNING_MESSAGE);
						}
						else {
							setVisible(false);
						}
					}
					catch (SlimException se) {
						JOptionPane.showMessageDialog(this,
							    "At least one parameters is invalid : " + se.getMessage(),
							    "Invalid Entries",
							    JOptionPane.WARNING_MESSAGE);
					}
				}
				else {
					try {
						SlimGroupContact sgc = new SlimGroupContact(nameField.getText(), list);
						boolean lUpdated = model.getContacts().updateContact(group, sgc);
						if (!lUpdated) {
							JOptionPane.showMessageDialog(this,
								    "GroupName already declared please remove it first or choose another One",
								    "Invalid Entries",
								    JOptionPane.WARNING_MESSAGE);
						}
						else {
							JOptionPane.showMessageDialog(this,
								    "Group updated successfully but changes will take effect only on new talks",
								    "Update Info",
								    JOptionPane.INFORMATION_MESSAGE);
							setVisible(false);
						}
					}
					catch (SlimException se) {
						JOptionPane.showMessageDialog(this,
							    "At least one parameters is invalid : " + se.getMessage(),
							    "Invalid Entries",
							    JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		}
		else if (e.getActionCommand().equals("Cancel")) {
			setVisible(false);
		}

	}
	


}
