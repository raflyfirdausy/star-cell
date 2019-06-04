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
import com.rfl.trn.starr_cell.Activity.TambahKaryawanActivity;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Model.KaryawanModel;
import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterListKaryawan extends RecyclerView.Adapter<AdapterListKaryawan.MyViewHolder> {


    private Context context;
    private List<KaryawanModel> data;
    private List<KaryawanModel> dataSementara;
    private List<String> key;

    public AdapterListKaryawan(Context context, List<KaryawanModel> data, List<String> key) {
        this.context = context;
        this.data = data;
        this.key = key;
        this.dataSementara = new ArrayList<>();
        this.dataSementara.addAll(data);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_karyawan_admin, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.setDataKeView(data.get(i), key.get(i));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_background)
        ImageView ivBackground;
        @BindView(R.id.iv_fotoKaryawan)
        ImageView ivFotoKaryawan;
        @BindView(R.id.tv_namaKaryawan)
        MyTextView tvNamaKaryawan;
        @BindView(R.id.tv_tanggalDiubah)
        MyTextView tvTanggalDiubah;
        @BindView(R.id.tv_alamatKaryawan)
        MyTextView tvAlamatKaryawan;
        @BindView(R.id.tv_noHpKaryawan)
        MyTextView tvNoHpKaryawan;
        @BindView(R.id.tv_statusKaryawan)
        MyTextView tvStatusKaryawan;
        @BindView(R.id.layout_currentKaryawan)
        LinearLayout layoutCurrentKaryawan;
        @BindView(R.id.cv_parent_karyawan)
        CardView cvParentKaryawan;
        @BindView(R.id.ll_parent_action)
        LinearLayout llParentAction;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        private void pindahActivity(KaryawanModel karyawanModel, String id, String jenis) {
            int sex;
            Intent intent = new Intent(context, TambahKaryawanActivity.class);
            intent.putExtra("jenis", jenis);
            intent.putExtra("key", karyawanModel.getIdKaryawan());
            intent.putExtra("namaKaryawan", karyawanModel.getNamaKaryawan());
            intent.putExtra("alamatKaryawan", karyawanModel.getAlamatKaryawan());
            intent.putExtra("noHpKaryawan", String.valueOf(karyawanModel.getNomerHp()));
            if (karyawanModel.getJenisKelamin().equalsIgnoreCase("Laki-Laki")) {
                sex = 1;
            } else {
                sex = 2;
            }
            intent.putExtra("sexKaryawan", sex);

            if (karyawanModel.getPhotoUrl() != null) {
                intent.putExtra("urlFoto", karyawanModel.getPhotoUrl());
            }
            context.startActivity(intent);
        }

        void setDataKeView(final KaryawanModel isiData, final String s) {
            tvNamaKaryawan.setText(isiData.getNamaKaryawan());
            tvNoHpKaryawan.setText(String.valueOf(isiData.getNomerHp()));
            tvAlamatKaryawan.setText(isiData.getAlamatKaryawan());
            tvTanggalDiubah.setText(String.valueOf(new Bantuan(context).getDatePretty(isiData.getTanggalDiubah(), false)));
            tvStatusKaryawan.setText(isiData.getStatusKerja());
            llParentAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String[] listItem = new String[]{"Lihat Data Karyawan",
                            "Edit Data Karyawan", "Hapus Karyawan ", "Batal"};
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(context);
                    builder.setTitle("Pilih Aksi Untuk " + isiData.getNamaKaryawan())
                            .setItems(listItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        lihatDataKonter(isiData);
                                    } else if (which == 1) {
                                        pindahActivity(isiData, isiData.getIdKaryawan(), "edit");
                                    } else if (which == 2) {
                                        hapusKonter(isiData);
                                    }
                                }
                            })
                            .setCancelable(true)
                            .create()
                            .show();
                }
            });
        }

        private void hapusKonter(final KaryawanModel isiData) {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            SweetAlertDialog tanya = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Peringatan")
                    .setContentText("Apakah kamu yakin akan menghapus " +
                            isiData.getNamaKaryawan()
                            + " ?\nSemua Data pada konter tersebut akan di hapus secara permanent. " +
                            "Data yang telah di hapus tidak dapat di kembalikan")
                    .setConfirmText("Ya, Hapus")
                    .setCancelText("Batal")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            //TODO : hapus konter
                            sweetAlertDialog.dismissWithAnimation();
                            databaseReference.child("karyawan")
                                    .child(isiData.getIdKaryawan())
                                    .removeValue();
                            notifyDataSetChanged();
                        }
                    });
            tanya.show();


        }

        private void lihatDataKonter(KaryawanModel karyawanModel) {
            final SweetAlertDialog detail = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Detail Data " + karyawanModel.getNamaKaryawan())
                    .setContentText(
                            "Nama Konter : " + karyawanModel.getNamaKaryawan() + "\n" +
                                    "Alamat Konter : " + karyawanModel.getAlamatKaryawan()
                    );
            detail.show();
        }
    }

    public void cariKaryawan(String text) {
        text = text.toLowerCase(Locale.getDefault());
        data.clear();
        if (text.length() == 0) {
            data.addAll(dataSementara);
        } else {
            for (int i = 0; i < dataSementara.size(); i++) {
                if (dataSementara.get(i).getNamaKaryawan().toLowerCase(Locale.getDefault()).contains(text)) {
                    data.add(dataSementara.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
}
