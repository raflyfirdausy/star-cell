package com.rfl.trn.starr_cell.Activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.abhinay.input.CurrencyEditText;

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
    MyEditText myetHarga1;
    @BindView(R.id.myet_harga2)
    MyEditText myetHarga2;
    @BindView(R.id.myet_harga3)
    MyEditText myetHarga3;
    @BindView(R.id.btn_tambahBarang)
    MyTextView btnDaftar;
    @BindView(R.id.myet_konter)
    MyEditText myetKonter;
    @BindView(R.id.input_kategori)
    TextInputLayout inputKategori;
    @BindView(R.id.input_konter)
    TextInputLayout inputKonter;

    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference databaseReference;
    private List<KategoriModel> listKategori;
    private List<KonterModel> listKonter;
    private List<String> listIdKonter;
    private boolean tambahKategori = false;
    private String idKonter = null;
    private String idKategori = null;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);
        ButterKnife.bind(this);
        context = TambahBarangActivity.this;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        bindEditText();
    }

    private void bindEditText() {
        myetHarga1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                myetHarga1.removeTextChangedListener(this);

                try{
                    String originalString = s.toString();
                    Long longval;
                    if (originalString.contains(",")){
                        originalString = originalString.replaceAll(",","");

                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formater = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
                    formater.applyPattern("#,###,###,###");
                    String formattedString = formater.format(longval);

                    myetHarga1.setText(formattedString);
                    myetHarga1.setSelection(myetHarga1.getText().length());
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
        });
    }

    //TODO :: Fetch Data
    public void DialogKategori() {
        listKategori = new ArrayList<>();
        listKategori.clear();

        final Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_kategori_konter);

        final RecyclerView rv = (RecyclerView) dialog.findViewById(R.id.rv_kategori);
        final ImageButton buttonTambah = (ImageButton) dialog.findViewById(R.id.btn_add_kategori);
        final MyEditText namaKategori = (MyEditText) dialog.findViewById(R.id.myet_namaKategori);
        final LinearLayout tambah = (LinearLayout) dialog.findViewById(R.id.ll_tambah);
        final ImageButton add = (ImageButton) dialog.findViewById(R.id.btn_add_act);
        final MyTextView judul = (MyTextView) dialog.findViewById(R.id.mytv_judulDialog);
        final MyTextView kosong = (MyTextView) dialog.findViewById(R.id.mytv_rvKosong);

        judul.setText("Pilih Kategori");

        tambah.setVisibility(View.GONE);

        dialog.show();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tambahKategori) {
                    tambah.setVisibility(View.GONE);
                    tambahKategori = false;
                } else {
                    tambah.setVisibility(View.VISIBLE);
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
                            kosong.setVisibility(View.GONE);
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
                            });
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                            rv.setLayoutManager(layoutManager);
                            rv.setAdapter(adapterKategori);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        buttonTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idKategori = databaseReference.push().getKey();
                KategoriModel model = new KategoriModel(idKategori, namaKategori.getText().toString());
                databaseReference.child("kategori")
                        .child(idKategori)
                        .setValue(model);
                namaKategori.setText("");
            }
        });
    }

    public void DialogKonter() {
        listKonter = new ArrayList<>();
        listIdKonter = new ArrayList<>();
        listKonter.clear();
        listIdKonter.clear();
        final Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_kategori_konter);
        final RecyclerView rv = (RecyclerView) dialog.findViewById(R.id.rv_kategori);
        final ImageButton buttonTambah = (ImageButton) dialog.findViewById(R.id.btn_add_kategori);
        final MyEditText namaKategori = (MyEditText) dialog.findViewById(R.id.myet_namaKategori);
        final LinearLayout tambah = (LinearLayout) dialog.findViewById(R.id.ll_tambah);
        final ImageButton add = (ImageButton) dialog.findViewById(R.id.btn_add_act);
        final MyTextView judul = (MyTextView) dialog.findViewById(R.id.mytv_judulDialog);
        final MyTextView kosong = (MyTextView) dialog.findViewById(R.id.mytv_rvKosong);

        judul.setText("Pilih Konter");
        tambah.setVisibility(View.GONE);
        add.setVisibility(View.GONE);
        dialog.show();


        databaseReference.child("konter")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            kosong.setVisibility(View.GONE);
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
                            });
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                            rv.setLayoutManager(layoutManager);
                            rv.setAdapter(adapterDialogKonter);
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

        BarangModel model = new BarangModel(
                myetNamaBarang.getText().toString(),
                myetStokBarang.getText().toString(),
                myetHarga1.getText().toString(),
                myetHarga2.getText().toString(),
                myetHarga3.getText().toString());
//new Bantuan(context).swal_warning("Kategori "+idKategori+" konter "+idKonter);
        databaseReference.child("barang")
                .child(idKonter)
                .child(idKategori)
                .push()
                .setValue(model)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            new Bantuan(context).swal_sukses("Sukses menambahkan");
                        } else {
                            new Bantuan(context).swal_error("Gagal");
                        }
                    }
                });
    }

    //TODO :: LifeCycle


}
