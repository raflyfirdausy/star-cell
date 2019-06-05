package com.rfl.trn.starr_cell.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.rfl.trn.starr_cell.ActivityKaryawan.AbsenKaryawanActivity;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Helper.Waktu;
import com.rfl.trn.starr_cell.Model.AbsenModel;
import com.rfl.trn.starr_cell.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterCurrentKaryawan extends RecyclerView.Adapter<AdapterCurrentKaryawan.ViewHolder> {
    private Context context;
    private List<AbsenModel> data;


    public AdapterCurrentKaryawan(Context context, List<AbsenModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_current_karyawan, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setDataKewView(context, data.get(i));
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    static
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_background)
        ImageView ivBackground;
        @BindView(R.id.iv_fotoKaryawan)
        ImageView ivFotoKaryawan;
        @BindView(R.id.tv_namaKaryawan)
        TextView tvNamaKaryawan;
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

        String namaKaryawan;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @SuppressLint("SetTextI18n")
        void setDataKewView(final Context context, final AbsenModel isiData) {
            if (isiData.getStatus().equalsIgnoreCase("pending")) {
                ivBackground.setImageResource(R.drawable.gradient_scooter);
            } else if (isiData.getStatus().equalsIgnoreCase("accept")) {
                ivBackground.setImageResource(R.drawable.gradient_flare);
            } else if (isiData.getStatus().equalsIgnoreCase("reject")) {
                ivBackground.setImageResource(R.drawable.gradient_red_mist);
            }
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("karyawan")
                    .child(isiData.getIdKaryawan())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                namaKaryawan = dataSnapshot.child("namaKaryawan").getValue(String.class);
                                tvNamaKaryawan.setText(namaKaryawan);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            new Bantuan(context).swal_error(databaseError.getMessage());
                        }
                    });

            tvWaktuAbsen.setText(new Waktu(context).getWaktuLengkapIndonesia(isiData.getWaktuMasuk()));
            if (!isiData.isLembur()) {
                tvJenisAbsen.setText("Absen Normal");
            } else {
                tvJenisAbsen.setText("Absen Lembur");
            }
            tvPesanAbsen.setText(isiData.getPesan());
            tvStatusAbsen.setText(isiData.getStatus());

            Picasso.get()
                    .load(isiData.getUrlFoto())
                    .placeholder(R.drawable.ic_karyawan)
                    .error(R.drawable.error_center_x)
                    .fit()
                    .into(ivFotoKaryawan);

            layoutCurrentKaryawan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                    dialog.setTitleText("Perhatian");
                    dialog.setContentText("Apakah anda ingin absen keluar untuk karyawan dengan nama " + namaKaryawan + " ?");
                    dialog.setConfirmText("Iya, Absen Keluar");
                    dialog.setCancelText("Batal");

                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            //TODO : pindah absen keluar
                            sweetAlertDialog.dismissWithAnimation();
                            Intent intent = new Intent(context, AbsenKaryawanActivity.class);
                            intent.putExtra("idCurrentKaryawan", isiData.getIdCurrentKaryawan());
                            intent.putExtra("idKaryawan", isiData.getIdKaryawan());
                            intent.putExtra("idAbsenMasuk", isiData.getIdAbsen());
                            if (isiData.isLembur()) {
                                intent.putExtra("jenis", "absenKeluarLembur");
                            } else {
                                intent.putExtra("jenis", "absenKeluarNormal");
                            }
                            context.startActivity(intent);
                        }
                    });
                    dialog.show();
                }
            });
        }
    }
}
