package com.app.crewbid.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextview extends TextView {
	public CustomTextview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public CustomTextview(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		// TODO Auto-generated constructor stub
	}

	public CustomTextview(Context context) {
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
