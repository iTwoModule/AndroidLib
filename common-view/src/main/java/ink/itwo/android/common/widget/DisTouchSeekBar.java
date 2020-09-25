package ink.itwo.android.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 Created by wang on 2020/9/23. */
public class DisTouchSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {

    public DisTouchSeekBar(@NonNull Context context) {
        super(context);
    }

    public DisTouchSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DisTouchSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

}
