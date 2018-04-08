package com.vmloft.develop.app.screencast.manager;

import com.vmloft.develop.app.screencast.VConstants;
import com.vmloft.develop.app.screencast.VError;
import com.vmloft.develop.app.screencast.callback.ControlCallback;
import com.vmloft.develop.app.screencast.entity.ClingDevice;
import com.vmloft.develop.app.screencast.entity.RemoteItem;
import com.vmloft.develop.app.screencast.utils.ClingUtil;
import com.vmloft.develop.library.tools.utils.VMLog;

import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.avtransport.callback.Pause;
import org.fourthline.cling.support.avtransport.callback.Play;
import org.fourthline.cling.support.avtransport.callback.Seek;
import org.fourthline.cling.support.avtransport.callback.SetAVTransportURI;
import org.fourthline.cling.support.avtransport.callback.Stop;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.ProtocolInfo;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.item.Item;
import org.fourthline.cling.support.model.item.VideoItem;
import org.fourthline.cling.support.renderingcontrol.callback.GetVolume;
import org.fourthline.cling.support.renderingcontrol.callback.SetMute;
import org.fourthline.cling.support.renderingcontrol.callback.SetVolume;
import org.seamless.util.MimeType;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lzan13 on 2018/3/10.
 * 控制点管理器
 */
public class ControlManager {

    // 视频传输服务
    public static final String AV_TRANSPORT = "AVTransport";
    // DMR 设备的控制服务
    public static final String RENDERING_CONTROL = "RenderingControl";


    private static ControlManager instance;
    private Service avtService;
    private Service rcService;

    private CastState state = CastState.STOP;
    private boolean isMute = false;

    private ControlManager() {
        avtService = findServiceFromDevice(AV_TRANSPORT);
        rcService = findServiceFromDevice(RENDERING_CONTROL);
    }

    public static ControlManager getInstance() {
        if (instance == null) {
            instance = new ControlManager();
        }
        return instance;
    }

    /**
     * 开始新的投屏播放，需要先停止上一次的投屏
     *
     * @param item 需要投屏播放的本地资源对象
     */
    public void newPlayCast(final Item item, final ControlCallback callback) {
        stopCast(new ControlCallback() {
            @Override
            public void onSuccess() {
                setAVTransportURI(item, new ControlCallback() {
                    @Override
                    public void onSuccess() {
                        playCast(callback);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        callback.onError(code, msg);
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                callback.onError(code, msg);
            }
        });
    }

    /**
     * 开始投屏，需要先停止上一个投屏
     *
     * @param item 需要投屏的远程网络资源对象
     */
    public void newPlayCast(final RemoteItem item, final ControlCallback callback) {
        stopCast(new ControlCallback() {
            @Override
            public void onSuccess() {
                setAVTransportURI(item, new ControlCallback() {
                    @Override
                    public void onSuccess() {
                        playCast(callback);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        callback.onError(code, msg);
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                callback.onError(code, msg);
            }
        });
    }

    /**
     * 播放投屏
     */
    public void playCast(final ControlCallback callback) {
        if (checkAVTService()) {
            callback.onError(VError.SERVICE_IS_NULL, "AVTService is null");
            return;
        }
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new Play(avtService) {
            @Override
            public void success(ActionInvocation invocation) {
                VMLog.i("Play success");
                callback.onSuccess();
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                VMLog.e("Play error %s", msg);
                callback.onError(VError.UNKNOWN, msg);
            }
        });
    }

    /**
     * 暂停投屏
     */
    public void pauseCast(final ControlCallback callback) {
        if (checkAVTService()) {
            callback.onError(VError.SERVICE_IS_NULL, "AVTService is null");
            return;
        }
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new Pause(avtService) {
            @Override
            public void success(ActionInvocation invocation) {
                VMLog.i("Pause success");
                callback.onSuccess();
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                VMLog.e("Pause error %s", msg);
                callback.onError(VError.UNKNOWN, msg);
            }
        });
    }

    /**
     * 停止投屏
     */
    public void stopCast(final ControlCallback callback) {
        if (checkAVTService()) {
            callback.onError(VError.SERVICE_IS_NULL, "AVTService is null");
            return;
        }
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new Stop(avtService) {
            @Override
            public void success(ActionInvocation invocation) {
                VMLog.i("Stop success");
                callback.onSuccess();
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                VMLog.e("Stop error %s", msg);
                callback.onError(VError.UNKNOWN, msg);
            }
        });
    }

    /**
     * 设置投屏进度
     *
     * @param target 目标进度
     */
    public void seekCast(final String target, final ControlCallback callback) {
        if (checkAVTService()) {
            callback.onError(VError.SERVICE_IS_NULL, "AVTService is null");
            return;
        }
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new Seek(avtService, target) {
            @Override
            public void success(ActionInvocation invocation) {
                VMLog.d("Seek success - %s", target);
                callback.onSuccess();
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                VMLog.e("Seek error %s", msg);
                callback.onError(VError.UNKNOWN, msg);
            }
        });
    }

    /**
     * 设置投屏音量
     */
    public void setVolumeCast(int volume, final ControlCallback callback) {
        if (checkRCService()) {
            callback.onError(VError.SERVICE_IS_NULL, "RCService is null");
            return;
        }
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new SetVolume(rcService, volume) {
            @Override
            public void success(ActionInvocation invocation) {
                VMLog.d("setVolume success");
                callback.onSuccess();
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                VMLog.e("setVolume error %s", msg);
                callback.onError(VError.UNKNOWN, msg);
            }
        });
    }

    /**
     * 获取投屏音量
     */
    public void getVolumeCast(final ControlCallback callback) {
        if (checkRCService()) {
            callback.onError(VError.SERVICE_IS_NULL, "RCService is null");
            return;
        }
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new GetVolume(rcService) {
            @Override
            public void received(ActionInvocation actionInvocation, int currentVolume) {
                VMLog.d("getVolume success");
                callback.onSuccess();
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                VMLog.e("getVolume error %s", msg);
                callback.onError(VError.UNKNOWN, msg);
            }
        });
    }

