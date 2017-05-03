package com.oz.lanslim.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.PeopleInAvailabilityListener;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimStateEnum;
import com.oz.lanslim.model.SlimTalk;
import com.oz.lanslim.model.SlimTalkListener;

public class MainPane extends JPanel implements 
	ActionListener, ChangeListener,	ContainerListener,  FocusListener, 
	PeopleInAvailabilityListener, SlimTalkListener, TalkSelector, SettingsListener {
	
	private SlimModel model;
	
	private JToolBar mainBar;
	private JButton newTalkButton;
	private JButton settingsButton;
	private JButton aboutButton;
	private JButton exitButton;
	private JButton minimizeButton;
	private JComboBox stateBox;
	private StateComboBoxRenderer stateBoxRenderer;
	private JTextField stateField;
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
		model.getContacts().registerPeopleInListener(this);
		init();
	}
	
	
	private void init() {
		
		BorderLayout lLayout = new BorderLayout();
		setLayout(lLayout);
		setPreferredSize(new java.awt.Dimension(700, 400));
		{
			mainBar = new JToolBar();
			mainBar.setFloatable(false);
			mainBar.setRollover(true);
			add(mainBar, BorderLayout.NORTH);
			{
				settingsButton = new JButton();
				settingsButton.setIcon(new SlimIcon("process.png")); //$NON-NLS-1$
				settingsButton.setActionCommand(MainPaneActionCommand.SETTINGS);
				settingsButton.addActionListener(this);
				settingsButton.setToolTipText(Externalizer.getString("LANSLIM.56")); //$NON-NLS-1$
				mainBar.add(settingsButton);

				newTalkButton = new JButton();
				newTalkButton.setIcon(new SlimIcon("comments.png")); //$NON-NLS-1$
				newTalkButton.setActionCommand(MainPaneActionCommand.NEW_TALK);
				newTalkButton.addActionListener(this);
				newTalkButton.setToolTipText(Externalizer.getString("LANSLIM.53")); //$NON-NLS-1$
				mainBar.add(newTalkButton);

				{
					Integer[] intArray = new Integer[SlimStateEnum.NUMBER];
					for (int i = 0; i < SlimStateEnum.NUMBER; i++) {
						intArray[i] = new Integer(i);
					}
					stateBoxRenderer = new StateComboBoxRenderer(model.getSettings().isTextWithButton());; 

				    stateBox = new JComboBox(intArray);
					stateBox.setRenderer(stateBoxRenderer);
					stateBox.addActionListener(this);
					stateBox.setActionCommand(MainPaneActionCommand.STATE);
					stateBox.setToolTipText(Externalizer.getString("LANSLIM.231")); //$NON-NLS-1$
			        mainBar.add(stateBox);
				}

				{
					stateField = new JTextField();
					stateField.setPreferredSize(new Dimension(150, 20));
					stateField.setMaximumSize(new Dimension(180, 20));
					stateField.setToolTipText(Externalizer.getString("LANSLIM.232")); //$NON-NLS-1$
					stateField.setText(model.getSettings().getContactInfo().getMood());
					stateField.addFocusListener(this);
					mainBar.addSeparator();
			        mainBar.add(stateField);
				}

				mainBar.add(Box.createHorizontalGlue());

				aboutButton = new JButton();
				aboutButton.setIcon(new SlimIcon("help.png")); //$NON-NLS-1$
				aboutButton.setActionCommand(MainPaneActionCommand.ABOUT);
				aboutButton.addActionListener(this);
				aboutButton.setToolTipText(Externalizer.getString("LANSLIM.60")); //$NON-NLS-1$
				mainBar.add(aboutButton);
				
				minimizeButton = new JButton();
				minimizeButton.setIcon(new SlimIcon("down.png")); //$NON-NLS-1$
				minimizeButton.setActionCommand(MainPaneActionCommand.MINIMIZE);
				minimizeButton.addActionListener(this);
				minimizeButton.setToolTipText(Externalizer.getString("LANSLIM.62")); //$NON-NLS-1$
				mainBar.add(minimizeButton);
				
				exitButton = new JButton();
				exitButton.setIcon(new SlimIcon("exit.png")); //$NON-NLS-1$
				exitButton.setActionCommand(MainPaneActionCommand.EXIT);
				exitButton.addActionListener(this);
				exitButton.setToolTipText(Externalizer.getString("LANSLIM.65")); //$NON-NLS-1$
				mainBar.add(exitButton);
				
				mainBar.addMouseListener(new ToolBarMouseListener());
			}
			updateDisplay();
		}
		{
			mainSplitPane = new JSplitPane();
			mainSplitPane.setDividerSize(5);
			add(mainSplitPane);
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

	public void updateDisplay() {
		if (model.getSettings().isTextWithButton()) {
			stateBoxRenderer.setTextEnable(true);
		    stateBox.setPreferredSize(new Dimension(100, 30));
		    stateBox.setMaximumSize(new Dimension(100, 30));
		    stateBox.setMinimumSize(new Dimension(100, 30));
			settingsButton.setText(Externalizer.getString("LANSLIM.55")); //$NON-NLS-1$
			newTalkButton.setText(Externalizer.getString("LANSLIM.57")); //$NON-NLS-1$
			aboutButton.setText(Externalizer.getString("LANSLIM.59")); //$NON-NLS-1$
			minimizeButton.setText(Externalizer.getString("LANSLIM.61")); //$NON-NLS-1$
			exitButton.setText(Externalizer.getString("LANSLIM.64")); //$NON-NLS-1$
		}
		else {
			stateBoxRenderer.setTextEnable(false);
		    stateBox.setPreferredSize(new Dimension(50, 30));
		    stateBox.setMaximumSize(new Dimension(50, 30));
		    stateBox.setMinimumSize(new Dimension(50, 30));
			settingsButton.setText("");
			newTalkButton.setText("");
			aboutButton.setText("");
			minimizeButton.setText("");
			exitButton.setText("");
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand() == MainPaneActionCommand.SETTINGS) {
			boolean lOpenedTalks = model.getTalks().size() != 0;
			if (lOpenedTalks) {
				JOptionPane.showMessageDialog(getRootPane().getParent(),
					    Externalizer.getString("LANSLIM.66"), //$NON-NLS-1$
					    Externalizer.getString("LANSLIM.55"), //$NON-NLS-1$
					    JOptionPane.INFORMATION_MESSAGE);
			}
			SettingsFrame lFrame = new SettingsFrame((Frame)getRootPane().getParent(), 
					model.getSettings(), lOpenedTalks, this);
			lFrame.pack();
			lFrame.setLocationRelativeTo(getRootPane().getParent());
			lFrame.setVisible(true);
		}
		else if (e.getActionCommand() == MainPaneActionCommand.ABOUT) {
			AboutDialog lFrame = new AboutDialog((Frame)getRootPane().getParent());
			lFrame.pack();
			lFrame.setLocationRelativeTo(getRootPane().getParent());
			lFrame.setVisible(true);
		}
		else if (e.getActionCommand() == MainPaneActionCommand.MINIMIZE) {
			if (model.getSettings().isTrayEnable()) {
				getRootPane().getParent().setVisible(false);
			}
			else {
				((Frame)getRootPane().getParent()).setExtendedState(Frame.ICONIFIED);
			}
			
		}
		else if (e.getActionCommand() == MainPaneActionCommand.EXIT) {
			System.exit(0);
		}
		else if (e.getActionCommand() == MainPaneActionCommand.NEW_TALK) {
			if (model.getSettings().areValidSettings()) {
				if (e.getActionCommand() == MainPaneActionCommand.NEW_TALK) {
					NewTalkFrame lFrame = new NewTalkFrame(
							(Frame)getRootPane().getParent(), model, null, null);
					lFrame.pack();
					lFrame.setLocationRelativeTo(getRootPane().getParent());
					lFrame.setVisible(true);
				}
				else {
					JOptionPane.showMessageDialog(getRootPane().getParent(),
					    Externalizer.getString("LANSLIM.50"), //$NON-NLS-1$
					    Externalizer.getString("LANSLIM.28"), //$NON-NLS-1$
					    JOptionPane.WARNING_MESSAGE);
				}
			}
		}
		else if (e.getActionCommand() == MainPaneActionCommand.STATE) {
			model.getSettings().getContactInfo().setState(
					StateComboBoxRenderer.STATES[stateBox.getSelectedIndex()]);
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
			JOptionPane.showMessageDialog(getRootPane().getParent(),
			    Externalizer.getString("LANSLIM.58"), //$NON-NLS-1$
			    Externalizer.getString("LANSLIM.22"), //$NON-NLS-1$
			    JOptionPane.ERROR_MESSAGE);
		}
	}

	public void stateChanged(ChangeEvent e) {
		updateAvailabilities();
	}

	public void updateAvailabilities() {
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
		if (talkTabPanes.getSelectedComponent() != null) {
			if (((TalkPane)talkTabPanes.getSelectedComponent()).getTalk() !=  pTalk) {
				for (int i = 0; i < talkTabPanes.getComponents().length; i++) {
					TalkPane tp = (TalkPane)talkTabPanes.getComponents()[i];
					if (tp.getTalk() == pTalk) {
						talkTabPanes.setIconAt(talkTabPanes.indexOfComponent(tp), new SlimIcon("favorite.png")); //$NON-NLS-1$
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
	}
	
	public SlimTalk getDisplayedTalk() {
		if (talkTabPanes.getComponents().length > 0) {
			return ((TalkPane)talkTabPanes.getSelectedComponent()).getTalk();
		}
		return null;
	}
	
	private class MainPaneActionCommand {
		
		public static final String ABOUT = "about"; //$NON-NLS-1$
		
		public static final String SETTINGS = "settings"; //$NON-NLS-1$

		public static final String NEW_TALK = "newTalk"; //$NON-NLS-1$

		public static final String MINIMIZE = "minimize"; //$NON-NLS-1$
		
		public static final String EXIT = "exit"; //$NON-NLS-1$

		public static final String STATE = "state"; //$NON-NLS-1$
		
	}
	
	private class ToolBarMouseListener extends MouseAdapter implements ActionListener {
		
		private JPopupMenu popup = null;
		private JMenuItem showItem = null;
		
		private final ImageIcon CHECKED = new SlimIcon("check.png");
		private final ImageIcon BLANK = new SlimIcon("blank.png");
		
		protected ToolBarMouseListener() {
			init();
		}
		 
	    public void mousePressed(MouseEvent e) {
	        if (e.isPopupTrigger()) {
	            popup.show(e.getComponent(), e.getX(), e.getY());
	        }
        }

	    public void mouseReleased(MouseEvent e) {
	        if (e.isPopupTrigger()) {
	            popup.show(e.getComponent(), e.getX(), e.getY());
	        }
        }

		private void init() {
		    
			popup = new JPopupMenu();
			showItem = new JMenuItem(); //$NON-NLS-1$ //$NON-NLS-2$
			showItem.setText(Externalizer.getString("LANSLIM.233"));
			showItem.setToolTipText(Externalizer.getString("LANSLIM.234"));
			if (model.getSettings().isTextWithButton()) {
				showItem.setIcon(CHECKED);
			}
			else {
				showItem.setIcon(BLANK);
			}
			showItem.addActionListener(this);
	        popup.add(showItem);
		}
		
		public void actionPerformed(ActionEvent e) {
			if (model.getSettings().isTextWithButton()) {
				model.getSettings().setTextWithButton(false);
				showItem.setIcon(BLANK);
			}
			else {
				model.getSettings().setTextWithButton(true);
				showItem.setIcon(CHECKED);
			}
			updateDisplay();
		}
		
	}

	public void focusGained(FocusEvent pE) {
		// update mood is done when focus is lost by state field
	}


	public void focusLost(FocusEvent pE) {
		model.getSettings().getContactInfo().setMood(stateField.getText());
	}

	public void refreshState(SlimStateEnum pState) {
		
		stateBox.setSelectedIndex(StateComboBoxRenderer.getStateIndex(pState));
	}

}
