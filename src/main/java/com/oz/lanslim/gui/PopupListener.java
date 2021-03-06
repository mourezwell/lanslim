package com.oz.lanslim.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimGroupContact;
import com.oz.lanslim.model.SlimModel;

public class PopupListener extends MouseAdapter {
    private JPopupMenu popup;
    private JTable contactTable;
    private SlimModel model;
    private boolean doubleclick;
    private Timer doubleTimer;
    
    public PopupListener(JPopupMenu popupMenu, JTable pTable, SlimModel pModel) {
        popup = popupMenu;
        contactTable = pTable;
        model = pModel;
        doubleTimer = new Timer();
        doubleclick = false;
    }

    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
        else if (e.getClickCount() > 1 && !doubleclick) {
        	doubleclick = true;
        	if (contactTable.getSelectedRows().length == 1) {
        		Set cl = new HashSet();
        		SlimContact sc = model.getContacts().getContactByName(
						(String)contactTable.getModel().getValueAt(contactTable.getSelectedRow(), 0));
    			if (sc.isGroup()) {
					cl.addAll(((SlimGroupContact)sc).getOnlineMembers());
    			}
    			else {
					cl.add(sc);
    			}
        		try {
                    cl.add(model.getSettings().getContactInfo());
        			model.getTalks().startNewTalk(Externalizer.getString("LANSLIM.12"), new ArrayList(cl)); //$NON-NLS-1$
				}
				catch (SlimException se) {
					JOptionPane.showMessageDialog(SlimGUITUtils.getTopLevelCompoenent(e.getComponent()),
					    Externalizer.getString("LANSLIM.30"), //$NON-NLS-1$
					    Externalizer.getString("LANSLIM.22"), //$NON-NLS-1$
					    JOptionPane.ERROR_MESSAGE);
				}
    		}
        	else {
				JOptionPane.showMessageDialog(SlimGUITUtils.getTopLevelCompoenent(e.getComponent()),
					    Externalizer.getString("LANSLIM.31"), //$NON-NLS-1$
					    Externalizer.getString("LANSLIM.28"), //$NON-NLS-1$
					    JOptionPane.WARNING_MESSAGE);
        	}
        	doubleTimer.schedule(new TimerTask() {
        		public void run() {
        			doubleclick = false;
        		}
        	}, 1000);
        }
    }
}
