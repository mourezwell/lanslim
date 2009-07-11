package com.oz.lanslim.network;

import com.oz.lanslim.message.SlimAvailabilityUserMessage;
import com.oz.lanslim.model.SlimModel;

public class ReceiveAvailabiltyTask 
	implements Runnable {
	
	private SlimModel model;
	private SlimAvailabilityUserMessage message;

	public ReceiveAvailabiltyTask(SlimModel pModel, SlimAvailabilityUserMessage pMessage) {
		model = pModel;
		message = pMessage;
	}
	
	public void run() {
		model.getContacts().receiveAvailabiltyMessage(message);
		model.notifyMessageReceived(message);
	}

}
