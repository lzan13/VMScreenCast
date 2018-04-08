package com.vmloft.develop.app.screencast.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.widget.LinearLayout;

import com.vmloft.develop.app.screencast.R;
import com.vmloft.develop.app.screencast.callback.ItemClickListener;
import com.vmloft.develop.app.screencast.manager.ClingManager;
import com.vmloft.develop.app.screencast.ui.adapter.LocalContentAdapter;
import com.vmloft.develop.app.screencast.ui.event.DIDLEvent;
import com.vmloft.develop.library.tools.VMFragment;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzan13 on 2018/3/19.
 * 本地内容界面
 */
public class LocalContentFragment extends VMFragment {

    @BindView(R.id.layout_parent_directory) LinearLayout parentDirectory;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private LayoutManager layoutManager;
    private LocalContentAdapter adapter;
    private List<DIDLObject> objectList = new ArrayList<>();

    /**
     * 创建实例对象的工厂方法
     */
    public static LocalContentFragment newInstance() {
        LocalContentFragment fragment = new LocalContentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int initLayoutId() {
        return R.layout.fragment_local_content;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this, getView());
        layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        adapter = new LocalContentAdapter(activity, objectList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        setItemClickListener();
    }

    private void setItemClickListener() {
        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemAction(int action, Object object) {
                if (object instanceof Container) {
                    Container container = (Container) object;
                    searchLocalContent(container.getId());
                } else if (object instanceof Item) {
                    Item item = (Item) object;
                    ClingManager.getInstance().setLocalItem(item);
                    startActivity(new Intent(activity, MediaPlayActivity.class));
                }
            }
        });
    }

    @Override
    protected void initData() {
    }

    @OnClick(R.id.layout_parent_directory)
    public void onClick() {
        searchLocalContent("0");
    }


    private void searchLocalContent(String containerId) {
        ClingManager.getInstance().searchLocalContent(containerId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(DIDLEvent event) {
        objectList.clear();
        if (event.content.getContainers().size() > 0) {
            objectList.addAll(event.content.getContainers());
        } else if (event.content.getItems().size() > 0) {
            objectList.addAll(event.content.getItems());
        }
        adapter.refresh();
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
