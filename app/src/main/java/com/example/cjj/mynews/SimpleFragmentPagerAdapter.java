package com.example.cjj.mynews;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.cjj.mynews.okhttp.MyOkHttp;

/**
 * Created by CJJ on 2016/2/16.
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"体育新闻","科技新闻","国际新闻"};
    private String dataUrl[]=new String[]{MyOkHttp.TIYU_URL,MyOkHttp.KEJI_URL,MyOkHttp.WORLD_URL};
    private Context context;

    public SimpleFragmentPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(dataUrl[position]);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
