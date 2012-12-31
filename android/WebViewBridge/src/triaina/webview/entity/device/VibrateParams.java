package triaina.webview.entity.device;

import triaina.commons.json.annotation.Exclude;
import triaina.webview.entity.Params;
import android.os.Parcel;
import android.os.Parcelable;

public class VibrateParams implements Params {
	private Long[] mPattern;
	private Integer mRepeat;
	private Long mMsec;
	
	public VibrateParams() {}
	
	public VibrateParams(Parcel source) {
		mPattern = (Long[])source.readArray(VibrateParams.class.getClassLoader());
		mRepeat = source.readInt();
		mMsec = source.readLong();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeArray(mPattern);
		dest.writeInt(mRepeat);
		dest.writeLong(mMsec);
	}
	
	public Long[] getPattern() {
		return mPattern;
	}
	
	public void setPattern(Long[] pattern) {
		mPattern = pattern;
	}
	
	public void setRepeat(Integer repeat) {
		mRepeat = repeat;
	}
	
	public Integer getRepeat() {
		return mRepeat;
	}
	
	public Long getMsec() {
		return mMsec;
	}
	
	public void setMillisecound(Long msec) {
		mMsec = msec;
	}

	@Exclude
	public static final Parcelable.Creator<VibrateParams> CREATOR = new Parcelable.Creator<VibrateParams>() {
        @Override
        public VibrateParams createFromParcel(Parcel source) {
            return new VibrateParams(source);
        }
        @Override
        public VibrateParams[] newArray(int size) {
            return new VibrateParams[size];
        }
    };
    
	@Override
	public int describeContents() {
		return 0;
	}
}
