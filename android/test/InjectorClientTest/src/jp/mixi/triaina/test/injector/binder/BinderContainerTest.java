package jp.mixi.triaina.test.injector.binder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jp.mixi.triaina.injector.binder.BinderContainer;
import jp.mixi.triaina.injector.binder.DynamicBinder;
import junit.framework.TestCase;

public class BinderContainerTest extends TestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		BinderContainer.clear();
	}

	public void testPutAndGet() throws Exception {
		DynamicBinder binder = new DynamicBinder("AAA", "a1");
		binder.bind(List.class);
		binder.to(ArrayList.class);
		BinderContainer.put(binder);
		
		binder = new DynamicBinder("AAA", "a2");
		binder.bind(List.class);
		binder.to(LinkedList.class);
		BinderContainer.put(binder);
		
		List<DynamicBinder> list = BinderContainer.get(List.class);
		assertEquals(2, list.size());
		
		assertEquals("AAA", list.get(0).getName());
		assertEquals("AAA", list.get(1).getName());
	}
	
	public void testPutAndGetOnException() {
		DynamicBinder binder = new DynamicBinder("AAA", "a1");
		binder.bind(List.class);
		binder.to(ArrayList.class);
		BinderContainer.put(binder);
		
		binder = new DynamicBinder("BBB", "a2");
		binder.bind(List.class);
		binder.to(LinkedList.class);
		try {
			BinderContainer.put(binder);
			fail();
		} catch (Exception exp) {
		}
		
		List<DynamicBinder> list = BinderContainer.get(List.class);
		assertEquals(1, list.size());		
		assertEquals("AAA", list.get(0).getName());
	}
}
