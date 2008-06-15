package com.oz.lanslim.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.oz.lanslim.model.SlimModel;

public class MainPaneWindowListener extends WindowAdapter {
	
	private SlimModel model = null;
	private JFrame mainFrame = null;
	
	public MainPaneWindowListener(SlimModel pModel, JFrame pMaineFrame) {
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
		else { // close As tray and tray diasabled
			int a = JOptionPane.showConfirmDialog(mainFrame,
				    "Tray icon is disabled, please check that systray native library (dll or so) is in the same directory as jar (exe). Are you sure you want to exit ?",
				    "Close confirmation",
				    JOptionPane.YES_NO_OPTION,
				    JOptionPane.WARNING_MESSAGE);
			
			if (a == JOptionPane.YES_OPTION) {
				System.exit(0);
            }
		}
	}
}
