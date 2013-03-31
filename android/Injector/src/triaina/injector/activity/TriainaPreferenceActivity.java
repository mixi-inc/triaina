package triaina.injector.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

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
import roboguice.inject.ContextScope;
import roboguice.inject.PreferenceListener;
import roboguice.service.event.OnDestroyEvent;
import roboguice.service.event.OnStartEvent;
import triaina.injector.TriainaInjector;
import triaina.injector.TriainaInjectorFactory;
import triaina.injector.activity.event.OnPostCreateEvent;
import triaina.injector.activity.event.OnRestoreInstanceStateEvent;
import triaina.injector.activity.event.OnSaveInstanceStateEvent;

import javax.inject.Inject;

public class TriainaPreferenceActivity extends PreferenceActivity {
    protected EventManager eventManager;
    protected PreferenceListener preferenceListener;

    @Inject ContentViewListener ignored; // BUG find a better place to put this

    /** {@inheritDoc } */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final TriainaInjector injector = TriainaInjectorFactory.getInjector(this);
        eventManager = injector.getInstance(EventManager.class);
        preferenceListener = injector.getInstance(PreferenceListener.class);
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
    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        super.setPreferenceScreen(preferenceScreen);

        final ContextScope scope = TriainaInjectorFactory.getInjector(this).getInstance(ContextScope.class);
        synchronized (ContextScope.class) {
            scope.enter(this);
            try {
                preferenceListener.injectPreferenceViews();
            } finally {
                scope.exit(this);
            }
        }
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
    protected void onNewIntent(Intent intent ) {
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
        TriainaInjectorFactory.getInjector(this).injectViewMembers(this);
        eventManager.fire(new OnContentChangedEvent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        eventManager.fire(new OnActivityResultEvent(requestCode, resultCode, data));
    }
}
