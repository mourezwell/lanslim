package com.oz.lanslim.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HTMLConstants {

	public static final  String HTTP = "http"; //$NON-NLS-1$
	
	public static final  String TAGEND = "\">"; //$NON-NLS-1$

	public static final  String BOLD = "<b>"; //$NON-NLS-1$
	public static final  String ENDBOLD = "</b>"; //$NON-NLS-1$
	public static final  String ITALIC = "<i>"; //$NON-NLS-1$
	public static final  String ENDITALIC = "</i>"; //$NON-NLS-1$
	public static final  String UNDERLINE = "<u>"; //$NON-NLS-1$
	public static final  String ENDUNDERLINE = "</u>"; //$NON-NLS-1$
	
	public static final  String FONTCOLOR = "<font color=\""; //$NON-NLS-1$
	public static final  String FONTSIZE = "\"size=\""; //$NON-NLS-1$
	public static final  String ENDFONT = "</font>"; //$NON-NLS-1$
	
	public static final  String LINK = "<a href=\""; //$NON-NLS-1$
	public static final  String ENDLINK = "</a>"; //$NON-NLS-1$

	public static final  String NEWLINE = "<br>"; //$NON-NLS-1$

	public static final  String HTML = "<html>"; //$NON-NLS-1$
	public static final  String ENDHTML = "</html>"; //$NON-NLS-1$

	public static final  String IMAGE = "<img src=\""; //$NON-NLS-1$
	public static final  String ENDIMAGE = "</img>"; //$NON-NLS-1$
	
	public static final  String GREY = "888888"; //$NON-NLS-1$
	public static final  String BLACK = "000000"; //$NON-NLS-1$
	
	public static final  String SYSTEM = "SYSTEM"; //$NON-NLS-1$
	public static final  String DEFAULT_SIZE = "4"; //$NON-NLS-1$
	public static final  String DEFAULT_COLOR = BLACK;

	private static final DateFormat myFormat = new SimpleDateFormat("HH:mm:ss"); //$NON-NLS-1$
	public static String DATE = " [" + myFormat.format(new Date()) + "]>"; //$NON-NLS-1$ //$NON-NLS-2$
	
	public static String getHeader(String pSender) {
		return FONTCOLOR + DEFAULT_SIZE + FONTSIZE + DEFAULT_SIZE 
			+ TAGEND + BOLD + pSender + DATE + ENDBOLD + ENDFONT ;
	}
}
