package com.rfl.trn.starr_cell.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.rfl.trn.starr_cell.R;

import butterknife.BindView;
import butterknife.OnClick;

public class AdminLaporanSemuaActivity extends AppCompatActivity {

    @BindView(R.id.rl_laporanHariIni)
    RelativeLayout rlLaporanHariIni;
    private Context context = AdminLaporanSemuaActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_laporan_semua);
    }

    @OnClick(R.id.rl_laporanHariIni)
    public void laporanHariIni(){
        startActivity(new Intent(context,AdminSemuaLaporanLayoutActivity.class));
    }
}
