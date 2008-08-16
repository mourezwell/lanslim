package com.oz.lanslim.gui;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimTalk;

public class TalkPopupMenu extends JPopupMenu implements ActionListener {

	private SlimModel model;
	private JTabbedPane talkDisplay;
	
	public TalkPopupMenu(SlimModel pModel, JTabbedPane pDisplay) {
		model = pModel;
		talkDisplay = pDisplay;
		init();
		
	}
	
	private void init() {
		JMenuItem renameTalkMenuItem = new JMenuItem(Externalizer.getString("LANSLIM.111"), new SlimIcon("note_edit.png")); //$NON-NLS-1$ //$NON-NLS-2$
    	renameTalkMenuItem.addActionListener(this);
    	renameTalkMenuItem.setActionCommand(TalkActionCommand.TALK_EDIT);
    	add(renameTalkMenuItem);
    	
    	JMenuItem exportTalkMenuItem = new JMenuItem(Externalizer.getString("LANSLIM.107"), new SlimIcon("page_next.png")); //$NON-NLS-1$ //$NON-NLS-2$
		exportTalkMenuItem.setActionCommand(TalkActionCommand.EXPORT_TALK);
		exportTalkMenuItem.addActionListener(this);
		add(exportTalkMenuItem);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == TalkActionCommand.TALK_EDIT) {
			TalkPane tp = (TalkPane)talkDisplay.getSelectedComponent();
			SlimTalk st = tp.getTalk();
			NewTalkFrame lFrame = new NewTalkFrame((Frame)getRootPane().getParent(), 
					model, null, st);
			lFrame.pack();
			lFrame.setLocationRelativeTo(getRootPane().getParent());
			lFrame.setVisible(true);
		}
		else if (e.getActionCommand() == TalkActionCommand.EXPORT_TALK) {
			TalkPane tp = (TalkPane)talkDisplay.getSelectedComponent();
			FileDialog fileChooser = new FileDialog((Frame)getRootPane().getParent(), Externalizer.getString("LANSLIM.108"), FileDialog.SAVE); //$NON-NLS-1$
			fileChooser.show();
            String lFileName = fileChooser.getFile();
            if (lFileName != null) {
            	File file = new File(fileChooser.getDirectory() + File.separator + lFileName);
                try {
                	tp.export(file);
                }
                catch (IOException ioe) {
    				JOptionPane.showMessageDialog(getRootPane().getParent(),
					    Externalizer.getString("LANSLIM.109", SlimLogger.shortFormatException(ioe)), //$NON-NLS-1$
					    Externalizer.getString("LANSLIM.110"), //$NON-NLS-1$
					    JOptionPane.ERROR_MESSAGE);
                }
            }
		}
	}
	
	public class TalkActionCommand {

		public static final String TALK_EDIT = "editTalk"; //$NON-NLS-1$

		public static final String EXPORT_TALK = "exportTalk"; //$NON-NLS-1$

	}

}
