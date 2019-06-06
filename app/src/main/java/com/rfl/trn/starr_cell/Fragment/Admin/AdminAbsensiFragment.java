package com.rfl.trn.starr_cell.Fragment.Admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rfl.trn.starr_cell.Adapter.AdapterListAbsensi;
import com.rfl.trn.starr_cell.Adapter.AdapterTabAbsen;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Model.AbsenModel;
import com.rfl.trn.starr_cell.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AdminAbsensiFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.ll_belum_ada_absen)
    LinearLayout llBelumAdaAbsen;
    @BindView(R.id.tab_absen)
    TabLayout tabAbsen;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private AdapterTabAbsen adapterTabAbsen;
    private DatabaseReference databaseReference;
    private List<AbsenModel> list = new ArrayList<>();
    private List<String> keyAbsen = new ArrayList<>();
    private AdapterListAbsensi adapterListAbsensi;


    public AdminAbsensiFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_absensi, container, false);
        unbinder = ButterKnife.bind(this, view);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        adapterTabAbsen = new AdapterTabAbsen(getFragmentManager());
        adapterTabAbsen.addFragment(new TabAdminAbsenMasukFragment(),"Absen Masuk");
        adapterTabAbsen.addFragment(new TabAdminAbsenKeluarFragment(),"Absen Keluar");

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // semuaAbsen();
        viewpager.setAdapter(adapterTabAbsen);
        tabAbsen.setupWithViewPager(viewpager);

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
