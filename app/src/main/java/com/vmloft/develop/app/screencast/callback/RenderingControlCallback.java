package com.vmloft.develop.app.screencast.callback;

import android.content.Context;

import com.vmloft.develop.app.screencast.entity.RenderingControlInfo;
import com.vmloft.develop.library.tools.utils.VMLog;

import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.avtransport.lastchange.AVTransportLastChangeParser;
import org.fourthline.cling.support.lastchange.EventedValue;
import org.fourthline.cling.support.lastchange.LastChange;
import org.fourthline.cling.support.lastchange.LastChangeParser;
import org.fourthline.cling.support.model.Channel;
import org.fourthline.cling.support.renderingcontrol.lastchange.ChannelMute;
import org.fourthline.cling.support.renderingcontrol.lastchange.ChannelVolume;
import org.fourthline.cling.support.renderingcontrol.lastchange.RenderingControlLastChangeParser;
import org.fourthline.cling.support.renderingcontrol.lastchange.RenderingControlVariable;

import java.util.List;

/**
 * Created by lzan13 on 2018/3/5.
 * 投屏播放控制相关回调，这里主要会回调音量和是否静音
 */
public abstract class RenderingControlCallback extends BaseSubscriptionCallback {
    private final String TAG = this.getClass().getSimpleName();

    protected RenderingControlCallback(Service service) {
        super(service);
    }

    @Override
    protected LastChangeParser getLastChangeParser() {
        return new RenderingControlLastChangeParser();
    }

    @Override
    protected void onReceived(List<EventedValue> values) {
        RenderingControlInfo info = new RenderingControlInfo();
        for (EventedValue entry : values) {
            if ("Mute".equals(entry.getName())) {
                Object obj = entry.getValue();
                if (obj instanceof ChannelMute) {
                    ChannelMute cm = (ChannelMute) obj;
                    if (Channel.Master.equals(cm.getChannel())) {
                        info.setMute(cm.getMute());
                    }
                }
            }
            if ("Volume".equals(entry.getName())) {
                Object obj = entry.getValue();
                if (obj instanceof ChannelVolume) {
                    ChannelVolume cv = (ChannelVolume) obj;
                    if (Channel.Master.equals(cv.getChannel())) {
                        info.setVolume(cv.getVolume());
                    }
                }
            }
            if ("PresetNameList".equals(entry.getName())) {
                Object obj = entry.getValue();
                info.setPresetNameList(obj.toString());
            }
        }
        VMLog.d("RenderingControlCallback onReceived:%b, %d", info.isMute(), info.getVolume());
        received(info);
    }

    protected abstract void received(RenderingControlInfo info);

}
