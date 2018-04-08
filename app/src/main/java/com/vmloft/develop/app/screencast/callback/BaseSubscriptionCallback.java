package com.vmloft.develop.app.screencast.callback;

import com.vmloft.develop.library.tools.utils.VMLog;

import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;

/**
 * Created by lzan13 on 2018/3/9.
 */
public abstract class BaseSubscriptionCallback extends SubscriptionCallback {

    // 订阅持续时间 秒，这里设置3小时
    private static final int SUB_DURATION = 60 * 60 * 3;

    protected BaseSubscriptionCallback(Service service) {
        this(service, SUB_DURATION);
    }

    protected BaseSubscriptionCallback(Service service, int requestedDurationSeconds) {
        super(service, requestedDurationSeconds);
    }

    @Override
    protected void failed(GENASubscription subscription, UpnpResponse responseStatus,
            Exception exception, String defaultMsg) {
        VMLog.d("SubscriptionCallback failed");
    }


    @Override
    protected void ended(GENASubscription subscription, CancelReason reason,
            UpnpResponse responseStatus) {
        VMLog.d("SubscriptionCallback ended");
    }

    @Override
    protected void established(GENASubscription subscription) {}

    @Override
    protected void eventReceived(GENASubscription subscription) {}

    @Override
    protected void eventsMissed(GENASubscription subscription, int numberOfMissedEvents) {}
}
