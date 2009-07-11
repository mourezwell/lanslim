package com.oz.lanslim;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.SwingUtilities;

import com.oz.lanslim.gui.AboutDialog;
import com.oz.lanslim.gui.SlimMainFrame;
import com.oz.lanslim.gui.TrayIconManager;
import com.oz.lanslim.model.SlimModel;

public class LANSLIMMain {

	private static final String DEBUG_FLAG = "debug"; //$NON-NLS-1$

	private SlimModel model = null;
	private SlimMainFrame frame = null;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable()  {
			public void run() {
				try {
					new LANSLIMMain();
				} 
				catch (Throwable t) {
					SlimLogger.logException("main", t); //$NON-NLS-1$
					System.exit(0);
				}
			}
		});
	}
	
	public LANSLIMMain() throws IOException, SlimException {
		super();
		
		if (System.getProperty(DEBUG_FLAG) == null) {
			PrintStream lErrStream = new PrintStream(
					new FileOutputStream(System.getProperty("user.home") +  File.separator + "lanslim.err", true), true); //$NON-NLS-1$ //$NON-NLS-2$
			System.setErr(lErrStream);
			PrintStream lOutStream = new PrintStream(
					new FileOutputStream(System.getProperty("user.home") +  File.separator + "lanslim.out", true), true); //$NON-NLS-1$ //$NON-NLS-2$
			System.setOut(lOutStream);
		}
		
		model = new SlimModel();
		
		Thread shutdowHook = new Thread("Shutdown") { //$NON-NLS-1$
			public void run() {
				try {
					model.exit();
				}
				catch (SlimException se) {
					SlimLogger.logException("Shutdown", se); //$NON-NLS-1$
				}
			}
		};
		Runtime.getRuntime().addShutdownHook(shutdowHook);
		
		frame = new SlimMainFrame(model);
		
		new TrayIconManager(model, frame);
				
		frame.setVisible(true);
		
		if (!model.getSettings().isTrayEnable() || !model.getSettings().isStartAsTray()) {
			frame.setVisible(true);
			frame.toFront();
		}
		if (model.getSettings().isAutoCheckVersion()) {
			AboutDialog lDialog = new AboutDialog(frame);
			lDialog.checkVersion(true);
		}

	}
}
