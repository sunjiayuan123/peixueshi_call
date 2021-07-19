package com.peixueshi.crm.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class CustomDialog extends AlertDialog {
	private View view;
	public CustomDialog(Context context, int theme,View view) {
		super(context, theme);
		CustomDialog.this.view = view;
	}

	public CustomDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(view);
	}
	
}
