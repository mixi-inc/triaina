package triaina.commons.test.workerservice;

import triaina.commons.test.mock.MockJob;
import triaina.commons.workerservice.NetworkWorkerService;
import android.content.Intent;
import android.test.ServiceTestCase;

public class NetowrkWorkerServiceTest extends
		ServiceTestCase<NetworkWorkerService> {

	public NetowrkWorkerServiceTest() {
		super(NetworkWorkerService.class);
	}

	public void testOnCreate() {
		MockJob job = new MockJob();
		Intent intent = new Intent(getContext(), NetworkWorkerService.class);
		intent.putExtra(NetworkWorkerService.EXTRA_JOB, job);
		startService(intent);
		
		NetworkWorkerService service = this.getService();
		assertNotNull(service);
	}
}
