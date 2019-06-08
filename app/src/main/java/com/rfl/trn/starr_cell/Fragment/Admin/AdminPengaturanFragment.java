package com.rfl.trn.starr_cell.Fragment.Admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rfl.trn.starr_cell.Activity.PengaturanUmumActivity;
import com.rfl.trn.starr_cell.Adapter.AdapterListPengaturanKonter;
import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.Helper.Bantuan;
import com.rfl.trn.starr_cell.Model.KonterModel;
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
public class AdminPengaturanFragment extends Fragment {


    @BindView(R.id.iv_karyawan)
    ImageView ivKaryawan;
    @BindView(R.id.tv_namaKonter)
    MyTextView tvNamaKonter;
    @BindView(R.id.tv_detailKaryawan)
    MyTextView tvDetailKaryawan;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.iv_arrowUmum)
    ImageView ivArrowUmum;
    @BindView(R.id.rl_pengaturanUmum)
    RelativeLayout rlPengaturanUmum;
    @BindView(R.id.iv_arrowKonter)
    ImageView ivArrowKonter;
    @BindView(R.id.rl_pengaturanKonter)
    RelativeLayout rlPengaturanKonter;
    @BindView(R.id.rv_konterPengaturan)
    ShimmerRecyclerView rvKonterPengaturan;
    @BindView(R.id.rl_rvKonter)
    RelativeLayout rlRvKonter;

    private Unbinder unbinder;
    private boolean arrowIsUp = true;
    private DatabaseReference databaseReference;
    private List<KonterModel> list;
    private AdapterListPengaturanKonter adapterListPengaturanKonter;

    public AdminPengaturanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_pengaturan, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        rlRvKonter.setVisibility(View.GONE);
        if (arrowIsUp){
            ivArrowKonter.setImageResource(R.drawable.ic_keyboard_arrow_up);
        }else {
            ivArrowKonter.setImageResource(R.drawable.ic_keyboard_arrow_down);
        }

    }

    //TODO :: FetchData
    public void getAllKonter(){
        rvKonterPengaturan.showShimmerAdapter();
        list = new ArrayList<>();
           databaseReference.child("konter")
                   .addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           KonterModel model;
                           list.clear();
                            if (dataSnapshot.exists()){
                                for (DataSnapshot data : dataSnapshot.getChildren()){
                                    try {
                                        model = data.getValue(KonterModel.class);
                                        model.setKey(data.getKey());
                                        list.add(model);
                                    }catch (NullPointerException e){
                                        new Bantuan(getActivity()).swal_error(e.getMessage());
                                    }

                                }
                            }else {

                            }
                            adapterListPengaturanKonter = new AdapterListPengaturanKonter(getActivity(),list);
                           RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                           rvKonterPengaturan.setLayoutManager(layoutManager);
                           rvKonterPengaturan.setAdapter(adapterListPengaturanKonter);

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {
                            new Bantuan(getActivity()).swal_error(databaseError.getMessage());
                       }
                   });
    }

    //TODO :: Bind
    @OnClick(R.id.rl_pengaturanUmum)
    public void gotoPengaturanUmum(){
        Aksi(1);
    }
    @OnClick(R.id.rl_pengaturanKonter)
    public void konterKlik(){
        if (arrowIsUp){
            ivArrowKonter.setImageResource(R.drawable.ic_keyboard_arrow_up);
            arrowIsUp = false;
        }else {
            ivArrowKonter.setImageResource(R.drawable.ic_keyboard_arrow_down);
            arrowIsUp = true;
        }
        Aksi(2);
    }

    public void Aksi(int posoisi){
        switch (posoisi){
            case 1:
                startActivity(new Intent(getActivity(), PengaturanUmumActivity.class));
                break;
            case 2:
                if (arrowIsUp){
                    rlRvKonter.setVisibility(View.GONE);
                }else {
                    rlRvKonter.setVisibility(View.VISIBLE);
                    getAllKonter();
                }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
