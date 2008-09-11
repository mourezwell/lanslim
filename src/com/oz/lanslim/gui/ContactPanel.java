package com.oz.lanslim.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.StringConstants;
import com.oz.lanslim.model.ContactViewListener;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimContactList;
import com.oz.lanslim.model.SlimGroupContact;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimTalk;
import com.oz.lanslim.model.SlimUserContact;

public class ContactPanel extends JPanel 
	implements KeyListener, ContactViewListener, ActionListener  {

	private JToolBar contactBar;
	private JButton newContactButton;
	private JButton newGroupButton;
	private JButton deleteButton;
	private JButton editButton;
	private JButton refreshButton;
	private JButton newCatButton;
	
	private JScrollPane contactTablePane;
	private ContactView contactTable;
	private JPanel filterPane;
	private JLabel filterLabel;
	private JTextField filterField;
	private JCheckBox hideGroupCheckBox;
	private JCheckBox hideOfflineCheckBox;

	private ContactPopupMenu popupMenu;

	private SlimModel model;
	private TalkSelector talkDisplay;
	
	public ContactPanel(SlimModel pModel, TalkSelector pDisplay) {
		model = pModel;
		talkDisplay = pDisplay;
		init();
		model.getSettings().registerContcatViewListener(this);

	}
	
	private void init() {
		
		setLayout(new BorderLayout());
		setPreferredSize(new java.awt.Dimension(200, 100));

		contactBar = new JToolBar();
		contactBar.setFloatable(false);
		contactBar.setRollover(true);
		add(contactBar, BorderLayout.NORTH);
		{
			newContactButton = new JButton();
			newContactButton.setIcon(new SlimIcon("user_add.png")); //$NON-NLS-1$
			newContactButton.setActionCommand(ContactPaneActionCommand.USER_ADD);
			newContactButton.addActionListener(this);
			newContactButton.setToolTipText(Externalizer.getString("LANSLIM.77")); //$NON-NLS-1$
			contactBar.add(newContactButton);
		}
		{
			newGroupButton = new JButton();
			newGroupButton.setIcon(new SlimIcon("users.png")); //$NON-NLS-1$
			newGroupButton.setActionCommand(ContactPaneActionCommand.GROUP_ADD);
			newGroupButton.addActionListener(this);
			newGroupButton.setToolTipText(Externalizer.getString("LANSLIM.78")); //$NON-NLS-1$
			contactBar.add(newGroupButton);
		}
		{
			deleteButton = new JButton();
			deleteButton.setIcon(new SlimIcon("user_remove.png")); //$NON-NLS-1$
			deleteButton.setActionCommand(ContactPaneActionCommand.USER_DEL);
			deleteButton.addActionListener(this);
			deleteButton.setToolTipText(Externalizer.getString("LANSLIM.79")); //$NON-NLS-1$
			contactBar.add(deleteButton);
		}
		{
			editButton = new JButton();
			editButton.setIcon(new SlimIcon("note_edit.png")); //$NON-NLS-1$
			editButton.setActionCommand(ContactPaneActionCommand.USER_EDIT);
			editButton.addActionListener(this);
			editButton.setToolTipText(Externalizer.getString("LANSLIM.80")); //$NON-NLS-1$
			contactBar.add(editButton);
		}
		{
			refreshButton = new JButton();
			refreshButton.setIcon(new SlimIcon("refresh.png")); //$NON-NLS-1$
			refreshButton.setActionCommand(ContactPaneActionCommand.ALL_REFRESH);
			refreshButton.addActionListener(this);
			refreshButton.setToolTipText(Externalizer.getString("LANSLIM.81")); //$NON-NLS-1$
			contactBar.add(refreshButton);
		}
		{
			newCatButton = new JButton();
			newCatButton.setIcon(new SlimIcon("folder_add.png")); //$NON-NLS-1$
			newCatButton.setActionCommand(ContactPaneActionCommand.CATEGORY_NEW);
			newCatButton.addActionListener(this);
			newCatButton.setToolTipText(Externalizer.getString("LANSLIM.82")); //$NON-NLS-1$
			contactBar.add(newCatButton);
		}
		{
			contactTablePane = new JScrollPane();
			add(contactTablePane, BorderLayout.CENTER);
	        //Create the popup menu.
	        popupMenu = new ContactPopupMenu(this);
			{
				if (!model.getSettings().isContactTreeView()) {
					contactTable = new ContactTable(model, popupMenu);
					contactTablePane.setViewportView(((JTable)contactTable));
					newCatButton.setEnabled(false);
				}
				else {
					contactTable = new ContactTree(model, popupMenu);
					contactTablePane.setViewportView(((JTree)contactTable));
					newCatButton.setEnabled(true);
				}
				model.getContacts().updateListener();
			}
		}
		{
			filterPane = new JPanel();
			filterPane.setLayout(new GridLayout(2, 2));
			add(filterPane, BorderLayout.SOUTH);
			{
				hideGroupCheckBox = new JCheckBox(Externalizer.getString("LANSLIM.83")); //$NON-NLS-1$
				hideGroupCheckBox.addActionListener(this);
				hideGroupCheckBox.setActionCommand(ContactPaneActionCommand.HIDE_GROUPS);
				hideGroupCheckBox.setSelected(model.getSettings().isGroupHidden());
				filterPane.add(hideGroupCheckBox);
			}
			{
				hideOfflineCheckBox = new JCheckBox(Externalizer.getString("LANSLIM.84")); //$NON-NLS-1$
				hideOfflineCheckBox.addActionListener(this);
				hideOfflineCheckBox.setActionCommand(ContactPaneActionCommand.HIDE_OFFLINE);
				hideOfflineCheckBox.setSelected(model.getSettings().isOfflineHidden());
				filterPane.add(hideOfflineCheckBox);
			}
			{
				filterLabel = new JLabel(Externalizer.getString("LANSLIM.85"), new SlimIcon("search.png"), SwingConstants.LEADING); //$NON-NLS-1$ //$NON-NLS-2$
				filterPane.add(filterLabel);
				filterField = new JTextField();
				filterField.setColumns(15);
				filterField.addKeyListener(this);
				filterPane.add(filterField);
			}
		}
	}
	
	public void keyPressed(KeyEvent e) {
		// nothing to do here
	}

	public void keyReleased(KeyEvent e) {
		contactTable.filter(filterField.getText(), 
				hideGroupCheckBox.isSelected(), hideOfflineCheckBox.isSelected());
	}

	public void keyTyped(KeyEvent e) {
		// nothing to do here
	}
	
	public void updateContactDisplay(boolean isTreeview) {
		
		if (isTreeview && contactTable instanceof JTable) {
			contactTable = new ContactTree(model, popupMenu);
			contactTablePane.setViewportView(((JTree)contactTable));
			newCatButton.setEnabled(true);
		}
		else if (!isTreeview && contactTable instanceof JTree) {
			contactTable = new ContactTable(model, popupMenu);
			contactTablePane.setViewportView(((JTable)contactTable));
			newCatButton.setEnabled(false);
		}
		model.getContacts().updateListener();

	}

	protected ContactView getContactView() {
		return contactTable;
	}

	public void actionPerformed(ActionEvent e ) {
		if (model.getSettings().areValidSettings()) {
			if (e.getActionCommand() == ContactPaneActionCommand.HIDE_GROUPS) {
				model.getSettings().setGroupHidden(hideGroupCheckBox.isSelected());
				contactTable.filter(filterField.getText(), 
						hideGroupCheckBox.isSelected(), hideOfflineCheckBox.isSelected());
			}
			else if (e.getActionCommand() == ContactPaneActionCommand.HIDE_OFFLINE) {
				model.getSettings().setOfflineHidden(hideOfflineCheckBox.isSelected());
				contactTable.filter(filterField.getText(), 
						hideGroupCheckBox.isSelected(), hideOfflineCheckBox.isSelected());
			}
			else if (e.getActionCommand() == ContactPaneActionCommand.NEW_TALK_POPUP) {
				List lContactsToTalkWith = new ArrayList();
				SlimContact[] lSelectedContacts = contactTable.getSelectedContacts();
				for (int j = 0 ; j < lSelectedContacts.length; j++) {
					lContactsToTalkWith.add(lSelectedContacts[j]);
				}
				NewTalkFrame lFrame = new NewTalkFrame((Frame)getRootPane().getParent(), 
						model, lContactsToTalkWith, null);
				lFrame.pack();
				lFrame.setLocationRelativeTo(getRootPane().getParent());
				lFrame.setVisible(true);
			}
			else if (e.getActionCommand() == ContactPaneActionCommand.INVITE_POPUP) {
				SlimContact[] lSelectedContacts = contactTable.getSelectedContacts();
				if (lSelectedContacts.length >= 1) {
					List cl = new ArrayList();
					for (int j = 0 ; j < lSelectedContacts.length; j++) {
						SlimContact sc  = lSelectedContacts[j];
						if (sc.isGroup()) {
							cl.addAll(((SlimGroupContact)sc).getOnlineMembers());
						}
						else {
							if (sc.getAvailability() == SlimAvailabilityEnum.ONLINE) {
								cl.add(sc);
							}
						}
					}
					SlimTalk st = talkDisplay.getDisplayedTalk();
					if (st != null) {
						try {
							st.addPeople(cl);
						}
						catch (SlimException se) {
							JOptionPane.showMessageDialog(getRootPane().getParent(),
								    Externalizer.getString("LANSLIM.25"), //$NON-NLS-1$
								    Externalizer.getString("LANSLIM.22"), //$NON-NLS-1$
								    JOptionPane.ERROR_MESSAGE);
						}
					}
					else {
						JOptionPane.showMessageDialog(getRootPane().getParent(),
							    Externalizer.getString("LANSLIM.23"), //$NON-NLS-1$
							    Externalizer.getString("LANSLIM.28"), //$NON-NLS-1$
							    JOptionPane.WARNING_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(getRootPane().getParent(),
					    Externalizer.getString("LANSLIM.17"), //$NON-NLS-1$
					    Externalizer.getString("LANSLIM.28"), //$NON-NLS-1$
					    JOptionPane.WARNING_MESSAGE);
				}
			}
			else if (e.getActionCommand() == ContactPaneActionCommand.USER_ADD) {
				NewUserFrame lFrame = new NewUserFrame((Frame)getRootPane().getParent(), 
						model, null, false);
				lFrame.pack();
				lFrame.setLocationRelativeTo(getRootPane().getParent());
				lFrame.setVisible(true);
			}
			else if (e.getActionCommand() == ContactPaneActionCommand.GROUP_ADD) {
				NewGroupFrame lFrame = new NewGroupFrame((Frame)getRootPane().getParent(), 
						model, null);
				lFrame.pack();
				lFrame.setLocationRelativeTo(getRootPane().getParent());
				lFrame.setVisible(true);
			}
			else if (e.getActionCommand() == ContactPaneActionCommand.ALL_REFRESH) {
				model.getContacts().refresh();
			}
			else if (e.getActionCommand() == ContactPaneActionCommand.USER_EDIT) {
				SlimContact[] lSelectedContacts = contactTable.getSelectedContacts();
				if (lSelectedContacts.length == 1) {
					SlimContact sc = lSelectedContacts[0];
					if (sc.isGroup()) {
						NewGroupFrame lFrame = new NewGroupFrame((Frame)getRootPane().getParent(), 
								model, (SlimGroupContact)sc);
						lFrame.pack();
						lFrame.setLocationRelativeTo(getRootPane().getParent());
						lFrame.setVisible(true);
						
					}
					else {
						if (sc.getAvailability().equals(SlimAvailabilityEnum.OFFLINE)) {
							NewUserFrame lFrame = new NewUserFrame((Frame)getRootPane().getParent(), 
									model, (SlimUserContact)sc, false);
							lFrame.pack();
							lFrame.setLocationRelativeTo(getRootPane().getParent());
							lFrame.setVisible(true);
						}
						else {
							NewUserFrame lFrame = new NewUserFrame((Frame)getRootPane().getParent(), 
									model, (SlimUserContact)sc, true);
							lFrame.pack();
							lFrame.setLocationRelativeTo(getRootPane().getParent());
							lFrame.setVisible(true);
						}
					}
				}
				else {
					String[] lSelectedCategories = contactTable.getSelectedCategories();
					if (lSelectedCategories.length == 1) {
						NewCategoryFrame lFrame = new NewCategoryFrame((Frame)getRootPane().getParent(), 
								model, lSelectedCategories[0]);
						lFrame.pack();
						lFrame.setLocationRelativeTo(getRootPane().getParent());
						lFrame.setVisible(true);
					}
					else {
						JOptionPane.showMessageDialog(getRootPane().getParent(),
						    Externalizer.getString("LANSLIM.86"), //$NON-NLS-1$
						    Externalizer.getString("LANSLIM.28"), //$NON-NLS-1$
						    JOptionPane.WARNING_MESSAGE);
					}
				}
			}
			else if (e.getActionCommand() == ContactPaneActionCommand.USER_REFRESH_POPUP) {
				SlimContact[] lSelectedContacts = contactTable.getSelectedContacts();
				if (lSelectedContacts.length == 1) {
					model.getContacts().refresh(lSelectedContacts[0]);
				}
				else {
					JOptionPane.showMessageDialog(getRootPane().getParent(),
						    Externalizer.getString("LANSLIM.31"), //$NON-NLS-1$
						    Externalizer.getString("LANSLIM.28"), //$NON-NLS-1$
						    JOptionPane.WARNING_MESSAGE);
				}
			}
			else if (e.getActionCommand() == ContactPaneActionCommand.USER_DEL) {
				SlimContact[] lSelectedContacts = contactTable.getSelectedContacts();
				String lToDel = StringConstants.EMPTY;
				if (lSelectedContacts.length < 1) {
					String[] lSelectedCategories = contactTable.getSelectedCategories();
					if (lSelectedCategories.length < 1) {
						JOptionPane.showMessageDialog(getRootPane().getParent(),
							    Externalizer.getString("LANSLIM.87"), //$NON-NLS-1$
							    Externalizer.getString("LANSLIM.28"), //$NON-NLS-1$
							    JOptionPane.WARNING_MESSAGE);
					}
					else {
						for (int j = 0 ; j < lSelectedCategories.length; j++) {
							lToDel = lToDel + StringConstants.NEW_LINE + lSelectedCategories[j];
						}
						int a = JOptionPane.showConfirmDialog(getRootPane().getParent(),
							    Externalizer.getString("LANSLIM.88", lToDel), //$NON-NLS-1$
							    Externalizer.getString("LANSLIM.89"), //$NON-NLS-1$
							    JOptionPane.YES_NO_OPTION,
							    JOptionPane.WARNING_MESSAGE);
						if (a == JOptionPane.YES_OPTION) {
							for (int j = 0 ; j < lSelectedCategories.length; j++) {
								if (lSelectedCategories[j].equals(SlimContactList.CATEGORY_GROUP) 
										|| lSelectedCategories[j].equals(SlimContactList.CATEGORY_UNDEFINED)) {
									JOptionPane.showMessageDialog(getRootPane().getParent(),
										    Externalizer.getString("LANSLIM.185", //$NON-NLS-1$
										    		SlimContactList.CATEGORY_GROUP, SlimContactList.CATEGORY_UNDEFINED),
										    Externalizer.getString("LANSLIM.18"), //$NON-NLS-1$
										    JOptionPane.WARNING_MESSAGE);
								}
								else {
									model.getContacts().removeCategory(lSelectedCategories[j]);
								}
							}
		                }
					}
				}
				else {
					
					for (int j = 0 ; j < lSelectedContacts.length; j++) {
						lToDel = lToDel + StringConstants.NEW_LINE + lSelectedContacts[j].getName();
					}
					int a = JOptionPane.showConfirmDialog(getRootPane().getParent(),
						    Externalizer.getString("LANSLIM.90", lToDel), //$NON-NLS-1$
						    Externalizer.getString("LANSLIM.89"), //$NON-NLS-1$
						    JOptionPane.YES_NO_OPTION,
						    JOptionPane.WARNING_MESSAGE);
					if (a == JOptionPane.YES_OPTION) {
						for (int j = 0 ; j < lSelectedContacts.length; j++) {
							model.getContacts().removeContactByName(lSelectedContacts[j].getName());
						}
	                }
				}
			}
			else if (e.getActionCommand() == ContactPaneActionCommand.CATEGORY_NEW) {
				NewCategoryFrame lFrame = new NewCategoryFrame((Frame)getRootPane().getParent(), 
						model, null);
				lFrame.pack();
				lFrame.setLocationRelativeTo(getRootPane().getParent());
				lFrame.setVisible(true);
			}
		}
		else {
			JOptionPane.showMessageDialog(getRootPane().getParent(),
				    Externalizer.getString("LANSLIM.50"), //$NON-NLS-1$
				    Externalizer.getString("LANSLIM.28"), //$NON-NLS-1$
				    JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public class ContactPaneActionCommand {

		public static final String USER_DEL = "userDel"; //$NON-NLS-1$
		
		public static final String USER_ADD = "userAdd"; //$NON-NLS-1$
		
		public static final String USER_EDIT = "userEdit"; //$NON-NLS-1$
		
		public static final String GROUP_ADD = "groupAdd"; //$NON-NLS-1$

		public static final String NEW_TALK_POPUP = "newTalkPopup"; //$NON-NLS-1$

		public static final String INVITE_POPUP = "invitePopup"; //$NON-NLS-1$
		
		public static final String USER_REFRESH_POPUP = "userRefresh"; //$NON-NLS-1$

		public static final String CATEGORY_NEW = "categoryNew"; //$NON-NLS-1$

		public static final String ALL_REFRESH = "allRefresh"; //$NON-NLS-1$

		public static final String HIDE_GROUPS = "hideGroups"; //$NON-NLS-1$

		public static final String HIDE_OFFLINE = "hideOffline"; //$NON-NLS-1$
	}
	
}
