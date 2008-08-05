package com.oz.lanslim.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimTalk;
import com.oz.lanslim.model.SlimTalkListener;

public class MainPane extends JPanel implements ActionListener, ChangeListener, 
	ContainerListener, SlimTalkListener, TalkSelector {
	
	private SlimModel model;
	
	private JToolBar mainBar;
	private JButton newTalkButton;
	private JButton settingsButton;
	private JButton aboutButton;
	private JButton exitButton;
	private JButton minimizeButton;
	private JSplitPane mainSplitPane;
	
	private JTabbedPane talkTabPanes;
	private JPopupMenu talkTabPanesPopupMenu;
	
	private JSplitPane mainLeftPane;
	private PeopleInPanel peopleInPane;
	private ContactPanel contactPane;
	
	public MainPane(SlimModel pModel) {
		super();
		model = pModel;
		model.getTalks().registerListener(this);
		init();
	}
	
	private void init() {
		
		BorderLayout lLayout = new BorderLayout();
		this.setLayout(lLayout);
		this.setPreferredSize(new java.awt.Dimension(700, 400));
		{
			mainBar = new JToolBar();
			mainBar.setFloatable(false);
			mainBar.setRollover(true);
			this.add(mainBar, BorderLayout.NORTH);
			{
				settingsButton = new JButton();
				settingsButton.setIcon(new SlimIcon("process.png"));
				settingsButton.setText("Settings");
				settingsButton.setMnemonic(KeyEvent.VK_S);
				settingsButton.setActionCommand(MainPaneActionCommand.SETTINGS);
				settingsButton.addActionListener(this);
				settingsButton.setToolTipText("Set settings");
				mainBar.add(settingsButton);

				newTalkButton = new JButton();
				newTalkButton.setIcon(new SlimIcon("comments.png"));
				newTalkButton.setText("New Talk");
				settingsButton.setMnemonic(KeyEvent.VK_N);
				newTalkButton.setActionCommand(MainPaneActionCommand.NEW_TALK);
				newTalkButton.addActionListener(this);
				newTalkButton.setToolTipText("Start new Talk");
				mainBar.add(newTalkButton);
				
				mainBar.add(Box.createHorizontalGlue());

				aboutButton = new JButton();
				aboutButton.setIcon(new SlimIcon("help.png"));
				aboutButton.setText("About");
				aboutButton.setMnemonic(KeyEvent.VK_F1);
				aboutButton.setActionCommand(MainPaneActionCommand.ABOUT);
				aboutButton.addActionListener(this);
				aboutButton.setToolTipText("About Dialog");
				mainBar.add(aboutButton);
				
				minimizeButton = new JButton();
				minimizeButton.setIcon(new SlimIcon("down.png"));
				minimizeButton.setText("Minimize");
				minimizeButton.setActionCommand(MainPaneActionCommand.MINIMIZE);
				minimizeButton.addActionListener(this);
				minimizeButton.setToolTipText("Minimize Application");
				mainBar.add(minimizeButton);
				
				exitButton = new JButton();
				exitButton.setIcon(new SlimIcon("exit.png"));
				exitButton.setText("Exit");
				exitButton.setMnemonic(KeyEvent.VK_ESCAPE);
				exitButton.setActionCommand(MainPaneActionCommand.EXIT);
				exitButton.addActionListener(this);
				exitButton.setToolTipText("Exit Application");
				mainBar.add(exitButton);
			}
		}
		{
			mainSplitPane = new JSplitPane();
			mainSplitPane.setDividerSize(5);
			this.add(mainSplitPane);
			{
				talkTabPanes = new JTabbedPane();
				talkTabPanes.setUI(new TabbedPaneCloseButtonUI());
				talkTabPanes.setPreferredSize(new java.awt.Dimension(400, 400));
				talkTabPanes.addContainerListener(this);
				talkTabPanes.addChangeListener(this);
				mainSplitPane.add(talkTabPanes, JSplitPane.RIGHT);

				//Create the popup menu.
				talkTabPanesPopupMenu = new TalkPopupMenu(model, talkTabPanes);
		        talkTabPanes.addMouseListener(new TabPaneMouseListener(talkTabPanesPopupMenu));
			}
			{
				mainLeftPane = new JSplitPane();
				mainLeftPane.setPreferredSize(new java.awt.Dimension(200, 400));
				mainLeftPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
				mainSplitPane.add(mainLeftPane, JSplitPane.LEFT);
				{
					peopleInPane = new PeopleInPanel(model, this);
					mainLeftPane.add(peopleInPane, JSplitPane.TOP);
				}
				{
					contactPane = new ContactPanel(model, this);
					mainLeftPane.add(contactPane, JSplitPane.BOTTOM);
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand() == MainPaneActionCommand.SETTINGS) {
			if (model.getTalks().size() == 0) {
				SettingsFrame lFrame = new SettingsFrame((Frame)this.getRootPane().getParent(), model, false);
				lFrame.pack();
				lFrame.setLocationRelativeTo(this);
				lFrame.setVisible(true);
			}
			else {
				JOptionPane.showMessageDialog(this,
					    "Name and Host settings are locked while talks are opened",
					    "Information",
					    JOptionPane.INFORMATION_MESSAGE);
				SettingsFrame lFrame = new SettingsFrame((Frame)this.getRootPane().getParent(), model, true);
				lFrame.pack();
				lFrame.setLocationRelativeTo(this);
				lFrame.setVisible(true);
			}
		}
		else if (e.getActionCommand() == MainPaneActionCommand.ABOUT) {
			AboutDialog.showDialog(this);
		}
		else if (e.getActionCommand() == MainPaneActionCommand.MINIMIZE) {
			if (model.getSettings().isTrayEnable() && model.getSettings().isCloseAsTray()) {
				this.getRootPane().getParent().setVisible(false);
			}
			else {
				((Frame)this.getRootPane().getParent()).setExtendedState(Frame.ICONIFIED);
			}
			
		}
		else if (e.getActionCommand() == MainPaneActionCommand.EXIT) {
			System.exit(0);
		}
		else if (e.getActionCommand() == MainPaneActionCommand.NEW_TALK) {
			if (model.getSettings().areValidSettings()) {
				if (e.getActionCommand() == MainPaneActionCommand.NEW_TALK) {
					NewTalkFrame lFrame = new NewTalkFrame(
							(Frame)this.getRootPane().getParent(), model, null, null);
					lFrame.pack();
					lFrame.setLocationRelativeTo(this);
					lFrame.setVisible(true);
				}
				else {
					JOptionPane.showMessageDialog(this,
					    "All actions are disabled until you set properly your settings",
					    "Invalid Action",
					    JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}

	public void componentAdded(ContainerEvent e) {
		// Model allready uptodate
	}
	
	public void componentRemoved(ContainerEvent e) {
		try {
			model.getTalks().closeTalk(((TalkPane)e.getChild()).getTalk());
		}
		catch (SlimException se) {
			JOptionPane.showMessageDialog(this,
			    "Some users may not have received the notifiaction that you have left the talk",
			    "Network Error",
			    JOptionPane.ERROR_MESSAGE);
		}
	}

	public void stateChanged(ChangeEvent e) {
		if (talkTabPanes.getSelectedComponent() != null) {
			peopleInPane.update(
					((TalkPane)talkTabPanes.getSelectedComponent()).getTalk().getPeopleIn());
			talkTabPanes.setIconAt(talkTabPanes.getSelectedIndex(), null);
		}
		else {
			peopleInPane.update(new ArrayList());
		}
	}

	public void notifyNewTalk(SlimTalk pTalk) {
		TalkPane newTalkPane = new TalkPane(this, pTalk);
		talkTabPanes.addTab(pTalk.getTitle(), null, newTalkPane, null);
		talkTabPanes.setSelectedComponent(newTalkPane);
		//peopleInArea updated via change event
		talkTabPanes.setToolTipTextAt(talkTabPanes.getSelectedIndex(), pTalk.getPeopleInListAsString());
	}
	
	public void notifyTextTalkUpdate(SlimTalk pTalk) {
		if (((TalkPane)talkTabPanes.getSelectedComponent()).getTalk() !=  pTalk) {
			for (int i = 0; i < talkTabPanes.getComponents().length; i++) {
				TalkPane tp = (TalkPane)talkTabPanes.getComponents()[i];
				if (tp.getTalk() == pTalk) {
					talkTabPanes.setIconAt(talkTabPanes.indexOfComponent(tp), new SlimIcon("favorite.png"));
					talkTabPanes.setToolTipTextAt(talkTabPanes.indexOfComponent(tp), pTalk.getPeopleInListAsString()); 
				}
			}
		}
		else {
			peopleInPane.update(pTalk.getPeopleIn());
			talkTabPanes.setToolTipTextAt(talkTabPanes.getSelectedIndex(), pTalk.getPeopleInListAsString());
			talkTabPanes.setTitleAt(talkTabPanes.getSelectedIndex(), pTalk.getTitle());
		}
	}
	
	public SlimTalk getDisplayedTalk() {
		if (talkTabPanes.getComponents().length > 0) {
			return ((TalkPane)talkTabPanes.getSelectedComponent()).getTalk();
		}
		return null;
	}
	
	public class MainPaneActionCommand {
		
		public static final String ABOUT = "about";
		
		public static final String SETTINGS = "settings";

		public static final String NEW_TALK = "newTalk";

		public static final String MINIMIZE = "minimize";
		
		public static final String EXIT = "exit";
		
	}

}
