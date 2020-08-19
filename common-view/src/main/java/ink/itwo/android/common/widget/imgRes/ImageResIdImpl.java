package ink.itwo.android.common.widget.imgRes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author wang
 * on 2018/10/22
 */

public class ImageResIdImpl implements   ImageResource, Serializable, Parcelable {
    private int resId;

    public ImageResIdImpl(int resId) {
        this.resId = resId;
    }

    @Override
    public Object thumbnailUrl() {
        return resId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.resId);
    }

    protected ImageResIdImpl(Parcel in) {
        this.resId = in.readInt();
    }

    public static final Creator<ImageResIdImpl> CREATOR = new Creator<ImageResIdImpl>() {
        @Override
        public ImageResIdImpl createFromParcel(Parcel source) {
            return new ImageResIdImpl(source);
        }

        @Override
        public ImageResIdImpl[] newArray(int size) {
            return new ImageResIdImpl[size];
        }
    };
}
