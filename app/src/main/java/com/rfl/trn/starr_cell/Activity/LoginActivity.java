package com.rfl.trn.starr_cell.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.rfl.trn.starr_cell.Custom.MyEditText;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private Context context = LoginActivity.this;

    @BindView(R.id.myet_email)
    MyEditText myetEmail;
    @BindView(R.id.myet_password)
    MyEditText myetPassword;
    @BindView(R.id.btn_login)
    MyTextView btnLogin;

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
    void login(){
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
                        public void onSuccess(AuthResult authResult) {
                            loading.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            loading.showContentText(false);
                            loading.setTitleText("Berhasil Login");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loading.dismissWithAnimation();
                                    //TODO : pindah activity ke main
                                    startActivity(new Intent(context, MainActivity.class));
                                    finish();
                                }
                            }, 1000);
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
}
