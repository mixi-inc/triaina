package triaina.webview.entity.device;

import android.os.Parcel;
import android.os.Parcelable;
import triaina.commons.json.annotation.Exclude;
import triaina.webview.entity.Params;

public class SendNotificationParams implements Params {
    private String mProgress;
    private String mCompleted;
    private String mFailed;
    private String mSummary;

    public SendNotificationParams() {
    }

    public SendNotificationParams(Parcel src) {
        mProgress = src.readString();
        mCompleted = src.readString();
        mFailed = src.readString();
        mSummary = src.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mProgress);
        dest.writeString(mCompleted);
        dest.writeString(mFailed);
        dest.writeString(mSummary);
    }

    public String getProgress() {
        return mProgress;
    }

    public String getCompleted() {
        return mCompleted;
    }

    public String getFailed() {
        return mFailed;
    }

    public String getSummary() {
        return mSummary;
    }

    public int describeContents() {
        return 0;
    }

    @Exclude
    public static final Parcelable.Creator<SendNotificationParams> CREATOR = new Parcelable.Creator<SendNotificationParams>() {
        @Override
        public SendNotificationParams createFromParcel(Parcel source) {
            return new SendNotificationParams(source);
        }

        @Override
        public SendNotificationParams[] newArray(int size) {
            return new SendNotificationParams[size];
        }
    };
}
