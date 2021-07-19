package com.peixueshi.crm;

import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peixueshi.crm.audio.interfaces.IAudioCallback;
import com.peixueshi.crm.audio.interfaces.IPhoneState;
import com.peixueshi.crm.audio.utils.AudioRecorder;
import com.peixueshi.crm.audio.utils.AudioStatus;
import com.peixueshi.crm.audio.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by ZhouMeng on 2018/9/5.
 * 主页，主要用来测试功能
 */

public class AudioStartUtil implements  IAudioCallback, IPhoneState {

    public static AudioStartUtil instance;
    private TextView tvRecordTime;
    private LinearLayout llReset;
    private ImageView ivController;
    private LinearLayout llFinish;

    private AudioRecorder audioRecorder;
    private boolean isKeepTime;
    /**
     * 支持定时和周期性执行的线程池
     */
    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
    private int time;
    private static final int INITIAL_DELAY = 0;
    private static final int PERIOD = 1000;

    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    private List<String> mPermissionList = new ArrayList<>();
    private final static int ACCESS_FINE_ERROR_CODE = 0x0245;

//    private MyHandler myHandler = new MyHandler(this);
    private final static int HANDLER_CODE = 0x0249;

    public void onCreate() {
        initData();
        instance = this;
    }



    /**
     * 初始化数据
     */
    private void initData() {

        audioRecorder = AudioRecorder.getInstance(this);
        scheduledThreadPool.scheduleAtFixedRate(() -> {
            if (isKeepTime) {
                ++time;
//                myHandler.sendEmptyMessage(HANDLER_CODE);
            }
        }, INITIAL_DELAY, PERIOD, TimeUnit.MILLISECONDS);


    }









    /**
     * 暂停录音和状态修改
     */
    private void phoneToPause() {
        audioRecorder.pauseRecord();
        ivController.setImageResource(R.drawable.icon_stop);
        isKeepTime = false;
    }



    public void videoStart(String fileName) {
        try
        {
            if (audioRecorder.getStatus() == AudioStatus.STATUS_NO_READY) {
                //初始化录音
//                String fileName = new SimpleDateFormat("yyyyMMddhhmmss", Locale.CHINA).format(new Date());
                audioRecorder.createDefaultAudio(fileName);
                audioRecorder.startRecord();
                ivController.setImageResource(R.drawable.icon_start);
                isKeepTime = true;

            } else {
                if (audioRecorder.getStatus() == AudioStatus.STATUS_START) {
                    phoneToPause();
                } else {
                    audioRecorder.startRecord();
                    ivController.setImageResource(R.drawable.icon_start);
                    isKeepTime = true;
                }
            }
        } catch(
        IllegalStateException e)

        {
            e.printStackTrace();
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_controller:
                String  fileNameKey = System.currentTimeMillis()+ UUID.randomUUID().toString().replace("-", "");
                videoStart(fileNameKey);
                break;

            case R.id.ll_finish:
                finishAndReset();
                break;

            case R.id.ll_reset:
                audioRecorder.setReset();
                finishAndReset();
                break;
        }
    }

    private void finishAndReset() {
        isKeepTime = false;
        audioRecorder.stopRecord();
        ivController.setImageResource(R.drawable.icon_stop);
        time = 0;

    }



    @Override
    public void phone() {
        if (audioRecorder.getStatus() == AudioStatus.STATUS_START) {
//            phoneToPause();
        }
    }

    public void requestOver(Message msg) {
        switch (msg.what) {
            case HANDLER_CODE:
                tvRecordTime.setText(TimeUtil.formatLongToTimeStr(time));
                break;
        }
    }

    @Override
    public void showPlay(String filePath) {

    }
}