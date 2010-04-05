package com.oz.lanslim.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimIconListener;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimStateEnum;

public class TrayIconManager implements SlimIconListener, ActionListener {

	private final Object lock = new Object();
	
	private SlimModel model = null;
	private SlimMainFrame frame = null;
	private boolean focus = false;
	
	private Timer iconTimer = null;
	private TimerTask iconTimerTask = null;
	private boolean negativeIcon = false;
	private SystemTray systemTray = null;
	private TrayIcon trayIcon = null;
	private JPopupMenu trayMenu = null;
	private SlimStateEnum state = null;
	
	public TrayIconManager(SlimModel pModel, SlimMainFrame pFrame) {
		model = pModel;
		frame = pFrame;
		state = model.getSettings().getContactInfo().getState();
		init();
		model.registerIconListener(this);
		model.getSettings().getContactInfo().registerListener(this);
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
		    JMenuItem availItem = new JMenuItem();
		    availItem.setText(Externalizer.getString("LANSLIM.226")); //$NON-NLS-1$
		    availItem.setIcon(new SlimIcon("accept.png")); //$NON-NLS-1$
		    availItem.addActionListener(this);
		    availItem.setActionCommand(TrayMenuCommand.AVAILABLE);
		    trayMenu.add(availItem);
		    JMenuItem busyItem = new JMenuItem();
		    busyItem.setIcon(new SlimIcon("busy.png")); //$NON-NLS-1$
		    busyItem.setText(Externalizer.getString("LANSLIM.227")); //$NON-NLS-1$
		    busyItem.addActionListener(this);
		    busyItem.setActionCommand(TrayMenuCommand.BUSY);
		    trayMenu.add(busyItem);
		    JMenuItem awayItem = new JMenuItem();
		    awayItem.setText(Externalizer.getString("LANSLIM.228")); //$NON-NLS-1$
		    awayItem.setIcon(new SlimIcon("away.png")); //$NON-NLS-1$
		    awayItem.addActionListener(this);
		    awayItem.setActionCommand(TrayMenuCommand.AWAY);
		    trayMenu.add(awayItem);
		    trayMenu.addSeparator();

		    JMenuItem showItem = new JMenuItem();
		    showItem.setText(Externalizer.getString("LANSLIM.0")); //$NON-NLS-1$
		    showItem.setIcon(new SlimIcon("eject.png")); //$NON-NLS-1$
		    showItem.addActionListener(this);
		    showItem.setActionCommand(TrayMenuCommand.SHOW);
		    trayMenu.add(showItem);
		    JMenuItem hideItem = new JMenuItem();
		    hideItem.setIcon(new SlimIcon("minimize.png")); //$NON-NLS-1$
		    hideItem.setText(Externalizer.getString("LANSLIM.61")); //$NON-NLS-1$
		    hideItem.addActionListener(this);
		    hideItem.setActionCommand(TrayMenuCommand.HIDE);
		    trayMenu.add(hideItem);
		    trayMenu.addSeparator();
		    
		    JMenuItem quitItem = new JMenuItem();
		    quitItem.setText(Externalizer.getString("LANSLIM.1")); //$NON-NLS-1$
		    quitItem.setIcon(new SlimIcon("remove.png")); //$NON-NLS-1$
		    quitItem.addActionListener(this);
		    quitItem.setActionCommand(TrayMenuCommand.QUIT);
		    trayMenu.add(quitItem);
		    trayMenu.addSeparator();
		    JMenuItem aboutItem = new JMenuItem();
		    aboutItem.setText(Externalizer.getString("LANSLIM.59")); //$NON-NLS-1$
		    aboutItem.setIcon(new SlimIcon("about.png")); //$NON-NLS-1$
		    aboutItem.addActionListener(this);
		    aboutItem.setActionCommand(TrayMenuCommand.ABOUT);
		    trayMenu.add(aboutItem);
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
			synchronized (lock) {
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
		} 
		catch (Exception e) {
			SlimLogger.logException("IconBlinking", e); //$NON-NLS-1$
		}
	}
 
	public void stopIconBlinking() {

		try {
			synchronized (lock) {
				if (iconTimerTask != null) {
					iconTimerTask.cancel();
					iconTimerTask = null;
				}
				negativeIcon = false;
				updateIcon(false);
			}
		} 
		catch (Exception e) {
			SlimLogger.logException("main", e); //$NON-NLS-1$
		}
	}
	
	public void updateState(SlimStateEnum pState) {
		synchronized (lock) {
			state = pState;
			updateIcon(false);
			model.getContacts().sendUpdateMoodOrState();
			frame.refreshState(pState);
		}
	}

	public void setFocus(boolean pFocus) {
		synchronized (lock) {
			focus = pFocus;
			if (focus) {
				stopIconBlinking();
			}
		}
	}
	
	private class TrayMenuCommand {
		
		private static final String SHOW = "SHOW"; //$NON-NLS-1$
		private static final String HIDE = "HIDE"; //$NON-NLS-1$
		private static final String QUIT = "QUIT"; //$NON-NLS-1$
		private static final String BUSY = "BUSY"; //$NON-NLS-1$
		private static final String AWAY = "AWAY"; //$NON-NLS-1$
		private static final String AVAILABLE = "AVAILABLE"; //$NON-NLS-1$
		private static final String ABOUT = "ABOUT"; //$NON-NLS-1$
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(TrayMenuCommand.QUIT)) {
			System.exit(0);	
		}
		else if (e.getActionCommand().equals(TrayMenuCommand.HIDE)) {
			try {
				frame.setVisible(false);
			} 
			catch (Exception ex) {
				SlimLogger.logException("TrayShow", ex); //$NON-NLS-1$
			}
		}
		else if (e.getActionCommand().equals(TrayMenuCommand.BUSY)) {
			model.getSettings().getContactInfo().setState(SlimStateEnum.BUSY);
		}
		else if (e.getActionCommand().equals(TrayMenuCommand.AWAY)) {
			model.getSettings().getContactInfo().setState(SlimStateEnum.AWAY);
		}
		else if (e.getActionCommand().equals(TrayMenuCommand.AVAILABLE)) {
			model.getSettings().getContactInfo().setState(SlimStateEnum.AVAILABLE);
		}
		else if (e.getActionCommand().equals(TrayMenuCommand.ABOUT)) {
			AboutDialog lDialog = new AboutDialog(frame);
			lDialog.pack();
			lDialog.setLocationRelativeTo(frame.getRootPane().getParent());
			lDialog.setVisible(true);
		}
		else { //SHOW
			try {
				frame.setVisible(true);
			} 
			catch (Exception ex) {
				SlimLogger.logException("TrayShow", ex); //$NON-NLS-1$
			}
		}
	}

	protected void updateIcon(boolean pNegativeSwitch) {
		
		synchronized (lock) {
			
			ImageIcon lIcon = SlimIcon.getApplicationIcon(state, negativeIcon);
			
			if (pNegativeSwitch) {
				negativeIcon = !negativeIcon;
			}
			
			frame.setIconImage(lIcon.getImage());
			if (trayIcon != null) {
				trayIcon.setIcon(lIcon);
			}
		}
	}
	
	private class IconTimerTask extends TimerTask {
		
		public void run() {
			SwingUtilities.invokeLater(new Runnable()  {
				public void run() {
					updateIcon(true);
				}
			});
		}
	}
}

