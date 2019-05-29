package com.rfl.trn.starr_cell.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Model.KaryawanModel;
import com.rfl.trn.starr_cell.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailKaryawanActivity extends AppCompatActivity {

    @BindView(R.id.iv_detailKaryawan)
    ImageView ivDetailKaryawan;
    @BindView(R.id.mytv_detailNamaKaryawan)
    MyTextView mytvDetailNamaKaryawan;
    @BindView(R.id.mytv_detailStatusKaryawan)
    MyTextView mytvDetailStatusKaryawan;
    @BindView(R.id.mytv_detailAlamatKaryawan)
    MyTextView mytvDetailAlamatKaryawan;
    @BindView(R.id.mytv_detailNoHpKaryawan)
    MyTextView mytvDetailNoHpKaryawan;
    @BindView(R.id.mytv_detailTanggalKaryawan)
    MyTextView mytvDetailTanggalKaryawan;

    @BindView(R.id.progres_Karyawan)
    ProgressBar progresKaryawan;
    @BindView(R.id.btn_reload)
    Button btnReload;
    @BindView(R.id.ll_detail_failed)
    LinearLayout llDetailFailed;
    @BindView(R.id.sv_detailKaryawan)
    ScrollView svDetailKaryawan;

    private DatabaseReference databaseReference;
    private Context context = DetailKaryawanActivity.this;
    private KaryawanModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_karyawan);
        ButterKnife.bind(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        model = new KaryawanModel();
        Intent intent = getIntent();
        String idKaryawan = intent.getStringExtra("id");

        getKaryawan(idKaryawan);
    }

    private void getKaryawan(String idKaryawan) {
        llDetailFailed.setVisibility(View.GONE);
        svDetailKaryawan.setVisibility(View.GONE);
        databaseReference.child("karyawan")
                .child(idKaryawan)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progresKaryawan.setVisibility(View.GONE);
                        if (dataSnapshot.exists()) {
                            svDetailKaryawan.setVisibility(View.VISIBLE);
                            model = dataSnapshot.getValue(KaryawanModel.class);
                            mytvDetailNamaKaryawan.setText(model.getNamaKarywan());
                            mytvDetailStatusKaryawan.setText(model.getStatusKerja());
                            mytvDetailAlamatKaryawan.setText(model.getAlamatKaryawan());
                            mytvDetailNoHpKaryawan.setText(String.valueOf(model.getNomerHp()));
                            mytvDetailTanggalKaryawan.setText(new Bantuan(context).getDatePretty(model.getTanggalDiubah(),false));
                            Picasso.get()
                                    .load(model.getPhotoUrl())
                                    .placeholder(R.drawable.bg_take_pict)
                                    .into(ivDetailKaryawan);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        llDetailFailed.setVisibility(View.VISIBLE);
                    }
                });
    }
    @OnClick(R.id.btn_reload)
    void Reload(){


    }

}
