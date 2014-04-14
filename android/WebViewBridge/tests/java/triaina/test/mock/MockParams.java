package triaina.test.mock;

import triaina.webview.entity.Params;
import android.os.Parcel;


public class MockParams implements Params {
	private String mAaa;
	private String mBbb;
	
	public void setAaa(String aaa) {
		mAaa = aaa;
	}
	
	public String getAaa() {
		return mAaa;
	}
	
	public void setBbb(String bbb) {
		mBbb = bbb;
	}
	
	public String getBbb() {
		return mBbb;
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

}
