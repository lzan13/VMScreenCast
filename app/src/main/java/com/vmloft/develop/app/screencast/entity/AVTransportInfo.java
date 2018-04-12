package com.vmloft.develop.app.screencast.entity;

/**
 * Created by lzan13 on 2018/4/11.
 */
public class AVTransportInfo {
    private String state;
    private String mediaDuration;
    private String timePosition;

    public AVTransportInfo() {}

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMediaDuration() {
        return mediaDuration;
    }

    public void setMediaDuration(String mediaDuration) {
        this.mediaDuration = mediaDuration;
    }

    public String getTimePosition() {
        return timePosition;
    }

    public void setTimePosition(String timePosition) {
        this.timePosition = timePosition;
    }
}
