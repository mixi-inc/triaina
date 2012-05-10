package jp.mixi.triaina.injector.binder;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.mixi.triaina.commons.utils.ArrayListUtils;

import com.google.inject.ConfigurationException;
import com.google.inject.spi.Message;

public class BinderContainer {	
	private static Map<Class<?>, List<DynamicBinder>> mContainer = new HashMap<Class<?>, List<DynamicBinder>>();
	
	public static void put(DynamicBinder binder) {
		Class<?> type = binder.getBindClass();
		synchronized (BinderContainer.class) {
			List<DynamicBinder> list = mContainer.get(type);
			if (list == null)
				list = new ArrayList<DynamicBinder>();
			else {
				DynamicBinder t = list.get(0);
		 		if (!binder.getName().equals(t.getName()))
					throw new ConfigurationException(ArrayListUtils.toArrayList(new Message(type.getName() + " is already defined to " + t.getName())));
			}
			
			list.add(binder);
			mContainer.put(type, list);	
		}
	}
	
	public static List<DynamicBinder> get(Class<?> type) {
		synchronized (BinderContainer.class) {
			List<DynamicBinder> list = mContainer.get(type);
			if (list == null)
				return new ArrayList<DynamicBinder>();
			return new ArrayList<DynamicBinder>(list);
		}
	}
	
	//for test
	public static void clear() {
		synchronized (BinderContainer.class) {
			mContainer.clear();
		}
	}
}
