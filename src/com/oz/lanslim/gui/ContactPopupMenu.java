package com.oz.lanslim.gui;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.oz.lanslim.gui.ContactPanel.ContactPaneActionCommand;

public class ContactPopupMenu extends JPopupMenu {


	public ContactPopupMenu(ActionListener pListner) {
    
		JMenuItem userEditMenuItem = new JMenuItem("Edit User/Group/Category", new SlimIcon("note_edit.png"));
        userEditMenuItem.addActionListener(pListner);
        userEditMenuItem.setActionCommand(ContactPaneActionCommand.USER_EDIT);
        add(userEditMenuItem);

        JMenuItem userDelMenuItem = new JMenuItem("Remove User/Group/Category", new SlimIcon("user_remove.png"));
    	userDelMenuItem.addActionListener(pListner);
    	userDelMenuItem.setActionCommand(ContactPaneActionCommand.USER_DEL);
        add(userDelMenuItem);

        JMenuItem inviteMenuItem = new JMenuItem("Invite to current talk", new SlimIcon("mail_add.png"));
    	inviteMenuItem.setActionCommand(ContactPaneActionCommand.INVITE_POPUP);
    	inviteMenuItem.addActionListener(pListner);
        add(inviteMenuItem);

        JMenuItem newTalkMenuItem = new JMenuItem("Start new talk", new SlimIcon("comment.png"));
        newTalkMenuItem.setActionCommand(ContactPaneActionCommand.NEW_TALK_POPUP);
        newTalkMenuItem.addActionListener(pListner);
        add(newTalkMenuItem);
        
        JMenuItem refreshMenuItem = new JMenuItem("Refresh Availability", new SlimIcon("refresh.png"));
    	refreshMenuItem.setActionCommand(ContactPaneActionCommand.USER_REFRESH_POPUP);
    	refreshMenuItem.addActionListener(pListner);
        add(refreshMenuItem);
        
	}
}
