package com.rfl.trn.starr_cell.ActivityKaryawan;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.rfl.trn.starr_cell.Adapter.AdapterListAbsensi;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Model.AbsenModel;
import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListCurrentKaryawanActivity extends AppCompatActivity {

    @BindView(R.id.iv_karyawan)
    ImageView ivKaryawan;
    @BindView(R.id.tv_judul)
    MyTextView tvJudul;
    @BindView(R.id.tv_detailKaryawan)
    MyTextView tvDetailKaryawan;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.ll_belum_ada_absen_masuk)
    LinearLayout llBelumAdaAbsenMasuk;
    @BindView(R.id.rv_absen)
    ShimmerRecyclerView rvAbsen;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAnalytics firebaseAnalytics;
    private Context context = ListCurrentKaryawanActivity.this;
    private List<AbsenModel> listCurrentKaryawan = new ArrayList<>();
    private AdapterCurrentKaryawan adapterCurrentKaryawan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_current_karyawan);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        getSupportActionBar().setSubtitle(R.string.karyawan_saat_ini);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        getAndSetDataCurrentKaryawan();
    }

    private void getAndSetDataCurrentKaryawan() {
        databaseReference.child("currentKaryawan")
                .child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listCurrentKaryawan.clear();
                        if (dataSnapshot.exists()) {
                            rvAbsen.setVisibility(View.VISIBLE);
                            llBelumAdaAbsenMasuk.setVisibility(View.GONE);
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                databaseReference.child("absen")
                                        .child(Objects.requireNonNull(data.child("idAbsen").getValue(String.class)))
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                AbsenModel absenModel;
                                                if(dataSnapshot.exists()){
                                                    absenModel = dataSnapshot.getValue(AbsenModel.class);
                                                    listCurrentKaryawan.add(absenModel);
//                                                    new Bantuan(context).swal_sukses(String.valueOf(listCurrentKaryawan.get(0).getIdKaryawan()));
                                                    adapterCurrentKaryawan = new AdapterCurrentKaryawan(context, listCurrentKaryawan);
                                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                                                    rvAbsen.setLayoutManager(layoutManager);
                                                    rvAbsen.setAdapter(adapterCurrentKaryawan);
                                                } else {
                                                    new Bantuan(context).swal_error("Data absen tidak di temukan !");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                new Bantuan(context).swal_error("Erorr cek absen karyawan : " + databaseError.getMessage());
                                            }
                                        });
                            }
                        } else {
                            rvAbsen.setVisibility(View.GONE);
                            llBelumAdaAbsenMasuk.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        new Bantuan(context).swal_error("Erorr cek current karyawan : " + databaseError.getMessage());
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
