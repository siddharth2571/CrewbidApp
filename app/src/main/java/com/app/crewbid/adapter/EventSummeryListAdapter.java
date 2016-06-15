package com.app.crewbid.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.crewbid.R;
import com.app.crewbid.interfaces.KeyInterface;
import com.app.crewbid.model.ClsEventSummeryList;
import com.app.crewbid.utility.Utility;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventSummeryListAdapter extends BaseAdapter implements KeyInterface {
    private ArrayList<ClsEventSummeryList> productLists = new ArrayList<ClsEventSummeryList>();
    private Context contex;
    private Fragment fragment;
    private LayoutInflater inflater = null;
    private static final String TAG = EventSummeryListAdapter.class.getSimpleName();

    //web service
    private int CALL_ADD_TO_CART = 102;
    private int CALL_ADD_TO_FAVOURITE = 103;
    private int CALL_PRODUCT_LIKE = 104;
    private int position = -1;

    public static String imageUrl;


    public EventSummeryListAdapter(Context ctx, Fragment fragment,
                                   ArrayList<ClsEventSummeryList> productLists) {
        this.contex = ctx;
        this.fragment = fragment;
        this.productLists = productLists;
        inflater = ((Activity) contex).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return productLists.size();
    }

    @Override
    public Object getItem(int position) {
        return productLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.frag_eventsummry, null);
            holder = new ViewHolder();

            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtDays = (TextView) convertView.findViewById(R.id.txtDaysLeft);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            holder.txtBids = (TextView) convertView.findViewById(R.id.txtBids);
            holder.txtCityName = (TextView) convertView.findViewById(R.id.txtCityName);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
            holder.txtFunds = (TextView) convertView.findViewById(R.id.txtAddFundsValue);
            holder.imgThumb = (CircleImageView) convertView.findViewById(R.id.imgThumb);
            holder.awarded_done = (TextView) convertView.findViewById(R.id.awarded_done);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ClsEventSummeryList clsEventSummeryLis = productLists.get(position);

        clsEventSummeryLis.setBidDetails(Utility.bids);

//        Log.i("thumb", EventSummeryListAdapter.imageUrl);

        Picasso.with(contex).load(EventSummeryListAdapter.imageUrl).error(contex.getResources().getDrawable(R.drawable.temp_profile))
                .into(holder.imgThumb);

        holder.txtFunds.setText(Utility.isNotNull(clsEventSummeryLis
                .getFilterBudget()) ? clsEventSummeryLis.getFilterBudget() : "");


        // holder.txtFunds.setText(Utility.isNotNull(Utility.Budget) ? Utility.Budget : "");

        holder.txtName.setText(Utility.isNotNull(clsEventSummeryLis
                .getProjectTitle()) ? clsEventSummeryLis.getProjectTitle() : "");
        holder.txtDays.setText(Utility.isNotNull(clsEventSummeryLis
                .getDateStarts()) ? (setDateDifferent(clsEventSummeryLis.getDateStarts()) + " Days Left") : "");

        holder.txtDescription.setText(Utility.isNotNull(clsEventSummeryLis
                .getProjectDescription()) ? clsEventSummeryLis
                .getProjectDescription() : "");

        holder.txtPrice.setText("Budget-$" + (Utility.isNotNull(Utility.Budget) ? Utility.Budget : "0"));

        holder.txtBids.setText(Utility.isNotNull(Utility.bids) ? Utility.bids + " Bids" : "0" + " Bids");
        holder.txtCityName.setText(Utility.isNotNull(clsEventSummeryLis
                .getCityName()) ? "" + clsEventSummeryLis.getCityName() : "0" + " Trackers");

//        Toast.makeText(contex, Utility.getBidIsAwarded() + "", Toast.LENGTH_SHORT).show();

        if (TextUtils.isEmpty(Utility.getBidIsAwarded())) {
            holder.awarded_done.setVisibility(View.GONE);
        } else {
            if (Utility.getBidIsAwarded().equalsIgnoreCase("awarded")) {
                holder.awarded_done.setVisibility(View.VISIBLE);
            }
        }

        holder.txtStatus.setText(clsEventSummeryLis.getCid() + "");

//		holder.txtStatus.setText("Join");
//		ImageView imageViewprogress  =  (ImageView) holder.imgThumb.findViewById(R.id.imageviewprogress);
//		imageViewprogress.setScaleType(ScaleType.CENTER_CROP);
//		
//		if (AppDelegate.imageLoaderRecycler == null) {
//			AppDelegate.imageLoaderRecycler = new ImageLoaderRecycler(contex);
//		}
//		
//		int pixelSize = contex.getResources().getDimensionPixelSize(R.dimen.thumb_image_width);
//		Utility.log(TAG,"image Size in Pixl : "+pixelSize);
//		AppDelegate.imageLoaderRecycler.displayImageFromUrl(clsProductList.getProductThumb(),
//				holder.imgThumb,R.drawable.ic_default, false, pixelSize);

        return convertView;
    }

    public class ViewHolder {
        TextView txtName = null, txtDescription = null, txtPrice = null, awarded_done = null,
                txtDays = null, txtBids = null, txtCityName = null, txtStatus = null, txtFunds;
        CircleImageView imgThumb = null;
    }

    public ArrayList<ClsEventSummeryList> getArrayList() {
        return this.productLists;
    }

    public void setArrayList(ArrayList<ClsEventSummeryList> productLists) {
        this.productLists = productLists;
    }

    public String setDateDifferent(String date1) {

        String remaingdays = "0";
        SimpleDateFormat serverformat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

//        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        java.util.Date d1 = null;
        java.util.Date d2 = null;

        Calendar c = Calendar.getInstance();
        String date2 = serverformat.format(c.getTime());

        try {
            d1 = serverformat.parse(date1);
            d2 = serverformat.parse(date2);
            //in milliseconds
            long diff = d2.getTime() - d1.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            System.out.print(diffDays + " days, ");
            remaingdays = diffDays + "";
            System.out.print(diffHours + " hours, ");
            System.out.print(diffMinutes + " minutes, ");
            System.out.print(diffSeconds + " seconds.");

        } catch (Exception e) {
            remaingdays = "0";
            e.printStackTrace();
        }

        if (Integer.parseInt(remaingdays.trim()) > 0) {
            return remaingdays;
        } else {
            return "0";
        }

    }


}
