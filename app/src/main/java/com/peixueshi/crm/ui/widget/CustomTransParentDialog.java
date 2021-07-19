package com.peixueshi.crm.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class CustomTransParentDialog extends AlertDialog {
	private View view;
	public CustomTransParentDialog(Context context, int theme,View view) {
		super(context, theme);
		this.view = view;
	}

	public CustomTransParentDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(view);
	}
}
