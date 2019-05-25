package com.rfl.trn.starr_cell.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Model.KonterModel;
import com.rfl.trn.starr_cell.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterListKonter extends RecyclerView.Adapter<AdapterListKonter.MyViewHolder> {

    @BindView(R.id.iv_konter)
    ImageView ivKonter;
    @BindView(R.id.tv_namaKonter)
    MyTextView tvNamaKonter;
    @BindView(R.id.tv_alamatKonter)
    MyTextView tvAlamatKonter;
    @BindView(R.id.ll_parent)
    CardView llParent;
    private Context context;
    private List<KonterModel> data;


    public AdapterListKonter(Context context, List<KonterModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_konter, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(i);
        myViewHolder.setData(data.get(i),color);
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
        @BindView(R.id.tv_alamatKonter)
        TextView tvAlamatKonter;
        @BindView(R.id.ll_parent)
        LinearLayout llParent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(final KonterModel konterModel, int color){
            tvNamaKonter.setText(konterModel.getNamaKonter());
            tvAlamatKonter.setText(konterModel.getAlamatKonter());

            String firstLetter = String.valueOf(konterModel.getNamaKonter().charAt(0));


            TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color);
            ivKonter.setImageDrawable(drawable);

            llParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Bantuan(context).swal_sukses(konterModel.getKey());
                }
            });
        }
    }
}
