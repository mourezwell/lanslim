package com.oz.lanslim;

import com.oz.lanslim.model.SlimStateListener;

public interface SlimIconListener extends SlimStateListener {
	
	public void startIconBlinking(boolean pBlink, boolean pPop, String pMessage);

	public void stopIconBlinking();

	public void setFocus(boolean psFocus);
}
