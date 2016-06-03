package com.app.crewbid.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.app.crewbid.R;

public class ProgressDialogUtility {

	private static ProgressDialog progressDialog = null;
	private static AlertDialog alertDialog = null;

	public synchronized static void show(Activity activity, String msg,
			boolean cancellable) {
		if (progressDialog != null && progressDialog.isShowing())
			return;
		progressDialog = new ProgressDialog(activity);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(msg == null ? "Please wait..." : msg);
		progressDialog.setCancelable(cancellable);
		progressDialog.show();
	}

	public synchronized static void dismiss() {
		if (progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
		progressDialog = null;
	}

	public synchronized static void showAlert(Activity activity,
			String message, boolean cancellable) {
		if (alertDialog != null && alertDialog.isShowing()) {
			try {
				alertDialog.dismiss();
				alertDialog = null;
			} catch (Exception e) {
			}
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(activity)
				.setCancelable(cancellable)
				.setTitle(activity.getResources().getString(R.string.app_name))
				.setMessage(message)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						alertDialog.dismiss();
						alertDialog = null;
					}
				});
		alertDialog = builder.create();
		alertDialog.show();
	}

}
