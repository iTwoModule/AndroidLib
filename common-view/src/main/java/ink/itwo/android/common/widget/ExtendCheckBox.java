package ink.itwo.android.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.DimenRes;
import androidx.appcompat.widget.AppCompatCheckBox;

import ink.itwo.android.common.control.R;


/**扩大checkBox点击范围
 * Created by wang on 17/9/15.
 */

public class ExtendCheckBox extends AppCompatCheckBox {

    private int mDrawableHeight;//xml文件中设置的高度
    private int mDrawableWidth;//xml文件中设置的宽度
    private boolean isCentre = false;
    private Drawable drawableCentre;
    private int height, width;


    public ExtendCheckBox(Context context) {
        this(context, null, 0);
    }

    public ExtendCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExtendCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Drawable drawableLeft = null, drawableTop = null, drawableRight = null, drawableBottom = null;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendCheckBox);
        setClickable(true);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.ExtendCheckBox_drawableHeight) {
                mDrawableHeight = a.getDimensionPixelSize(R.styleable.ExtendCheckBox_drawableHeight, 50);
            } else if (attr == R.styleable.ExtendCheckBox_drawableWidth) {
                mDrawableWidth = a.getDimensionPixelSize(R.styleable.ExtendCheckBox_drawableWidth, 50);
            } else if (attr == R.styleable.ExtendCheckBox_drawableTop) {
                drawableTop = a.getDrawable(attr);
            } else if (attr == R.styleable.ExtendRadioButton_drawableRight) {
                drawableRight = a.getDrawable(attr);
            } else if (attr == R.styleable.ExtendCheckBox_drawableBottom) {
                drawableBottom = a.getDrawable(attr);
            } else if (attr == R.styleable.ExtendCheckBox_drawableLeft) {
                drawableLeft = a.getDrawable(attr);
            } else if (attr == R.styleable.ExtendCheckBox_drawableCentre) {
                drawableCentre = a.getDrawable(attr);
                isCentre = true;
            }
            int heightResValue = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "layout_height", 0);
            int widthResValu = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "layout_width", 0);
            if (heightResValue != 0) {
                height = getDimensionPx(heightResValue);
            }
            if (widthResValu != 0) {
                width = getDimensionPx(widthResValu);
            }
        }
        a.recycle();
        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);

    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        if (isCentre && drawableCentre != null) {
            drawableCentre.setBounds(0, (height - mDrawableHeight) / 2, mDrawableWidth, (height + mDrawableHeight) / 2);
            setCompoundDrawables(left, drawableCentre, right, bottom);
            return;
        }

        if (left != null) {
            left.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
        if (right != null) {
            right.setBounds(getWidth(), 0, getWidth() + mDrawableWidth, mDrawableHeight);
        }
        if (top != null) {
            top.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
        setCompoundDrawables(left, top, right, bottom);
    }
    /** 从Dimension中转换成PX */
    private   int getDimensionPx(@DimenRes int id) {
        return (int) getContext().getResources().getDimension(id);
    }
}
