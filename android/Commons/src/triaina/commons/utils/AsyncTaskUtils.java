package triaina.commons.utils;

import android.os.AsyncTask;

public final class AsyncTaskUtils {
    private AsyncTaskUtils() {}

    /** Returns whether the task specified as an argument is running or not.
     * @param theTask {@link AsyncTask} to be checked.
     * @return True if the task is running, false otherwise.
     */
    public static boolean isRunning(final AsyncTask<?, ?, ?> theTask) {
        return (theTask != null && 
                theTask.getStatus().equals(AsyncTask.Status.RUNNING));
    }

    /**
     * Abort the running task.
     *
     * @param theTask
     * @return True if the task has been successfully aborted. False otherwise.
     */
    public static boolean abort(AsyncTask<?, ?, ?> theTask) {
        if (!AsyncTaskUtils.isRunning(theTask)) {
            return false;
        }
        theTask.cancel(true);
        theTask = null;
        return true;
    }
}
