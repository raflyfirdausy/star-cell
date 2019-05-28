package com.rfl.trn.starr_cell.Fragment.Admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rfl.trn.starr_cell.Activity.TambahBarangActivity;
import com.rfl.trn.starr_cell.Adapter.AdapterListBarang;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Model.BarangModel;
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
public class AdminBarangFragment extends Fragment {

    @BindView(R.id.iv_karyawan)
    ImageView ivKaryawan;
    @BindView(R.id.tv_namaKonter)
    MyTextView tvNamaKonter;
    @BindView(R.id.tv_detailKaryawan)
    MyTextView tvDetailKaryawan;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.bg_noData)
    LinearLayout bgNoData;
    @BindView(R.id.ll_belum_da_konter)
    LinearLayout llBelumDaKonter;
    @BindView(R.id.rv_barang)
    RecyclerView rvBarang;
    @BindView(R.id.fab_tambahBarang)
    FloatingActionButton fabTambahBarang;
    Unbinder unbinder;

    private List<BarangModel> list;
    private AdapterListBarang adapterListBarang;
    private DatabaseReference databaseReference;

    public AdminBarangFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_transaksi, container, false);
        unbinder = ButterKnife.bind(this, view);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        getBarang();
        return view;
    }

    //TODO :: Fetch Data
    private void getBarang() {
        databaseReference.child("barang")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list = new ArrayList<>();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                BarangModel model = new BarangModel();

                                model = data.getValue(BarangModel.class);
                                list.add(model);
                            }

                            adapterListBarang = new AdapterListBarang(getActivity(), list);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            rvBarang.setLayoutManager(layoutManager);
                            rvBarang.setAdapter(adapterListBarang);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    //TODO :: Bind(OnCLick dll)
    @OnClick(R.id.fab_tambahBarang)
    void tambahBarang() {
        startActivity(new Intent(getActivity(), TambahBarangActivity.class));
    }

    //TODO :: LifeCycle
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
