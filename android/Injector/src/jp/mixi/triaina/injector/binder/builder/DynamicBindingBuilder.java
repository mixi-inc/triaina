package jp.mixi.triaina.injector.binder.builder;

public interface DynamicBindingBuilder {
	  public <T> DynamicLinkedBindingBuilder<T> bind(Class<T> clazz);
}
