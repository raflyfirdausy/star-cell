package com.rfl.trn.starr_cell.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Interface.IDialog;
import com.rfl.trn.starr_cell.Model.BarangModel;
import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdapterListBarang extends RecyclerView.Adapter<AdapterListBarang.MyViewHolder> {


    private Context context;
    private List<BarangModel> data;
    private List<BarangModel> dataSementara;
    private List<String> key;
    private IDialog listener;
    private BarangModel model;
    private String keyBarang;
    private DatabaseReference databaseReference;

    public AdapterListBarang(Context context, List<BarangModel> data, List<String> lisKey, IDialog iDialog) {
        this.context = context;
        this.data = data;
        this.listener = iDialog;
        this.key = lisKey;
        this.dataSementara = new ArrayList<>();
        this.dataSementara.addAll(data);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_barang, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        keyBarang = key.get(i);

        myViewHolder.setDataKewView(data.get(i));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_namaBarang)
        MyTextView tvNamaBarang;
        @BindView(R.id.tv_hargaBarang)
        MyTextView tvHargaBarang;
        @BindView(R.id.ll_parent)
        LinearLayout llParent;
        @BindView(R.id.iv_optionMenu)
        ImageView ivOptionMenu;
        @BindView(R.id.rl_parent)
        RelativeLayout rlParent;
        @BindView(R.id.tv_konterBarang)
        MyTextView tvKonterBarang;
        @BindView(R.id.tv_kategoriBarang)
        MyTextView tvKategoriBarang;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        void setDataKewView(BarangModel isiData) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
            tvNamaBarang.setText(isiData.getNamaBarang());
            tvKategoriBarang.setText(isiData.getIdKategori());
            tvKonterBarang.setText(isiData.getIdKonter());
            tvHargaBarang.setText("Rp " + isiData.getHarga1());

            databaseReference.child("barang")
                    .child(keyBarang)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                BarangModel model = dataSnapshot.getValue(BarangModel.class);

                                databaseReference.child("konter")
                                        .child(Objects.requireNonNull(model).getIdKonter())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    String namaKonter = dataSnapshot.child("namaKonter").getValue(String.class);
                                                    tvKonterBarang.setText(namaKonter);
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
                                                    tvKategoriBarang.setText(namaKategori);
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
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_detail_barang);


            final MyTextView myetDialogNamaBarang = dialog.findViewById(R.id.myet_dialogNamaBarang);
            final MyTextView myetDialogKonterBarang = dialog.findViewById(R.id.myet_dialogKonterBarang);
            final MyTextView myetDialogKategoriBarang = dialog.findViewById(R.id.myet_dialogKategoriBarang);
            final MyTextView myetDialogStokBarang = dialog.findViewById(R.id.myet_dialogStokBarang);
            final MyTextView myetDialogHarga1Barang = dialog.findViewById(R.id.myet_dialogHarga1Barang);
            final MyTextView myetDialogHarga2Barang = dialog.findViewById(R.id.myet_dialogHarga2Barang);
            final MyTextView myetDialogHarga3Barang = dialog.findViewById(R.id.myet_dialogHarga3Barang);
            final MyTextView myetDialogTanggalBarang = dialog.findViewById(R.id.myet_dialogTanggalBarang);
            databaseReference.child("barang")
                    .child(keyBarang)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                BarangModel model = dataSnapshot.getValue(BarangModel.class);
                                myetDialogNamaBarang.setText(model.getNamaBarang());
                                myetDialogStokBarang.setText(model.getStokBarang());
                                myetDialogHarga1Barang.setText("Rp." + String.valueOf(model.getHarga1()));
                                myetDialogHarga2Barang.setText("Rp." + String.valueOf(model.getHarga2()));
                                myetDialogHarga3Barang.setText("Rp." + String.valueOf(model.getHarga3()));

                                myetDialogTanggalBarang.setText(String.valueOf(new Bantuan(context).getDatePretty(model.getTanggalDiubah(), false)));

                                databaseReference.child("konter")
                                        .child(model.getIdKonter())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    String namaKonter = dataSnapshot.child("namaKonter").getValue(String.class);
                                                    myetDialogKonterBarang.setText(namaKonter);
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
                                                    myetDialogKategoriBarang.setText(namaKategori);
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
            rlParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                }
            });

        }
        @OnClick(R.id.iv_optionMenu)
        void inflateMenu() {
            final PopupMenu popupMenu = new PopupMenu(context, ivOptionMenu);
            popupMenu.inflate(R.menu.menu_pilihan_barang);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.menu_EditBarang) {
                        listener.onItemPopUpMenu(keyBarang, 1);
                    } else if (item.getItemId() == R.id.menu_DetailBarang_) {
                        listener.onItemPopUpMenu(keyBarang, 2);
                    }
                    return true;
                }
            });
            popupMenu.show();

        }

    }





    public void cariBarang(String text) {
        text = text.toLowerCase(Locale.getDefault());
        data.clear();
        if (text.length() == 0) {
            data.addAll(dataSementara);
        } else {
            for (int i = 0; i < dataSementara.size(); i++) {
                if (dataSementara.get(i).getNamaBarang().toLowerCase(Locale.getDefault()).contains(text)) {
                    data.add(dataSementara.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
}
