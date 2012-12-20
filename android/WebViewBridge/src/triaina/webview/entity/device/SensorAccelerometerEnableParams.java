package triaina.webview.entity.device;

import triaina.commons.json.annotation.Exclude;
import triaina.webview.entity.Params;
import android.os.Parcel;
import android.os.Parcelable;

public class SensorAccelerometerEnableParams implements Params {
	private Integer mRate;
	private String mCallback;
	
	public SensorAccelerometerEnableParams() {}
	
	public SensorAccelerometerEnableParams(Parcel source) {
		mRate = source.readInt();
		mCallback = source.readString();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mRate);
		dest.writeString(mCallback);
	}
	
	public Integer getRate() {
		return mRate;
	}
	
	public String getCallback() {
		return mCallback;
	}
	
	@Exclude
	public static final Parcelable.Creator<SensorAccelerometerEnableParams> CREATOR = new Parcelable.Creator<SensorAccelerometerEnableParams>() {
		@Override
		public SensorAccelerometerEnableParams createFromParcel(Parcel source) {
            return new SensorAccelerometerEnableParams(source);
		}
		@Override
		public SensorAccelerometerEnableParams[] newArray(int size) {
            return new SensorAccelerometerEnableParams[size];
        }
    };
    
	@Override
	public int describeContents() {
		return 0;
	}
}
