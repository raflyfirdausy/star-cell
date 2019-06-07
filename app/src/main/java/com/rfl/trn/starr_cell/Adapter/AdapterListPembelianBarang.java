package com.rfl.trn.starr_cell.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Model.ListPembelianBarangModel;
import com.rfl.trn.starr_cell.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterListPembelianBarang extends RecyclerView.Adapter<AdapterListPembelianBarang.ViewHolder> {
    private Context context;
    private List<ListPembelianBarangModel> data;

    public AdapterListPembelianBarang(Context context, List<ListPembelianBarangModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_daftar_pembelian_sementara, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setDatakeView(context, data.get(i));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layoutParent)
        LinearLayout layoutParent;
        @BindView(R.id.ivGambarBarangSementara)
        ImageView ivGambarBarangSementara;
        @BindView(R.id.tvNamaBarangSementara)
        TextView tvNamaBarangSementara;
        @BindView(R.id.tvRincianHarga)
        TextView tvRincianHarga;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        public void setDatakeView(Context context, ListPembelianBarangModel data) {
            String duaHuruf = data.getNamaBarang().substring(0, 2);
            TextDrawable gambar = TextDrawable.builder().buildRoundRect(duaHuruf, Color.parseColor("#3498db"), 8);
            ivGambarBarangSementara.setImageDrawable(gambar);

            double jumlahKaliHarga = data.getJumlahMasukKeranjang() * Double.parseDouble(data.getHargaBarang());

            tvNamaBarangSementara.setText(data.getNamaBarang());
            tvRincianHarga.setText(
                    data.getJumlahMasukKeranjang() + " x " +
                            "Rp " + new Bantuan(context).formatHarga(data.getHargaBarang())
                            + " = Rp " + new Bantuan(context).formatHarga(String.valueOf(jumlahKaliHarga))
            );
        }
    }
}
