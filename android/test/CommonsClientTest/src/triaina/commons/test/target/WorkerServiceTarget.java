package triaina.commons.test.target;

import triaina.commons.workerservice.Job;
import triaina.commons.workerservice.Worker;
import triaina.commons.workerservice.WorkerService;

public class WorkerServiceTarget extends WorkerService {
	public Worker<?> getWorkerTest(Job job) {
		return super.getWorker(job);
	}
}
