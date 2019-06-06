package com.rfl.trn.starr_cell.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Model.KategoriModel;
import com.rfl.trn.starr_cell.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterKategoriPenjualan extends RecyclerView.Adapter<AdapterKategoriPenjualan.ViewHolder> {
    List<KategoriModel> data;
    Context context;
    int index = 0;

    public AdapterKategoriPenjualan(Context context, List<KategoriModel> data) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_kategori_penjualan, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.cardviewParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = i;
                notifyDataSetChanged();
            }
        });
        if(index == i){
            viewHolder.cardviewParent.setCardBackgroundColor(Color.parseColor("#185E8B"));
        } else {
            viewHolder.cardviewParent.setCardBackgroundColor(Color.parseColor("#3498db"));
        }
        viewHolder.tvKategori.setText(data.get(i).getNamaKategori());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    static
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_kategori)
        MyTextView tvKategori;
        @BindView(R.id.cardviewParent)
        CardView cardviewParent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @SuppressLint("ResourceAsColor")
        public void setDataKewView(final Context context, final KategoriModel data, int position) {
//            cardviewParent.setCardBackgroundColor(Color.parseColor("#DB3434"));
            tvKategori.setText(data.getNamaKategori());
            cardviewParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Bantuan(context).toastShort(data.getNamaKategori());
                }
            });
        }
    }
}
