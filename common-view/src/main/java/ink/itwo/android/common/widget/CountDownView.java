package ink.itwo.android.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseLongArray;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import ink.itwo.android.common.control.R;

/** 倒计时
 @author wang
 on 2018/1/5 */

public class CountDownView extends LinearLayout {
    private TextView tvDay, tvHour, tvMinute, tvSecond, tvMilliseconds;
    private SparseLongArray sparseArray = new SparseLongArray();
    private long deadline;

    public CountDownView(Context context) {
        super(context);
        init(context, null);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();
        super.onDetachedFromWindow();
    }

    private void init(Context context, AttributeSet attrs) {
        int layoutId = R.layout.widget_count_down;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
            if (typedArray != null) {
                layoutId = typedArray.getResourceId(R.styleable.CountDownView_count_down_layout_id, R.layout.widget_count_down);
                typedArray.recycle();
            }

        }
        LayoutInflater.from(context).inflate(layoutId, this, true);
        tvDay = findViewById(R.id.tv_day);
        tvHour = findViewById(R.id.tv_hour);
        tvMinute = findViewById(R.id.tv_minute);
        tvSecond = findViewById(R.id.tv_second);
        tvMilliseconds = findViewById(R.id.tv_milliseconds);
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
        stop();
        start();
    }


    public void start() {
        postDelayed(runnable, 1000);
    }

    public void stop() {
        removeCallbacks(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            formatTime();
        }
    };

    private void formatTime() {
        if (deadline <= 0) {
            start();
            return;
        }
        long countDown = deadline - System.currentTimeMillis();
        sparseArray.put(0, TimeUnit.MILLISECONDS.toDays(countDown));
        sparseArray.put(1, TimeUnit.MILLISECONDS.toHours(countDown) % 24);
        sparseArray.put(2, TimeUnit.MILLISECONDS.toMinutes(countDown) % 60);
        sparseArray.put(3, TimeUnit.MILLISECONDS.toSeconds(countDown) % 60);
        sparseArray.put(4, countDown % 100);

        if (tvDay != null) {
            tvDay.setText(String.valueOf(sparseArray.get(0)));
        }
        if (tvHour != null) {
            tvHour.setText(formatString(sparseArray.get(1)));
        }
        if (tvMinute != null) {
            tvMinute.setText(formatString(sparseArray.get(2)));
        }
        if (tvSecond != null) {
            tvSecond.setText(formatString(sparseArray.get(3)));
        }
        if (tvMilliseconds != null) {
            tvMilliseconds.setText(formatString(sparseArray.get(4)));
        }
        start();
    }

    private String formatString(long l) {
        if (l<=0) return "00";
        return l < 10 ? "0" + String.valueOf(l) : String.valueOf(l);
    }


}
