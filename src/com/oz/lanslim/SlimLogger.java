package com.oz.lanslim;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class SlimLogger {

	private static final String LOG_FILE_NAME = "lanslim.log";
		
	private static BufferedWriter writer = null;
	private static boolean initOK = false;
	private static boolean initError = false;
	
	private static void init() {
		try {
			File logFile = new File(
					System.getProperty("user.home") +  File.separator + LOG_FILE_NAME);
			writer = new BufferedWriter(new FileWriter(logFile, true));
			writer.write("LANSLIM logger init at " + new Date() + "\n");
			writer.flush();
			initOK = true;
		}
		catch (IOException ioe) {
			initError = true;
			System.err.println("Unable to initialise Logger due to " + ioe + ":" + ioe.getMessage());
		}
	}
	
	public static void log(String pMessage) {
		if (!initOK) {
			init();
		}
		if (!initError) {
			try {
				writer.write(pMessage + "\n");
				writer.flush();
			}
			catch (IOException ioe) {
				System.out.println("Unable to log message due to " + ioe + ":" + ioe.getMessage());
				try {
					writer.close();
				}
				catch (IOException ioex) {
					// ignore exception
				}
			}
		}
	}
}
