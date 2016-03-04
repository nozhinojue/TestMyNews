package com.example.cjj.mynews.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cjj.mynews.R;
import com.example.cjj.mynews.fragment.MusicFragment;
import com.example.cjj.mynews.fragment.NewsFragment;
import com.example.cjj.mynews.fragment.PicFragment;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private  DrawerLayout mDrawerLayout;
    private LinearLayout dlMenu;
    private TextView tvActionbar;
    private Fragment currentFragment;
    private NewsFragment newsFragment;
    private MusicFragment musicFragment;
    private PicFragment picFragment;
    public static final String TITLE_PIC = "经典美图";
    public static final String TITLE_NEWS = "时事新闻";
    public static final String TITLE_MUSIC = "优美音乐";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main2A);
        dlMenu= (LinearLayout) findViewById(R.id.dl_menu_main2A);          //侧滑菜单
        ImageView ivActionbar= (ImageView) findViewById(R.id.iv_actionbar);//actionbar图标
        ivActionbar.setOnClickListener(this);
        tvActionbar= (TextView) findViewById(R.id.tv_actionbar);//actionbar文字

        Button btnNews= (Button) findViewById(R.id.bt_news_main2A);
        btnNews.setOnClickListener(this);
        Button btnMusic= (Button) findViewById(R.id.bt_music_main2A);
        btnMusic.setOnClickListener(this);
        Button btnPic= (Button) findViewById(R.id.bt_pic_main2A);
        btnPic.setOnClickListener(this);

        currentFragment = new Fragment();
        if (newsFragment == null) {
            newsFragment = new NewsFragment();
        }

        switchFragment(currentFragment,newsFragment,TITLE_NEWS);   //刚进入时，加载newsFragment

    }

    /**
     * 切换fragment
     */
    private void switchFragment(Fragment fromFg,Fragment toFg,String titleText){
        if (fromFg == null || toFg == null)
            return;
        if(currentFragment!=toFg){
            FragmentManager fragmentManager= getSupportFragmentManager();
            if(!toFg.isAdded()){   //如果没有被添加过,隐藏当前 添加下一个
                fragmentManager.beginTransaction().hide(fromFg).add(R.id.fl_content_main2A,toFg).commit();
            }else {
                fragmentManager.beginTransaction().hide(fromFg).show(toFg).commit();
            }
            tvActionbar.setText(titleText);
            currentFragment=toFg;
        }

    }



    //点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_actionbar:   //actionbar图标
                if(mDrawerLayout.isDrawerOpen(dlMenu))  //侧滑菜单是否打开
                {
                    mDrawerLayout.closeDrawer(dlMenu);
                }else{
                    mDrawerLayout.openDrawer(dlMenu);
                }
                break;
            case R.id.bt_news_main2A:
                if(newsFragment==null){
                    newsFragment=new NewsFragment();
                }
                switchFragment(currentFragment,newsFragment,TITLE_NEWS);

                mDrawerLayout.closeDrawer(dlMenu);  //关闭侧滑菜单
                break;
            case R.id.bt_music_main2A:
               if(musicFragment==null){
                   musicFragment=new MusicFragment();
               }
                switchFragment(currentFragment,musicFragment,TITLE_MUSIC);

                mDrawerLayout.closeDrawer(dlMenu);
                break;
            case R.id.bt_pic_main2A:
               if(picFragment==null){
                   picFragment=new PicFragment();
               }
                switchFragment(currentFragment,picFragment,TITLE_PIC);

                mDrawerLayout.closeDrawer(dlMenu);
                break;
        }
    }
}
