package jp.mixi.triaina.webview.entity.web;

import jp.mixi.triaina.commons.json.annotation.Exclude;
import jp.mixi.triaina.webview.entity.Params;
import android.os.Parcel;
import android.os.Parcelable;

public class WiFiP2PDiscoverParams implements Params {
	private WiFiP2PDeviceParams[] mDevices;

	public WiFiP2PDiscoverParams() {
	}

	public WiFiP2PDiscoverParams(Parcel src) {
		mDevices = (WiFiP2PDeviceParams[]) src.readArray(getClass().getClassLoader());
	}
	
	public void setDevices(WiFiP2PDeviceParams[] devices) {
		mDevices = devices;
	}

	public WiFiP2PDeviceParams[] getDevices() {
		return mDevices;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeArray(mDevices);
	}

	@Exclude
	public static final Parcelable.Creator<WiFiP2PDiscoverParams> CREATOR = new Parcelable.Creator<WiFiP2PDiscoverParams>() {
		@Override
		public WiFiP2PDiscoverParams createFromParcel(Parcel source) {
			return new WiFiP2PDiscoverParams(source);
		}

		@Override
		public WiFiP2PDiscoverParams[] newArray(int size) {
			return new WiFiP2PDiscoverParams[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}
}
