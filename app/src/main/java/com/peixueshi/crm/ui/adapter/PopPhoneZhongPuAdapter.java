package com.peixueshi.crm.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import com.peixueshi.crm.R;
import com.peixueshi.crm.activity.MusicService;
import com.peixueshi.crm.app.utils.NetUtils;
import com.peixueshi.crm.utils.DownLoadUtil;
import com.peixueshi.crm.utils.PromptManager;
import com.peixueshi.crm.utils.Util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PopPhoneZhongPuAdapter extends BaseAdapter {

    private List<Map<String,String>> mapInfos;

    private Activity activity;
    private MusicService musicService = new MusicService();

    public void setData(List<Map<String, String>> listPidInfo) {
        this.mapInfos = listPidInfo;
    }

    @Override
    public int getCount() {
        return mapInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mapInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_phone_zhong_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        Map<String,String> info = mapInfos.get(position);
        String phone = info.get("targer_number");
        String newPhone;
        if(phone != null && phone.length() == 11){
             newPhone = phone.substring(0,3)+"****"+phone.substring(7,phone.length());
        }else{
             newPhone = phone;
        }

        if(NetUtils.isEnable(activity)){
            holder.tv_phone.setText(phone);
        }else{
            holder.tv_phone.setText(newPhone);
        }
        holder.tv_call_time.setText(Util.getDataTime(info.get("call_time")));
        holder.tv_call_state.setText("??????:"+info.get("main_number"));
        holder.tv_call_zhu.setText("????????????:"+info.get("call_numbers")+"  ??????:"+info.get("p_name"));

        if(Integer.valueOf(info.get("talk_time")) == 0){
            holder.tv_call_long.setText("?????????");
            holder.tv_phone.setTextColor(activity.getResources().getColor(R.color.red_f4333c));
        }else{
            holder.tv_call_long.setText(Util.getTime(Integer.valueOf(info.get("talk_time"))));
            holder.tv_phone.setTextColor(activity.getResources().getColor(R.color.text_common_gray));
        }


        if(mListener!=null){
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnItemClickListener(position);
                }
            });
        }

        holder.iv_play.setOnClickListener(new View.OnClickListener() {
            String file;
            @Override
            public void onClick(View v) {
                String recording = info.get("recording");
                String substring = recording.substring(recording.lastIndexOf("/"));
                Log.e("tag", "onClick: "+substring );
                if (TextUtils.isEmpty(recording)){
                    PromptManager.showMyToast("?????????????????????????????????",activity);
                    return;
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AsyncTask<String,Integer,String>(){


                            @Override
                            protected String doInBackground(String... strings) {
                                file = DownLoadUtil.downLoad(recording,substring);
                                return file;
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                if(file == null){
                                    PromptManager.showMyToast("?????????????????????????????????",activity);
                                    return;
                                }
                                musicService.play(file);
                                showDialog();
                            }
                        }.execute();
                    }
                });


            }
        });
        return convertView;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //??????UI
            int mMax = musicService.player.getDuration()/1000;//????????????
            if (msg.what == UPDATE) {
                try {
                    seekBar.setProgress(msg.arg1);
                    if(duratTotal >= 0){
                        tv_duration_end.setText("-"+Util.getTime(duratTotal));
                    }

                  /*  tv_duration.setText(mMax);
                    tv_duration_end.setText(msg.arg2 / 1000);*/
//                        txtInfo.setText(setPlayInfo(msg.arg2 / 1000, mMax / 1000));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                seekBar.setProgress(0);
//                    txtInfo.setText("??????????????????");
            }
        }
    };

    private View playView;
    private SeekBar seekBar;
    private SeekBar vb_video;
    private ImageView iv_star;
    private TextView tv_duration;
    private TextView tv_duration_end;
    AudioManager am;
   public void videoVoiceControll(){
       am = (AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);
       //????????????????????????
       int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
       vb_video.setMax(maxVolume);
       //??????????????????
       int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
       vb_video.setProgress(currentVolume);


       vb_video.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               if(fromUser){
                   //??????????????????
                   am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                   int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                   vb_video.setProgress(currentVolume);
               }
           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }
           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {
           }
       });
    }

    int duratTotal;
    ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int position, mMax, sMax;
            while (!Thread.currentThread().isInterrupted()) {
                if (musicService.player != null && musicService.player.isPlaying()) {
                    duratTotal--;
                    position = musicService.getCurrentProgress();//??????????????????????????????(???)
                    mMax = musicService.player.getDuration();//????????????
                    sMax = seekBar.getMax();//seekBar????????????????????????
                    Message m = handler.obtainMessage();//????????????Message
                    m.arg1 = position * sMax / mMax;//seekBar?????????????????????
                    m.arg2 = position;
                    m.what = UPDATE;
                    handler.sendMessage(m);
                    //  handler.sendEmptyMessage(UPDATE);
                    try {
                        Thread.sleep(1000);// ?????????1???????????????????????????
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };// ??????????????????????????????
    //???????????????handler??????
    private void showDialog() {
        if(playView == null){
            playView = View.inflate(activity, R.layout.video_play_view,null);
            seekBar = playView.findViewById(R.id.sb);
            vb_video = playView.findViewById(R.id.vb_video);
             iv_star = playView.findViewById(R.id.iv_star);
             tv_duration = playView.findViewById(R.id.tv_duration);
             tv_duration_end = playView.findViewById(R.id.tv_duration_end);
        }
        //??????
        iv_star.setBackgroundResource(R.drawable.dia_play_pre);


        duratTotal = musicService.player.getDuration()/1000;
        tv_duration.setText(Util.getTime(duratTotal));
      /*playView.findViewById(R.id.iv_voice_right).getBackground().setAlpha(3);
        playView.findViewById(R.id.iv_voice_left).getBackground().setAlpha(3);*/

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {//????????????SeekBar??????????????????

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//????????????SeekBar????????????

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//????????????SeekBar????????????  SeekBar????????????????????????
                int progress = seekBar.getProgress();
                Log.i("TAG:", "" + progress + "");
                int musicMax = musicService.player.getDuration(); //??????????????????????????????
                int seekBarMax = seekBar.getMax();
                musicService.player.seekTo(musicMax * progress / seekBarMax);//??????????????????
           /*     autoChange = true;
                manulChange = false;*/
            }
        });

        iv_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlay){
                    isPlay = false;
                    iv_star.setBackgroundResource(R.drawable.dia_play_nor);
                    musicService.pause();
                }else{
                    isPlay = true;
                    iv_star.setBackgroundResource(R.drawable.dia_play_pre);
                    musicService.goPlay();
                }
            }
        });
        PromptManager.showCustomDialogVideo(activity,playView , Gravity.CENTER,Gravity.CENTER);




     /*   thread = new Thread(runnable);
        thread.start();*/


        mExecutorService.submit(runnable);

    }

    private boolean isPlay = true;
    private int UPDATE = 1;
    static class ViewHolder{
        TextView tv_phone;
        TextView tv_call_time;
        TextView tv_call_state;
        TextView tv_call_long;
        ImageView iv_play;
        TextView tv_call_zhu;
        public ViewHolder(View view) {//tv_call_state
            tv_phone =  view.findViewById(R.id.tv_phone);
            tv_call_time =  view.findViewById(R.id.tv_call_time);
            tv_call_state = view.findViewById(R.id.tv_call_state);
            tv_call_long =  view.findViewById(R.id.tv_call_long);
            iv_play =  view.findViewById(R.id.iv_play);
            tv_call_zhu = view.findViewById(R.id.tv_call_zhu);
        }

    }

    public PopPhoneZhongPuAdapter(Activity activity, List<Map<String,String>> infos) {
        this.activity = activity;
        mapInfos = infos;
    }

  /*  @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_phone_lister_item, parent, false);
        ViewHolder holder = new ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            Map<String,String> info = mapInfos.get(position);
            holder.tv_xiangmu.setText(info.get("cord_name"));
            holder.tv_phone.setText(info.get("cstu_phone"));
            holder.tv_lister_time.setText("????????????:  "+Util.getTime(Integer.valueOf(info.get("cord_lecture_at")))+"??????");
            holder.tv_lister_at.setText("????????????:  "+Util.stampToDate(info.get("stuinfo_at")));

        if(mListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnItemClickListener(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mapInfos.size();
    }
*/
    public interface OnItemClickListener{
        void OnItemClickListener(int pos);
    }
    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener mListener){
        this.mListener=mListener;
    }
    public String TAG = "UserDataZiAdapter";

}