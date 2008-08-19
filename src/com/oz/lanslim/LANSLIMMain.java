package com.oz.lanslim;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;

import com.oz.lanslim.gui.MainPane;
import com.oz.lanslim.gui.MainPaneWindowListener;
import com.oz.lanslim.gui.SlimIcon;
import com.oz.lanslim.model.HTMLConstants;
import com.oz.lanslim.model.SlimIconListener;
import com.oz.lanslim.model.SlimModel;

public class LANSLIMMain extends JFrame 
	implements WindowFocusListener, SlimIconListener, ActionListener, ComponentListener {

	
	private static final String TITLE = "LANSLIM"; //$NON-NLS-1$
	private static final String DEBUG_FLAG = "debug"; //$NON-NLS-1$
	
	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel"); //$NON-NLS-1$
		} catch(Exception e) {
			SlimLogger.logException("Look&Feel setting", e); //$NON-NLS-1$
		}
	}

	private JPanel mainPanel = null;
	private SlimModel model = null;
	private boolean hasFocus = false;
	private Timer iconTimer = null;
	private TimerTask iconTimerTask = null;
	private boolean negativeIcon = true;
	private TimerTask newMessageWindowTask = null;
	private JWindow newMessageWindow = null;
	private SystemTray systemTray = null;
	private TrayIcon trayIcon = null;
	private JPopupMenu trayMenu = null;
	
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
		iconTimer = new Timer();
		iconTimer.schedule(new TimerTask() {
			public void run() {
				Thread.currentThread().setName("Icon Timer"); //$NON-NLS-1$
			}
		}, 0);
		model = new SlimModel(this);
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
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setLocationRelativeTo(null);
			setTitle(TITLE);
			setIconImage(new SlimIcon("comment_edit.png").getImage()); //$NON-NLS-1$
			addWindowFocusListener(this);

			mainPanel = new MainPane(model);
			getContentPane().add(mainPanel, BorderLayout.CENTER);
			setSize(model.getSettings().getW(), model.getSettings().getH());
			setLocation(model.getSettings().getX(), model.getSettings().getY());
			addWindowListener(new MainPaneWindowListener(model, this));
			addComponentListener(this);
					    
		    try {
		    	systemTray = SystemTray.getDefaultSystemTray();
			    trayMenu = new JPopupMenu();
			    JMenuItem showItem = new JMenuItem();
			    showItem.setText(Externalizer.getString("LANSLIM.0")); //$NON-NLS-1$
			    showItem.addActionListener(this);
			    showItem.setActionCommand(LANSLIMMainActionCommand.SHOW);
			    trayMenu.add(showItem);
			    trayMenu.addSeparator();
			    JMenuItem quitItem = new JMenuItem();
			    quitItem.setText(Externalizer.getString("LANSLIM.1")); //$NON-NLS-1$
			    quitItem.addActionListener(this);
			    quitItem.setActionCommand(LANSLIMMainActionCommand.QUIT);
			    trayMenu.add(quitItem);
			    trayIcon = new TrayIcon(new SlimIcon("comment_edit.png"), TITLE, trayMenu); //$NON-NLS-1$
				trayIcon.addBalloonActionListener(this);
				trayIcon.addActionListener(this);
			    systemTray.addTrayIcon(trayIcon);
			    
			} 
			catch (UnsatisfiedLinkError ule) {
				model.getSettings().setTrayEnable(false);
				
				newMessageWindow = new JWindow();
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice gd = ge.getDefaultScreenDevice();
			    GraphicsConfiguration gc = gd.getDefaultConfiguration();
			    Rectangle vb = gc.getBounds();
			    newMessageWindow.setBounds(vb.x + vb.width - 120, vb.y +  vb.height - 60, 120, 30);
			    JButton label = new JButton();
			    label.setText(HTMLConstants.HTML + HTMLConstants.UNDERLINE 
			    		+ "New Message" + HTMLConstants.ENDUNDERLINE + HTMLConstants.ENDHTML); //$NON-NLS-1$
			    label.setIcon(new SlimIcon("comments.png")); //$NON-NLS-1$
			    label.setActionCommand(LANSLIMMainActionCommand.SHOW);
			    label.addActionListener(this);
			    newMessageWindow.getContentPane().add(label);
			    newMessageWindow.setVisible(true);
			    newMessageWindow.setVisible(false);
			}
		}
		catch (Exception e) {
			SlimLogger.logException("LANSLIMMain.initGUI", e); //$NON-NLS-1$
		}
		
		if (!model.getSettings().isTrayEnable() || !model.getSettings().isStartAsTray()) {
			setVisible(true);
			toFront();
		}
		
	}

	public void windowGainedFocus(WindowEvent e) {
		hasFocus = true;
		stopIconBlinking();
	}

	public void windowLostFocus(WindowEvent e) {
		hasFocus = false;
	}
	
	public synchronized void startIconBlinking() {
		SwingUtilities.invokeLater(new Runnable()  {
			public void run() {
				try {
					if (!hasFocus) {
						if (iconTimerTask == null) {
							iconTimerTask = new TimerTask() {
								public void run() {
									if (negativeIcon) {
										setIconImage(new SlimIcon("comment_edit.png").getImage()); //$NON-NLS-1$
										if (trayIcon != null) {
											trayIcon.setIcon(new SlimIcon("comment_edit.png")); //$NON-NLS-1$
										}
										negativeIcon = false;
									}
									else {
										setIconImage(new SlimIcon("comment_edit_neg.png").getImage()); //$NON-NLS-1$
										if (trayIcon != null) {
											trayIcon.setIcon(new SlimIcon("comment_edit_neg.png")); //$NON-NLS-1$
										}
										negativeIcon = true;
									}
								}
							};
							iconTimer.scheduleAtFixedRate(iconTimerTask, new Date(), 500);
						}
					}
					if (newMessageWindow != null) {
					    newMessageWindow.setVisible(true);
					    newMessageWindow.toFront();
					    newMessageWindowTask = new TimerTask() {
							public void run() {
								hideNewMessageWindow();
							}
						};
						iconTimer.schedule(newMessageWindowTask, 3000);
					}
					else if (trayIcon != null) {
						trayIcon.displayMessage(TITLE, Externalizer.getString("LANSLIM.2"), TrayIcon.INFO_MESSAGE_TYPE); //$NON-NLS-1$
					}
					// else should not happen since at least one solution should work 
				} 
				catch (Exception e) {
					SlimLogger.logException("IconBlinking",e); //$NON-NLS-1$
				}
			}
		});
	}
 
	private synchronized void stopIconBlinking() {

		SwingUtilities.invokeLater(new Runnable()  {
			public void run() {
				try {
					if (iconTimerTask != null) {
						iconTimerTask.cancel();
						iconTimerTask = null;
					}
					negativeIcon =  false;
					setIconImage(new SlimIcon("comment_edit.png").getImage()); //$NON-NLS-1$
					if (trayIcon != null) {
						trayIcon.setIcon(new SlimIcon("comment_edit.png")); //$NON-NLS-1$
					}
				} 
				catch (Exception e) {
					SlimLogger.logException("main", e); //$NON-NLS-1$
				}
			}
		});
	}

	public synchronized void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(LANSLIMMainActionCommand.QUIT)) {
			System.exit(0);	
		}
		else {
 			SwingUtilities.invokeLater(new Runnable()  {
				public void run() {
					try {
						setVisible(true);
						if (newMessageWindow != null) {
							newMessageWindow.setVisible(false);
						}
					} 
					catch (Exception ex) {
						SlimLogger.logException("TrayShow", ex); //$NON-NLS-1$
					
					}
				}
			});
		}
	}
	

	public synchronized void hideNewMessageWindow() {
		SwingUtilities.invokeLater(new Runnable()  {
			public void run() {
				try {
					newMessageWindow.setVisible(false);
				} 
				catch (Exception e) {
					SlimLogger.logException("hideNewMessageWindow", e); //$NON-NLS-1$
				
				}
			}
		});
	}

	public void componentHidden(ComponentEvent e) {
		// Nothing to do
	}

	public void componentMoved(ComponentEvent e) {
		if (model != null) {
			model.getSettings().setLocation(e.getComponent().getLocation());
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
	
	private class LANSLIMMainActionCommand {
		
		private static final String SHOW = "SHOW"; //$NON-NLS-1$
		private static final String QUIT = "QUIT"; //$NON-NLS-1$
	}
	
}
