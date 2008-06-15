package com.oz.lanslim.gui;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimCategoryListener;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimContactList;
import com.oz.lanslim.model.SlimContactListener;
import com.oz.lanslim.model.SlimGroupContact;
import com.oz.lanslim.model.SlimUserContact;

public class ContactTreeModel extends DefaultTreeModel 
	implements SlimContactListener, SlimCategoryListener {

	private SlimContactList list = null;
	private String prefixNameFilter = null;
	private boolean hideGroupFilter = false;
	private boolean hideOfflineFilter = false;
	private DefaultMutableTreeNode undefinedNode = null;
	private DefaultMutableTreeNode groupNode = null;
	private CategoryExpander expander = null;
	
	protected static final String ROOT_NAME = "Lanslim";
	
	public ContactTreeModel(SlimContactList pList, boolean pHideGroup, boolean pHideOffline) {
		
		super(new DefaultMutableTreeNode(ROOT_NAME, true), true);
		list = pList;
		list.addContactListener(this);
		list.addCategoryListener(this);
		prefixNameFilter = "";
		hideGroupFilter = pHideGroup;
		hideOfflineFilter = pHideOffline;
		
		build();
	}

	private void build() {
		
		Set cats = list.getAllCategories();
		Iterator it1 = cats.iterator();
		while (it1.hasNext()) {
			String cat = (String)it1.next();
			addCategoryNode(new DefaultMutableTreeNode(cat, true), list.isExpanded(cat));
		}
		undefinedNode = getCategoryNode(SlimContactList.UNDEFINED_CATEGORY_NAME);
		groupNode = getCategoryNode(SlimContactList.GROUP_CATEGORY_NAME);
		if (!hideGroupFilter) {
			List groups = list.getAllGroupContact();
			Iterator it2 = groups.iterator();
			while (it2.hasNext()) {
				SlimGroupContact group = (SlimGroupContact)it2.next();
				if (group.getName().startsWith(prefixNameFilter)) {
					addContactInCategoryNode(group, getCategoryNode(SlimContactList.GROUP_CATEGORY_NAME));
				}
			}
		}
		else {
			removeNodeFromParent(groupNode);
		}
		
		List users = list.getAllUserContact();
		Iterator it3 = users.iterator();
		while (it3.hasNext()) {
			SlimUserContact user = (SlimUserContact)it3.next();
			if (!user.equals(list.getSettingsUser())) {
				if (user.getAvailability() != SlimAvailabilityEnum.OFFLINE || !hideOfflineFilter) {
					if (user.getName().startsWith(prefixNameFilter)) {
						addContactInCategoryNode(user, getCategoryNode(list.getCategory(user)));
					}
				}
			}
		}
	}
	
	private void addCategoryNode(DefaultMutableTreeNode pCategoryNode, Boolean pExpanded) {
		Enumeration existingCats = root.children();
		boolean inserted = false;
		while (existingCats.hasMoreElements() && !inserted) {
			DefaultMutableTreeNode lCatNode = (DefaultMutableTreeNode)existingCats.nextElement();
			String lCat = (String)lCatNode.getUserObject();
			if (((String)pCategoryNode.getUserObject()).compareToIgnoreCase(lCat) < 0) {
				insertNodeInto(pCategoryNode, (MutableTreeNode)root, root.getIndex(lCatNode));
				inserted = true;
			}
		}
		if (!inserted) {
			insertNodeInto(pCategoryNode, (MutableTreeNode)root, root.getChildCount());
		}
		
		if (expander != null) {
			if (pExpanded.booleanValue()) {
				expander.expandNode(pCategoryNode);
			}
			else {
				expander.expandNode(pCategoryNode);
			}
		}
	}
	
	private DefaultMutableTreeNode getCategoryNode(String pCat) {
		if (pCat == null) {
			return undefinedNode;
		}
		Enumeration existingCats = root.children();
		while (existingCats.hasMoreElements()) {
			DefaultMutableTreeNode lCatNode = (DefaultMutableTreeNode)existingCats.nextElement();
			String lCat = (String)lCatNode.getUserObject();
			if (pCat.equals(lCat)) {
				return lCatNode;
			}
		}
		return null;
	}
	
	private void addContactInCategoryNode(SlimContact contact, MutableTreeNode categoryNode) {
		Enumeration existingContacts = categoryNode.children();
		boolean inserted = false;
		while (existingContacts.hasMoreElements() && !inserted) {
			DefaultMutableTreeNode lCatNode = (DefaultMutableTreeNode)existingContacts.nextElement();
			SlimContact lContact = (SlimContact)lCatNode.getUserObject();
			if (contact.getName().compareToIgnoreCase(lContact.getName()) < 0) {
				insertNodeInto(new DefaultMutableTreeNode(contact, false), 
						categoryNode, categoryNode.getIndex(lCatNode));
				inserted = true;
			}
		}
		if (!inserted) {
			insertNodeInto(new DefaultMutableTreeNode(contact, false), 
					categoryNode, categoryNode.getChildCount());
		}
	}


	public void notifyContactError(String pMessage) {
		JOptionPane.showMessageDialog(null,
		    pMessage,
		    "Contact Update Error",
		    JOptionPane.WARNING_MESSAGE);
    }

	public void updateContacts() {
		reset();
		build();
	}

	public void filter(String pPrefix, boolean pHideGroup, boolean pHideOffline) {
		
		if (pHideGroup && !hideGroupFilter) {
			hideGroupFilter = pHideGroup;
			removeNodeFromParent(groupNode);
		}
		else if (!pHideGroup && hideGroupFilter) {
			hideGroupFilter = pHideGroup;
			addCategoryNode(groupNode, list.isExpanded(SlimContactList.CATEGORY_GROUP));
		}
		else if (!pHideOffline && hideOfflineFilter) {
			hideOfflineFilter = pHideOffline;
			List users = list.getAllUserContact();
			Iterator it3 = users.iterator();
			while (it3.hasNext()) {
				SlimUserContact user = (SlimUserContact)it3.next();
				if (!user.equals(list.getSettingsUser())) {
					if (user.getAvailability() == SlimAvailabilityEnum.OFFLINE) {
						if (user.getName().toLowerCase().startsWith(prefixNameFilter.toLowerCase())) {
							addContactInCategoryNode(user, getCategoryNode(list.getCategory(user)));
						}
					}
				}
			}
		}
		else if (pHideOffline && !hideOfflineFilter) {
			hideOfflineFilter = pHideOffline;
			Set cats = list.getAllCategories();
			Iterator it1 = cats.iterator();
			while (it1.hasNext()) {
				hideOfflineUserFromCat((String)it1.next());
			}
			hideOfflineUserFromCat(SlimContactList.UNDEFINED_CATEGORY_NAME);
		}
		else if (!prefixNameFilter.equals(pPrefix)) {
			prefixNameFilter = pPrefix;
			reset();
			build();
		}
		// else no change
	}

	private void reset() {
		Set cats = list.getAllCategories();
		Iterator it1 = cats.iterator();
		while (it1.hasNext()) {
			String cat = (String)it1.next();
			if (getCategoryNode(cat) != null) {
				removeNodeFromParent(getCategoryNode(cat));
			}
		}
	}
	
	private void hideOfflineUserFromCat(String pCat) {
		DefaultMutableTreeNode catNode = getCategoryNode(pCat);
		if (catNode != null && catNode.getChildCount() > 0) {
			DefaultMutableTreeNode child = catNode.getFirstLeaf();
			while (child != null) {
				SlimContact suc = (SlimContact)child.getUserObject();
				if (suc.getAvailability() == SlimAvailabilityEnum.OFFLINE) {
					DefaultMutableTreeNode next = (DefaultMutableTreeNode)catNode.getChildAfter(child);
					removeNodeFromParent(child);
					child = next;
				}
				else {
					child = (DefaultMutableTreeNode)catNode.getChildAfter(child);
				}
			}
		}
	}

	public void addCategory(String pCat, Boolean pExpanded) {
		addCategoryNode(new DefaultMutableTreeNode(pCat, true), pExpanded);
	}

	public void removeCategory(String pCat) {
		DefaultMutableTreeNode lNode = getCategoryNode(pCat);
		Enumeration existingContacts = lNode.children();
		boolean removed = false;
		while (existingContacts.hasMoreElements() && !removed) {
			DefaultMutableTreeNode lCatNode = (DefaultMutableTreeNode)existingContacts.nextElement();
			SlimContact lContact = (SlimContact)lCatNode.getUserObject();
			addContactInCategoryNode(lContact, 
					getCategoryNode(SlimContactList.UNDEFINED_CATEGORY_NAME));
		}
	}

	public void renameCategory(String pOldCat, String pNewCat) {
		DefaultMutableTreeNode lNode = getCategoryNode(pOldCat);
		removeNodeFromParent(lNode);
		lNode.setUserObject(pNewCat);
		addCategoryNode(lNode, list.isExpanded(pNewCat));
	}

	public void moveUserIntoCategory(SlimUserContact pContact, String pOldCat, String pNewCat) {
		DefaultMutableTreeNode lNode = getCategoryNode(pOldCat);
		Enumeration existingContacts = lNode.children();
		boolean removed = false;
		while (existingContacts.hasMoreElements() && !removed) {
			DefaultMutableTreeNode lCatNode = (DefaultMutableTreeNode)existingContacts.nextElement();
			SlimContact lContact = (SlimContact)lCatNode.getUserObject();
			if (lContact.getName().equalsIgnoreCase(pContact.getName())) {
				removeNodeFromParent(lCatNode);
			}
		}
		addContactInCategoryNode(pContact, getCategoryNode(pNewCat));
	}

	public void registerExpander(CategoryExpander pExpander) {
		expander = pExpander;
	}

}
