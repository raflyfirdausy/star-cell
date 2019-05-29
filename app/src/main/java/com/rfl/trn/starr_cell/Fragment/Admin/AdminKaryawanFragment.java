package com.rfl.trn.starr_cell.Fragment.Admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rfl.trn.starr_cell.Activity.TambahKaryawanActivity;
import com.rfl.trn.starr_cell.Adapter.AdapterListKaryawan;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Model.KaryawanModel;
import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminKaryawanFragment extends Fragment {


    @BindView(R.id.iv_karyawan)
    ImageView ivKaryawan;
    @BindView(R.id.tv_namaKonter)
    MyTextView tvNamaKonter;
    @BindView(R.id.tv_detailKaryawan)
    MyTextView tvDetailKaryawan;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.bg_noDataKaryawan)
    LinearLayout bgNoDataKaryawan;
    @BindView(R.id.ll_belum_da_konter)
    LinearLayout llBelumDaKonter;
    @BindView(R.id.fab_tambahKaryawan)
    FloatingActionButton fabTambahKaryawan;
    Unbinder unbinder;
    @BindView(R.id.rv_karyawan)
    RecyclerView rvKaryawan;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private List<KaryawanModel> list = new ArrayList<>();
    private List<String> listKey = new ArrayList<>();
    private AdapterListKaryawan adapterListKaryawan;


    public AdminKaryawanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_karyawan, container, false);
        unbinder = ButterKnife.bind(this, view);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getKaryawan();
        return view;
    }

    //TODO :: Fetch Data
    private void getKaryawan() {
        databaseReference.child("karyawan")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            bgNoDataKaryawan.setVisibility(View.GONE);
                            list.clear();
                            listKey.clear();
                            KaryawanModel model;
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                model = data.getValue(KaryawanModel.class);
                                String key = data.getKey();
                                list.add(model);
                                listKey.add(key);

                            }
                            adapterListKaryawan = new AdapterListKaryawan(getActivity(), list,listKey);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            rvKaryawan.setLayoutManager(layoutManager);
                            rvKaryawan.setAdapter(adapterListKaryawan);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    //TODO :: Bind(OnCLick dll)
    @OnClick(R.id.fab_tambahKaryawan)
    void tambahKaryawan() {
        startActivity(new Intent(getActivity(), TambahKaryawanActivity.class));
    }

    //TODO :: LifeCycle
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
