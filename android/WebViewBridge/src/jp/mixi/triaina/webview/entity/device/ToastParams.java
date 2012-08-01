package jp.mixi.triaina.webview.entity.device;

import android.os.Parcel;
import android.os.Parcelable;
import jp.mixi.triaina.commons.json.annotation.Exclude;
import jp.mixi.triaina.webview.entity.Params;

public class ToastParams implements Params {
	private String mMessage;

	public ToastParams() {}
	
	public ToastParams(Parcel source) {
		mMessage = source.readString();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mMessage);
	}
	
	public String getMessage() {
		return mMessage;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Exclude
	public static final Parcelable.Creator<ToastParams> CREATOR = new Parcelable.Creator<ToastParams>() {
        @Override
        public ToastParams createFromParcel(Parcel source) {
            return new ToastParams(source);
        }
        @Override
        public ToastParams[] newArray(int size) {
            return new ToastParams[size];
        }
    };

}
