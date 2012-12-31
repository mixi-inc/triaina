package triaina.webview.entity.device;

import triaina.commons.json.annotation.Exclude;
import triaina.webview.entity.Params;
import android.os.Parcel;
import android.os.Parcelable;

public class NotificationNotifyParams implements Params {
	private String mIcon;
	private String mTitle;
	private String mText;
	private String mView;
	private String mValue;

	public NotificationNotifyParams() {
	}

	public NotificationNotifyParams(Parcel source) {
		mIcon = source.readString();
		mTitle = source.readString();
		mText = source.readString();
		mView = source.readString();
		mValue = source.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mIcon);
		dest.writeString(mTitle);
		dest.writeString(mText);
		dest.writeString(mView);
		dest.writeString(mValue);
	}

	public String getIcon() {
		return mIcon;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getText() {
		return mText;
	}

	public String getView() {
		return mView;
	}

	public String getValue() {
		return mValue;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Exclude
	public static final Parcelable.Creator<NotificationNotifyParams> CREATOR = new Parcelable.Creator<NotificationNotifyParams>() {
		@Override
		public NotificationNotifyParams createFromParcel(Parcel source) {
			return new NotificationNotifyParams(source);
		}

		@Override
		public NotificationNotifyParams[] newArray(int size) {
			return new NotificationNotifyParams[size];
		}
	};
}
