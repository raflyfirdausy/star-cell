package com.rfl.trn.starr_cell.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Interface.IKonfirmasiAbsen;
import com.rfl.trn.starr_cell.Model.AbsenModel;
import com.rfl.trn.starr_cell.Model.KaryawanModel;
import com.rfl.trn.starr_cell.R;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterListAbsensi extends RecyclerView.Adapter<AdapterListAbsensi.MyViewHolder> {

    private static final int ITEM_PEMISAH = 1;
    private static final int ITEM_ABSEN = 2;
    private Context context;
    private List<AbsenModel> data;
    private List<String> keyAbsen;
    private IKonfirmasiAbsen listener;


    public AdapterListAbsensi(Context context, List<AbsenModel> data, List<String> keyAbsen) {
        this.context = context;
        this.data = data;
        this.keyAbsen = keyAbsen;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_absen_admin, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.setDataKeViewAbsen(data.get(i), keyAbsen.get(i));

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
        @BindView(R.id.tv_waktuAbsen)
        MyTextView tvWaktuAbsen;
        @BindView(R.id.tv_jenisAbsen)
        MyTextView tvJenisAbsen;
        @BindView(R.id.tv_pesanAbsen)
        MyTextView tvPesanAbsen;
        @BindView(R.id.tv_statusAbsen)
        MyTextView tvStatusAbsen;
        @BindView(R.id.layout_currentKaryawan)
        LinearLayout layoutCurrentKaryawan;

        DatabaseReference databaseReference;
        String namaKaryawan, namaKonter;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setDataKeViewAbsen(final AbsenModel data, final String s) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("karyawan")
                    .child(data.getIdKaryawan())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                KaryawanModel model;
                                model = dataSnapshot.getValue(KaryawanModel.class);
                                tvNamaKaryawan.setText(model.getNamaKaryawan());
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            tvPesanAbsen.setText(data.getPesan());
            tvJenisAbsen.setText(data.getJenisAbsen());
//            tvWaktuAbsen.setText(data.getWaktuMasuk().toString());
            tvStatusAbsen.setText(data.getStatus());
            layoutCurrentKaryawan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] listItem = new String[0];
                    if (data.getStatus().equalsIgnoreCase("pending")) {
                        listItem = new String[]{"Lihat Data Absen",
                                "Tolak", "Terima", "Batal"};

                    } else {
                        listItem = new String[]{"Lihat Data Absen"};
                    }
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(context);
                    builder.setTitle("Pilih Aksi Untuk " + data.getIdKaryawan())
                            .setItems(listItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        lihatDataAbsen(data, s);
                                    } else if (which == 1) {
                                        TolakAbsen(data, s);
                                    } else if (which == 2) {
                                        TerimaAbsen(data, s);
                                    }
                                }
                            })
                            .setCancelable(true)
                            .create()
                            .show();

                }
            });
        }

        private void TerimaAbsen(final AbsenModel data, final String s) {
            final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
            dialog.setTitleText("Peringatan");
            dialog.setContentText("Terima Absen Karyawan " + data.getIdKaryawan() + "?");
            dialog.setConfirmText("Iya, Terima");
            dialog.setCancelText("Tidak dulu");
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    Long time = new Date().getTime();
                    databaseReference
                            .child("absen")
                            .child(s)
                            .child("status").setValue("accept");

                    databaseReference
                            .child("absen")
                            .child(s)
                            .child("waktuDiterima").setValue(time);
                    dialog.dismissWithAnimation();
                }
            });
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {


                    dialog.dismissWithAnimation();
                }
            });
            dialog.show();
        }

        private void TolakAbsen(AbsenModel data, final String edit) {
            final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
            dialog.setTitleText("Peringatan");
            dialog.setContentText("Tolak Absen Karyawan " + data.getIdKaryawan() + "?");
            dialog.setConfirmText("Iya, Tolak");
            dialog.setCancelText("Tidak dulu");
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    Long time = new Date().getTime();
                    databaseReference
                            .child("absen")
                            .child(edit)
                            .child("status").setValue("reject");
                    databaseReference
                            .child("absen")
                            .child(edit)
                            .child("waktuDiterima").setValue(time);
                    dialog.dismissWithAnimation();
                }
            });
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {

                    dialog.dismissWithAnimation();
                }
            });
            dialog.show();
        }

        private void lihatDataAbsen(AbsenModel data, String s) {
            final SweetAlertDialog detail = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Detail Data " + data.getIdKaryawan())
                    .setContentText(
                            "Nama Konter : " + data.getIdKonter() + "\n" +
                                    "Alamat Konter : " + data.getJenisAbsen()
                    );
            detail.show();
        }


    }
}
