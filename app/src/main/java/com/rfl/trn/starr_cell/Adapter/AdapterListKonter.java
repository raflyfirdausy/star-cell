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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
import java.util.Objects;

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
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseAuth firebaseAuth, firebaseAuth2;


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

    private void lihatDataKonter(KonterModel konterModel) {
        final SweetAlertDialog detail = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Detail Data " + konterModel.getNamaKonter())
                .setContentText(
                        "Nama Konter : " + konterModel.getNamaKonter() + "\n" +
                                "Email Konter : " + konterModel.getEmailKonter() + "\n" +
                                "Alamat Konter : " + konterModel.getAlamatKonter()
                );
        detail.show();
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

    private void hapusKonter(final KonterModel konterModel) {
        SweetAlertDialog tanya = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Peringatan")
                .setContentText("Apakah kamu yakin akan menghapus " +
                        konterModel.getNamaKonter()
                        + " ?\nSemua Data pada konter tersebut akan di hapus secara permanent. " +
                        "Data yang telah di hapus tidak dapat di kembalikan")
                .setConfirmText("Ya, Hapus")
                .setCancelText("Batal")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        //TODO : hapus konter
                        sweetAlertDialog.dismissWithAnimation();
                        prosesHapusKonter(konterModel);
                    }
                });
        tanya.show();
    }

    private void prosesHapusKonter(final KonterModel konterModel) {
        final SweetAlertDialog loading = new Bantuan(context).swal_loading("Tunggu beberapa saat, proses hapus data konter");
        loading.show();
        FirebaseOptions firebaseOptions = new Bantuan(context).getFirebaseOptions();

        try {
            FirebaseApp firebaseApp = FirebaseApp.initializeApp(context.getApplicationContext(),
                    firebaseOptions,
                    context.getString(R.string.app_name));
            firebaseAuth2 = FirebaseAuth.getInstance(firebaseApp);
        } catch (IllegalStateException e) {
            firebaseAuth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance(context.getString(R.string.app_name)));
        }

        firebaseAuth2.signInWithEmailAndPassword(konterModel.getEmailKonter(), konterModel.getPassword())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        final FirebaseUser firebaseUser = firebaseAuth2.getCurrentUser();
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(konterModel.getEmailKonter(), konterModel.getPassword());
                        Objects.requireNonNull(firebaseUser).reauthenticate(credential)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                firebaseUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //TODO : hapus di database
                                        databaseReference.child("konter")
                                                .child(konterModel.getKey())
                                                .setValue(null)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        //TODO : hapus data neng firebase storage e urung :v erorr nek hapus folder
                                                        loading.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                        loading.showContentText(true);
                                                        loading.setTitleText("Sukses");
                                                        loading.setContentText("Berhasil menghapus data konter");
                                                        firebaseAuth2.signOut();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                new Bantuan(context).swal_error(e.getMessage());
                                                loading.dismissWithAnimation();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        new Bantuan(context).swal_error("Error hapus akun : " + e.getMessage());
                                        loading.dismissWithAnimation();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                new Bantuan(context).swal_error("Error reauthenticate : " + e.getMessage());
                                loading.dismissWithAnimation();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new Bantuan(context).swal_error(e.getMessage());
                loading.dismissWithAnimation();
            }
        });
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
                    final String[] listItem = new String[]{"Lihat Data Konter", "Edit Data Konter", "Ubah Password", "Hapus konter " + konterModel.getNamaKonter(), "Batal"};
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(context);
                    builder.setTitle("Pilih Aksi Untuk " + konterModel.getNamaKonter())
                            .setItems(listItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        lihatDataKonter(konterModel);
                                    } else if (which == 1) {
                                        pindahActivity(konterModel, "edit");
                                    } else if (which == 2) {
                                        pindahActivity(konterModel, "password");
                                    } else if (which == 3) {
                                        hapusKonter(konterModel);
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
}
