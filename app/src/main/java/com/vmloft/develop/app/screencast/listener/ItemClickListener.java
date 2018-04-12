package com.vmloft.develop.app.screencast.listener;

import com.vmloft.develop.library.tools.adapter.VCommonListener;

/**
 * Created by lzan13 on 2018/3/10.
 */
public abstract class ItemClickListener implements VCommonListener {
    @Override
    public abstract void onItemAction(int action, Object object);
}
