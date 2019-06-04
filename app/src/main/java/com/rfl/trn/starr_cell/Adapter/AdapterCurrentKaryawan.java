package com.rfl.trn.starr_cell.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rfl.trn.starr_cell.Custom.MyTextView;
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

        }
    }
}
