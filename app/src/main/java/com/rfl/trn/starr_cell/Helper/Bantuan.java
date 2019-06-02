package com.rfl.trn.starr_cell.Helper;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.google.firebase.FirebaseOptions;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.rfl.trn.starr_cell.R;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class Bantuan {

    private Context context;

    public Bantuan(Context context) {
        this.context = context;
    }

    public void toastShort(String pesan) {
        Toast.makeText(context, pesan, Toast.LENGTH_SHORT).show();
    }

    public void toastLong(String pesan) {
        Toast.makeText(context, pesan, Toast.LENGTH_LONG).show();
    }

    public void alertDialogDebugging(String pesan) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Info Debugging")
                .setMessage(pesan)
                .setPositiveButton(android.R.string.yes, null)
                .setCancelable(false)
                .show();
    }

    public void alertDialogPeringatan(String pesan) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Peringatan")
                .setMessage(pesan)
                .setPositiveButton(android.R.string.yes, null)
                .setCancelable(false)
                .show();
    }

    public void alertDialogInformasi(String pesan) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Informasi")
                .setMessage(pesan)
                .setPositiveButton(android.R.string.yes, null)
                .setCancelable(false)
                .show();
    }

    public void swal_basic(String pesan) {
        new SweetAlertDialog(context)
                .setTitleText(pesan)
                .show();
    }

    public void swal_title(String title, String subtitle) {
        new SweetAlertDialog(context)
                .setTitleText(title)
                .setContentText(subtitle)
                .show();
    }

    public void swal_error(String pesan) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(pesan)
                .show();
    }

    public void swal_warning(String pesan) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Peringatan")
                .setContentText(pesan)
                .show();
    }

    public void swal_sukses(String pesan) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Peringatan")
                .setContentText(pesan)
                .show();
    }

    public SweetAlertDialog swal_loading(String pesan) {
        SweetAlertDialog SAD =  new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        SAD.getProgressHelper().setBarColor(Color.parseColor("#2980b9"));
        SAD.setTitleText("Loading...");
        SAD.setContentText(pesan);
        SAD.setCancelable(false);

        return SAD;
    }

    public FirebaseOptions getFirebaseOptions(){
        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl(context.getString(R.string.databaseUrl))
                .setApiKey(context.getString(R.string.apiKey))
                .setApplicationId(context.getString(R.string.applicationId))
                .build();
        return firebaseOptions;
    }

    public  String getDatePretty(long timestamp, boolean showTimeOfDay) {
        DateTime yesterdayDT = new DateTime(
                DateTime
                        .now()
                        .getMillis() - 1000 * 60 * 60 * 24);

        yesterdayDT = yesterdayDT
                .withTime(0, 0, 0, 0);

        Interval today = new Interval(
                DateTime
                        .now()
                        .withTimeAtStartOfDay(), Days.ONE);

        Interval yesterday = new Interval(yesterdayDT, Days.ONE);

        DateTimeFormatter timeFormatter = DateTimeFormat.shortTime();
        DateTimeFormatter dateFormatter = DateTimeFormat.shortDate();

        if (today.contains(timestamp)) {

            if (showTimeOfDay) {
                return timeFormatter.print(timestamp);
            } else {
                return "Hari ini" + " " + timeFormatter.print(timestamp);
            }

        } else if (yesterday.contains(timestamp)) {

            return "Kemarin" + " " + timeFormatter.print(timestamp);

        } else {

            return dateFormatter.print(timestamp);

        }
    }

    public  long getDayTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

    public static Bitmap getRoundbmp(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


}
