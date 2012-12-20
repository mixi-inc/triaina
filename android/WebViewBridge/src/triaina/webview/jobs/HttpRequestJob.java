package triaina.webview.jobs;

import android.os.Parcel;

import android.os.Parcelable;
import triaina.commons.workerservice.Job;
import triaina.commons.workerservice.annotation.Assign;
import triaina.webview.entity.device.NetHttpSendParams;
import triaina.webview.workers.HttpRequestWorker;

@Assign(worker=HttpRequestWorker.class)
public class HttpRequestJob implements Job {
	private NetHttpSendParams mParams;
	private String mCookie;
	
	public HttpRequestJob() {}
	
	public HttpRequestJob(Parcel source) {
		mParams = source.readParcelable(NetHttpSendParams.class.getClassLoader());
		mCookie = source.readString();
	}
	
	public NetHttpSendParams getParams() {
		return mParams;
	}
	
	public void setParams(NetHttpSendParams params) {
		mParams = params;
	}
	
	public String getCookie() {
		return mCookie;
	}
	
	public void setCookie(String cookie) {
		mCookie = cookie;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(mParams, 0);
		dest.writeString(mCookie);
	}
	
	public static final Parcelable.Creator<HttpRequestJob> CREATOR = new Parcelable.Creator<HttpRequestJob>() {
        @Override
        public HttpRequestJob createFromParcel(Parcel source) {
            return new HttpRequestJob(source);
        }
        @Override
        public HttpRequestJob[] newArray(int size) {
            return new HttpRequestJob[size];
        }
    };
	
	@Override
	public int describeContents() {
		return 0;
	}
}
