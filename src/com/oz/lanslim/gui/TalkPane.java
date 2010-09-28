package com.oz.lanslim.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.html.HTMLEditorKit;

import com.inet.jortho.SpellChecker;
import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.StringConstants;
import com.oz.lanslim.model.HTMLConstants;
import com.oz.lanslim.model.SlimTalk;
import com.oz.lanslim.model.SlimTalkListener;

public class TalkPane extends JSplitPane 
	implements ActionListener, SlimTalkListener, KeyListener, CaretListener {

	private static final String COLOR_BUTTON_SIZE = "5"; //$NON-NLS-1$
	private static final String COLOR_BUTTON_TEXT = "A"; //$NON-NLS-1$
	private static final String COMMENT_PREFIX = "#"; //$NON-NLS-1$
	
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
	private JButton escapeXMLButton;
	private JButton bellButton;
	private JComboBox smileyCBox;
	private JComboBox sizeComboBox;
	private JButton fontButton;
	private JButton attachmentButton;
	private JButton spellingButton;
	
	private SlimTalkListener mainPane = null;
	private SlimTalk talkModel = null; 
	private String[] history = null; 
	private int historyIndex = 0; 
	private int historyCallIndex = 0; 
	
	private boolean underline = false;
	private boolean bold = false;
	private boolean italic = false;
	private boolean spelling = false;
	private File attachment = null;
	
	public TalkPane(SlimTalkListener pMainPane, SlimTalk pTalk) {
		super();
		mainPane = pMainPane;
		talkModel = pTalk;
		underline = pTalk.isDefaultUndeline();
		bold = pTalk.isDefaultBold();
		italic = pTalk.isDefaultItalic();
		setName(pTalk.getId());
		init();
		talkModel.registerListener(this);
		history = new String[10];
		historyIndex = 0;
		historyCallIndex = 0;
	}
	
	
	public SlimTalk getTalk() {
		return talkModel;
	}

	private void init() {

		setOrientation(JSplitPane.VERTICAL_SPLIT);
		{
			talkAreaPane = new JScrollPane();
			add(talkAreaPane, JSplitPane.TOP);
			{
				talkArea = new JEditorPane();
				talkArea.setEditorKit(new HTMLEditorKit());
				talkArea.setEditable(false);
				talkAreaPane.setViewportView(talkArea);
				talkArea.setText(HTMLConstants.HTML + talkModel.getText() + HTMLConstants.ENDHTML);
				talkArea.addHyperlinkListener(new SlimHyperlinkListener(this));
				talkArea.setTransferHandler(new ContactTransferHandler(talkModel));
				talkAreaPane.setPreferredSize(new java.awt.Dimension(310, 200));
			}
		}
		{
			newMessagePane = new JPanel();
			add(newMessagePane, JSplitPane.BOTTOM);
			BorderLayout lLayout = new BorderLayout();
			newMessagePane.setLayout(lLayout);
			{
				messageToolbar = new JToolBar();
				messageToolbar.setFloatable(false);
				messageToolbar.setRollover(true);
				newMessagePane.add(messageToolbar, BorderLayout.NORTH);
				{
					boldButton = new JButton();
					boldButton.setIcon(new SlimIcon("bold.png")); //$NON-NLS-1$
					boldButton.addActionListener(this);
					boldButton.setActionCommand(TalkPaneActionCommand.BOLD);
					boldButton.setToolTipText(Externalizer.getString("LANSLIM.91")); //$NON-NLS-1$
					boldButton.setBorder(SlimButtonBorder.getSelectedBorder(talkModel.isDefaultBold()));
			        messageToolbar.addSeparator();
					messageToolbar.add(boldButton);
				}
				{
					italicButton = new JButton();
					italicButton.setIcon(new SlimIcon("italic.png")); //$NON-NLS-1$
					italicButton.addActionListener(this);
					italicButton.setActionCommand(TalkPaneActionCommand.ITALIC);
					italicButton.setToolTipText(Externalizer.getString("LANSLIM.92")); //$NON-NLS-1$
					italicButton.setBorder(SlimButtonBorder.getSelectedBorder(talkModel.isDefaultItalic()));
			        messageToolbar.addSeparator();
					messageToolbar.add(italicButton);
				}
				{
					underlineButton = new JButton();
					underlineButton.setIcon(new SlimIcon("underline.png")); //$NON-NLS-1$
					underlineButton.addActionListener(this);
					underlineButton.setActionCommand(TalkPaneActionCommand.UNDERLINE);
					underlineButton.setToolTipText(Externalizer.getString("LANSLIM.93")); //$NON-NLS-1$
					underlineButton.setBorder(SlimButtonBorder.getSelectedBorder(talkModel.isDefaultUndeline()));
			        messageToolbar.addSeparator();
					messageToolbar.add(underlineButton);
				}
				{
					colorButton = new JButton();
					colorButton.setText(HTMLConstants.HTML + HTMLConstants.FONTCOLOR 
						+ talkModel.getMessageFontColor() + HTMLConstants.FONTSIZE 
						+ COLOR_BUTTON_SIZE + HTMLConstants.TAGEND + HTMLConstants.BOLD + COLOR_BUTTON_TEXT
						+ HTMLConstants.ENDBOLD + HTMLConstants.ENDFONT + HTMLConstants.ENDHTML);
					colorButton.setMaximumSize(new Dimension(20, 20));
					colorButton.setMinimumSize(new Dimension(20, 20));
					colorButton.addActionListener(this); 
					colorButton.setActionCommand(TalkPaneActionCommand.COLOR);
					colorButton.setToolTipText(Externalizer.getString("LANSLIM.94")); //$NON-NLS-1$
					colorButton.setBorder(SlimButtonBorder.getSelectedBorder(false));
					messageToolbar.addSeparator();
					messageToolbar.add(colorButton);
				}
				{
					fontButton = new JButton();
					fontButton.setIcon(new SlimIcon("font.png")); //$NON-NLS-1$
					fontButton.addActionListener(this); 
					fontButton.setActionCommand(TalkPaneActionCommand.FACE);
					fontButton.setToolTipText(Externalizer.getString("LANSLIM.237", talkModel.getMessageFontFace())); //$NON-NLS-1$
					fontButton.setBorder(SlimButtonBorder.getSelectedBorder(false));
					messageToolbar.addSeparator();
					messageToolbar.add(fontButton);
				}
				{
					ComboBoxModel sizeComboBoxModel = 
						new DefaultComboBoxModel(
								new String[] { "1", "2", "3", "4", "5", "6", "7" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
					sizeComboBox = new JComboBox();
					sizeComboBox.setModel(sizeComboBoxModel);
					sizeComboBox.setSelectedIndex(Integer.parseInt(talkModel.getMessageFontSize()) -1);
					sizeComboBox.setMaximumSize(new Dimension(40, 22));
					sizeComboBox.addActionListener(this);
					sizeComboBox.setActionCommand(TalkPaneActionCommand.SIZE);
					sizeComboBox.setToolTipText(Externalizer.getString("LANSLIM.95")); //$NON-NLS-1$
			        messageToolbar.addSeparator();
					messageToolbar.add(sizeComboBox);
				}
				{
					Integer[] intArray = new Integer[SlimTalk.SMILEY_TEXT.length];
					for (int i = 0; i < SlimTalk.SMILEY_TEXT.length; i++) {
						intArray[i] = new Integer(i);
					}
					smileyCBox = new JComboBox(intArray);
			        smileyCBox.setRenderer(new SmileyComboBoxRenderer(true));
			        smileyCBox.setMaximumRowCount(5);
			        smileyCBox.setMaximumSize(new Dimension(80, 22));
			        smileyCBox.addActionListener(this);
			        smileyCBox.setActionCommand(TalkPaneActionCommand.SMILEY);
			        smileyCBox.setToolTipText(Externalizer.getString("LANSLIM.96")); //$NON-NLS-1$
			        messageToolbar.addSeparator();
					messageToolbar.add(smileyCBox);
				}
				{
					escapeXMLButton = new JButton();
					escapeXMLButton.setIcon(new SlimIcon("xml_accept.png")); //$NON-NLS-1$
					escapeXMLButton.addActionListener(this);
					escapeXMLButton.setActionCommand(TalkPaneActionCommand.AUTO_ESCAPE);
					escapeXMLButton.setToolTipText(Externalizer.getString("LANSLIM.218")); //$NON-NLS-1$
					escapeXMLButton.setBorder(SlimButtonBorder.getSelectedBorder(false));
			        messageToolbar.addSeparator();
					messageToolbar.add(escapeXMLButton);
				}
				{
					spellingButton = new JButton();
					spellingButton.setIcon(new SlimIcon("search_cancel.png")); //$NON-NLS-1$
					spellingButton.addActionListener(this);
					spellingButton.setActionCommand(TalkPaneActionCommand.SPELLING);
					spellingButton.setToolTipText(Externalizer.getString("LANSLIM.247")); //$NON-NLS-1$
					spellingButton.setBorder(SlimButtonBorder.getSelectedBorder(false));
			        messageToolbar.addSeparator();
					messageToolbar.add(spellingButton);
				}
				{
					bellButton = new JButton();
					bellButton.setIcon(new SlimIcon("sound.png")); //$NON-NLS-1$
					bellButton.addActionListener(this);
					bellButton.setActionCommand(TalkPaneActionCommand.SOUND);
					bellButton.setToolTipText(Externalizer.getString("LANSLIM.225")); //$NON-NLS-1$
					bellButton.setBorder(SlimButtonBorder.getSelectedBorder(false));
			        messageToolbar.addSeparator();
					messageToolbar.add(bellButton);
				}
				{
					attachmentButton = new JButton();
					attachmentButton.setIcon(new SlimIcon("attachment.png")); //$NON-NLS-1$
					attachmentButton.addActionListener(this);
					attachmentButton.setActionCommand(TalkPaneActionCommand.ATTACHMENT);
					attachmentButton.setToolTipText(Externalizer.getString("LANSLIM.216")); //$NON-NLS-1$
					attachmentButton.setBorder(SlimButtonBorder.getSelectedBorder(false));
			        messageToolbar.addSeparator();
					messageToolbar.add(attachmentButton);
				}
				{
					sendButton = new JButton();
					sendButton.setIcon(new SlimIcon("next.png")); //$NON-NLS-1$
					sendButton.addActionListener(this);
					sendButton.setActionCommand(TalkPaneActionCommand.SEND);
					sendButton.setToolTipText(Externalizer.getString("LANSLIM.97")); //$NON-NLS-1$
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
					newMessageArea.setToolTipText(Externalizer.getString("LANSLIM.98")); //$NON-NLS-1$
					newMessageArea.addKeyListener(this);
					newMessageArea.setLineWrap(true);
					newMessageArea.setWrapStyleWord(true);
					newMessageArea.setText(StringConstants.EMPTY);
					newMessageArea.addCaretListener(this);
					newMessageArea.addMouseListener(new EscapeXMLCharMouseListener(this));
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
		
		if (e.getActionCommand() == TalkPaneActionCommand.SEND) {
			send();
			newMessageArea.requestFocus();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.COLOR) {
		    Color newColor = JColorChooser.showDialog(
		    		TalkPane.this,
		            Externalizer.getString("LANSLIM.99"), //$NON-NLS-1$
		            Color.black);
			 if (newColor != null) {
				 talkModel.setMessageFontColor(toDoubleHex(newColor.getRed()) 
						 + toDoubleHex(newColor.getGreen()) 
						 + toDoubleHex(newColor.getBlue()));
				 colorButton.setText(HTMLConstants.HTML + HTMLConstants.FONTCOLOR 
							+ talkModel.getMessageFontColor() + HTMLConstants.FONTSIZE 
							+ COLOR_BUTTON_SIZE + HTMLConstants.TAGEND + HTMLConstants.BOLD + COLOR_BUTTON_TEXT
							+ HTMLConstants.ENDBOLD + HTMLConstants.ENDFONT + HTMLConstants.ENDHTML);
			 }
	    	 newMessageArea.requestFocus();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.SIZE) {
			talkModel.setMessageFontSize(sizeComboBox.getSelectedItem().toString());
    		newMessageArea.requestFocus();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.FACE) {
			SlimFontChooser lFontDialog = new SlimFontChooser((Frame)getRootPane().getParent(), talkModel.getMessageFontFace());
			lFontDialog.pack();
			lFontDialog.setLocationRelativeTo(this);
			lFontDialog.setVisible(true);
			
			String lChosenFont = lFontDialog.getCurrentFont(); 
			talkModel.setMessageFontFace(lChosenFont);
			fontButton.setToolTipText(Externalizer.getString("LANSLIM.237", lChosenFont)); //$NON-NLS-1$
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
		else if (e.getActionCommand() == TalkPaneActionCommand.SMILEY) {
    		int lPos = newMessageArea.getSelectionStart();
			String before = newMessageArea.getText().substring(0, lPos);
			String after = newMessageArea.getText().substring(lPos);
    		newMessageArea.setText(before + SlimTalk.SMILEY_TEXT[smileyCBox.getSelectedIndex()] + after);
    		newMessageArea.setCaretPosition(lPos 
    				+ SlimTalk.SMILEY_TEXT[smileyCBox.getSelectedIndex()].length());
    		newMessageArea.requestFocus();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.ESCAPE_CHAR) {
			escapeXMLChar();
			newMessageArea.requestFocus();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.AUTO_ESCAPE) {
			boolean lEscape = !talkModel.isEscapeXMLchar();
			talkModel.setEscapeXMLChar(lEscape);
			if (lEscape) {
				escapeXMLButton.setIcon(new SlimIcon("xml.png")); //$NON-NLS-1$
				escapeXMLButton.setToolTipText(Externalizer.getString("LANSLIM.219")); //$NON-NLS-1$
			}
			else {
				escapeXMLButton.setIcon(new SlimIcon("xml_accept.png")); //$NON-NLS-1$
				escapeXMLButton.setToolTipText(Externalizer.getString("LANSLIM.218")); //$NON-NLS-1$
			}
			newMessageArea.requestFocus();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.SPELLING) {
			spelling = !spelling;
			if (spelling) {
				spellingButton.setIcon(new SlimIcon("search_cancel.png")); //$NON-NLS-1$
				spellingButton.setToolTipText(Externalizer.getString("LANSLIM.246")); //$NON-NLS-1$
		        SpellChecker.register( newMessageArea );
			}
			else {
				spellingButton.setIcon(new SlimIcon("search.png")); //$NON-NLS-1$
				spellingButton.setToolTipText(Externalizer.getString("LANSLIM.247")); //$NON-NLS-1$
		        SpellChecker.unregister( newMessageArea );
			}
			newMessageArea.requestFocus();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.SOUND) {
			try {
				talkModel.sendSound();
			}
			catch (SlimException se) {
				JOptionPane.showMessageDialog(getRootPane().getParent(),
				    Externalizer.getString("LANSLIM.45", se.getMessage()), //$NON-NLS-1$ //$NON-NLS-2$
				    Externalizer.getString("LANSLIM.22"), //$NON-NLS-1$
				    JOptionPane.ERROR_MESSAGE);
			}
			catch (IOException ioe) {
				JOptionPane.showMessageDialog(getRootPane().getParent(),
				    Externalizer.getString("LANSLIM.45", ioe.getMessage()), //$NON-NLS-1$ //$NON-NLS-2$
				    Externalizer.getString("LANSLIM.22"), //$NON-NLS-1$
				    JOptionPane.ERROR_MESSAGE);
			}
			newMessageArea.requestFocus();
		}
		else if (e.getActionCommand() == TalkPaneActionCommand.ATTACHMENT) {
			if (attachment == null) {
				
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = fc.showOpenDialog(this);
		        if (returnVal == JFileChooser.APPROVE_OPTION && fc.getSelectedFile() != null) {
		        	attachment = fc.getSelectedFile();
					attachmentButton.setIcon(new SlimIcon("attachment_cancel.png")); //$NON-NLS-1$
					attachmentButton.setToolTipText(Externalizer.getString("LANSLIM.217", attachment.getName())); //$NON-NLS-1$
				}
			}
			else {
				attachment = null;
				attachmentButton.setIcon(new SlimIcon("attachment.png")); //$NON-NLS-1$
				attachmentButton.setToolTipText(Externalizer.getString("LANSLIM.216")); //$NON-NLS-1$
			}
			newMessageArea.requestFocus();
		}

	}

	protected static String toDoubleHex(int i) {
		String result = Integer.toHexString(i);
		if (result.length() == 1) {
			return ("0" + result); //$NON-NLS-1$
		}
		return result.toUpperCase();
	}

	public void notifyNewTalk(SlimTalk pTalk) {
		// job is done directly in main pane
	}

	private void send() {
		try {
			String lMessage = newMessageArea.getText();
			talkModel.sendUpdateTalkMessage(lMessage, attachment);
			attachment = null;
			attachmentButton.setIcon(new SlimIcon("attachment.png")); //$NON-NLS-1$
			attachmentButton.setToolTipText(Externalizer.getString("LANSLIM.216")); //$NON-NLS-1$
			history[historyIndex] = lMessage;
			if (historyIndex < 9) {
				historyIndex++;
			}
			else {
				historyIndex = 0;
			}
			historyCallIndex = historyIndex;
			newMessageArea.setText(StringConstants.EMPTY);
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
			JOptionPane.showMessageDialog(getRootPane().getParent(),
			    Externalizer.getString("LANSLIM.45", se.getMessage()), //$NON-NLS-1$ //$NON-NLS-2$
			    Externalizer.getString("LANSLIM.22"), //$NON-NLS-1$
			    JOptionPane.ERROR_MESSAGE);
		}
		catch (IOException ioe) {
			JOptionPane.showMessageDialog(getRootPane().getParent(),
			    Externalizer.getString("LANSLIM.45", ioe.getMessage()), //$NON-NLS-1$ //$NON-NLS-2$
			    Externalizer.getString("LANSLIM.22"), //$NON-NLS-1$
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
					+ COLOR_BUTTON_SIZE + HTMLConstants.TAGEND + HTMLConstants.BOLD + COLOR_BUTTON_TEXT
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
					+ COLOR_BUTTON_SIZE + HTMLConstants.TAGEND + HTMLConstants.BOLD + COLOR_BUTTON_TEXT
					+ HTMLConstants.ENDBOLD + HTMLConstants.ENDFONT + HTMLConstants.ENDHTML);
			underlineButton.setEnabled(true);
			italicButton.setEnabled(true);
			boldButton.setEnabled(true);
	        smileyCBox.setRenderer(new SmileyComboBoxRenderer(true));
			smileyCBox.setEnabled(true);
			sizeComboBox.setEnabled(true);
		}
	}

	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	        if ((e.getModifiers() & InputEvent.ALT_MASK) != 0) {
        		int lPos = newMessageArea.getSelectionStart();
				String before = newMessageArea.getText().substring(0, lPos);
				String after = newMessageArea.getText().substring(lPos);
				newMessageArea.setText(before + StringConstants.NEW_LINE + after);
				newMessageArea.setCaretPosition(lPos + StringConstants.NEW_LINE.length());
	        }
	        else {
	        	send();		        
        	}
		}
		else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
			if (historyCallIndex == 0) {
				historyCallIndex = 9;
	        }
			else {
				historyCallIndex--;
			}
			newMessageArea.setText(history[historyCallIndex]);
		}
		else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
			if (historyCallIndex == 9) {
				historyCallIndex = 0;
	        }
			else {
				historyCallIndex++;
			}
			newMessageArea.setText(history[historyCallIndex]);
		}
		else if (e.getKeyCode() == KeyEvent.VK_B
				&& ((e.getModifiers() & InputEvent.CTRL_MASK) != 0)) {
			styleButtonAction(bold, HTMLConstants.BOLD, HTMLConstants.ENDBOLD);
			bold = !bold;
			boldButton.setBorder(SlimButtonBorder.getSelectedBorder(bold));
			newMessageArea.requestFocus();
		}
		else if (e.getKeyCode() == KeyEvent.VK_I
				&& ((e.getModifiers() & InputEvent.CTRL_MASK) != 0)) {
			styleButtonAction(italic, HTMLConstants.ITALIC, HTMLConstants.ENDITALIC);
			italic = !italic;
			italicButton.setBorder(SlimButtonBorder.getSelectedBorder(italic));
			newMessageArea.requestFocus();
		}
		else if (e.getKeyCode() == KeyEvent.VK_U
				&& ((e.getModifiers() & InputEvent.CTRL_MASK) != 0)) {
			styleButtonAction(underline, HTMLConstants.UNDERLINE, HTMLConstants.ENDUNDERLINE);
			underline = !underline;
			underlineButton.setBorder(SlimButtonBorder.getSelectedBorder(underline));
			newMessageArea.requestFocus();
		}
		else if (e.getKeyCode() == KeyEvent.VK_F1) {
			insertShortcut(0);
		}
		else if (e.getKeyCode() == KeyEvent.VK_F2) {
			insertShortcut(1);
		}
		else if (e.getKeyCode() == KeyEvent.VK_F3) {
			insertShortcut(2);
		}
		else if (e.getKeyCode() == KeyEvent.VK_F4) {
			insertShortcut(3);
		}
		else if (e.getKeyCode() == KeyEvent.VK_F5) {
			insertShortcut(4);
		}
		else if (e.getKeyCode() == KeyEvent.VK_F6) {
			insertShortcut(5);
		}
		else if (e.getKeyCode() == KeyEvent.VK_F7) {
			insertShortcut(6);
		}
		else if (e.getKeyCode() == KeyEvent.VK_F8) {
			insertShortcut(7);
		}
		else if (e.getKeyCode() == KeyEvent.VK_F9) {
			insertShortcut(8);
		}
		else if (e.getKeyCode() == KeyEvent.VK_F10) {
			insertShortcut(9);
		}
		else if (e.getKeyCode() == KeyEvent.VK_F11) {
			insertShortcut(10);
		}
		else if (e.getKeyCode() == KeyEvent.VK_F12) {
			insertShortcut(11);
		}
	}

	
	private void insertShortcut(int i) {

		if (newMessageArea.getSelectedText() == null) {
			int lPos = newMessageArea.getCaret().getDot();
			String before = newMessageArea.getText().substring(0, lPos);
			String after = newMessageArea.getText().substring(lPos);
			newMessageArea.setText(before + talkModel.getShortcut(i) + after);
			newMessageArea.setCaretPosition(lPos + talkModel.getShortcut(i).length());
		}
		else {
			int start = newMessageArea.getSelectionStart();
			int end = newMessageArea.getSelectionEnd();
			String before = newMessageArea.getText().substring(0, start);
			String after = newMessageArea.getText().substring(end);
			newMessageArea.setText(before + talkModel.getShortcut(i) + after);
			newMessageArea.setCaretPosition(start + talkModel.getShortcut(i).length());
		}
	}
	
 	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if ((e.getModifiers() & InputEvent.ALT_MASK) == 0) {
				newMessageArea.setText(StringConstants.EMPTY);
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

	public void keyTyped(KeyEvent e) {
		// nothing to do here
	}

	protected void export(File pFile) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(pFile));
		writer.write(COMMENT_PREFIX + Externalizer.getString("LANSLIM.100")); //$NON-NLS-1$
		writer.write(StringConstants.LINE_SEPARATOR);
		writer.write(COMMENT_PREFIX + Externalizer.getString("LANSLIM.101", new Date())); //$NON-NLS-1$
		writer.write(StringConstants.LINE_SEPARATOR);
		writer.write(COMMENT_PREFIX + Externalizer.getString("LANSLIM.46", talkModel.getTitle())); //$NON-NLS-1$
		writer.write(StringConstants.LINE_SEPARATOR);
		writer.write(COMMENT_PREFIX + Externalizer.getString("LANSLIM.102", talkModel.getPeopleInListAsString())); //$NON-NLS-1$
		writer.write(StringConstants.LINE_SEPARATOR);
		writer.write(COMMENT_PREFIX + Externalizer.getString("LANSLIM.103")); //$NON-NLS-1$
		writer.write(StringConstants.LINE_SEPARATOR);
		
		Clipboard cb = new Clipboard("Lanslim"); //$NON-NLS-1$
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
	                writer.write(str.substring(last+1, start) + StringConstants.LINE_SEPARATOR);
	            	last = start;
	            }
	            c = str.indexOf('[', c+1);
            }
            writer.write(str.substring(last+1) + StringConstants.LINE_SEPARATOR);
        } 
        catch (UnsupportedFlavorException ufe) {
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
	
	private class TalkPaneActionCommand {

		public static final String FACE = "face"; //$NON-NLS-1$

		public static final String COLOR = "color"; //$NON-NLS-1$
		
		public static final String BOLD = "bold"; //$NON-NLS-1$

		public static final String ITALIC = "italic"; //$NON-NLS-1$

		public static final String UNDERLINE = "underline"; //$NON-NLS-1$
		
		public static final String SEND = "send"; //$NON-NLS-1$
		
		public static final String SMILEY = "smiley"; //$NON-NLS-1$

		public static final String SIZE = "size"; //$NON-NLS-1$

		public static final String ESCAPE_CHAR = "escapeChar"; //$NON-NLS-1$

		public static final String AUTO_ESCAPE = "autoEscape"; //$NON-NLS-1$

		public static final String SOUND = "sound"; //$NON-NLS-1$

		public static final String ATTACHMENT = "attachment"; //$NON-NLS-1$

		public static final String SPELLING = "spelling"; //$NON-NLS-1$

	}

	private class EscapeXMLCharMouseListener extends MouseAdapter {
		
		EscapeXMLCharacterPopupMenu popupMenu = null;
		
		public EscapeXMLCharMouseListener(TalkPane pPane) {
			popupMenu = new EscapeXMLCharacterPopupMenu(pPane);
		}
		
		public void mouseReleased(MouseEvent e) {
	        if (e.isPopupTrigger()) {
	        	popupMenu.show(e.getComponent(), e.getX(), e.getY());
	        }
		}
		
	}

	private class EscapeXMLCharacterPopupMenu extends JPopupMenu {

		public EscapeXMLCharacterPopupMenu(TalkPane pPane) {
			JMenuItem userEditMenuItem = new JMenuItem(Externalizer.getString("LANSLIM.218")); //$NON-NLS-1$
	        userEditMenuItem.addActionListener(pPane);
	        userEditMenuItem.setActionCommand(TalkPaneActionCommand.ESCAPE_CHAR);
	        add(userEditMenuItem);
		}
	}
	
	private void escapeXMLChar() {
		
		int start = 0;
		int end = newMessageArea.getText().length();
		int pos = newMessageArea.getCaret().getDot();
		
		if (newMessageArea.getSelectedText() != null) {
			start = newMessageArea.getSelectionStart();
			end = newMessageArea.getSelectionEnd();
			pos = end;
		}
		String before = newMessageArea.getText().substring(0, start);
		String after = newMessageArea.getText().substring(end);
		String lBeforeReplace = newMessageArea.getText().substring(start, end);
		int count = 0;
		int from = 0;
		int last = 0;
		while (from < lBeforeReplace.length()) {
			last = lBeforeReplace.indexOf('<', from);
			if (last != -1) {
				count = count + 1;
				from = last + 1;
			}
			else {
				break;
			}
		}
		newMessageArea.setText(before + lBeforeReplace.replaceAll("<", "&lt;") + after); //$NON-NLS-1$ //$NON-NLS-2$
		newMessageArea.setCaretPosition(pos + + count * 3);
	}

	protected String downlaodLink(String pURL) {
		
		String lResult = null;
		String lTempDir = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
		String lTmpFileName = pURL.substring(HTMLConstants.FILE_PROTOCOL.length());
		String lTargetFileName = lTmpFileName.substring(0, lTmpFileName.lastIndexOf('.'));
		File lFinal = new File(talkModel.getDownloadDir(), lTargetFileName);
		File lTmp = new File(lTempDir, lTmpFileName); 
		if (lTmp.exists()) {
			boolean lConfirmed = !lFinal.exists();
			if (!lConfirmed) {
    			int lAnswer = JOptionPane.showConfirmDialog(getRootPane().getParent(), 
    					Externalizer.getString("LANSLIM.238", lTargetFileName, talkModel.getDownloadDir()), //$NON-NLS-1$
    					Externalizer.getString("LANSLIM.239"), //$NON-NLS-1$
    					JOptionPane.YES_NO_OPTION);
    			lConfirmed = (lAnswer == JOptionPane.YES_OPTION);
    			if (lConfirmed) {
    				boolean lSuccess = lFinal.delete();
    				if (!lSuccess) {
    					lConfirmed = false;
            			JOptionPane.showMessageDialog(getRootPane().getParent(), 
            					Externalizer.getString("LANSLIM.240", lTargetFileName, talkModel.getDownloadDir()), //$NON-NLS-1$
            					Externalizer.getString("LANSLIM.241"), //$NON-NLS-1$
            					JOptionPane.ERROR_MESSAGE);
    				}
    			}
			}
			if (lConfirmed) {
				boolean lSuccess = lTmp.renameTo(lFinal);
				if (!lSuccess || !lSuccess) {
        			JOptionPane.showMessageDialog(getRootPane().getParent(), 
        					Externalizer.getString("LANSLIM.240", lTargetFileName, talkModel.getDownloadDir()), //$NON-NLS-1$
        					Externalizer.getString("LANSLIM.241"), //$NON-NLS-1$
        					JOptionPane.ERROR_MESSAGE);
				}
				else {
					lResult = HTMLConstants.FILE_PROTOCOL + talkModel.getDownloadDir();
				}
			}
		}
		else if (lFinal.exists()) {
			lResult = HTMLConstants.FILE_PROTOCOL + talkModel.getDownloadDir();
		}
		else {
			JOptionPane.showMessageDialog(getRootPane().getParent(), 
					Externalizer.getString("LANSLIM.240", lTargetFileName, lTempDir), //$NON-NLS-1$
					Externalizer.getString("LANSLIM.241"), //$NON-NLS-1$
					JOptionPane.ERROR_MESSAGE);
		}
		return lResult;
	}
	
}
