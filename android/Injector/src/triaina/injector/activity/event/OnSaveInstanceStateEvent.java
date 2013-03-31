package triaina.injector.activity.event;

import android.os.Bundle;

/**
 * Event representing the execution of an activity onSaveInstanceState().
 */
public class OnSaveInstanceStateEvent {
    private final Bundle outInstanceState;

    public OnSaveInstanceStateEvent (final Bundle outInstanceState) {
        this.outInstanceState = outInstanceState;
    }

    public Bundle getOutInstanceState () {
        return outInstanceState;
    }
}