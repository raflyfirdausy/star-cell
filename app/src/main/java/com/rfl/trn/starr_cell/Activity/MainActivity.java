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
import android.widget.SearchView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.rfl.trn.starr_cell.Fragment.Admin.AdminAbsensiFragment;
import com.rfl.trn.starr_cell.Fragment.Admin.AdminBarangFragment;
import com.rfl.trn.starr_cell.Fragment.Admin.AdminDashboardFragment;
import com.rfl.trn.starr_cell.Fragment.Admin.AdminKaryawanFragment;
import com.rfl.trn.starr_cell.Fragment.Admin.AdminKonterFragment;
import com.rfl.trn.starr_cell.Fragment.Admin.AdminLaporanFragment;
import com.rfl.trn.starr_cell.Fragment.Admin.AdminPengaturanFragment;
import com.rfl.trn.starr_cell.Fragment.Admin.AdminTransaksiFragment;
import com.rfl.trn.starr_cell.Fragment.Admin.AdminUbahKataSandiFragment;
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
    private boolean tekanKembaliUntukKeluar = false;

    private Context context = MainActivity.this;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAnalytics mFirebaseAnalytics;
    private String instanceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.app_name));
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

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                instanceId = instanceIdResult.getToken();

                                    databaseReference
                                            .child("admin")
                                            .child(user.getUid())
                                            .child("instanceId")
                                            .setValue(instanceId);

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (!tekanKembaliUntukKeluar) {
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
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //TODO : Action ketika tombol submit :v
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //TODO : Action ketika textnya berubah seperti dia yang sekarang bukan lagi yang dulu :(
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        FragmentTransaction FT = getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        if (id == R.id.nav_dashboard) {
            Objects.requireNonNull(getSupportActionBar()).setSubtitle(getString(R.string.dashboard));
            FT.replace(R.id.fl_content, new AdminDashboardFragment(), "admin_dashboard").commit();
        } else if (id == R.id.nav_konter) {
            Objects.requireNonNull(getSupportActionBar()).setSubtitle(getString(R.string.konter));
            FT.replace(R.id.fl_content, new AdminKonterFragment(), "admin_konter").commit();
        } else if (id == R.id.nav_karyawan) {
            Objects.requireNonNull(getSupportActionBar()).setSubtitle(getString(R.string.karyawan));
            FT.replace(R.id.fl_content, new AdminKaryawanFragment(), "admin_karyawan").commit();
        } else if (id == R.id.nav_barang) {
            Objects.requireNonNull(getSupportActionBar()).setSubtitle(getString(R.string.barang));
            FT.replace(R.id.fl_content, new AdminBarangFragment(), "admin_barang").commit();
        } else if (id == R.id.nav_absensi) {
            Objects.requireNonNull(getSupportActionBar()).setSubtitle(getString(R.string.absensi));
            FT.replace(R.id.fl_content, new AdminAbsensiFragment(), "admin_absensi").commit();
        } else if (id == R.id.nav_transaksi) {
            Objects.requireNonNull(getSupportActionBar()).setSubtitle(getString(R.string.transaksi));
            FT.replace(R.id.fl_content, new AdminTransaksiFragment(), "admin_transaksi").commit();
        } else if (id == R.id.nav_Pengaturan){
            Objects.requireNonNull(getSupportActionBar()).setSubtitle(getString(R.string.Pengaturan));
            FT.replace(R.id.fl_content, new AdminPengaturanFragment(),"admin_pengaturan").commit();
        }else if (id == R.id.nav_laporan){
            Objects.requireNonNull(getSupportActionBar()).setSubtitle(getString(R.string.Pengaturan));
            FT.replace(R.id.fl_content, new AdminLaporanFragment(),"admin_laporan").commit();
        }else if (id == R.id.nav_logout) {
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
                                    .setContentText("Klik OK Untuk ke halaman login")
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
        } else if(id == R.id.nav_ubahKataSandi){
            Objects.requireNonNull(getSupportActionBar()).setSubtitle(getString(R.string.ubah_kata_sandi));
            FT.replace(R.id.fl_content, new AdminUbahKataSandiFragment(), "admin_ubahKataSandi").commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
