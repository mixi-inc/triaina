package jp.mixi.triaina.injector.binder.builder;

public interface DynamicLinkedBindingBuilder<T> {
	public void to(Class<? extends T> implementation);
}
