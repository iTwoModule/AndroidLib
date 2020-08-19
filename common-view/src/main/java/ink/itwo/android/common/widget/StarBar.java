package ink.itwo.android.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;

import ink.itwo.android.common.control.R;

/**评分控件
 * Created by wang on 2018/5/3. */
public class StarBar extends LinearLayout {
    private int numStar = 5;
    private boolean stepHalf = true;
    private float value = 0f;
    private @DrawableRes int progressRsdId = R.drawable.ic_star_check,
            secondProgressResId = R.drawable.ic_star_secod,
            normalResId = R.drawable.ic_star_normal;
    private float marginLeftStar, marginRightStar, widthStar, heightStar;
    private boolean isIndicator = false;
    private LayoutParams params;
    private float stepSize = 0.5f;
    private OnStarBarChangeListener l;


    public StarBar(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public StarBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public StarBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public StarBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (context != null && attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StarBar);
            if (ta != null) {
                numStar = ta.getInteger(R.styleable.StarBar_numStar, numStar);
                stepHalf = ta.getBoolean(R.styleable.StarBar_stepHalf, stepHalf);
                value = ta.getFloat(R.styleable.StarBar_value, value);
                progressRsdId = ta.getResourceId(R.styleable.StarBar_progressRsdId, progressRsdId);
                secondProgressResId = ta.getResourceId(R.styleable.StarBar_secondProgressResId, secondProgressResId);
                normalResId = ta.getResourceId(R.styleable.StarBar_normalResId, normalResId);
                marginLeftStar = ta.getDimension(R.styleable.StarBar_marginLeftStar, marginLeftStar);
                marginRightStar = ta.getDimension(R.styleable.StarBar_marginRightStar, marginRightStar);
                widthStar = ta.getDimension(R.styleable.StarBar_widthStar, widthStar);
                heightStar = ta.getDimension(R.styleable.StarBar_heightStar, heightStar);
                isIndicator = ta.getBoolean(R.styleable.StarBar_isIndicator, isIndicator);
                ta.recycle();
            }
        }
        stepSize = stepHalf ? 0.5f : 1f;

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setClickable(!isIndicator);

        params = new LayoutParams(widthStar == 0 ? LayoutParams.WRAP_CONTENT : (int) widthStar, heightStar == 0 ? LayoutParams.WRAP_CONTENT : (int) heightStar);
        params.leftMargin = (int) marginLeftStar;
        params.rightMargin = (int) marginRightStar;

        for (float i = 0; i < numStar; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(normalResId);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            addView(imageView);
        }
        setValue(value);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return !isIndicator || super.dispatchTouchEvent(event);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            float touchX = event.getX();
            for (int i = 0; i < getChildCount(); i++) {

                View childAt = getChildAt(i);
                int left = childAt.getLeft();
                int right = childAt.getRight();
                if (touchX > left && touchX < right) {
                    if (stepHalf && touchX <= (left + right) / 2) {
                        setValue(i + 0.5f + 1);
                        if (l != null) l.onValue(i + 0.5f + 1);
                        return super.dispatchTouchEvent(event);
                    }
                    setValue(i + 1);
                    if (l != null) l.onValue(i + 1);
                    return super.dispatchTouchEvent(event);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void setValue(@FloatRange(from = 0f) float value) {
        this.value = value;
        if (this.value >= numStar) this.value = numStar;

        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt != null && childAt instanceof ImageView) {
                ImageView imageView = (ImageView) childAt;
                if (stepHalf && i + stepSize == value - 1)
                    imageView.setImageResource(secondProgressResId);
                else if (i <= value - 1) imageView.setImageResource(progressRsdId);
                else imageView.setImageResource(normalResId);
            }
        }
    }

    public float getValue() {
        return value;
    }

    public interface OnStarBarChangeListener {
        void onValue(float value);
    }

    public StarBar setOnStarBarChangeListener(OnStarBarChangeListener l) {
        this.l = l;
        return this;
    }
}
