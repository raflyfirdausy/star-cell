package com.rfl.trn.starr_cell.Fragment.Admin;

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

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rfl.trn.starr_cell.Adapter.AdapterListAbsensi;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Model.AbsenModel;
import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TabAdminAbsenKeluarFragment extends Fragment {



    @BindView(R.id.ll_pending_absen)
    LinearLayout llPendingAbsen;
    @BindView(R.id.iv_background)
    ImageView ivBackground;
    @BindView(R.id.ll_accept_absen)
    LinearLayout llAcceptAbsen;
    @BindView(R.id.ll_reject_absen)
    LinearLayout llRejectAbsen;
    @BindView(R.id.ll_head_kategori)
    LinearLayout llHeadKategori;
    @BindView(R.id.tv_status)
    MyTextView tvStatus;
    @BindView(R.id.ll_belum_ada_absen)
    LinearLayout llBelumAdaAbsen;
    @BindView(R.id.rv_absensi_keluar)
    ShimmerRecyclerView rvAbsensiKeluar;
    Unbinder unbinder;

    private List<AbsenModel> list;
    private List<String> keyAbsen;
    private DatabaseReference databaseReference;
    private AdapterListAbsensi adapterListAbsensi;

    public TabAdminAbsenKeluarFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_absensi_keluar, container, false);
        unbinder = ButterKnife.bind(this, view);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        semuaPendingAbsen();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //TODO :: Fetch Data
    private void semuaAcceptAbsen() {
        rvAbsensiKeluar.showShimmerAdapter();
        list = new ArrayList<>();
        keyAbsen = new ArrayList<>();
        llBelumAdaAbsen.setVisibility(View.GONE);
        databaseReference.child("absen")
                .orderByChild("jenisAbsen")
                .equalTo("Keluar")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            try {
                                list.clear();
                                keyAbsen.clear();
                                AbsenModel model = null;
                                for (DataSnapshot data : dataSnapshot.getChildren()) {

                                    if (data.child("status").getValue(String.class).equalsIgnoreCase("accept")) {
                                        String key = data.getKey();
                                        model = data.getValue(AbsenModel.class);
                                        list.add(model);
                                        keyAbsen.add(key);
                                    }

                                }
                                adapterListAbsensi = new AdapterListAbsensi(getActivity(), list, keyAbsen);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                rvAbsensiKeluar.setLayoutManager(layoutManager);
                                rvAbsensiKeluar.setAdapter(adapterListAbsensi);
                            } catch (NullPointerException e) {
                                new Bantuan(getContext()).swal_error(e.getMessage());
                            }
                        } else {
                            rvAbsensiKeluar.setVisibility(View.GONE);
                            llBelumAdaAbsen.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        new Bantuan(getActivity()).swal_error(databaseError.getMessage());
                    }
                });

    }

    private void semuaPendingAbsen() {
        rvAbsensiKeluar.showShimmerAdapter();
        list = new ArrayList<>();
        keyAbsen = new ArrayList<>();
        llBelumAdaAbsen.setVisibility(View.GONE);
        databaseReference.child("absen")
                .orderByChild("jenisAbsen")
                .equalTo("Keluar")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            try {
                                list.clear();
                                keyAbsen.clear();
                                AbsenModel model = null;
                                for (DataSnapshot data : dataSnapshot.getChildren()) {

                                    if (data.child("status").getValue(String.class).equalsIgnoreCase("pending")) {
                                        String key = data.getKey();
                                        model = data.getValue(AbsenModel.class);
                                        list.add(model);
                                        keyAbsen.add(key);
                                    }

                                }
                                adapterListAbsensi = new AdapterListAbsensi(getActivity(), list, keyAbsen);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                rvAbsensiKeluar.setLayoutManager(layoutManager);
                                rvAbsensiKeluar.setAdapter(adapterListAbsensi);
                            } catch (NullPointerException e) {
                                new Bantuan(getContext()).swal_error(e.getMessage());
                            }
                        } else {
                            rvAbsensiKeluar.setVisibility(View.GONE);
                            llBelumAdaAbsen.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        new Bantuan(getActivity()).swal_error(databaseError.getMessage());
                    }
                });

    }

    private void semuaRejectAbsen() {
        rvAbsensiKeluar.showShimmerAdapter();
        list = new ArrayList<>();
        keyAbsen = new ArrayList<>();
        llBelumAdaAbsen.setVisibility(View.GONE);
        databaseReference.child("absen")
                .orderByChild("jenisAbsen")
                .equalTo("Keluar")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            try {
                                list.clear();
                                keyAbsen.clear();
                                AbsenModel model = null;
                                for (DataSnapshot data : dataSnapshot.getChildren()) {

                                    if (data.child("status").getValue(String.class).equalsIgnoreCase("accept")) {
                                        String key = data.getKey();
                                        model = data.getValue(AbsenModel.class);
                                        list.add(model);
                                        keyAbsen.add(key);
                                    }

                                }
                                adapterListAbsensi = new AdapterListAbsensi(getActivity(), list, keyAbsen);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                rvAbsensiKeluar.setLayoutManager(layoutManager);
                                rvAbsensiKeluar.setAdapter(adapterListAbsensi);
                            } catch (NullPointerException e) {
                                new Bantuan(getContext()).swal_error(e.getMessage());
                            }
                        } else {
                            rvAbsensiKeluar.setVisibility(View.GONE);
                            llBelumAdaAbsen.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        new Bantuan(getActivity()).swal_error(databaseError.getMessage());
                    }
                });

    }
    //TODO :: Bind(OnCLick dll)

    @OnClick(R.id.ll_accept_absen)
    public void accept_sort() {
        semuaAcceptAbsen();
        tvStatus.setText("Accept");
    }

    @OnClick(R.id.ll_pending_absen)
    public void pending_sort() {
        semuaPendingAbsen();
        tvStatus.setText("Pending");
    }

    @OnClick(R.id.ll_reject_absen)
    public void reject_sort() {
        semuaRejectAbsen();
        tvStatus.setText("Reject");
    }
    //TODO :: LifeCycle
}
