package com.oz.lanslim.gui;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.gui.ContactPanel.ContactPaneActionCommand;

public class ContactPopupMenu extends JPopupMenu {


	public ContactPopupMenu(ActionListener pListner) {
    
		JMenuItem userEditMenuItem = new JMenuItem(Externalizer.getString("LANSLIM.51"), new SlimIcon("note_edit.png")); //$NON-NLS-1$ //$NON-NLS-2$
        userEditMenuItem.addActionListener(pListner);
        userEditMenuItem.setActionCommand(ContactPaneActionCommand.USER_EDIT);
        add(userEditMenuItem);

        JMenuItem userDelMenuItem = new JMenuItem(Externalizer.getString("LANSLIM.52"), new SlimIcon("user_remove.png")); //$NON-NLS-1$ //$NON-NLS-2$
    	userDelMenuItem.addActionListener(pListner);
    	userDelMenuItem.setActionCommand(ContactPaneActionCommand.USER_DEL);
        add(userDelMenuItem);

        JMenuItem inviteMenuItem = new JMenuItem(Externalizer.getString("LANSLIM.33"), new SlimIcon("mail_add.png")); //$NON-NLS-1$ //$NON-NLS-2$
    	inviteMenuItem.setActionCommand(ContactPaneActionCommand.INVITE_POPUP);
    	inviteMenuItem.addActionListener(pListner);
        add(inviteMenuItem);

        JMenuItem newTalkMenuItem = new JMenuItem(Externalizer.getString("LANSLIM.53"), new SlimIcon("comment.png")); //$NON-NLS-1$ //$NON-NLS-2$
        newTalkMenuItem.setActionCommand(ContactPaneActionCommand.NEW_TALK_POPUP);
        newTalkMenuItem.addActionListener(pListner);
        add(newTalkMenuItem);
        
        JMenuItem refreshMenuItem = new JMenuItem(Externalizer.getString("LANSLIM.54"), new SlimIcon("refresh.png")); //$NON-NLS-1$ //$NON-NLS-2$
    	refreshMenuItem.setActionCommand(ContactPaneActionCommand.USER_REFRESH_POPUP);
    	refreshMenuItem.addActionListener(pListner);
        add(refreshMenuItem);
        
        JMenuItem userBlockItem = new JMenuItem(Externalizer.getString("LANSLIM.243"), new SlimIcon("user_block.png")); //$NON-NLS-1$ //$NON-NLS-2$
        userBlockItem.setActionCommand(ContactPaneActionCommand.USER_BLOCK);
        userBlockItem.addActionListener(pListner);
        add(userBlockItem);
	}
}
