package com.oz.lanslim.gui;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.model.HTMLConstants;

public class SlimHyperlinkListener implements HyperlinkListener {

	private TalkPane talk = null;
	
	public SlimHyperlinkListener(TalkPane pTalk) {
		talk = pTalk;
	}
	
	public void hyperlinkUpdate(HyperlinkEvent ev) {
		if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED)  {
			String url = ev.getURL().toString();
	        try {
	        	if (url.startsWith(HTMLConstants.FILE_PROTOCOL) && talk != null) {
	        		url = talk.downlaodLink(url);
	        	}
	        	if (url != null) {
			        String osName = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$
	    	        Runtime rt = Runtime.getRuntime();
					if (osName.indexOf( "win" ) >= 0) { //$NON-NLS-1$
	        	        rt.exec( "rundll32 url.dll,FileProtocolHandler " + url); //$NON-NLS-1$
	                }
			        else if (osName.indexOf("mac") >= 0) { //$NON-NLS-1$
	                    rt.exec( "open " + url); //$NON-NLS-1$
					}
	                else if (osName.indexOf("ix") >=0 || osName.indexOf("ux") >=0 || osName.indexOf("sun") >=0) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	        	        String[] browsers = 
	        	        	{"epiphany", "firefox", "mozilla", "konqueror",	"netscape","opera","links","lynx"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
	        	        // Build a command string which looks like "browser1
						// "url" || browser2 "url" ||..."
	        	        StringBuffer cmd = new StringBuffer();
	        	        for (int i = 0 ; i < browsers.length ; i++)
	        		        cmd.append((i == 0  ? "" : " || " ) + browsers[i] +" \"" + url + "\" "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	        	        rt.exec(new String[] { "sh", "-c", cmd.toString() }); //$NON-NLS-1$ //$NON-NLS-2$
	                }
	        	}
			}
			catch (Exception ex) {
				SlimLogger.logException("hyperlinkUpdate", ex); //$NON-NLS-1$
			}
		}
	}

}
