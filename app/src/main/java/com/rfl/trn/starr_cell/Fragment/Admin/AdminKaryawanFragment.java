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
import com.rfl.trn.starr_cell.Adapter.AdapterListKonter;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Model.KaryawanModel;
import com.rfl.trn.starr_cell.Model.KonterModel;
import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminKaryawanFragment extends Fragment {


    @BindView(R.id.iv_konter)
    ImageView ivKonter;
    @BindView(R.id.tv_namaKonter)
    MyTextView tvNamaKonter;
    @BindView(R.id.tv_detailKonter)
    MyTextView tvDetailKonter;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.ll_belum_da_konter)
    LinearLayout llBelumDaKonter;
    @BindView(R.id.rv_karyawan)
    RecyclerView rvKaryawan;
    @BindView(R.id.fab_tambahKaryawan)
    FloatingActionButton fabTambahKaryawan;
    Unbinder unbinder;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private List<KaryawanModel> list = new ArrayList<>();
    private AdapterListKaryawan adapterListKaryawan;
    private RecyclerView rv;

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
        databaseReference.child("KARYAWAN")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            KaryawanModel model;
                            for (DataSnapshot data: dataSnapshot.getChildren() ){
                                model = data.getValue(KaryawanModel.class);

                                list.add(model);

                            }
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            rv.setLayoutManager(layoutManager);
                           adapterListKaryawan = new AdapterListKaryawan(getActivity(), list);
                           rv.setAdapter(adapterListKaryawan);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabTambahKaryawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TambahKaryawanActivity.class));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