    /**
     * 静音投屏
     */
    public void muteCast(boolean mute, final ControlCallback callback) {
        if (checkRCService()) {
            callback.onError(VError.SERVICE_IS_NULL, "RCService is null");
            return;
        }
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new SetMute(rcService, mute) {
            @Override
            public void success(ActionInvocation invocation) {
                VMLog.d("Mute success");
                callback.onSuccess();
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                VMLog.e("Mute error %s", msg);
                callback.onError(VError.UNKNOWN, msg);
            }
        });
    }

    /**
     * 设置本地资源音视频传输 URI
     *
     * @param item 需要投屏的资源
     */
    public void setAVTransportURI(Item item, final ControlCallback callback) {
        if (checkAVTService()) {
            callback.onError(VError.SERVICE_IS_NULL, "service is null");
            return;
        }
        final String uri = item.getFirstResource().getValue();
        DIDLContent content = new DIDLContent();
        content.addItem(item);
        String metadata = "";
        try {
            metadata = new DIDLParser().generate(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        VMLog.d("metadata: %s", metadata);
        UnsignedIntegerFourBytes instanceId = new UnsignedIntegerFourBytes(0);
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new SetAVTransportURI(instanceId, avtService, uri, metadata) {
            @Override
            public void success(ActionInvocation invocation) {
                VMLog.i("setAVTransportURI success %s", uri);
                callback.onSuccess();
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                VMLog.e("setAVTransportURI - error %s url:%s", msg, uri);
                callback.onError(VError.UNKNOWN, msg);
            }
        });
    }

    /**
     * 设置远程资源音视频传输 URI
     */
    public void setAVTransportURI(RemoteItem item, final ControlCallback callback) {
        if (checkAVTService()) {
            callback.onError(VError.SERVICE_IS_NULL, "service is null");
            return;
        }
        String metadata = ClingUtil.getItemMetadata(item);
        VMLog.i("metadata: " + metadata);
        final String uri = item.getUrl();
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new SetAVTransportURI(avtService, item.getUrl(), metadata) {
            @Override
            public void success(ActionInvocation invocation) {
                VMLog.i("setAVTransportURI success url:%s", uri);
                callback.onSuccess();
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                VMLog.e("setAVTransportURI - error %s url:%s", msg, uri);
                callback.onError(VError.UNKNOWN, msg);
            }
        });
    }

    /**
     * 检查视频传输服务是否存在
     */
    private boolean checkAVTService() {
        if (avtService == null) {
            avtService = findServiceFromDevice(AV_TRANSPORT);
        }
        return avtService == null;
    }

    /**
     * 检查视频播放控制服务是否存在
     */
    private boolean checkRCService() {
        if (rcService == null) {
            rcService = findServiceFromDevice(RENDERING_CONTROL);
        }
        return rcService == null;
    }

    /**
     * 通过指定服务类型，搜索当前选择的设备的服务
     *
     * @param type 需要的服务类型
     */
    public Service findServiceFromDevice(String type) {
        UDAServiceType serviceType = new UDAServiceType(type);
        ClingDevice device = DeviceManager.getInstance().getCurrClingDevice();
        if (device == null) {
            return null;
        }
        return device.getDevice().findService(serviceType);
    }

    public CastState getState() {
        return state;
    }

    public void setState(CastState state) {
        this.state = state;
    }

    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean mute) {
        isMute = mute;
    }

    /**
     * 销毁，释放资源
     */
    public void destroy() {
        instance = null;
        avtService = null;
        rcService = null;
    }

    public enum CastState {
        PLAY,
        PAUSE,
        STOP,
        REQUEST
    }

}
