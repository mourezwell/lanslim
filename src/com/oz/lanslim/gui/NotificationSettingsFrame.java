package com.oz.lanslim.gui;	
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.model.HTMLConstants;
import com.oz.lanslim.model.SlimSettings;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class NotificationSettingsFrame extends JDialog implements ActionListener {
	
	private JCheckBox availabiltyBlinkBox;
	private JCheckBox availabiltyBubbleBox;
	private JCheckBox newTalkBlinkBox;
	private JCheckBox newTalkBubbleBox;
	private JCheckBox newMessageBlinkBox;
	private JCheckBox newMessageBubbleBox;
	private JCheckBox peopleInChangeBlinkBox;
	private JCheckBox peopleInChangeBubbleBox;
	
	private JButton cancelButton;
	private JButton okButton;

	private SlimSettings model;
	
	public NotificationSettingsFrame(Dialog pParent, SlimSettings pSettings) {
		super(pParent, true);
		model = pSettings;
		initGUI();
	}

	private void initGUI() {
		try {
			setTitle(Externalizer.getString("LANSLIM.73")); //$NON-NLS-1$
			
			FormLayout thisLayout = new FormLayout(
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)",  //$NON-NLS-1$
					"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)"); //$NON-NLS-1$
			getContentPane().setLayout(thisLayout);
			setSize(200, 410);
			setResizable(false);
			
			JLabel lTypeLabel = new JLabel(Externalizer.getString("LANSLIM.74")); //$NON-NLS-1$
			getContentPane().add(lTypeLabel, new CellConstraints("2, 2, 1, 1, default, default"));  //$NON-NLS-1$
			JLabel lBlinkLabel = new JLabel(Externalizer.getString("LANSLIM.75")); //$NON-NLS-1$
			getContentPane().add(lBlinkLabel, new CellConstraints("4, 2, 1, 1, default, default")); //$NON-NLS-1$
			JLabel lBubbleLabel = new JLabel(Externalizer.getString("LANSLIM.76")); //$NON-NLS-1$
			getContentPane().add(lBubbleLabel, new CellConstraints("6, 2, 1, 1, default, default")); //$NON-NLS-1$ 
			
			{
				JLabel lLabel = new JLabel(Externalizer.getString("LANSLIM.203")); //$NON-NLS-1$
				getContentPane().add(lLabel, new CellConstraints("2, 4, 1, 1, default, default")); //$NON-NLS-1$
				availabiltyBlinkBox = new JCheckBox();
				availabiltyBlinkBox.setSelected(model.isNotifAvailabiltyBlink());
				getContentPane().add(availabiltyBlinkBox, new CellConstraints("4, 4, 1, 1, default, default")); //$NON-NLS-1$
				availabiltyBubbleBox = new JCheckBox();
				availabiltyBubbleBox.setSelected(model.isNotifAvailabiltyBubble());
				getContentPane().add(availabiltyBubbleBox, new CellConstraints("6, 4, 1, 1, default, default")); //$NON-NLS-1$
			}
			{
				JLabel lLabel = new JLabel(Externalizer.getString("LANSLIM.204")); //$NON-NLS-1$
				getContentPane().add(lLabel, new CellConstraints("2, 6, 1, 1, default, default")); //$NON-NLS-1$
				newMessageBlinkBox = new JCheckBox();
				newMessageBlinkBox.setSelected(model.isNotifNewMessageBlink());
				getContentPane().add(newMessageBlinkBox, new CellConstraints("4, 6, 1, 1, default, default")); //$NON-NLS-1$
				newMessageBubbleBox = new JCheckBox();
				newMessageBubbleBox.setSelected(model.isNotifNewMessageBubble());
				getContentPane().add(newMessageBubbleBox, new CellConstraints("6, 6, 1, 1, default, default")); //$NON-NLS-1$
			}
			{
				JLabel lLabel = new JLabel(Externalizer.getString("LANSLIM.205")); //$NON-NLS-1$
				getContentPane().add(lLabel, new CellConstraints("2, 8, 1, 1, default, default")); //$NON-NLS-1$
				newTalkBlinkBox = new JCheckBox();
				newTalkBlinkBox.setSelected(model.isNotifNewTalkBlink());
				getContentPane().add(newTalkBlinkBox, new CellConstraints("4, 8, 1, 1, default, default")); //$NON-NLS-1$
				newTalkBubbleBox = new JCheckBox();
				newTalkBubbleBox.setSelected(model.isNotifNewTalkBubble());
				getContentPane().add(newTalkBubbleBox, new CellConstraints("6, 8, 1, 1, default, default")); //$NON-NLS-1$
			}
			{
				JLabel lLabel = new JLabel(Externalizer.getString("LANSLIM.206")); //$NON-NLS-1$
				getContentPane().add(lLabel, new CellConstraints("2, 10, 1, 1, default, default")); //$NON-NLS-1$
				peopleInChangeBlinkBox = new JCheckBox();
				peopleInChangeBlinkBox.setSelected(model.isNotifPeopleInBlink());
				getContentPane().add(peopleInChangeBlinkBox, new CellConstraints("4, 10, 1, 1, default, default")); //$NON-NLS-1$
				peopleInChangeBubbleBox = new JCheckBox();
				peopleInChangeBubbleBox.setSelected(model.isNotifPeopleInBubble());
				getContentPane().add(peopleInChangeBubbleBox, new CellConstraints("6, 10, 1, 1, default, default")); //$NON-NLS-1$
			}
			{
				JLabel trayWarning = new JLabel();
				if (!model.isTrayEnable()) {
					trayWarning.setIcon(new SlimIcon("info.png")); //$NON-NLS-1$
					trayWarning.setText(HTMLConstants.HTML + Externalizer.getString("LANSLIM.144") + HTMLConstants.ENDHTML); //$NON-NLS-1$
					
					availabiltyBubbleBox.disable();
					peopleInChangeBubbleBox.disable();
					newTalkBubbleBox.disable();
					newMessageBubbleBox.disable();
					
					lBubbleLabel.disable();
				}
				getContentPane().add(trayWarning, new CellConstraints("2, 12 , 5, 1, default, default")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			{
				okButton = new JButton();
				okButton.setText(Externalizer.getString("LANSLIM.15")); //$NON-NLS-1$
				okButton.setActionCommand(ShortcutFrameActionCommand.OK);
				okButton.addActionListener(this);
				getContentPane().add(okButton, new CellConstraints("3, 14, 1, 1, default, default")); //$NON-NLS-1$
				
				cancelButton = new JButton();
				cancelButton.setText(Externalizer.getString("LANSLIM.16")); //$NON-NLS-1$
				cancelButton.setActionCommand(ShortcutFrameActionCommand.CANCEL);
				cancelButton.addActionListener(this);
				getContentPane().add(cancelButton, new CellConstraints("5, 14, 1, 1, default, default")); //$NON-NLS-1$

			}
		} catch(Exception e) {
			SlimLogger.logException("ShortcutFrame.initGUI", e); //$NON-NLS-1$
		}	
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals(ShortcutFrameActionCommand.OK)) {
			model.setNotifAvailabiltyBlink(availabiltyBlinkBox.isSelected());
			model.setNotifAvailabiltyBubble(availabiltyBubbleBox.isSelected());
			model.setNotifNewMessageBlink(newMessageBlinkBox.isSelected());
			model.setNotifNewMessageBubble(newMessageBubbleBox.isSelected());
			model.setNotifNewTalkBlink(newTalkBlinkBox.isSelected());
			model.setNotifNewTalkBubble(newTalkBubbleBox.isSelected());
			model.setNotifPeopleInBlink(peopleInChangeBlinkBox.isSelected());
			model.setNotifPeopleInBubble(peopleInChangeBubbleBox.isSelected());
			setVisible(false);
		}
		else if (e.getActionCommand().equals(ShortcutFrameActionCommand.CANCEL)) {
			setVisible(false);
		}

	}
	
	private class ShortcutFrameActionCommand {
		
		public static final String OK = "ok"; //$NON-NLS-1$
		
		public static final String CANCEL = "cancel"; //$NON-NLS-1$
		
	}


}
