package com.rfl.trn.starr_cell.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.rfl.trn.starr_cell.Activity.TambahBarangActivity;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Interface.IDialog;
import com.rfl.trn.starr_cell.Model.BarangModel;
import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterListBarang extends RecyclerView.Adapter<AdapterListBarang.MyViewHolder> {
    private Context context;
    private List<BarangModel> data;
    private List<BarangModel> dataSementara;
    private List<String> key;
    private IDialog listener;
    private BarangModel model;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_barang_admin, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.setDataKewView(data.get(i), key.get(i));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_backgroundBarang)
        ImageView ivBackgroundBarang;
        @BindView(R.id.iv_fotoBarang)
        ImageView ivFotoBarang;
        @BindView(R.id.tv_namaBarang)
        MyTextView tvNamaBarang;
        @BindView(R.id.tv_tanggalDiubah)
        MyTextView tvTanggalDiubah;
        @BindView(R.id.tv_hargaBarang)
        MyTextView tvHargaBarang;
        @BindView(R.id.tv_kategoriBarang)
        MyTextView tvKategoriBarang;
        @BindView(R.id.tv_stokBarang)
        MyTextView tvStokBarang;
        @BindView(R.id.ll_parent_barang)
        LinearLayout llParentBarang;
        @BindView(R.id.cv_parent_karyawan)
        CardView cvParentKaryawan;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void setDataKewView(final BarangModel isiData, final String s) {
            tvNamaBarang.setText(isiData.getNamaBarang());
            tvTanggalDiubah.setText("Diubah "+String.valueOf(new Bantuan(context).getDatePretty(isiData.getTanggalDiubah(),false)));
            tvKategoriBarang.setText(isiData.getIdKategori());
            tvHargaBarang.setText("Harga "+isiData.getHarga1());
            tvStokBarang.setText("Stok "+isiData.getStokBarang());
            llParentBarang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String[] listItem = new String[]{"Lihat Data Barang",
                            "Edit Data Barang", "Hapus Barang ", "Batal"};
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(context);
                    builder.setTitle("Pilih Aksi Untuk " + isiData.getNamaBarang())
                            .setItems(listItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        lihatDataBarang(isiData);
                                    } else if (which == 1) {
                                        pindahActivity(isiData, s, "edit");
                                    } else if (which == 2) {
                                        hapusBarang(isiData, s);
                                    }
                                }
                            })
                            .setCancelable(true)
                            .create()
                            .show();
                }
            });

        }

        private void hapusBarang(final BarangModel isiData, final String s) {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            SweetAlertDialog tanya = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Peringatan")
                    .setContentText("Apakah kamu yakin akan menghapus " +
                            isiData.getNamaBarang()
                            + " ?\nSemua Data barang tersebut akan di hapus secara permanent. " +
                            "Data yang telah di hapus tidak dapat di kembalikan")
                    .setConfirmText("Ya, Hapus")
                    .setCancelText("Batal")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            //TODO : hapus konter
                            sweetAlertDialog.dismissWithAnimation();
                            databaseReference.child("barang")
                                    .child(s)
                                    .removeValue();
                            notifyDataSetChanged();
                        }
                    });
            tanya.show();
        }

        private void pindahActivity(BarangModel isiData, String s, String edit) {

            context.startActivity(new Intent(context, TambahBarangActivity.class).putExtra("id", s));
        }

        private void lihatDataBarang(BarangModel isiData) {
            final SweetAlertDialog detail = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Detail Data " + isiData.getNamaBarang())
                    .setContentText(
                            "Harga : " + isiData.getHarga1() + "\n" +
                                    "Stok : " + isiData.getStokBarang()
                    );
            detail.show();
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


