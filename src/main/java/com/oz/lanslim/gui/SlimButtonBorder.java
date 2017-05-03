package com.oz.lanslim.gui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.jgoodies.looks.LookUtils;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPUtils;

public class SlimButtonBorder {

	private static class SlimSelectedButtonBorder extends AbstractBorder {	
		
		private Insets insets = new Insets(3, 3, 3, 3);
		
        public Insets getBorderInsets(Component c) { return insets; }

        public Insets getBorderInsets(Component c, Insets newInsets) {
            newInsets.top    = insets.top;
            newInsets.left   = insets.left;
            newInsets.bottom = insets.bottom;
            newInsets.right  = insets.right;
            return newInsets;
        }
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        	AbstractButton b = (AbstractButton) c;
        	ButtonModel model = b.getModel();

            if (model.isEnabled() && model.isRollover()) {
            	PlasticXPUtils.drawButtonBorder(g, x, y, w, h,
                        PlasticLookAndFeel.getControl(),
                        PlasticLookAndFeel.getControlDarkShadow(),
                        LookUtils.getSlightlyBrighter(
                                PlasticLookAndFeel.getControlDarkShadow(),
                                1.25f));
        	}
            else {
               	PlasticXPUtils.drawButtonBorder(g, x, y, w, h,
                        PlasticLookAndFeel.getControl(),
                        MetalLookAndFeel.getBlack(),
                        LookUtils.getSlightlyBrighter(
                        		MetalLookAndFeel.getBlack(),
                                1.25f));
            }

        }
	};
	
	private static Border selectedBorder = new SlimSelectedButtonBorder();

	private static class SlimUnSelectedButtonBorder extends AbstractBorder {	
		
		private Insets insets = new Insets(3, 3, 3, 3);
		
        public Insets getBorderInsets(Component c) { return insets; }

        public Insets getBorderInsets(Component c, Insets newInsets) {
            newInsets.top    = insets.top;
            newInsets.left   = insets.left;
            newInsets.bottom = insets.bottom;
            newInsets.right  = insets.right;
            return newInsets;
        }
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        	AbstractButton b = (AbstractButton) c;
        	ButtonModel model = b.getModel();

            if (model.isEnabled() && !model.isRollover()) {
            	PlasticXPUtils.drawButtonBorder(g, x, y, w, h,
                        PlasticLookAndFeel.getControl(),
                        PlasticLookAndFeel.getControlHighlight(),
                        LookUtils.getSlightlyBrighter(
                                PlasticLookAndFeel.getControlHighlight(),
                                1.25f));
        	}
            else {
	        	PlasticXPUtils.drawButtonBorder(g, x, y, w, h,
	                    PlasticLookAndFeel.getControl(),
	                    PlasticLookAndFeel.getControlDarkShadow(),
	                    LookUtils.getSlightlyBrighter(
	                            PlasticLookAndFeel.getControlDarkShadow(),
	                            1.25f));
            }

        }
	};
	
	private static Border unSelectedBorder = new SlimUnSelectedButtonBorder();

	public static Border getSelectedBorder(boolean isSelected) {
		if (isSelected) {
			return selectedBorder;
		}
		return unSelectedBorder;
	}

}
