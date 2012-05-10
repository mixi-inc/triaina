package jp.mixi.triaina.webview.entity.device;

import jp.mixi.triaina.commons.json.annotation.Exclude;
import jp.mixi.triaina.webview.entity.Result;
import android.os.Parcel;
import android.os.Parcelable;

public class WiFiGetDeviceAddressResult implements Result {
	private String mDeviceAddress;
	
	public WiFiGetDeviceAddressResult() {}
	
	public WiFiGetDeviceAddressResult(Parcel source) {
		mDeviceAddress = source.readString();
	}
	
	public void setDeviceAddress(String deviceAddress) {
		mDeviceAddress = deviceAddress;
	}
	
	public String getDeviceAddress() {
		return mDeviceAddress;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mDeviceAddress);
	}
	
	@Exclude
	public static final Parcelable.Creator<WiFiGetDeviceAddressResult> CREATOR = new Parcelable.Creator<WiFiGetDeviceAddressResult>() {
        @Override
        public WiFiGetDeviceAddressResult createFromParcel(Parcel source) {
            return new WiFiGetDeviceAddressResult(source);
        }
        @Override
        public WiFiGetDeviceAddressResult[] newArray(int size) {
            return new WiFiGetDeviceAddressResult[size];
        }
    };
    
	@Override
	public int describeContents() {
		return 0;
	}
}
