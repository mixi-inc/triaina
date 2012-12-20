package triaina.injector.binder.builder;

import triaina.injector.binder.DynamicBinder;

public class DynamicBindingBuilderImpl implements DynamicBindingBuilder {
	private DynamicBinder mBinder;
	
	public DynamicBindingBuilderImpl(DynamicBinder binder) {
		mBinder = binder;
	}

	@Override
	public <T> DynamicLinkedBindingBuilder<T> bind(Class<T> clazz) {
		mBinder.bind(clazz);
		return new DynamicLinkedBindingBuilderImpl<T>(mBinder);
	}
}
