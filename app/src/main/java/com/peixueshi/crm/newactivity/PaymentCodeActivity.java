package com.peixueshi.crm.newactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.mf.library.utils.ToastUtils;
import com.peixueshi.crm.R;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.utils.MatrixToImageWriterWithLogo;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

public class PaymentCodeActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.hand_text)
    TextView handText;
    @BindView(R.id.rl_title_view)
    RelativeLayout rlTitleView;
    @BindView(R.id.tv_pay_text)
    TextView tvPayText;
    @BindView(R.id.rb_zhifubao)
    RadioButton rbZhifubao;
    @BindView(R.id.rb_weixin)
    RadioButton rbWeixin;
    @BindView(R.id.rb_juhe)
    RadioButton rbJuhe;
    @BindView(R.id.rb_saobai)
    RadioButton rbSaobai;
    @BindView(R.id.rg_pay_select)
    RadioGroup rgPaySelect;
    @BindView(R.id.iv_show_pay)
    ImageView ivShowPay;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.tv_create)
    TextView tvCreate;
    @BindView(R.id.commit_pic)
    TextView commitPic;
    private byte[] byteArray;
    private int type;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCommonComponent.builder()
                .appComponent(appComponent)
                .commonModule(new CommonModule())
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_payment_code;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        Intent intent = getIntent();
        /*intent.putExtra("orderId", orderUserInfo.getOrderId());
        intent.putExtra("phone",orderUserInfo.getPhone());
        intent.putExtra("price",price);*/
        String phone = intent.getStringExtra("phone");
        int orderId = intent.getIntExtra("orderId", 0);
        double price = intent.getDoubleExtra("price", 0);
        reqPayByUrls("tool/get_code?order_id=", 2, orderId + "", phone, "￥" + price);
    }


    @OnClick({R.id.iv_back, R.id.commit_pic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.commit_pic:
                saveImage();
                break;
        }
    }

    private void saveImage() {
        if (ivShowPay!=null){
            ivShowPay.setDrawingCacheEnabled(true);
            //   viewById.buildDrawingCache();
            ivShowPay.buildDrawingCache(true);
            // 2. 将布局转成bitmap
            createPicture(ivShowPay);
        }
    }

    // 2. 将布局转成bitmap
    private void createPicture(View view) {
        if (view.getDrawingCache() != null) {
            view.setDrawingCacheEnabled(false);
            view.destroyDrawingCache();
            view.setDrawingCacheBackgroundColor(Color.WHITE);
            //将画布背景设为白色并生成bitmap
            Bitmap bitmap = loadBitmapFromView(view);
            saveImageToGallery(this,bitmap);
        }
    }

    private Bitmap loadBitmapFromView(View v) {//将生成的画布背景设为白色
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        //   c.drawColor(Color.WHITE);
        Log.e("tag", "loadBitmapFromView: "+w+"=="+h );
        /** 如果不设置canvas画布为白色，则生成透明 */
        // v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }

    View payView;

    //保存图片
    private void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "yuxue");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
        ToastUtils.showShort("保存成功");
    }
    private void reqPayByUrls(String uri_api, int pay_type, String orderId, String phone, String price) {
        try {
            String url = Constants.payhost + uri_api + orderId + "&pay_type=" + pay_type;
            Log.e("tag", "reqPayByUrls: " + url);
            OkHttpUtils.newGet(this, url, new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(PaymentCodeActivity.this, message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject object) throws
                        Exception {
                    String url = null;
                    if (object.has("data")) {
                        url = object.getString("data");
                        if (pay_type == 1) {//聚合
                            url = object.getString("data");
                            byteArray = Base64.decode(url.split(",")[1], Base64.DEFAULT);
                        }
                    } else if (object.has("qrpay")) {//扫呗
                        url = object.getString("qrpay");
                    }

                    //    rbZhifubao.setChecked(true);
                    rgPaySelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            //tool/get_code?order_id=203&pay_type=2

                               /* pay_type   1  聚合支付

                                2 支付宝支付  3：扫呗支付   4：微信支付*/
                            if (checkedId == rbZhifubao.getId()) {
                                type = 0;//支付宝
                                reqPayByUrls("tool/get_code?order_id=", 2, orderId, phone, price);
                            } else if (checkedId == rbWeixin.getId()) {
                                type = 1;//微信
                                reqPayByUrls("tool/get_code?order_id=", 4, orderId, phone, price);
                            } else if (checkedId == rbJuhe.getId()) {
                                type = 2;//聚合
                                reqPayByUrls("tool/get_code?order_id=", 1, orderId, phone, price);
                            } else {
                                type = 3;//扫呗
                                reqPayByUrls("tool/get_code?order_id=", 3, orderId, phone, price);
                            }
                        }
                    });
                    String finalUrl = url;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvPhoneNumber.setText("手机号:" + phone + "   应付金额:" + price);

                            if (byteArray != null) {
                                ivShowPay.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                                byteArray = null;
                            } else {
                                MatrixToImageWriterWithLogo.createQRImage(finalUrl, ivShowPay);
                            }

                        }
                    });
                    /*if (payView == null) {
                        payView = View.inflate(PaymentCodeActivity.this, R.layout.image_show_dialog, null);
                        iv_show_pay = payView.findViewById(R.id.iv_show_pay);
                        tv_phone = payView.findViewById(R.id.tv_phone_number);

                        rg_pay_select = payView.findViewById(R.id.rg_pay_select);
                        rb_zhifubao = payView.findViewById(R.id.rb_zhifubao);
                        RadioButton rb_weixin = payView.findViewById(R.id.rb_weixin);
                        RadioButton rb_juhe = payView.findViewById(R.id.rb_juhe);
                        RadioButton rb_saobai = payView.findViewById(R.id.rb_saobai);


                    }


                   */
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
}