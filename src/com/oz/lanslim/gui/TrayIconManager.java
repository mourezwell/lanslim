package com.oz.lanslim.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimIconListener;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.model.SlimModel;

public class TrayIconManager implements SlimIconListener, ActionListener {

	private SlimModel model = null;
	private SlimMainFrame frame = null;
	private boolean focus = false;
	
	private Timer iconTimer = null;
	private TimerTask iconTimerTask = null;
	private boolean negativeIcon = true;
	private SystemTray systemTray = null;
	private TrayIcon trayIcon = null;
	private JPopupMenu trayMenu = null;

	public TrayIconManager(SlimModel pModel, SlimMainFrame pFrame) {
		model = pModel;
		frame = pFrame;
		init();
		model.registerIconListener(this);
	}
	
	public void init() {
		
		iconTimer = new Timer();
		iconTimer.schedule(new TimerTask() {
			public void run() {
				Thread.currentThread().setName("Icon Timer"); //$NON-NLS-1$
			}
		}, 0);

	    try {
	    	systemTray = SystemTray.getDefaultSystemTray();
		    trayMenu = new JPopupMenu();
		    JMenuItem showItem = new JMenuItem();
		    showItem.setText(Externalizer.getString("LANSLIM.0")); //$NON-NLS-1$
		    showItem.addActionListener(this);
		    showItem.setActionCommand(TrayMenuCommand.SHOW);
		    trayMenu.add(showItem);
		    trayMenu.addSeparator();
		    JMenuItem quitItem = new JMenuItem();
		    quitItem.setText(Externalizer.getString("LANSLIM.1")); //$NON-NLS-1$
		    quitItem.addActionListener(this);
		    quitItem.setActionCommand(TrayMenuCommand.QUIT);
		    trayMenu.add(quitItem);
		    trayIcon = new TrayIcon(new SlimIcon("comment_edit.png"), SlimMainFrame.TITLE, trayMenu); //$NON-NLS-1$
			trayIcon.addBalloonActionListener(this);
			trayIcon.addActionListener(this);
		    systemTray.addTrayIcon(trayIcon);
		    
		} 
		catch (UnsatisfiedLinkError ule) {
			model.getSettings().setTrayEnable(false);
		}
	}

	public void startIconBlinking(final boolean pBlink, final boolean pPop, final String pMessage) {

		try {
			if (!focus) {
				if (iconTimerTask == null && pBlink) {
					iconTimerTask = new IconTimerTask();
					iconTimer.scheduleAtFixedRate(iconTimerTask, new Date(), 500);
				}
				if (trayIcon != null && pPop) {
					trayIcon.displayMessage(SlimMainFrame.TITLE, pMessage, TrayIcon.INFO_MESSAGE_TYPE); //$NON-NLS-1$
				}
				// else should not happen since at least one solution should work
			}
		} 
		catch (Exception e) {
			SlimLogger.logException("IconBlinking", e); //$NON-NLS-1$
		}
	}
 
	public void stopIconBlinking() {

		try {
			if (iconTimerTask != null) {
				iconTimerTask.cancel();
				iconTimerTask = null;
			}
			negativeIcon =  false;
			frame.setIconImage(new SlimIcon("comment_edit.png").getImage()); //$NON-NLS-1$
			if (trayIcon != null) {
				trayIcon.setIcon(new SlimIcon("comment_edit.png")); //$NON-NLS-1$
			}
		} 
		catch (Exception e) {
			SlimLogger.logException("main", e); //$NON-NLS-1$
		}
	}
	
	public void setFocus(boolean pFocus) {
		focus = pFocus;
		if (focus) {
			stopIconBlinking();
		}
	}
	
	private class TrayMenuCommand {
		
		private static final String SHOW = "SHOW"; //$NON-NLS-1$
		private static final String QUIT = "QUIT"; //$NON-NLS-1$
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(TrayMenuCommand.QUIT)) {
			System.exit(0);	
		}
		else {
			try {
				frame.setVisible(true);
			} 
			catch (Exception ex) {
				SlimLogger.logException("TrayShow", ex); //$NON-NLS-1$
			
			}
		}
	}

	private class IconTimerTask extends TimerTask {
		
		public void run() {
			SwingUtilities.invokeLater(new Runnable()  {
				public void run() {
					if (negativeIcon) {
						frame.setIconImage(new SlimIcon("comment_edit.png").getImage()); //$NON-NLS-1$
						if (trayIcon != null) {
							trayIcon.setIcon(new SlimIcon("comment_edit.png")); //$NON-NLS-1$
						}
						negativeIcon = false;
					}
					else {
						frame.setIconImage(new SlimIcon("comment_edit_neg.png").getImage()); //$NON-NLS-1$
						if (trayIcon != null) {
							trayIcon.setIcon(new SlimIcon("comment_edit_neg.png")); //$NON-NLS-1$
						}
						negativeIcon = true;
					}
				}
			});
		}
	}
}

