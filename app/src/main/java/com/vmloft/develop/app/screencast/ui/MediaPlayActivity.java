package com.vmloft.develop.app.screencast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vmloft.develop.app.screencast.R;
import com.vmloft.develop.app.screencast.callback.ControlCallback;
import com.vmloft.develop.app.screencast.entity.AVTransportInfo;
import com.vmloft.develop.app.screencast.entity.RemoteItem;
import com.vmloft.develop.app.screencast.entity.RenderingControlInfo;
import com.vmloft.develop.app.screencast.manager.ClingManager;
import com.vmloft.develop.app.screencast.manager.ControlManager;
import com.vmloft.develop.app.screencast.ui.event.ControlEvent;
import com.vmloft.develop.library.tools.VMActivity;
import com.vmloft.develop.library.tools.utils.VMDateUtil;
import com.vmloft.develop.library.tools.utils.VMLog;

import org.fourthline.cling.support.model.item.Item;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzan13 on 2018/3/19.
 * 播放控制界面
 */
public class MediaPlayActivity extends VMActivity {
    @BindView(R.id.widget_toolbar) Toolbar toolbar;

    @BindView(R.id.text_content_title) TextView contentTitleView;
    @BindView(R.id.text_content_url) TextView contentUrlView;

    @BindView(R.id.img_volume) ImageView volumeView;
    @BindView(R.id.seek_bar_volume) SeekBar volumeSeekbar;
    @BindView(R.id.seek_bar_progress) SeekBar progressSeekbar;
    @BindView(R.id.text_play_time) TextView playTimeView;
    @BindView(R.id.text_play_max_time) TextView playMaxTimeView;
    @BindView(R.id.img_stop) ImageView stopView;
    @BindView(R.id.img_previous) ImageView previousView;
    @BindView(R.id.img_play) ImageView playView;
    @BindView(R.id.img_next) ImageView nextView;

    public Item localItem;
    public RemoteItem remoteItem;

