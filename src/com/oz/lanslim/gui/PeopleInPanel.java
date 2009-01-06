package com.oz.lanslim.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.PeopleInAvailabilityListener;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimTalk;
import com.oz.lanslim.model.SlimUserContact;

public class PeopleInPanel extends JPanel implements ActionListener, PeopleInAvailabilityListener {

	private JToolBar peopleInBar;
	private JLabel peopleInLabel;
	private JButton peopleInviteButton;
	private JButton peopleExcludeButton;
	private JScrollPane peopleInAreaPane;
	private JList peopleInList;
	private DefaultListModel peopleInModel;

	private TalkSelector talkDisplay;
	private SlimModel model;
	
	PeopleInPopupMenu popupMenu;
	
	public PeopleInPanel(SlimModel pModel, TalkSelector pDisplay) {
		model = pModel;
		talkDisplay = pDisplay;
		init();
		pModel.getContacts().registerPeopleInListener(this);
	}
	
	private void init() {
		setLayout(new BorderLayout());
		setPreferredSize(new java.awt.Dimension(200, 100));
		
		peopleInBar = new JToolBar();
		peopleInBar.setFloatable(false);
		peopleInBar.setRollover(true);
		add(peopleInBar, BorderLayout.NORTH);
		{
			peopleInLabel = new JLabel();
			peopleInLabel.setText(Externalizer.getString("LANSLIM.32")); //$NON-NLS-1$
			peopleInBar.add(peopleInLabel);
		}
		{
			peopleInviteButton = new JButton();
			peopleInviteButton.setIcon(new SlimIcon("mail_add.png")); //$NON-NLS-1$
			peopleInviteButton.setActionCommand(PeopleInAcionCommand.INVITE);
			peopleInviteButton.setToolTipText(Externalizer.getString("LANSLIM.33")); //$NON-NLS-1$
			peopleInviteButton.addActionListener(this);
			peopleInBar.addSeparator();
			peopleInBar.add(peopleInviteButton);
		}
		{
			peopleExcludeButton = new JButton();
			peopleExcludeButton.setIcon(new SlimIcon("mail_remove.png")); //$NON-NLS-1$
			peopleExcludeButton.setActionCommand(PeopleInAcionCommand.EXCLUDE);
			peopleExcludeButton.setToolTipText(Externalizer.getString("LANSLIM.26")); //$NON-NLS-1$
			peopleExcludeButton.addActionListener(this);
			peopleInBar.addSeparator();
			peopleInBar.add(peopleExcludeButton);
		}
		peopleInAreaPane = new JScrollPane();
		add(peopleInAreaPane, BorderLayout.CENTER);
		{
			peopleInList = new JList();
			peopleInModel =  new DefaultListModel();
			peopleInList.setModel(peopleInModel);
			peopleInList.setCellRenderer(new PeopleInCellRenderer());
			peopleInList.setTransferHandler(new ContactTransferHandler(talkDisplay));
			peopleInList.setToolTipText(Externalizer.getString("LANSLIM.187"));  //$NON-NLS-1$
			peopleInAreaPane.setViewportView(peopleInList);
			
			popupMenu = new PeopleInPopupMenu(this);
	        //Add listener to the table so the popup menu can come up.
			peopleInList.addMouseListener(new PeopleInMouseListener(popupMenu, peopleInList));
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == PeopleInAcionCommand.INVITE) {
			SlimTalk st = talkDisplay.getDisplayedTalk();
			if (st != null) {
				NewTalkFrame lFrame = new NewTalkFrame((Frame)getRootPane().getParent(), 
						model, null, st);
				lFrame.pack();
				lFrame.setLocationRelativeTo(getRootPane().getParent());
				lFrame.setVisible(true);
			}
			else {
				JOptionPane.showMessageDialog(getRootPane().getParent(),
					    Externalizer.getString("LANSLIM.23"), //$NON-NLS-1$
					    Externalizer.getString("LANSLIM.28"), //$NON-NLS-1$
					    JOptionPane.WARNING_MESSAGE);
			}
		}
		else if (e.getActionCommand() == PeopleInAcionCommand.EXCLUDE) {
			if (peopleInList.getSelectedIndex() != -1) {
				SlimTalk st = talkDisplay.getDisplayedTalk();
				if (st != null) {
					try {
						Object[] excluded = peopleInList.getSelectedValues();
						for (int i = 0; i < excluded.length; i++) {
							if (excluded[i] instanceof SlimUserContact) {
								st.removePeople((SlimUserContact)excluded[i]);
							}
						}
					}
					catch (SlimException lE) {
						JOptionPane.showMessageDialog(getRootPane().getParent(),
							    Externalizer.getString("LANSLIM.34"), //$NON-NLS-1$
							    Externalizer.getString("LANSLIM.22"), //$NON-NLS-1$
							    JOptionPane.WARNING_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(getRootPane().getParent(),
						    Externalizer.getString("LANSLIM.35"), //$NON-NLS-1$
						    Externalizer.getString("LANSLIM.28"), //$NON-NLS-1$
						    JOptionPane.WARNING_MESSAGE);
				}
			}
			else {
				JOptionPane.showMessageDialog(getRootPane().getParent(),
					    Externalizer.getString("LANSLIM.17"), //$NON-NLS-1$
					    Externalizer.getString("LANSLIM.28"), //$NON-NLS-1$
					    JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	
	public void update(List pPeopleIn) {
		
		peopleInModel.clear();
		Iterator lIt = pPeopleIn.iterator(); 
		while (lIt.hasNext()) {
			SlimUserContact suc = (SlimUserContact)lIt.next();
			int i = 0;
			boolean indexreached = false;
			SlimUserContact psuc = null; 
			while (!indexreached && i < peopleInModel.size()) {
				psuc = (SlimUserContact)peopleInModel.get(i);
				if (suc.getName().compareToIgnoreCase(psuc.getName()) < 0) {
					indexreached = true;
					peopleInModel.insertElementAt(suc, i);
				}
				i++;
			}
			if (!indexreached) {
				peopleInModel.insertElementAt(suc, i);
			}
		}
	}
	
	public class PeopleInAcionCommand {

		public static final String INVITE = "invite"; //$NON-NLS-1$
		
		public static final String EXCLUDE = "exclude"; //$NON-NLS-1$
		

	}

	public void updateAvailabilities() {
		repaint();	
	}

}
