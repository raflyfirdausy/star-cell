package com.rfl.trn.starr_cell.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.rfl.trn.starr_cell.Adapter.AdapterDialogKonter;
import com.rfl.trn.starr_cell.Adapter.AdapterKategori;
import com.rfl.trn.starr_cell.Custom.MyEditText;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Interface.IDialog;
import com.rfl.trn.starr_cell.Model.BarangModel;
import com.rfl.trn.starr_cell.Model.KategoriModel;
import com.rfl.trn.starr_cell.Model.KonterModel;
import com.rfl.trn.starr_cell.R;
import com.wajahatkarim3.easymoneywidgets.EasyMoneyEditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TambahBarangActivity extends AppCompatActivity {


    @BindView(R.id.iv_karyawan)
    ImageView ivKaryawan;
    @BindView(R.id.tv_detailKaryawan)
    MyTextView tvDetailKaryawan;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.myet_kategori)
    MyEditText myetKategori;
    @BindView(R.id.myet_namaBarang)
    MyEditText myetNamaBarang;
    @BindView(R.id.myet_stokBarang)
    MyEditText myetStokBarang;
    @BindView(R.id.myet_harga1)
    EasyMoneyEditText myetHarga1;
    @BindView(R.id.myet_harga2)
    EasyMoneyEditText myetHarga2;
    @BindView(R.id.myet_harga3)
    EasyMoneyEditText myetHarga3;
    @BindView(R.id.btn_tambahBarang)
    MyTextView btnDaftar;
    @BindView(R.id.myet_konter)
    MyEditText myetKonter;
    @BindView(R.id.input_kategori)
    TextInputLayout inputKategori;
    @BindView(R.id.input_konter)
    TextInputLayout inputKonter;
    RecyclerView rvDialog;
    ImageButton tambahItemDialog;
    MyEditText namaDialog;
    LinearLayout layoutTambahItemDialog;
    ImageButton buttonTambahItem;
    MyTextView judulDialog, dialogKosong;
    Dialog dialog;
    Context context = TambahBarangActivity.this;
    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference databaseReference;
    private List<KategoriModel> listKategori;
    private List<KonterModel> listKonter;
    private List<String> listIdKonter;
    private boolean tambahKategori = false;
    private String idKonter = null;
    private String idKategori = null;
    private String idBarang = null;
    private Date date = new Date();
    private Long timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);
        ButterKnife.bind(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        getSupportActionBar().setSubtitle(R.string.tambah_data_barang);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_kategori_konter);
        rvDialog = dialog.findViewById(R.id.rv_kategori);
        tambahItemDialog = dialog.findViewById(R.id.btn_add_kategori);
        namaDialog = dialog.findViewById(R.id.myet_namaKategori);
        layoutTambahItemDialog = dialog.findViewById(R.id.ll_tambah);
        buttonTambahItem = dialog.findViewById(R.id.btn_add_act);
        judulDialog = dialog.findViewById(R.id.mytv_judulDialog);
        dialogKosong = dialog.findViewById(R.id.mytv_rvKosong);

        Intent intent = getIntent();
        if (intent.getExtras() == null) {
            idBarang = databaseReference.push().getKey();
            timestamp = date.getTime();
        } else {
            String id = intent.getStringExtra("id");
            idBarang = id;
            setField(id);
        }
    }

    private void setField(String id) {
        databaseReference.child("barang")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            BarangModel model = dataSnapshot.getValue(BarangModel.class);
                            myetNamaBarang.setText(model.getNamaBarang());
                            myetStokBarang.setText(model.getStokBarang());
                            myetHarga1.setText(String.valueOf(model.getHarga1()));
                            myetHarga2.setText(String.valueOf(model.getHarga2()));
                            myetHarga3.setText(String.valueOf(model.getHarga3()));
                            idKategori = model.getIdKategori();
                            idKonter = model.getIdKonter();
                            timestamp = model.getTanggalDiubah();

                            databaseReference.child("konter")
                                    .child(model.getIdKonter())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                String namaKonter = dataSnapshot.child("namaKonter").getValue(String.class);
                                                myetKonter.setText(namaKonter);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                            databaseReference.child("kategori")
                                    .child(model.getIdKategori())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                String namaKategori = dataSnapshot.child("namaKategori").getValue(String.class);
                                                myetKategori.setText(namaKategori);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    //TODO :: Fetch Data
    public void DialogKategori() {
        listKategori = new ArrayList<>();
        listKategori.clear();
        layoutTambahItemDialog.setVisibility(View.GONE);
        dialog.show();


        buttonTambahItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tambahKategori) {
                    layoutTambahItemDialog.setVisibility(View.GONE);
                    tambahKategori = false;
                } else {
                    layoutTambahItemDialog.setVisibility(View.VISIBLE);
                    tambahKategori = true;
                }
            }
        });
        databaseReference.child("kategori")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            listKategori.clear();
                            dialogKosong.setVisibility(View.GONE);
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                KategoriModel model;
                                model = data.getValue(KategoriModel.class);
                                listKategori.add(model);
                            }
                            AdapterKategori adapterKategori = new AdapterKategori(listKategori, context, new IDialog() {
                                @Override
                                public void onItemClick(String id, String nama, boolean isDismiss) {
                                    myetKategori.setText(nama);
                                    idKategori = id;

                                    if (isDismiss) {
                                        {
                                            dialog.dismiss();
                                        }
                                    }
                                }

                                @Override
                                public void onItemPopUpMenu(String id, int menu) {

                                }
                            });
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                            rvDialog.setLayoutManager(layoutManager);
                            rvDialog.setAdapter(adapterKategori);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        tambahItemDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idKategori = databaseReference.push().getKey();
                KategoriModel model = new KategoriModel(idKategori,
                        namaDialog.getText().toString(),
                        new Bantuan(context).getDayTimestamp(timestamp));
                databaseReference.child("kategori")
                        .child(idKategori)
                        .setValue(model);
                namaDialog.setText("");
            }
        });
    }

    public void DialogKonter() {
        listKonter = new ArrayList<>();
        listIdKonter = new ArrayList<>();
        listKonter.clear();
        listIdKonter.clear();


        judulDialog.setText("Pilih Konter");
        layoutTambahItemDialog.setVisibility(View.GONE);
        tambahItemDialog.setVisibility(View.GONE);
        dialog.show();


        databaseReference.child("konter")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            dialogKosong.setVisibility(View.GONE);
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                KonterModel model;
                                String idKonter = data.getKey();
                                model = data.getValue(KonterModel.class);
                                listKonter.add(model);
                                listIdKonter.add(idKonter);
                            }
                            AdapterDialogKonter adapterDialogKonter = new AdapterDialogKonter(context, listKonter, listIdKonter, new IDialog() {
                                @Override
                                public void onItemClick(String id, String nama, boolean isDismiss) {
                                    if (isDismiss) {
                                        myetKonter.setText(nama);
                                        idKonter = id;
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onItemPopUpMenu(String id, int menu) {

                                }
                            });
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                            rvDialog.setLayoutManager(layoutManager);
                            rvDialog.setAdapter(adapterDialogKonter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

    }

    //TODO :: Bind(OnCLick dll)
    @OnClick(R.id.myet_kategori)
    void kategoriKlik() {
        DialogKategori();
    }

    @OnClick(R.id.myet_konter)
    void kontelKlik() {
        DialogKonter();
    }

    @OnClick(R.id.btn_tambahBarang)
    void tambahBarang() {
        if (cekInput()) {
            final SweetAlertDialog loading = new Bantuan(context).swal_loading("Tunggu beberapa saat, proses menyimpan barang");
            loading.show();
            BarangModel model = new BarangModel(
                    myetNamaBarang.getText().toString(),
                    myetStokBarang.getText().toString(),
                    myetHarga1.getValueString(),
                    myetHarga2.getValueString(),
                    myetHarga3.getValueString(),
                    idKonter,
                    idKategori,
                    timestamp
            );
            databaseReference.child("barang")
                    .child(idBarang)
                    .setValue(model)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                loading.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                loading.showContentText(true);
                                loading.setTitleText("Sukses");
                                loading.setContentText("Berhasil menambahkan");
                                loading.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        finish();
                                    }
                                });

                            } else {
                                new Bantuan(context).swal_error("Gagal");
                            }
                        }
                    });
            bersihInput();
        } else {
            new Bantuan(context).swal_error("Masih ada data yang belum diinput . !");
        }

    }

    private void bersihInput() {
        myetNamaBarang.setText("");
        myetHarga1.setText("");
        myetHarga2.setText("");
        myetHarga3.setText("");
    }

    private boolean cekInput() {
        return !TextUtils.isEmpty(myetNamaBarang.getText()) &&
                !TextUtils.isEmpty(myetStokBarang.getText()) &&
                !TextUtils.isEmpty(myetKategori.getText()) &&
                !TextUtils.isEmpty(myetKonter.getText()) &&
                !TextUtils.isEmpty(myetHarga1.getText());
    }

    //TODO :: LifeCycle


}
