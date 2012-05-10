package jp.mixi.triaina.injector.internal;

import java.lang.reflect.Field;
import java.util.List;

import jp.mixi.triaina.commons.utils.ClassUtils;
import jp.mixi.triaina.commons.utils.FieldUtils;
import jp.mixi.triaina.injector.TriainaEnvironment;
import jp.mixi.triaina.injector.binder.BinderContainer;
import jp.mixi.triaina.injector.binder.DynamicBinder;

import com.google.inject.Injector;
import com.google.inject.MembersInjector;

public class DynamicMembersInjector<T> implements MembersInjector<T> {	
	private Field mField;
	private Injector mInjector;
	
	public DynamicMembersInjector(Field field, Injector injector) {
		field.setAccessible(true);
		mField = field;
		mInjector = injector;
	}
	
	@Override
	public void injectMembers(T instance) {
		Class<?> type = mField.getType();
		List<DynamicBinder> list = BinderContainer.get(type);
		if (list.size() < 1)
			return;
		
		String value = getEnviromentValue(list.get(0).getName());
		for (DynamicBinder binder : list) {
			if (value.equals(binder.getValue()))
				FieldUtils.setNoException(instance, mField, ClassUtils.newInstance(binder.getImplementClass()));
		}
	}
	
	private String getEnviromentValue(String name) {
		TriainaEnvironment env = mInjector.getInstance(TriainaEnvironment.class);
		String value = env.get(name);
		if (value == null)
			value = DynamicBinder.DEFAULT_VALUE;
		return value;
	}
}
