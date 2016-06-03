package com.app.crewbid.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.crewbid.AppDelegate;
import com.app.crewbid.CardDetailActivity;
import com.app.crewbid.MainFragmentActivity;
import com.app.crewbid.R;
import com.app.crewbid.fragment.EventFragment;
import com.app.crewbid.fragment.HomeFragment;
import com.app.crewbid.interfaces.KeyInterface;
import com.app.crewbid.model.ClsEventSummeryList;
import com.app.crewbid.network.AsyncTaskCompleteListener;
import com.app.crewbid.network.ClsNetworkResponse;
import com.app.crewbid.network.NetworkParam;
import com.app.crewbid.network.NetworkTask;
import com.app.crewbid.prefs.PreferenceData;
import com.app.crewbid.ui.ImageViewProgress;
import com.app.crewbid.utility.ProgressDialogUtility;
import com.app.crewbid.utility.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EventSummeryVenderListAdapter extends BaseAdapter implements KeyInterface {
    private ArrayList<ClsEventSummeryList> productLists = new ArrayList<ClsEventSummeryList>();
    public Context contex;
    private Fragment fragment;
    private LayoutInflater inflater = null;
    private static final String TAG = EventSummeryVenderListAdapter.class.getSimpleName();

    //web service
    private int CALL_ADD_TO_CART = 102;
    private int CALL_ADD_TO_FAVOURITE = 103;
    private int CALL_PRODUCT_LIKE = 104;
    private int position = -1;

    private static final int REQ_PLACE_BID = 3000;

    public EventSummeryVenderListAdapter(Context ctx, Fragment fragment,
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
            convertView = inflater.inflate(R.layout.frag_eventsummry_vender, null);
            holder = new ViewHolder();

            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtDays = (TextView) convertView.findViewById(R.id.txtDaysLeft);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            holder.txtBids = (TextView) convertView.findViewById(R.id.txtBids);
            holder.txtCityName = (TextView) convertView.findViewById(R.id.txtCityName);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
            holder.btnBid = (Button) convertView.findViewById(R.id.btnPlaceBid);
            holder.btnAddTrack = (Button) convertView.findViewById(R.id.btnAddTrackList);
            holder.imgThumb = (ImageViewProgress) convertView.findViewById(R.id.imgThumb);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final EditText editTextBid = (EditText) convertView.findViewById(R.id.editTxtMidRect);

        ClsEventSummeryList clsEventSummeryLis = productLists.get(position);

        clsEventSummeryLis.setBidDetails(EventFragment.bids);


        holder.txtName.setText(Utility.isNotNull(clsEventSummeryLis
                .getProjectTitle()) ? clsEventSummeryLis.getProjectTitle() : "");
        holder.txtDays.setText(Utility.isNotNull(clsEventSummeryLis
                .getDateStarts()) ? setDateDifferent(clsEventSummeryLis.getDateStarts()) + " Days Left" : "");
        holder.txtDescription.setText(Utility.isNotNull(clsEventSummeryLis
                .getProjectDescription()) ? clsEventSummeryLis
                .getProjectDescription() : "");
/* -----15-04
        holder.txtPrice.setText("Budget-$" + (Utility.isNotNull(clsEventSummeryLis
                .getFilterBudget()) ? clsEventSummeryLis.getFilterBudget() : "0"));
*/

        holder.txtPrice.setText("Budget-$" + Utility.CrewFunded);

        holder.txtBids.setText(Utility.isNotNull(clsEventSummeryLis
                .getBidDetails()) ? clsEventSummeryLis.getBidDetails() + " Bids" : "0" + " Bids");
        holder.txtCityName.setText(Utility.isNotNull(clsEventSummeryLis
                .getCityName()) ? clsEventSummeryLis.getCityName() + "" : "0" + " Trackers");
        holder.txtStatus.setText(clsEventSummeryLis.getCid() + "");

        Log.e("bidstatus", Utility.getBidIsAwarded() + "=" + clsEventSummeryLis.getBidstatus());

        if (Utility.getBidIsAwarded().equalsIgnoreCase("awarded")) {
            editTextBid.setEnabled(false);
            holder.btnBid.setEnabled(false);
            holder.btnBid.setVisibility(View.INVISIBLE);
        } else {
            if (clsEventSummeryLis.getBidstatus().equalsIgnoreCase("placed")) {
                Log.e("bidstatus", "=" + clsEventSummeryLis.getBidstatus());
                holder.btnBid.setText("EditBid");
            } else {
                Log.e("bidstatus", "=" + clsEventSummeryLis.getBidstatus());
                holder.btnBid.setText("PlaceBid");
            }
        }

        holder.btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = v.getId();
//                ((ClsEventSummeryList) productLists.get(position)).bidDetails = editTextBid
//                        .getText().toString();
                Log.d("vlue in edit text", "" + editTextBid.getText().toString());
//                Log.d("position", "" + position);
                Log.d("vlue in getCid", "" + productLists.get(0).getCid());
//                productLists.get(position).getCid();
                Log.d("vlue in getCid", "=>" + AppDelegate.getString(PreferenceData.HASACCOUNT));
                if (AppDelegate.getString(PreferenceData.HASACCOUNT).equals("0")) {
                    contex.startActivity(new Intent(contex, CardDetailActivity.class).putExtra("bidvalue", editTextBid.getText().toString()).putExtra("cid", productLists.get(0).getCid()));
                } else {
                    placeBidES(editTextBid.getText().toString(), productLists.get(0).getCid());
                }
            }
        });

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

    public void placeBidES(String value, String cid) {

//		isRefreshing = true;
//        String userId =Utility.getUserIdSummery();
        String userId = AppDelegate.getString(PreferenceData.USERID);
        // from logine
//		userId = "247";
        NetworkTask networkTask = new NetworkTask(contex);
        networkTask.setmCallback(asyncTaskCompleteListener);
        String pid = Utility.getProductIdSummery();
        Log.d("pid", "pid:- " + pid);
        networkTask.setHashMapParams(new NetworkParam().getAddBidtoEventParam(Integer.parseInt(pid), userId, cid, value, "1", "5", "proposal123"));
        networkTask.execute(NetworkParam.METHOD_ADD_BID_TO_EVENT,
                String.valueOf(REQ_PLACE_BID));

        ProgressDialogUtility.show(((Activity) contex), null, false);

    }

    AsyncTaskCompleteListener asyncTaskCompleteListener = new AsyncTaskCompleteListener() {
        @Override
        public void onTaskComplete(ClsNetworkResponse clsResponse) {
//			setFootviewVisibility(false);
            // TODO Auto-generated method stub
            if (clsResponse.getRequestCode() == REQ_PLACE_BID) {
                ProgressDialogUtility.dismiss();
                if (clsResponse.isSuccess()) {
                    Log.d("bid placed", "bid placed");
                    MainFragmentActivity.toast((Activity) contex,
                            "bid placed");
                    gotoHomeFragment();

                } else {
//					offsetNo = -1;
                    MainFragmentActivity.toast((Activity) contex,
                            clsResponse.getDispMessage());
                }
            }
        }

        @Override
        public ClsNetworkResponse doBackGround(ClsNetworkResponse clsResponse) {
            // TODO Auto-generated method stub
            if (!clsResponse.isSuccess()
                    || clsResponse.getResult_String() == null)
                return clsResponse;
            if (clsResponse.getRequestCode() == REQ_PLACE_BID) {

                Log.d("ss", "doBackGround ParserMyCrewEventsSummery");
//                clsResponse = new ParserMyCrewEventsSummery(clsResponse).parse();
            }
            return clsResponse;
        }
    };

    public class ViewHolder {
        TextView txtName = null, txtDescription = null, txtPrice = null,
                txtDays = null, txtBids = null, txtCityName = null, txtStatus = null;
        ImageViewProgress imgThumb = null;
        EditText editTextBid = null;
        Button btnBid = null, btnAddTrack = null;
    }

    public ArrayList<ClsEventSummeryList> getArrayList() {
        return this.productLists;
    }

    public void setArrayList(ArrayList<ClsEventSummeryList> productLists) {
        this.productLists = productLists;
    }

    private void gotoHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        ((MainFragmentActivity) contex).switchContent(
                homeFragment, false, FRAG_HOME);
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
