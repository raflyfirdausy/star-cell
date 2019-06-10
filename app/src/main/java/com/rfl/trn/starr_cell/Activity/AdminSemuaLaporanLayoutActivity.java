package com.rfl.trn.starr_cell.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.anychart.AnyChartView;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.rfl.trn.starr_cell.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminSemuaLaporanLayoutActivity extends AppCompatActivity {

    @BindView(R.id.cv_beratBadan)
    AnyChartView cvBeratBadan;
    @BindView(R.id.cv_collapse_chart)
    CardView cvCollapseChart;
    @BindView(R.id.rv_laporanSemua)
    ShimmerRecyclerView rvLaporanSemua;
    @BindView(R.id.ll_collapsed)
    LinearLayout llCollapsed;

    private Context context = AdminSemuaLaporanLayoutActivity.this;
    private boolean isCollapsed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_semua_laporan_layout);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        getSupportActionBar().setSubtitle("Semua Laporan");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        rvLaporanSemua.showShimmerAdapter();
        llCollapsed.setVisibility(View.VISIBLE);

    }
    @OnClick(R.id.cv_collapse_chart)
    public void setCollapsed(){
        if (!isCollapsed){
            isCollapsed = true;
            llCollapsed.setVisibility(View.GONE);
        }else {
            isCollapsed = false;
            llCollapsed.setVisibility(View.VISIBLE);
        }
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
