package com.rfl.trn.starr_cell.Helper;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Waktu {
    private Context context;

    public Waktu(Context context) {
        this.context = context;
    }

    public int getTanggalTimestamp(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public int getBulanTimestamp(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return cal.get(Calendar.MONTH);
    }

    public int getTahunTimestamp(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return cal.get(Calendar.YEAR);
    }

    public int getJam24Timestamp(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public int getJam12Timestamp(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return cal.get(Calendar.HOUR);
    }

    public String getJamLengkap(long timestamp) {
        Date date = new Date(timestamp);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }

    public String getWaktuLengkap(long timestamp) {
        Date date = new Date(timestamp);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy | HH:mm:ss");
        return getNamaHariIndonesia(timestamp) + ", " + sdf.format(date);
    }

    public String getWaktuLengkapIndonesia(long timestamp) {
        return getNamaHariIndonesia(timestamp) +
                ", " +
                getTanggalTimestamp(timestamp) +
                " " +
                getNamaBulanIndonesia(timestamp) +
                " " +
                getTahunTimestamp(timestamp) +
                " - " +
                getJamLengkap(timestamp) +
                " WIB";
    }

    public boolean isHariSama(long waktu1, long waktu2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(waktu1);
        cal2.setTimeInMillis(waktu2);
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    public String getNamaHariIndonesia(long timestamp) {
        String[] namaHari = {"Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"};
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return namaHari[cal.get(Calendar.DAY_OF_WEEK) - 1];
    }

    public String getNamaBulanIndonesia(long timestamp) {
        String[] namaBulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni",
                "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return namaBulan[cal.get(Calendar.MONTH)];
    }


}
