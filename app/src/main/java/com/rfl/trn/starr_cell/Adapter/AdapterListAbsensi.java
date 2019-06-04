package com.rfl.trn.starr_cell.Adapter;

import android.content.Context;
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
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Interface.IKonfirmasiAbsen;
import com.rfl.trn.starr_cell.Model.AbsenModel;
import com.rfl.trn.starr_cell.Model.KaryawanModel;
import com.rfl.trn.starr_cell.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterListAbsensi extends RecyclerView.Adapter<AdapterListAbsensi.MyViewHolder> {



    private Context context;
    private List<AbsenModel> data;
    private IKonfirmasiAbsen listener;
    private static final int ITEM_PEMISAH = 1;
    private static final int ITEM_ABSEN = 2;


    public AdapterListAbsensi(Context context, List<AbsenModel> data) {
        this.context = context;
        this.data = data;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_absen_admin, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.setDataKeViewAbsen(data.get(i));

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

        public void setDataKeViewAbsen(final AbsenModel data) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("karyawan")
                    .child(data.getIdKaryawan())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
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
            tvWaktuAbsen.setText(String.valueOf(new Bantuan(context).getDatePretty(data.getWaktuMasuk(),true)));
            tvNamaKaryawan.setText(data.getIdKaryawan());
            tvStatusAbsen.setText(data.getStatus());
        }


    }
}
