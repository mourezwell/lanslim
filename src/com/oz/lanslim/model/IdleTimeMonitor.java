package com.oz.lanslim.model;

public class IdleTimeMonitor implements Runnable {

	private static final int AWAY_DELAY = 120;
	
	private boolean windowsEnable = false;
	private boolean linuxEnable = false;
	private boolean running = false;
	private boolean awayDetected = false;
	private SlimUserContact user = null;

	public IdleTimeMonitor(SlimUserContact pUser) {
		user = pUser;
		
		try {
			Win32IdleTime.getIdleTimeMillis();
			windowsEnable = true;
		}
		catch (NoClassDefFoundError e) {
			//Optionnel
		}
		catch (UnsatisfiedLinkError e) {
			//Optionnel
		}

		try {
			LinuxIdleTime.getIdleTimeMillis();
			linuxEnable = true;
		}
		catch (NoClassDefFoundError e) {
			//Optionnel
		}
		catch (UnsatisfiedLinkError e) {
			//Optionnel
		}
	}

	private int getIdleTime() {
		int lRes = 0;
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.indexOf( "win" ) >= 0) { //$NON-NLS-1$
			if (windowsEnable) {
				lRes = Win32IdleTime.getIdleTimeMillis();
			}
        }
        else if (osName.indexOf("mac") >= 0) { //$NON-NLS-1$
			//default value 0 used
		}
        else if (osName.indexOf("ix") >=0 || osName.indexOf("ux") >=0 || osName.indexOf("sun") >=0) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if (linuxEnable) {
				lRes = (int)LinuxIdleTime.getIdleTimeMillis();
			}
		}
		return lRes;
	}
	
    public void run() {
    	
    	if (windowsEnable || linuxEnable) {
    		running = true;
    		int lIdleSec = 0;
    		boolean lAway = false;
    		while(running) {
    			lIdleSec = getIdleTime() / 1000;
    			lAway = lIdleSec > AWAY_DELAY;
             
	            if (user.getState().equals(SlimStateEnum.AVAILABLE) && lAway) {
	            	awayDetected = true;
	            	user.setState(SlimStateEnum.AWAY);
	            }
	            else if (awayDetected && !lAway && user.getState().equals(SlimStateEnum.AWAY)) {
	            	awayDetected = false;
	            	user.setState(SlimStateEnum.AVAILABLE);
	            }
	            
	            try { 
	            	Thread.sleep(1000); 
            	} 
	            catch (InterruptedException ex) {
	            	/*interruption*/
            	}
    		}
        }
    }
   
    public void stop() {
    	running = false;
    }
}
