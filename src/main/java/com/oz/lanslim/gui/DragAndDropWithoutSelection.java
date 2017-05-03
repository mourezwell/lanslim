/*
 *-------------------------------------------------------------------------------------------------
 *   Classe : DragAndDropWithoutSelection
 *   Copyright 2007 by AIRBUS France
 *-------------------------------------------------------------------------------------------------
 */
package com.oz.lanslim.gui;

import java.awt.Component;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DragAndDropWithoutSelection {

    
    public static void hackSingleSelectionDND(Component c){
        MouseListener dragListener = null;

        // the default dnd implemntation requires to first select node and then drag
        try{
            Class clazz = Class.forName("javax.swing.plaf.basic.BasicDragGestureRecognizer"); //$NON-NLS-1$
            MouseListener[] mouseListeners = c.getMouseListeners();
            for(int i = 0; i<mouseListeners.length; i++){
                if(clazz.isAssignableFrom(mouseListeners[i].getClass())){
                    dragListener = mouseListeners[i];
                    break;
                }
            }

            if(dragListener!=null){
                c.removeMouseListener(dragListener);
                c.removeMouseMotionListener((MouseMotionListener)dragListener);
                c.addMouseListener(dragListener);
                c.addMouseMotionListener((MouseMotionListener)dragListener);
            }
        } catch(ClassNotFoundException e){
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}
