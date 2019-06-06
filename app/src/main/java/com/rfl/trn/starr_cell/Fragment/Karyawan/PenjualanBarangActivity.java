package com.rfl.trn.starr_cell.Fragment.Karyawan;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rfl.trn.starr_cell.Adapter.AdapterCurrentKaryawan;
import com.rfl.trn.starr_cell.Adapter.AdapterKategoriPenjualan;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Model.KategoriModel;
import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PenjualanBarangActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvKategori)
    ShimmerRecyclerView rvKategori;
    private Context context = PenjualanBarangActivity.this;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAnalytics firebaseAnalytics;
    private List<KategoriModel> listKategori = new ArrayList<>();
    private AdapterKategoriPenjualan adapterKategoriPenjualan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_barang);
        ButterKnife.bind(this);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.app_name));
        getSupportActionBar().setSubtitle("Transaksi Baru");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getAndSetKategori();
    }

    private void getAndSetKategori() {
        databaseReference.child("kategori")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listKategori.clear();
                        if(dataSnapshot.exists()){
                            listKategori.add(new KategoriModel("semua","semua", new Date().getTime()));
                            for(DataSnapshot data : dataSnapshot.getChildren()){
                             listKategori.add(data.getValue(KategoriModel.class));
                            }
                            adapterKategoriPenjualan = new AdapterKategoriPenjualan(context, listKategori);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,0,false);
                            rvKategori.setLayoutManager(layoutManager);
                            rvKategori.setAdapter(adapterKategoriPenjualan);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        new Bantuan(context).swal_error(databaseError.getMessage());
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_penjualan, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //TODO : Action ketika tombol submit :v
                searchView.clearFocus();
                return true;
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
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_transaksiTambahan:
                new Bantuan(context).swal_sukses("action_transaksiTambahan");
                return true;
            case R.id.action_simpanTransaksi:
                new Bantuan(context).swal_sukses("action_simpanTransaksi");
                return true;
            case R.id.action_clearTransaksi:
                new Bantuan(context).swal_sukses("action_clearTransaksi");
                return true;
            case R.id.action_daftarPesanan:
                new Bantuan(context).swal_sukses("action_daftarPesanan");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
