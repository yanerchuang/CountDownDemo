package com.ywj.countdowntextviewdemo;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * 倒计时TextView
 * Created by weijing on 2017-08-21 14:43.
 */

public class TimerTextView2 extends TextView {

    /**
     * 倒计时时间
     */
    private long mCurrntTimeMillis;

    /**
     * 剩余倒计时时间
     */
    private long mLastMillis;
    /**
     * 间隔时间差(两次发送handler)
     */
    private long mIntervalMillis = 1_000;
    /**
     * 开始倒计时code
     */
    private final int MSG_WHAT_START = 10_010;
    private DecimalFormat decimalFormat;


    public TimerTextView2(Context context) {
        super(context);
    }

    public TimerTextView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerTextView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_WHAT_START:
//                    Log.e("l", mLastMillis + "");
                    if (mLastMillis > 0) {
                        mHandler.sendEmptyMessageDelayed(MSG_WHAT_START, mIntervalMillis);
                        setText(getStringTime(mLastMillis));
                    }
                    break;
            }
        }
    };

    private String getStringTime(long lastTimeMillis) {
        StringBuffer stringBuffer = new StringBuffer();
        long currentTimeMillis = System.currentTimeMillis();
        Log.e("l",currentTimeMillis+"");
        if (lastTimeMillis > currentTimeMillis) {
            lastTimeMillis = lastTimeMillis ^ currentTimeMillis;
            currentTimeMillis = lastTimeMillis ^ currentTimeMillis;
            lastTimeMillis = lastTimeMillis ^ currentTimeMillis;
        }
        final long distanceMillis = currentTimeMillis - lastTimeMillis;
        final long seconds = distanceMillis / 1000;
        final long minutes = seconds / 60;
        final long hours = minutes / 60;
        final long days = hours / 24;
        if (days > 0) {
            stringBuffer.append(days).append("天");
        }
        if (hours > 0) {
            stringBuffer.append(format(hours % 24)).append("时");
        }
        if (minutes > 0) {
            stringBuffer.append(format(minutes% 24)).append("分");
        }
        if (seconds > 0) {
            stringBuffer.append(format(seconds % 60)).append("秒");
        }

        return stringBuffer.toString();
    }

    private String format(long number) {
        if (decimalFormat == null)
            decimalFormat = new DecimalFormat("00");

        return decimalFormat.format(number);
    }


    /**
     * 设置倒计时时间
     *
     * @param millis 毫秒值
     */
    public void setCurrntTimeMillis(long millis) {
        mCurrntTimeMillis = millis;
    }

    /**
     * 开始倒计时
     */
    public void start() {
        mLastMillis = mCurrntTimeMillis;
        mHandler.sendEmptyMessage(MSG_WHAT_START);
    }


    /**
     * 停止倒计时
     */
    public void stop() {
        mHandler.removeMessages(MSG_WHAT_START);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeMessages(MSG_WHAT_START);
    }
}
