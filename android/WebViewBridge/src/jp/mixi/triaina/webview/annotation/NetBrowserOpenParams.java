package jp.mixi.triaina.webview.annotation;

import android.os.Parcel;
import android.os.Parcelable;
import jp.mixi.triaina.commons.json.annotation.Exclude;
import jp.mixi.triaina.webview.entity.Params;

public class NetBrowserOpenParams implements Params {
	private String mUrl;
	
	public NetBrowserOpenParams() {}
	
	public NetBrowserOpenParams(Parcel parcel) {
		mUrl = parcel.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mUrl);
	}
	
	public String getUrl() {
		return mUrl;
	}
	
	public void setUrl(String url) {
		mUrl = url;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Exclude
	public static final Parcelable.Creator<NetBrowserOpenParams> CREATOR = new Parcelable.Creator<NetBrowserOpenParams>() {
		@Override
		public NetBrowserOpenParams createFromParcel(Parcel source) {
			return new NetBrowserOpenParams(source);
		}
        
		@Override
		public NetBrowserOpenParams[] newArray(int size) {
			return new NetBrowserOpenParams[size];
        }
    };
}
