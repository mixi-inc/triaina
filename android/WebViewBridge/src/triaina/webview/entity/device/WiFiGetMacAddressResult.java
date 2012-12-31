package triaina.webview.entity.device;

import triaina.commons.json.annotation.Exclude;
import triaina.webview.entity.Result;
import android.os.Parcel;
import android.os.Parcelable;

public class WiFiGetMacAddressResult implements Result {
	private String mMacAddress;
	
	public WiFiGetMacAddressResult() {}
	
	public WiFiGetMacAddressResult(Parcel source) {
		mMacAddress = source.readString();
	}
	
	public void setMacAddress(String macAddress) {
		mMacAddress = macAddress;
	}
	
	public String getMacAddress() {
		return mMacAddress;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mMacAddress);
	}
	
	@Exclude
	public static final Parcelable.Creator<WiFiGetMacAddressResult> CREATOR = new Parcelable.Creator<WiFiGetMacAddressResult>() {
        @Override
        public WiFiGetMacAddressResult createFromParcel(Parcel source) {
            return new WiFiGetMacAddressResult(source);
        }
        @Override
        public WiFiGetMacAddressResult[] newArray(int size) {
            return new WiFiGetMacAddressResult[size];
        }
    };
    
	@Override
	public int describeContents() {
		return 0;
	}
}
