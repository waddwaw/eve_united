package com.example.li.eve_united.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


import com.example.li.eve_united.R;
import com.example.li.eve_united.bean.UserBean;
import com.example.li.eve_united.db.ChatProvider;
import com.example.li.eve_united.fragment.HomeFragment;
import com.example.li.eve_united.service.NetService;
import com.example.li.eve_united.utils.SPUtils;
import com.google.gson.Gson;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.ui.fragments.CommunityMainFragment;


public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private String[] mTitles = new String[]{"人人LOVE", "发现"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initViews();
        UserBean user = new Gson().fromJson((String) SPUtils.get(MainActivity.this,"LoUser","null"),UserBean.class);
        NetService.USER=user.getUser();
        Intent in = new Intent(MainActivity.this, NetService.class);
        startService(in);
        //测试方法

//        Cursor c = this.getContentResolver().query(ChatProvider.URI_CHAT_ALL,null,null,null,null);
//        while (c.moveToNext()){
//            Log.e("TAG",c.getString(c.getColumnIndex("msg")));
//        }
    }

    private void initData() {
        CommunitySDK mCommSDK = CommunityFactory.getCommSDK(this);
// 初始化sdk，请传递ApplicationContext
        mCommSDK.initSDK(this);
    }

    private void initViews() {
        mTabLayout = (TabLayout) findViewById(R.id.id_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if(position==1){
                    CommunityMainFragment mFeedsFragment = new CommunityMainFragment();
                 //设置Feed流页面的返回按钮不可见
                    mFeedsFragment.setBackButtonVisibility(View.INVISIBLE);
                    return mFeedsFragment;
                }
                return new HomeFragment();
            }

            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);


    }
}
