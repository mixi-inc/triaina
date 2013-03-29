package triaina.injector.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.inject.Inject;

import roboguice.activity.event.OnActivityResultEvent;
import roboguice.activity.event.OnConfigurationChangedEvent;
import roboguice.activity.event.OnContentChangedEvent;
import roboguice.activity.event.OnCreateEvent;
import roboguice.activity.event.OnNewIntentEvent;
import roboguice.activity.event.OnPauseEvent;
import roboguice.activity.event.OnRestartEvent;
import roboguice.activity.event.OnResumeEvent;
import roboguice.activity.event.OnStopEvent;
import roboguice.event.EventManager;
import roboguice.inject.ContentViewListener;
import roboguice.service.event.OnDestroyEvent;
import roboguice.service.event.OnStartEvent;
import triaina.injector.TriainaInjector;
import triaina.injector.TriainaInjectorFactory;
import triaina.injector.activity.event.OnPostCreateEvent;
import triaina.injector.activity.event.OnRestoreInstanceStateEvent;
import triaina.injector.activity.event.OnSaveInstanceStateEvent;

public class TriainaFragmentActivity extends FragmentActivity {
	protected EventManager eventManager;

	@Inject
	ContentViewListener ignored; // BUG find a better place to put this

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TriainaInjector injector = TriainaInjectorFactory.getInjector(this);
		eventManager = injector.getInstance(EventManager.class);
		injector.injectMembersWithoutViews(this);
		super.onCreate(savedInstanceState);
		eventManager.fire(new OnCreateEvent(savedInstanceState));
	}

	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        eventManager.fire(new OnPostCreateEvent(savedInstanceState));
    }

    @Override
	protected void onRestart() {
		super.onRestart();
		eventManager.fire(new OnRestartEvent());
	}

	@Override
	protected void onStart() {
		super.onStart();
		eventManager.fire(new OnStartEvent());
	}

	@Override
	protected void onResume() {
		super.onResume();
		eventManager.fire(new OnResumeEvent());
	}

	@Override
	protected void onPause() {
		super.onPause();
		eventManager.fire(new OnPauseEvent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		eventManager.fire(new OnNewIntentEvent());
	}

	@Override
	protected void onStop() {
		try {
			eventManager.fire(new OnStopEvent());
		} finally {
			super.onStop();
		}
	}
	
	@Override
	protected void onDestroy() {
		try {
			eventManager.fire(new OnDestroyEvent());
		} finally {
			try {
			    TriainaInjectorFactory.destroyInjector(this);
			} finally {
				super.onDestroy();
			}
		}
	}

	@Override
    protected void onSaveInstanceState (final Bundle outState) {
        super.onSaveInstanceState(outState);
        eventManager.fire(new OnSaveInstanceStateEvent(outState));
    }

    @Override
    protected void onRestoreInstanceState (final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        eventManager.fire(new OnRestoreInstanceStateEvent(savedInstanceState));
    }

    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		final Configuration currentConfig = getResources().getConfiguration();
		super.onConfigurationChanged(newConfig);
		eventManager.fire(new OnConfigurationChangedEvent(currentConfig, newConfig));
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		TriainaInjector injector = TriainaInjectorFactory.getInjector(this);
		injector.injectViewMembers(this);
		eventManager.fire(new OnContentChangedEvent());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		eventManager.fire(new OnActivityResultEvent(requestCode, resultCode, data));
	}
}
