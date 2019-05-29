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
import com.rfl.trn.starr_cell.Model.KonterModel;
import com.rfl.trn.starr_cell.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterDialogKonter extends RecyclerView.Adapter<AdapterDialogKonter.MyViewHolder> {
    Context context;
    List<KonterModel> data;
    List<String> idKOnter ;
    IDialog listener;



    public AdapterDialogKonter(Context context, List<KonterModel> data, List<String> listIdKonter, IDialog IDialog) {
        this.context = context;
        this.data = data;
        this.idKOnter = listIdKonter;
        this.listener = IDialog;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_dialog_kategori_konter, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.setData(data.get(i),idKOnter.get(i));
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

        public void setData(final KonterModel konterModel, final String s) {
            tvNamaKategori.setText(konterModel.getNamaKonter());
            llParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(s,konterModel.getNamaKonter(),true);
                }
            });
        }
    }

}
