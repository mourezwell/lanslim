package com.oz.lanslim.network;

import java.io.IOException;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.message.SlimAvailabilityUserMessage;
import com.oz.lanslim.message.SlimErrorTalkMessage;
import com.oz.lanslim.message.SlimExitTalkMessage;
import com.oz.lanslim.message.SlimInviteTalkMessage;
import com.oz.lanslim.message.SlimMessage;
import com.oz.lanslim.message.SlimMessageTypeEnum;
import com.oz.lanslim.message.SlimNewTalkMessage;
import com.oz.lanslim.message.SlimUpdateTalkMessage;
import com.oz.lanslim.message.SlimUpdateUserMessage;
import com.oz.lanslim.model.SlimIconListener;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimTalk;
import com.oz.lanslim.model.SlimUserContact;

public class SlimNetworkAdapter implements Runnable {

	private static final long DELAY = 500;

	private SlimSocket socket;
	private SlimModel model;
	private Thread socketListener;
	private Timer delayedTimer;
	private boolean initOK = false;
	private SlimIconListener iconListener = null;
	
	public SlimNetworkAdapter(SlimModel pModel, SlimIconListener pIconListener) {
		
		model = pModel;
		delayedTimer = new Timer();
		delayedTimer.schedule(new TimerTask() {
			public void run() {
				Thread.currentThread().setName("Delayed Timer");
			}
		}, 0);
		iconListener = pIconListener;
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
					socketListener = new Thread(this, "Socket Listener");
					socketListener.start();
					initOK = true;
			}
			else {
				throw new SlimException("Unable to initialize network due to invalid Settings please check them");
			}
		}
		catch (SocketException se) {
			throw new SlimException("Unable to initialize network due to " + se + " :" + se.getMessage(), se);
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
							model.getContacts().receiveAvailabiltyMessage(saum);
						} 
						else if (SlimMessageTypeEnum.UPDATE_SETTINGS.equals(sm.getType())) {
							SlimUpdateUserMessage suum = (SlimUpdateUserMessage)sm;
							model.getContacts().receiveUpdateUserMessage(suum);
						}
						else if (SlimMessageTypeEnum.NEW_TALK.equals(sm.getType())) {
							SlimNewTalkMessage sntm = (SlimNewTalkMessage)sm;
							boolean success = model.getTalks().receiveNewTalkMessage(sntm);
						} 
						else if (SlimMessageTypeEnum.ERROR_TALK.equals(sm.getType())) {
							SlimErrorTalkMessage setm = (SlimErrorTalkMessage)sm;
							SlimTalk st = model.getTalks().getTalkById(setm.getTalkId());
							if (st != null) {
								st.receiveErrorTalkMessage(setm);
							}
							else {
								SlimLogger.log("Received invalid talk id in message, message ignored " + setm);
							}
						} 
						else if (SlimMessageTypeEnum.EXIT_TALK.equals(sm.getType())) {
							SlimExitTalkMessage setm = (SlimExitTalkMessage)sm;
							SlimTalk st = model.getTalks().getTalkById(setm.getTalkId());
							if (st != null) {
								st.receiveExitTalkMessage(setm);
							}
							else {
								SlimLogger.log("Received invalid talk id in message, message ignored " + setm);
							}
						} 
						else if (SlimMessageTypeEnum.INVITE_TALK.equals(sm.getType())) {
							SlimInviteTalkMessage sitm = (SlimInviteTalkMessage)sm;
							SlimTalk st = model.getTalks().getTalkById(sitm.getTalkId());
							if (st != null) {
								st.receiveInviteTalkMessage(sitm);
							}
							else {
								SlimLogger.log("Received invalid talk id in message, message ignored " + sitm);
							}
						} 
						else if (SlimMessageTypeEnum.UPDATE_TALK.equals(sm.getType())) {
							SlimUpdateTalkMessage sutm = (SlimUpdateTalkMessage)sm;
							SlimTalk st = model.getTalks().getTalkById(sutm.getTalkId());
							if (st != null) {
								st.receiveUpdateTalkMessage(sutm);
								iconListener.startIconBlinking();
							}
							else {
								SlimLogger.log("Received invalid talk id in message, message ignored " + sutm);
							}
						}
					}
					catch (RuntimeException re) {
						SlimLogger.log(re + " caught when receiving message " + re.getMessage());
					}
				}
				else {
					SlimLogger.log("Received invalid message or contacts not initailzed yet " + sm);
				}
			}
		}
		catch (SocketException se) {
			// cas  nominal d'arret
		}
		catch (IOException ioe) {
			SlimLogger.log(ioe + ":" + ioe.getMessage() + " at SlimNetworkAdapater.run()");
		}
	}
	
	public void send(SlimMessage pMessage, SlimUserContact pContact) throws SlimException {
		
		if (pMessage.getType() == SlimMessageTypeEnum.UPDATE_SETTINGS) {
			reset();
		}
		if (initOK) {
			try {
				socket.send(pMessage, pContact);
			}
			catch (IOException ioe) {
				throw new SlimException("Unable to Send message due to " + ioe + ":" + ioe.getMessage(), ioe);
			}
		}
		else {
			throw new SlimException("Unable to send message due to invalid settings please check them");
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
	
	public void sendDelayedMessage(SlimMessage pMessage, SlimUserContact pContact) throws SlimException {
		
		if (initOK) {
			delayedTimer.schedule(new DelayedMessageTask(pMessage, pContact), DELAY);		
		}
		else {
			throw new SlimException("Unable to send message due to invalid settings please check them");
		}
	}
	
	private class DelayedMessageTask extends TimerTask {
		private SlimMessage message;
		private SlimUserContact contact;
		
		public DelayedMessageTask(SlimMessage pMessage, SlimUserContact pContact) {
			message = pMessage;
			contact = pContact; 
		}
		
		public void run() {
			try {
				send(message, contact);
			}
			catch (SlimException se) {
				SlimLogger.log(se + ":" + se.getMessage() + " at DelayedMessageTask.run()");
			}
		}
	}

	public void stop() {
		if (socket != null) {
			socket.close();
		}
	}
}
