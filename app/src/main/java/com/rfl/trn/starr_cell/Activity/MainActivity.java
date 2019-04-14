package com.rfl.trn.starr_cell.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.rfl.trn.starr_cell.Fragment.Admin.AdminBarangFragment;
import com.rfl.trn.starr_cell.Fragment.Admin.AdminDashboardFragment;
import com.rfl.trn.starr_cell.Fragment.Admin.AdminKaryawanFragment;
import com.rfl.trn.starr_cell.Fragment.Admin.AdminKonterFragment;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    boolean tekanKembaliUntukKeluar = false;


    private Context context = MainActivity.this;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Star Cell");
        getSupportActionBar().setSubtitle(getString(R.string.dashboard));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        navView.getMenu().getItem(0).setChecked(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_content, new AdminDashboardFragment(), "dasboard_admin")
                .commit();

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (tekanKembaliUntukKeluar == false) {
                new Bantuan(context).toastLong("Tekan sekali lagi untuk keluar");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tekanKembaliUntukKeluar = true;

                    }
                }, 1000);
            } else {
                tekanKembaliUntukKeluar = false;
                super.onBackPressed();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        FragmentTransaction FT = getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        if (id == R.id.nav_dashboard) {
            FT.replace(R.id.fl_content, new AdminDashboardFragment(), "admin_dashboard").commit();
        } else if (id == R.id.nav_konter) {
            FT.replace(R.id.fl_content, new AdminKonterFragment(), "admin_konter").commit();
        } else if (id == R.id.nav_karyawan) {
            FT.replace(R.id.fl_content, new AdminKaryawanFragment(), "admin_karyawan").commit();
        } else if (id == R.id.nav_barang) {
            FT.replace(R.id.fl_content, new AdminBarangFragment(), "admin_barang").commit();
        } else if (id == R.id.nav_logout) {
//            new Bantuan(context).alertDialogInformasi("Coming Soon !");
            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Peringatan")
                    .setContentText("Apakah kamu ingin logout dari aplikasi ?")
                    .setConfirmText("YA")
                    .setCancelText("TIDAK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            firebaseAuth.signOut();
                            sDialog
                                    .setTitleText("Berhasil Logout !")
                                    .setConfirmText("OK")
                                    .showContentText(false)
                                    .setConfirmClickListener(null)
                                    .showCancelButton(false)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            startActivity(new Intent(context, LoginActivity.class));
                                            finish();
                                        }
                                    })
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        }
                    })
                    .show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
