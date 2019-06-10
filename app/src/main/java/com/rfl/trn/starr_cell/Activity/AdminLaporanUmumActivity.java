package com.rfl.trn.starr_cell.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.anychart.AnyChartView;
import com.rfl.trn.starr_cell.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.supercharge.shimmerlayout.ShimmerLayout;

public class AdminLaporanUmumActivity extends AppCompatActivity {

    @BindView(R.id.cv_beratBadan)
    AnyChartView cvBeratBadan;
    @BindView(R.id.shimmer_text)
    ShimmerLayout shimmerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_laporan_umum);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        getSupportActionBar().setSubtitle("Laporan Semua");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

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
