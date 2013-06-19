package triaina.webview.entity.device;

import triaina.commons.json.annotation.Exclude;
import triaina.webview.entity.Result;
import android.os.Parcel;
import android.os.Parcelable;

public class FormPictureSelectResult implements Result {
	private FormFile[] mFiles;
	
	public FormPictureSelectResult() {}
	
	public FormPictureSelectResult(Parcel source) {
		mFiles = (FormFile[])source.readArray(this.getClass().getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeArray(mFiles);
	}
	
	public FormFile[] getFiles() {
		return mFiles;
	}
	
	public void setFiles(FormFile[] files) {
		mFiles = files;
	}

	@Exclude
	public static final Parcelable.Creator<FormPictureSelectResult> CREATOR = new Parcelable.Creator<FormPictureSelectResult>() {
        @Override
        public FormPictureSelectResult createFromParcel(Parcel source) {
            return new FormPictureSelectResult(source);
        }
        @Override
        public FormPictureSelectResult[] newArray(int size) {
            return new FormPictureSelectResult[size];
        }
	};

	@Override
	public int describeContents() {
		return 0;
	}
	
	public static class FormFile implements Result {
		private String mId;
		private String mFileName;
		private String mThumbnail;
		
		public FormFile() {}
		
		public FormFile(Parcel source) {
			mId = source.readString();
			mFileName = source.readString();
			mThumbnail = source.readString();
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(mId);
			dest.writeString(mFileName);
			dest.writeString(mThumbnail);
		}

		public String getId() {
			return mId;
		}

		public void setId(String mId) {
			this.mId = mId;
		}

		public String getFileName() {
			return mFileName;
		}

		public void setFileName(String mFileName) {
			this.mFileName = mFileName;
		}
		
		public void setThumbnail(String thumbnail) {
			this.mThumbnail = thumbnail;
		}
		
		public String getThumbnail() {
			return this.mThumbnail;
		}
		
		@Exclude
		public static final Parcelable.Creator<FormFile> CREATOR = new Parcelable.Creator<FormFile>() {
	        @Override
	        public FormFile createFromParcel(Parcel source) {
	            return new FormFile(source);
	        }
	        @Override
	        public FormFile[] newArray(int size) {
	            return new FormFile[size];
	        }
		};
		
		@Override
		public int describeContents() {
			return 0;
		}
	}
}
