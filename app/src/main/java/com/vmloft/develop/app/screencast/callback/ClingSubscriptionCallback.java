package com.vmloft.develop.app.screencast.callback;

import android.content.Context;

import com.vmloft.develop.app.screencast.callback.BaseSubscriptionCallback;
import com.vmloft.develop.library.tools.utils.VMLog;

import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.meta.Service;

import java.util.Map;

/**
 * Created by lzan13 on 2018/3/5.
 */
public class ClingSubscriptionCallback extends BaseSubscriptionCallback {
    private final String TAG = this.getClass().getSimpleName();

    protected ClingSubscriptionCallback(Service service, Context context) {
        super(service);
    }

    @Override
    protected void eventReceived(GENASubscription subscription) {
        Map values = subscription.getCurrentValues();
        if (values != null && values.containsKey("LastChange")) {
            String lastChange = values.get("LastChange").toString();
            VMLog.i("LastChange: " + lastChange);
        }
    }
}
