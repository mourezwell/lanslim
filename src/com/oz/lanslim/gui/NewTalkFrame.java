package com.oz.lanslim.gui;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimGroupContact;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimTalk;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
*/
public class NewTalkFrame extends JDialog implements ActionListener {
	
	private JLabel titleLabel;
	private JTextField titleField;
	private JPanel buttonPanel;
	private JButton cancelButton;
	private JButton okButton;
	private JPanel contactCheckPanel;
	private JScrollPane contactScrollPane;
	private JLabel contactLabel;

	private List contactCheckBoxList;

	private SlimModel model;
	private SlimTalk talk;
	
	public NewTalkFrame(Frame pParent, SlimModel pModel, List pPreSelcetedContacts, SlimTalk pTalk) {
		super(pParent, true);
		model = pModel;
		contactCheckBoxList = new ArrayList();
		talk = pTalk;
		initGUI(pPreSelcetedContacts);
	}

	private void initGUI( List pPreSelcetdContacts) {
		try {
			if (talk == null) {
				setTitle("New Talk");
			}
			else {
				setTitle("Edit/Invite to Talk");
			}
			
			FormLayout thisLayout = new FormLayout(
					"max(p;5dlu), max(p;5dlu), max(p;5dlu)", 
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)");
			getContentPane().setLayout(thisLayout);
			//this.setPreferredSize(new java.awt.Dimension(250, 225));
			this.setSize(250, 225);
			this.setResizable(false);
			{
				titleField = new JTextField();
				getContentPane().add(titleField, new CellConstraints("3, 1, 1, 1, default, default"));
				titleField.setPreferredSize(new java.awt.Dimension(140, 20));
				titleField.setText("New");
			}
			{
				contactLabel = new JLabel();
				getContentPane().add(contactLabel, new CellConstraints("1, 3, 1, 1, default, default"));
				contactLabel.setText("Contacts");
			}
			{
				contactScrollPane = new JScrollPane();
				getContentPane().add(contactScrollPane, new CellConstraints("3, 3, 1, 1, default, default"));
				{
					contactCheckPanel = new JPanel();
					contactScrollPane.setViewportView(contactCheckPanel);
					BoxLayout jPanel1Layout = new BoxLayout(contactCheckPanel, javax.swing.BoxLayout.Y_AXIS);
					contactCheckPanel.setLayout(jPanel1Layout);
					contactScrollPane.setPreferredSize(new java.awt.Dimension(168, 128));
					for (Iterator it = model.getContacts().getOnlineContact().iterator(); it.hasNext();) {
						SlimContact c = (SlimContact)it.next();
						JCheckBox cb = new JCheckBox();
						contactCheckPanel.add(cb);
						cb.addActionListener(this);
						cb.setText(c.getName());
						if (c.isGroup()) {
							cb.setFont(new Font("Default", Font.ITALIC, 11));
						}
						else {
							cb.setFont(new Font("Default", Font.PLAIN, 11));
						}
						contactCheckBoxList.add(cb);
						if (pPreSelcetdContacts != null && pPreSelcetdContacts.contains(c)) {
							cb.setSelected(true);
						}
						if (talk != null && talk.getPeopleIn().indexOf(c.getName()) >= 0) {
							cb.setSelected(true);
							cb.setEnabled(false);
						}
					}
				}
			}
			{
				titleLabel = new JLabel();
				getContentPane().add(titleLabel, new CellConstraints("1, 1, 1, 1, default, default"));
				titleLabel.setText("Title");
				titleLabel.setPreferredSize(new java.awt.Dimension(20, 14));
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
			if (talk != null) {
				titleField.setText(talk.getTitle());
			}
		} catch(Exception e) {
			SlimLogger.log(e + ":" + e.getMessage() + " at NewTalkFrame.initGUI");
		}	
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals("OK")) {
			
			if (talk == null) { // cas new Talk
				
				ArrayList l = new ArrayList();
				for (Iterator it = contactCheckBoxList.iterator(); it.hasNext();) {
					JCheckBox cb = (JCheckBox)it.next(); 
					if (cb.isSelected()){
						l.add(cb.getText());
					}
				}
				if (l.size() < 1) {
					JOptionPane.showMessageDialog(this,
						    "At least one contact must be selected",
						    "Invalid Entries",
						    JOptionPane.WARNING_MESSAGE);
				}
				else {
					Set cl = new HashSet();
					for (Iterator it = l.iterator(); it.hasNext();) {
						String s = (String)it.next();
						SlimContact c = model.getContacts().getContactByName(s);
						if (c.isGroup()) {
							cl.addAll(((SlimGroupContact)c).getOnlineMembers());
						}
						else {
							cl.add(c);
						}
					}
					if (cl.size() == 0) {
						JOptionPane.showMessageDialog(this,
						    "Unable to start new Talk with no online contact",
						    "Invalid Entries",
						    JOptionPane.WARNING_MESSAGE);
					}
					else {
						try {
                            cl.add(model.getSettings().getContactInfo());
							model.getTalks().addTalk(titleField.getText(), new ArrayList(cl));
						}
						catch (SlimException se) {
							JOptionPane.showMessageDialog(this,
							    "Unable to send new Talk Invitation please check your settings",
							    "Network Error",
							    JOptionPane.ERROR_MESSAGE);
						}
						setVisible(false);
					}
				}
			}
			else {
				// update title
				talk.setTitle(titleField.getText());
				// update user
				ArrayList l = new ArrayList();
				for (Iterator it = contactCheckBoxList.iterator(); it.hasNext();) {
					JCheckBox cb = (JCheckBox)it.next();
					if (cb.isSelected()){
						l.add(cb.getText());
					}
				}
				if (l.size() < 1) {
					JOptionPane.showMessageDialog(this,
						    "At least one contact must be selected",
						    "Invalid Entries",
						    JOptionPane.WARNING_MESSAGE);
				}
				else {
					List cl = new ArrayList();
					for (Iterator it = l.iterator(); it.hasNext();) {
						String s = (String)it.next();
						SlimContact c = model.getContacts().getContactByName(s);
						if (c.isGroup()) {
							cl.addAll(((SlimGroupContact)c).getOnlineMembers());
						}
						else {
							cl.add(c);
						}
					}
					try {
						talk.addPeople(cl);
					}
					catch (SlimException se) {
						JOptionPane.showMessageDialog(this,
						    "Unable to send Invitation message or notification",
						    "Network Error",
						    JOptionPane.ERROR_MESSAGE);
					}
					setVisible(false);
				}
			}
		}
		else if (e.getActionCommand().equals("Cancel")) {
			setVisible(false);
		}

	}
	


}
