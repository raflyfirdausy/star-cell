package com.rfl.trn.starr_cell.Activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.rfl.trn.starr_cell.Custom.MyEditText;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PengaturanUmumActivity extends AppCompatActivity {

    @BindView(R.id.iv_karyawan)
    ImageView ivKaryawan;
    @BindView(R.id.tv_detailKaryawan)
    MyTextView tvDetailKaryawan;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.myet_namaKonter)
    MyEditText myetNamaKonter;
    @BindView(R.id.myet_alamatKonter)
    MyEditText myetAlamatKonter;
    @BindView(R.id.input_kategori)
    TextInputLayout inputKategori;
    @BindView(R.id.myet_telpKOnter)
    MyEditText myetTelpKOnter;
    @BindView(R.id.input_konter)
    TextInputLayout inputKonter;
    @BindView(R.id.btn_editablePengaturan)
    MyTextView btnEditablePengaturan;
    @BindView(R.id.btn_simpanPengaturan)
    MyTextView btnSimpanPengaturan;

    private boolean isEditable = false;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_umum);
        ButterKnife.bind(this);
        enablleEditetxt(false);
    }

    @OnClick(R.id.btn_editablePengaturan)
    private void setEditable(){
        if (!isEditable){
            isEditable = true;
            enablleEditetxt(true);
        }else {
            isEditable = false;
            enablleEditetxt(false);
        }
    }
    @OnClick(R.id.btn_simpanPengaturan)
    private void simpanPengaturan(){
      
    }

    private void enablleEditetxt(boolean b) {
        myetNamaKonter.setEnabled(b);
        myetAlamatKonter.setEnabled(b);
        myetTelpKOnter.setEnabled(b);
    }
}
