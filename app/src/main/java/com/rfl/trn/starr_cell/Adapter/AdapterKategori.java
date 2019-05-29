package com.rfl.trn.starr_cell.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Interface.IDialog;
import com.rfl.trn.starr_cell.Model.KategoriModel;
import com.rfl.trn.starr_cell.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterKategori extends RecyclerView.Adapter<AdapterKategori.MyViewHolder> {
    List<KategoriModel> data;
    Context context;
    IDialog listener;


    public AdapterKategori(List<KategoriModel> data, Context context, IDialog IDialog) {
        this.data = data;
        this.context = context;
        this.listener = IDialog;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_list_dialog_kategori_konter, viewGroup, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.setData(data.get(i));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_namaKategori)
        MyTextView tvNamaKategori;
        @BindView(R.id.ll_parent)
        LinearLayout llParent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        void setData(final KategoriModel s) {
            tvNamaKategori.setText(s.getNamaKategori());

            llParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(s.getIdKategroi(),s.getNamaKategori(),true);
                }
            });
        }
    }
}
