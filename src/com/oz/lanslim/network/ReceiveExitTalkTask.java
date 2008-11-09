package com.oz.lanslim.network;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.message.SlimExitTalkMessage;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimTalk;

public class ReceiveExitTalkTask 
	implements Runnable {
	
	private SlimModel model;
	private SlimExitTalkMessage message;

	public ReceiveExitTalkTask(SlimModel pModel, SlimExitTalkMessage pMessage) {
		model = pModel;
		message = pMessage;
	}
	
	public void run() {
		SlimTalk st = model.getTalks().getTalkById(message.getTalkId());
		if (st != null) {
			st.receiveExitTalkMessage(message);
		}
		else {
			SlimLogger.log(Externalizer.getString("LANSLIM.43")); //$NON-NLS-1$
		}

	}

}
