package jp.mixi.triaina.injector.internal;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeConverterBinding;

import jp.mixi.triaina.injector.TriainaInjector;
import roboguice.inject.ContextScopedRoboInjector;

public class TriainaInjectorImpl implements TriainaInjector {
	private ContextScopedRoboInjector mDelegate;
	
	public TriainaInjectorImpl(ContextScopedRoboInjector injector) {
		mDelegate = injector;
	}
	
	@Override
	public Injector createChildInjector(Iterable<? extends Module> modules) {
		return mDelegate.createChildInjector(modules);
	}

	@Override
	public Injector createChildInjector(Module... modules) {
		return mDelegate.createChildInjector(modules);
	}

	@Override
	public <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> type) {
		return mDelegate.findBindingsByType(type);
	}

	public Map<Key<?>, Binding<?>> getAllBindings() {
		return mDelegate.getAllBindings();
	}

	@Override
	public <T> Binding<T> getBinding(Key<T> key) {
		return mDelegate.getBinding(key);
	}

	@Override
	public <T> Binding<T> getBinding(Class<T> type) {
		return mDelegate.getBinding(type);
	}

	@Override
	public Map<Key<?>, Binding<?>> getBindings() {
		return mDelegate.getBindings();
	}

	public <T> Binding<T> getExistingBinding(Key<T> key) {
		return mDelegate.getExistingBinding(key);
	}

	@Override
	public <T> T getInstance(Key<T> key) {
		return mDelegate.getInstance(key);
	}

	@Override
	public <T> T getInstance(Class<T> type) {
		return mDelegate.getInstance(type);
	}

	@Override
	public <T> MembersInjector<T> getMembersInjector(Class<T> type) {
		return mDelegate.getMembersInjector(type);
	}

	@Override
	public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
		return mDelegate.getMembersInjector(typeLiteral);
	}

	@Override
	public Injector getParent() {
		return mDelegate.getParent();
	}

	@Override
	public <T> Provider<T> getProvider(Key<T> key) {
		return mDelegate.getProvider(key);
	}

	@Override
	public <T> Provider<T> getProvider(Class<T> type) {
		return mDelegate.getProvider(type);
	}

	public Map<Class<? extends Annotation>, Scope> getScopeBindings() {
		return mDelegate.getScopeBindings();
	}

	public Set<TypeConverterBinding> getTypeConverterBindings() {
		return mDelegate.getTypeConverterBindings();
	}

	@Override
	public void injectMembers(Object instance) {
		mDelegate.injectMembers(instance);
	}

	@Override
	public void injectMembersWithoutViews(Object instance) {
		mDelegate.injectMembersWithoutViews(instance);
	}

	@Override
	public void injectViewMembers(Activity activity) {
		mDelegate.injectViewMembers(activity);
	}

	@Override
	public void injectViewMembers(Fragment fragment) {
		mDelegate.injectViewMembers(fragment);
	}
}
