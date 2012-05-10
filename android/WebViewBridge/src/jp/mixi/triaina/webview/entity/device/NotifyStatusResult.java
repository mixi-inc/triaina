package jp.mixi.triaina.webview.entity.device;

import jp.mixi.triaina.commons.json.annotation.Exclude;
import jp.mixi.triaina.webview.entity.Result;
import android.os.Parcel;
import android.os.Parcelable;

public class NotifyStatusResult implements Result {
	private String mOrientation;
	
	public NotifyStatusResult() {}
	
	public NotifyStatusResult(Parcel source) {
		mOrientation = source.readString();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mOrientation);
	}
	
	public void setOrientation(String orientation) {
		mOrientation = orientation;
	}
	
	public String getOrientation() {
		return mOrientation;
	}
	
	@Exclude
	public static final Parcelable.Creator<NotifyStatusResult> CREATOR = new Parcelable.Creator<NotifyStatusResult>() {
        @Override
        public NotifyStatusResult createFromParcel(Parcel source) {
            return new NotifyStatusResult(source);
        }
        @Override
        public NotifyStatusResult[] newArray(int size) {
            return new NotifyStatusResult[size];
        }
    };
	
	@Override
	public int describeContents() {
		return 0;
	}
}
