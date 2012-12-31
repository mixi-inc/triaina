package triaina.webview.entity.device;

import android.os.Parcel;
import android.os.Parcelable;
import triaina.commons.json.annotation.Exclude;
import triaina.webview.entity.Params;

public class ToastParams implements Params {
    private String mText;
    private Integer mDuration;

    public ToastParams() {
    }

    public ToastParams(Parcel source) {
        mText = source.readString();
        mDuration = source.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mText);
        dest.writeInt(mDuration);
    }

    public String getText() {
        return mText;
    }

    public Integer getDuration() {
        return mDuration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Exclude
    public static final Parcelable.Creator<ToastParams> CREATOR = new Parcelable.Creator<ToastParams>() {
        @Override
        public ToastParams createFromParcel(Parcel source) {
            return new ToastParams(source);
        }

        @Override
        public ToastParams[] newArray(int size) {
            return new ToastParams[size];
        }
    };

}
