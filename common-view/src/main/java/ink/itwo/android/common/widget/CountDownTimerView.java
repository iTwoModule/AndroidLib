package ink.itwo.android.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.ArrayList;
import java.util.List;

import ink.itwo.android.common.control.R;


/**
 获取验证码时的倒计时控件
 Created by wang on 2018/4/12. */

public class CountDownTimerView extends AppCompatTextView implements View.OnClickListener {

    private String startText = "获取", phone = "", hintText = "手机号格式错误";
    @StringRes private int lastFormatArgsRes;
    private int duration = 60;
    private CountDownTimer countDownTimer;
    //开始倒计时之前的背景 //倒计时进行中的背景
    @DrawableRes private int resIdBefore, resIdAfter;
    //    private CountDownTimerViewListener listener;
    private List<CountDownTimerViewListener> listeners;

    public CountDownTimerView(Context context) {
        super(context);
        initView(context, null);
    }

    public CountDownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CountDownTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CountDownTimerView);
            resIdAfter = a.getResourceId(R.styleable.CountDownTimerView_resIdAfter, R.color.black_999999);
            resIdBefore = a.getResourceId(R.styleable.CountDownTimerView_resIdBefore, R.color.black_999999);
            duration = a.getInteger(R.styleable.CountDownTimerView_duration, 60);
            int startTextRes = a.getResourceId(R.styleable.CountDownTimerView_startText, 0);
            if (startTextRes != 0) {
                startText = getResources().getString(startTextRes);
            }
            int hintTextRes = a.getResourceId(R.styleable.CountDownTimerView_hintText, 0);
            if (hintTextRes != 0) {
                hintText = getResources().getString(hintTextRes);
            }
            lastFormatArgsRes = a.getResourceId(R.styleable.CountDownTimerView_lastFormatArgs, 0);
            a.recycle();
        }
        setOnClickListener(this);
        setBackgroundResource(resIdBefore);
        countDownTimer = new CountDownTimer(duration * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //long minTemp = millisUntilFinished;
                String s = "";
                if (lastFormatArgsRes == 0) {
                    s = String.valueOf((millisUntilFinished / 1000)) + "s";
                } else {
                    s = getContext().getResources().getString(lastFormatArgsRes, String.valueOf((millisUntilFinished / 1000)));
                }
                setText(s);
                if (listeners != null) {
                    for (CountDownTimerViewListener listener : listeners) {
                        listener.onTick(millisUntilFinished);
                    }
                }
            }

            @Override
            public void onFinish() {
                CountDownTimerView.this.onFinish();
            }
        };
        setText(startText);
    }

    public void onFinish() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        setText(startText);
        setClickable(true);
        setBackgroundResource(resIdBefore);
        if (listeners != null) {
            for (CountDownTimerViewListener listener : listeners) {
                listener.onFinish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (listeners != null) {
            for (CountDownTimerViewListener listener : listeners) {
                if (listener.onIntercept()) {
                    return;
                }
            }
        }
        if (phone == null || phone.length() != 11) {
            Toast.makeText(getContext(), hintText, Toast.LENGTH_LONG).show();
            return;
        }

        if (countDownTimer != null) {
            setClickable(false);
            setBackgroundResource(resIdAfter);
            countDownTimer.start();
            if (listeners != null) {
                for (CountDownTimerViewListener listener : listeners) {
                    listener.onStart();
                }
            }
        }
    }

    public void startTime() {
        countDownTimer.start();
        setClickable(false);
        setBackgroundResource(resIdAfter);
    }

//    @Override
//    protected void onDetachedFromWindow() {
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//            countDownTimer = null;
//        }
//        super.onDetachedFromWindow();
//    }

    public interface CountDownTimerViewListener {
        boolean onIntercept();

        void onStart();

        void onFinish();

        void onTick(long millisUntilFinished);
    }

    public static class SimpleCountDownTimerViewListener implements CountDownTimerViewListener {

        @Override
        public boolean onIntercept() {
            return false;
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onFinish() {

        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }

    public void addCountDownListener(CountDownTimerViewListener listener) {
        if (listeners == null) listeners = new ArrayList<>();
        listeners.add(listener);
    }

    public void setMobile(String phone) {
        this.phone = phone;
    }

    public void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}
