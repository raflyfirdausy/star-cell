package com.rfl.trn.starr_cell.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rfl.trn.starr_cell.Custom.MyEditText;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Model.KaryawanModel;
import com.rfl.trn.starr_cell.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TambahKaryawanActivity extends AppCompatActivity {
    @BindView(R.id.iv_karyawan)
    ImageView ivKonter;
    @BindView(R.id.tv_detailKaryawan)
    MyTextView tvDetailKonter;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.iv_gambarKaryawan)
    ImageView ivGambarKaryawan;
    @BindView(R.id.myet_namaKaryawan)
    MyEditText myetNamaKaryawan;
    @BindView(R.id.myet_noHpKaryawan)
    MyEditText myetNoHpKaryawan;
    @BindView(R.id.radioLakiLaki)
    RadioButton radioLakiLaki;
    @BindView(R.id.radioPerempuan)
    RadioButton radioPerempuan;
    @BindView(R.id.radioGroupJenisKelamin)
    RadioGroup radioGroupJenisKelamin;
    @BindView(R.id.btn_daftar)
    MyTextView btnDaftar;

    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference databaseReference;
    private RadioButton radioButtonSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_karyawan);
        ButterKnife.bind(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    @OnClick(R.id.btn_daftar)
    void tambahKaryawan() {

        String idKaryawan = databaseReference.push().getKey();

        int selectedId = radioGroupJenisKelamin.getCheckedRadioButtonId();


        radioButtonSex = (RadioButton) findViewById(selectedId);


        KaryawanModel model = new KaryawanModel(
                idKaryawan,
                radioButtonSex.getText().toString(),
                "photo",
                "belum aktif",
                myetNamaKaryawan.getText().toString(),
                Integer.parseInt(myetNoHpKaryawan.getText().toString()));

        databaseReference.child("karyawan")
                .child(idKaryawan)
                .setValue(model);


    }
}
