package com.oz.lanslim.gui;

import java.awt.Component;

public class SlimGUITUtils {

	public static Component getTopLevelCompoenent(Component c) {
		Component parent = c;
		Component child = c;
		while (parent != null) {
			child = parent;
			parent = child.getParent();
		}
		return child;
	}

}
