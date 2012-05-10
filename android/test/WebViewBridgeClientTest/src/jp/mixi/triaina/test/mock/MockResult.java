package jp.mixi.triaina.test.mock;

import jp.mixi.triaina.webview.entity.Result;
import android.os.Parcel;

public class MockResult implements Result {
	private String mAaa;
	private String mBbb;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}

	public String getAaa() {
		return mAaa;
	}

	public void setAaa(String mAaa) {
		this.mAaa = mAaa;
	}

	public String getBbb() {
		return mBbb;
	}

	public void setBbb(String mBbb) {
		this.mBbb = mBbb;
	}
}
