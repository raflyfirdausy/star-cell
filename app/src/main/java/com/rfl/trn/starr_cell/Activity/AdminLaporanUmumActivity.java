package com.rfl.trn.starr_cell.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anychart.AnyChartView;
import com.rfl.trn.starr_cell.R;

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

        shimmerText.startShimmerAnimation();
    }
}
