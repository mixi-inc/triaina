package jp.mixi.triaina.commons.test.utils;

import jp.mixi.triaina.commons.utils.NamingConventionUtils;
import junit.framework.TestCase;

public class NamingConventionUtilsTest extends TestCase {

	public void testFromJavaFieldNameToJSONName() {
		String name = NamingConventionUtils.fromJavaFieldNameToJSONName("mAaaAaa");
		assertEquals("aaa_aaa", name);
		
		name = NamingConventionUtils.fromJavaFieldNameToJSONName("m");
		assertEquals("m", name);
		
		name = NamingConventionUtils.fromJavaFieldNameToJSONName("aaa");
		assertEquals("aaa", name);
		
		name = NamingConventionUtils.fromJavaFieldNameToJSONName("aaaAaa");
		assertEquals("aaa_aaa", name);
		
		name = NamingConventionUtils.fromJavaFieldNameToJSONName("AaaAaa");
		assertEquals("aaa_aaa", name);
	}
	
	public void testFromJavaClassNameToDotDelimited() {
		String name = NamingConventionUtils.fromJavaClassNameToDotDelimited("AaaAaa");
		assertEquals("aaa.aaa", name);
	}
}
