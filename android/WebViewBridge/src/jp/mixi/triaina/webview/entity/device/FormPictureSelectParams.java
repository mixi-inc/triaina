package jp.mixi.triaina.webview.entity.device;

import jp.mixi.triaina.commons.json.annotation.Exclude;
import jp.mixi.triaina.webview.entity.Params;
import android.os.Parcel;
import android.os.Parcelable;

public class FormPictureSelectParams implements Params {
	private String mId;
	private Integer mMax;
	private String[] mFilter;
	private String[] mSelected;
	
	public FormPictureSelectParams() {}
	
	public FormPictureSelectParams(Parcel source) {
		mId = source.readString();
		mMax = source.readInt();
		mFilter = source.createStringArray();
		mSelected = source.createStringArray();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId);
		dest.writeInt(mMax);
		dest.writeStringArray(mFilter);
		dest.writeStringArray(mSelected);
	}
	
	public String getId() {
		return mId;
	}
	
	public void setId(String mId) {
		this.mId = mId;
	}
	
	public Integer getMax() {
		return mMax;
	}
	
	public void setMax(Integer mMax) {
		this.mMax = mMax;
	}
	
	public String[] getFilter() {
		return mFilter;
	}
	
	public void setFilter(String[] mFilter) {
		this.mFilter = mFilter;
	}
	
	public String[] getSelected() {
		return mSelected;
	}
	
	public void setSelected(String[] selected) {
		mSelected = selected;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Exclude
	public static final Parcelable.Creator<FormPictureSelectParams> CREATOR = new Parcelable.Creator<FormPictureSelectParams>() {
        @Override
        public FormPictureSelectParams createFromParcel(Parcel source) {
            return new FormPictureSelectParams(source);
        }
        @Override
        public FormPictureSelectParams[] newArray(int size) {
            return new FormPictureSelectParams[size];
        }
    };
}
