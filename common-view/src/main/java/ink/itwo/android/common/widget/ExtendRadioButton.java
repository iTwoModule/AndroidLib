package ink.itwo.android.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatRadioButton;

import ink.itwo.android.common.control.R;


/**
 RadioButton的drawable可控大小
 Created by wang on 17/9/15. */

public class ExtendRadioButton extends AppCompatRadioButton {
    private int mDrawableHeight;//xml文件中设置的高度
    private int mDrawableWidth;//xml文件中设置的宽度

    public ExtendRadioButton(Context context) {
        this(context, null, 0);
    }

    public ExtendRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExtendRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Drawable drawableLeft = null, drawableTop = null, drawableRight = null, drawableBottom = null;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendRadioButton);
        setClickable(true);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.ExtendRadioButton_drawableHeight) {
                mDrawableHeight = a.getDimensionPixelSize(R.styleable.ExtendRadioButton_drawableHeight, 50);
            } else if (attr == R.styleable.ExtendRadioButton_drawableWidth) {
                mDrawableWidth = a.getDimensionPixelSize(R.styleable.ExtendRadioButton_drawableWidth, 50);
            } else if (attr == R.styleable.ExtendRadioButton_drawableTop) {
                drawableTop = a.getDrawable(attr);
            } else if (attr == R.styleable.ExtendRadioButton_drawableBottom) {
                drawableBottom = a.getDrawable(attr);
            } else if (attr == R.styleable.ExtendRadioButton_drawableRight) {
                drawableRight = a.getDrawable(attr);
            } else if (attr == R.styleable.ExtendRadioButton_drawableLeft) {
                drawableLeft = a.getDrawable(attr);
            }
        }
        a.recycle();
        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        if (left != null) {
            left.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
        if (right != null) {
            right.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
        if (top != null) {
            top.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
        setCompoundDrawables(left, top, right, bottom);
    }

}
