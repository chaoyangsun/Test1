package com.yangge.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.yangge.myapplication.customview.CustomActivity;
import com.yangge.myapplication.imitationalipay.AlipayActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button custom_view;
    private Button btn_alipay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initListener();
    }

    private void initListener() {
        custom_view.setOnClickListener(this);
        btn_alipay.setOnClickListener(this);
    }

    private void init() {
        custom_view = (Button) findViewById(R.id.custom_view);
        btn_alipay = (Button) findViewById(R.id.btn_alipay);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.custom_view:
                CustomActivity.startAction(this);
                break;
            case R.id.btn_alipay:
                AlipayActivity.startAction(this);
                break;
        }
    }
}