    private int defaultVolume = 10;
    private int currVolume = defaultVolume;
    private boolean isMute = false;
    private int currProgress = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_play);

        ButterKnife.bind(activity);

        init();
    }

    private void init() {
        String title = getString(R.string.title_cast_control);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        localItem = ClingManager.getInstance().getLocalItem();
        remoteItem = ClingManager.getInstance().getRemoteItem();
        String url = "";
        String duration = "";
        if (localItem != null) {
            title = localItem.getTitle();
            url = localItem.getFirstResource().getValue();
            duration = localItem.getFirstResource().getDuration();
        }
        if (remoteItem != null) {
            title = remoteItem.getTitle();
            url = remoteItem.getUrl();
            duration = remoteItem.getDuration();
        }
        toolbar.setTitle(title);
        contentTitleView.setText(title);
        contentUrlView.setText(url);

        if (!TextUtils.isEmpty(duration)) {
            playMaxTimeView.setText(duration);
            progressSeekbar.setMax((int) VMDateUtil.fromTimeString(duration));
        }

        setVolumeSeekListener();
        setProgressSeekListener();
    }

    /**
     * 设置音量拖动监听
     */
    private void setVolumeSeekListener() {
        volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                VMLog.d("Volume seek position: %d", progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setVolume(seekBar.getProgress());
            }
        });
    }

    /**
     * 设置播放进度拖动监听
     */
    private void setProgressSeekListener() {
        progressSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currProgress = seekBar.getProgress();
                playTimeView.setText(VMDateUtil.toTimeString(currProgress));
                seekCast(currProgress);
            }
        });
    }

    @OnClick({R.id.img_volume, R.id.img_stop, R.id.img_previous, R.id.img_play, R.id.img_next})
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.img_volume:
            mute();
            break;
        case R.id.img_stop:
            stop();
            break;
        case R.id.img_previous:
            break;
        case R.id.img_play:
            play();
            break;
        case R.id.img_next:

            break;
        }
    }

    /**
     * 静音开关
     */
    private void mute() {
        // 先获取当前是否静音
        isMute = ControlManager.getInstance().isMute();
        ControlManager.getInstance().muteCast(!isMute, new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setMute(!isMute);
                if (isMute) {
                    if (currVolume == 0) {
                        currVolume = defaultVolume;
                    }
                    setVolume(currVolume);
                }
                // 这里是根据之前的状态判断的
                if (isMute) {
                    volumeView.setImageResource(R.drawable.ic_volume_up_24dp);
                } else {
                    volumeView.setImageResource(R.drawable.ic_volume_off_24dp);
                }
            }

            @Override
            public void onError(int code, String msg) {
                showToast(String.format("Mute cast failed %s", msg));
            }
        });
    }

    /**
     * 设置音量大小
     */
    private void setVolume(int volume) {
        currVolume = volume;
        ControlManager.getInstance().setVolumeCast(volume, new ControlCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int code, String msg) {
                showToast(String.format("Set cast volume failed %s", msg));
            }
        });
    }


    /**
     * 播放开关
     */
    private void play() {
        if (ControlManager.getInstance().getState() == ControlManager.CastState.STOPED) {
            if (localItem != null) {
                newPlayCastLocalContent();
            } else {
                newPlayCastRemoteContent();
            }
        } else if (ControlManager.getInstance().getState() == ControlManager.CastState.PAUSED) {
            playCast();
        } else if (ControlManager.getInstance().getState() == ControlManager.CastState.PLAYING) {
            pauseCast();
        } else {
            Toast.makeText(activity, "正在连接设备，稍后操作", Toast.LENGTH_SHORT).show();
        }
    }

    private void stop() {
        ControlManager.getInstance().unInitScreenCastCallback();
        stopCast();
    }

    private void newPlayCastLocalContent() {
        ControlManager.getInstance().setState(ControlManager.CastState.TRANSITIONING);
        ControlManager.getInstance().newPlayCast(localItem, new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);
                ControlManager.getInstance().initScreenCastCallback();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playView.setImageResource(R.drawable.ic_pause_circle_outline_24dp);
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
                showToast(String.format("New play cast local content failed %s", msg));
            }
        });
    }

    private void newPlayCastRemoteContent() {
        ControlManager.getInstance().setState(ControlManager.CastState.TRANSITIONING);
        ControlManager.getInstance().newPlayCast(remoteItem, new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);
                ControlManager.getInstance().initScreenCastCallback();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playView.setImageResource(R.drawable.ic_pause_circle_outline_24dp);
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
                showToast(String.format("New play cast remote content failed %s", msg));
            }
        });
    }

    private void playCast() {
        ControlManager.getInstance().playCast(new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playView.setImageResource(R.drawable.ic_pause_circle_outline_24dp);
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                showToast(String.format("Play cast failed %s", msg));
            }
        });
    }

    private void pauseCast() {
        ControlManager.getInstance().pauseCast(new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.PAUSED);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playView.setImageResource(R.drawable.ic_play_circle_outline_24dp);
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                showToast(String.format("Pause cast failed %s", msg));
            }
        });
    }

    private void stopCast() {
        ControlManager.getInstance().stopCast(new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playView.setImageResource(R.drawable.ic_play_circle_outline_24dp);
                        onFinish();
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                showToast(String.format("Stop cast failed %s", msg));
            }
        });
    }

    /**
     * 改变投屏进度
     */
    private void seekCast(int progress) {
        String target = VMDateUtil.toTimeString(progress);
        ControlManager.getInstance().seekCast(target, new ControlCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int code, String msg) {
                showToast(String.format("Seek cast failed %s", msg));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(ControlEvent event) {
        AVTransportInfo avtInfo = event.getAvtInfo();
        if (avtInfo != null) {
            if (!TextUtils.isEmpty(avtInfo.getState())) {
                if (avtInfo.getState().equals("TRANSITIONING")) {
                    ControlManager.getInstance().setState(ControlManager.CastState.TRANSITIONING);
                } else if (avtInfo.getState().equals("PLAYING")) {
                    ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);
                    playView.setImageResource(R.drawable.ic_pause_circle_outline_24dp);
                } else if (avtInfo.getState().equals("PAUSED_PLAYBACK")) {
                    ControlManager.getInstance().setState(ControlManager.CastState.PAUSED);
                    playView.setImageResource(R.drawable.ic_play_circle_outline_24dp);
                } else if (avtInfo.getState().equals("STOPPED")) {
                    ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
                    playView.setImageResource(R.drawable.ic_play_circle_outline_24dp);
                    onFinish();
                } else {
                    ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
                    playView.setImageResource(R.drawable.ic_play_circle_outline_24dp);
                    onFinish();
                }
            }
            if (!TextUtils.isEmpty(avtInfo.getMediaDuration())) {
                playMaxTimeView.setText(avtInfo.getMediaDuration());
            }
            if (!TextUtils.isEmpty(avtInfo.getTimePosition())) {
                long progress = VMDateUtil.fromTimeString(avtInfo.getTimePosition());
                progressSeekbar.setProgress((int) progress);
                playTimeView.setText(avtInfo.getTimePosition());
            }
        }

        RenderingControlInfo rcInfo = event.getRcInfo();
        if (rcInfo != null && ControlManager.getInstance()
                .getState() != ControlManager.CastState.STOPED) {
            if (rcInfo.isMute() || rcInfo.getVolume() == 0) {
                volumeView.setImageResource(R.drawable.ic_volume_off_24dp);
                ControlManager.getInstance().setMute(true);
            } else {
                volumeView.setImageResource(R.drawable.ic_volume_up_24dp);
                ControlManager.getInstance().setMute(false);
            }
            volumeSeekbar.setProgress(rcInfo.getVolume());
        }
    }

    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.play_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.screen_cast:
            startActivity(new Intent(activity, DeviceListActivity.class));
            break;
        default:
            return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
