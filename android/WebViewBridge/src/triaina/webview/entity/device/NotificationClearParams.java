package triaina.webview.entity.device;

import android.os.Parcel;
import android.os.Parcelable;
import triaina.commons.json.annotation.Exclude;
import triaina.webview.entity.Params;

public class NotificationClearParams implements Params {
	private String mId;

	public NotificationClearParams() {
	}

	public NotificationClearParams(Parcel source) {
		mId = source.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId);
	}

	public String getId() {
		return mId;
	}

	@Exclude
	public static final Parcelable.Creator<NotificationClearParams> CREATOR = new Parcelable.Creator<NotificationClearParams>() {
		@Override
		public NotificationClearParams createFromParcel(Parcel source) {
			return new NotificationClearParams(source);
		}

		@Override
		public NotificationClearParams[] newArray(int size) {
			return new NotificationClearParams[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}
}
