package com.oz.lanslim.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.oz.lanslim.model.SlimModel;

public class MainPaneWindowListener extends WindowAdapter {
	
	private SlimModel model = null;
	private JFrame mainFrame = null;
	
	public MainPaneWindowListener(SlimModel pModel, JFrame pMaineFrame) {
		model = pModel;
		mainFrame = pMaineFrame;
	}

	public void windowClosing(WindowEvent e) {
		
		if (model.getSettings().isCloseAsTrayForced() && model.getSettings().isTrayEnable()) {
			mainFrame.setVisible(false);
		}
		else if (model.getSettings().isCloseForced()) {
			System.exit(0);
		}
		else { // no choice done yet or choice disabled
			CloseActionFrame lFrame =  new CloseActionFrame(model, mainFrame);
			lFrame.pack();
			lFrame.setLocationRelativeTo(mainFrame);
			lFrame.setVisible(true);
		}
	}
}
