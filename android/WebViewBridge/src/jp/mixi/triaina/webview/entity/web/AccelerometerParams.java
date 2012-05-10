package jp.mixi.triaina.webview.entity.web;

import jp.mixi.triaina.commons.json.annotation.Exclude;
import jp.mixi.triaina.webview.entity.Params;
import android.os.Parcel;
import android.os.Parcelable;

public class AccelerometerParams implements Params {
	private Double mX;
	private Double mY;
	private Double mZ;
	
	public AccelerometerParams() {}
	
	public AccelerometerParams(Double x, Double y, Double z) {
		mX = x;
		mY = y;
		mZ = z;
	}
	
	public AccelerometerParams(Parcel source) {
		mX = source.readDouble();
		mY = source.readDouble();
		mZ = source.readDouble();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(mX);
		dest.writeDouble(mY);
		dest.writeDouble(mZ);
	}
	
	public void setX(Double x) {
		mX = x;
	}
	
	public void setY(Double y) {
		mY = y;
	}
	
	public void setZ(Double z) {
		mZ = z;
	}
	
	public Double getX() {
		return mX;
	}
	
	public Double getY() {
		return mY;
	}
	
	public Double getZ() {
		return mZ;
	}
	
	@Exclude
	public static final Parcelable.Creator<AccelerometerParams> CREATOR = new Parcelable.Creator<AccelerometerParams>() {
		@Override
		public AccelerometerParams createFromParcel(Parcel source) {
            return new AccelerometerParams(source);
		}
		@Override
		public AccelerometerParams[] newArray(int size) {
            return new AccelerometerParams[size];
        }
    };
    
	@Override
	public int describeContents() {
		return 0;
	}
}
