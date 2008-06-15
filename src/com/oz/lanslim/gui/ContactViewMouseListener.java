package com.oz.lanslim.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimGroupContact;
import com.oz.lanslim.model.SlimModel;

public class ContactViewMouseListener extends MouseAdapter {
    private JPopupMenu popup;
    private ContactView contactView;
    private SlimModel model;
    private boolean doubleclick;
    private Timer doubleTimer;
    
    public ContactViewMouseListener(JPopupMenu popupMenu, ContactView pView, SlimModel pModel) {
        popup = popupMenu;
        contactView = pView;
        model = pModel;
        doubleTimer = new Timer();
        doubleclick = false;
    }

    public void mousePressed(MouseEvent e) {
    	if (contactView.consolidateClickPoint(e)) {
    		maybeShowPopup(e);	
    	}
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
        	if (model.getSettings().areValidSettings()) {
        		SlimContact[] scs = contactView.getSelectedContacts();
        		List cl = new ArrayList();
        		if (scs.length == 1) {
        			SlimContact sc = scs[0];
	    			if (sc.isGroup()) {
	    				if (((SlimGroupContact)sc).getOnlineMembers().size() > 0) {
	    					cl.addAll(((SlimGroupContact)sc).getOnlineMembers());
	    				}
	    				else {
	    					JOptionPane.showMessageDialog(e.getComponent(),
							    "Unable to start new Talk Message with group whose members are offline",
							    "Invalid Action",
							    JOptionPane.WARNING_MESSAGE);
	    				}
	    			}
	    			else {
	    				if (sc.getAvailability() == SlimAvailabilityEnum.ONLINE) {
	    					cl.add(sc);
	    				}
	    				else {
	    					JOptionPane.showMessageDialog(e.getComponent(),
							    "Unable to start new Talk Message with offline contact",
							    "Invalid Action",
							    JOptionPane.WARNING_MESSAGE);
	    				}
	    			}
	    			
	    			if (cl.size() > 0) {
		        		try {
                            cl.add(model.getSettings().getContactInfo());
		        			model.getTalks().addTalk(model.getSettings().getContactInfo().getName() + "-" + sc.getName(), cl);
						}
						catch (SlimException se) {
							JOptionPane.showMessageDialog(e.getComponent(),
							    "Unable to send new Talk Message please check your settings",
							    "Network Error",
							    JOptionPane.ERROR_MESSAGE);
						}
	    			}
	    		}
        		else if (contactView.getSelectedCategories().length == 1) {
        			// nothing to do the tree will expand or collapse as usual
        		}
	        	else {
					JOptionPane.showMessageDialog(e.getComponent(),
						    "One and only one contact must be selected in the table please select it first",
						    "Invalid Action",
						    JOptionPane.WARNING_MESSAGE);
	        	}
			}
			else {
				JOptionPane.showMessageDialog(e.getComponent(),
				    "All actions are disabled until you set properly your settings",
				    "Invalid Action",
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
