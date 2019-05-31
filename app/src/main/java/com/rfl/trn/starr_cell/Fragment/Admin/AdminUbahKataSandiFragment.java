package com.rfl.trn.starr_cell.Fragment.Admin;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.rfl.trn.starr_cell.Custom.MyEditText;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.R;

import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminUbahKataSandiFragment extends Fragment {


    @BindView(R.id.myet_kataSandiLama)
    MyEditText myetKataSandiLama;
    @BindView(R.id.myet_kataSandiBaru)
    MyEditText myetKataSandiBaru;
    @BindView(R.id.myet_ulangiKataSandiBaru)
    MyEditText myetUlangiKataSandiBaru;
    @BindView(R.id.tvBtn_simpan)
    MyTextView tvBtnSimpan;
    Unbinder unbinder;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private AuthCredential credential;
    private Date date = new Date();

    public AdminUbahKataSandiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_ubah_kata_sandi, container, false);
        unbinder = ButterKnife.bind(this, view);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tvBtn_simpan)
    public void onViewClicked() {
        prosesUbahKataSandi();
    }

    private void prosesUbahKataSandi() {
        if ((TextUtils.isEmpty(Objects.requireNonNull(myetKataSandiLama.getText()).toString())) ||
                (TextUtils.isEmpty(Objects.requireNonNull(myetKataSandiBaru.getText()).toString())) ||
                (TextUtils.isEmpty(Objects.requireNonNull(myetUlangiKataSandiBaru.getText()).toString()))) {
            new Bantuan(getActivity()).swal_error("Masih ada data yang kosong!");
        } else if (!myetKataSandiBaru.getText().toString().equals(myetUlangiKataSandiBaru.getText().toString())) {
            new Bantuan(getActivity()).swal_error("Konfirmasi kata sandi baru tidak cocok!");
        } else {
            final SweetAlertDialog loading = new Bantuan(getActivity()).swal_loading("Proses ubah kata sandi");
            loading.show();

            credential = EmailAuthProvider.getCredential(Objects.requireNonNull(
                    Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail()),
                    myetKataSandiLama.getText().toString());

            firebaseAuth.getCurrentUser()
                    .reauthenticate(credential)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            firebaseAuth.getCurrentUser()
                                    .updatePassword(myetKataSandiBaru.getText().toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            databaseReference.child("admin")
                                                    .child(firebaseAuth.getCurrentUser().getUid())
                                                    .child("password")
                                                    .setValue(myetKataSandiBaru.getText().toString())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            databaseReference.child("admin")
                                                                    .child(firebaseAuth.getCurrentUser().getUid())
                                                                    .child("lastPasswordChanged")
                                                                    .setValue(date.getTime())
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            loading.dismissWithAnimation();
                                                                            new Bantuan(getActivity()).toastLong("Kata sandi berhasil diubah");
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    new Bantuan(getActivity()).swal_error("Error saat proses simpan ke database lastpasswordchanged : " + e.getMessage());
                                                                }
                                                            });
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    new Bantuan(getActivity()).swal_error("Error saat proses simpan ke database : " + e.getMessage());
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loading.dismissWithAnimation();
                                    new Bantuan(getActivity()).swal_error("Error saat proses ubah kata sandi : " + e.getMessage());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loading.dismissWithAnimation();
                    new Bantuan(getActivity()).swal_error("Error saat memasukan kata sandi lama : " + e.getMessage());
                }
            });

        }
    }
}
