package com.oz.lanslim.gui;

import javax.swing.tree.DefaultMutableTreeNode;

public interface CategoryExpander {

	public void expandNode(DefaultMutableTreeNode pCatNode);

	public void collapseNode(DefaultMutableTreeNode pCatNode);
}
