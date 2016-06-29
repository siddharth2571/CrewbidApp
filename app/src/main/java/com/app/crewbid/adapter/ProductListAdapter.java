package com.app.crewbid.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.crewbid.R;
import com.app.crewbid.interfaces.KeyInterface;
import com.app.crewbid.model.ClsProductList;
import com.app.crewbid.ui.ImageViewProgress;
import com.app.crewbid.utility.Utility;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductListAdapter extends BaseAdapter implements KeyInterface {
    private ArrayList<ClsProductList> productLists = new ArrayList<ClsProductList>();
    private Context contex;
    private Fragment fragment;
    private LayoutInflater inflater = null;
    private static final String TAG = ProductListAdapter.class.getSimpleName();

    //web service
    private int CALL_ADD_TO_CART = 102;
    private int CALL_ADD_TO_FAVOURITE = 103;
    private int CALL_PRODUCT_LIKE = 104;
    private int position = -1;


    public ProductListAdapter(Context ctx, Fragment fragment,
                              ArrayList<ClsProductList> productLists) {
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

        ClsProductList clsProductList = productLists.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_product_list, null);
            holder = new ViewHolder();

            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtDays = (TextView) convertView.findViewById(R.id.txtDaysLeft);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            holder.txtBids = (TextView) convertView.findViewById(R.id.txtBids);
            holder.txtTrackers = (TextView) convertView.findViewById(R.id.txtTrackers);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
            holder.txtStatusAward = (TextView) convertView.findViewById(R.id.txtStatusAward);
            holder.thumbImage = (CircleImageView) convertView.findViewById(R.id.imgThumb);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(contex).load(Utility.STATIC_IMAGE_URL + clsProductList.getProductThumb()).error(contex.getResources().getDrawable(R.drawable.temp_profile)).into(holder.thumbImage);

        Log.i("thumbimg", Utility.STATIC_IMAGE_URL + clsProductList.getProductThumb());

        holder.txtName.setText(Utility.isNotNull(clsProductList
                .getProductTitle()) ? clsProductList.getProductTitle() : "");

        // counting days left from function
//        String remaingDays = setDateDifferent(Utility.isNotNull(clsProductList
//                .getProductDateTime()) ? clsProductList.getProductDateTime() : "");

        String remaingDays = clsProductList.getDaysLeft();

        holder.txtDays.setText(remaingDays + " Days Left");

        holder.txtDescription.setText(Utility.isNotNull(clsProductList
                .getProductDescription()) ? clsProductList
                .getProductDescription() : "");

//        -----12/04 changes
//        holder.txtPrice.setText("Budget-$" + (Utility.isNotNull(clsProductList
//                .getProductPrice()) ? clsProductList.getProductPrice() : "0"));

        holder.txtPrice.setText("Budget-$" + (Utility.isNotNull(clsProductList
                .getCrewsFunded()) ? clsProductList.getCrewsFunded() : "0"));

        holder.txtBids.setText(Utility.isNotNull(clsProductList
                .getBids()) ? clsProductList.getBids() + " Bids" : "0" + " Bids");

        holder.txtTrackers.setText(Utility.isNotNull(clsProductList
                .getProductTrackingCount()) ? clsProductList.getProductTrackingCount() + " Trackers" : "0" + " Trackers");
        holder.txtStatus.setText("Join");

//        ----12-04-2016
//        holder.txtStatusAward.setText(
//                clsProductList.getStatusIsAward().equalsIgnoreCase("awarded") ? clsProductList.getStatusIsAward() : "");

        holder.txtStatusAward.setText(
                clsProductList.getStatusIsAward().equalsIgnoreCase("awarded") ? "Awarded" : "");


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
        TextView txtName = null, txtDescription = null, txtStatusAward = null, txtPrice = null,
                txtDays = null, txtBids = null, txtTrackers = null, txtStatus = null;
        ImageViewProgress imgThumb = null;
        CircleImageView thumbImage;
    }

    public ArrayList<ClsProductList> getArrayList() {
        return this.productLists;
    }

    public void setArrayList(ArrayList<ClsProductList> productLists) {
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
