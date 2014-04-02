package triaina.test.injector.binder.builder;

import java.util.List;

import triaina.injector.binder.DynamicBinder;
import triaina.injector.binder.builder.DynamicBindingBuilderImpl;

import junit.framework.TestCase;

public class DynamicBindingBuilderImplTest extends TestCase {

	public void testBind() {
		DynamicBinder binder = new DynamicBinder("AAA", "aaa");
		DynamicBindingBuilderImpl builder = new DynamicBindingBuilderImpl(binder);
		builder.bind(List.class);
		assertEquals(List.class, binder.getBindClass());
	}
}
