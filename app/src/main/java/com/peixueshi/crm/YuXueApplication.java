package com.peixueshi.crm;

import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.jess.arms.base.BaseApplication;
import com.peixueshi.crm.app.utils.NetUtils;
import com.mf.library.utils.Constant;
import com.taobao.sophix.SophixManager;

public class YuXueApplication extends BaseApplication {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        SophixManager.getInstance().queryAndLoadNewPatch();//热修复
        context=getApplicationContext();
        Constant.context=context ;
        Log.d("YuxueApplication","启动了");
   //     initInfos("IWlriYocQfQC2ykfmq-W5bpH5YiGwnR8HELIK-eN:gG4-Cyz8p3dwdznmHL_YszsQ2hw=:eyJzY29wZSI6IndvcmtzcGFjIiwiZGVhZGxpbmUiOjE1ODczNjQ1NzIsImNhbGxiYWNrVXJsIjoiaHR0cDovL2ZpbGUuYXBpLmJlaWppbmd5dXh1ZS5jbi9maWxlL2NhbGxiYWNrIiwiY2FsbGJhY2tCb2R5Ijoie1wia2V5XCI6XCIkKGtleSlcIixcImhhc2hcIjpcIiQoZXRhZylcIixcImZzaXplXCI6JChmc2l6ZSksXCJidWNrZXRcIjpcIiQoYnVja2V0KVwiLFwibmFtZVwiOlwiJCh4Om5hbWUpXCIsXCJ0b2tlblwiOlwiJCh4OnRva2VuKVwifSIsImNhbGxiYWNrQm9keVR5cGUiOiJhcHBsaWNhdGlvbi9qc29uIn0=");
        if (!NetUtils.isConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "网络未连接，请检查网络设置！", Toast.LENGTH_SHORT).show();
            return;
        }

        MyCatchException mException=MyCatchException.getInstance();
        mException.init(getApplicationContext());  //注册

       /* if (isMainProcess(getApplicationContext())) {
               startService(new Intent(this, LocalService.class));
             } else {
                return;
        }*/
    }

           private static MainActivity mainActivity = null;
           public static MainActivity getMainActivity() {
              return mainActivity;
            }

         public static void setMainActivity(MainActivity activity) {
              mainActivity = activity;
           }

    /**
     * 是否为主进程
     */
      public boolean isMainProcess(Context context) {
             boolean isMainProcess;
                isMainProcess = context.getApplicationContext().getPackageName().equals
                    (getCurrentProcessName(context));
               return isMainProcess;
       }


        /**
         * 获取当前进程名
         */
        public String getCurrentProcessName(Context context) {
                 int pid = android.os.Process.myPid();
                String processName = "";
                ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService
                        (Context.ACTIVITY_SERVICE);
                for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
                        if (process.pid == pid) {
                               processName = process.processName;
                           }
                    }
                return processName;
            }
}
