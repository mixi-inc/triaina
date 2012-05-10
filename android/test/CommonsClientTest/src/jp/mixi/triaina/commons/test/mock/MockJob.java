package jp.mixi.triaina.commons.test.mock;

import android.os.Parcel;
import android.os.Parcelable;
import jp.mixi.triaina.commons.json.annotation.Exclude;
import jp.mixi.triaina.commons.workerservice.Job;
import jp.mixi.triaina.commons.workerservice.annotation.Assign;

@Assign(worker=MockWorker.class, decorators={MockWorkerDecorator.class})
public class MockJob implements Job {
	private String data;

	public MockJob() {}
	
	public MockJob(Parcel source) {
		data = source.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(data);
	}
	
	@Exclude
	public static final Parcelable.Creator<MockJob> CREATOR = new Parcelable.Creator<MockJob>() {
        @Override
        public MockJob createFromParcel(Parcel source) {
            return new MockJob(source);
        }
        @Override
        public MockJob[] newArray(int size) {
            return new MockJob[size];
        }
    };

}
