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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.clans.fab.FloatingActionButton;
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
import com.rfl.trn.starr_cell.Helper.SwipeToDelete;
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
    FloatingActionButton tambahItemDialog;
    MyEditText namaDialog;
    LinearLayout layoutTambahItemDialog;
    FloatingActionButton buttonTambahItem;
    MyTextView judulDialog, dialogKosong;
    @BindView(R.id.myet_hargabeli)
    EasyMoneyEditText myetHargabeli;


    private Dialog dialog;
    private Context context = TambahBarangActivity.this;
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
    private AdapterKategori adapterKategori;
    ;
    private View dialogView;

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
        LayoutInflater inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_kategori_konter, null);
        dialog.setContentView(dialogView);
        dialog.setCancelable(false);
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
                            myetHargabeli.setText(String.valueOf(model.getHargaBeli()));
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
                        new Bantuan(context).swal_error(databaseError.getMessage());
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
                            adapterKategori = new AdapterKategori(listKategori, context, new IDialog() {
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
                        new Bantuan(context).swal_error(databaseError.getMessage());
                    }
                });

        SwipeToDelete swipeToDeleteCallback = new SwipeToDelete(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final KategoriModel item = adapterKategori.getData().get(position);
                final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);

                adapterKategori.removeItem(position);
                databaseReference.child("barang")
                        .orderByChild("idKategori")
                        .equalTo(item.getIdKategori())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    dialog.setTitleText("Peringatan");
                                    dialog.setContentText("Masih ada barang dengan kategori " + item.getNamaKategori() + ".\nSilahkan hapus terlebih dahulu barangnya..");
                                    dialog.setConfirmText("Ke daftar Barang");
                                    dialog.setCancelText("Ga jadi hapus");
                                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            adapterKategori.restoreItem(item, position);
                                            dialog.dismissWithAnimation();
                                        }
                                    });
                                    dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            adapterKategori.restoreItem(item, position);
                                            dialog.dismissWithAnimation();
                                        }
                                    });
                                } else {
                                    dialog.setTitleText("Peringatan");
                                    dialog.setContentText("Anda akan menghapus kategori " + item.getNamaKategori() + ".\nLanjutkan ?");
                                    dialog.setConfirmText("Iya, Hapus");
                                    dialog.setCancelText("Nggak jadi ");
                                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            databaseReference.child("kategori")
                                                    .child(item.getIdKategori())
                                                    .removeValue();
                                            dialog.dismissWithAnimation();
                                        }
                                    });
                                    dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            adapterKategori.restoreItem(item, position);
                                            dialog.dismissWithAnimation();
                                        }
                                    });
                                }
                                dialog.show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                new Bantuan(context).swal_error(databaseError.getMessage());
                            }
                        });

                rvDialog.scrollToPosition(position);
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rvDialog);

        tambahItemDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(namaDialog.getText())) {
                    new Bantuan(context).swal_error("Nama kategori tidak boleh Kosong !");
                } else {
                    String idKategori = databaseReference.push().getKey();
                    KategoriModel model = new KategoriModel(idKategori,
                            namaDialog.getText().toString(),
                            new Bantuan(context).getDayTimestamp(timestamp));
                    databaseReference.child("kategori")
                            .child(idKategori)
                            .setValue(model);
                    namaDialog.setText("");

                }
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
                        new Bantuan(context).swal_error(databaseError.getMessage());
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
            BarangModel model = new BarangModel();

            model.setNamaBarang(myetNamaBarang.getText().toString());
            model.setStokBarang(myetStokBarang.getText().toString());
            model.setHarga1(myetHarga1.getValueString());
            model.setHarga2(myetHarga2.getValueString());
            model.setHarga3(myetHarga3.getValueString());
            model.setHargaBeli(myetHargabeli.getValueString());
            model.setIdKonter(idKonter);
            model.setIdKategori(idKategori);
            model.setTanggalDiubah(timestamp);

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
        return  !TextUtils.isEmpty(myetNamaBarang.getText()) &&
                !TextUtils.isEmpty(myetStokBarang.getText()) &&
                !TextUtils.isEmpty(myetKategori.getText()) &&
                !TextUtils.isEmpty(myetKonter.getText()) &&
                !TextUtils.isEmpty(myetHarga1.getText()) &&
                !TextUtils.isEmpty(myetHargabeli.getText());
    }

    //TODO :: LifeCycle
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
