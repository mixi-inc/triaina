package triaina.injector;

import triaina.injector.binder.DynamicBinder;
import triaina.injector.binder.builder.DynamicBindingBuilder;
import triaina.injector.binder.builder.DynamicBindingBuilderImpl;

import com.google.inject.AbstractModule;

public abstract class AbstractTriainaModule extends AbstractModule {
	protected DynamicBindingBuilder define(String name, String value) {
		DynamicBinder binder = new DynamicBinder(name, value);
		return new DynamicBindingBuilderImpl(binder);
	}
	
	protected DynamicBindingBuilder define(String name) {
		return define(name, DynamicBinder.DEFAULT_VALUE);
	}
}
