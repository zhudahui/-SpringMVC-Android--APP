package com.xp.calendar.calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileclient.activity.IndexActivity;
import com.mobileclient.activity.MoreActivity;
import com.mobileclient.activity.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements CalendarCard.OnCellClickListener{
    Intent intent = new Intent();
    private ViewPager mViewPager;
    private TextView monthText;
    private int mCurrentIndex = 498;
    private CalendarViewAdapter<CalendarCard> adapter;
    private List<Custom> listDay;
    private CalendarCard[] views;
    private SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
    private LinearLayout indicatorLayout;
    enum SildeDirection {
        RIGHT, LEFT, NO_SILDE
    }
    private SildeDirection mDirection = SildeDirection.NO_SILDE;
    private TextView text2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.vp_calendar);
        monthText = (TextView) findViewById(R.id.tvCurrentMonth);
        indicatorLayout = (LinearLayout) findViewById(R.id.layout_drop);
        text2=findViewById(R.id.title);
        text2.setText("校历");
        int month = DateUtil.getCurrentMonthNow();
        int year = DateUtil.getCurrentYeatNow();
        CustomDate c = new CustomDate(year, month, 1);
        monthText.setText(showTimeCount(c));
        ImageView back = (ImageView) this.findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        initData();

    }

    private void initData() {
        listDay = new ArrayList<>();
        for (int i = 1;i<5;i++){
            Custom custom = new Custom(2016,7,i);
            listDay.add(custom);
        }
        for (int i = 1;i<5;i++){
            Custom custom = new Custom(2016,7+i,i);
            listDay.add(custom);
        }
        views = new CalendarCard[6];
        for (int i = 0; i < 6; i++) {
            views[i] = new CalendarCard(this, this, listDay);
        }
        adapter = new CalendarViewAdapter<>(views);
        setViewPager();

        CustomDate c = new CustomDate(DateUtil.getCurrentYeatNow(), DateUtil.getCurrentMonthNow(), DateUtil.getCurrentMonthDay());


    }

    @Override
    public void clickDate(CustomDate date) {
        Toast.makeText(this,showTimeCountAll(date), Toast.LENGTH_SHORT).show();
        intent.setClass(MainActivity.this,SpecialActivity.class);
        intent.putExtra("data",showTimeCountAll(date));
        startActivity(intent);
    }

    @Override
    public void changeDate(CustomDate date) {

    }

    private void setViewPager() {
        mViewPager.setAdapter(adapter);
//        initCategoryBarPoint(indicatorLayout);
        mViewPager.setCurrentItem(mCurrentIndex);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                measureDirection(position);
                updateCalendarView(position);
//                setIndicator(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void initCategoryBarPoint(LinearLayout indicatorLayout) {

        indicatorLayout.removeAllViews();

        android.widget.LinearLayout.LayoutParams lp;
        for (int i = 0; i < views.length; i++) {
            View v = new View(this);
            v.setBackgroundResource(R.drawable.point_backgrond_calendar);
            v.setEnabled(false);
            lp = new android.widget.LinearLayout.LayoutParams(dip2px(getApplicationContext(), 12), dip2px(getApplicationContext(), 4));
            lp.leftMargin = dip2px(getApplicationContext(), 3);
            lp.rightMargin = dip2px(getApplicationContext(), 3);
            v.setLayoutParams(lp);
            indicatorLayout.addView(v);
        }

        if (indicatorLayout.getChildCount() > 0)
            indicatorLayout.getChildAt(0).setEnabled(true);

    }

    /**
     * 设置指示器
     *
     * @param selectedPosition 默认指示器位置
     */
    private void setIndicator(int selectedPosition) {
        for (int i = 0; i < views.length; i++) {
            indicatorLayout.getChildAt(i).setEnabled(false);
        }
        if (views.length > selectedPosition)
            indicatorLayout.getChildAt(selectedPosition).setEnabled(true);

    }
    /* 计算方向
   *
    */
    private void measureDirection(int arg0) {

        if (arg0 > mCurrentIndex) {
            mDirection = SildeDirection.RIGHT;

        } else if (arg0 < mCurrentIndex) {
            mDirection = SildeDirection.LEFT;
        }
        mCurrentIndex = arg0;
    }
    // 更新日历视图
    private void updateCalendarView(int arg0) {
        CustomDate customDate = new CustomDate();
        CalendarCard[] mShowViews = adapter.getAllItems();
        if (mDirection == SildeDirection.RIGHT) {
            customDate = mShowViews[arg0 % mShowViews.length].rightSlide();
        } else if (mDirection == SildeDirection.LEFT) {
            customDate = mShowViews[arg0 % mShowViews.length].leftSlide();
        }
        mDirection = SildeDirection.NO_SILDE;
        if (customDate != null) {
            monthText.setText(showTimeCount(customDate));
           //进行网络请求

        }


    }
    public String showTimeCount(CustomDate time) {
        String timeCount;
        long minuec = time.month;
        String minue = "0" + minuec;
        minue = minue.substring(minue.length() - 2, minue.length());
        long secc = time.day;
        String sec = "0" + secc;
        sec = sec.substring(sec.length() - 2, sec.length());
        timeCount = time.year + "年" + minue + "月";
        return timeCount;
    }

    public String showTimeCountAll(CustomDate time) {
        String timeCount;
        long minuec = time.month;
        String minue = "0" + minuec;
        minue = minue.substring(minue.length() - 2, minue.length());
        long secc = time.day;
        String sec = "0" + secc;
        sec = sec.substring(sec.length() - 2, sec.length());
        timeCount = time.year + minue + sec;
        return timeCount;
    }

    public int dip2px(Context ctx, float dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        //dp = px/density
        int px = (int) (dp * density + 0.5f);
        return px;
    }
}
