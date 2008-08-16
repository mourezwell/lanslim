package com.oz.lanslim.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimGroupContact;
import com.oz.lanslim.model.SlimModel;

public class ContactViewMouseListener extends MouseAdapter {
    
	private static final String TALK_SEPARATOR = "-"; //$NON-NLS-1$
	
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
	    					JOptionPane.showMessageDialog(SlimGUITUtils.getTopLevelCompoenent(e.getComponent()),
							    Externalizer.getString("LANSLIM.27"), //$NON-NLS-1$
							    Externalizer.getString("LANSLIM.28"), //$NON-NLS-1$
							    JOptionPane.WARNING_MESSAGE);
	    				}
	    			}
	    			else {
	    				if (sc.getAvailability() == SlimAvailabilityEnum.ONLINE) {
	    					cl.add(sc);
	    				}
	    				else {
	    					JOptionPane.showMessageDialog(SlimGUITUtils.getTopLevelCompoenent(e.getComponent()),
							    Externalizer.getString("LANSLIM.19"), //$NON-NLS-1$
							    Externalizer.getString("LANSLIM.28"), //$NON-NLS-1$
							    JOptionPane.WARNING_MESSAGE);
	    				}
	    			}
	    			
	    			if (cl.size() > 0) {
		        		try {
                            cl.add(model.getSettings().getContactInfo());
		        			model.getTalks().addTalk(model.getSettings().getContactInfo().getName() 
		        					+ TALK_SEPARATOR + sc.getName(), cl);
						}
						catch (SlimException se) {
							JOptionPane.showMessageDialog(SlimGUITUtils.getTopLevelCompoenent(e.getComponent()),
							    Externalizer.getString("LANSLIM.30"), //$NON-NLS-1$
							    Externalizer.getString("LANSLIM.22"), //$NON-NLS-1$
							    JOptionPane.ERROR_MESSAGE);
						}
	    			}
	    		}
        		else if (contactView.getSelectedCategories().length == 1) {
        			// nothing to do the tree will expand or collapse as usual
        		}
	        	else {
					JOptionPane.showMessageDialog(SlimGUITUtils.getTopLevelCompoenent(e.getComponent()),
						    Externalizer.getString("LANSLIM.31"), //$NON-NLS-1$
						    Externalizer.getString("LANSLIM.28"), //$NON-NLS-1$
						    JOptionPane.WARNING_MESSAGE);
	        	}
			}
			else {
				JOptionPane.showMessageDialog(SlimGUITUtils.getTopLevelCompoenent(e.getComponent()),
				    Externalizer.getString("LANSLIM.50"), //$NON-NLS-1$
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
