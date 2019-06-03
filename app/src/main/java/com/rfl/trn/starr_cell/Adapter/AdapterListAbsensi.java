package com.rfl.trn.starr_cell.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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
import com.rfl.trn.starr_cell.Model.AbsenModel;
import com.rfl.trn.starr_cell.Model.KaryawanModel;
import com.rfl.trn.starr_cell.Model.KonterModel;
import com.rfl.trn.starr_cell.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterListAbsensi extends RecyclerView.Adapter<AdapterListAbsensi.MyViewHolder> {


    private Context context;
    private List<AbsenModel> data;

    public AdapterListAbsensi(Context context, List<AbsenModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_absensi, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        myViewHolder.setDataKeView(data.get(i));

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_karyawan)
        ImageView ivKaryawan;
        @BindView(R.id.tv_namaKonter)
        MyTextView tvNamaKonter;
        @BindView(R.id.tv_detailKaryawan)
        MyTextView tvDetailKaryawan;
        @BindView(R.id.ll_parent_absen)
        CardView llParent;

        DatabaseReference databaseReference;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setDataKeView(final AbsenModel data) {
            final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);


            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("karyawan")
                    .child(data.getIdKaryawan())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                KaryawanModel karyawanModel = new KaryawanModel();
                                karyawanModel = dataSnapshot.getValue(KaryawanModel.class);
                                tvNamaKonter.setText(karyawanModel.getNamaKaryawan());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
            databaseReference.child("konter")
                    .child(data.getIdKonter())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                KonterModel konterModel = new KonterModel();
                                konterModel = dataSnapshot.getValue(KonterModel.class);
                                tvDetailKaryawan.setText(konterModel.getNamaKonter());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            Picasso.get()
                    .load(data.getUrlFoto())
                    .into(ivKaryawan);
            llParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.setTitleText("Konfirmasi");
                    dialog.setContentText("Karyawan"+tvDetailKaryawan.getText()+".\n Telah melakuan absen pada "+data.getWaktuMasuk());
                    dialog.setConfirmText("Terima");
                    dialog.setCancelText("Tolak");
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

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
            });

        }
    }
}
