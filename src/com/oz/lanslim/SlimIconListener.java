package com.oz.lanslim;

public interface SlimIconListener {
	
	public void startIconBlinking(boolean pBlink, boolean pPop, String pMessage);

	public void stopIconBlinking();

	public void setFocus(boolean psFocus);
}
