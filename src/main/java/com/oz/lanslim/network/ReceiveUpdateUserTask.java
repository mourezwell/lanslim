package com.oz.lanslim.network;

import com.oz.lanslim.message.SlimUpdateUserMessage;
import com.oz.lanslim.model.SlimModel;

public class ReceiveUpdateUserTask 
	implements Runnable {
	
	private SlimModel model;
	private SlimUpdateUserMessage message;

	public ReceiveUpdateUserTask(SlimModel pModel, SlimUpdateUserMessage pMessage) {
		model = pModel;
		message = pMessage;
	}
	
	public void run() {
		model.getContacts().receiveUpdateUserMessage(message);
	}

}