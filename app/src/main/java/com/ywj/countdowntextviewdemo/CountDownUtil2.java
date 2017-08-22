package com.ywj.countdowntextviewdemo;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by weijing on 2017-08-22 11:50.
 */

public class CountDownUtil2 {
    /**
     * 开始倒计时code
     */
    private final int MSG_WHAT_START = 10_010;
    private final TextView mTextView;
    private WeakReference<TextView> mWeak;
    /**
     * 倒计时时间
     */
    private long mCountDownMillis = 60_000;
    /**
     * 提示文字
     */
    private String mHintText = "重新发送";

    /**
     * 剩余倒计时时间
     */
    private long mLastMillis;

    /**
     * 间隔时间差(两次发送handler)
     */
    private long mIntervalMillis = 1_000;

    /**
     * 可用状态下字体颜色Id
     */
    private int usableColorId = android.R.color.holo_blue_light;
    /**
     * 不可用状态下字体颜色Id
     */
    private int unusableColorId = android.R.color.darker_gray;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_WHAT_START:
                    if (mLastMillis > 0) {
                        setUsable(false);
                        mLastMillis -= mIntervalMillis;
                        mHandler.sendEmptyMessageDelayed(MSG_WHAT_START, mIntervalMillis);
                        Log.e("ll",""+mLastMillis+(mTextView==null?"null":"not null"));
                    } else {
                        setUsable(true);
                    }
                    break;
            }
        }
    };

    public CountDownUtil2(TextView textView) {
        this.mTextView = textView;
        mWeak=new WeakReference<>(mTextView);
    }

    public CountDownUtil2(TextView textView, long countDownMillis) {
        this.mTextView = textView;
        this.mCountDownMillis = countDownMillis;
    }

    public CountDownUtil2 setCountDownMillis(long countDownMillis) {
        this.mCountDownMillis = countDownMillis;
        return this;
    }


    /**
     * 设置是否可用
     *
     * @param usable
     */
    private void setUsable(boolean usable) {


        if (usable) {
            //可用
            if (!mTextView.isClickable()) {
                mTextView.setClickable(usable);
                mTextView.setTextColor(mTextView.getResources().getColor(usableColorId));
                mTextView.setText(mHintText);
            }
        } else {
            //不可用
            if (mTextView.isClickable()) {
                mTextView.setClickable(usable);
                mTextView.setTextColor(mTextView.getResources().getColor(unusableColorId));
            }
            String content = mLastMillis / 1000 + "秒后" + mHintText;
            mTextView.setText(content);

        }
    }

    /**
     * 设置倒计时颜色
     *
     * @param usableColorId   可用状态下的颜色
     * @param unusableColorId 不可用状态下的颜色
     */
    public CountDownUtil2 setCountDownColor(@ColorRes int usableColorId, @ColorRes int unusableColorId) {
        this.usableColorId = usableColorId;
        this.unusableColorId = unusableColorId;
        return this;
    }

    /**
     * 开始倒计时
     */
    public CountDownUtil2 start() {
        mLastMillis = mCountDownMillis;
        mHandler.sendEmptyMessage(MSG_WHAT_START);
        return this;
    }

    public CountDownUtil2 setOnClickListener(@Nullable final View.OnClickListener onClickListener) {
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(v);
                mHandler.removeMessages(MSG_WHAT_START);
                start();
            }
        });
        return this;
    }

    /**
     * 重置停止倒计时
     */
    public CountDownUtil2 reset() {
        mLastMillis = 0;
        mHandler.sendEmptyMessage(MSG_WHAT_START);
        return this;
    }
}
