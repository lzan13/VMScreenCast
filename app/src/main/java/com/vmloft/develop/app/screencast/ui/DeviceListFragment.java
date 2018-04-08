package com.vmloft.develop.app.screencast.ui;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.widget.Toast;

import com.vmloft.develop.app.screencast.R;
import com.vmloft.develop.app.screencast.callback.ItemClickListener;
import com.vmloft.develop.app.screencast.entity.ClingDevice;
import com.vmloft.develop.app.screencast.manager.DeviceManager;
import com.vmloft.develop.app.screencast.ui.adapter.ClingDeviceAdapter;
import com.vmloft.develop.app.screencast.ui.event.DeviceEvent;
import com.vmloft.develop.library.tools.VMFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lzan13 on 2018/3/9.
 */

public class DeviceListFragment extends VMFragment {

    @BindView(R.id.recycler_view) RecyclerView recycleView;

    private ClingDeviceAdapter adapter;
    private LayoutManager layoutManager;

    @Override
    protected int initLayoutId() {
        return R.layout.framgnet_device_list;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this, getView());

        layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        adapter = new ClingDeviceAdapter(activity);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(adapter);

        setItemClickListener();
    }

    @Override
    protected void initData() {

    }

    private void setItemClickListener() {
        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemAction(int action, Object object) {
                ClingDevice device = (ClingDevice) object;
                DeviceManager.getInstance().setCurrClingDevice(device);
                Toast.makeText(activity,
                        "选择了设备 " + device.getDevice().getDetails().getFriendlyName(),
                        Toast.LENGTH_LONG).show();
                refresh();
            }
        });
    }

    public void refresh() {
        if (adapter == null) {
            adapter = new ClingDeviceAdapter(activity);
            recycleView.setAdapter(adapter);
        }
        adapter.refresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(DeviceEvent event) {
        refresh();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
