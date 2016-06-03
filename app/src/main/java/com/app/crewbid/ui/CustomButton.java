package com.app.crewbid.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton extends Button {
	public CustomButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public CustomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		// TODO Auto-generated constructor stub
	}

	public CustomButton(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}

	// TODO initializing controls
	public void init() {
		if (!isInEditMode()) {
			// Typeface mTf = Typeface.createFromAsset(getContext().getAssets(),
			// "MyriadPro-Bold.otf");
			// setTypeface(mTf);
		}
	}

}
