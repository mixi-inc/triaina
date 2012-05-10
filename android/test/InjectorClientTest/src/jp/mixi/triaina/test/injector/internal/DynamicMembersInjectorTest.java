package jp.mixi.triaina.test.injector.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeConverterBinding;

import jp.mixi.triaina.injector.TriainaEnvironment;
import jp.mixi.triaina.injector.binder.BinderContainer;
import jp.mixi.triaina.injector.binder.DynamicBinder;
import jp.mixi.triaina.injector.internal.DynamicMembersInjector;
import junit.framework.TestCase;

public class DynamicMembersInjectorTest extends TestCase {
	static class MockObject {
		public List<String> list;
	}
	
	public void testInjectMembers() throws Exception {
		DynamicBinder binder = new DynamicBinder("AAA", DynamicBinder.DEFAULT_VALUE);
		binder.bind(List.class);
		binder.to(ArrayList.class);
		BinderContainer.put(binder);
		
		Field field = MockObject.class.getField("list");
		DynamicMembersInjector<MockObject> injector = new DynamicMembersInjector<MockObject>(field, new MockInjector());
		MockObject m = new MockObject();
		injector.injectMembers(m);
		assertEquals(ArrayList.class, m.list.getClass());
	}
	
	static class MockInjector implements Injector {

		@Override
		public <T> MembersInjector<T> getMembersInjector(
				TypeLiteral<T> typeLiteral) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> MembersInjector<T> getMembersInjector(Class<T> type) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> type) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> Provider<T> getProvider(Key<T> key) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> Provider<T> getProvider(Class<T> type) {
			// TODO Auto-generated method stub
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T getInstance(Class<T> type) {
			return (T) new TriainaEnvironment();
		}

		@Override
		public Injector getParent() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Injector createChildInjector(Iterable<? extends Module> modules) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Injector createChildInjector(Module... modules) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map<Key<?>, Binding<?>> getAllBindings() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map<Key<?>, Binding<?>> getBindings() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> Binding<T> getBinding(Key<T> key) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> Binding<T> getBinding(Class<T> type) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> Binding<T> getExistingBinding(Key<T> arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> T getInstance(Key<T> key) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map<Class<? extends Annotation>, Scope> getScopeBindings() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Set<TypeConverterBinding> getTypeConverterBindings() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void injectMembers(Object instance) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
