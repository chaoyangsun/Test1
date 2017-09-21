package com.yangge.myapplication.customview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.yangge.myapplication.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义12
 */
public class CustomActivity extends Activity {
    private CircleBar circle;
    private ArcProgressbar arcProgressbar;
    private int mProgress;

    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, CustomActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        this.arcProgressbar = (ArcProgressbar) findViewById(R.id.arcProcgressbar);
        this.circle = (CircleBar) findViewById(R.id.circle);
        setGrade(75);
        init();
    }

    private void init() {
        circle.setSweepAngle(243);//设置弧形的角度 270*比例
        circle.setText(90.15 + "");//设置显示的百分比
        circle.setDesText("合 格 率");//设置描述文字
        circle.setWheelColor(Color.CYAN);
        circle.setTextColor(Color.GREEN);
        circle.setDesTextColor(Color.RED);
        circle.startCustomAnimation();//开启动画
    }

    private void setGrade(final int g) {
        mProgress = 1;
        arcProgressbar.reset();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (0 < (mProgress / 3.6) && (mProgress / 3.6) <= 59) {
                    arcProgressbar.setmArcColor(Color.parseColor("#ff0000"));
//                    tv_grade.setTextColor(Color.parseColor("#ff0000"));
                } else if (60 < (mProgress / 3.6) && (mProgress / 3.6) <= 79) {
                    arcProgressbar.setmArcColor(Color.parseColor("#f39700"));
//                    tv_grade.setTextColor(Color.parseColor("#f39700"));
                } else if (80 < (mProgress / 3.6) && (mProgress / 3.6) <= 100) {
                    arcProgressbar.setmArcColor(Color.parseColor("#42ae7c"));
//                    tv_grade.setTextColor(Color.parseColor("#42ae7c"));
                }
                if (msg.what == 0x1223) {
                    arcProgressbar.setProgress(mProgress * (1));
//                    tv_grade.setText("" + (int) (mProgress / 3.6));
                } else if (msg.what == 0x1224) {
//                    tv_grade.setText("" + g);
                }
            }
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                if (mProgress < (int) (((float) 360 / 100) * g)) {
                    msg.what = 0x1223;
                    mProgress++;
                } else {
                    msg.what = 0x1224;
                    this.cancel();
                }
                handler.sendMessage(msg);
            }
        }, 0, 5);
    }
}
