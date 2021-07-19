package com.peixueshi.crm.base;


import com.peixueshi.crm.bean.LoginData;

import org.litepal.LitePal;

import java.util.List;

/**
 * 作者:sgm
 * 描述:存储用户信息的数据库操作类
 */
public class UserManager {
    public static List<LoginData> login;
    public static boolean IS_LOGIN;
    public  static void initData(){
        login = LitePal.findAll(LoginData.class);
//        LogUtils.debugInfo("sgm","====长度：" + login.size());
        if(login!=null && login.size()>0){
            IS_LOGIN = true;
//            LogUtils.debugInfo("sgm","====openid：" + login.get(0).getOpenid());
//            LogUtils.debugInfo("sgm","====uid：" + login.get(0).getId());
        }
    }

    public  static LoginData getUser(){
        List<LoginData> login = LitePal.findAll(LoginData.class);
        return login.get(0);
    }

    //账号退出调用即可
    public  static void layout(){
        LitePal.deleteAll(LoginData.class);
        IS_LOGIN = false;
    }
}
