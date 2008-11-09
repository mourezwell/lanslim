package com.oz.lanslim.network;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.message.SlimNewTalkMessage;
import com.oz.lanslim.model.SlimModel;

public class ReceiveNewTalkTask 
	implements Runnable {
	
	private SlimModel model;
	private SlimNewTalkMessage message;

	public ReceiveNewTalkTask(SlimModel pModel, SlimNewTalkMessage pMessage) {
		model = pModel;
		message = pMessage;
	}
	
	public void run() {
		boolean success = model.getTalks().receiveNewTalkMessage(message);
		if (!success) {
			SlimLogger.log(Externalizer.getString("LANSLIM.42")); //$NON-NLS-1$
		}
	}

}
