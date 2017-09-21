package com.yangge.myapplication.imitationalipay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.yangge.myapplication.R;

public class AlipayActivity extends AppCompatActivity implements OnOffsetChangedListener {
    private final static String TAG = "AlipayActivity";
    private AppBarLayout abl_bar;
    private View tl_expand, tl_collapse;
    private View v_expand_mask, v_collapse_mask, v_pay_mask;
    private int mMaskColor;
    private RecyclerView rv_content;

    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, AlipayActivity.class);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay);
        mMaskColor = getResources().getColor(R.color.blue_dark);
//		rv_content = (RecyclerView) findViewById(R.id.rv_content);
//		rv_content.setLayoutManager(new GridLayoutManager(this, 4));
//		rv_content.setAdapter(new LifeAdapter(this, LifeItem.getDefault()));

        abl_bar = (AppBarLayout) findViewById(R.id.abl_bar);
        tl_expand = (View) findViewById(R.id.tl_expand);
        tl_collapse = (View) findViewById(R.id.tl_collapse);
        v_expand_mask = (View) findViewById(R.id.v_expand_mask);
        v_collapse_mask = (View) findViewById(R.id.v_collapse_mask);
        v_pay_mask = (View) findViewById(R.id.v_pay_mask);
        abl_bar.addOnOffsetChangedListener(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.d(TAG, "verticalOffset="+verticalOffset);
        int offset = Math.abs(verticalOffset);
        int total = appBarLayout.getTotalScrollRange();
        Log.e(TAG, "-----total-----" + total);
        int alphaIn = offset;
        int alphaOut = total - offset;
//        int alphaOut = (200-offset)<0?0:200-offset;
        int maskColorIn = Color.argb(alphaIn, Color.red(mMaskColor),
                Color.green(mMaskColor), Color.blue(mMaskColor));
        int maskColorInDouble = Color.argb((int) (alphaIn*1.0/total*255*2), Color.red(mMaskColor),
                Color.green(mMaskColor), Color.blue(mMaskColor));
        int maskColorOut = Color.argb((int) (alphaOut*1.0/total*255*2), Color.red(mMaskColor),
                Color.green(mMaskColor), Color.blue(mMaskColor));
        if (offset <= total / 2) {//处于拉伸状态
            tl_expand.setVisibility(View.VISIBLE);
            tl_collapse.setVisibility(View.GONE);
            v_expand_mask.setBackgroundColor(maskColorInDouble);
        } else {//处于收缩状态
            tl_expand.setVisibility(View.GONE);
            tl_collapse.setVisibility(View.VISIBLE);
            v_collapse_mask.setBackgroundColor(maskColorOut);
        }
        v_pay_mask.setBackgroundColor(maskColorIn);
    }

}
