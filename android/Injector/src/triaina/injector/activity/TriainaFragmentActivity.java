package triaina.injector.activity;

import triaina.injector.TriainaApplication;
import triaina.injector.TriainaInjector;

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
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class TriainaFragmentActivity extends FragmentActivity {
	protected EventManager eventManager;

	@Inject
	ContentViewListener ignored; // BUG find a better place to put this

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TriainaInjector injector = ((TriainaApplication)getApplication()).getInjector(this);
		eventManager = injector.getInstance(EventManager.class);
		injector.injectMembersWithoutViews(this);
		super.onCreate(savedInstanceState);
		eventManager.fire(new OnCreateEvent(savedInstanceState));
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
				((TriainaApplication)getApplication()).destroyInjector(this);
			} finally {
				super.onDestroy();
			}
		}
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
		TriainaInjector injector = ((TriainaApplication)getApplication()).getInjector(this);
		injector.injectViewMembers(this);
		eventManager.fire(new OnContentChangedEvent());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		eventManager.fire(new OnActivityResultEvent(requestCode, resultCode, data));
	}
}
