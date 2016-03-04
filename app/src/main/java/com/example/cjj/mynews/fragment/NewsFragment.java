package com.example.cjj.mynews.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cjj.mynews.R;
import com.example.cjj.mynews.SimpleFragmentPagerAdapter;


public class NewsFragment extends Fragment {
    private SimpleFragmentPagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment、
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        mPagerAdapter = new SimpleFragmentPagerAdapter(getChildFragmentManager(), getContext());

        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout_newsFg);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);//tab均分,适合少的tab,TabLayout.GRAVITY_CENTER
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//适合很多tab
        //tabLayout.setTabMode(TabLayout.MODE_FIXED);//tab均分,适合少的tab

        mViewPager= (ViewPager) view.findViewById(R.id.viewPager_newsFg);
        mViewPager.setOffscreenPageLimit(4);    // 设置缓存多少个 Tab对应的 fragment
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        return view;
    }

}
