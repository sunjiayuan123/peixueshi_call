package com.peixueshi.crm.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.peixueshi.crm.R;


public class LoadTransDialog extends AlertDialog {

    public LoadTransDialog(Context context, int theme) {
		super(context, theme);
	}
	public LoadTransDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tranparent_dialog);
        ImageView iv_loading = (ImageView) findViewById(R.id.progressbar_loading);
		if (iv_loading != null) {
			iv_loading.setImageResource(R.drawable.loading_image);
            AnimationDrawable animationDrawable = (AnimationDrawable) iv_loading.getDrawable();
			animationDrawable.start();
		}
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }
}
