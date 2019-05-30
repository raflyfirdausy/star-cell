package com.rfl.trn.starr_cell.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.rfl.trn.starr_cell.BuildConfig;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Helper.Internet;
import com.rfl.trn.starr_cell.R;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    private Context context = SplashActivity.this;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAnalytics mFirebaseAnalytics;
    private boolean terupdate;
    private String LINK_DOWNLOAD;
    private String VERSI_TERBARU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        boolean cekKoneksi = new Internet().CekKoneksi(context);
        if (cekKoneksi) {

            databaseReference.child("BUILD_CONFIG")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                LINK_DOWNLOAD = dataSnapshot.child("LINK_DOWNLOAD").getValue(String.class);
                                VERSI_TERBARU = dataSnapshot.child("VERSION_NAME").getValue(String.class);
                                terupdate = Objects.requireNonNull(dataSnapshot.child("VERSION_CODE").getValue(String.class))
                                        .equalsIgnoreCase(String.valueOf(BuildConfig.VERSION_CODE)) &&
                                        Objects.requireNonNull(dataSnapshot.child("VERSION_NAME").getValue(String.class))
                                                .equalsIgnoreCase(BuildConfig.VERSION_NAME);
                            }

                            final Timer timer = new Timer();
                            if (terupdate) {
                                timer.schedule(new Splash(), 1000);
                            } else {
                                SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Peringatan")
                                        .setContentText("Anda masih menggunakan versi aplikasi yang lama (versi "
                                                + BuildConfig.VERSION_NAME + ") silahkan update aplikasi ke " +
                                                "versi yang terbaru (versi " + VERSI_TERBARU + ")")
                                        .setConfirmText("Update Sekarang")
                                        .setCancelText("Nanti Saja")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(LINK_DOWNLOAD)));
                                                sweetAlertDialog.dismissWithAnimation();
                                                finish();
                                            }
                                        }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                timer.schedule(new Splash(), 1000);
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        });
                                dialog.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            new Bantuan(context).swal_error(databaseError.getMessage());
                        }
                    });


        } else {
            Snackbar.make(coordinator, R.string.tidak_ada_koneksi, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.coba_lagi, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(context, SplashActivity.class));
                            finish();
                        }
                    })
                    .show();
        }
    }

    class Splash extends TimerTask {
        @Override
        public void run() {
            if (firebaseAuth.getCurrentUser() != null) {
                pindahActivity();
            } else {
                startActivity(new Intent(context, LoginActivity.class));
                finish();
            }
        }
    }

    private void pindahActivity() {
        databaseReference.child("admin")
                .child(firebaseAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            startActivity(new Intent(context, MainActivity.class));
                            finish();
                        } else {
                            databaseReference.child("konter")
                                    .child(firebaseAuth.getCurrentUser().getUid())
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
                                                new Bantuan(context).swal_error(firebaseAuth.getCurrentUser().getUid() + "\nTidak ada dalam database");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            new Bantuan(context).swal_error(databaseError.getMessage());
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        new Bantuan(context).swal_error(databaseError.getMessage());
                    }
                });
    }
}
