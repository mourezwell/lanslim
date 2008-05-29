package com.oz.lanslim.model;

public interface SlimTalkListener {

	public void notifyTextTalkUpdate(SlimTalk pTalk);

	public void notifyNewTalk(SlimTalk pTalk);
	
	public void notifyNewTalkError(String pMessage);

}
