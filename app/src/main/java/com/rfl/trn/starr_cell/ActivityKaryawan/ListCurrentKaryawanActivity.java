package com.rfl.trn.starr_cell.ActivityKaryawan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListCurrentKaryawanActivity extends AppCompatActivity {

    @BindView(R.id.iv_karyawan)
    ImageView ivKaryawan;
    @BindView(R.id.tv_judul)
    MyTextView tvJudul;
    @BindView(R.id.tv_detailKaryawan)
    MyTextView tvDetailKaryawan;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.ll_belum_ada_absen_masuk)
    LinearLayout llBelumAdaAbsenMasuk;
    @BindView(R.id.rv_daftarCurrentKaryawan)
    RecyclerView rvDaftarCurrentKaryawan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_current_karyawan);
        ButterKnife.bind(this);
    }
}
