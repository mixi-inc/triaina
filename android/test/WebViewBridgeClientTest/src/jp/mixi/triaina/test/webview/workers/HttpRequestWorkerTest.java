package jp.mixi.triaina.test.webview.workers;

import jp.mixi.triaina.commons.workerservice.NetworkWorkerService;
import jp.mixi.triaina.test.mock.MockNetworkJob;
import android.content.Intent;
import android.os.Handler;
import android.test.ServiceTestCase;

public class HttpRequestWorkerTest extends ServiceTestCase<NetworkWorkerService> {
	private Handler mHandler;
	private Intent mIntent;
	
	public HttpRequestWorkerTest() {
		super(NetworkWorkerService.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mHandler = new Handler();
		
		mIntent = new Intent(getContext(), NetworkWorkerService.class);
		mIntent.putExtra(NetworkWorkerService.EXTRA_JOB, new MockNetworkJob());
	}

	public void testProcess() throws Exception {
		startService(mIntent);
		/*
		UsingNetworkWorkerService service = getService();
		HttpRequestWorker worker = new HttpRequestWorker();
		HttpRequestJob job = new HttpRequestJob();
		
		NetHttpSendParams params = new NetHttpSendParams();
		params.setUrl("http://172.17.202.21:3000/post");
		Bundle body = new Bundle();
		body.putString("aaa", "AAA");
		params.setBody(body);
		params.setMethod("POST");
		
		Bundle headers = new Bundle();
		headers.putString("Content-Type", "multipart/form-data");
		params.setHeaders(headers);
		job.setParams(params);
		
		worker.onNetworkStatusChanged(true);
		assertTrue(worker.isConnected());		
		
		worker.process(job, 0, 0, null, service, mHandler);
		*/
	}
}
