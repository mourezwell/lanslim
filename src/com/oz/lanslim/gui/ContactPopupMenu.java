package com.oz.lanslim.gui;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.oz.lanslim.gui.MainPane.MainPaneActionCommand;

public class ContactPopupMenu extends JPopupMenu {

	private JMenuItem userDelMenuItem;
	private JMenuItem userEditMenuItem;
	private JMenuItem newTalkMenuItem;
	private JMenuItem inviteMenuItem;
	private JMenuItem refreshMenuItem;

	public ContactPopupMenu(MainPane pPane) {
    
        userEditMenuItem = new JMenuItem("Edit User/Group/Category", new SlimIcon("note_edit.png"));
        userEditMenuItem.addActionListener(pPane);
        userEditMenuItem.setActionCommand(MainPaneActionCommand.USER_EDIT);
        add(userEditMenuItem);

    	userDelMenuItem = new JMenuItem("Remove User/Group/Category", new SlimIcon("user_remove.png"));
    	userDelMenuItem.addActionListener(pPane);
    	userDelMenuItem.setActionCommand(MainPaneActionCommand.USER_DEL);
        add(userDelMenuItem);

    	inviteMenuItem = new JMenuItem("Invite to current talk", new SlimIcon("mail_add.png"));
    	inviteMenuItem.setActionCommand(MainPaneActionCommand.INVITE_POPUP);
    	inviteMenuItem.addActionListener(pPane);
        add(inviteMenuItem);

        newTalkMenuItem = new JMenuItem("Start new talk", new SlimIcon("comment.png"));
        newTalkMenuItem.setActionCommand(MainPaneActionCommand.NEW_TALK_POPUP);
        newTalkMenuItem.addActionListener(pPane);
        add(newTalkMenuItem);
        
    	refreshMenuItem = new JMenuItem("Refresh Availability", new SlimIcon("refresh.png"));
    	refreshMenuItem.setActionCommand(MainPaneActionCommand.USER_REFRESH_POPUP);
    	refreshMenuItem.addActionListener(pPane);
        add(refreshMenuItem);
        
	}
	
}
