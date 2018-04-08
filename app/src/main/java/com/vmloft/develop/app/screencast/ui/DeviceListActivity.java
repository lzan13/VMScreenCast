package com.vmloft.develop.app.screencast.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.vmloft.develop.app.screencast.R;
import com.vmloft.develop.library.tools.VMActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lzan13 on 2018/3/19.
 * 投屏设备列表
 */
public class DeviceListActivity extends VMActivity {

    @BindView(R.id.widget_toolbar) Toolbar toolbar;

    private DeviceListFragment deviceListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        ButterKnife.bind(activity);

        init();
    }

    private void init() {

        toolbar.setTitle("选择投屏设备");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deviceListFragment = new DeviceListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fts = fragmentManager.beginTransaction();
        fts.replace(R.id.fragment_container, deviceListFragment);
        fts.commit();
    }
}
