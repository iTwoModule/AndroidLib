package ink.itwo.android.common.widget.imgRes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by wang
 * on 2018/6/11
 */
public class ImageResImpl implements ImageResource, Serializable, Parcelable {
    private String imageURL;

    public ImageResImpl(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public Object thumbnailUrl() {
        return imageURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageURL);
    }

    protected ImageResImpl(Parcel in) {
        this.imageURL = in.readString();
    }

    public static final Creator<ImageResImpl> CREATOR = new Creator<ImageResImpl>() {
        @Override
        public ImageResImpl createFromParcel(Parcel source) {
            return new ImageResImpl(source);
        }

        @Override
        public ImageResImpl[] newArray(int size) {
            return new ImageResImpl[size];
        }
    };
}
