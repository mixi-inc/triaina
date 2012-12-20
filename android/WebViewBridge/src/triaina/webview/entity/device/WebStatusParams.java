package triaina.webview.entity.device;

import triaina.commons.json.annotation.Exclude;
import triaina.webview.entity.Params;
import android.os.Parcel;
import android.os.Parcelable;

public class WebStatusParams implements Params {
	
	public WebStatusParams() {}
	
	public WebStatusParams(Parcel source) {
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
	
	@Exclude
	public static final Parcelable.Creator<WebStatusParams> CREATOR = new Parcelable.Creator<WebStatusParams>() {
        @Override
        public WebStatusParams createFromParcel(Parcel source) {
            return new WebStatusParams(source);
        }
        @Override
        public WebStatusParams[] newArray(int size) {
            return new WebStatusParams[size];
        }
    };
}
