package triaina.webview.entity.device;

import triaina.commons.json.annotation.Exclude;
import triaina.webview.entity.Params;
import android.os.Parcel;
import android.os.Parcelable;

public class NotificationParams implements Params {
	private Integer mPriority;
	private String mTitle;
	private String mText;

	public NotificationParams() {}
	
	public NotificationParams(Parcel source) {
		mPriority = source.readInt();
		mTitle = source.readString();
		mText = source.readString();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mPriority);
		dest.writeString(mTitle);
		dest.writeString(mText);
	}
	
	public Integer getPriority() {
		return mPriority;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public String getText() {
		return mText;
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
