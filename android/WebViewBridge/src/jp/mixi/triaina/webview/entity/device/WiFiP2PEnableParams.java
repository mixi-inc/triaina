package jp.mixi.triaina.webview.entity.device;

import jp.mixi.triaina.commons.json.annotation.Exclude;
import jp.mixi.triaina.webview.entity.Params;
import android.os.Parcel;
import android.os.Parcelable;

public class WiFiP2PEnableParams implements Params {
	private String mDiscoverCallback;
	
	public WiFiP2PEnableParams() {}

	public WiFiP2PEnableParams(Parcel src) {
		mDiscoverCallback = src.readString();
	}
	
	public String getDiscoverCallback() {
		return mDiscoverCallback;
	}
	
	@Override
    public void writeToParcel(Parcel dest, int flags) {
	    dest.writeString(mDiscoverCallback);
    }
	
	@Exclude
	public static final Parcelable.Creator<WiFiP2PEnableParams> CREATOR = new Parcelable.Creator<WiFiP2PEnableParams>() {
		@Override
		public WiFiP2PEnableParams createFromParcel(Parcel source) {
            return new WiFiP2PEnableParams(source);
		}
		@Override
		public WiFiP2PEnableParams[] newArray(int size) {
            return new WiFiP2PEnableParams[size];
        }
    };

	@Override
    public int describeContents() {
	    return 0;
	}
}
