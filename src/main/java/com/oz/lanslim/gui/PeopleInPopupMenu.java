package com.oz.lanslim.gui;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.gui.PeopleInPanel.PeopleInAcionCommand;

public class PeopleInPopupMenu extends JPopupMenu {

	
	public PeopleInPopupMenu(ActionListener pListner) {

        JMenuItem inviteMenuItem = new JMenuItem(Externalizer.getString("LANSLIM.26"), new SlimIcon("mail_remove.png")); //$NON-NLS-1$ //$NON-NLS-2$
    	inviteMenuItem.setActionCommand(PeopleInAcionCommand.EXCLUDE);
    	inviteMenuItem.addActionListener(pListner);
        add(inviteMenuItem);
	}

}
