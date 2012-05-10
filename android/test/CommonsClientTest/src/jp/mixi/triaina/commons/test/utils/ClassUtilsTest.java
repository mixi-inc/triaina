package jp.mixi.triaina.commons.test.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import jp.mixi.triaina.commons.utils.ClassUtils;
import junit.framework.TestCase;

public class ClassUtilsTest extends TestCase {

	public void testIsImplement() {
		assertTrue(ClassUtils.isImplement(AaaImpl.class, Aaa.class));
		assertTrue(ClassUtils.isImplement(AaaImplExt.class, Aaa.class));
		assertFalse(ClassUtils.isImplement(Object.class, Aaa.class));
	}

	public void testGetGenericType() {
		Type[] types = ClassUtils.getGenericType(BbbImpl.class, Bbb.class);
		assertEquals(1, types.length);
		assertEquals(AaaImplExt.class, types[0]);
		
	}
	
	public void testGetMethodsByName() throws Exception {
		Method[] ms = ClassUtils.getMethodsByName(BbbImpl.class, "func");
		
		assertEquals(2, ms.length);
		assertNotNull(ms[0]);
		assertNotNull(ms[1]);
		assertFalse("Both objects are equals", ms[0].equals(ms[1]));
		//find which method is which, as order of the methods in the list isn't guaranteed by the Java reflection API
		final int bridgeMethodIndex = ms[0].isBridge() ? 0 : 1;
		final int nonBridgeMethodIndex = (bridgeMethodIndex + 1) % 2;
		assertTrue(ms[bridgeMethodIndex].isBridge());
		assertFalse(ms[nonBridgeMethodIndex].isBridge());
		
		assertEquals(AaaImplExt.class, ms[nonBridgeMethodIndex].getGenericParameterTypes()[0]);
		assertEquals(Aaa.class       , ms[bridgeMethodIndex   ].getGenericParameterTypes()[0]);
	}
	
	public void testToClasses() throws Exception {
		Class<?>[] classes = ClassUtils.toClasses("a", new Integer(1), "b");
		assertEquals(String.class, classes[0]);
		assertEquals(Integer.class, classes[1]);
		assertEquals(String.class, classes[2]);
	}
	
	public void testNewInstanceWithArgs() throws Exception {
		Cons c = ClassUtils.newInstance(Cons.class, "aaa", "bbb");
		assertEquals("aaa", c.mAaa);
		assertEquals("bbb", c.mBbb);
	}
	
	public void testGetConstructor() throws Exception {
		Constructor<Cons> c = ClassUtils.getConstructor(Cons.class, new Class[]{String.class, String.class});
		assertNotNull(c);
	}
	
	static class Cons {
		private String mAaa;
		private String mBbb;
		public Cons(String aaa, String bbb) {
			mAaa = aaa;
			mBbb = bbb;
		}
	}
	
	static interface Aaa {
	}
	
	static class AaaImpl implements Aaa {	
	}
	
	static class AaaImplExt extends AaaImpl {
	}
	
	static interface Bbb<T extends Aaa> {
		public void func(T arg);
	}
	
	static class BbbImpl implements Bbb<AaaImplExt> {
		@Override
		public void func(AaaImplExt aaa) {
		}
	}
}
