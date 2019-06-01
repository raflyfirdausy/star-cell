package com.rfl.trn.starr_cell.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.rfl.trn.starr_cell.Custom.MyEditText;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.R;
import com.scwang.wave.MultiWaveHeader;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.myet_email)
    MyEditText myetEmail;
    @BindView(R.id.myet_password)
    MyEditText myetPassword;
    @BindView(R.id.btn_login)
    MyTextView btnLogin;
    @BindView(R.id.frame)
    ImageView frame;
    @BindView(R.id.head)
    MyTextView head;
    @BindView(R.id.tv_login)
    MyTextView tvLogin;
    @BindView(R.id.email1)
    TextInputLayout email1;
    @BindView(R.id.password)
    TextInputLayout password;
    @BindView(R.id.remember)
    CheckBox remember;
    @BindView(R.id.forgotpass)
    MyTextView forgotpass;
    @BindView(R.id.linear4)
    LinearLayout linear4;
    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.waveHeader)
    MultiWaveHeader waveHeader;
    @BindView(R.id.tv_loginKaryawan)
    TextView tvLoginKaryawan;
    @BindView(R.id.tv_loginAdmin)
    TextView tvLoginAdmin;
    @BindView(R.id.layout_login)
    LinearLayout layoutLogin;
    private boolean isLoginAdmin = false;
    private Context context = LoginActivity.this;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @OnClick(R.id.btn_login)
    void login() {
        if ((TextUtils.isEmpty(Objects.requireNonNull(myetEmail.getText()).toString())) ||
                (TextUtils.isEmpty(Objects.requireNonNull(myetPassword.getText()).toString()))) {
            new Bantuan(context).swal_warning("Email / Password tidak boleh kosong !");
        } else {
            final SweetAlertDialog loading = new Bantuan(context).swal_loading("Tunggu beberapa saat, proses login");
            loading.show();
            firebaseAuth.signInWithEmailAndPassword(myetEmail.getText().toString(),
                    myetPassword.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(final AuthResult authResult) {
                            loading.dismissWithAnimation();
                            pindahActivity(authResult);
//                            loading.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//                            loading.showContentText(false);
//                            loading.setTitleText("Berhasil Login");
//                            loading.setContentText("Selamat Datang kembali " + authResult.getUser().getEmail());
//                            loading.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    sweetAlertDialog.dismissWithAnimation();
//                                    pindahActivity(authResult);
//                                }
//                            });
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    loading.dismissWithAnimation();
//                                    //TODO : pindah activity ke main
//                                    pindahActivity(authResult);
//                                }
//                            }, 1500);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loading.dismissWithAnimation();
                            new Bantuan(context).swal_error(e.getMessage());
                        }
                    });
        }
    }

    private void pindahActivity(AuthResult authResult) {
        if (isLoginAdmin) {
            databaseReference.child("admin")
                    .child(authResult.getUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                startActivity(new Intent(context, MainActivity.class));
                                finish();
                            } else {
                                new Bantuan(context).swal_error("Admin tidak ditemukan!");
                                firebaseAuth.signOut();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            new Bantuan(context).swal_error(databaseError.getMessage());
                        }
                    });
        } else {
            databaseReference.child("konter")
                    .child(authResult.getUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //TODO : MainActivity Konter
                                SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Peringatan")
                                        .setContentText("MainActivityKonter Coming Soon :p")
                                        .setConfirmText("BODO AMAAT")
                                        .setCancelText("YA TERUUS ??")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                firebaseAuth.signOut();
                                                startActivity(new Intent(context, SplashActivity.class));
                                                sweetAlertDialog.dismissWithAnimation();
                                                finish();
                                            }
                                        }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                firebaseAuth.signOut();
                                                startActivity(new Intent(context, SplashActivity.class));
                                                sweetAlertDialog.dismissWithAnimation();
                                                finish();
                                            }
                                        });
                                dialog.show();
                            } else {
                                firebaseAuth.signOut();
                                new Bantuan(context).swal_error("Karyawan tidak ditemukan!");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            new Bantuan(context).swal_error(databaseError.getMessage());
                        }
                    });
        }
    }

    @OnClick({R.id.tv_loginKaryawan, R.id.tv_loginAdmin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_loginKaryawan:
                setLoginKaryawan();
                break;
            case R.id.tv_loginAdmin:
                setLoginAdmin();
                break;
        }
    }

    private void setLoginKaryawan() {
        isLoginAdmin = false;
        animateLayout();
        frame.setImageResource(R.drawable.ic_karyawan);
        tvLogin.setText(getString(R.string.login_karyawan));
    }

    private void setLoginAdmin() {
        isLoginAdmin = true;
        animateLayout();
        frame.setImageResource(R.drawable.ic_admin);
        tvLogin.setText(getString(R.string.login_admin));
    }

    private void animateLayout() {
        layoutLogin.setAlpha(0f);
        layoutLogin.animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(null);
    }
}
