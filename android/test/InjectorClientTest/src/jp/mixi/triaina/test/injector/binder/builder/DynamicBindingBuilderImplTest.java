package jp.mixi.triaina.test.injector.binder.builder;

import java.util.List;

import jp.mixi.triaina.injector.binder.DynamicBinder;
import jp.mixi.triaina.injector.binder.builder.DynamicBindingBuilderImpl;
import junit.framework.TestCase;

public class DynamicBindingBuilderImplTest extends TestCase {

	public void testBind() {
		DynamicBinder binder = new DynamicBinder("AAA", "aaa");
		DynamicBindingBuilderImpl builder = new DynamicBindingBuilderImpl(binder);
		builder.bind(List.class);
		assertEquals(List.class, binder.getBindClass());
	}
}
