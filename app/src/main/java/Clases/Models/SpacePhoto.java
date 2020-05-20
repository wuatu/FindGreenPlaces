package Clases.Models;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chike on 2/11/2017.
 */

public class SpacePhoto implements Parcelable {

    private String mUrl;
    private String mTitle;
    SpacePhoto[] getSpacePhotos;
    public SpacePhoto() {
    }

    public SpacePhoto(String url, String title) {
        mUrl = url;
        mTitle = title;
    }

    protected SpacePhoto(Parcel in) {
        mUrl = in.readString();
        mTitle = in.readString();
    }

    public SpacePhoto(String mUrl, String mTitle, SpacePhoto[] getSpacePhotos) {
        this.mUrl = mUrl;
        this.mTitle = mTitle;
        this.getSpacePhotos = getSpacePhotos;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public SpacePhoto[] getGetSpacePhotos() {
        return getSpacePhotos;
    }

    public void setGetSpacePhotos(SpacePhoto[] getSpacePhotos) {
        this.getSpacePhotos = getSpacePhotos;
    }

    public static Creator<SpacePhoto> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<SpacePhoto> CREATOR = new Creator<SpacePhoto>() {
        @Override
        public SpacePhoto createFromParcel(Parcel in) {
            return new SpacePhoto(in);
        }

        @Override
        public SpacePhoto[] newArray(int size) {
            return new SpacePhoto[size];
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mUrl);
        parcel.writeString(mTitle);
    }
}
