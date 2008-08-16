package com.oz.lanslim;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class SlimLogger {

	private static final String LOG_FILE_NAME = "lanslim.log"; //$NON-NLS-1$
	public static final String LOG_EXCEPTION_SEPARATOR = ":"; //$NON-NLS-1$
	public static final String LOG_EXCEPTION_START = "["; //$NON-NLS-1$
	public static final String LOG_EXCEPTION_END = "]"; //$NON-NLS-1$
		
	private static BufferedWriter writer = null;
	private static boolean initOK = false;
	private static boolean initError = false;
	
	private static void init() {
		try {
			File logFile = new File(System.getProperty("user.home") +  File.separator + LOG_FILE_NAME); //$NON-NLS-1$
			writer = new BufferedWriter(new FileWriter(logFile, true));
			writer.write(Externalizer.getString("LANSLIM.4", new Date())); //$NON-NLS-1$
			writer.write(StringConstants.LINE_SEPARATOR);
			writer.flush();
			initOK = true;
		}
		catch (IOException ioe) {
			initError = true;
			System.err.println(Externalizer.getString("LANSLIM.5", ioe, ioe.getMessage()));  //$NON-NLS-1$
		}
	}
	
	
	public static void log(String pMessage) {
		if (!initOK) {
			init();
		}
		if (!initError) {
			try {
				writer.write(pMessage);
				writer.write(StringConstants.LINE_SEPARATOR);
				writer.flush();
			}
			catch (IOException ioe) {
				System.out.println(
						Externalizer.getString("LANSLIM.6", shortFormatException(ioe)));  //$NON-NLS-1$
				try {
					writer.close();
				}
				catch (IOException ioex) {
					// ignore exception
				}
			}
		}
	}
	
	public static void logException(String pMessage, Throwable e) {
		if (!initOK) {
			init();
		}
		if (!initError) {
			try {
				writer.write(Externalizer.getString("LANSLIM.3", pMessage, shortFormatException(e)));  //$NON-NLS-1$
				writer.write(StringConstants.LINE_SEPARATOR);
				writer.flush();
			}
			catch (IOException ioe) {
				System.out.println(Externalizer.getString("LANSLIM.6", shortFormatException(e)));  //$NON-NLS-1$

				try {
					writer.close();
				}
				catch (IOException ioex) {
					// ignore exception
				}
			}
		}
	}

	
	public static String shortFormatException(Throwable e) {
		return LOG_EXCEPTION_START + e + LOG_EXCEPTION_SEPARATOR + e.getMessage() + LOG_EXCEPTION_END;
	}

	public static String longFormatException(Throwable e) {
		if (e.getStackTrace() != null) {
			StringBuffer sb = new StringBuffer();
			sb.append(shortFormatException(e));
			sb.append(StringConstants.NEW_LINE);
			for (int i = 0; i < e.getStackTrace().length; i++) {
				sb.append(e.getStackTrace()[i]);
				sb.append(StringConstants.NEW_LINE);
			}
			return sb.toString();
		}
		return shortFormatException(e);
	}

}
