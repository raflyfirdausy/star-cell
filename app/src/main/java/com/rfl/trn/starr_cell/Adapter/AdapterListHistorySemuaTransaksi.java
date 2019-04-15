package com.rfl.trn.starr_cell.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class AdapterListHistorySemuaTransaksi extends RecyclerView.Adapter<AdapterListHistorySemuaTransaksi.MyViewHolder> {
    private Context context;
    private List<String> data;

    public AdapterListHistorySemuaTransaksi(Context context, List<String> data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_semua_history,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final List<String> isiData = data;

        myViewHolder.setDataKewView(isiData);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setDataKewView(List<String> isiData) {
        }
    }
}
