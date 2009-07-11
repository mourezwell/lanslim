package com.oz.lanslim.network;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.message.SlimInviteTalkMessage;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimTalk;

public class ReceiveInvitationTask 
	implements Runnable {
	
	private SlimModel model;
	private SlimInviteTalkMessage message;

	public ReceiveInvitationTask(SlimModel pModel, SlimInviteTalkMessage pMessage) {
		model = pModel;
		message = pMessage;
	}
	
	public void run() {
		SlimTalk st = model.getTalks().getTalkById(message.getTalkId());
		if (st != null) {
			st.receiveInviteTalkMessage(message);
			model.notifyMessageReceived(message);
		}
		else {
			SlimLogger.log(Externalizer.getString("LANSLIM.43")); //$NON-NLS-1$
		}
	}

}
