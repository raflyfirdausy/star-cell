package com.rfl.trn.starr_cell.Fragment.Karyawan;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rfl.trn.starr_cell.ActivityKaryawan.AbsenKaryawanActivity;
import com.rfl.trn.starr_cell.ActivityKaryawan.ListCurrentKaryawanActivity;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AbsensiKaryawanFragment extends Fragment {

    private static final int MAX_TOTAL_CURRENT_KARYAWAN = 2;
    @BindView(R.id.iv_karyawan)
    ImageView ivKaryawan;
    @BindView(R.id.tv_namaKonter)
    MyTextView tvNamaKonter;
    @BindView(R.id.tv_detailKaryawan)
    MyTextView tvDetailKaryawan;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.tv_karyawan)
    TextView tvKaryawan;
    @BindView(R.id.layout_karyawanSaatIni)
    LinearLayout layoutKaryawanSaatIni;
    @BindView(R.id.layout_absenMasuk)
    RelativeLayout layoutAbsenMasuk;
    @BindView(R.id.layout_absenKeluar)
    RelativeLayout layoutAbsenKeluar;
    Unbinder unbinder;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private long totalCurrentKaryawan = 0;
    private List<String> listCurrentKaryawan = new ArrayList<>();

    public AbsensiKaryawanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_absensi_karyawan, container, false);
        unbinder = ButterKnife.bind(this, view);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getTotalCurrentKaryawan();
        setAndgetCurrentKaryawan();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.layout_karyawanSaatIni,
            R.id.layout_absenMasuk,
            R.id.layout_absenKeluar,
            R.id.layout_absenMasukLembur,
            R.id.layout_absenKeluarLembur})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_absenMasuk:
                if (totalCurrentKaryawan < MAX_TOTAL_CURRENT_KARYAWAN) {
                    Intent intent = new Intent(getActivity(), AbsenKaryawanActivity.class);
                    intent.putExtra("jenis", "absenMasukNormal");
                    startActivity(intent);
                } else {
                    new Bantuan(getActivity()).swal_error(getString(R.string.karyawan_lebih_dari_dua));
                }
                break;
            case R.id.layout_absenMasukLembur:
                if (totalCurrentKaryawan < MAX_TOTAL_CURRENT_KARYAWAN) {
                    Intent intent = new Intent(getActivity(), AbsenKaryawanActivity.class);
                    intent.putExtra("jenis", "absenMasukLembur");
                    startActivity(intent);
                } else {
                    new Bantuan(getActivity()).swal_error(getString(R.string.karyawan_lebih_dari_dua));
                }
                break;
            case R.id.layout_absenKeluar:
                startActivity(new Intent(getActivity(), ListCurrentKaryawanActivity.class));
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private void getTotalCurrentKaryawan() {
        databaseReference.child("currentKaryawan")
                .child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        totalCurrentKaryawan = dataSnapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        new Bantuan(getActivity()).swal_error(databaseError.getMessage());
                    }
                });
    }

    private void setAndgetCurrentKaryawan() {
        databaseReference.child("currentKaryawan")
                .child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listCurrentKaryawan.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                databaseReference.child("karyawan")
                                        .child(Objects.requireNonNull(data.child("idKaryawan").getValue(String.class)))
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                listCurrentKaryawan.add(dataSnapshot.child("namaKaryawan").getValue(String.class));
                                                if (tvKaryawan != null) {
                                                    String currentKaryawan = "";
                                                    for (int i = 0; i < listCurrentKaryawan.size(); i++) {
                                                        currentKaryawan += listCurrentKaryawan.get(i) + "\n";
                                                        tvKaryawan.setText(currentKaryawan);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                new Bantuan(getActivity()).swal_error(databaseError.getMessage());
                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        new Bantuan(getActivity()).swal_error(databaseError.getMessage());
                    }
                });
    }
}
