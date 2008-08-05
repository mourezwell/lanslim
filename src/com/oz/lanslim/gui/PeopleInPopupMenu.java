package com.oz.lanslim.gui;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.oz.lanslim.gui.PeopleInPanel.PeopleInAcionCommand;

public class PeopleInPopupMenu extends JPopupMenu {

	
	public PeopleInPopupMenu(ActionListener pListner) {

        JMenuItem inviteMenuItem = new JMenuItem("Exclude from current talk", new SlimIcon("mail_remove.png"));
    	inviteMenuItem.setActionCommand(PeopleInAcionCommand.EXCLUDE);
    	inviteMenuItem.addActionListener(pListner);
        add(inviteMenuItem);
	}

}
