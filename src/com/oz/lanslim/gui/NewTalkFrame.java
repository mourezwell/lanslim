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
import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimGroupContact;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimTalk;
import com.oz.lanslim.model.SlimUserContact;

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
				setTitle(Externalizer.getString("LANSLIM.10")); //$NON-NLS-1$
			}
			else {
				setTitle(Externalizer.getString("LANSLIM.11")); //$NON-NLS-1$
			}
			
			FormLayout thisLayout = new FormLayout(
					"max(p;5dlu), max(p;5dlu), max(p;5dlu)",  //$NON-NLS-1$
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)"); //$NON-NLS-1$
			getContentPane().setLayout(thisLayout);
			setSize(250, 225);
			setResizable(false);
			{
				titleField = new JTextField();
				getContentPane().add(titleField, new CellConstraints("3, 1, 1, 1, default, default")); //$NON-NLS-1$
				titleField.setPreferredSize(new java.awt.Dimension(140, 20));
				titleField.setText(Externalizer.getString("LANSLIM.12")); //$NON-NLS-1$
			}
			{
				contactLabel = new JLabel();
				getContentPane().add(contactLabel, new CellConstraints("1, 3, 1, 1, default, default")); //$NON-NLS-1$
				contactLabel.setText(Externalizer.getString("LANSLIM.13")); //$NON-NLS-1$
			}
			{
				contactScrollPane = new JScrollPane();
				getContentPane().add(contactScrollPane, new CellConstraints("3, 3, 1, 1, default, default")); //$NON-NLS-1$
				{
					contactCheckPanel = new JPanel();
					contactScrollPane.setViewportView(contactCheckPanel);
					BoxLayout jPanel1Layout = new BoxLayout(contactCheckPanel, javax.swing.BoxLayout.Y_AXIS);
					contactCheckPanel.setLayout(jPanel1Layout);
					contactScrollPane.setPreferredSize(new java.awt.Dimension(168, 128));
					for (Iterator it = model.getContacts().getAllContacts().iterator(); it.hasNext();) {
						SlimContact c = (SlimContact)it.next();
						JCheckBox cb = new JCheckBox();
						contactCheckBoxList.add(cb);
						cb.addActionListener(this);
						cb.setText(c.getName());
						Font lFont = cb.getFont();
						if (c.isGroup()) {
							cb.setFont(lFont.deriveFont(Font.ITALIC));
						}
						else if (c.getAvailability() == SlimAvailabilityEnum.OFFLINE) {
							cb.setFont(lFont.deriveFont(Font.PLAIN));
						}
						else {
							cb.setFont(lFont);
						}
						int i = 0;
						while (i < contactCheckPanel.getComponentCount() 
							&& c.getName().compareToIgnoreCase(((JCheckBox)contactCheckPanel.getComponent(i)).getText()) > 0) {
							i++;
						}
						contactCheckPanel.add(cb, i);
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
				getContentPane().add(titleLabel, new CellConstraints("1, 1, 1, 1, default, default")); //$NON-NLS-1$
				titleLabel.setText(Externalizer.getString("LANSLIM.14")); //$NON-NLS-1$
				titleLabel.setPreferredSize(new java.awt.Dimension(20, 14));
			}
			{
				buttonPanel = new JPanel();
				okButton = new JButton();
				okButton.setText(Externalizer.getString("LANSLIM.15")); //$NON-NLS-1$
				okButton.setActionCommand(NewTalkActionCommand.OK);
				okButton.addActionListener(this);
				
				cancelButton = new JButton();
				cancelButton.setText(Externalizer.getString("LANSLIM.16")); //$NON-NLS-1$
				cancelButton.setActionCommand(NewTalkActionCommand.CANCEL);
				cancelButton.addActionListener(this);

				buttonPanel.add(okButton);
				buttonPanel.add(cancelButton);
				
				getContentPane().add(buttonPanel, new CellConstraints("3, 5, 1, 1, default, default")); //$NON-NLS-1$
			}
			if (talk != null) {
				titleField.setText(talk.getTitle());
			}
		} catch(Exception e) {
			SlimLogger.logException("NewTalkFrame.initGUI", e); //$NON-NLS-1$
		}	
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals(NewTalkActionCommand.OK)) {
			
			if (talk == null) { // cas new Talk
				
				ArrayList l = new ArrayList();
				for (Iterator it = contactCheckBoxList.iterator(); it.hasNext();) {
					JCheckBox cb = (JCheckBox)it.next(); 
					if (cb.isSelected()){
						l.add(cb.getText());
					}
				}
				if (l.size() < 1) {
					JOptionPane.showMessageDialog(getRootPane().getParent(),
						    Externalizer.getString("LANSLIM.17"), //$NON-NLS-1$
						    Externalizer.getString("LANSLIM.18"), //$NON-NLS-1$
						    JOptionPane.WARNING_MESSAGE);
				}
				else {
					Set cl = new HashSet();
					for (Iterator it = l.iterator(); it.hasNext();) {
						String s = (String)it.next();
						SlimContact c = model.getContacts().getContactByName(s);
						if (c.isGroup()) {
							cl.addAll(((SlimGroupContact)c).getMembers());
						}
						else {
							cl.add(c);
						}
					}
					try {
                        cl.add(model.getSettings().getContactInfo());
						model.getTalks().startNewTalk(titleField.getText(), new ArrayList(cl));
					}
					catch (SlimException se) {
						JOptionPane.showMessageDialog(getRootPane().getParent(),
						    Externalizer.getString("LANSLIM.21"), //$NON-NLS-1$
						    Externalizer.getString("LANSLIM.22"), //$NON-NLS-1$
						    JOptionPane.ERROR_MESSAGE);
					}
					setVisible(false);
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
					JOptionPane.showMessageDialog(getRootPane().getParent(),
						    Externalizer.getString("LANSLIM.17"), //$NON-NLS-1$
						    Externalizer.getString("LANSLIM.18"), //$NON-NLS-1$
						    JOptionPane.WARNING_MESSAGE);
				}
				else {
					Set cl = new HashSet();
					for (Iterator it = l.iterator(); it.hasNext();) {
						String s = (String)it.next();
						SlimContact sc = model.getContacts().getContactByName(s);
						if (sc.isGroup()) {
							cl.addAll(((SlimGroupContact)sc).getOnlineMembers());
						}
						else if (sc.getAvailability() == SlimAvailabilityEnum.ONLINE) {
							cl.add(sc);
						} 
					}
					if (cl.size() < 1) {
						JOptionPane.showMessageDialog(getRootPane().getParent(),
							    Externalizer.getString("LANSLIM.19"), //$NON-NLS-1$
							    Externalizer.getString("LANSLIM.28"), //$NON-NLS-1$
							    JOptionPane.WARNING_MESSAGE);
					}
					else {
						try {
							Iterator lIt = cl.iterator();
							while (lIt.hasNext()) {
								talk.addPeople((SlimUserContact)lIt.next());
							}
						}
						catch (SlimException se) {
							JOptionPane.showMessageDialog(getRootPane().getParent(),
							    Externalizer.getString("LANSLIM.20"), //$NON-NLS-1$
							    Externalizer.getString("LANSLIM.22"), //$NON-NLS-1$
							    JOptionPane.ERROR_MESSAGE);
						}
						setVisible(false);
					}
				}
			}
		}
		else if (e.getActionCommand().equals(NewTalkActionCommand.CANCEL)) {
			setVisible(false);
		}

	}
	
	private class NewTalkActionCommand {
		
		private static final String OK = "OK"; //$NON-NLS-1$
		
		private static final String CANCEL = "CANCEL"; //$NON-NLS-1$
	}


}
