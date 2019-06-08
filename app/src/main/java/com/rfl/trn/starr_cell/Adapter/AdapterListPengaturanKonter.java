package com.rfl.trn.starr_cell.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.rfl.trn.starr_cell.Activity.PengaturanKonterActivity;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Model.KonterModel;
import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterListPengaturanKonter extends RecyclerView.Adapter<AdapterListPengaturanKonter.MyViewHolder> {


    private Context context;
    private List<KonterModel> data;
    private List<KonterModel> dataSementara;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseAuth firebaseAuth, firebaseAuth2;


    public AdapterListPengaturanKonter(Context context, List<KonterModel> data) {
        this.context = context;
        this.data = data;
        this.dataSementara = new ArrayList<>();
        this.dataSementara.addAll(data);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_konter_new, viewGroup, false);

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

    public void cariKonter(String text) {
        text = text.toLowerCase(Locale.getDefault());
        data.clear();
        if (text.length() == 0) {
            data.addAll(dataSementara);
        } else {
            for (int i = 0; i < dataSementara.size(); i++) {
                if (dataSementara.get(i).getNamaKonter().toLowerCase(Locale.getDefault()).contains(text)) {
                    data.add(dataSementara.get(i));
                }
            }
        }
        notifyDataSetChanged();
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
        @BindView(R.id.rl_item_parent)
        RelativeLayout rlItemParent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

       public void setData(final KonterModel konterModel, final int color) {
            tvNamaKonter.setText(konterModel.getNamaKonter());
            tvAlamatKonter.setText(konterModel.getAlamatKonter());
            String firstLetter = String.valueOf(konterModel.getNamaKonter().charAt(0));
            TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color);
            ivKonter.setImageDrawable(drawable);
            rlItemParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, PengaturanKonterActivity.class));
                }
            });

        }
    }
}
