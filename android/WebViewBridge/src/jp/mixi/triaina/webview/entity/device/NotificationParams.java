package jp.mixi.triaina.webview.entity.device;

import jp.mixi.triaina.commons.json.annotation.Exclude;
import jp.mixi.triaina.webview.entity.Params;
import android.os.Parcel;
import android.os.Parcelable;

public class NotificationParams implements Params {
	private Integer mMessageLevel;
	private String mMessage;

	public NotificationParams() {}
	
	public NotificationParams(Parcel source) {
		mMessageLevel = source.readInt();
		mMessage = source.readString();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mMessageLevel);
		dest.writeString(mMessage);
	}
	
	public Integer getMessageLevel() {
		return mMessageLevel;
	}
	
	public String getMessage() {
		return mMessage;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Exclude
	public static final Parcelable.Creator<NotificationParams> CREATOR = new Parcelable.Creator<NotificationParams>() {
        @Override
        public NotificationParams createFromParcel(Parcel source) {
            return new NotificationParams(source);
        }
        @Override
        public NotificationParams[] newArray(int size) {
            return new NotificationParams[size];
        }
    };
}
