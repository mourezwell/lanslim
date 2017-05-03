package com.oz.lanslim.gui;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimIconListener;
import com.oz.lanslim.model.SlimModel;

public class SlimMainFrameWindowListener extends WindowAdapter 
	implements ComponentListener {
	
	private SlimModel model = null;
	private SlimMainFrame mainFrame = null;
	
	public SlimMainFrameWindowListener(SlimModel pModel, SlimMainFrame pMaineFrame) {
		model = pModel;
		mainFrame = pMaineFrame;
	}

	public void windowClosing(WindowEvent e) {
		if (model.getSettings().isCloseAsTray() && model.getSettings().isTrayEnable()) {
			mainFrame.setVisible(false);
		}
		else if (!model.getSettings().isCloseAsTray()) {
			System.exit(0);
		}
		else { // close As tray and tray disabled
			int a = JOptionPane.showConfirmDialog(mainFrame,
				    Externalizer.getString("LANSLIM.105"), //$NON-NLS-1$
				    Externalizer.getString("LANSLIM.106"), //$NON-NLS-1$
				    JOptionPane.YES_NO_OPTION,
				    JOptionPane.WARNING_MESSAGE);
			
			if (a == JOptionPane.YES_OPTION) {
				System.exit(0);
            }
		}
	}
	
	public void componentResized(ComponentEvent e) {
		if (model != null) {
			model.getSettings().setSize(e.getComponent().getSize());
		}
	}

	public void componentShown(ComponentEvent e) {
		// Nothing to do
	}

	public void componentHidden(ComponentEvent e) {
		// Nothing to do
	}

	public void componentMoved(ComponentEvent e) {
		model.getSettings().scheduleLocationSaving(e.getComponent());
	}

	public void windowGainedFocus(WindowEvent e) {
		
		SlimIconListener lListener = model.getIconListener();
		if (lListener != null) {
			lListener.setFocus(true);
		}
		mainFrame.setVisible(true);
	}

	public void windowLostFocus(WindowEvent e) {
		
		SlimIconListener lListener = model.getIconListener();
		if (lListener != null) {
			lListener.setFocus(false);
		}
	}
}
