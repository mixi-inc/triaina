package triaina.injector;

import javax.inject.Inject;
import javax.inject.Provider;

import android.content.Context;

import roboguice.inject.ContextScope;

public class TriainaContextScopedProvider<T> {
	@Inject protected ContextScope scope;
	@Inject protected Provider<T> provider;

	public T get(final Context context) {
		synchronized (ContextScope.class) {
			scope.enter(context);
			try {
				return provider.get();
			} finally {
				scope.exit(context);
			}
		}
	}
}
