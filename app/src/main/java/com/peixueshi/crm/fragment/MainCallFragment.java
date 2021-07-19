package com.peixueshi.crm.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.mf.library.utils.ToastUtils;
import com.peixueshi.crm.R;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.utils.PromptManager;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 作者:孙家圆
 * 拨号界面
 */
public class MainCallFragment extends BaseFragment {


    @BindView(R.id.tv_text1)
    TextView tvText1;
    @BindView(R.id.tv_text2)
    TextView tvText2;
    @BindView(R.id.bt_delete)
    ImageView btDelete;
    @BindView(R.id.bt_one)
    ImageView btOne;
    @BindView(R.id.bt_two)
    ImageView btTwo;
    @BindView(R.id.bt_three)
    ImageView btThree;
    @BindView(R.id.bt_four)
    ImageView btFour;
    @BindView(R.id.bt_five)
    ImageView btFive;
    @BindView(R.id.bt_six)
    ImageView btSix;
    @BindView(R.id.bt_seven)
    ImageView btSeven;
    @BindView(R.id.bt_eight)
    ImageView btEight;
    @BindView(R.id.bt_nine)
    ImageView btNine;
    @BindView(R.id.bt_star)
    ImageView btStar;
    @BindView(R.id.bt_zero)
    ImageView btZero;
    @BindView(R.id.bt_bottom)
    ImageView btBottom;
    @BindView(R.id.bt_function)
    ImageView btFunction;
    @BindView(R.id.bt_call)
    ImageView btCall;
    @BindView(R.id.bt_all)
    ImageView btAll;
    Unbinder unbinder;
    @BindView(R.id.lin_call)
    LinearLayout linCall;
    Unbinder unbinder1;
    private String all = "";

    private static final int DTMF_DURATION_MS = 120; // 声音的播放时间
    private Object mToneGeneratorLock = new Object(); // 监视器对象锁
    private ToneGenerator mToneGenerator;             // 声音产生器
    private static boolean mDTMFToneEnabled;         // 系统参数“按键操作音”标志位

    public MainCallFragment() {
        // Required empty public constructor
    }


    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerCommonComponent.builder()
                .appComponent(appComponent)
                .commonModule(new CommonModule())
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return ArmsUtils.inflate(getActivity(), R.layout.fragment_main_call);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
//按键声音播放设置及初始化
        try {
            // 获取系统参数“按键操作音”是否开启
            mDTMFToneEnabled = Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.DTMF_TONE_WHEN_DIALING, 1) == 1;
            synchronized (mToneGeneratorLock) {
                if (mDTMFToneEnabled && mToneGenerator == null) {
                    mToneGenerator = new ToneGenerator(
                            AudioManager.STREAM_DTMF, 80); // 设置声音的大小
                    getActivity().setVolumeControlStream(AudioManager.STREAM_DTMF);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            mDTMFToneEnabled = false;
            mToneGenerator = null;
        }
        btDelete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                all = "";
                tvText1.setText("");
                tvText2.setText("");
                return true;
            }
        });
    }

    @Override
    public void setData(@Nullable Object data) {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            Log.e("tag", "onHiddenChanged: " );
            linCall.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 播放按键声音
     */
    private void playTone(int tone) {
        if (!mDTMFToneEnabled) {
            return;
        }
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        if (ringerMode == AudioManager.RINGER_MODE_SILENT
                || ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
            // 静音或者震动时不发出声音
            return;
        }
        synchronized (mToneGeneratorLock) {
            if (mToneGenerator == null) {
                Log.w(TAG, "playTone: mToneGenerator == null, tone: " + tone);
                return;
            }
            mToneGenerator.startTone(tone, DTMF_DURATION_MS);   //发出声音
        }
    }

    @OnClick({R.id.tv_text2,R.id.bt_delete, R.id.bt_one, R.id.bt_two, R.id.bt_three, R.id.bt_four, R.id.bt_five, R.id.bt_six, R.id.bt_seven, R.id.bt_eight, R.id.bt_nine, R.id.bt_star, R.id.bt_zero, R.id.bt_bottom, R.id.bt_function, R.id.bt_call, R.id.bt_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_text2:
                linCall.setVisibility(View.VISIBLE);
                break;

            case R.id.bt_delete:
                deleteNum();
                break;
            case R.id.bt_one:
                all += "1";
                Log.e("tag", "onViewClicked: " + all + "====" + 1);
                tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_1);
                break;
            case R.id.bt_two:
                all += "2";
                tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_2);
                break;
            case R.id.bt_three:

                all += "3";
                tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_3);
                break;
            case R.id.bt_four:
                all += "4";
                tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_4);
                break;
            case R.id.bt_five:
                all +="5";
                tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_5);
                break;
            case R.id.bt_six:
                all += "6";
                tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_6);
                break;
            case R.id.bt_seven:
                all += "7";
                tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_7);
                break;
            case R.id.bt_eight:
                all += "8";
                tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_8);
                break;
            case R.id.bt_nine:
                all += "9";
                tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_9);
                break;
            case R.id.bt_star:
                all += "*";
                tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_S);
                break;
            case R.id.bt_zero:
                all += "0";
                tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_0);
                break;
            case R.id.bt_bottom:
                all += "#";
                tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_P);
                break;
            case R.id.bt_function:
                linCall.setVisibility(View.GONE);
                break;
            case R.id.bt_call:
                SubscriptionManager sm = SubscriptionManager.from(getContext());
                @SuppressLint("MissingPermission") List<SubscriptionInfo> subs = sm.getActiveSubscriptionInfoList();
                Log.e(TAG, "onViewClicked: "+subs );
                if (subs == null) {
                    Log.d(TAG, "checkDualSim: " + "no sim");
                    PromptManager.showMyToast("no sim",getActivity());
                    return;
                }else {
                    if (subs.size()>0){
                        if (all.matches("^(1)\\d{10}$")){
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + all));
                            startActivity(intent);
                        }else {
                            ToastUtils.showShort("请输入正确的手机号");
                        }
                    }else {
                        PromptManager.showMyToast("no sim",getActivity());
                    }

                }


                break;
            case R.id.bt_all:
               deleteNum();
                break;
        }
    }
    public void  deleteNum(){
        String s1 = tvText2.getText().toString();

        if ( s1!= null) {
            if (s1.length() > 0) {
                all = s1.substring(0, s1.length() - 1);
                tvText1.setText(all);
                tvText2.setText(all);
            }
        }
    }

}
