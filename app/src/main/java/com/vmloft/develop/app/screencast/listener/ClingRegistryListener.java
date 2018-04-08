package com.vmloft.develop.app.screencast.listener;

import com.vmloft.develop.app.screencast.manager.DeviceManager;
import com.vmloft.develop.library.tools.utils.VMLog;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;

/**
 * Created by lzan13 on 2018/3/9.
 * 监听当前局域网设备变化
 */
public class ClingRegistryListener extends DefaultRegistryListener {
    @Override
    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
        VMLog.d("remoteDeviceDiscoveryStarted %s", device.getDisplayString());
//        onDeviceAdded(device);
    }

    @Override
    public void remoteDeviceDiscoveryFailed(Registry registry, RemoteDevice device, Exception ex) {
        VMLog.e("remoteDeviceDiscoveryFailed %s - %s", device.getDisplayString(), ex.toString());
//        onDeviceRemoved(device);
    }

    @Override
    public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
        VMLog.i("remoteDeviceAdded %s", device.getDisplayString());
        onDeviceAdded(device);
    }

    @Override
    public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
        VMLog.e("remoteDeviceRemoved %s", device.getDisplayString());
        onDeviceRemoved(device);
    }

    @Override
    public void localDeviceAdded(Registry registry, LocalDevice device) {
        VMLog.d("localDeviceAdded %s", device.getDisplayString());
//        onDeviceAdded(device);
    }

    @Override
    public void localDeviceRemoved(Registry registry, LocalDevice device) {
        VMLog.d("localDeviceRemoved %s", device.getDisplayString());
//        onDeviceRemoved(device);
    }

    /**
     * 新增 DLNA 设备
     */
    public void onDeviceAdded(Device device) {
        DeviceManager.getInstance().addDevice(device);
    }

    /**
     * 移除 DLNA 设备
     */
    public void onDeviceRemoved(Device device) {
        DeviceManager.getInstance().removeDevice(device);
    }
}
