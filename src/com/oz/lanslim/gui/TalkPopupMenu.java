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
		JMenuItem renameTalkMenuItem = new JMenuItem("Edit Talk", new SlimIcon("note_edit.png"));
    	renameTalkMenuItem.addActionListener(this);
    	renameTalkMenuItem.setActionCommand(TalkActionCommand.TALK_EDIT);
    	add(renameTalkMenuItem);
    	
    	JMenuItem exportTalkMenuItem = new JMenuItem("Export Talk To File", new SlimIcon("page_next.png"));
		exportTalkMenuItem.setActionCommand(TalkActionCommand.EXPORT_TALK);
		exportTalkMenuItem.addActionListener(this);
		add(exportTalkMenuItem);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == TalkActionCommand.TALK_EDIT) {
			TalkPane tp = (TalkPane)talkDisplay.getSelectedComponent();
			SlimTalk st = tp.getTalk();
			NewTalkFrame lFrame = new NewTalkFrame((Frame)this.getRootPane().getParent(), 
					model, null, st);
			lFrame.pack();
			lFrame.setLocationRelativeTo(this);
			lFrame.setVisible(true);
		}
		else if (e.getActionCommand() == TalkActionCommand.EXPORT_TALK) {
			TalkPane tp = (TalkPane)talkDisplay.getSelectedComponent();
			FileDialog fileChooser = new FileDialog((Frame)this.getRootPane().getParent(), "Export Talk", FileDialog.SAVE);
			fileChooser.show();
            String lFileName = fileChooser.getFile();
            if (lFileName != null) {
            	File file = new File(fileChooser.getDirectory() + File.separator + lFileName);
                try {
                	tp.export(file);
                }
                catch (IOException ioe) {
    				JOptionPane.showMessageDialog(this,
					    "Unable to export talk due to : " + ioe + ":" + ioe.getMessage(),
					    "Action Error",
					    JOptionPane.ERROR_MESSAGE);
                }
            }
		}
	}
	
	public class TalkActionCommand {

		public static final String TALK_EDIT = "editTalk";

		public static final String EXPORT_TALK = "exportTalk";

	}

}
