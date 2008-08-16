package com.oz.lanslim;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.oz.lanslim.model.SlimSettings;

public class Externalizer {
	
	private static final String BUNDLE_BASE_NAME = "lang.lanslimMessages"; //$NON-NLS-1$

	private static final String BUNDLE_SEPARATOR = "_"; //$NON-NLS-1$

	private static final String COMPL_START_REGEX = "\\{"; //$NON-NLS-1$
	private static final String COMPL_END_REGEX = "\\}"; //$NON-NLS-1$


	private static ResourceBundle RESOURCE_BUNDLE = null;

	private Externalizer() {
		// private constructor to force static access
	}

	public static void setLanguage(String pLanguage) {
		String lLanguage = pLanguage;
		if (lLanguage == null) {
			System.out.println("Invalid language " + lLanguage  //$NON-NLS-1$
					+ " using default " + SlimSettings.DEFAULT_LANGUAGE); //$NON-NLS-1$
			lLanguage = SlimSettings.DEFAULT_LANGUAGE;
		}
		try {
			ResourceBundle lLanguageBundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME + BUNDLE_SEPARATOR + lLanguage);
			RESOURCE_BUNDLE = lLanguageBundle;
		}
		catch (MissingResourceException ex) {
			System.out.println("Invalid language " + lLanguage  //$NON-NLS-1$
					+ " using default " + SlimSettings.DEFAULT_LANGUAGE); //$NON-NLS-1$
			lLanguage = SlimSettings.DEFAULT_LANGUAGE;
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_BASE_NAME + BUNDLE_SEPARATOR + lLanguage);
		}
	}
	
	
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} 
		catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	public static String getString(String key, Object pComp) {
		String lRaw = getString(key);
		return lRaw.replaceFirst(COMPL_START_REGEX + 0 + COMPL_END_REGEX, pComp.toString());
	}
	
	public static String getString(String key, Object pComp1, Object pComp2) {
		String lRaw = getString(key);
		lRaw = lRaw.replaceFirst(COMPL_START_REGEX + 0 + COMPL_END_REGEX, pComp1.toString());
		lRaw = lRaw.replaceFirst(COMPL_START_REGEX + 1 + COMPL_END_REGEX, pComp2.toString());
		return lRaw;
	}

	public static String getString(String key, Object[] pComp) {
		String lRaw = getString(key);
		for (int i=0; i < pComp.length; i++) {
			lRaw = lRaw.replaceFirst(COMPL_START_REGEX + i + COMPL_END_REGEX, pComp[i].toString()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return lRaw;
	}

}
