package com.oz.lanslim.gui;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.model.SlimModel;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
*/
public class CloseActionFrame extends JFrame implements ActionListener {
	
	private JLabel titleLabel;
	private ButtonGroup closeActionGroup;
	private JRadioButton minimizeButton;
	private JRadioButton closeButton;
	private JCheckBox rememberButton;
	private JPanel buttonPanel;
	private JButton cancelButton;
	private JButton okButton;

	private SlimModel model;
	private JFrame mainFrame;
	
	public CloseActionFrame(SlimModel pModel, JFrame pMainFrame) {
		super();
		model = pModel;
		mainFrame = pMainFrame;
		initGUI();
	}

	private void initGUI() {
		try {
			setIconImage(new SlimIcon("comment_edit.png").getImage());
			setTitle("Choose Close Action");
			FormLayout thisLayout = new FormLayout(
					"max(p;5dlu)", 
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)");
			getContentPane().setLayout(thisLayout);
			//this.setPreferredSize(new java.awt.Dimension(250, 225));
			this.setSize(270, 225);
			this.setResizable(false);
			{
				titleLabel = new JLabel();
				getContentPane().add(titleLabel, new CellConstraints("1, 1, 1, 1, default, default"));
				if (!model.getSettings().isTrayEnable()) {
					titleLabel.setIcon(new SlimIcon("info.png"));
					titleLabel.setText("<html>SystemTrayIcon disabled, please check that <br> tray.dll(or so) is present in the executable directory</html>");
				}
			}
			{
				JPanel closeActionGroupPanel = new JPanel(new GridLayout(0, 1));
			    Border border = BorderFactory.createTitledBorder("Close prefered action");
			    closeActionGroupPanel.setBorder(border);
				closeActionGroup =  new ButtonGroup();
				getContentPane().add(closeActionGroupPanel, new CellConstraints("1, 3, 1, 1, default, default"));
				minimizeButton = new JRadioButton("Minimize in System Tray");
				closeActionGroupPanel.add(minimizeButton);
				closeActionGroup.add(minimizeButton);
			    closeButton = new JRadioButton("Exit Application");
			    closeActionGroupPanel.add(closeButton);
			    closeActionGroup.add(closeButton);
			    
			    if (model.getSettings().isTrayEnable()) {
			    	minimizeButton.setSelected(true);
			    }
			    else {
			    	closeButton.setSelected(true);
			    	minimizeButton.setEnabled(false);
			    }
			}
			{
				rememberButton =  new JCheckBox("Remember my preference");
				getContentPane().add(rememberButton, new CellConstraints("1, 5, 1, 1, default, default"));
			}
			{
				buttonPanel = new JPanel();
				okButton = new JButton();
				okButton.setText("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				
				cancelButton = new JButton();
				cancelButton.setText("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);

				buttonPanel.add(okButton);
				buttonPanel.add(cancelButton);
				
				getContentPane().add(buttonPanel, new CellConstraints("1, 7, 1, 1, default, center"));
			}
		} catch(Exception e) {
			SlimLogger.log(e + ":" + e.getMessage() + " at NewTalkFrame.initGUI");
		}	
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals("OK")) {
			if (rememberButton.isSelected()) {
				model.getSettings().setCloseAsTrayForced(minimizeButton.isSelected());
				model.getSettings().setCloseForced(closeButton.isSelected());
			}
			if (closeButton.isSelected()) {
				System.exit(0);
			}
			else { // minimize button
				setVisible(false);
				mainFrame.setVisible(false);
			}
		}
		else if (e.getActionCommand().equals("Cancel")) {
			setVisible(false);
		}
	}
}
