package triaina.webview.entity.device;

import android.os.Parcel;
import android.os.Parcelable;
import triaina.commons.json.annotation.Exclude;
import triaina.webview.entity.Result;

public class NotificationResult implements Result {
	private String mId;

	public NotificationResult() {
	}
	
	public NotificationResult(String id) {
		mId = id;
	}

	public NotificationResult(Parcel source) {
		mId = source.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId);
	}
	
	public void setId(String id) {
		mId = id;
	}
	
	public String getId() {
		return mId;
	}

	@Exclude
	public static final Parcelable.Creator<NotificationResult> CREATOR = new Parcelable.Creator<NotificationResult>() {
		@Override
		public NotificationResult createFromParcel(Parcel source) {
			return new NotificationResult(source);
		}

		@Override
		public NotificationResult[] newArray(int size) {
			return new NotificationResult[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}
}
