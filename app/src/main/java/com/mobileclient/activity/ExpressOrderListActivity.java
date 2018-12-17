package com.mobileclient.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobileclient.fragment.OrderOneFragment;
import com.mobileclient.fragment.OrderThreeFragment;
import com.mobileclient.fragment.OrderTwoFragment;

import java.util.ArrayList;
import java.util.List;

public class ExpressOrderListActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title, item_weixin, item_tongxunlu, item_faxian;
    private ViewPager vp;
    // private OneFragment oneFragment;
    private OrderTwoFragment twoFragment;
    private OrderThreeFragment threeFragment;
    private OrderOneFragment oneFragment;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;


//==============
//================下拉刷新==============================
private final static int REFRESH_COMPLETE = 0;
    private final static int LOAD_COMPLETE = 1;
    //==============================================
    private ViewPager advPager1 = null;
    private ViewPager viewPager;
    private View view1, view2, view3;
    private List<View> viewList;//view数组
    private MyProgressDialog dialog; //进度条    @Override
    RelativeLayout tvSearchRlt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除工具栏
        getSupportActionBar().hide();
        setContentView(R.layout.order_layout);
        initViews();

        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        vp.setOffscreenPageLimit(4);//ViewPager的缓存为4帧
        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(0);//初始设置ViewPager选中第一帧
        item_weixin.setTextColor(Color.parseColor("#66CDAA"));
        tvSearchRlt = (RelativeLayout) findViewById(R.id.tv_search_rlt);
      //  Log.i("ppppp","ppppp"+declare.getUserType());
        tvSearchRlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpressOrderListActivity.this,SearchActivity.class);
                int location[] = new int[2];
                tvSearchRlt.getLocationOnScreen(location);
                intent.putExtra("x",location[0]);
                intent.putExtra("y",location[1]);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
        //===================================================
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        LayoutInflater inflater=getLayoutInflater();
        view1 = inflater.inflate(R.layout.layout1, null);
        view2 = inflater.inflate(R.layout.layout2,null);
        view3 = inflater.inflate(R.layout.layout3, null);

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);


        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));


                return viewList.get(position);
            }
        };


        viewPager.setAdapter(pagerAdapter);

        //===============================================





        //ViewPager的监听事件
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*此方法在页面被选中时调用*/
                //title.setText(titles[position]);
                changeTextColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
                arg0 ==1的时辰默示正在滑动，
                arg0==2的时辰默示滑动完毕了，
                arg0==0的时辰默示什么都没做。*/
            }
        });
    }

    /**
     * 初始化布局View
     */
    private void initViews() {
       // title = (TextView) findViewById(R.id.title);
        item_weixin = (TextView) findViewById(R.id.item_weixin);
        item_tongxunlu = (TextView) findViewById(R.id.item_tongxunlu);
        item_faxian = (TextView) findViewById(R.id.item_faxian);
        // item_me = (TextView) findViewById(R.id.item_me);

        item_weixin.setOnClickListener(this);
        item_tongxunlu.setOnClickListener(this);
        item_faxian.setOnClickListener(this);
        // item_me.setOnClickListener(this);

        vp = (ViewPager) findViewById(R.id.mainViewPager);
        // oneFragment = new OneFragment();
        twoFragment = new OrderTwoFragment();
        threeFragment = new OrderThreeFragment();
        oneFragment = new OrderOneFragment();
        //给FragmentList添加数据
        //  mFragmentList.add(oneFragment);
        mFragmentList.add(oneFragment);
        mFragmentList.add(twoFragment);
        mFragmentList.add(threeFragment);
    }

    /**
     * 点击底部Text 动态修改ViewPager的内容
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_weixin:
                vp.setCurrentItem(0, true);
                break;
            case R.id.item_tongxunlu:
                vp.setCurrentItem(1, true);
                break;
            case R.id.item_faxian:
                vp.setCurrentItem(2, true);
                break;
//            case R.id.item_me:
//                vp.setCurrentItem(3, true);
//                break;
        }
    }


    public class FragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }

    /*
     *由ViewPager的滑动修改底部导航Text的颜色
     */
    private void changeTextColor(int position) {
        if (position == 0) {
            item_weixin.setTextColor(Color.parseColor("#66CDAA"));
            item_tongxunlu.setTextColor(Color.parseColor("#000000"));
            item_faxian.setTextColor(Color.parseColor("#000000"));
            //   item_me.setTextColor(Color.parseColor("#000000"));
        } else if (position == 1) {
            item_tongxunlu.setTextColor(Color.parseColor("#66CDAA"));
            item_weixin.setTextColor(Color.parseColor("#000000"));
            item_faxian.setTextColor(Color.parseColor("#000000"));
            // item_me.setTextColor(Color.parseColor("#000000"));
        } else if (position == 2) {
            item_faxian.setTextColor(Color.parseColor("#66CDAA"));
            item_weixin.setTextColor(Color.parseColor("#000000"));
            item_tongxunlu.setTextColor(Color.parseColor("#000000"));
            // item_me.setTextColor(Color.parseColor("#000000"));
        } else if (position == 3) {
            //item_me.setTextColor(Color.parseColor("#66CDAA"));
            item_weixin.setTextColor(Color.parseColor("#000000"));
            item_tongxunlu.setTextColor(Color.parseColor("#000000"));
            item_faxian.setTextColor(Color.parseColor("#000000"));
        }
    }
}
