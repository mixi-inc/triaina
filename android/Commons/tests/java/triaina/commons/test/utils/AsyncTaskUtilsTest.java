
package triaina.commons.test.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import triaina.commons.utils.AsyncTaskUtils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.test.AndroidTestCase;

public class AsyncTaskUtilsTest extends AndroidTestCase {
    public static class PseudoActivity extends Thread {
        private TestTask theTask;
        private final CountDownLatch loadLatch = new CountDownLatch(1);
        private final CountDownLatch startLatch = new CountDownLatch(1);
        private final CountDownLatch checkLatch = new CountDownLatch(1);
        private final CountDownLatch endLatch = new CountDownLatch(1);
        public Handler mHandler;
        public void run() {
            Looper.prepare();
            theTask = new TestTask();
            loadLatch.countDown();
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // do nothing
                }
            };
            Looper.loop();
        }
        public AsyncTask<Void, Void, Void> getTask() {
            return theTask;
        }
        public void startSync() throws InterruptedException {
            start();
            loadLatch.await(5000, TimeUnit.MILLISECONDS);
        }
        public void waitForStart() throws InterruptedException {
            startLatch.await(5000, TimeUnit.MILLISECONDS);
        }
        public boolean isStarted() {
            return startLatch.getCount() == 0;
        }
        public void checkPoint() {
            checkLatch.countDown();
        }
        public void waitForEnd() throws InterruptedException {
            endLatch.await(5000, TimeUnit.MILLISECONDS);
        }
        public boolean isEnded() {
            return endLatch.getCount() == 0;
        }
        private class TestTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... params) {
                startLatch.countDown();
                try {
                    checkLatch.await(5000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    fail("interrupted");
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                endLatch.countDown();
            }
        };
    }
    
    public void testIsRunning() {
        assertFalse("null is false", AsyncTaskUtils.isRunning(null));
        
        PseudoActivity a = new PseudoActivity();
        try {
            a.startSync();
        } catch (InterruptedException e1) {
            fail("interrupted");
        }
        assertFalse("not start", AsyncTaskUtils.isRunning(a.getTask()));

        // start and wait for the task start running
        a.getTask().execute();

        try {
            a.waitForStart();
        } catch (InterruptedException e) {
            fail("interrupted");
        }
        assertTrue("the task has been started", a.isStarted());
        assertTrue("running", AsyncTaskUtils.isRunning(a.getTask()));
        
        a.checkPoint();
        
        // wait for the task ends
        try {
            a.waitForEnd();
        } catch (InterruptedException e) {
            fail("interrupted");
        }
        assertTrue("the task was finished", a.isEnded());
        
        // probably some time required to change the state
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            fail("interrupted");
        }
        
        assertFalse("finished", AsyncTaskUtils.isRunning(a.getTask()));
    }
}
