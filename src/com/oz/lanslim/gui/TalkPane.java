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
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.html.HTMLEditorKit;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.HTMLConstants;
import com.oz.lanslim.model.SlimTalk;
import com.oz.lanslim.model.SlimTalkListener;

public class TalkPane extends JSplitPane 
	implements ActionListener, SlimTalkListener, KeyListener, CaretListener {
	
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

	private SlimTalkListener mainPane = null;
	private SlimTalk talkModel = null; 
	private String[] history = null; 
	private int historyIndex = 0; 
	private int historyCallIndex = 0; 
	
	private boolean underline = false;
	private boolean bold = false;
	private boolean italic = false;
	
	public TalkPane(SlimTalkListener pMainPane, SlimTalk pTalk) {
		super();
		mainPane = pMainPane;
		talkModel = pTalk;
		init();
		talkModel.registerListener(this);
		setName(pTalk.getId());
		history = new String[10];
		historyIndex = 0;
		historyCallIndex = 0;
		underline = pTalk.isDefaultUndeline();
		bold = pTalk.isDefaultBold();
		italic = pTalk.isDefaultItalic();
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
				talkArea.setText(HTMLConstants.HTML + talkModel.getText() + HTMLConstants.ENDHTML);
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
					boldButton.setBorder(SlimButtonBorder.getSelectedBorder(talkModel.isDefaultBold()));
			        messageToolbar.addSeparator();
					messageToolbar.add(boldButton);
				}
				{
					italicButton = new JButton();
					italicButton.setIcon(new SlimIcon("italic.png"));
					italicButton.addActionListener(this);
					italicButton.setActionCommand(TalkPaneActionCommand.ITALIC);
					italicButton.setToolTipText("Italic");
					italicButton.setBorder(SlimButtonBorder.getSelectedBorder(talkModel.isDefaultItalic()));
			        messageToolbar.addSeparator();
					messageToolbar.add(italicButton);
				}
				{
					underlineButton = new JButton();
					underlineButton.setIcon(new SlimIcon("underline.png"));
					underlineButton.addActionListener(this);
					underlineButton.setActionCommand(TalkPaneActionCommand.UNDERLINE);
					underlineButton.setToolTipText("Underline");
					underlineButton.setBorder(SlimButtonBorder.getSelectedBorder(talkModel.isDefaultUndeline()));
			        messageToolbar.addSeparator();
					messageToolbar.add(underlineButton);
				}
				{
					colorButton = new JButton();
					colorButton.setText(HTMLConstants.HTML + HTMLConstants.FONTCOLOR 
						+ talkModel.getMessageFontColor() + HTMLConstants.FONTSIZE 
						+ "5" + HTMLConstants.TAGEND + HTMLConstants.BOLD + "A" 
						+ HTMLConstants.ENDBOLD + HTMLConstants.ENDFONT + HTMLConstants.ENDHTML);
					colorButton.setMaximumSize(new Dimension(20, 20));
					colorButton.setMinimumSize(new Dimension(20, 20));
					colorButton.addActionListener(this); 
					colorButton.setActionCommand(TalkPaneActionCommand.COLOR);
					colorButton.setToolTipText("Color");
					colorButton.setBorder(SlimButtonBorder.getSelectedBorder(false));
					messageToolbar.addSeparator();
					messageToolbar.add(colorButton);
				}
				{
					ComboBoxModel sizeComboBoxModel = 
						new DefaultComboBoxModel(
								new String[] { "1", "2", "3", "4", "5", "6", "7" });
					sizeComboBox = new JComboBox();
					sizeComboBox.setModel(sizeComboBoxModel);
					sizeComboBox.setSelectedIndex(Integer.parseInt(talkModel.getMessageFontSize()) -1);
					sizeComboBox.setMaximumSize(new Dimension(40, 20));
					sizeComboBox.addActionListener(this);
					sizeComboBox.setActionCommand(TalkPaneActionCommand.SIZE);
					sizeComboBox.setToolTipText("Size (Html style, default is 4)");
			        messageToolbar.addSeparator();
					messageToolbar.add(sizeComboBox);
				}
				{
					Integer[] intArray = new Integer[SlimTalk.smileyText.length];
					for (int i = 0; i < SlimTalk.smileyText.length; i++) {
						intArray[i] = new Integer(i);
					}
					smileyCBox = new JComboBox(intArray);
			        smileyCBox.setRenderer(new SmileyComboBoxRenderer(true));
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
					newMessageArea.setToolTipText("Enter to send message, Alt+Enter to insert newline, Shift+Up (+Down) To browse your message history");
					newMessageArea.addKeyListener(this);
					newMessageArea.setLineWrap(true);
					newMessageArea.setWrapStyleWord(true);
					newMessageArea.setText("");
					newMessageArea.addCaretListener(this);
					if (talkModel.isDefaultBold()) {
						newMessageArea.setText(newMessageArea.getText() + HTMLConstants.BOLD);
					}
					if (talkModel.isDefaultItalic()) {
						newMessageArea.setText(newMessageArea.getText() + HTMLConstants.ITALIC);
					}
					if (talkModel.isDefaultUndeline()) {
						newMessageArea.setText(newMessageArea.getText() + HTMLConstants.UNDERLINE);
					}
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
				 colorButton.setText(HTMLConstants.HTML + HTMLConstants.FONTCOLOR 
							+ talkModel.getMessageFontColor() + HTMLConstants.FONTSIZE 
							+ "5" + HTMLConstants.TAGEND + HTMLConstants.BOLD + "A" 
							+ HTMLConstants.ENDBOLD + HTMLConstants.ENDFONT + HTMLConstants.ENDHTML);
			 }
	    	 newMessageArea.requestFocus();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.SIZE) {
			talkModel.setMessageFontSize(sizeComboBox.getSelectedItem().toString());
    		newMessageArea.requestFocus();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.BOLD) {
			styleButtonAction(bold, HTMLConstants.BOLD, HTMLConstants.ENDBOLD);
			bold = !bold;
			boldButton.setBorder(SlimButtonBorder.getSelectedBorder(bold));
			newMessageArea.requestFocus();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.ITALIC) {
			styleButtonAction(italic, HTMLConstants.ITALIC, HTMLConstants.ENDITALIC);
			italic = !italic;
			italicButton.setBorder(SlimButtonBorder.getSelectedBorder(italic));
			newMessageArea.requestFocus();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.UNDERLINE) {
			styleButtonAction(underline, HTMLConstants.UNDERLINE, HTMLConstants.ENDUNDERLINE);
			underline = !underline;
			underlineButton.setBorder(SlimButtonBorder.getSelectedBorder(underline));
			newMessageArea.requestFocus();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.SEND) {
			send();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.SMILEY) {
    		int lPos = newMessageArea.getSelectionStart();
			String before = newMessageArea.getText().substring(0, lPos);
			String after = newMessageArea.getText().substring(lPos);
    		newMessageArea.setText(before + SlimTalk.smileyText[smileyCBox.getSelectedIndex()] + after);
    		newMessageArea.setCaretPosition(lPos 
    				+ SlimTalk.smileyText[smileyCBox.getSelectedIndex()].length());
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
			if (talkModel.isDefaultBold()) {
				newMessageArea.setText(newMessageArea.getText() + HTMLConstants.BOLD);
			}
			if (talkModel.isDefaultItalic()) {
				newMessageArea.setText(newMessageArea.getText() + HTMLConstants.ITALIC);
			}
			if (talkModel.isDefaultUndeline()) {
				newMessageArea.setText(newMessageArea.getText() + HTMLConstants.UNDERLINE);
			}
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
		talkArea.setText(HTMLConstants.HTML + pTalk.getText() + HTMLConstants.ENDHTML);
		talkArea.setCaretPosition(talkArea.getDocument().getLength());
		mainPane.notifyTextTalkUpdate(pTalk);
		
		if (talkModel.getPeopleIn().size() <= 1) {
			newMessageArea.setEnabled(false);
			sendButton.setEnabled(false);
			colorButton.setEnabled(false);
			colorButton.setText(HTMLConstants.HTML + HTMLConstants.FONTCOLOR 
					+ HTMLConstants.GREY + HTMLConstants.FONTSIZE 
					+ "5" + HTMLConstants.TAGEND + HTMLConstants.BOLD + "A" 
					+ HTMLConstants.ENDBOLD + HTMLConstants.ENDFONT + HTMLConstants.ENDHTML);
			underlineButton.setEnabled(false);
			italicButton.setEnabled(false);
			boldButton.setEnabled(false);
	        smileyCBox.setRenderer(new SmileyComboBoxRenderer(false));
			smileyCBox.setEnabled(false);
			sizeComboBox.setEnabled(false);
		}
		else {
			newMessageArea.setEnabled(true);
			sendButton.setEnabled(true);
			colorButton.setEnabled(true);
			colorButton.setText(HTMLConstants.HTML + HTMLConstants.FONTCOLOR 
					+ talkModel.getMessageFontColor() + HTMLConstants.FONTSIZE 
					+ "5" + HTMLConstants.TAGEND + HTMLConstants.BOLD + "A" 
					+ HTMLConstants.ENDBOLD + HTMLConstants.ENDFONT + HTMLConstants.ENDHTML);
			underlineButton.setEnabled(true);
			italicButton.setEnabled(true);
			boldButton.setEnabled(true);
	        smileyCBox.setRenderer(new SmileyComboBoxRenderer(true));
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
				newMessageArea.setCaretPosition(before.length());
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


	public void caretUpdate(CaretEvent e) {
		
		String s = newMessageArea.getText().substring(0, Math.min(e.getDot(), e.getMark()));
		bold = s.lastIndexOf(HTMLConstants.BOLD) > s.lastIndexOf(HTMLConstants.ENDBOLD);
		boldButton.setBorder(SlimButtonBorder.getSelectedBorder(bold));
		italic = s.lastIndexOf(HTMLConstants.ITALIC) > s.lastIndexOf(HTMLConstants.ENDITALIC);
		italicButton.setBorder(SlimButtonBorder.getSelectedBorder(italic));
		underline = s.lastIndexOf(HTMLConstants.UNDERLINE) > s.lastIndexOf(HTMLConstants.ENDUNDERLINE);
		underlineButton.setBorder(SlimButtonBorder.getSelectedBorder(underline));
	}

	private void styleButtonAction(boolean currValue, String ifFalse, String ifTrue) {

		if (newMessageArea.getSelectedText() == null) {
			int start = newMessageArea.getCaret().getDot();
			String before = newMessageArea.getText().substring(0, start);
			String after = newMessageArea.getText().substring(start);
			if (!currValue) {
				newMessageArea.setText(before + ifFalse + after);
				newMessageArea.setCaretPosition(start + ifFalse.length());
			}
			else {
				newMessageArea.setText(before + ifTrue + after);
				newMessageArea.setCaretPosition(start + ifTrue.length());
			}
        }
		else {
			int start = newMessageArea.getSelectionStart();
			int end = newMessageArea.getSelectionEnd();
			String before = newMessageArea.getText().substring(0, start);
			String after = newMessageArea.getText().substring(end);
			if (!currValue) {
				newMessageArea.setText(before + ifFalse + newMessageArea.getSelectedText() + ifTrue + after);
			}
			else {
				newMessageArea.setText(before + ifTrue + newMessageArea.getSelectedText() + ifFalse + after);
			}
			newMessageArea.setCaretPosition(end + ifFalse.length() + ifTrue.length());
		}
	}
	
}
