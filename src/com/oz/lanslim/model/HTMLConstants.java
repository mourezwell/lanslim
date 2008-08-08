package com.oz.lanslim.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HTMLConstants {

	public static final  String TAGEND = "\">";

	public static final  String BOLD = "<b>";
	public static final  String ENDBOLD = "</b>";
	public static final  String ITALIC = "<i>";
	public static final  String ENDITALIC = "</i>";
	public static final  String UNDERLINE = "<u>";
	public static final  String ENDUNDERLINE = "</u>";
	
	public static final  String FONTCOLOR = "<font color=\"";
	public static final  String FONTSIZE = "\"size=\"";
	public static final  String ENDFONT = "</font>";
	
	public static final  String LINK = "<a href=\"";
	public static final  String ENDLINK = "</a>";

	public static final  String NEWLINE = "<br>";

	public static final  String HTML = "<html>";
	public static final  String ENDHTML = "</html>";

	public static final  String IMAGE = "<img src=\"";
	public static final  String ENDIMAGE = "</img>";
	
	public static final  String GREY = "888888";
	public static final  String BLACK = "000000";
	
	public static final  String SYSTEM = "SYSTEM";
	public static final  String DEFAULT_SIZE = "4";
	public static final  String DEFAULT_COLOR = BLACK;

	private static final DateFormat myFormat = new SimpleDateFormat("HH:mm:ss");
	public static String DATE = " [" + myFormat.format(new Date()) + "]>";
	
	public static String getHeader(String pSender) {
		return FONTCOLOR + DEFAULT_SIZE + FONTSIZE + DEFAULT_SIZE 
			+ TAGEND + BOLD + pSender + DATE + ENDBOLD + ENDFONT ;
	}
}
