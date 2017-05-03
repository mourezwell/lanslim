package com.oz.lanslim.network;

import java.io.IOException;
import java.net.SocketException;

import javax.swing.SwingUtilities;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.message.SlimAvailabilityUserMessage;
import com.oz.lanslim.message.SlimExcludeTalkMessage;
import com.oz.lanslim.message.SlimExitTalkMessage;
import com.oz.lanslim.message.SlimFileAttachmentMessage;
import com.oz.lanslim.message.SlimInviteTalkMessage;
import com.oz.lanslim.message.SlimMessage;
import com.oz.lanslim.message.SlimMessageTypeEnum;
import com.oz.lanslim.message.SlimNewTalkMessage;
import com.oz.lanslim.message.SlimUpdateTalkMessage;
import com.oz.lanslim.message.SlimUpdateUserMessage;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimUserContact;

public class SlimNetworkAdapter implements Runnable {

	private SlimSocket socket;
	private SlimModel model;
	private Thread socketListener;
	private boolean initOK = false;
	
	public SlimNetworkAdapter(SlimModel pModel) {
		
		model = pModel;
		try {
			initSocket();
		}
		catch (SlimException se) {
			// invalid settings are allowed at start
		}
	}
	
	private void initSocket() throws SlimException {
		try {
			if (model.getSettings().areValidSettings()) {
				initOK = false;
					socket = new SlimSocket(model.getSettings().getContactInfo().getHost(), 
							model.getSettings().getContactInfo().getPort());
					socketListener = new Thread(this, "Socket Listener"); //$NON-NLS-1$
					socketListener.start();
					initOK = true;
			}
			else {
				throw new SlimException(Externalizer.getString("LANSLIM.41", Externalizer.getString("LANSLIM.40"))); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		catch (SocketException se) {
			throw new SlimException(Externalizer.getString("LANSLIM.41", SlimLogger.shortFormatException(se))); //$NON-NLS-1$
		}
	}

	public void run() {
		try {
			while (true) {
				SlimMessage sm = socket.receive();
				if (sm != null && model.getContacts() != null) {
					try {
						if (SlimMessageTypeEnum.AVAILABILITY.equals(sm.getType())) {
							SlimAvailabilityUserMessage saum = (SlimAvailabilityUserMessage)sm;
							SwingUtilities.invokeLater(new ReceiveAvailabiltyTask(model, saum));
						} 
						else if (SlimMessageTypeEnum.UPDATE_USER.equals(sm.getType())) {
							SlimUpdateUserMessage suum = (SlimUpdateUserMessage)sm;
							SwingUtilities.invokeLater(new ReceiveUpdateUserTask(model, suum));
						}
						else if (SlimMessageTypeEnum.NEW_TALK.equals(sm.getType())) {
							SlimNewTalkMessage sntm = (SlimNewTalkMessage)sm;
							SwingUtilities.invokeLater(new ReceiveNewTalkTask(model, sntm));
						} 
						else if (SlimMessageTypeEnum.EXIT_TALK.equals(sm.getType())) {
							SlimExitTalkMessage setm = (SlimExitTalkMessage)sm;
							SwingUtilities.invokeLater(new ReceiveExitTalkTask(model, setm));
						} 
						else if (SlimMessageTypeEnum.INVITE_TALK.equals(sm.getType())) {
							SlimInviteTalkMessage sitm = (SlimInviteTalkMessage)sm;
							SwingUtilities.invokeLater(new ReceiveInvitationTask(model, sitm));
						} 
						else if (SlimMessageTypeEnum.UPDATE_TALK.equals(sm.getType())) {
							SlimUpdateTalkMessage sutm = (SlimUpdateTalkMessage)sm;
							SwingUtilities.invokeLater(new ReceiveTalkMessageTask(model, sutm));
						}
						else if (SlimMessageTypeEnum.EXCLUDE_TALK.equals(sm.getType())) {
							SlimExcludeTalkMessage setm = (SlimExcludeTalkMessage)sm;
							SwingUtilities.invokeLater(new ReceiveExcludeTalkTask(model, setm));
						}				
						else if (SlimMessageTypeEnum.ATTACHMENT_TALK.equals(sm.getType())) {
							SlimFileAttachmentMessage sfam = (SlimFileAttachmentMessage)sm;
							SwingUtilities.invokeLater(new ReceiveFileAttachmentMessageTask(model, sfam));
						}				
						else {
							SlimLogger.log(Externalizer.getString("LANSLIM.44")); //$NON-NLS-1$
						}
					}
					catch (RuntimeException re) {
						SlimLogger.logException("adapter.receive", re); //$NON-NLS-1$
					}
				}
				else {
					SlimLogger.log(Externalizer.getString("LANSLIM.44")); //$NON-NLS-1$
				}
			}
		}
		catch (SocketException se) {
			// cas  nominal d'arret
		}
		catch (IOException ioe) {
			SlimLogger.logException("adapter.receive", ioe); //$NON-NLS-1$
		}
	}
	
	public void send(SlimMessage pMessage, SlimUserContact pContact) throws SlimException {
		
		if (pMessage.getType() == SlimMessageTypeEnum.UPDATE_USER) {
			reset();
		}
		if (initOK) {
			try {
				socket.send(pMessage, pContact);
			}
			catch (Exception ioe) {
				throw new SlimException(Externalizer.getString("LANSLIM.45", SlimLogger.shortFormatException(ioe))); //$NON-NLS-1$
			}
		}
		else {
			throw new SlimException(Externalizer.getString("LANSLIM.45", Externalizer.getString("LANSLIM.40"))); //$NON-NLS-1$  //$NON-NLS-2$
		}
	}

	public void reset() throws SlimException {
		stop();
		try {
			Thread.sleep(10);
		} 
		catch (InterruptedException ie) {
			// nothing to do
		}
		initSocket();
	}

	public void stop() {
		if (socket != null) {
			socket.close();
		}
	}
}
