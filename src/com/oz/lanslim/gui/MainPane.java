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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
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
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.JTableHeader;

import com.oz.lanslim.LANSLIMMain;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimGroupContact;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimTalk;
import com.oz.lanslim.model.SlimTalkListener;
import com.oz.lanslim.model.SlimUserContact;

public class MainPane extends JPanel 
	implements ActionListener, ChangeListener, ContainerListener, SlimTalkListener, KeyListener {
	
	private SlimModel model;
	
	private JToolBar mainBar;
	private JButton newTalkButton;
	private JButton settingsButton;
	private JButton aboutButton;
	private JButton exitButton;
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
	private JScrollPane contactTablePane;
	private JTable contactTable;
	private ContactTableModel contactTableModel;
	private JPanel filterPane;
	private JLabel filterLabel;
	private JTextField filterField;
	private JCheckBox hideGroupCheckBox;
	private JCheckBox hideOfflineCheckBox;
	
	private JPopupMenu popupMenu;
	private JMenuItem userDelMenuItem;
	private JMenuItem userEditMenuItem;
	private JMenuItem newTalkMenuItem;
	private JMenuItem inviteMenuItem;
	private JMenuItem refreshMenuItem;
	
	
	public MainPane(SlimModel pModel) {
		super();
		model = pModel;
		model.getTalks().registerListener(this);
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
						contactTablePane = new JScrollPane();
						contactPane.add(contactTablePane, BorderLayout.CENTER);
						{
							contactTable = new JTable() {
								String[] columnToolTips = new String[] { "Name", "Status" };
					            //Implement table header tool tips. 
					            protected JTableHeader createDefaultTableHeader() {
					                return new JTableHeader(columnModel) {
					                    public String getToolTipText(MouseEvent e) {
					                        java.awt.Point p = e.getPoint();
					                        int index = columnModel.getColumnIndexAtX(p.x);
					                        int realIndex = columnModel.getColumn(index).getModelIndex();
					                        return columnToolTips[realIndex];
					                    }
					                };
					            }
							};
							contactTable.setDefaultRenderer(SlimAvailabilityEnum.class, new AvailabilityRenderer());
							contactTableModel = new ContactTableModel(model.getContacts(), 
									model.getSettings().isGroupHidden(), model.getSettings().isOfflineHidden());
							TableSorter ts = new TableSorter(contactTableModel, contactTable.getTableHeader());
							ts.setColumnComparator(String.class, new Comparator() {
							        public int compare(Object o1, Object o2) {
							            return ((String) o1).compareToIgnoreCase((String)o2);
							        }
							    });
							contactTable.setModel(ts);
							contactTable.getColumnModel().getColumn(1).setPreferredWidth(20);
							contactTable.setTransferHandler(new ContactTransferHandler(model));
							contactTable.setDragEnabled(true);
							contactTablePane.setViewportView(contactTable);
						}
				        //Create the popup menu.
				        popupMenu = new JPopupMenu();
				        {
					        userEditMenuItem = new JMenuItem("Edit User/Group", new SlimIcon("note_edit.png"));
					        userEditMenuItem.addActionListener(this);
					        userEditMenuItem.setActionCommand(MainPaneActionCommand.USER_EDIT);
					        popupMenu.add(userEditMenuItem);
				        }
				        {
				        	userDelMenuItem = new JMenuItem("Remove User/Group", new SlimIcon("user_remove.png"));
				        	userDelMenuItem.addActionListener(this);
				        	userDelMenuItem.setActionCommand(MainPaneActionCommand.USER_DEL);
					        popupMenu.add(userDelMenuItem);
				        }
				        {
				        	inviteMenuItem = new JMenuItem("Invite to current talk", new SlimIcon("mail_add.png"));
				        	inviteMenuItem.setActionCommand(MainPaneActionCommand.INVITE_POPUP);
				        	inviteMenuItem.addActionListener(this);
					        popupMenu.add(inviteMenuItem);
				        }
				        {
					        newTalkMenuItem = new JMenuItem("Start new talk", new SlimIcon("comment.png"));
					        newTalkMenuItem.setActionCommand(MainPaneActionCommand.NEW_TALK_POPUP);
					        newTalkMenuItem.addActionListener(this);
					        popupMenu.add(newTalkMenuItem);
				        }
				        {
				        	refreshMenuItem = new JMenuItem("Refresh Availability", new SlimIcon("refresh.png"));
				        	refreshMenuItem.setActionCommand(MainPaneActionCommand.USER_REFRESH_POPUP);
				        	refreshMenuItem.addActionListener(this);
					        popupMenu.add(refreshMenuItem);
				        }
				        //Add listener to the table so the popup menu can come up.
				        MouseListener popupListener = new ContactTableMouseListener(popupMenu, contactTable, model);
				        contactTable.addMouseListener(popupListener);
					}
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
					}
					{
						filterPane = new JPanel();
						filterPane.setLayout(new GridLayout(2, 2));
						hideGroupCheckBox = new JCheckBox("Hide Groups");
						hideGroupCheckBox.addActionListener(this);
						hideGroupCheckBox.setActionCommand(MainPaneActionCommand.HIDE_GROUPS);
						hideGroupCheckBox.setSelected(model.getSettings().isGroupHidden());
						filterPane.add(hideGroupCheckBox);
						hideOfflineCheckBox = new JCheckBox("Hide Offline");
						hideOfflineCheckBox.addActionListener(this);
						hideOfflineCheckBox.setActionCommand(MainPaneActionCommand.HIDE_OFFLINE);
						hideOfflineCheckBox.setSelected(model.getSettings().isOfflineHidden());
						filterPane.add(hideOfflineCheckBox);
						filterLabel = new JLabel("Filter Name", new SlimIcon("search.png"), SwingConstants.LEADING);
						filterPane.add(filterLabel);
						filterField = new JTextField();
						filterField.setColumns(15);
						filterField.addKeyListener(this);
						filterPane.add(filterField);
						contactPane.add(filterPane, BorderLayout.SOUTH);

					}
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand() == MainPaneActionCommand.SETTINGS) {
			SettingsFrame lFrame = new SettingsFrame(model);
			lFrame.pack();
			lFrame.setLocationRelativeTo(this);
			lFrame.setVisible(true);
		}
		else if (e.getActionCommand() == MainPaneActionCommand.ABOUT) {
			JOptionPane.showMessageDialog(this,
					"Version " + LANSLIMMain.VERSION + " \n" +
				    "This little Tool has been made by Olivier Mourez\n" +
				    "If you have any improvements to propose or any issue to report\n" + 
				    "you can contact me at olivier.mourez@gmail.com",
				    "About LANSLIM (Local Area Network Server Less Instant Messaging)",
				    JOptionPane.INFORMATION_MESSAGE);
		}
		else if (e.getActionCommand() == MainPaneActionCommand.EXIT) {
			System.exit(0);
		}
		else if (model.getSettings().areValidSettings()) {
			if (e.getActionCommand() == MainPaneActionCommand.NEW_TALK) {
				NewTalkFrame lFrame = new NewTalkFrame(model, null, null);
				lFrame.pack();
				lFrame.setLocationRelativeTo(this);
				lFrame.setVisible(true);
			}
			else if (e.getActionCommand() == MainPaneActionCommand.TALK_EDIT) {
				SlimTalk st = ((TalkPane)talkTabPanes.getSelectedComponent()).getTalk();
				NewTalkFrame lFrame = new NewTalkFrame(model, null, st);
				lFrame.pack();
				lFrame.setLocationRelativeTo(this);
				lFrame.setVisible(true);
			}
			else if (e.getActionCommand() == MainPaneActionCommand.NEW_TALK_POPUP) {
				List lContactsToTalkWith = new ArrayList();
				int[] lSelectedContactIndex = contactTable.getSelectedRows();
				for (int j = 0 ; j < lSelectedContactIndex.length; j++) {
					int i = lSelectedContactIndex[j];
					lContactsToTalkWith.add(model.getContacts().getContactByName(
							(String)contactTable.getModel().getValueAt(i, 0)));
				}
				NewTalkFrame lFrame = new NewTalkFrame(model, lContactsToTalkWith, null);
				lFrame.pack();
				lFrame.setLocationRelativeTo(this);
				lFrame.setVisible(true);
			}
			else if (e.getActionCommand() == MainPaneActionCommand.INVITE) {
				if (talkTabPanes.getComponents().length > 0) {
					SlimTalk st = ((TalkPane)talkTabPanes.getSelectedComponent()).getTalk();
					NewTalkFrame lFrame = new NewTalkFrame(model, null, st);
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
					int[] lSelectedContactIndex = contactTable.getSelectedRows();
					if (lSelectedContactIndex.length >= 1) {
						List cl = new ArrayList();
						for (int j = 0 ; j < lSelectedContactIndex.length; j++) {
							int i = lSelectedContactIndex[j];
							SlimContact sc = model.getContacts().getContactByName(
									(String)contactTable.getModel().getValueAt(i, 0));
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
						    "At least one contact must be selected in the table",
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
				NewUserFrame lFrame = new NewUserFrame(model, null);
				lFrame.pack();
				lFrame.setLocationRelativeTo(this);
				lFrame.setVisible(true);
			}
			else if (e.getActionCommand() == MainPaneActionCommand.GROUP_ADD) {
				NewGroupFrame lFrame = new NewGroupFrame(model, null);
				lFrame.pack();
				lFrame.setLocationRelativeTo(this);
				lFrame.setVisible(true);
			}
			else if (e.getActionCommand() == MainPaneActionCommand.ALL_REFRESH) {
				model.getContacts().refresh();
			}
			else if (e.getActionCommand() == MainPaneActionCommand.USER_EDIT) {
				int[] lSelectedContactIndex = contactTable.getSelectedRows();
				if (lSelectedContactIndex.length == 1) {
					SlimContact sc = model.getContacts().getContactByName(
							(String)contactTable.getModel().getValueAt(lSelectedContactIndex[0], 0));
					if (sc.isGroup()) {
						NewGroupFrame lFrame = new NewGroupFrame(model, (SlimGroupContact)sc);
						lFrame.pack();
						lFrame.setLocationRelativeTo(this);
						lFrame.setVisible(true);
						
					}
					else {
						NewUserFrame lFrame = new NewUserFrame(model, (SlimUserContact)sc);
						lFrame.pack();
						lFrame.setLocationRelativeTo(this);
						lFrame.setVisible(true);
					}
				}
				else {
					JOptionPane.showMessageDialog(this,
						    "One and only one contact must be selected in the table please select it first",
						    "Invalid Action",
						    JOptionPane.WARNING_MESSAGE);
				}
			}
			else if (e.getActionCommand() == MainPaneActionCommand.USER_REFRESH_POPUP) {
				int[] lSelectedContactIndex = contactTable.getSelectedRows();
				if (lSelectedContactIndex.length == 1) {
					SlimContact sc = model.getContacts().getContactByName(
							(String)contactTable.getModel().getValueAt(lSelectedContactIndex[0], 0));
					model.getContacts().refresh(sc);
				}
				else {
					JOptionPane.showMessageDialog(this,
						    "One and only one contact must be selected in the table please select it first",
						    "Invalid Action",
						    JOptionPane.WARNING_MESSAGE);
				}
			}
			else if (e.getActionCommand() == MainPaneActionCommand.USER_DEL) {
				int[] lSelectedContactIndex = contactTable.getSelectedRows();
				if (lSelectedContactIndex.length < 1) {
					JOptionPane.showMessageDialog(this,
						    "At least one contact must be selected in the table",
						    "Invalid Action",
						    JOptionPane.WARNING_MESSAGE);
				}
				else {
					String lContactsToDel = "";
					for (int j = 0 ; j < lSelectedContactIndex.length; j++) {
						int i = lSelectedContactIndex[j];
						lContactsToDel = lContactsToDel + "\n-" + contactTable.getModel().getValueAt(i, 0);
					}
					int a = JOptionPane.showConfirmDialog(this,
						    "Are you sure you want to delete contact(s) listed here below" + lContactsToDel,
						    "Delete confirmation",
						    JOptionPane.YES_NO_OPTION,
						    JOptionPane.WARNING_MESSAGE);
					if (a == JOptionPane.YES_OPTION) {
						for (int j = 0 ; j < lSelectedContactIndex.length; j++) {
							int i = lSelectedContactIndex[j];
							model.getContacts().removeContactByName(
									(String)contactTable.getModel().getValueAt(i, 0));
						}
	                }
				}
			}
			else if (e.getActionCommand() == MainPaneActionCommand.HIDE_GROUPS) {
				model.getSettings().setGroupHidden(hideGroupCheckBox.isSelected());
				contactTableModel.filter(filterField.getText(), 
						hideGroupCheckBox.isSelected(), hideOfflineCheckBox.isSelected());
			}
			else if (e.getActionCommand() == MainPaneActionCommand.HIDE_OFFLINE) {
				model.getSettings().setOfflineHidden(hideOfflineCheckBox.isSelected());
				contactTableModel.filter(filterField.getText(), 
						hideGroupCheckBox.isSelected(), hideOfflineCheckBox.isSelected());
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
		contactTableModel.filter(filterField.getText(), 
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

	private class MainPaneActionCommand {

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
		
		public static final String EXIT = "exit";
		
		public static final String ALL_REFRESH = "allRefresh";
		
		public static final String TALK_EDIT = "editTalk";

		public static final String EXPORT_TALK = "exportTalk";

		public static final String HIDE_GROUPS = "hideGroups";

		public static final String HIDE_OFFLINE = "hideOffline";

		public static final String USER_REFRESH_POPUP = "userRefresh";

	}

}
