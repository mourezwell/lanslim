package com.oz.lanslim.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;


public class TabPaneMouseListener extends MouseAdapter {

    private JPopupMenu popup;
    
    public TabPaneMouseListener(JPopupMenu renamePopup) {
        popup = renamePopup;
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
    }

	
}
