package com.oz.lanslim.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.model.SlimModel;

public class SlimMainFrame extends JFrame {

	public static final String TITLE = "LANSLIM"; //$NON-NLS-1$
	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel"); //$NON-NLS-1$
		} catch(Exception e) {
			SlimLogger.logException("Look&Feel setting", e); //$NON-NLS-1$
		}
	}

	private JPanel mainPanel = null;
	private SlimMainFrameWindowListener listener = null;

	private SlimModel model = null;

	public SlimMainFrame(SlimModel pModel) {
		super();
		model = pModel;
		initGUI();
	}
	
	private void initGUI() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle(TITLE);
		setIconImage(new SlimIcon("comment_edit.png").getImage()); //$NON-NLS-1$
		listener = new SlimMainFrameWindowListener(model, this);
		
		addWindowFocusListener(listener);
		addWindowListener(listener);
		addWindowStateListener(listener);
		addComponentListener(listener);

		mainPanel = new MainPane(model);
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		setSize(model.getSettings().getW(), model.getSettings().getH());
		setLocation(model.getSettings().getX(), model.getSettings().getY());
	}

}
