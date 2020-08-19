package ink.itwo.android.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import ink.itwo.android.common.control.R;


/**
 计数器控件
 Created by wang on 2018/4/24. */
public class CountView extends LinearLayout implements View.OnClickListener {
    public static final int NORMAL = 0, LIMIT_MAX = 1, LIMIT_MIN = 2;

    private int minValue = 1, maxValue = Integer.MAX_VALUE, value,
            stepValue = 1;
    private TextView tvResult;
    private View viewSub, viewAdd;
    private OnCountValueListener l;

    public CountView(Context context) {
        super(context);
        init(context, null);
    }

    public CountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }


    public CountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CountView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (context == null) return;
        int layoutId = R.layout.widget_count_view;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CountView);
            if (ta != null) {
                minValue = ta.getInteger(R.styleable.CountView_minCount, minValue);
                maxValue = ta.getInteger(R.styleable.CountView_maxCount, maxValue);
                value = ta.getInteger(R.styleable.CountView_initialCount, minValue);
                stepValue = ta.getInteger(R.styleable.CountView_stepCount, stepValue);
                layoutId = ta.getResourceId(R.styleable.CountView_layout_id, R.layout.widget_count_view);
                ta.recycle();
            }
        }
        LayoutInflater.from(context).inflate(layoutId, this, true);
        viewAdd = findViewById(R.id.view_add);
        viewSub = findViewById(R.id.view_sub);
        tvResult = findViewById(R.id.tv_result);
        viewAdd.setOnClickListener(this);
        viewSub.setOnClickListener(this);
        tvResult.setText(String.valueOf(value));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.view_sub) {
            sub();
        } else if (v.getId() == R.id.view_add) {
            add();
        }
    }

    private void sub() {
        if (value - minValue < stepValue) {
            if (l != null) l.value(value, LIMIT_MIN);
            return;
        }
        value -= stepValue;
        tvResult.setText(String.valueOf(value));
        if (l != null) l.value(value, NORMAL);
    }

    private void add() {
        if (value + stepValue > maxValue) {
            if (l != null) l.value(value, LIMIT_MAX);
            return;
        }
        value += stepValue;
        tvResult.setText(String.valueOf(value));
        if (l != null) l.value(value, NORMAL);
    }


    public interface OnCountValueListener {
        void value(int value, int state);
    }


    public void setCountValueListener(OnCountValueListener l) {
        this.l = l;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public void setStepValue(int stepValue) {
        this.stepValue = stepValue;
    }

    public void setValue(int value) {
        if (this.value == value) return;
        if (tvResult != null) tvResult.setText(String.valueOf(value));
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
