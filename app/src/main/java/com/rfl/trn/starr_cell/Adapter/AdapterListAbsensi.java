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
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Interface.IKonfirmasiAbsen;
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
    private IKonfirmasiAbsen listener;
    private static final int ITEM_ACCEPT = 1;
    private static final int ITEM_PENDING = 2;
    private static final int ITEM_REJECT = 3;


    public AdapterListAbsensi(Context context, List<AbsenModel> data, IKonfirmasiAbsen iKonfirmasiAbsen) {
        this.context = context;
        this.data = data;
        this.listener = iKonfirmasiAbsen;
    }

    @Override
    public int getItemViewType(int position) {
        AbsenModel absenModel = data.get(position);
        if (absenModel.getStatus().equalsIgnoreCase("accept")){
            return ITEM_ACCEPT;
        }else if(absenModel.getStatus().equalsIgnoreCase("pending")){
            return ITEM_PENDING;
        }else if (absenModel.getStatus().equalsIgnoreCase("reject")){
            return ITEM_REJECT;
        }else {
            return ITEM_REJECT;
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        MyViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (viewType){
            case ITEM_ACCEPT:
                View view;
                break;
            case ITEM_PENDING:
                break;
            case ITEM_REJECT:
                break;
        }
        return viewHolder;
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
        String namaKaryawan,namaKonter;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setDataKeView(final AbsenModel data) {


            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("karyawan")
                    .child(data.getIdKaryawan())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                KaryawanModel karyawanModel ;
                                karyawanModel = dataSnapshot.getValue(KaryawanModel.class);
                                namaKaryawan = karyawanModel.getNamaKaryawan();
                                tvNamaKonter.setText(namaKaryawan);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            new Bantuan(context).swal_error(databaseError.getMessage());
                        }
                    });
            databaseReference.child("konter")
                    .child(data.getIdKonter())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                KonterModel konterModel ;
                                konterModel = dataSnapshot.getValue(KonterModel.class);
                                namaKonter = konterModel.getNamaKonter();
                                tvDetailKaryawan.setText(konterModel.getNamaKonter());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            new Bantuan(context).swal_error(databaseError.getMessage());
                        }
                    });

            Picasso.get()
                    .load(data.getUrlFoto())
                    .into(ivKaryawan);
            llParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.AlertKonfirmas(namaKaryawan,namaKonter, data);
                }
            });

        }
    }
}
