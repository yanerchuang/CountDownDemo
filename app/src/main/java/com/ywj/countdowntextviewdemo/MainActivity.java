package com.ywj.countdowntextviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CountDownTextView countDownTextView = (CountDownTextView) findViewById(R.id.countDownTextView);
        countDownTextView.setCountDownMillis(5000);
        countDownTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MainActivity","点击事件");
            }
        });
        countDownTextView.start();


        TimerTextView timerTextView = (TimerTextView) findViewById(R.id.timerTextView);
        timerTextView.setLastMillis(System.currentTimeMillis()+5000);
        timerTextView.setContentBeforAfter("剩余","付款");
        timerTextView.setOutOfDateText("已过期");
        timerTextView.start();

        final TimerTextView timerTextView2 = (TimerTextView) findViewById(R.id.timerTextView2);
        timerTextView2.setLastMillis(System.currentTimeMillis()+5000);
        timerTextView2.setContentBeforAfter("剩余","抢购");
        timerTextView2.setOutOfDateText("立即抢购");
        timerTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timerTextView2.isFinished()){
                    return;
                }
                timerTextView2.setText("抢购成功");
                Toast.makeText(MainActivity.this, "抢购成功", Toast.LENGTH_SHORT).show();
            }
        });
        timerTextView2.start();

        TextView textView = (TextView) findViewById(R.id.textView);
        new CountDownUtil(textView)
                .setCountDownMillis(60_000L)
                .setCountDownColor(android.R.color.holo_blue_light,android.R.color.darker_gray)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("MainActivity","发送成功");
                    }
                })
                .start();
    }

}
