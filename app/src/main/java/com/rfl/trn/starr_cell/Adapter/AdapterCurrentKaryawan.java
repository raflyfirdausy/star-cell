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
import com.rfl.trn.starr_cell.Model.AbsenModel;
import com.rfl.trn.starr_cell.R;

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
        viewHolder.setDataKewView(data.get(i));
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

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setDataKewView(AbsenModel isiData) {
            if(isiData.getStatus().equalsIgnoreCase("pending")){
                ivBackground.setImageResource(R.drawable.gradient_scooter);
            } else if(isiData.getStatus().equalsIgnoreCase("accept")){
                ivBackground.setImageResource(R.drawable.gradient_flare);
            } else if(isiData.getStatus().equalsIgnoreCase("reject")){
                ivBackground.setImageResource(R.drawable.gradient_red_mist);
            }
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("karyawan")
                    .child(isiData.getIdKaryawan())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                tvNamaKaryawan.setText(dataSnapshot.child("namaKaryawan").getValue(String.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            //njiot context si piwe :v
                        }
                    });

            tvWaktuAbsen.setText(isiData.getWaktuMasuk().toString());
            if (!isiData.isLembur()) {
                tvJenisAbsen.setText("Absen Normal");
            } else {
                tvJenisAbsen.setText("Absen Lembur");
            }
            tvPesanAbsen.setText(isiData.getPesan());
            tvStatusAbsen.setText(isiData.getStatus());
        }
    }
}
