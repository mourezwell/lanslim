package com.oz.lanslim.network;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.message.SlimExcludeTalkMessage;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimTalk;

public class ReceiveExcludeTalkTask 
	implements Runnable {
	
	private SlimModel model;
	private SlimExcludeTalkMessage message;

	public ReceiveExcludeTalkTask(SlimModel pModel, SlimExcludeTalkMessage pMessage) {
		model = pModel;
		message = pMessage;
	}
	
	public void run() {
		SlimTalk st = model.getTalks().getTalkById(message.getTalkId());
		if (st != null) {
			st.receiveExcludeTalkMessage(message);
		}
		else {
			SlimLogger.log(Externalizer.getString("LANSLIM.43")); //$NON-NLS-1$
		}

	}

}
