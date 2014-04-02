package triaina.test.injector.binder.builder;

import java.util.ArrayList;

import triaina.injector.binder.DynamicBinder;
import triaina.injector.binder.builder.DynamicLinkedBindingBuilderImpl;

import junit.framework.TestCase;

public class DynamicLinkedBindingBuilderImplTest extends TestCase {

	@SuppressWarnings("unchecked")
	public void testTo() {
		DynamicBinder binder = new DynamicBinder("AAA", "aaa");
		@SuppressWarnings("rawtypes")
		DynamicLinkedBindingBuilderImpl builder = new DynamicLinkedBindingBuilderImpl(binder);
		builder.to(ArrayList.class);
		assertEquals(ArrayList.class, binder.getImplementClass());
	}

}
