package com.oz.lanslim.network;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.message.SlimUpdateTalkMessage;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimTalk;

public class ReceiveTalkMessageTask 
	implements Runnable {
	
	private SlimModel model;
	private SlimUpdateTalkMessage message;
	
	public ReceiveTalkMessageTask(SlimModel pModel, SlimUpdateTalkMessage pMessage) {
		model = pModel;
		message = pMessage;
	}
	
	public void run() {
		SlimTalk st = model.getTalks().getTalkById(message.getTalkId());
		if (st != null) {
			st.receiveUpdateTalkMessage(message);
			model.notifyMessageReceived(message);
		}
		else {
			SlimLogger.log(Externalizer.getString("LANSLIM.43")); //$NON-NLS-1$
		}
	}

}
