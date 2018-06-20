package com.vmloft.develop.app.screencast;

import com.vmloft.develop.app.screencast.manager.ClingManager;
import com.vmloft.develop.library.tools.VMApp;

/**
 * Created by lzan13 on 2018/3/15.
 */
public class VApplication extends VMApp {
    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }


    public void init() {
        ClingManager.getInstance().startClingService();
    }
}
