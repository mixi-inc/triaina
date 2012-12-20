package triaina.webview.entity.web;

import triaina.commons.json.annotation.Exclude;
import triaina.webview.entity.Params;
import android.os.Parcel;
import android.os.Parcelable;

public class WiFiP2PDeviceParams implements Params {
	private String mDeviceName;
	private String mDeviceAddress;

	public WiFiP2PDeviceParams() {}
	
	public WiFiP2PDeviceParams(Parcel src) {
		mDeviceName = src.readString();
		mDeviceAddress = src.readString();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mDeviceName);
		dest.writeString(mDeviceAddress);
	}

	public String getDeviceName() {
		return mDeviceName;
	}
	
	public void setDeviceName(String deviceName) {
		mDeviceName = deviceName;
	}
		
	public String getDeviceAddress() {
		return mDeviceAddress;
	}
	
	public void setDeviceAddress(String deviceAddress) {
		mDeviceAddress = deviceAddress;
	}
	
	@Exclude
	public static final Parcelable.Creator<WiFiP2PDeviceParams> CREATOR = new Parcelable.Creator<WiFiP2PDeviceParams>() {
        @Override
        public WiFiP2PDeviceParams createFromParcel(Parcel source) {
            return new WiFiP2PDeviceParams(source);
        }
        @Override
        public WiFiP2PDeviceParams[] newArray(int size) {
            return new WiFiP2PDeviceParams[size];
        }
    };
    
	@Override
	public int describeContents() {
		return 0;
	}
}
