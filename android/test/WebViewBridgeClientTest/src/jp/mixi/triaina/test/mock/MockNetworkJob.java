package jp.mixi.triaina.test.mock;

import android.os.Parcel;
import android.os.Parcelable;
import jp.mixi.triaina.commons.json.annotation.Exclude;
import jp.mixi.triaina.commons.workerservice.Job;
import jp.mixi.triaina.commons.workerservice.annotation.Assign;

@Assign(worker=MockNetworkWorker.class)
public class MockNetworkJob implements Job {
	private String data;

	public MockNetworkJob() {}
	
	public MockNetworkJob(Parcel source) {
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
	public static final Parcelable.Creator<MockNetworkJob> CREATOR = new Parcelable.Creator<MockNetworkJob>() {
        @Override
        public MockNetworkJob createFromParcel(Parcel source) {
            return new MockNetworkJob(source);
        }
        @Override
        public MockNetworkJob[] newArray(int size) {
            return new MockNetworkJob[size];
        }
    };
}
