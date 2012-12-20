package triaina.webview.entity.device;

import triaina.commons.json.annotation.Exclude;
import triaina.webview.entity.Result;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class NetHttpSendResult implements Result {
	private String mCode;
	private Bundle mHeaders;
	private String mResponseText;

	public NetHttpSendResult() {}
	
	public NetHttpSendResult(Parcel source) {
		mCode = source.readString();
		mHeaders = source.readBundle();
		mResponseText = source.readString();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mCode);
		dest.writeBundle(mHeaders);
		dest.writeString(mResponseText);
	}
	
	@Exclude
	public static final Parcelable.Creator<NetHttpSendResult> CREATOR = new Parcelable.Creator<NetHttpSendResult>() {
        @Override
        public NetHttpSendResult createFromParcel(Parcel source) {
            return new NetHttpSendResult(source);
        }
        @Override
        public NetHttpSendResult[] newArray(int size) {
            return new NetHttpSendResult[size];
        }
    };
    
	@Override
	public int describeContents() {
		return 0;
	}

	public String getCode() {
		return mCode;
	}

	public void setCode(String code) {
		mCode = code;
	}

	public Bundle getHeaders() {
		return mHeaders;
	}

	public void setHeaders(Bundle headers) {
		mHeaders = headers;
	}

	public String getResponseText() {
		return mResponseText;
	}

	public void setResponseText(String responseText) {
		mResponseText = responseText;
	}
}
