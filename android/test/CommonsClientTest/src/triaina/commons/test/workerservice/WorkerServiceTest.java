package triaina.commons.test.workerservice;

import triaina.commons.test.mock.MockJob;
import triaina.commons.test.mock.MockWorker;
import triaina.commons.test.mock.MockWorkerDecorator;
import triaina.commons.test.target.WorkerServiceTarget;
import triaina.commons.workerservice.WorkerService;
import android.content.Intent;
import android.test.ServiceTestCase;

public class WorkerServiceTest extends ServiceTestCase<WorkerServiceTarget> {

	public WorkerServiceTest() {
		super(WorkerServiceTarget.class);
	}

	public void testOnCreate() {
		MockJob job = new MockJob();
		Intent intent = new Intent(getContext(), WorkerServiceTarget.class);
		intent.putExtra(WorkerService.EXTRA_JOB, job);
		startService(intent);
		
		WorkerService service = this.getService();
		assertNotNull(service);
	}
	
	public void testGetWorker() throws Exception {
		MockJob job = new MockJob();
		Intent intent = new Intent(getContext(), WorkerServiceTarget.class);
		intent.putExtra(WorkerService.EXTRA_JOB, job);
		startService(intent);
		
		WorkerServiceTarget service = this.getService();
		job = new MockJob();
		MockWorkerDecorator decorator = (MockWorkerDecorator)service.getWorkerTest(job);
		MockWorker worker = decorator.getMockWorker();
		assertNotNull(worker);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		//shutdownService();
		super.tearDown();
	}
}
