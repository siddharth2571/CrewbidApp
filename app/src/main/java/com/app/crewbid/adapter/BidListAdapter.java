package com.app.crewbid.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.crewbid.MainFragmentActivity;
import com.app.crewbid.R;
import com.app.crewbid.interfaces.KeyInterface;
import com.app.crewbid.model.ClsBidOfEvent;
import com.app.crewbid.network.AsyncTaskCompleteListener;
import com.app.crewbid.network.ClsNetworkResponse;
import com.app.crewbid.network.NetworkParam;
import com.app.crewbid.network.NetworkTask;
import com.app.crewbid.utility.ProgressDialogUtility;
import com.app.crewbid.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BidListAdapter extends BaseAdapter implements KeyInterface {
    private ArrayList<ClsBidOfEvent> productLists = new ArrayList<ClsBidOfEvent>();
    private Context contex;
    private Fragment fragment;
    private LayoutInflater inflater = null;
    private static final String TAG = BidListAdapter.class.getSimpleName();

    //web service
    private int CALL_ADD_TO_CART = 102;
    private int CALL_ADD_TO_FAVOURITE = 103;
    private int CALL_PRODUCT_LIKE = 104;
    private int position = -1;

    private static final int REQ_AWARD = 3000;


    public BidListAdapter(Context ctx, Fragment fragment,
                          ArrayList<ClsBidOfEvent> productLists) {
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
            convertView = inflater.inflate(R.layout.item_product_list, null);
            holder = new ViewHolder();

            holder.txtPhone = (TextView) convertView.findViewById(R.id.txtPhone);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtBidId = (TextView) convertView.findViewById(R.id.txtDaysLeft);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            holder.txtBids = (TextView) convertView.findViewById(R.id.txtBids);
            holder.txtTrackers = (TextView) convertView.findViewById(R.id.txtTrackers);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
            holder.imgThumb = (CircleImageView) convertView.findViewById(R.id.imgThumb);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtPhone.setVisibility(View.VISIBLE);

        ClsBidOfEvent clsProductList = productLists.get(position);

        holder.txtName.setText(Utility.isNotNull(clsProductList
                .getFirst_name() + "  " + clsProductList.getLast_name()) ? clsProductList
                .getFirst_name() + "  " + clsProductList.getLast_name() : "");
        holder.txtBidId.setText("Bid #" + (Utility.isNotNull(clsProductList
                .getBidId()) ? clsProductList.getBidId() : ""));
        holder.txtDescription.setText(Utility.isNotNull(clsProductList
                .getProposal()) ? clsProductList
                .getProposal() : "");
        holder.txtPrice.setText("- Total " + (Utility.isNotNull(clsProductList
                .getBidamount()) ? clsProductList.getBidamount() : "0"));
//        holder.txtBids.setText(Utility.isNotNull(clsProductList.getProductBidCount()) ? clsProductList.getProductBidCount() + " Bids" : "0" + " Bids");
//        holder.txtTrackers.setText(Utility.isNotNull(clsProductList
//                .getProductTrackingCount()) ? clsProductList.getProductTrackingCount() + " Trackers" : "0" + " Trackers");
        holder.txtStatus.setText("awarded");

        holder.txtPhone.setText("Phone: " + clsProductList.getPhone());

        holder.txtStatus.setTag(position);

        holder.txtPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCall("09033515195");
            }
        });

        holder.txtStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (Integer) v.getTag();
//                Toast.makeText(contex, "Works", Toast.LENGTH_SHORT).show();

                Log.e("txtStatus", "txtStatus" + productLists.get(pos).getBidId());
                placeAwardES(productLists.get(pos).getBidId());

            }
        });

