package com.rfl.trn.starr_cell.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rfl.trn.starr_cell.Custom.MyEditText;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.R;

import java.util.HashMap;
import java.util.Objects;

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
    private Context context = PengaturanUmumActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_umum);
        getSupportActionBar().setSubtitle("Pengaturan Umum");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("pengaturan").child("semua");
        ButterKnife.bind(this);
        enablleEditetxt(false);
        btnEditablePengaturan.setText("Edit");
        myetNamaKonter.setTextColor(getResources().getColor(R.color.gray_btn_bg_pressed_color));
        myetAlamatKonter.setTextColor(getResources().getColor(R.color.gray_btn_bg_pressed_color));
        myetTelpKOnter.setTextColor(getResources().getColor(R.color.gray_btn_bg_pressed_color));

        setText();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setText() {
        databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            try {
                                myetNamaKonter.setText(dataSnapshot.child("namaKonter").getValue(String.class));
                                myetAlamatKonter.setText(dataSnapshot.child("alamatKonter").getValue(String.class));
                                myetTelpKOnter.setText(dataSnapshot.child("noTelp").getValue(String.class));
                            }catch (NullPointerException e){
                                new Bantuan(context).swal_error(e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        new Bantuan(context).swal_error(databaseError.getMessage());
                    }
                });
    }

    @OnClick(R.id.btn_editablePengaturan)
    public void setEditable(){
        if (!isEditable){
            isEditable = true;
            btnEditablePengaturan.setText("Batal");
            myetNamaKonter.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            myetAlamatKonter.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            myetTelpKOnter.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            enablleEditetxt(isEditable);
        }else {
            isEditable = false;
            btnEditablePengaturan.setText("Edit");
            myetNamaKonter.setTextColor(getResources().getColor(R.color.gray_btn_bg_pressed_color));
            myetAlamatKonter.setTextColor(getResources().getColor(R.color.gray_btn_bg_pressed_color));
            myetTelpKOnter.setTextColor(getResources().getColor(R.color.gray_btn_bg_pressed_color));
            enablleEditetxt(isEditable);
        }
    }
    @OnClick(R.id.btn_simpanPengaturan)
    public void simpanPengaturan(){
        HashMap<String,String> data = new HashMap<>();
        data.put("namaKonter",myetNamaKonter.getText().toString());
        data.put("alamatKonter",myetAlamatKonter.getText().toString());
        data.put("noTelp",myetTelpKOnter.getText().toString());
      databaseReference
              .setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()){
                  new Bantuan(context).swal_sukses("Berhasil Menambahkan pengaturan");
                  enablleEditetxt(false);
              }
          }
      });
    }

    private void enablleEditetxt(boolean b) {
        myetNamaKonter.setEnabled(b);
        myetAlamatKonter.setEnabled(b);
        myetTelpKOnter.setEnabled(b);


    }
}
