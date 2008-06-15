package com.oz.lanslim.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimModel;

public class ContactTree extends JTree implements ContactView, 
	CategoryExpander, TreeExpansionListener {
	
	private SlimModel slimModel = null;
	private ContactTreeModel treeModel = null;
	
	public ContactTree(SlimModel pModel, ContactPopupMenu pPopupMenu) {
		super();
		slimModel = pModel;

		if (slimModel.getSettings().isQuickDnd()) {
         	DragAndDropWithoutSelection.hackSingleSelectionDND(this);
    	}
	 

		treeModel = new ContactTreeModel(slimModel.getContacts(), 
				slimModel.getSettings().isGroupHidden(), slimModel.getSettings().isOfflineHidden());
		setCellRenderer(new ContactTreeCellRenderer());
		setModel(treeModel);
		setRootVisible(false);
		setExpandsSelectedPaths(true);
		addTreeExpansionListener(this);
        treeModel.registerExpander(this);
		
	    // Where the tree is initialized:
        getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

        //Add listener to the table so the popup menu can come up.
        MouseListener popupListener = 
        	new ContactViewMouseListener(pPopupMenu, this, pModel);
        addMouseListener(popupListener);
        
		setTransferHandler(new ContactTransferHandler(this, slimModel));
		setDragEnabled(true);
		
	}

	public void filter(String pPrefix, boolean pHideGroup, boolean pHideOffline) {
		treeModel.filter(pPrefix, pHideGroup, pHideOffline);
	}

	public SlimContact[] getSelectedContacts() {
		
		TreePath[] tps = getSelectionPaths();
		List lContacts = new ArrayList();
		for (int i = 0; i < tps.length; i++) {
			DefaultMutableTreeNode tn = (DefaultMutableTreeNode)tps[i].getLastPathComponent();
			if (tn.getUserObject() instanceof SlimContact) {
				lContacts.add(tn.getUserObject());
			}
		}
		return (SlimContact[])lContacts.toArray(new SlimContact[lContacts.size()]);
	}
	
	public String[] getSelectedCategories() {
		
		TreePath[] tps = getSelectionPaths();
		List lCategories = new ArrayList();
		for (int i = 0; i < tps.length; i++) {
			DefaultMutableTreeNode tn = (DefaultMutableTreeNode)tps[i].getLastPathComponent();
			if (tn.getUserObject() instanceof String) {
				lCategories.add(tn.getUserObject());
			}
		}
		return (String[])lCategories.toArray(new String[lCategories.size()]);
	}

	public boolean consolidateClickPoint(MouseEvent e) {
        int row = getRowForLocation(e.getX(), e.getY());
        // The autoscroller can generate drag events outside the Table's range.
        if (row == -1) {
            return false;
        }
        if (getSelectionCount() == 0) {
        	setSelectionRow(row);
        }
        return true;

	}

	public void collapseNode(DefaultMutableTreeNode pCatNode) {
		collapsePath(new TreePath(pCatNode.getPath()));
	}

	public void expandNode(DefaultMutableTreeNode pCatNode) {
		expandPath(new TreePath(pCatNode.getPath()));
	}

	public void treeCollapsed(TreeExpansionEvent event) {
		TreePath tp = event.getPath();
		DefaultMutableTreeNode tn = (DefaultMutableTreeNode)tp.getLastPathComponent();
		String lName = (String)tn.getUserObject();
		if (!ContactTreeModel.ROOT_NAME.equals(lName)) {
			slimModel.getContacts().setExpanded(lName, new Boolean(false));
		}
	}

	public void treeExpanded(TreeExpansionEvent event) {
		TreePath tp = event.getPath();
		DefaultMutableTreeNode tn = (DefaultMutableTreeNode)tp.getLastPathComponent();
		String lName = (String)tn.getUserObject();
		if (!ContactTreeModel.ROOT_NAME.equals(lName)) {
			slimModel.getContacts().setExpanded(lName, new Boolean(true));
		}
	}

}
