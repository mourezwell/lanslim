package com.oz.lanslim;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestExternalizer {

	
	@Before
	public void init() {
		Externalizer.setLanguage("EN");
	}

	@Test
	public void testGetStringNoParam() {
		Assert.assertEquals("Show", Externalizer.getString("LANSLIM.0"));
	}

	@Test
	public void testGetStringUnknownParam() {
		Assert.assertEquals("!LANSLIM.!", Externalizer.getString("LANSLIM."));
	}

	@Test
	public void testSetLanguage() {
		Assert.assertEquals("Quit", Externalizer.getString("LANSLIM.1"));
		Externalizer.setLanguage("FR");
		Assert.assertEquals("Quitter", Externalizer.getString("LANSLIM.1"));
		Externalizer.setLanguage("DE"); // use defautl english
		Assert.assertEquals("Quit", Externalizer.getString("LANSLIM.1"));
		Externalizer.setLanguage(null); // use defautl english
		Assert.assertEquals("Quit", Externalizer.getString("LANSLIM.1"));
	}
	

	@Test
	public void testGetStringOneParam() {
		Assert.assertEquals("New message from toto", Externalizer.getString("LANSLIM.2", "toto"));
		Assert.assertEquals("New message from titi", Externalizer.getString("LANSLIM.2", "titi"));
	}

	@Test
	public void testGetStringTwoParam() {
		Assert.assertEquals("Exception caught in toto tata", Externalizer.getString("LANSLIM.3", "toto", "tata"));
		Assert.assertEquals("Exception caught in titi tutu", Externalizer.getString("LANSLIM.3", "titi", "tutu"));
	}

	@Test
	public void testGetStringNParam() {
		String[] params = { "toto", "tata"};
		Assert.assertEquals("Exception caught in toto tata", Externalizer.getString("LANSLIM.3", params));
	}

}
