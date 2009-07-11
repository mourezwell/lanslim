package com.oz.lanslim.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;

import com.oz.lanslim.Externalizer;

public class SlimHyperLink extends JLabel implements MouseListener {

	/** The URL for this hyperlink */
	private String url = null;

	/** The color representing the normal Hyperlink. Default color is blue */
	private Color normalColor;

	/** The color representing the visited Hyperlink. Default color is red. */
	private Color visitedColor;

	/** The default color for normal Hyperlink*/
	private static Color DEFAULTNORMAL = Color.blue;
	
	/** The default color for normal Hyperlink */
	private static Color DEFAULTVISITED = Color.decode("#840084");

	private SlimHyperlinkListener actionListener = null;

	// Popup menu to send URL to clipboard
	private HyperLinkPopupMenu popupMenu;

	/**
	 * Create a hyperlink with no identical URL target
	 * @param label Text of label
	 */
	public SlimHyperLink(String label) {
		this(label, label);
	}

	/**
	 * Create a hyperlink using the given URL. Set the normal color to the default.
	 * @param label Text of linklabel
	 * @param URLThe URL to associate with this linklabel
	 */
	public SlimHyperLink(String label, String URL) {
		this(label, URL, DEFAULTNORMAL);
	}

	/**
	 * Creates a hyperlink using the given URL with the specified color. Sets
	 * the visited color to the default.
	 * @param label Text of label
	 * @param URL Universal Resource Locator (URL)
	 * @param color Color of the normal hyperlink
	 */
	public SlimHyperLink(String label, String URL, Color normalcolor) {
		this(label, URL, normalcolor, DEFAULTVISITED);
	}

	/**
	 * Creates a hyperlink using the given URL with the specified color.
	 * @param label Text of label
	 * @param URL Universal Resource Locator (URL)
	 * @param color Color of normal hyperlink
	 * @param color Color of visited
	 */
	public SlimHyperLink(String label, String URL, Color normalcolor, Color visitedcolor) {
		super(label);
		setURL(URL);
		setNormalColor(normalcolor);
		setVisitedColor(visitedcolor);
		addMouseListener(this);
		popupMenu = new HyperLinkPopupMenu();
		actionListener = new SlimHyperlinkListener();
		setToolTipText(url);
	}

	/**
	 * Set the label for the HyperLink.
	 * @param newLabel the label to display
	 */
	public void setLabel(String newLabel) {
		super.setText(newLabel);
	}

	/**
	 * Get the label for the HyperLink.
	 * @return The label for this HyperLink
	 */
	public String getLabel() {
		return super.getText();
	}

	/**
	 * Sets the URL to associate with this hyperlink.
	 * @param newURL The associated URL.
	 */
	public void setURL(String newURL) {
		url = newURL;
	}

	/**
	 * Returns the URL associated with this hyperlink.
	 * @return The URL assosicated with this hyperlink as a String
	 */
	public String getURL() {
		return url;
	}

	/**
	 * Sets the visited color for this hyperlink. The visited color will appear
	 * when the link has been clicked as determined by the isClicked attribute.
	 * @param color Color of visited link
	 */
	public void setVisitedColor(Color newVisitedColor) {
		visitedColor = newVisitedColor;
		repaint();
	}

	/**
	 * Gets the visited color for this hyperlink.
	 * @return color Color of visited link
	 */
	public Color getVisitedColor() {
		return visitedColor;
	}

	/**
	 * Sets the normal color for this hyperlink. The normal color appears is
	 * used when the link is first and subsequently drawn, up until the point
	 * where it is actively clicked.
	 * @return color Color of normal hyperlink
	 */
	public void setNormalColor(Color newNormalColor) {
		normalColor = newNormalColor;
		setForeground(normalColor);
		repaint();
	}

	/**
	 * Gets the normal color for this hyperlink.
	 * @return color Color of normal hyperlink
	 */
	public Color getNormalColor() {
		return normalColor;
	}

	/**
	 * Paints the hyperlink.
	 * @param g The Graphics object to paint on
	 */
	public void paint(Graphics g) {
		// let the label itself handle most of the hardwork
		super.paint(g);

		// Check that a hyperlink was specified
		if (url == null)
			return;

		// Get width of hyperlink label component
		Dimension d = getSize();
		int height = d.height;

		// Get width of URL label to calculate the line position
		FontMetrics metrics = getFontMetrics(getFont());
		int strWidth = metrics.stringWidth(getText());
		int yoffset = (height / 2) + metrics.getHeight() / 2;
		g.drawLine(0, yoffset, strWidth, yoffset);
	}

	/**
	 * Event handler for when the mouse clicks the hyperlink
	 * @param MouseEvent Event
	 */
	public void mouseClicked(MouseEvent e) {
		// no implementation required
	}

	/**
	 * Event handler for when the mouse enters the hyperlink
	 * @param MouseEvent Event
	 */
	public void mouseEntered(MouseEvent e) {
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	/**
	 * Event handler for when the mouse leaves the hyperlink
	 * @param MouseEvent Event
	 */
	public void mouseExited(MouseEvent e) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * Event handler for when the user press the mouse button
	 * @param MouseEvent Event
	 */
	public void mousePressed(MouseEvent e) {
		// no implementation required
	}

	/**
	 * Event handler for when the user release the mouse button
	 * @param MouseEvent Event
	 */
	public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
        	popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
        else {
			setForeground(visitedColor);
			try {
				URL protocalUrl = new URL(url);
				HyperlinkEvent ev = new HyperlinkEvent(this, EventType.ACTIVATED, protocalUrl);
				actionListener.hyperlinkUpdate(ev);
			}
			catch (MalformedURLException lE) {
				// no action
			}
			repaint();
        }
	}

	private class HyperLinkPopupMenu extends JPopupMenu {

		public HyperLinkPopupMenu() {
			JMenuItem userEditMenuItem = new JMenuItem(Externalizer.getString("LANSLIM.215")); //$NON-NLS-1$
	        userEditMenuItem.addActionListener(new HyperLinkPopupActionListener());
	        add(userEditMenuItem);
		}
	}

	private class HyperLinkPopupActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			StringSelection ss = new StringSelection(url);
	        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
		}
	}
	
}
