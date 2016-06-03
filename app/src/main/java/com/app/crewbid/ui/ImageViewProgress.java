package com.app.crewbid.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.app.crewbid.R;

public class ImageViewProgress extends RelativeLayout {

	Context mContext;
	View mImageProgressView;
	ImageView mImageView;
	ProgressBar mProgressbar;

	public ImageViewProgress(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init();
	}

	public ImageViewProgress(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		this.mContext = context;
		init();
	}

	public ImageViewProgress(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	public void init() {
		LayoutInflater inflator = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageProgressView = inflator.inflate(
				R.layout.layout_imageview_progress, null);
		this.addView(mImageProgressView);
		if (!isInEditMode()) {
			initializeReference();
		}
	}

	public void initializeReference() {
		mImageView = (ImageView) mImageProgressView
				.findViewById(R.id.imageviewprogress);
		mImageView.setVisibility(View.VISIBLE);
		mProgressbar = (ProgressBar) mImageProgressView
				.findViewById(R.id.progressBar);
		mProgressbar.setIndeterminate(true);
	}

	public void showImage(boolean value) {
		if (value) {
			mImageView.setVisibility(View.VISIBLE);
			mProgressbar.setVisibility(View.INVISIBLE);
		} else {
			mImageView.setVisibility(View.INVISIBLE);
			mProgressbar.setVisibility(View.VISIBLE);
		}
	}

	public void setImageResource(int rid) {
		mImageView.setImageResource(rid);
		mProgressbar.setVisibility(View.INVISIBLE);
	}

	public Drawable getDrawable() {
		return mImageView.getDrawable();
	}

	public void setImageBitmap(Bitmap bitmap) {
		mProgressbar.setVisibility(View.INVISIBLE);
		mImageView.setVisibility(View.VISIBLE);
		mImageView.setImageBitmap(bitmap);
	}

}
