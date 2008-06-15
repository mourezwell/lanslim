package com.oz.lanslim.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.ContactViewListener;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimGroupContact;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimTalk;
import com.oz.lanslim.model.SlimTalkListener;
import com.oz.lanslim.model.SlimUserContact;

public class MainPane extends JPanel implements ActionListener, ChangeListener, 
	ContainerListener, SlimTalkListener, KeyListener, ContactViewListener {
	
	private SlimModel model;
	
	private JToolBar mainBar;
	private JButton newTalkButton;
	private JButton settingsButton;
	private JButton aboutButton;
	private JButton exitButton;
	private JButton minimizeButton;
	private JSplitPane mainSplitPane;
	
	private JTabbedPane talkTabPanes;
	private JPopupMenu talkTabPanesPopupMenu;
	private JMenuItem renameTalkMenuItem;
	private JMenuItem exportTalkMenuItem;
	
	private JSplitPane mainLeftPane;
	private JPanel peopleInPane;
	private JToolBar peopleInBar;
	private JLabel peopleInLabel;
	private JButton peopleInviteButton;
	private JScrollPane peopleInAreaPane;
	private JTextArea peopleInArea;

	private JPanel contactPane;
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
	
	
	public MainPane(SlimModel pModel) {
		super();
		model = pModel;
		model.getTalks().registerListener(this);
		model.getSettings().registerContcatViewListener(this);
		init();
	}
	
	private void init() {
		
		BorderLayout lLayout = new BorderLayout();
		this.setLayout(lLayout);
		this.setPreferredSize(new java.awt.Dimension(700, 400));
		{
			mainBar = new JToolBar();
			mainBar.setFloatable(false);
			mainBar.setRollover(true);
			this.add(mainBar, BorderLayout.NORTH);
			{
				settingsButton = new JButton();
				settingsButton.setIcon(new SlimIcon("process.png"));
				settingsButton.setText("Settings");
				settingsButton.setMnemonic(KeyEvent.VK_S);
				settingsButton.setActionCommand(MainPaneActionCommand.SETTINGS);
				settingsButton.addActionListener(this);
				settingsButton.setToolTipText("Set settings");
				mainBar.add(settingsButton);

				newTalkButton = new JButton();
				newTalkButton.setIcon(new SlimIcon("comments.png"));
				newTalkButton.setText("New Talk");
				settingsButton.setMnemonic(KeyEvent.VK_N);
				newTalkButton.setActionCommand(MainPaneActionCommand.NEW_TALK);
				newTalkButton.addActionListener(this);
				newTalkButton.setToolTipText("Start new Talk");
				mainBar.add(newTalkButton);
				
				mainBar.add(Box.createHorizontalGlue());

				aboutButton = new JButton();
				aboutButton.setIcon(new SlimIcon("help.png"));
				aboutButton.setText("About");
				aboutButton.setMnemonic(KeyEvent.VK_F1);
				aboutButton.setActionCommand(MainPaneActionCommand.ABOUT);
				aboutButton.addActionListener(this);
				aboutButton.setToolTipText("About Dialog");
				mainBar.add(aboutButton);
				
				minimizeButton = new JButton();
				minimizeButton.setIcon(new SlimIcon("down.png"));
				minimizeButton.setText("Minimize");
				minimizeButton.setActionCommand(MainPaneActionCommand.MINIMIZE);
				minimizeButton.addActionListener(this);
				minimizeButton.setToolTipText("Minimize Application");
				mainBar.add(minimizeButton);
				
				exitButton = new JButton();
				exitButton.setIcon(new SlimIcon("exit.png"));
				exitButton.setText("Exit");
				exitButton.setMnemonic(KeyEvent.VK_ESCAPE);
				exitButton.setActionCommand(MainPaneActionCommand.EXIT);
				exitButton.addActionListener(this);
				exitButton.setToolTipText("Exit Application");
				mainBar.add(exitButton);
			}
		}
		{
			mainSplitPane = new JSplitPane();
			mainSplitPane.setDividerSize(5);
			this.add(mainSplitPane);
			{
				talkTabPanes = new JTabbedPane();
				talkTabPanes.setUI(new TabbedPaneCloseButtonUI());
				talkTabPanes.setPreferredSize(new java.awt.Dimension(400, 400));
				talkTabPanes.addContainerListener(this);
				talkTabPanes.addChangeListener(this);
				mainSplitPane.add(talkTabPanes, JSplitPane.RIGHT);

				//Create the popup menu.
				talkTabPanesPopupMenu = new JPopupMenu();
		        {
		        	renameTalkMenuItem = new JMenuItem("Edit Talk", new SlimIcon("note_edit.png"));
		        	renameTalkMenuItem.addActionListener(this);
		        	renameTalkMenuItem.setActionCommand(MainPaneActionCommand.TALK_EDIT);
		        	talkTabPanesPopupMenu.add(renameTalkMenuItem);
		        	
					exportTalkMenuItem = new JMenuItem("Export Talk To File", new SlimIcon("page_next.png"));
					exportTalkMenuItem.setActionCommand(MainPaneActionCommand.EXPORT_TALK);
					exportTalkMenuItem.addActionListener(this);
					talkTabPanesPopupMenu.add(exportTalkMenuItem);
		        }
		        talkTabPanes.addMouseListener(new TabPaneMouseListener(talkTabPanesPopupMenu));
			}
			{
				mainLeftPane = new JSplitPane();
				mainLeftPane.setPreferredSize(new java.awt.Dimension(200, 400));
				mainLeftPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
				mainSplitPane.add(mainLeftPane, JSplitPane.LEFT);
				{
					peopleInPane = new JPanel();
					mainLeftPane.add(peopleInPane, JSplitPane.TOP);
					BorderLayout interlocutorsPaneLayout = new BorderLayout();
					peopleInPane.setLayout(interlocutorsPaneLayout);
					peopleInPane.setPreferredSize(new java.awt.Dimension(200, 100));
					{
						peopleInBar = new JToolBar();
						peopleInBar.setFloatable(false);
						peopleInBar.setRollover(true);
						peopleInPane.add(peopleInBar, BorderLayout.NORTH);
						{
							peopleInLabel = new JLabel();
							peopleInLabel.setText("Who's In ?");
							peopleInBar.add(peopleInLabel);
						}
						{
							peopleInviteButton = new JButton();
							peopleInviteButton.setIcon(new SlimIcon("mail_add.png"));
							peopleInviteButton.setActionCommand(MainPaneActionCommand.INVITE);
							peopleInviteButton.setToolTipText("Invite contact to current talk");
							peopleInviteButton.addActionListener(this);
							peopleInBar.addSeparator();
							peopleInBar.add(peopleInviteButton);
						}
					}
					{
						peopleInAreaPane = new JScrollPane();
						peopleInPane.add(peopleInAreaPane, BorderLayout.CENTER);
						{
							peopleInArea = new JTextArea();
							peopleInArea.setEnabled(false);
							peopleInArea.setTransferHandler(new ContactTransferHandler(talkTabPanes));
							peopleInAreaPane.setViewportView(peopleInArea);
						}
					}
				}
				{
					contactPane = new JPanel();
					contactPane.setLayout(new BorderLayout());
					contactPane.setPreferredSize(new java.awt.Dimension(200, 100));
					mainLeftPane.add(contactPane, JSplitPane.BOTTOM);
					{
						contactBar = new JToolBar();
						contactBar.setFloatable(false);
						contactBar.setRollover(true);
						contactPane.add(contactBar, BorderLayout.NORTH);
						{
							newContactButton = new JButton();
							newContactButton.setIcon(new SlimIcon("user_add.png"));
							newContactButton.setActionCommand(MainPaneActionCommand.USER_ADD);
							newContactButton.addActionListener(this);
							newContactButton.setToolTipText("Add User");
							contactBar.add(newContactButton);
						}
						{
							newGroupButton = new JButton();
							newGroupButton.setIcon(new SlimIcon("users.png"));
							newGroupButton.setActionCommand(MainPaneActionCommand.GROUP_ADD);
							newGroupButton.addActionListener(this);
							newGroupButton.setToolTipText("Add Group");
							contactBar.add(newGroupButton);
						}
						{
							deleteButton = new JButton();
							deleteButton.setIcon(new SlimIcon("user_remove.png"));
							deleteButton.setActionCommand(MainPaneActionCommand.USER_DEL);
							deleteButton.addActionListener(this);
							deleteButton.setToolTipText("Delete Selected Contact(s)");
							contactBar.add(deleteButton);
						}
						{
							editButton = new JButton();
							editButton.setIcon(new SlimIcon("note_edit.png"));
							editButton.setActionCommand(MainPaneActionCommand.USER_EDIT);
							editButton.addActionListener(this);
							editButton.setToolTipText("Edit Selected Contact");
							contactBar.add(editButton);
						}
						{
							refreshButton = new JButton();
							refreshButton.setIcon(new SlimIcon("refresh.png"));
							refreshButton.setActionCommand(MainPaneActionCommand.ALL_REFRESH);
							refreshButton.addActionListener(this);
							refreshButton.setToolTipText("Resfresh All Availability");
							contactBar.add(refreshButton);
						}
						{
							newCatButton = new JButton();
							newCatButton.setIcon(new SlimIcon("folder_add.png"));
							newCatButton.setActionCommand(MainPaneActionCommand.CATEGORY_NEW);
							newCatButton.addActionListener(this);
							newCatButton.setToolTipText("Add Category (Contact Folder)");
							contactBar.add(newCatButton);
						}

					}
					{
						contactTablePane = new JScrollPane();
						contactPane.add(contactTablePane, BorderLayout.CENTER);

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
						contactPane.add(filterPane, BorderLayout.SOUTH);
						{
							hideGroupCheckBox = new JCheckBox("Hide Groups");
							hideGroupCheckBox.addActionListener(this);
							hideGroupCheckBox.setActionCommand(MainPaneActionCommand.HIDE_GROUPS);
							hideGroupCheckBox.setSelected(model.getSettings().isGroupHidden());
							filterPane.add(hideGroupCheckBox);
						}
						{
							hideOfflineCheckBox = new JCheckBox("Hide Offline");
							hideOfflineCheckBox.addActionListener(this);
							hideOfflineCheckBox.setActionCommand(MainPaneActionCommand.HIDE_OFFLINE);
							hideOfflineCheckBox.setSelected(model.getSettings().isOfflineHidden());
							filterPane.add(hideOfflineCheckBox);
						}
						{
							filterLabel = new JLabel("Filter Name", new SlimIcon("search.png"), SwingConstants.LEADING);
							filterPane.add(filterLabel);
							filterField = new JTextField();
							filterField.setColumns(15);
							filterField.addKeyListener(this);
							filterPane.add(filterField);
						}
					}
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand() == MainPaneActionCommand.SETTINGS) {
			if (model.getTalks().size() == 0) {
				SettingsFrame lFrame = new SettingsFrame((Frame)this.getRootPane().getParent(), model);
				lFrame.pack();
				lFrame.setLocationRelativeTo(this);
				lFrame.setVisible(true);
			}
			else {
				JOptionPane.showMessageDialog(this,
					    "All talks must be closed",
					    "Invalid Action",
					    JOptionPane.WARNING_MESSAGE);
			}
		}
		else if (e.getActionCommand() == MainPaneActionCommand.ABOUT) {
			AboutDialog.showDialog(this);
		}
		else if (e.getActionCommand() == MainPaneActionCommand.MINIMIZE) {
			if (model.getSettings().isTrayEnable() && model.getSettings().isCloseAsTray()) {
				this.getRootPane().getParent().setVisible(false);
			}
			else {
				((Frame)this.getRootPane().getParent()).setExtendedState(Frame.ICONIFIED);
			}
			
		}
		else if (e.getActionCommand() == MainPaneActionCommand.EXIT) {
			System.exit(0);
		}
		else if (model.getSettings().areValidSettings()) {
			if (e.getActionCommand() == MainPaneActionCommand.NEW_TALK) {
				NewTalkFrame lFrame = new NewTalkFrame(
						(Frame)this.getRootPane().getParent(), model, null, null);
				lFrame.pack();
				lFrame.setLocationRelativeTo(this);
				lFrame.setVisible(true);
			}
			else if (e.getActionCommand() == MainPaneActionCommand.TALK_EDIT) {
				SlimTalk st = ((TalkPane)talkTabPanes.getSelectedComponent()).getTalk();
				NewTalkFrame lFrame = new NewTalkFrame((Frame)this.getRootPane().getParent(), 
						model, null, st);
				lFrame.pack();
				lFrame.setLocationRelativeTo(this);
				lFrame.setVisible(true);
			}
			else if (e.getActionCommand() == MainPaneActionCommand.NEW_TALK_POPUP) {
				List lContactsToTalkWith = new ArrayList();
				SlimContact[] lSelectedContacts = contactTable.getSelectedContacts();
				for (int j = 0 ; j < lSelectedContacts.length; j++) {
					lContactsToTalkWith.add(lSelectedContacts[j]);
				}
				NewTalkFrame lFrame = new NewTalkFrame((Frame)this.getRootPane().getParent(), 
						model, lContactsToTalkWith, null);
				lFrame.pack();
				lFrame.setLocationRelativeTo(this);
				lFrame.setVisible(true);
			}
			else if (e.getActionCommand() == MainPaneActionCommand.INVITE) {
				if (talkTabPanes.getComponents().length > 0) {
					SlimTalk st = ((TalkPane)talkTabPanes.getSelectedComponent()).getTalk();
					NewTalkFrame lFrame = new NewTalkFrame((Frame)this.getRootPane().getParent(), 
							model, null, st);
					lFrame.pack();
					lFrame.setLocationRelativeTo(this);
					lFrame.setVisible(true);
				}
				else {
					JOptionPane.showMessageDialog(this,
						    "At least one talk must be started",
						    "Invalid Action",
						    JOptionPane.WARNING_MESSAGE);
				}
			}
			else if (e.getActionCommand() == MainPaneActionCommand.EXPORT_TALK) {
				TalkPane tp = (TalkPane)talkTabPanes.getSelectedComponent();
				FileDialog fileChooser = new FileDialog((Frame)this.getRootPane().getParent(), "Export Talk", FileDialog.SAVE);
				fileChooser.show();
	            String lFileName = fileChooser.getFile();
	            if (lFileName != null) {
	            	File file = new File(fileChooser.getDirectory() + File.separator + lFileName);
	                try {
	                	tp.export(file);
	                }
	                catch (IOException ioe) {
	    				JOptionPane.showMessageDialog(this,
						    "Unable to export talk due to : " + ioe + ":" + ioe.getMessage(),
						    "Action Error",
						    JOptionPane.ERROR_MESSAGE);
	                }
	            }
			}
			else if (e.getActionCommand() == MainPaneActionCommand.INVITE_POPUP) {
				if (talkTabPanes.getComponents().length > 0) {
					SlimTalk st = ((TalkPane)talkTabPanes.getSelectedComponent()).getTalk();
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
						try {
							st.addPeople(cl);
						}
						catch (SlimException se) {
							JOptionPane.showMessageDialog(this,
								    "Unable to send Invitation message or notification",
								    "Network Error",
								    JOptionPane.ERROR_MESSAGE);
						}
					}
					else {
						JOptionPane.showMessageDialog(this,
						    "At least one contact must be selected",
						    "Invalid Action",
						    JOptionPane.WARNING_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(this,
						    "At least one talk must be started",
						    "Invalid Action",
						    JOptionPane.WARNING_MESSAGE);
				}
			}
			else if (e.getActionCommand() == MainPaneActionCommand.USER_ADD) {
				NewUserFrame lFrame = new NewUserFrame((Frame)this.getRootPane().getParent(), 
						model, null);
				lFrame.pack();
				lFrame.setLocationRelativeTo(this);
				lFrame.setVisible(true);
			}
			else if (e.getActionCommand() == MainPaneActionCommand.GROUP_ADD) {
				NewGroupFrame lFrame = new NewGroupFrame((Frame)this.getRootPane().getParent(), 
						model, null);
				lFrame.pack();
				lFrame.setLocationRelativeTo(this);
				lFrame.setVisible(true);
			}
			else if (e.getActionCommand() == MainPaneActionCommand.ALL_REFRESH) {
				model.getContacts().refresh();
			}
			else if (e.getActionCommand() == MainPaneActionCommand.USER_EDIT) {
				SlimContact[] lSelectedContacts = contactTable.getSelectedContacts();
				if (lSelectedContacts.length == 1) {
					SlimContact sc = lSelectedContacts[0];
					if (sc.isGroup()) {
						NewGroupFrame lFrame = new NewGroupFrame((Frame)this.getRootPane().getParent(), 
								model, (SlimGroupContact)sc);
						lFrame.pack();
						lFrame.setLocationRelativeTo(this);
						lFrame.setVisible(true);
						
					}
					else {
						NewUserFrame lFrame = new NewUserFrame((Frame)this.getRootPane().getParent(), 
								model, (SlimUserContact)sc);
						lFrame.pack();
						lFrame.setLocationRelativeTo(this);
						lFrame.setVisible(true);
					}
				}
				else {
					String[] lSelectedCategories = contactTable.getSelectedCategories();
					if (lSelectedCategories.length == 1) {
						NewCategoryFrame lFrame = new NewCategoryFrame((Frame)this.getRootPane().getParent(), 
								model, lSelectedCategories[0]);
						lFrame.pack();
						lFrame.setLocationRelativeTo(this);
						lFrame.setVisible(true);
					}
					else {
						JOptionPane.showMessageDialog(this,
						    "One and only one user/group/category must be selected please select it first",
						    "Invalid Action",
						    JOptionPane.WARNING_MESSAGE);
					}
				}
			}
			else if (e.getActionCommand() == MainPaneActionCommand.USER_REFRESH_POPUP) {
				SlimContact[] lSelectedContacts = contactTable.getSelectedContacts();
				if (lSelectedContacts.length == 1) {
					model.getContacts().refresh(lSelectedContacts[0]);
				}
				else {
					JOptionPane.showMessageDialog(this,
						    "One and only one contact must be selected please select it first",
						    "Invalid Action",
						    JOptionPane.WARNING_MESSAGE);
				}
			}
			else if (e.getActionCommand() == MainPaneActionCommand.USER_DEL) {
				SlimContact[] lSelectedContacts = contactTable.getSelectedContacts();
				String lToDel = "";
				if (lSelectedContacts.length < 1) {
					String[] lSelectedCategories = contactTable.getSelectedCategories();
					if (lSelectedCategories.length < 1) {
						JOptionPane.showMessageDialog(this,
							    "At least one Group/User/Category must be selected",
							    "Invalid Action",
							    JOptionPane.WARNING_MESSAGE);
					}
					else {
						for (int j = 0 ; j < lSelectedCategories.length; j++) {
							lToDel = lToDel + "\n-" + lSelectedCategories[j];
						}
						int a = JOptionPane.showConfirmDialog(this,
							    "Are you sure you want to delete categorie(s) listed here below" + lToDel,
							    "Delete confirmation",
							    JOptionPane.YES_NO_OPTION,
							    JOptionPane.WARNING_MESSAGE);
						if (a == JOptionPane.YES_OPTION) {
							for (int j = 0 ; j < lSelectedCategories.length; j++) {
								model.getContacts().removeCategory(lSelectedCategories[j]);
							}
		                }
						
					}

				}
				else {
					
					for (int j = 0 ; j < lSelectedContacts.length; j++) {
						lToDel = lToDel + "\n-" + lSelectedContacts[j].getName();
					}
					int a = JOptionPane.showConfirmDialog(this,
						    "Are you sure you want to delete contact(s) listed here below" + lToDel,
						    "Delete confirmation",
						    JOptionPane.YES_NO_OPTION,
						    JOptionPane.WARNING_MESSAGE);
					if (a == JOptionPane.YES_OPTION) {
						for (int j = 0 ; j < lSelectedContacts.length; j++) {
							model.getContacts().removeContactByName(lSelectedContacts[j].getName());
						}
	                }
				}
			}
			else if (e.getActionCommand() == MainPaneActionCommand.HIDE_GROUPS) {
				model.getSettings().setGroupHidden(hideGroupCheckBox.isSelected());
				contactTable.filter(filterField.getText(), 
						hideGroupCheckBox.isSelected(), hideOfflineCheckBox.isSelected());
			}
			else if (e.getActionCommand() == MainPaneActionCommand.HIDE_OFFLINE) {
				model.getSettings().setOfflineHidden(hideOfflineCheckBox.isSelected());
				contactTable.filter(filterField.getText(), 
						hideGroupCheckBox.isSelected(), hideOfflineCheckBox.isSelected());
			}
			else if (e.getActionCommand() == MainPaneActionCommand.CATEGORY_NEW) {
				Frame mainFrame = JOptionPane.getFrameForComponent(this);
				NewCategoryFrame lFrame = new NewCategoryFrame(mainFrame, model, null);
				lFrame.pack();
				lFrame.setLocationRelativeTo(this);
				lFrame.setVisible(true);
			}
		}
		else {
			JOptionPane.showMessageDialog(this,
			    "All actions are disabled until you set properly your settings",
			    "Invalid Action",
			    JOptionPane.WARNING_MESSAGE);
		}
	}

	public void componentAdded(ContainerEvent e) {
		// Model allready uptodate
	}
	
	public void componentRemoved(ContainerEvent e) {
		try {
			model.getTalks().closeTalk(((TalkPane)e.getChild()).getTalk());
		}
		catch (SlimException se) {
			JOptionPane.showMessageDialog(this,
			    "Some users may not have received the notifiaction that you have left the talk",
			    "Network Error",
			    JOptionPane.ERROR_MESSAGE);
		}
	}

	public void stateChanged(ChangeEvent e) {
		if (talkTabPanes.getSelectedComponent() != null) {
			peopleInArea.setText(
					((TalkPane)talkTabPanes.getSelectedComponent()).getTalk().getPeopleIn());
			if (((Component)e.getSource()).isValid()) {
					talkTabPanes.setIconAt(talkTabPanes.getSelectedIndex(), null);
			}
		}
		else {
			peopleInArea.setText("");
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
	
	public void notifyNewTalk(SlimTalk pTalk) {
		TalkPane newTalkPane = new TalkPane(this, pTalk);
		talkTabPanes.addTab(pTalk.getTitle(), null, newTalkPane, null);
		talkTabPanes.setSelectedComponent(newTalkPane);
		//peopleInArea updated via change event
		talkTabPanes.setToolTipTextAt(talkTabPanes.getSelectedIndex(), pTalk.getPeopleIn().replaceAll("\n", ", "));
	}
	
	public void notifyTextTalkUpdate(SlimTalk pTalk) {
		if (((TalkPane)talkTabPanes.getSelectedComponent()).getTalk() !=  pTalk) {
			for (int i = 0; i < talkTabPanes.getComponents().length; i++) {
				TalkPane tp = (TalkPane)talkTabPanes.getComponents()[i];
				if (tp.getTalk() == pTalk) {
					talkTabPanes.setIconAt(talkTabPanes.indexOfComponent(tp), new SlimIcon("favorite.png"));
					talkTabPanes.setToolTipTextAt(talkTabPanes.indexOfComponent(tp), pTalk.getPeopleIn().replaceAll("\n", ", "));
				}
			}
		}
		if (((TalkPane)talkTabPanes.getSelectedComponent()).getTalk() ==  pTalk) {
			peopleInArea.setText(pTalk.getPeopleIn());
			talkTabPanes.setToolTipTextAt(talkTabPanes.getSelectedIndex(), pTalk.getPeopleIn().replaceAll("\n", ", "));
			talkTabPanes.setTitleAt(talkTabPanes.getSelectedIndex(), pTalk.getTitle());
		}
	}
	
	public void notifyNewTalkError(String pMessage) {
		JOptionPane.showMessageDialog(this,
				pMessage,
			    "New Talk Error",
			    JOptionPane.ERROR_MESSAGE);
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

	protected class MainPaneActionCommand {

		public static final String ABOUT = "about";
		
		public static final String SETTINGS = "settings";

		public static final String NEW_TALK = "newTalk";

		public static final String NEW_TALK_POPUP = "newTalkPopup";
		
		public static final String INVITE = "invite";
		
		public static final String INVITE_POPUP = "invitePopup";
		
		public static final String USER_DEL = "userDel";
		
		public static final String USER_ADD = "userAdd";
		
		public static final String USER_EDIT = "userEdit";
		
		public static final String GROUP_ADD = "groupAdd";
		
		public static final String MINIMIZE = "minimize";
		
		public static final String EXIT = "exit";
		
		public static final String ALL_REFRESH = "allRefresh";
		
		public static final String TALK_EDIT = "editTalk";

		public static final String EXPORT_TALK = "exportTalk";

		public static final String HIDE_GROUPS = "hideGroups";

		public static final String HIDE_OFFLINE = "hideOffline";

		public static final String USER_REFRESH_POPUP = "userRefresh";

		public static final String CATEGORY_NEW = "categoryNew";

	}

}
