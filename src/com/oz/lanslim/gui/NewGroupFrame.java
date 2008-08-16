package com.oz.lanslim.gui;	
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimGroupContact;
import com.oz.lanslim.model.SlimModel;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
*/
public class NewGroupFrame extends JDialog implements ActionListener {
	
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
	
	public NewGroupFrame(Frame pParent, SlimModel pModel, SlimGroupContact pContact) {
		super(pParent, true);
		model = pModel;
		membersCheckBoxList = new ArrayList();
		group = pContact;
		initGUI();
	}

	private void initGUI() {
		try {
			if (group == null) {
				setTitle(Externalizer.getString("LANSLIM.119")); //$NON-NLS-1$
			}
			else {
				setTitle(Externalizer.getString("LANSLIM.120")); //$NON-NLS-1$
			}
			
			FormLayout thisLayout = new FormLayout(
					"max(p;5dlu), max(p;5dlu), max(p;5dlu)",  //$NON-NLS-1$
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)"); //$NON-NLS-1$
			getContentPane().setLayout(thisLayout);
			setSize(250, 225);
			setResizable(false);
			{
				nameField = new JTextField();
				getContentPane().add(nameField, new CellConstraints("3, 1, 1, 1, default, default")); //$NON-NLS-1$
				nameField.setPreferredSize(new java.awt.Dimension(140, 20));
				if (group != null) {
					nameField.setText(group.getName());
					nameField.setEditable(false);
				}
			}
			{
				membersLabel = new JLabel();
				getContentPane().add(membersLabel, new CellConstraints("1, 3, 1, 1, default, default")); //$NON-NLS-1$
				membersLabel.setText(Externalizer.getString("LANSLIM.121")); //$NON-NLS-1$
			}
			{
				membersScrollPane = new JScrollPane();
				getContentPane().add(membersScrollPane, new CellConstraints("3, 3, 1, 1, default, default")); //$NON-NLS-1$
				{
					membersCheckPanel = new JPanel();
					membersScrollPane.setViewportView(membersCheckPanel);
					BoxLayout jPanel1Layout = new BoxLayout(membersCheckPanel, javax.swing.BoxLayout.Y_AXIS);
					membersCheckPanel.setLayout(jPanel1Layout);
					membersScrollPane.setPreferredSize(new java.awt.Dimension(168, 128));
					for (Iterator it = model.getContacts().getAllUserContact().iterator(); it.hasNext();) {
						SlimContact c =(SlimContact)it.next();
						JCheckBox cb = new JCheckBox();
						int i = 0;
						while (i < membersCheckPanel.getComponentCount() 
							&& c.getName().compareToIgnoreCase(((JCheckBox)membersCheckPanel.getComponent(i)).getText()) > 0) {
							i++;
						}
						membersCheckPanel.add(cb, i);
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
				getContentPane().add(nameLabel, new CellConstraints("1, 1, 1, 1, default, default")); //$NON-NLS-1$
				nameLabel.setText(Externalizer.getString("LANSLIM.7")); //$NON-NLS-1$
				nameLabel.setPreferredSize(new java.awt.Dimension(20, 14));
			}
			{
				buttonPanel = new JPanel();
				okButton = new JButton();
				okButton.setText(Externalizer.getString("LANSLIM.15")); //$NON-NLS-1$
				okButton.setActionCommand(NewGroupActionCommand.OK);
				okButton.addActionListener(this);
				
				cancelButton = new JButton();
				cancelButton.setText(Externalizer.getString("LANSLIM.16")); //$NON-NLS-1$
				cancelButton.setActionCommand(NewGroupActionCommand.CANCEL);
				cancelButton.addActionListener(this);

				buttonPanel.add(okButton);
				buttonPanel.add(cancelButton);
				
				getContentPane().add(buttonPanel, new CellConstraints("3, 5, 1, 1, default, default")); //$NON-NLS-1$
			}
		} catch(Exception e) {
			SlimLogger.logException("NewGroupFrame.initGUI", e); //$NON-NLS-1$
		}	
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals(NewGroupActionCommand.OK)) {
			
			List list = new ArrayList();
			for (Iterator it = membersCheckBoxList.iterator(); it.hasNext();) {
				JCheckBox cb = (JCheckBox)it.next();
				if (cb.isSelected()){
					list.add(model.getContacts().getContactByName(cb.getText()));
				}
			}
			if (list.size() == 0) {
				JOptionPane.showMessageDialog(getRootPane().getParent(),
					    Externalizer.getString("LANSLIM.17"), //$NON-NLS-1$
					    Externalizer.getString("LANSLIM.18"), //$NON-NLS-1$
					    JOptionPane.WARNING_MESSAGE);
			}
			else {
				if (group == null) {
					try {
						group = new SlimGroupContact(nameField.getText(), list);
						boolean lAdded = model.getContacts().addContact(group);
						if (!lAdded) {
							group = null;
							JOptionPane.showMessageDialog(getRootPane().getParent(),
								    Externalizer.getString("LANSLIM.123"), //$NON-NLS-1$
								    Externalizer.getString("LANSLIM.18"), //$NON-NLS-1$
								    JOptionPane.WARNING_MESSAGE);
						}
						else {
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
						SlimGroupContact sgc = new SlimGroupContact(nameField.getText(), list);
						boolean lUpdated = model.getContacts().updateContact(group, sgc);
						if (!lUpdated) {
							JOptionPane.showMessageDialog(getRootPane().getParent(),
								    Externalizer.getString("LANSLIM.123"), //$NON-NLS-1$
								    Externalizer.getString("LANSLIM.18"), //$NON-NLS-1$
								    JOptionPane.WARNING_MESSAGE);
						}
						else {
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
		}
		else if (e.getActionCommand().equals(NewGroupActionCommand.CANCEL)) {
			setVisible(false);
		}

	}
	
	private class NewGroupActionCommand {
		
		private static final String OK = "OK"; //$NON-NLS-1$
		
		private static final String CANCEL = "CANCEL"; //$NON-NLS-1$
	}



}
