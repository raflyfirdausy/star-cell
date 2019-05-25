package com.rfl.trn.starr_cell.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.rfl.trn.starr_cell.R;

public class TambahKaryawanActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_karyawan);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }
}
