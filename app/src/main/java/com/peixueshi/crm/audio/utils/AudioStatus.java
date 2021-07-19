package com.peixueshi.crm.audio.utils;

/**
 * Created by ZhouMeng on 2018/9/5.
 * 录音对象的状态
 */

public enum AudioStatus {
    //未开始
    STATUS_NO_READY,
    //预备
    STATUS_READY,
    //录音
    STATUS_START,
    //暂停
    STATUS_PAUSE,
    //停止
    STATUS_STOP
}
