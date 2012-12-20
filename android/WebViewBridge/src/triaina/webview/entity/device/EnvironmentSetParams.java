package triaina.webview.entity.device;

import triaina.webview.entity.Params;
import android.os.Parcel;
import android.os.Parcelable;

public class EnvironmentSetParams implements Params {
	private String mName;
	private String mValue;

	public EnvironmentSetParams() {}
	
	public EnvironmentSetParams(Parcel source) {
		mName = source.readString();
		mValue = source.readString();
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public String getValue() {
		return mValue;
	}
	
	public void setValue(String value) {
		mValue = value;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mName);
		dest.writeString(mValue);
	}
	
	public static final Parcelable.Creator<EnvironmentSetParams> CREATOR = new Parcelable.Creator<EnvironmentSetParams>() {
        @Override
        public EnvironmentSetParams createFromParcel(Parcel source) {
            return new EnvironmentSetParams(source);
        }
        @Override
        public EnvironmentSetParams[] newArray(int size) {
            return new EnvironmentSetParams[size];
        }
    };
    
	@Override
	public int describeContents() {
		return 0;
	}
}
