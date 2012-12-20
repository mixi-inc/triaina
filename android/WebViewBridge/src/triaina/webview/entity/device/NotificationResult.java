package triaina.webview.entity.device;

import android.os.Parcel;
import android.os.Parcelable;
import triaina.commons.json.annotation.Exclude;
import triaina.webview.entity.Result;

public class NotificationResult implements Result {
	private Integer mId;

	public NotificationResult() {
	}
	
	public NotificationResult(Integer id) {
		mId = id;
	}

	public NotificationResult(Parcel source) {
		mId = source.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mId);
	}
	
	public void setId(Integer id) {
		mId = id;
	}
	
	public Integer getId() {
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
