package com.rfl.trn.starr_cell.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.widget.ImageView;

import com.rfl.trn.starr_cell.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminLaporanSemuaActivity extends AppCompatActivity {


    @BindView(R.id.iv_icon_card)
    ImageView ivIconCard;
    @BindView(R.id.cv_laporanHariIni)
    CardView cvLaporanHariIni;
    @BindView(R.id.iv_icon_card1)
    ImageView ivIconCard1;
    private Context context = AdminLaporanSemuaActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_laporan_semua);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        getSupportActionBar().setSubtitle("Semua Laporan");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
    @OnClick(R.id.cv_laporanHariIni)
    public void kelaporanHariIni(){
        startActivity(new Intent(context,AdminSemuaLaporanLayoutActivity.class));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
