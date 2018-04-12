package com.vmloft.develop.app.screencast.callback;

import com.vmloft.develop.app.screencast.entity.AVTransportInfo;
import com.vmloft.develop.library.tools.utils.VMLog;

import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.avtransport.lastchange.AVTransportLastChangeParser;
import org.fourthline.cling.support.lastchange.EventedValue;
import org.fourthline.cling.support.lastchange.LastChangeParser;

import java.util.List;

/**
 * Created by lzan13 on 2018/3/5.
 */
public abstract class AVTransportCallback extends BaseSubscriptionCallback {
    private final String TAG = this.getClass().getSimpleName();
    private final String AVT_STATE = "TransportState";
    private final String AVT_DURATION = "CurrentMediaDuration";
    private final String AVT_RELATIVE_TIME = "RelativeTimePosition";
    private final String AVT_ABSOLUTE_TIME = "AbsoluteTimePosition";


    protected AVTransportCallback(Service service) {
        super(service);
    }

    @Override
    protected LastChangeParser getLastChangeParser() {
        return new AVTransportLastChangeParser();
    }

    @Override
    protected void onReceived(List<EventedValue> values) {
        AVTransportInfo info = new AVTransportInfo();
        EventedValue value = values.get(0);
        String name = value.getName();
        Object obj = value.getValue();
        VMLog.d("AVTransportCallback onReceived: %s, %s, %d", name, obj.toString(), values.size());

        if (AVT_STATE.equals(name)) {
            info.setState(value.getValue().toString());
        } else if (AVT_DURATION.equals(name)) {
            info.setMediaDuration(value.getValue().toString());
        } else if (AVT_RELATIVE_TIME.equals(name)) {
            // info.setTimePosition((String) value.getValue());
        } else if (AVT_ABSOLUTE_TIME.equals(name)) {
            info.setTimePosition(value.getValue().toString());
        }

        received(info);
    }

    protected abstract void received(AVTransportInfo info);
}
