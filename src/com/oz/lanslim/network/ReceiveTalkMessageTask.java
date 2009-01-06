package com.oz.lanslim.network;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimIconListener;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.message.SlimUpdateTalkMessage;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimTalk;

public class ReceiveTalkMessageTask 
	implements Runnable {
	
	private SlimModel model;
	private SlimUpdateTalkMessage message;
	private SlimIconListener icon = null;
	
	public ReceiveTalkMessageTask(SlimModel pModel, SlimUpdateTalkMessage pMessage, 
			SlimIconListener pIcon) {
		model = pModel;
		message = pMessage;
		icon = pIcon;
	}
	
	public void run() {
		SlimTalk st = model.getTalks().getTalkById(message.getTalkId());
		if (st != null) {
			st.receiveUpdateTalkMessage(message);
			icon.startIconBlinking();
		}
		else {
			SlimLogger.log(Externalizer.getString("LANSLIM.43")); //$NON-NLS-1$
		}
	}

}
