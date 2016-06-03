package com.app.crewbid.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utility {

    public static String productIdSummery = null;
    public static String userIdSummery = null;
    public static String bidIsAwarded = null;
    public static String bids = null;
    public static String CrewFunded = null;
    public static String USER_EMAIL = null;
    public static String Budget = null;


    public static String STATIC_IMAGE_URL = "http://www.crewbid.com/uploads/attachments/auctions/";


    public static String getBidIsAwarded() {
        return bidIsAwarded;
    }

    public static void setBidIsAwarded(String bidIsAwarded) {
        Utility.bidIsAwarded = bidIsAwarded;
    }

    public static String getUserIdSummery() {
        return userIdSummery;
    }

    public static void setUserIdSummery(String userIdSummery) {
        Utility.userIdSummery = userIdSummery;
    }

    public static String getProductIdSummery() {
        return productIdSummery;
    }

    public static void setProductIdSummery(String productIdSummery) {
        Utility.productIdSummery = productIdSummery;
    }

    public static void log(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String getHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyhash = Base64.encodeToString(md.digest(),
                        Base64.DEFAULT);
                Log.d("KeyHash:", keyhash);
                return keyhash;
            }
        } catch (NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String filter(String input) {
        if (input == null || input.equalsIgnoreCase("null")) {
            return "";
        }
        return input;
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int width) {
        if (bmp == null)
            return null;

        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, bmp.getWidth(), bmp.getHeight()),
                new RectF(0, 0, width, width), Matrix.ScaleToFit.CENTER);
        bmp = Bitmap.createBitmap(bmp, 0, 0, width, width, m, true);

        // if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
        // bmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        // }

        Bitmap output = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);

        canvas.drawCircle(bmp.getWidth() / 2, bmp.getHeight() / 2,
                bmp.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bmp, rect, rect, paint);
        return output;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static double getDouble(String input) {
        if (TextUtils.isEmpty(input)) {
            return 0.0;
        }

        double d = 0.0;
        try {
            d = Double.valueOf(input);
        } catch (Exception e) {
        }
        return d;
    }

    public static int getInt(String input) {
        if (TextUtils.isEmpty(input)) {
            return -1;
        }

        int i = -1;
        try {
            i = Integer.valueOf(input);
        } catch (Exception e) {
        }
        return i;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public static String convertDateToFormat(String date, String inputFormat,
                                             String outputFormat, boolean convertLocalTimezone) {
        String dateOutput = null;
        Date objDate = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat(inputFormat,
                Locale.US);
        try {
            objDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (objDate == null)
            return "";

        if (convertLocalTimezone) {
            Calendar calendar = Calendar.getInstance();
            TimeZone zone = calendar.getTimeZone();
            int offset = zone.getRawOffset();
            objDate.setTime(objDate.getTime() + offset);
        }

        SimpleDateFormat format = new SimpleDateFormat(outputFormat, Locale.US);
        dateOutput = format.format(objDate);
        return dateOutput;
    }

    public static String convertMillisToDate(long millis, String inputFormat) {
        String date = "";
        Date objDate = new Date();
        objDate.setTime(millis);
        SimpleDateFormat dateFormat = new SimpleDateFormat(inputFormat,
                Locale.US);
        date = dateFormat.format(objDate);
        return date;
    }

    public static String getFileName(String path) {
        if (path == null) {
            return "";
        }
        if (path.lastIndexOf("/") != -1) {
            String filename = path.substring(path.lastIndexOf("/") + 1);
            return filename;
        }
        return "";
    }

    public static String shortFileName(String fileName) {
        if (fileName.length() > 19) {
            return fileName.substring(0, 10)
                    + "..."
                    + fileName.substring(fileName.length() - 6,
                    fileName.length());
        }
        return fileName;
    }

    public static String getExtension(String name) {
        String ext;
        if (name.lastIndexOf(".") == -1) {
            ext = "";
        } else {
            int index = name.lastIndexOf(".");
            ext = name.substring(index + 1, name.length());
        }
        return ext;
    }

    public static boolean isNotNull(Object object) {
        if (object != null
                && !(object.toString().trim().equals("") || object.toString()
                .trim().equalsIgnoreCase("null")))
            return true;
        else
            return false;
    }

    @SuppressLint("NewApi")
    public static final void setDrawable(Context context, View view, int resId) {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(context.getResources().getDrawable(resId));
        } else {
            view.setBackground(context.getResources().getDrawable(resId));
        }
    }

}
