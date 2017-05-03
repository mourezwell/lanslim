package com.oz.lanslim.model;

public interface SlimCategoryListener {

	public void addCategory(String pCat, Boolean pExpanded);
	
	public void renameCategory(String pCat, String pNewCat);
	
	public void removeCategory(String pCat);
	
	public void moveUserIntoCategory(SlimUserContact pContact, String pOldCat, String pNewCat);
	
}