//        Picasso.with(contex).load(Utility.STATIC_IMAGE_URL + clsProductList.getImageurl())
//                .error(R.drawable.temp_profile).into(holder.imgThumb);


        /* --- working code
        if (Utility.getBidIsAwarded().equalsIgnoreCase("awarded")) {
//            clsProductList.get
            //holder.txtStatus.setText("award");
            if (clsProductList.getBidStatus().equalsIgnoreCase("awarded")) {
                holder.txtStatus.setText("awarded");
                holder.txtStatus.setEnabled(false);
            } else {
                holder.txtStatus.setText("");
            }

        } else {
            holder.txtStatus.setText("award");
            holder.txtStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer) v.getTag();
                    Toast.makeText(contex, "Works", Toast.LENGTH_SHORT).show();

                    Log.e("txtStatus", "txtStatus" + productLists.get(pos).getBidId());
                    placeAwardES(productLists.get(pos).getBidId());

                }
            });
        }*/


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

    public void setCall(String callString) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:+" + callString));
        contex.startActivity(callIntent);

        if (ActivityCompat.checkSelfPermission(contex, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

    }

    public class ViewHolder {
        TextView txtName = null, txtDescription = null, txtPrice = null,
                txtBidId = null, txtBids = null, txtTrackers = null, txtStatus = null, txtPhone = null;
        CircleImageView imgThumb = null;
    }

    public ArrayList<ClsBidOfEvent> getArrayList() {
        return this.productLists;
    }

    public void setArrayList(ArrayList<ClsBidOfEvent> productLists) {
        this.productLists = productLists;
    }

    public void placeAwardES(String bidid) {

//		isRefreshing = true;
        String userId = Utility.getUserIdSummery();
//		userId = "247";
        NetworkTask networkTask = new NetworkTask(contex);
        networkTask.setmCallback(asyncTaskCompleteListener);
        String pid = Utility.getProductIdSummery();
        Log.d("pid", "pid:- " + pid);
        networkTask.setHashMapParams(new NetworkParam().getAwardEventParam(userId, bidid, "awarded"));
        networkTask.execute(NetworkParam.METHOD_SET_AWARD_OF_EVENT,
                String.valueOf(REQ_AWARD));
        ProgressDialogUtility.show(((Activity) contex), null, false);
    }

    public void placeAwardTransferPayment(String amount) {

//		isRefreshing = true;
        String userId = Utility.getUserIdSummery();
//		userId = "247";
        NetworkTask networkTask = new NetworkTask(contex);
        networkTask.setmCallback(asyncTaskCompleteListener1);
        String pid = Utility.getProductIdSummery();
        Log.d("pid", "pid:- " + pid);
        networkTask.setHashMapParams(new NetworkParam().getPaymentTransfer(userId, amount));
        networkTask.execute(NetworkParam.METHOD_TRANSFER_PAYMENT,
                String.valueOf(REQ_AWARD));

        ProgressDialogUtility.show(((Activity) contex), null, false);

    }


    AsyncTaskCompleteListener asyncTaskCompleteListener = new AsyncTaskCompleteListener() {
        @Override
        public void onTaskComplete(ClsNetworkResponse clsResponse) {
//			setFootviewVisibility(false);
            // TODO Auto-generated method stub
            if (clsResponse.getRequestCode() == REQ_AWARD) {
                ProgressDialogUtility.dismiss();
                if (clsResponse.isSuccess()) {

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(
                                clsResponse.getResult_String());
                        boolean success = jsonObject.optInt(STATUS) == 1 ? true : false;

                        if (success) {
                            placeAwardTransferPayment(Utility.CrewFunded);
                            Log.i("result", "=>" + Utility.CrewFunded);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("AwardES placed", " AwardES placed");
                    MainFragmentActivity.toast((Activity) contex,
                            "Award placed");
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
            if (clsResponse.getRequestCode() == REQ_AWARD) {

                Log.d("ss", "doBackGround placeAwardES");
//                clsResponse = new ParserMyCrewEventsSummery(clsResponse).parse();
            }
            return clsResponse;
        }
    };

//-------------------- second Services --------------------------------

    AsyncTaskCompleteListener asyncTaskCompleteListener1 = new AsyncTaskCompleteListener() {
        @Override
        public void onTaskComplete(ClsNetworkResponse clsResponse) {
//			setFootviewVisibility(false);
            // TODO Auto-generated method stub
            if (clsResponse.getRequestCode() == REQ_AWARD) {
                ProgressDialogUtility.dismiss();
                if (clsResponse.isSuccess()) {

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(
                                clsResponse.getResult_String());
                        boolean success = jsonObject.optInt(STATUS) == 1 ? true : false;

                        if (success)
                            Toast.makeText(contex, "Transfer Success", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("AwardES placed", " AwardES placed");
                    MainFragmentActivity.toast((Activity) contex,
                            "Award placed");

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
            if (clsResponse.getRequestCode() == REQ_AWARD) {

                Log.d("ss", "doBackGround placeAwardES");
//                clsResponse = new ParserMyCrewEventsSummery(clsResponse).parse();
            }
            return clsResponse;
        }
    };


}
