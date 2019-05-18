package com.rfl.trn.starr_cell.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rfl.trn.starr_cell.Helper.Internet;
import com.rfl.trn.starr_cell.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        boolean cekKoneksi = new Internet().CekKoneksi(context);
        if (cekKoneksi) {
            Timer timer = new Timer();
            timer.schedule(new Splash(), 1000);
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
                startActivity(new Intent(context, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(context, LoginActivity.class));
                finish();
            }
        }
    }
}
