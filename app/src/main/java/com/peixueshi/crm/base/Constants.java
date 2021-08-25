package com.peixueshi.crm.base;



import com.peixueshi.crm.bean.NewUserInfo;
import com.peixueshi.crm.bean.TkyLoginRes;
import com.peixueshi.crm.bean.UserInfo;

/**
 * 存放全局常量
 */
public class Constants {
    /**
     * shareprefence文件名称
     */
    public static final String Prefrence = "peixueshiappConfig";
    /**
     * 已拨打电话mapkey
     */
    public static  String CALLLOGMAPKEY = "calllogmapkey";
    /**
     * 开始拨打电话timekey
     */
    public static String CALLSTARTKEY = "callstartkey";

/*
 public static String host = "https://crm.api.beijingyuxue.cn/";
测试

    public static String a = "http://admin.api.beijingyuxue.cn/work/a_get";
    public static String b = "http://dept.api.beijingyuxue.cn/project/p_list";
    public static String c = "https://crm.api.beijingyuxue.cn/order/c_list";

    public static String d = "https://crm.api.beijingyuxue.cn/login/s_list";
    public static String e = "https://crm.api.beijingyuxue.cn/login/m_major?s_id=";
    public static String f = "https://crm.api.beijingyuxue.cn/login/h_list";
    public static String g = "https://crm.api.beijingyuxue.cn/login/c_list";
*/

    public static String hosttky = "https://tky.ketianyun.com/";

   public static boolean isWifiLimit = true;
   public static String stroDir = "/PeiXueShiCall/";

    //测试服 https://crm.api.qiliangjiaoyu.com/9--22222222222220
    public static String host = "https://crm.api.pxsedu.com/";
    public static String newhost = "https://crm.api.pxsedu.com/";
    public static String allhost = "https://crm.api.pxsedu.com/";
    public static String payhost = "https://crm.api.pxsedu.com/assist/";
    public static String crmhost = "https://crm.api.pxsedu.com/";
    public static String a = "http://admin.api.pxsedu.com/work/a_get";
    public static String b = "https://crm.api.pxsedu.com/team/p_list";
    public static String c = "https://crm.api.pxsedu.com/order/c_list";

    public static String d = "https://crm.api.pxsedu.com/login/s_list";
    public static String e = "https://crm.api.pxsedu.com/login/m_major?s_id=";
    public static String f = "https://crm.api.pxsedu.com/login/h_list";
    public static String g = "https://crm.api.pxsedu.com/login/c_list";
    public static String h = "https://crm.api.pxsedu.com/order/c_list";
    public static String i = "https://crm.api.pxsedu.com/login/m_major?s_id=";

    public  static  String chuXin="http://call.chuxinjiaoyu.net";
    public static String jwtToken="";
    public static int GID=2006;//axb公司id
    public static int callCard=0;
    public static String Phone="";
  //晟龙育德
/*   public static String host = "http://b.crm.api.bjzybx.cn/";
    public static String a = "http://b.crm.api.bjzybx.cn/login/a_get";
    public static String b = "http://b.jiaowu.api.bjzybx.cn/project/p_list";
    public static String c = "http://b.crm.api.bjzybx.cn/order/c_list";
    public static String d = "http://b.crm.api.bjzybx.cn/login/s_list";
    public static String e = "http://b.crm.api.bjzybx.cn/login/m_major?s_id=";
    public static String f = "http://b.crm.api.bjzybx.cn/login/h_list";
    public static String g = "http://b.crm.api.bjzybx.cn/login/c_list";

    public static String h = "http://b.crm.api.bjzybx.cn/order/c_list";
    public static String i = "http://b.crm.api.bjzybx.cn/login/m_major?s_id=";*/

  /* public static String stroDir = "/YuXueCall/";
    public static boolean isWifiLimit = false;


    //百诺星际
    public static String host = "http://crm.api.bnxjjy.com/";
    public static String a = "http://admin.api.bnxjjy.com/work/a_get";
    public static String b = "http://crm.api.bnxjjy.com/team/p_list";
    public static String c = "http://jiaowu.api.bnxjjy.com/class/c_list";

    public static String d = "http://crm.api.bnxjjy.com/login/s_list";
    public static String e = "http://crm.api.bnxjjy.com/login/m_major?s_id=";
    public static String f = "http://jiaowu.api.bnxjjy.com/login/h_list";
    public static String g = "http://jiaowu.api.bnxjjy.com/login/c_list";

    public static String h = "http://jiaowu.api.bnxjjy.com/class/c_list";
    public static String i = "http://jiaowu.api.bnxjjy.com/login/m_major?s_id=";*/
   /*  //中宇智学
    public static String host = "https://crm.api.pxsedu.com/";

    public static String a = "http://admin.api.pxsedu.com/work/a_get";
    public static String b = "http://dept.api.pxsedu.com/project/p_list";
    public static String c = "https://crm.api.pxsedu.com/order/c_list";

    public static String d = "https://crm.api.pxsedu.com/login/s_list";
    public static String e = "https://crm.api.pxsedu.com/login/m_major?s_id=";
    public static String f = "https://crm.api.pxsedu.com/login/h_list";
    public static String g = "https://crm.api.pxsedu.com/login/c_list";
*/
    public static UserInfo.InfoBean loginUserInfo;
    public static NewUserInfo.DataBean.StaffBean newloginUserInfo;
    public static String localCallNumber;
    public static String iccID;
    public static int screenHeight;
    public static boolean headMaster;
    public static String qiniuToken;
    public static String callAt;
    public static boolean isShouZi;
    public static boolean isRunningCall;
    public static boolean isUpdatingFile;

    public static TkyLoginRes tkyUserInfo;
    public static boolean isUpdatePass;
    public static boolean weekPass;
}
