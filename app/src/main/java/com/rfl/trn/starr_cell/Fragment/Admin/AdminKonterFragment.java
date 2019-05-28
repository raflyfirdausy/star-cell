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
import android.widget.LinearLayout;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rfl.trn.starr_cell.Activity.DaftarKonterActivity;
import com.rfl.trn.starr_cell.Adapter.AdapterListKonter;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Model.KonterModel;
import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminKonterFragment extends Fragment {


    @BindView(R.id.ll_belum_da_konter)
    LinearLayout rlBelumadaKonter;
    @BindView(R.id.fab_tambahKonter)
    FloatingActionButton fabTambahKonter;
    Unbinder unbinder;
    @BindView(R.id.rv_konter)
    RecyclerView rvKonter;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private List<KonterModel> list = new ArrayList<>();
    private AdapterListKonter adapterListKonter;

    public AdminKonterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_konter, container, false);
        unbinder = ButterKnife.bind(this, view);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getDataKonter();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabTambahKonter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DaftarKonterActivity.class));
            }
        });


    }

    private void getDataKonter() {
        databaseReference.child("konter")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        KonterModel konterModel = null;
                        list.clear();
                        if (dataSnapshot.exists()) {
                            rlBelumadaKonter.setVisibility(View.GONE);
                            rvKonter.setVisibility(View.VISIBLE);

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                konterModel = ds.getValue(KonterModel.class);
                                Objects.requireNonNull(konterModel).setKey(ds.getKey());
                                list.add(konterModel);
                            }

                            adapterListKonter = new AdapterListKonter(getActivity(), list);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            rvKonter.setLayoutManager(layoutManager);
                            rvKonter.setAdapter(adapterListKonter);
                        } else {
                            //TODO : belum ada data konter
                            rlBelumadaKonter.setVisibility(View.VISIBLE);
                            rvKonter.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        new Bantuan(getActivity()).swal_error(databaseError.getMessage());
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
