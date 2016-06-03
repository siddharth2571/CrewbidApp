package com.app.crewbid.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.crewbid.R;
import com.app.crewbid.model.ClsKeyValue;

public class CustomSpinnerAdapter extends BaseAdapter {
	private Context activity;
	private ArrayList<ClsKeyValue> list;
	private int textSize = 14;
	private LayoutInflater inflater;

	public ArrayList<ClsKeyValue> getList() {
		return list;
	}

	public void setList(ArrayList<ClsKeyValue> list) {
		this.list = list;
	}

	public CustomSpinnerAdapter(Context activity, ArrayList<ClsKeyValue> list) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.list = list;
		textSize = activity.getResources().getDimensionPixelSize(
				R.dimen.textsize_edittext);
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// createSpinnerDialog();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_spinner_text, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String display = (String) ((ClsKeyValue) getItem(arg0)).getValue();
		holder.textView.setText(display);
		holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		holder.textView.setSingleLine();
		return convertView;
	}

	private class ViewHolder {
		TextView textView;

		public ViewHolder(View v) {
			// TODO Auto-generated constructor stub
			textView = (TextView) v.findViewById(R.id.text1);
		}
	}

}
