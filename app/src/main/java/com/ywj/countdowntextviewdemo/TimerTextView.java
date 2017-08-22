package com.ywj.countdowntextviewdemo;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * 倒计时TextView
 * Created by weijing on 2017-08-21 14:43.
 * <p>
 * final TimerTextView timerTextView2 = (TimerTextView) findViewById(R.id.timerTextView2);
 * timerTextView2.setLastMillis(System.currentTimeMillis()+5000);
 * timerTextView2.setContentBeforAfter("剩余","抢购");
 * timerTextView2.setOutOfDateText("立即抢购");
 * timerTextView2.setOnClickListener(new View.OnClickListener() {
 *
 * public void onClick(View v) {
 * if (!timerTextView2.isFinished()){
 * return;
 * }
 * timerTextView2.setText("抢购成功");
 * Toast.makeText(MainActivity.this, "抢购成功", Toast.LENGTH_SHORT).show();
 * }
 * });
 * timerTextView2.start();
 */

public class TimerTextView extends TextView {


    /**
     * 最后时间
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
    /**
     * 前拼接字符串
     */
    private String mContentBefore = "";
    /**
     * 后拼接字符串
     */
    private String mContentAfter = "";
    /**
     * 倒计时结束后显示的内容
     */
    private String mOutOfDateText;
    /**
     * 倒计时是否完成
     */
    private boolean isFinished;


    public TimerTextView(Context context) {
        super(context);
    }

    public TimerTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_WHAT_START:
                    TimerTextView.this.handleMessage();
                    break;
            }
        }
    };

    private void handleMessage() {
        long currentTimeMillis = System.currentTimeMillis();
        if (mLastMillis > currentTimeMillis + 1000) {
            //1000ms以内就认为倒计时结束
            mHandler.sendEmptyMessageDelayed(MSG_WHAT_START, mIntervalMillis);
            final String formatTime = getFormatTime(mLastMillis - currentTimeMillis);

            setText(getTextContent(formatTime));
            Log.e("ll", currentTimeMillis + "--" + formatTime);

        } else {
            isFinished = true;
            if (TextUtils.isEmpty(mOutOfDateText)) {
                setText(getTextContent("00秒"));
            } else {
                setText(mOutOfDateText);
            }
        }
    }

    /**
     * 获取格式后的
     *
     * @param formatTime
     * @return
     */
    private String getTextContent(String formatTime) {
        return mContentBefore + formatTime + mContentAfter;
    }

    private String getFormatTime(long distanceMillis) {
        StringBuffer stringBuffer = new StringBuffer();

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
            stringBuffer.append(format(minutes % 24)).append("分");
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
    public void setLastMillis(long millis) {
        mLastMillis = millis;
        if (mLastMillis < System.currentTimeMillis()) {
            Log.e("e", "lastTimeMillis must bigger ran currentTimeMillis:" + System.currentTimeMillis());
        }
    }

    /**
     * 设置前后拼接字符串，before + formatTime + after
     *
     * @param before
     * @param after
     */
    public void setContentBeforAfter(String before, String after) {
        mContentBefore = before;
        mContentAfter = after;
    }

    public void setOutOfDateText(String outOfDateText) {
        mOutOfDateText = outOfDateText;
    }

    /**
     * 开始倒计时
     */
    public void start() {
        mHandler.sendEmptyMessage(MSG_WHAT_START);
    }

    /**
     * 是否完成
     *
     * @return
     */
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * 设置是否完成
     *
     * @param isFinished
     */
    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
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
