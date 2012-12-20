package triaina.webview.entity;

import triaina.commons.json.annotation.Exclude;

import triaina.commons.proguard.NoProGuard;
import android.os.Parcel;
import android.os.Parcelable;

public class Error implements Parcelable, NoProGuard {
	private String mCode;
	private String mMessage;
	private String mData;
	
	public Error(String code, String msg) {
		this(code, msg, null);
	}

	public Error(String code, String msg, String data) {
		mCode = code;
		mMessage = msg;
		mData = data;
	}
	
	public Error(Parcel source) {
		mCode = source.readString();
		mMessage = source.readString();
		mData = source.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mCode);
		dest.writeString(mMessage);
		dest.writeString(mData);
	}
	
	public String getCode() {
		return mCode;
	}
	
	public void setCode(String code) {
		mCode = code;
	}

	public String getMessage() {
		return mMessage;
	}
	
	public void setMessage(String message) {
		mMessage = message;
	}

	public String getData() {
		return mData;
	}
	
	public void setData(String data) {
		mData = data;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Exclude
	public static final Parcelable.Creator<Error> CREATOR = new Parcelable.Creator<Error>() {
        @Override
        public Error createFromParcel(Parcel source) {
            return new Error(source);
        }
        @Override
        public Error[] newArray(int size) {
            return new Error[size];
        }
    };
}
