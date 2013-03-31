package triaina.injector.activity.event;

import android.os.Bundle;

/**
 * Event representing the execution of an activity onPostCreate().
 */
public class OnPostCreateEvent {
    private final Bundle savedInstanceState;

    public OnPostCreateEvent (final Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
    }

    public Bundle getSavedInstanceState () {
        return savedInstanceState;
    }
}