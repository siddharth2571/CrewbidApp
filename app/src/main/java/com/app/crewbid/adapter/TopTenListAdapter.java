package com.app.crewbid.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.crewbid.AppDelegate;
import com.app.crewbid.R;
import com.app.crewbid.interfaces.KeyInterface;
import com.app.crewbid.model.ClsProductList;
import com.app.crewbid.model.ClsTopTen;
import com.app.crewbid.ui.ImageViewProgress;
import com.app.crewbid.utility.Utility;

public class TopTenListAdapter extends BaseAdapter implements KeyInterface
{
	private ArrayList<ClsTopTen> topTenLists = new ArrayList<ClsTopTen>();
	private Context contex;
	private LayoutInflater inflater = null;
	private static final String TAG = TopTenListAdapter.class.getSimpleName();
	
	public TopTenListAdapter(Context ctx,Fragment fragment,
			ArrayList<ClsTopTen> productLists) {
		this.contex = ctx;
		this.topTenLists = productLists;
		inflater = ((Activity) contex).getLayoutInflater();
	}

	@Override
	public int getCount() {
		return topTenLists.size();
	}

	@Override
	public Object getItem(int position) {
		return topTenLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_top_ten_list, null);
			holder = new ViewHolder();

			holder.relativeView = (RelativeLayout) convertView.findViewById(R.id.relativeView);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		
		if (position == 0) {
			Utility.setDrawable(contex, holder.relativeView,
					R.drawable.bg_top_ten_item_winner);
		} else {
			Utility.setDrawable(contex, holder.relativeView,
					R.drawable.bg_top_ten_item_card_base);
		}
		/*ClsTopTen clsTopTen = topTenLists.get(position);
		holder.txtName.setText(Utility.isNotNull(clsTopTen
				.getProductTitle()) ? clsTopTen.getProductTitle() : "");
		holder.txtDescription.setText(Utility.isNotNull(clsTopTen
				.getProductDescription()) ? clsTopTen
				.getProductDescription() : "");*/

		return convertView;
	}

	public class ViewHolder {
		public RelativeLayout relativeView;
		TextView txtName = null, txtDescription = null;
	}

	public ArrayList<ClsTopTen> getArrayList() {
		return this.topTenLists;
	}

	public void setArrayList(ArrayList<ClsTopTen> topTenLists) {
		this.topTenLists = topTenLists;
	}
	
}
