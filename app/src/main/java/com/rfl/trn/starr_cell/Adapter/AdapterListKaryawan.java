package com.rfl.trn.starr_cell.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rfl.trn.starr_cell.Activity.DetailKaryawanActivity;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Model.KaryawanModel;
import com.rfl.trn.starr_cell.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdapterListKaryawan extends RecyclerView.Adapter<AdapterListKaryawan.MyViewHolder> {
    private Context context;
    private List<KaryawanModel> data;
    private List<String> key;

    public AdapterListKaryawan(Context context, List<KaryawanModel> data, List<String> key) {
        this.context = context;
        this.data = data;
        this.key = key;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_karyawan, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        myViewHolder.setDataKeView(data.get(i),key.get(i));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_karyawan)
        ImageView ivKaryawan;
        @BindView(R.id.tv_namaKaryawan)
        MyTextView tvNamaKaryawan;
        @BindView(R.id.tv_noHpKaryawan)
        MyTextView tvNoHpKaryawan;
        @BindView(R.id.ll_parent)
        LinearLayout llParent;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void setDataKeView(KaryawanModel isiData,final String s) {
            tvNamaKaryawan.setText(isiData.getNamaKarywan());
            tvNoHpKaryawan.setText(String.valueOf(isiData.getNomerHp()));

            llParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,DetailKaryawanActivity.class)
                    .putExtra("id",s));
                }
            });


        }

    }

}
