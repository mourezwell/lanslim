package com.oz.lanslim.gui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JPopupMenu;

public class PeopleInMouseListener extends MouseAdapter {

    private JPopupMenu popup;
    private JList peopleList;
    
    public PeopleInMouseListener(JPopupMenu popupMenu, JList pList) {
        popup = popupMenu;
        peopleList = pList;
    }

	public boolean consolidateClickPoint(MouseEvent e) {
        Point p = e.getPoint();
        int row = peopleList.locationToIndex(p);
        // The autoscroller can generate drag events outside the Table's range.
        if (row == -1) {
            return false;
        }
        if (peopleList.getSelectedValues().length <= 1) {
        	peopleList.setSelectedIndex(row);
        }
        return true;
	}
    
    public void mousePressed(MouseEvent e) {
    	if (consolidateClickPoint(e)) {
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
    }
}
