package com.peixueshi.crm.newactivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.mf.library.utils.ToastUtils;
import com.yiguo.adressselectorlib.AddressSelector;
import com.peixueshi.crm.R;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.bean.CertificateBean;
import com.peixueshi.crm.bean.HelpSignBean;
import com.peixueshi.crm.ui.newadapter.NewISelectAdapter;
import com.peixueshi.crm.utils.JSONUtil;
import com.peixueshi.crm.view.wheel.CityPickerDialog;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.addapp.pickers.entity.City;
import cn.addapp.pickers.entity.County;
import cn.addapp.pickers.entity.Province;

public class NewSelectTypeActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.hand_text)
    TextView handText;
    @BindView(R.id.rl_title_view)
    RelativeLayout rlTitleView;
    @BindView(R.id.education_text)
    TextView educationText;
    @BindView(R.id.education_context)
    TextView educationContext;
    @BindView(R.id.education_select)
    TextView educationSelect;
    @BindView(R.id.education_rl)
    RelativeLayout educationRl;
    @BindView(R.id.area_text)
    TextView areaText;
    @BindView(R.id.area_context)
    TextView areaContext;
    @BindView(R.id.area_select)
    TextView areaSelect;
    @BindView(R.id.area_rl)
    RelativeLayout areaRl;
    @BindView(R.id.gradation_text)
    TextView gradationText;
    @BindView(R.id.gradation_context)
    TextView gradationContext;
    @BindView(R.id.gradation_select)
    TextView gradationSelect;
    @BindView(R.id.gradation_rl)
    RelativeLayout gradationRl;
    @BindView(R.id.school_profession_text)
    TextView schoolProfessionText;
    @BindView(R.id.school_profession_context)
    TextView schoolProfessionContext;
    @BindView(R.id.school_profession_select)
    TextView schoolProfessionSelect;
    @BindView(R.id.school_profession_rl)
    RelativeLayout schoolProfessionRl;
    @BindView(R.id.education_ll)
    LinearLayout educationLl;
    @BindView(R.id.project_text)
    TextView projectText;
    @BindView(R.id.project_context)
    TextView projectContext;
    @BindView(R.id.project_select)
    TextView projectSelect;
    @BindView(R.id.project_rl)
    RelativeLayout projectRl;
    @BindView(R.id.assist_apply_area_text)
    TextView assistApplyAreaText;
    @BindView(R.id.assist_apply_area_context)
    TextView assistApplyAreaContext;
    @BindView(R.id.assist_apply_area_select)
    TextView assistApplyAreaSelect;
    @BindView(R.id.assist_apply_area_rl)
    RelativeLayout assistApplyAreaRl;
    @BindView(R.id.assist_apply_ll)
    LinearLayout assistApplyLl;
    @BindView(R.id.certificate_text)
    TextView certificateText;
    @BindView(R.id.certificate_context)
    TextView certificateContext;
    @BindView(R.id.certificate_select)
    TextView certificateSelect;
    @BindView(R.id.certificate_rl)
    RelativeLayout certificateRl;
    @BindView(R.id.else_ll)
    LinearLayout elseLl;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.name_id)
    TextView nameId;
    @BindView(R.id.name_ed)
    EditText nameEd;
    @BindView(R.id.name_rl)
    RelativeLayout nameRl;
    @BindView(R.id.phone_text)
    TextView phoneText;
    @BindView(R.id.phone_ed)
    EditText phoneEd;
    @BindView(R.id.phone_rl)
    RelativeLayout phoneRl;
    @BindView(R.id.site_text)
    TextView siteText;
    @BindView(R.id.site_ed)
    EditText siteEd;
    @BindView(R.id.site_rl)
    RelativeLayout siteRl;
    @BindView(R.id.game_play)
    TextView gamePlay;
    @BindView(R.id.lls)
    LinearLayout lls;
    @BindView(R.id.address)
    AddressSelector addressSelector;
    private int type;
    private int degree_type;
    public static List<Map<String, String>> xueList = new ArrayList<>();
    public static List<HelpSignBean> xieList = new ArrayList<>();
    public static List<CertificateBean> otherList = new ArrayList<>();
    private List<HelpSignBean> helpSignBeans;
    private List<CertificateBean> certificateBeanList;

    private ArrayList<City> cities1 = new ArrayList<>();
    private ArrayList<City> cities2 = new ArrayList<>();
    private ArrayList<City> cities3 = new ArrayList<>();
    private Province province;
    private String provice;
    private String city;
    private String country;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_new_select_type;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //tab颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        if (type == 1) {
            handText.setText("选择学历");
            educationLl.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            handText.setText("协助报名");
            assistApplyLl.setVisibility(View.VISIBLE);
        } else if (type == 3) {
            handText.setText("其他");
            elseLl.setVisibility(View.VISIBLE);
        }
        provinces = new ArrayList<>();

    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }


    private class InitAreaTask extends AsyncTask<Integer, Integer, Boolean> {

        Context mContext;

        Dialog progressDialog;

        public InitAreaTask(Context context) {
            mContext = context;
            //  progressDialog = Util.createLoadingDialog(mContext, "请稍等...", true,
//                    0);
        }

        @Override
        protected void onPreExecute() {
            //   progressDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            //  progressDialog.dismiss();
            if (provinces.size() > 0) {
                Log.e("tag", "onPostExecute: ");
                showAddressDialog();
            } else {
                Toast.makeText(mContext, "数据初始化失败", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            String address = null;
            InputStream in = null;
            try {
                in = mContext.getResources().getAssets().open("addresses.txt");
                byte[] arrayOfByte = new byte[in.available()];
                in.read(arrayOfByte);
                address = EncodingUtils.getString(arrayOfByte, "UTF-8");
                JSONArray jsonList = new JSONArray(address);
                Gson gson = new Gson();
                for (int i = 0; i < jsonList.length(); i++) {
                    try {
                        provinces.add(gson.fromJson(jsonList.getString(i), Province.class));
                    } catch (Exception e) {
                    }
                }
                return true;
            } catch (Exception e) {
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
            }
            return false;
        }

    }

    private ArrayList<Province> provinces;

    private void showAddressDialog() {
        new CityPickerDialog(NewSelectTypeActivity.this, provinces, null, null, null, new CityPickerDialog.onCityPickedListener() {
            @Override
            public void onPicked(Province selectProvince, City selectCity, County selectCounty) {
                StringBuilder address = new StringBuilder();
                province = selectProvince;
                provice = selectProvince != null ? selectProvince.getAreaName() : "";
                city = selectCity != null ? selectCity.getAreaName() : "";
                country = selectCounty != null ? selectCounty.getAreaName() : "";
               /* if (country != null) {
                    address.append(provice + city + country);
                } else {
                    address.append(provice + city);
                }*/
                address.append(provice + city);
                //   assistApplyAreaContext.setItemTip(address.toString());
                assistApplyAreaContext.setText(address.toString());
            }
        }).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (xueList.size() > 0) {
            Map<String, String> info = xueList.get(xueList.size() - 1);
            String sch = info.get("sch");
            JSONObject jsonObject1 = JSONObject.parseObject(sch);
            String major = info.get("major");//专业
            JSONObject major_xueli = JSONObject.parseObject(major);
            try {
                String name1 = jsonObject1.getString("name");
                String name = major_xueli.getString("name");
                String price = major_xueli.getString("price");
                int status = major_xueli.getIntValue("status");

                // holder.tv_price.setText(price+"");
                String xueli;
                if (status == 1) {
                    xueli = "大专";
                } else if (status == 2) {
                    xueli = "中专";
                } else {
                    xueli = "本科";
                }
                schoolProfessionContext.setText(name1 + name + xueli);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ;
        }

    }

    @OnClick({R.id.iv_back, R.id.hand_text, R.id.rl_title_view, R.id.education_text, R.id.education_context, R.id.education_select, R.id.education_rl, R.id.area_text, R.id.area_context, R.id.area_select, R.id.area_rl, R.id.gradation_text, R.id.gradation_context, R.id.gradation_select, R.id.gradation_rl, R.id.school_profession_text, R.id.school_profession_context, R.id.school_profession_select, R.id.school_profession_rl, R.id.education_ll, R.id.project_text, R.id.project_context, R.id.project_select, R.id.project_rl, R.id.assist_apply_area_text, R.id.assist_apply_area_context, R.id.assist_apply_area_select, R.id.assist_apply_area_rl, R.id.assist_apply_ll, R.id.certificate_text, R.id.certificate_context, R.id.certificate_select, R.id.certificate_rl, R.id.else_ll, R.id.ll, R.id.name_id, R.id.name_ed, R.id.name_rl, R.id.phone_text, R.id.phone_ed, R.id.phone_rl, R.id.site_text, R.id.site_ed, R.id.site_rl, R.id.game_play, R.id.lls})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.hand_text://tab标题

                break;
            case R.id.rl_title_view:
                break;
            case R.id.education_text:
                break;
            case R.id.education_context:

                break;
            case R.id.education_select://学历-学历类型
                List<String> listType = new ArrayList<>();
                listType.add("网络自考");
                listType.add("网教直取");
                listType.add("成人教育");
                getPopupWindow(listType, 1);
                break;
            case R.id.education_rl:
                break;
            case R.id.area_text:
                break;
            case R.id.area_context:
                break;
            case R.id.area_select://学历-成考区域

                break;
            case R.id.area_rl:
                break;
            case R.id.gradation_text:
                break;
            case R.id.gradation_context:
                break;
            case R.id.gradation_select://学历-学历层次
                break;
            case R.id.gradation_rl:
                break;
            case R.id.school_profession_text:
                break;
            case R.id.school_profession_context:
                break;
            case R.id.school_profession_select://学历-学校/专业
                Intent intent = new Intent(this, NewNetWorkClassActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
                break;
            case R.id.school_profession_rl:
                break;
            case R.id.education_ll:
                break;
            case R.id.project_text:
                break;
            case R.id.project_context:
                break;
            case R.id.project_select://项目类型
                initDataXiangMu();
                break;
            case R.id.project_rl:
                break;
            case R.id.assist_apply_area_text:
                break;
            case R.id.assist_apply_area_context:
                break;
            case R.id.assist_apply_area_select://报考区域
                //三级联动
                if (provinces.size() > 0) {
                    showAddressDialog();
                } else {
                    new InitAreaTask(NewSelectTypeActivity.this).execute(0);
                }
                break;
            case R.id.assist_apply_area_rl:
                break;
            case R.id.assist_apply_ll:
                break;
            case R.id.certificate_text:
                break;
            case R.id.certificate_context:
                break;
            case R.id.certificate_select://证书名称
                getZhengShuData();
                break;
            case R.id.certificate_rl:
                break;
            case R.id.else_ll:
                break;
            case R.id.ll:
                break;
            case R.id.name_id:
                break;
            case R.id.name_ed:
                break;
            case R.id.name_rl:
                break;
            case R.id.phone_text:
                break;
            case R.id.phone_ed:
                break;
            case R.id.phone_rl:
                break;
            case R.id.site_text:
                break;
            case R.id.site_ed:
                break;
            case R.id.site_rl:
                break;
            case R.id.game_play://完成按钮
                String nameEdstr = nameEd.getText().toString();//名字
                String phoneEdstr = phoneEd.getText().toString();//身份证号
                String siteEdstr = siteEd.getText().toString();//户籍地址

                //获取学历选择的值
                String educationContextstr = educationContext.getText().toString();//学历类型
                String areaContextstr = areaContext.getText().toString();//成考区域
                String gradationContextstr = gradationContext.getText().toString();//学历层次
                String schoolProfessionContextstr = schoolProfessionContext.getText().toString();//学校/专业

                //获取协助报名选择的值
                String projectContextstr = projectContext.getText().toString();//项目类型
                String assistApplyAreaContextstr = assistApplyAreaContext.getText().toString();//报考区域

                //获取其他选择的值
                String certificateContextstr = certificateContext.getText().toString();//证书名称
                if (TextUtils.isEmpty(nameEdstr)) {
                    ToastUtils.showShort("请输入姓名");
                    break;
                } else if (TextUtils.isEmpty(phoneEdstr)) {
                    ToastUtils.showShort("请输入身份证号");
                    break;
                } else if (TextUtils.isEmpty(siteEdstr)) {
                    ToastUtils.showShort("请输入户籍地址");
                    break;
                }
                if (type == 1) {
                    if (TextUtils.isEmpty(educationContextstr)) {
                        ToastUtils.showShort("请选择学历类型");
                        break;
                    }
                    if (TextUtils.isEmpty(schoolProfessionContextstr)) {
                        ToastUtils.showShort("请选择学校/专业");
                        break;
                    }
                    if (NewSelectTypeActivity.xueList.size() > 0) {
                        Map<String, String> stringStringMap = NewSelectTypeActivity.xueList.get(0);
                        stringStringMap.put("stu_name", nameEdstr);
                        stringStringMap.put("id_card", phoneEdstr);
                        stringStringMap.put("address", siteEdstr);
                        if (educationContextstr.equals("网教自考")){
                            stringStringMap.put("Sstatus", "1");
                        }else if (educationContextstr.equals("网教直取")){
                            stringStringMap.put("Sstatus", "2");
                        }else {
                            stringStringMap.put("Sstatus", "3");
                        }
                        stringStringMap.put("educationType", educationContextstr);
                        stringStringMap.put("school", schoolProfessionContextstr);

                        List<Map<String, String>> xueli = NewAddOrderActivity.xueList;
                        int size = xueli.size();
                        int haveSelect = 0;
                        if (size > 0) {
                            for (int i = 0; i < xueli.size(); i++) {
                                String major = xueli.get(i).get("major");

                                com.alibaba.fastjson.JSONObject major_xueli = com.alibaba.fastjson.JSONObject.parseObject(major);
                                try {
                                    int id = major_xueli.getIntValue("id");
                                    String major1 = stringStringMap.get("major");
                                    com.alibaba.fastjson.JSONObject major_xue = com.alibaba.fastjson.JSONObject.parseObject(major1);
                                    int selectid = major_xue.getIntValue("id");
                                    String id_card = stringStringMap.get("id_card");
                                    String id_card1 = xueli.get(i).get("id_card");
                                    if (id == selectid && id_card.equals(id_card1)) {
                                        haveSelect = id;
                                    }
                                } catch (com.alibaba.fastjson.JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (haveSelect == 0) {
                            NewAddOrderActivity.xueList.add(stringStringMap);
                            NewSelectTypeActivity.xueList.clear();
                        }
                    }
                } else if (type == 2) {
                    if (TextUtils.isEmpty(assistApplyAreaContextstr)) {
                        ToastUtils.showShort("请选择报考区域");
                        break;
                    } else if (TextUtils.isEmpty(projectContextstr)) {
                        ToastUtils.showShort("请选择项目类型");
                        break;
                    }
                    int xieSelectNum = 0;
                    if (xieList.size() > 0) {
                        HelpSignBean helpSignBean = xieList.get(0);
                        helpSignBean.setStu_name(nameEdstr);
                        helpSignBean.setAddress(siteEdstr);
                        helpSignBean.setIdCard(phoneEdstr);
                        helpSignBean.setRegistrationArea(assistApplyAreaContextstr);

                        List<HelpSignBean> xieSelect = NewAddOrderActivity.xieList;
                        if (xieSelect.size() > 0) {
                            for (int i = 0; i < xieSelect.size(); i++) {
                                int helpId = xieSelect.get(i).getHelpId();
                                String idCard1 = xieSelect.get(i).getIdCard();
                                String idCard = NewSelectTypeActivity.xieList.get(0).getIdCard();
                                int helpId2 = NewSelectTypeActivity.xieList.get(0).getHelpId();

                                if (!TextUtils.isEmpty(idCard)) {
                                    if (helpId2 == helpId && idCard.equals(idCard1)) {
                                        xieSelectNum = helpId;
                                    }
                                }

                            }
                        }
                        if (xieSelectNum == 0) {
                            NewAddOrderActivity.xieList.add(helpSignBean);
                            NewSelectTypeActivity.xieList.clear();
                        }
                    }

                } else if (type == 3) {//其他
                    int otherSelect = 0;
                    if (TextUtils.isEmpty(certificateContextstr)) {
                        ToastUtils.showShort("请选择证书名称");
                        break;
                    }
                    if (otherList.size() > 0) {
                        List<CertificateBean> certificateBeanlist = NewAddOrderActivity.otherList;
                        CertificateBean certificateBean = otherList.get(0);
                        certificateBean.setStu_name(nameEdstr);
                        certificateBean.setAddress(siteEdstr);
                        certificateBean.setIdCard(phoneEdstr);
                        if (certificateBeanlist.size() > 0) {
                            for (int i = 0; i < certificateBeanlist.size(); i++) {
                                int certId = certificateBeanlist.get(i).getCertId();
                                String idCard1 = certificateBeanlist.get(i).getIdCard();
                                int certId1 = certificateBean.getCertId();
                                String idCard = certificateBean.getIdCard();

                                if (certId1 == certId && idCard.equals(idCard1)) {
                                    otherSelect = certId;
                                }
                            }
                        }
                        if (otherSelect == 0) {
                            NewAddOrderActivity.otherList.add(certificateBean);
                            NewSelectTypeActivity.otherList.clear();
                        }
                    }

                }
                schoolProfessionContext.setText("");
                educationContext.setText("");

                break;
            case R.id.lls:
                break;
        }
    }

    //选择的pop弹窗
    private void getPopupWindow(List<String> stringList, int type) {
        //子布局
        View viewSon = View.inflate(this, R.layout.new_select_type_pop, null);
        final PopupWindow popupWindow = new PopupWindow(viewSon, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setTouchable(true);//设置窗口触摸
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);//设置窗口外部可触摸   要结合setBackgroundDrawable来使用
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //获取手机屏幕的高度
        DisplayMetrics metric = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 设置弹出窗体的宽和高
        popupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        // this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setAnimationStyle(R.style.pop_shop_anim);//设置显示时长在style中
        //  popupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);//相对于父窗体底部
        popupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        TextView hand_text = (TextView) viewSon.findViewById(R.id.hand_text);//标题
        RecyclerView rlv = (RecyclerView) viewSon.findViewById(R.id.rlv);//RecyclerView
        TextView cancel = (TextView) viewSon.findViewById(R.id.cancel);//取消
        TextView wancheng = (TextView) viewSon.findViewById(R.id.wancheng);//完成
        if (type == 1) {
            hand_text.setText("学历类型");
        } else if (type == 2) {
            hand_text.setText("项目类型");
        } else if (type == 3) {
            hand_text.setText("证书名称");

        }
        //pop条目适配器
        NewISelectAdapter newISelectAdapter = new NewISelectAdapter(this, stringList);
        rlv.setAdapter(newISelectAdapter);
        newISelectAdapter.setOnItemClickListener(new NewISelectAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int pos) {
                String name = stringList.get(pos);
                if (type == 1) {
                    educationContext.setText(name);
                } else if (type == 2) {//helpSignBeans
                    projectContext.setText(name);
                    xieList.add(helpSignBeans.get(pos));
                } else if (type == 3) {//CertificateBean
                    certificateContext.setText(name);
                    otherList.add(certificateBeanList.get(pos));
                }
                newISelectAdapter.refreshItem(pos);

            }
        });
        //RecyclerView布局显示方式
        rlv.setLayoutManager(new LinearLayoutManager(this));
        rlv.setAdapter(newISelectAdapter);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {//关闭弹窗
            @Override
            public void onClick(View v) {
                backgroundAlpha(1f);
                popupWindow.dismiss();
            }
        });
        wancheng.setOnClickListener(new View.OnClickListener() {//完成按钮
            @Override
            public void onClick(View view) {
                backgroundAlpha(1f);
                popupWindow.dismiss();
            }
        });
        if (popupWindow.isShowing()) {
            backgroundAlpha(0.8f);
        }

    }

    private void initDataXiangMu() {
        try {
            String url = null;
            //url = Constants.host+"team/p_list";
            url = Constants.payhost;
           /* page: 1
            pid: 1001002
            pnum: 200
            state: 2
            typ: 1*/
            HashMap<String, String> keyMap = new HashMap<>();
            keyMap.put("page", "1");
            keyMap.put("pnum", "200");

            //http://tserver.api.yuxuejiaoyu.net/periphery/kb_proj/pro_list
            //http://tserver.api.yuxuejiaoyu.net/periphery/kb_proj/pro_list
            Log.e("tag", "initDataWangke: " + url + "periphery/kb_proj/sch_list");
            OkHttpUtils.newGet(this, url + "dict/helpsign", new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(NewSelectTypeActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(org.json.JSONObject jsonObject) throws
                        Exception {
                    org.json.JSONObject data = null;
                    try {
                        data = jsonObject.getJSONObject("data");
                        JSONArray list = data.getJSONArray("list");
                        if (list != null) {
                            helpSignBeans = JSONUtil.parserArrayList(list, HelpSignBean.class, 0);

                            List<String> list1 = new ArrayList<>();
                            for (int i = 0; i < helpSignBeans.size(); i++) {
                                list1.add(helpSignBeans.get(i).getPName());
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getPopupWindow(list1, 2);
                                }
                            });

                        }

                    } catch (org.json.JSONException e) {
                        e.printStackTrace();
                    }

                    return null;
                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void getZhengShuData() {//证书
        try {
            String url = null;
            //url = Constants.host+"team/p_list";
            url = Constants.payhost;
           /* page: 1
            pid: 1001002
            pnum: 200
            state: 2
            typ: 1*/
            HashMap<String, String> keyMap = new HashMap<>();
            keyMap.put("page", "1");
            keyMap.put("pnum", "200");

            //http://tserver.api.yuxuejiaoyu.net/periphery/kb_proj/pro_list
            //http://tserver.api.yuxuejiaoyu.net/periphery/kb_proj/pro_list
            Log.e("tag", "initDataWangke: " + url + "periphery/kb_proj/sch_list");
            OkHttpUtils.newGet(this, url + "dict/cert", new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(NewSelectTypeActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(org.json.JSONObject jsonObject) throws
                        Exception {
                    org.json.JSONObject data = null;
                    try {
                        data = jsonObject.getJSONObject("data");
                        JSONArray list = data.getJSONArray("list");
                        if (list != null) {
                            certificateBeanList = JSONUtil.parserArrayList(list, CertificateBean.class, 0);
                            List<String> list1 = new ArrayList<>();
                            for (int i = 0; i < certificateBeanList.size(); i++) {
                                list1.add(certificateBeanList.get(i).getName());
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getPopupWindow(list1, 3);
                                }
                            });

                        }

                    } catch (org.json.JSONException e) {
                        e.printStackTrace();
                    }

                    return null;
                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public void setTexts(String name, int type) {
        this.degree_type = type;
        educationContext.setText(name);
    }

    public void backgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = alpha; //设置透明度
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        this.getWindow().setAttributes(lp);
    }

}
