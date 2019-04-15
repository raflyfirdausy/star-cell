package com.rfl.trn.starr_cell.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterListKonter extends RecyclerView.Adapter<AdapterListKonter.MyViewHolder> {

    private Context context;
    private List<String> data ;


    public AdapterListKonter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_konter, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        //populasikan datane disini

        final List<String> isiData = data;

        myViewHolder.setDataKewView(isiData);

    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_konter)
        ImageView ivKonter;
        @BindView(R.id.tv_namaKonter)
        TextView tvNamaKonter;
        @BindView(R.id.tv_detailKonter)
        TextView tvDetailKonter;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

        public void setDataKewView(List<String> isiData) {
        }
    }
}
