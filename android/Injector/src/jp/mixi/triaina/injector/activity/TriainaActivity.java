package jp.mixi.triaina.injector.activity;

import com.google.inject.Inject;

import jp.mixi.triaina.injector.TriainaApplication;
import jp.mixi.triaina.injector.TriainaInjector;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import roboguice.activity.event.OnActivityResultEvent;
import roboguice.activity.event.OnConfigurationChangedEvent;
import roboguice.activity.event.OnContentChangedEvent;
import roboguice.activity.event.OnCreateEvent;
import roboguice.activity.event.OnDestroyEvent;
import roboguice.activity.event.OnNewIntentEvent;
import roboguice.activity.event.OnPauseEvent;
import roboguice.activity.event.OnRestartEvent;
import roboguice.activity.event.OnResumeEvent;
import roboguice.activity.event.OnStartEvent;
import roboguice.activity.event.OnStopEvent;
import roboguice.event.EventManager;
import roboguice.inject.ContentViewListener;

public class TriainaActivity extends Activity {
    protected EventManager mEventManager;

    @Inject ContentViewListener ignored; // BUG find a better place to put this

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	TriainaInjector injector = ((TriainaApplication)getApplication()).getInjector(this);
    	mEventManager = injector.getInstance(EventManager.class);
    	injector.injectMembersWithoutViews(this);
    	super.onCreate(savedInstanceState);
    	mEventManager.fire(new OnCreateEvent(savedInstanceState));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mEventManager.fire(new OnRestartEvent());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mEventManager.fire(new OnStartEvent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEventManager.fire(new OnResumeEvent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEventManager.fire(new OnPauseEvent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mEventManager.fire(new OnNewIntentEvent());
    }

    @Override
    protected void onStop() {
        try {
            mEventManager.fire(new OnStopEvent());
        } finally {
            super.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            mEventManager.fire(new OnDestroyEvent());
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
        mEventManager.fire(new OnConfigurationChangedEvent(currentConfig, newConfig));
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ((TriainaApplication)getApplication()).getInjector(this).injectMembers(this);
        mEventManager.fire(new OnContentChangedEvent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mEventManager.fire(new OnActivityResultEvent(requestCode, resultCode, data));
    }
}
