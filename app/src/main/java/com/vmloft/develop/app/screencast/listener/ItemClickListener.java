package com.vmloft.develop.app.screencast.listener;

import com.vmloft.develop.library.tools.adapter.VMAdapter;

/**
 * Created by lzan13 on 2018/3/10.
 */
public abstract class ItemClickListener implements VMAdapter.ICListener {

    @Override
    public abstract void onItemAction(int action, Object object);

    @Override
    public void onItemLongAction(int action, Object object) {
        
    }
}
