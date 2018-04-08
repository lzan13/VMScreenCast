package com.vmloft.develop.app.screencast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.vmloft.develop.app.screencast.R;
import com.vmloft.develop.library.tools.VMActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends VMActivity {

    @BindView(R.id.widget_toolbar) Toolbar toolbar;
    @BindView(R.id.widget_tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;

    private LocalContentFragment localContentFragment;
    private RemoteContentFragment remoteContentFragment;

    private Fragment[] fragments;
    private int currTabIndex = 0;
    private String[] tabsTitle = new String[]{"本地资源", "远程资源"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(activity);

        init();
        initFragment();
    }

    private void init() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
    }

    private void initFragment() {
        localContentFragment = LocalContentFragment.newInstance();
        remoteContentFragment = RemoteContentFragment.newInstance();

        fragments = new Fragment[]{localContentFragment, remoteContentFragment};
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // 设置 ViewPager 缓存个数
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(currTabIndex);
        // 添加 ViewPager 页面改变监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.fab_screen_cast)
    public void onClick() {
        startActivity(new Intent(activity, DeviceListActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        ClingManager.getInstance().destroy();
//        ControlManager.getInstance().destroy();
//        DeviceManager.getInstance().destroy();
    }


    /**
     * 自定义 ViewPager 适配器子类
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabsTitle[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return tabsTitle.length;
        }
    }
}
