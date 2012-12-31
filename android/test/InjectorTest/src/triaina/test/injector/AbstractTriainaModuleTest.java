package triaina.test.injector;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import triaina.injector.AbstractTriainaModule;
import triaina.injector.binder.BinderContainer;
import triaina.injector.binder.DynamicBinder;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.PrivateBinder;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.AnnotatedConstantBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.Message;
import com.google.inject.spi.TypeConverter;
import com.google.inject.spi.TypeListener;

import junit.framework.TestCase;

public class AbstractTriainaModuleTest extends TestCase {
	static class MockBinder implements Binder {

		@Override
		public void bindScope(Class<? extends Annotation> annotationType,
				Scope scope) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public <T> LinkedBindingBuilder<T> bind(Key<T> key) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> AnnotatedBindingBuilder<T> bind(TypeLiteral<T> typeLiteral) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> AnnotatedBindingBuilder<T> bind(Class<T> type) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AnnotatedConstantBindingBuilder bindConstant() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Stage currentStage() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void addError(String message, Object... arguments) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addError(Throwable t) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addError(Message message) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void convertToTypes(Matcher<? super TypeLiteral<?>> typeMatcher,
				TypeConverter converter) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void bindListener(Matcher<? super TypeLiteral<?>> typeMatcher,
				TypeListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void disableCircularProxies() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public <T> void requestInjection(TypeLiteral<T> type, T instance) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void requestInjection(Object instance) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void requestStaticInjection(Class<?>... types) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void install(Module module) {
			// TODO Auto-generated method stub
			
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
		public PrivateBinder newPrivateBinder() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void requireExplicitBindings() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Binder withSource(Object source) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Binder skipSources(@SuppressWarnings("rawtypes") Class... classesToSkip) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	public void testDefineStringString() {
		AbstractTriainaModule module = new AbstractTriainaModule() {
			@Override
			protected void configure() {
				define("AAA", "aaa").bind(List.class).to(ArrayList.class);
				define("AAA", "bbb").bind(List.class).to(LinkedList.class);
			}
		};
		module.configure(new MockBinder());
		List<DynamicBinder> list = BinderContainer.get(List.class);
		
		assertEquals(2, list.size());
		assertEquals("AAA", list.get(0).getName());
		assertEquals("AAA", list.get(1).getName());
	}
}
