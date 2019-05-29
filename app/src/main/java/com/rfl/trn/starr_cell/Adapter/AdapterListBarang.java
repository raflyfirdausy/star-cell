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
import com.rfl.trn.starr_cell.Model.BarangModel;
import com.rfl.trn.starr_cell.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterListBarang extends RecyclerView.Adapter<AdapterListBarang.MyViewHolder> {

    private Context context;
    private List<BarangModel> data;

    public AdapterListBarang(Context context, List<BarangModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_barang, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        myViewHolder.setDataKewView(data.get(i));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_barang)
        ImageView ivBarang;
        @BindView(R.id.tv_namaBarang)
        MyTextView tvNamaBarang;
        @BindView(R.id.tv_hargaBarang)
        MyTextView tvHargaBarang;
        @BindView(R.id.ll_parent)
        LinearLayout llParent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setDataKewView(BarangModel isiData) {
            tvNamaBarang.setText(isiData.getNamaBarang());
            tvHargaBarang.setText(String.valueOf("Rp."+isiData.getHarga1()));
        }

    }
}
