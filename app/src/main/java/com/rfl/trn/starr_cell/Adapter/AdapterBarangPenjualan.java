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

import com.amulyakhare.textdrawable.TextDrawable;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Fragment.Karyawan.PenjualanBarangActivity;
import com.rfl.trn.starr_cell.Model.BarangModel;
import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterBarangPenjualan extends RecyclerView.Adapter<AdapterBarangPenjualan.ViewHolder> {

    private Context context;
    private List<BarangModel> data;
    private List<BarangModel> dataSementara;


    public AdapterBarangPenjualan(Context context, List<BarangModel> data) {
        this.context = context;
        this.data = data;
        this.dataSementara = new ArrayList<>();
        this.dataSementara.addAll(data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_barang_penjualan, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        String duaHuruf = data.get(i).getNamaBarang().substring(0, 2);
        TextDrawable gambar = TextDrawable.builder().buildRoundRect(duaHuruf, Color.parseColor("#2980b9"), 8);
        viewHolder.ivBarang.setImageDrawable(gambar);

        TextDrawable gambarJumlah = TextDrawable.builder()
                .buildRound(String.valueOf(data.get(i).getJumlahMasukKeranjang()),
                        Color.parseColor("#2980b9"));
        if (data.get(i).getJumlahMasukKeranjang() > 0) {
            viewHolder.ivMasukKeranjang.setImageDrawable(gambarJumlah);
        }

        viewHolder.tvNamaBarang.setText(data.get(i).getNamaBarang());
        viewHolder.tvStokHarga.setText(
                "Stok : " + data.get(i).getStokBarang() + " | Rp " +
                        data.get(i).getHarga1() + " | Rp " + data.get(i).getHarga2() +
                        " | Rp " + data.get(i).getHarga3()
        );

        viewHolder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(i).setJumlahMasukKeranjang(data.get(i).getJumlahMasukKeranjang() + 1);

                if (context instanceof PenjualanBarangActivity) {
                    ((PenjualanBarangActivity) context).onItemBarangClick(data.get(i));
                }

                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void search(String namaBarang, String idKategori) {
        namaBarang = namaBarang.toLowerCase(Locale.getDefault());
        idKategori = idKategori.toLowerCase(Locale.getDefault());
        data.clear();

        if (idKategori.equalsIgnoreCase("semua")) {
            if (namaBarang.length() == 0) {
                data.addAll(dataSementara);
            } else {
                for (int i = 0; i < dataSementara.size(); i++) {
                    if (dataSementara.get(i).getNamaBarang().toLowerCase(Locale.getDefault()).contains(namaBarang)) {
                        data.add(dataSementara.get(i));
                    }
                }
            }
        } else {
            if (namaBarang.length() == 0) {
                for (int i = 0; i < dataSementara.size(); i++) {
                    if (dataSementara.get(i).getIdKategori().toLowerCase(Locale.getDefault()).contains(idKategori)) {
                        data.add(dataSementara.get(i));
                    }
                }
            } else {
                for (int i = 0; i < dataSementara.size(); i++) {
                    if (dataSementara.get(i).getIdKategori().toLowerCase(Locale.getDefault()).contains(idKategori)
                            && dataSementara.get(i).getNamaBarang().toLowerCase(Locale.getDefault()).contains(namaBarang)) {
                        data.add(dataSementara.get(i));
                    }
                }
            }
        }
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivBarang)
        ImageView ivBarang;
        @BindView(R.id.tvNamaBarang)
        MyTextView tvNamaBarang;
        @BindView(R.id.tvStokHarga)
        MyTextView tvStokHarga;
        @BindView(R.id.ivMasukKeranjang)
        ImageView ivMasukKeranjang;
        @BindView(R.id.ll_parent)
        LinearLayout llParent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
