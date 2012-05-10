package jp.mixi.triaina.commons.test.target;

import jp.mixi.triaina.commons.workerservice.Job;
import jp.mixi.triaina.commons.workerservice.Worker;
import jp.mixi.triaina.commons.workerservice.WorkerService;

public class WorkerServiceTarget extends WorkerService {
	public Worker<?> getWorkerTest(Job job) {
		return super.getWorker(job);
	}
}
