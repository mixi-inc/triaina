package triaina.injector.activity.event;

import android.os.Bundle;

/**
 * Event representing the execution of an activity onRestoreInstanceState().
 */
public class OnRestoreInstanceStateEvent {
    private final Bundle savedInstanceState;

    public OnRestoreInstanceStateEvent (final Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
    }

    public Bundle getSavedInstanceState () {
        return savedInstanceState;
    }
}