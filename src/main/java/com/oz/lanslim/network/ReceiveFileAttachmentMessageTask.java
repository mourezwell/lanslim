package com.oz.lanslim.network;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.message.SlimFileAttachmentMessage;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimTalk;

public class ReceiveFileAttachmentMessageTask 
	implements Runnable {
	
	private SlimModel model;
	private SlimFileAttachmentMessage message;
	
	public ReceiveFileAttachmentMessageTask(SlimModel pModel, SlimFileAttachmentMessage pMessage) {
		model = pModel;
		message = pMessage;
	}
	
	public void run() {
		SlimTalk st = model.getTalks().getTalkById(message.getTalkId());
		if (st != null) {
			st.receiveAttachmentMessage(message);
		}
		else {
			SlimLogger.log(Externalizer.getString("LANSLIM.43")); //$NON-NLS-1$
		}
	}

}
