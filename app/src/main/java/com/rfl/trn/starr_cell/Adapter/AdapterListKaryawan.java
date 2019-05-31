package com.rfl.trn.starr_cell.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rfl.trn.starr_cell.Activity.DaftarKonterActivity;
import com.rfl.trn.starr_cell.Activity.DetailKaryawanActivity;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Model.KaryawanModel;
import com.rfl.trn.starr_cell.Model.KonterModel;
import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterListKaryawan extends RecyclerView.Adapter<AdapterListKaryawan.MyViewHolder> {
    private Context context;
    private List<KaryawanModel> data;
    private List<KaryawanModel> dataSementara;
    private List<String> key;

    public AdapterListKaryawan(Context context, List<KaryawanModel> data, List<String> key) {
        this.context = context;
        this.data = data;
        this.key = key;
        this.dataSementara = new ArrayList<>();
        this.dataSementara.addAll(data);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_karyawan, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.setDataKeView(data.get(i), key.get(i));
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
        private void pindahActivity(KonterModel konterModel, String jenis) {
            Intent intent = new Intent(context, DaftarKonterActivity.class);
            intent.putExtra("jenis", jenis);
            intent.putExtra("key", konterModel.getKey());
            intent.putExtra("namaKonter", konterModel.getNamaKonter());
            intent.putExtra("alamatKonter", konterModel.getAlamatKonter());
            intent.putExtra("emailKonter", konterModel.getEmailKonter());
            intent.putExtra("passwordKonter", konterModel.getPassword());
            if (konterModel.getUrl_foto() != null) {
                intent.putExtra("urlFoto", konterModel.getUrl_foto());
            }
            context.startActivity(intent);
        }

        void setDataKeView(final KaryawanModel isiData, final String s) {
            tvNamaKaryawan.setText(isiData.getNamaKarywan());
            tvNoHpKaryawan.setText(String.valueOf(isiData.getNomerHp()));
            llParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, DetailKaryawanActivity.class)
                            .putExtra("id", s));

                }
            });
            llParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String[] listItem = new String[]{"Lihat Data Konter", "Edit Data Konter", "Ubah Password", "Hapus konter " , "Batal"};
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(context);
                    builder.setTitle("Pilih Aksi Untuk " + isiData.getNamaKarywan())
                            .setItems(listItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {

                                    } else if (which == 1) {
                                       // pindahActivity(konterModel, "edit");
                                    } else if (which == 2) {
                                        //pindahActivity(konterModel, "password");
                                    } else if (which == 3) {
                                       // hapusKonter(konterModel);
                                    }
                                }
                            })
                            .setCancelable(true)
                            .create()
                            .show();
                }
            });
        }
    }

    public void cariKaryawan(String text) {
        text = text.toLowerCase(Locale.getDefault());
        data.clear();
        if (text.length() == 0) {
            data.addAll(dataSementara);
        } else {
            for (int i = 0; i < dataSementara.size(); i++) {
                if (dataSementara.get(i).getNamaKarywan().toLowerCase(Locale.getDefault()).contains(text)) {
                    data.add(dataSementara.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
}
