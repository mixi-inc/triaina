package triaina.webview.entity.device;

import triaina.commons.json.annotation.Exclude;
import triaina.webview.entity.Params;
import android.os.Parcel;
import android.os.Parcelable;

public class AccelerometerEnableParams implements Params {
	private Integer mRate;
	private String mCallback;
	
	public AccelerometerEnableParams() {}
	
	public AccelerometerEnableParams(Parcel source) {
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
	public static final Parcelable.Creator<AccelerometerEnableParams> CREATOR = new Parcelable.Creator<AccelerometerEnableParams>() {
		@Override
		public AccelerometerEnableParams createFromParcel(Parcel source) {
            return new AccelerometerEnableParams(source);
		}
		@Override
		public AccelerometerEnableParams[] newArray(int size) {
            return new AccelerometerEnableParams[size];
        }
    };
    
	@Override
	public int describeContents() {
		return 0;
	}
}
