package triaina.injector.activity;

import android.accounts.AccountAuthenticatorActivity;
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
import triaina.injector.TriainaInjector;
import triaina.injector.TriainaInjectorFactory;
import triaina.injector.activity.event.OnPostCreateEvent;
import triaina.injector.activity.event.OnRestoreInstanceStateEvent;
import triaina.injector.activity.event.OnSaveInstanceStateEvent;

import javax.inject.Inject;

public class TriainaAccountAuthenticatorActivity extends AccountAuthenticatorActivity {
    protected EventManager mEventManager;

    @Inject ContentViewListener ignored; // BUG find a better place to put this

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TriainaInjector injector = TriainaInjectorFactory.getInjector(this);
        mEventManager = injector.getInstance(EventManager.class);
        injector.injectMembersWithoutViews(this);
        super.onCreate(savedInstanceState);
        mEventManager.fire(new OnCreateEvent(savedInstanceState));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mEventManager.fire(new OnPostCreateEvent(savedInstanceState));
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
                TriainaInjectorFactory.destroyInjector(this);
            } finally {
                super.onDestroy();
            }
        }
    }

    @Override
    protected void onSaveInstanceState (final Bundle outState) {
        super.onSaveInstanceState(outState);
        mEventManager.fire(new OnSaveInstanceStateEvent(outState));
    }

    @Override
    protected void onRestoreInstanceState (final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mEventManager.fire(new OnRestoreInstanceStateEvent(savedInstanceState));
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
        TriainaInjectorFactory.getInjector(this).injectMembers(this);
        mEventManager.fire(new OnContentChangedEvent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mEventManager.fire(new OnActivityResultEvent(requestCode, resultCode, data));
    }
}
