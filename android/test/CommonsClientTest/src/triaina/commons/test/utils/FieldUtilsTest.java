package triaina.commons.test.utils;

import java.lang.reflect.Field;

import triaina.commons.exception.CommonRuntimeException;
import triaina.commons.utils.FieldUtils;
import junit.framework.TestCase;

public class FieldUtilsTest extends TestCase {

	public void testSetNoException() throws Exception {
		Field field = Aaa.class.getField("aaa");
		Aaa a = new Aaa();
		FieldUtils.setNoException(a, field, new Object());
	}

	public void testSetAndGet() throws Exception {
		Field field = Aaa.class.getField("aaa");
		Aaa a = new Aaa();
		FieldUtils.set(a, field, "test");
		assertEquals("test", FieldUtils.get(a, field));
	}
	
	public void testSetOnException() throws Exception {
		try {
			Field field = Aaa.class.getField("aaa");
			Aaa a = new Aaa();
			FieldUtils.set(a, field, new Object());
			fail();
		} catch (CommonRuntimeException exp) {
		}
	}
	
	static class Aaa {
		public String aaa;
	}
}
