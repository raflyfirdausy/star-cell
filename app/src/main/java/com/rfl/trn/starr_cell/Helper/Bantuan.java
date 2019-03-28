package com.rfl.trn.starr_cell.Helper;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

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


}
