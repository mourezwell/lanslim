package com.oz.lanslim.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;
import javax.swing.text.html.HTMLEditorKit;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimTalk;
import com.oz.lanslim.model.SlimTalkListener;

public class TalkPane extends JSplitPane 
	implements ActionListener, SlimTalkListener, KeyListener {
	
	private JEditorPane talkArea;
	private JTextArea newMessageArea;
	private JScrollPane talkAreaPane;
	private JScrollPane messageAreaPane;
	private JPanel newMessagePane;
	private JToolBar messageToolbar;
	private JButton colorButton;
	private JButton sendButton;
	private JButton underlineButton;
	private JButton italicButton;
	private JButton boldButton;
	private JComboBox smileyCBox;
	private JComboBox sizeComboBox;

	private MainPane mainPane = null;
	private SlimTalk talkModel = null; 
	private String[] history = null; 
	private int historyIndex = 0; 
	private int historyCallIndex = 0; 
	
	public TalkPane(MainPane pPane, SlimTalk pTalk) {
		super();
		mainPane = pPane;
		talkModel = pTalk;
		init();
		talkModel.registerListener(this);
		setName(pTalk.getId());
		history = new String[10];
		historyIndex = 0;
		historyCallIndex = 0;
	}
	
	
	public SlimTalk getTalk() {
		return talkModel;
	}

	private void init() {

		this.setOrientation(JSplitPane.VERTICAL_SPLIT);
		{
			talkAreaPane = new JScrollPane();
			this.add(talkAreaPane, JSplitPane.TOP);
			{
				talkArea = new JEditorPane();
				talkArea.setEditorKit(new HTMLEditorKit());
				talkArea.setEditable(false);
				talkAreaPane.setViewportView(talkArea);
				talkArea.setText("<html>" + talkModel.getText() + "</html>");
				talkArea.addHyperlinkListener(new SlimHyperlinkListener());
				talkArea.setTransferHandler(new ContactTransferHandler(talkModel));
				talkAreaPane.setPreferredSize(new java.awt.Dimension(310, 200));
			}
		}
		{
			newMessagePane = new JPanel();
			this.add(newMessagePane, JSplitPane.BOTTOM);
			BorderLayout lLayout = new BorderLayout();
			newMessagePane.setLayout(lLayout);
			{
				messageToolbar = new JToolBar();
				messageToolbar.setFloatable(false);
				messageToolbar.setRollover(true);
				newMessagePane.add(messageToolbar, BorderLayout.NORTH);
				{
					boldButton = new JButton();
					boldButton.setIcon(new SlimIcon("bold.png"));
					boldButton.addActionListener(this);
					boldButton.setActionCommand(TalkPaneActionCommand.BOLD);
					boldButton.setToolTipText("Bold");
					messageToolbar.add(boldButton);
				}
				{
					italicButton = new JButton();
					italicButton.setIcon(new SlimIcon("italic.png"));
					italicButton.addActionListener(this);
					italicButton.setActionCommand(TalkPaneActionCommand.ITALIC);
					italicButton.setToolTipText("Italic");
					messageToolbar.add(italicButton);
				}
				{
					underlineButton = new JButton();
					underlineButton.setIcon(new SlimIcon("underline.png"));
					underlineButton.addActionListener(this);
					underlineButton.setActionCommand(TalkPaneActionCommand.UNDERLINE);
					underlineButton.setToolTipText("Underline");
					messageToolbar.add(underlineButton);
				}
				{
					ComboBoxModel sizeComboBoxModel = 
						new DefaultComboBoxModel(
								new String[] { "1", "2", "3", "4", "5", "6", "7" });
					sizeComboBox = new JComboBox();
					sizeComboBox.setModel(sizeComboBoxModel);
					sizeComboBox.setSelectedIndex(3);
					sizeComboBox.setMaximumSize(new Dimension(40, 20));
					sizeComboBox.addActionListener(this);
					sizeComboBox.setActionCommand(TalkPaneActionCommand.SIZE);
					sizeComboBox.setToolTipText("Size (Html style, default is 4)");
			        messageToolbar.addSeparator();
					messageToolbar.add(sizeComboBox);
				}
				{
					colorButton = new JButton();
					colorButton.setText("<html><font color=\"" + talkModel.getMessageFontColor() 
							+ "\"><b>A</b></font></html>");
					colorButton.setMaximumSize(new Dimension(20, 20));
					colorButton.setMinimumSize(new Dimension(20, 20));
					colorButton.addActionListener(this); 
					colorButton.setActionCommand(TalkPaneActionCommand.COLOR);
					colorButton.setToolTipText("Color");
					messageToolbar.addSeparator();
					messageToolbar.add(colorButton);
				}
				{
					int smileyNb = 14;
					Integer[] intArray = new Integer[smileyNb];
					for (int i = 0; i < smileyNb; i++) {
						intArray[i] = new Integer(i);
					}
					smileyCBox = new JComboBox(intArray);
			        smileyCBox.setRenderer(new SmileyComboBoxRenderer());
			        smileyCBox.setMaximumRowCount(5);
			        smileyCBox.setMaximumSize(new Dimension(80, 24));
			        smileyCBox.addActionListener(this);
			        smileyCBox.setActionCommand(TalkPaneActionCommand.SMILEY);
			        smileyCBox.setToolTipText("Smileys");
			        messageToolbar.addSeparator();
					messageToolbar.add(smileyCBox);
				}
				{
					sendButton = new JButton();
					sendButton.setIcon(new SlimIcon("next.png"));
					sendButton.addActionListener(this);
					sendButton.setActionCommand(TalkPaneActionCommand.SEND);
					sendButton.setToolTipText("Send Message (Enter)");
					messageToolbar.add(Box.createHorizontalGlue());
					messageToolbar.add(sendButton);
				}
			}
			{
				messageAreaPane = new JScrollPane();
				newMessagePane.add(messageAreaPane, BorderLayout.CENTER);
				{
					newMessageArea = new JTextArea();
					messageAreaPane.setViewportView(newMessageArea);
					newMessageArea.setText("");
					newMessageArea.setToolTipText("Enter to send message, Alt+Enter to insert newline, Shift+Up (+Down) To browse your message history");
					newMessageArea.addKeyListener(this);
					newMessageArea.setLineWrap(true);
					newMessageArea.setWrapStyleWord(true);
				}
			}
		}
		newMessageArea.requestFocus();
	}

	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand() == TalkPaneActionCommand.COLOR) {
		    Color newColor = JColorChooser.showDialog(
		    		TalkPane.this,
		            "Choose Text Color",
		            Color.black);
			 if (newColor != null) {
				 talkModel.setMessageFontColor(toDoubleHex(newColor.getRed()) 
						 + toDoubleHex(newColor.getGreen()) 
						 + toDoubleHex(newColor.getBlue()));
				 colorButton.setText("<html><font color=\"" + talkModel.getMessageFontColor() 
						 + "\"><b>A</b></font></html>");
			 }
	    	 newMessageArea.requestFocus();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.BOLD) {
			if (newMessageArea.getSelectedText() == null) {
				newMessageArea.setText(newMessageArea.getText() + "<b> </b>");
	        }
			else {
				int start = newMessageArea.getSelectionStart();
				int end = newMessageArea.getSelectionEnd();
				String before = newMessageArea.getText().substring(0, start);
				String after = newMessageArea.getText().substring(end);
				newMessageArea.setText(before + "<b>" + newMessageArea.getSelectedText() + "</b>" + after);
			}
			newMessageArea.requestFocus();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.ITALIC) {
			if (newMessageArea.getSelectedText() == null) {
				newMessageArea.setText(newMessageArea.getText() + "<i> </i>");
	        }
			else {
				int start = newMessageArea.getSelectionStart();
				int end = newMessageArea.getSelectionEnd();
				String before = newMessageArea.getText().substring(0, start);
				String after = newMessageArea.getText().substring(end);
				newMessageArea.setText(before + "<i>" + newMessageArea.getSelectedText() + "</i>" + after);
			}
			newMessageArea.requestFocus();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.UNDERLINE) {
			if (newMessageArea.getSelectedText() == null) {
				newMessageArea.setText(newMessageArea.getText() + "<u> </u>");
	        }
			else {
				int start = newMessageArea.getSelectionStart();
				int end = newMessageArea.getSelectionEnd();
				String before = newMessageArea.getText().substring(0, start);
				String after = newMessageArea.getText().substring(end);
				newMessageArea.setText(before + "<u>" + newMessageArea.getSelectedText() + "</u>" + after);
			}
			newMessageArea.requestFocus();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.SEND) {
			send();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.SMILEY) {
    		int lPos = newMessageArea.getSelectionStart();
			String before = newMessageArea.getText().substring(0, lPos);
			String after = newMessageArea.getText().substring(lPos);
    		newMessageArea.setText(before + "$" + smileyCBox.getSelectedIndex() + "$" + after);
    		newMessageArea.requestFocus();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.SIZE) {
			talkModel.setMessageFontSize(sizeComboBox.getSelectedItem().toString());
    		newMessageArea.requestFocus();
		}

	}

	protected static String toDoubleHex(int i) {
		String result = Integer.toHexString(i);
		if (result.length() == 1) {
			return ("0" + result);
		}
		return result.toUpperCase();
	}

	public void notifyNewTalk(SlimTalk pTalk) {
		// job is done directly in main pane
	}

	private void send() {
		try {
			talkModel.sendUpdateTalkMessage(newMessageArea.getText(), true);
			history[historyIndex] = newMessageArea.getText();
			if (historyIndex < 9) {
				historyIndex++;
			}
			else {
				historyIndex = 0;
			}
			historyCallIndex = historyIndex;
			newMessageArea.setText("");
		}
		catch (SlimException se) {
			JOptionPane.showMessageDialog(this,
			    "Message could not be sent  please check your settings",
			    "Network Error",
			    JOptionPane.ERROR_MESSAGE);
		}
		newMessageArea.requestFocus();
	}
	
	public void notifyTextTalkUpdate(SlimTalk pTalk) {
		talkArea.setText("<html>" + pTalk.getText() + "</html>");
		talkArea.setCaretPosition(talkArea.getDocument().getLength());
		mainPane.notifyTextTalkUpdate(pTalk);
		
		if (talkModel.getPeopleIn().size() <= 1) {
			newMessageArea.setEnabled(false);
			sendButton.setEnabled(false);
			colorButton.setEnabled(false);
			underlineButton.setEnabled(false);
			italicButton.setEnabled(false);
			boldButton.setEnabled(false);
			smileyCBox.setEnabled(false);
			sizeComboBox.setEnabled(false);
		}
		else {
			newMessageArea.setEnabled(true);
			sendButton.setEnabled(true);
			colorButton.setEnabled(true);
			underlineButton.setEnabled(true);
			italicButton.setEnabled(true);
			boldButton.setEnabled(true);
			smileyCBox.setEnabled(true);
			sizeComboBox.setEnabled(true);
		}
	}

	private class TalkPaneActionCommand {

		public static final String COLOR = "color";
		
		public static final String BOLD = "bold";

		public static final String ITALIC = "italic";

		public static final String UNDERLINE = "underline";
		
		public static final String SEND = "send";
		
		public static final String SMILEY = "smiley";

		public static final String SIZE = "size";

	}

	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	        if ((e.getModifiers() & InputEvent.ALT_MASK) != 0) {
        		int lPos = newMessageArea.getSelectionStart();
				String before = newMessageArea.getText().substring(0, lPos);
				String after = newMessageArea.getText().substring(lPos);
				newMessageArea.setText(before + "\n" + after);
	        }
	        else {
	        	send();		        
        	}
		}
		else if (e.getKeyCode() == KeyEvent.VK_UP 
				&& ((e.getModifiers() & InputEvent.SHIFT_MASK) != 0)) {
			if (historyCallIndex == 0) {
				historyCallIndex = 9;
	        }
			else {
				historyCallIndex--;
			}
			newMessageArea.setText(history[historyCallIndex]);
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN 
				&& ((e.getModifiers() & InputEvent.SHIFT_MASK) != 0)) {
			if (historyCallIndex == 9) {
				historyCallIndex = 0;
	        }
			else {
				historyCallIndex++;
			}
			newMessageArea.setText(history[historyCallIndex]);
		}

	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if ((e.getModifiers() & InputEvent.ALT_MASK) == 0) {
				newMessageArea.setText("");
			}
		}
	}

	public void keyTyped(KeyEvent e) {
		// nothing to do here
	}

	protected void export(File pFile) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(pFile));
		writer.write("#Lanslim Talk Export \n");
		writer.write("#Date : " + new Date() + "\n");
		writer.write("#Talk Title : " + talkModel.getTitle() + "\n");
		writer.write("#People In : " + talkModel.getPeopleInListAsString() + "\n");
		writer.write("#Talk Text :\n");
		Clipboard cb = new Clipboard("Lanslim");
		talkArea.select(0, talkArea.getSelectionEnd()); // select All
		talkArea.getTransferHandler().exportToClipboard(talkArea, cb, TransferHandler.COPY);
		Transferable t = cb.getContents(this);
        try {
            String str = (String)t.getTransferData(DataFlavor.stringFlavor);
            int last = 0;
            int c = str.indexOf('[', last);
            while (c > 0) {
	            int start = str.substring(0, c-1).lastIndexOf(' ');
	            if (start >= 0) {
	                writer.write(str.substring(last+1, start) + "\n");
	            	last = start;
	            }
	            c = str.indexOf('[', c+1);
            }
            writer.write(str.substring(last+1) + "\n");
            
        } catch (UnsupportedFlavorException ufe) {
        	 writer.write(ufe.getMessage());
        }
		writer.flush();
		writer.close();
	}


	
}
