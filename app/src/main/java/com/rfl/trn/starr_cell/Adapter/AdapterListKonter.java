package com.rfl.trn.starr_cell.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.rfl.trn.starr_cell.Activity.DaftarKonterActivity;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Model.KonterModel;
import com.rfl.trn.starr_cell.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterListKonter extends RecyclerView.Adapter<AdapterListKonter.MyViewHolder> {

    @BindView(R.id.iv_karyawan)
    ImageView ivKonter;
    @BindView(R.id.tv_namaKonter)
    MyTextView tvNamaKonter;
    @BindView(R.id.tv_alamatKonter)
    MyTextView tvAlamatKonter;
    @BindView(R.id.ll_parent)
    CardView llParent;
    private Context context;
    private List<KonterModel> data;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    public AdapterListKonter(Context context, List<KonterModel> data) {
        this.context = context;
        this.data = data;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_konter, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(i);
        myViewHolder.setData(data.get(i), color);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_karyawan)
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

        void setData(final KonterModel konterModel, int color) {
            tvNamaKonter.setText(konterModel.getNamaKonter());
            tvAlamatKonter.setText(konterModel.getAlamatKonter());
            String firstLetter = String.valueOf(konterModel.getNamaKonter().charAt(0));
            TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color);
            ivKonter.setImageDrawable(drawable);
            llParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String[] listItem = new String[]{"Lihat Data Konter", "Edit Data Konter", "Ubah Password", "Hapus Data " + konterModel.getNamaKonter(), "Batal"};
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(context);
                    builder.setTitle("Pilih Aksi Untuk " + konterModel.getNamaKonter())
                            .setItems(listItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (which == 0) {
                                        final SweetAlertDialog detail = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Detail Data " + konterModel.getNamaKonter())
                                                .setContentText(
                                                        "Nama Konter : " + konterModel.getNamaKonter() + "\n" +
                                                                "Email Konter : " + konterModel.getEmailKonter() + "\n" +
                                                                "Alamat Konter : " + konterModel.getAlamatKonter()
                                                );
                                        detail.show();
                                    } else if (which == 1) {
                                        pindahActivity(konterModel, "edit");
                                    } else if (which == 2) {
                                        pindahActivity(konterModel, "password");
                                    } else if (which == 3) {

                                        SweetAlertDialog tanya = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText("Peringatan")
                                                .setContentText("Apakah kamu yakin akan menghapus " +
                                                        konterModel.getNamaKonter()
                                                        + " ?\nSemua Data pada konter tersebut akan di hapus secara permanent. " +
                                                        "Data yang telah di hapus tidak dapat di kembalikan")
                                                .setConfirmText("Yakin")
                                                .setCancelText("Batal")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        //TODO : hapus konter
                                                        sweetAlertDialog.dismissWithAnimation();
                                                        new Bantuan(context).swal_warning("sabar mamank");
//                                                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                                        AuthCredential credential = EmailAuthProvider
//                                                                .getCredential(konterModel.getEmailKonter(), konterModel.getPassword());
//
//                                                        Objects.requireNonNull(user).reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<Void> task) {
//                                                                if (task.isSuccessful()) {
//                                                                    user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                                        @Override
//                                                                        public void onSuccess(Void aVoid) {
//                                                                            //TODO: Hapus data di firebase database
//                                                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//                                                                            databaseReference.child("konter").child(konterModel.getKey()).setValue(null)
//                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                                                        @Override
//                                                                                        public void onSuccess(Void aVoid) {
//                                                                                            new Bantuan(context).swal_sukses(konterModel.getNamaKonter() + " berhasil Di Hapus !");
//                                                                                        }
//                                                                                    }).addOnFailureListener(new OnFailureListener() {
//                                                                                @Override
//                                                                                public void onFailure(@NonNull Exception e) {
//                                                                                    new Bantuan(context).swal_error(konterModel.getNamaKonter() + " Gagal di hapus dari database !\n" + e.getMessage());
//                                                                                }
//                                                                            });
//                                                                        }
//                                                                    }).addOnFailureListener(new OnFailureListener() {
//                                                                        @Override
//                                                                        public void onFailure(@NonNull Exception e) {
//                                                                            new Bantuan(context).swal_error("Erorr hapus data : " + e.getMessage());
//                                                                        }
//                                                                    });
//                                                                } else {
//                                                                    new Bantuan(context).swal_error(Objects.requireNonNull(task.getException()).getMessage());
//                                                                }
//                                                            }
//                                                        });
                                                    }
                                                });
                                        tanya.show();
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

    private void pindahActivity(KonterModel konterModel, String jenis){
        Intent intent = new Intent(context, DaftarKonterActivity.class);
        intent.putExtra("jenis", jenis);
        intent.putExtra("key", konterModel.getKey());
        intent.putExtra("namaKonter", konterModel.getNamaKonter());
        intent.putExtra("alamatKonter", konterModel.getAlamatKonter());
        intent.putExtra("emailKonter", konterModel.getEmailKonter());
        intent.putExtra("passwordKonter", konterModel.getPassword());
        if(konterModel.getUrl_foto() != null){
            intent.putExtra("urlFoto", konterModel.getUrl_foto());
        }
        context.startActivity(intent);
    }
}
