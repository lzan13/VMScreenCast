package com.vmloft.develop.app.screencast.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;

import com.vmloft.develop.app.screencast.R;
import com.vmloft.develop.app.screencast.listener.ItemClickListener;
import com.vmloft.develop.app.screencast.entity.RemoteItem;
import com.vmloft.develop.app.screencast.manager.ClingManager;
import com.vmloft.develop.app.screencast.ui.adapter.RemoteContentAdapter;
import com.vmloft.develop.library.tools.VMFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lzan13 on 2018/3/19.
 * 远程内容界面
 */
public class RemoteContentFragment extends VMFragment {
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private LayoutManager layoutManager;
    private RemoteContentAdapter adapter;

    private List<RemoteItem> contentList = new ArrayList<>();

    /**
     * 创建实例对象的工厂方法
     *
     * @return 返回一个新的实例
     */
    public static RemoteContentFragment newInstance() {
        RemoteContentFragment fragment = new RemoteContentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int initLayoutId() {
        return R.layout.fragment_remote_content;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this, getView());
        layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        adapter = new RemoteContentAdapter(activity, contentList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        setItemClickListener();
    }

    private void setItemClickListener() {
        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemAction(int action, Object object) {
                ClingManager.getInstance().setRemoteItem((RemoteItem) object);
                startActivity(new Intent(activity, MediaPlayActivity.class));
            }
        });
    }

    @Override
    protected void initData() {
        RemoteItem pfzlItem = new RemoteItem("平凡之路 - 朴树", "p1302", "朴树", 25203597, "00:05:05",
                "640x352", "http://melove.net/data/videos/mv/PingFanZhiLu.mp4");
        RemoteItem sugarItem = new RemoteItem("Sugar - 魔力红", "1452282796", "魔力红", 24410338,
                "00:05:01", "640x352", "http://melove.net/data/videos/mv/Sugar.mp4");
        RemoteItem wakeItem = new RemoteItem("Wake - Y&F", "425703", "Hillsong Young And Free",
                107362668, "00:04:33", "1280x720", "http://melove.net/data/videos/mv/Wake.mp4");
        contentList.add(pfzlItem);
        contentList.add(sugarItem);
        contentList.add(wakeItem);

        refresh();
    }

    public void refresh() {
        adapter.refresh();
    }
}
